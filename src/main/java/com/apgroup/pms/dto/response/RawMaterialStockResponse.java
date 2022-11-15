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

	@ApiModelProperty(name = "원료 아이디", example = "A")
	private String id;
	
	@ApiModelProperty(name = "재고량", example = "200")
	private Integer stock;
	
	@ApiModelProperty(name = "에러메세지", example = "해당 원료가 존재하지 않습니다.")
	private String error;

}
