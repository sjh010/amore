package com.apgroup.pms.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 주문 entity
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

	private String orderNumber;
	
	private String orderCode;
	
	private String orderDate;
	
	private String sendDate;
	
	private String orderStatus;
	
	private String error;
	
}
