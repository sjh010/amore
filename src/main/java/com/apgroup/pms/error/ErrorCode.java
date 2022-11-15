package com.apgroup.pms.error;

/**
 * 에러 코드 및 메시지 정의
 */
public enum ErrorCode {

	
	
	ORDER_NOT_EXIST					("801", "해당 주문이 존재하지 않습니다."),
	ORDER_IS_EXIST					("802", "해당 주문번호가 존재합니다."),
	ORDER_CANCEL					("803", "주문취소"),
	
	UNABLE_TO_CANCEL_ORDER			("811", "제품 생산이 시작 된 이후에는 주문을 취소할 수 없습니다."),
	FAIL_TO_CANCEL_ORDER			("812", "주문 취소를 실패했습니다."),
	
	RAW_MATERIAL_IS_EXIST			("821", "해당 원료가 이미 존재합니다."),
	RAW_MATERIAL_NOT_ADDED			("822", "생산 설비의 원료는 최대 10가지입니다."),
	RAW_MATERIAL_DISCONTINUED		("823", "효능 단종"),
	RAW_MATERIAL_NOT_EXIST			("824", "해당 원료가 존재하지 않습니다."),
	RAW_MATERIAL_SHORT_ON_STOCK		("825", "원료 재고가 부족합니다."),					
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
