package com.apgroup.pms.type;

/**
 * 주문상태코드 enum
 */
public enum OrderStatusCode {

	ORDER_RECEPTION					(1, "01", "주문접수"),
	PRODUCT_IN_PRODUCTION			(2, "02", "제품생산중"),
	PRODUCT_PRODUCTION_COMPLETED	(3, "03", "제품생산완료"),
	READY_TO_SHIP					(4, "04", "발송준비중"),
	SHIPMENT_COMPLETED				(5, "05", "발송완료"),
	
	ORDER_CANCEL					(99, "99", "주문취소")
	;
	
	private int order;
	private String code;
	private String description;
		
	public int getOrder() {
		return order;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	private OrderStatusCode(int order, String code, String description) {
		this.order = order;
		this.code = code;
		this.description = description;
	}
	
	public static OrderStatusCode getByCode(String code) {
		for (OrderStatusCode statusCode : OrderStatusCode.values()) {
			if (statusCode.code.equals(code)) {
				return statusCode;
			}
		}
		
		return null;
	}
	
	public static boolean isCancellable(int order) {
		return PRODUCT_IN_PRODUCTION.order > order;
	}
	
}
