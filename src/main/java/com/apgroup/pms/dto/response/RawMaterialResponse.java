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

	@ApiModelProperty(example = "원료 아이디")
	private String id;
	
	@ApiModelProperty(example = "잔여량")
	private Integer remainsAmount;
	
	@ApiModelProperty(example = "재고량")
	private Integer stockAmount;
	
	@ApiModelProperty(example = "에러메세지")
	private String error;
}
