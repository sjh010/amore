package com.apgroup.pms.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 에러 response
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	@ApiModelProperty(name = "에러코드", example = "500")
	private String resultCode;
	
	@ApiModelProperty(name = "에러메시지", example = "시스템 에러")
	private String resultMessage;
	
}
