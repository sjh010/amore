package com.apgroup.pms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class RawMaterialStockResponse {

	@ApiModelProperty(example = "원료 아이디")
	private String id;
	
	@ApiModelProperty(example = "재고량")
	private Integer stock;
	
	@ApiModelProperty(example = "에러메세지")
	private String error;

}
