package com.apgroup.pms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 원료 response
 */
@Builder
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class RawMaterialResponse {

	@ApiModelProperty(name = "원료 아이디", example = "A")
	private String id;
	
	@ApiModelProperty(name = "잔여량", example = "100")
	private Integer remainsAmount;
	
	@ApiModelProperty(name = "재고량", example = "200")
	private Integer stockAmount;
	
	@ApiModelProperty(name = "에러메세지", example = "해당 원료가 이미 존재합니다.")
	private String error;
}
