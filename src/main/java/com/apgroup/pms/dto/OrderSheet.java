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

	private String orderNumber;	// 주문번호
	
	private String order;		// 주문코드
	
	private String effect1;		// 효능1
	
	private int rate1;			// 비율1
	
	private String effect2;		// 효능2
	
	private int rate2;			// 비율2
	
}