package com.apgroup.pms.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 재고 request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialStockRequest {

	@ApiModelProperty(example = "원료 아이디")
	@JsonProperty("id")
	@NotBlank(message = "원료아이디 없음")
	private String id;
	
	@ApiModelProperty(example = "재고량")
	@JsonProperty("amount")
	@Min(value = 1, message = "재고량 없음")
	private int stock;

}
