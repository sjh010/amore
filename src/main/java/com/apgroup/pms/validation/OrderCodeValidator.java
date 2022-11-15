package com.apgroup.pms.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.ObjectUtils;

/**
 * custom validator
 */
public class OrderCodeValidator implements ConstraintValidator<OrderCode, String> {

	@Override
	public boolean isValid(String order, ConstraintValidatorContext context) {
		if (ObjectUtils.isEmpty(order)) {
			return false;
		} else if (order.length() == 3 && order.substring(0, 1).matches("^[a-zA-Z]$") && order.indexOf("10") == 1) {
			// 한가지 효능에 비율 10 인 경우
			return true;
		} else if (order.length() == 4) {
			String effect1 	= order.substring(0, 1); 
			String rate1 	= order.substring(1, 2); 
			String effect2 	= order.substring(2, 3); 
			String rate2 	= order.substring(3, 4);
			
			if (effect1.matches("^[a-zA-Z]$") && effect2.matches("^[a-zA-Z]$") && !effect1.equalsIgnoreCase(effect2)) {
				if (rate1.matches("^[0-9]$") && rate2.matches("^[0-9]$")) {
					// 두가지 효능에 비율의 합이 10인 경우
					int rate1_int = Integer.valueOf(rate1);
					int rate2_int = Integer.valueOf(rate2);
					
					if (rate1_int >= rate2_int && (rate1_int + rate2_int) == 10) {
						// 비율이 높은 효능이 앞에 표시되는 경우
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false; 
		}
	}

}
