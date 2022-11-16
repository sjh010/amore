package com.apgroup.pms.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.apgroup.pms.dto.response.ErrorResponse;
import com.apgroup.pms.error.exception.EntityNotExistException;

import lombok.extern.slf4j.Slf4j;

/**
 * exception handler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public ErrorResponse handleMethodArgumentNotValidException(BindException e) {
		BindingResult bindingResult = e.getBindingResult();

		StringBuilder sb = new StringBuilder();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			sb.append("field: [").append(fieldError.getField()).append("]");
			sb.append(" input value: [").append(fieldError.getRejectedValue()).append("]");
		}

		log.error("Validation Exception : {}", sb);
		return ErrorResponse.builder()
				.resultCode("400")
				.resultMessage(sb.toString())
				.build();
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotExistException.class)
	public ErrorResponse handleEntityNotExistException(EntityNotExistException e) {
		return ErrorResponse.builder()
				.resultCode("404")
				.resultMessage(e.getMessage())
				.build();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected ErrorResponse defaultException(HttpServletRequest request, Exception e) {
		log.error(e.getMessage(), e);
		
		return ErrorResponse.builder()
				.resultCode("500")
				.resultMessage("서버 오류")
				.build();
	}
}
