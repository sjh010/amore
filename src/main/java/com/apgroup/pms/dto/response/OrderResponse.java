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

	@ApiModelProperty(example = "주문번호")
	@JsonProperty("order_number")
	private String order_number;
	
	@ApiModelProperty(example = "주문코드")
	@JsonProperty("order")
	private String order;
	
	@ApiModelProperty(example = "발송예정일")
	@JsonProperty("send_date")
	private String send_date;
	
	@ApiModelProperty(example = "에러메세지")
	@JsonProperty("error")
	private String error;	

}
