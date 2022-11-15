package com.apgroup.pms.dto.request;

import javax.validation.constraints.NotBlank;

import com.apgroup.pms.validation.OrderCode;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 주문 request
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequest {

	@ApiModelProperty(name = "주문번호", example = "20220801001")
	@JsonProperty("order_number")
	@NotBlank(message = "주문번호없음")
	private String orderNumber;
	
	@ApiModelProperty(name = "주문코드", example = "A7B3")
	@JsonProperty("order")
	@NotBlank(message = "주문코드없음")
	@OrderCode(message = "잘못된 주문코드")
	private String order;
	
	@ApiModelProperty(name = "주문일자", example = "220801")
	@JsonProperty("order_date")
	@NotBlank(message = "주문일자없음(yymmdd)")
	private String orderDate;
}
