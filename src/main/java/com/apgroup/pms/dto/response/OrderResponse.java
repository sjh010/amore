package com.apgroup.pms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 주문 response
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class OrderResponse {

	@ApiModelProperty(name = "주문번호", example = "20220801001")
	@JsonProperty("order_number")
	private String orderNumber;
	
	@ApiModelProperty(name = "주문코드", example = "A7B3")
	@JsonProperty("order")
	private String orderCode;

	@ApiModelProperty(name = "주문일자", example = "220801")
	@JsonProperty("order_date")
	private String orderDate;
	
	@ApiModelProperty(name = "발송예정일", example = "20220801")
	@JsonProperty("send_date")
	private String sendDate;
	
	@ApiModelProperty(name = "주문상태", example = "주문접수")
	@JsonProperty("order_status")
	private String orderStatus; 
	
	@ApiModelProperty(name = "에러메세지", example = "효능단종")
	@JsonProperty("error")
	private String error;	

}
