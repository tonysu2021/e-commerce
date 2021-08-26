package com.commerce.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.commerce.web.util.RegularUtils;

public class IdValidator implements ConstraintValidator<IdFormat, Object> {
	@Override
	public void initialize(IdFormat context) {
		// Do nothing because of X and Y.
	}

	@Override
	public boolean isValid(Object paramObj, ConstraintValidatorContext cxt) {
		if (paramObj instanceof String) {
			if (StringUtils.isBlank((String) paramObj)) {
				return false;
			}
			return RegularUtils.isMatched((String) paramObj, RegularUtils.DIGITAL_AND_ENGLISH_LINE);
		}
		return false;
	}
}
