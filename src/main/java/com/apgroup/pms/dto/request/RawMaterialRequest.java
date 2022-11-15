package com.apgroup.pms.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 원료 request
 */
@Getter
@Setter
@ToString
public class RawMaterialRequest {

	@ApiModelProperty(name = "원료 아이디", example = "E")
	@JsonProperty(value = "effect", required = true)
	@NotBlank
	private String id;
	
	@ApiModelProperty(name = "재고량", example = "200")
	@JsonProperty(value = "stock", required = false)
	@NotNull
	private Integer amount;
}
