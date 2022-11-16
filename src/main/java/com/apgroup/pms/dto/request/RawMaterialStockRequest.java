package com.apgroup.pms.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

	@ApiModelProperty(name = "원료 아이디", example = "A")
	@JsonProperty(value = "id", required = true)
	@NotBlank(message = "원료아이디 없음")
	private String id;
	
	@ApiModelProperty(name = "재고량", example = "200")
	@JsonProperty(value = "stock", required = true)
	@NotNull
	private Integer stock;

}
