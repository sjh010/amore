package com.apgroup.pms.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * custom validation
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderCodeValidator.class)
public @interface OrderCode {
	String message() default "주문코드";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

}
