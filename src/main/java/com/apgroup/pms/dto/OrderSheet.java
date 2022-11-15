package com.apgroup.pms.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 주문서 - 주문코드로부터 효능과 비율을 파싱 
 */
@Builder
@Setter
@Getter
@ToString
public class OrderSheet {

	private String orderNumber;
	
	private String order;
	
	private String effect1;
	
	private int rate1;
	
	private String effect2;
	
	private int rate2;
	
}