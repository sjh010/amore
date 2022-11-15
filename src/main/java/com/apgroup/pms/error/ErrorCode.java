package com.apgroup.pms.error;

public enum ErrorCode {

	ORDER_CANCEL					("800", "주문취소"),
	ORDER_NOT_EXIST					("801", "해당 주문이 존재하지 않습니다."),
	ORDER_IS_EXIST					("802", "해당 주문번호가 존재합니다."),
	UNABLE_TO_CANCEL_ORDER			("811", "제품 생산이 시작 된 이후에는 주문을 취소할 수 없습니다."),
	FAIL_TO_CANCEL_ORDER			("812", "주문 취소를 실패했습니다."),
	
	RAW_MATERIAL_IS_EXIST			("821", "해당 효능이 이미 존재합니다."),
	RAW_MATERIAL_DISCONTINUED		("822", "효능 단종"),
	RAW_MATERIAL_NOT_EXIST			("823", "해당 효능이 존재하지 않습니다."),
	RAW_MATERIAL_SHORT_ON_STOCK		("824", "원료 재고가 부족합니다."),					
	;
	
	private String code;
	private String message;
		
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	private ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static ErrorCode getByCode(String code) {
		for (ErrorCode statusCode : ErrorCode.values()) {
			if (statusCode.code.equals(code)) {
				return statusCode;
			}
		}
		
		return null;
	}
	
}
