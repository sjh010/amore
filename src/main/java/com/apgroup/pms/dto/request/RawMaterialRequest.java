package com.apgroup.pms.dto.request;

import javax.validation.constraints.NotBlank;

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

	@ApiModelProperty(example = "원료 아이디")
	@JsonProperty(value = "effect", required = true)
	@NotBlank
	private String id;
	
	@ApiModelProperty(example = "재고량")
	@JsonProperty(value = "stock", required = false)
	private int amount;
}
