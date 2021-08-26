package com.commerce.web.util;

import java.util.regex.Pattern;

public class RegularUtils {
	
	private RegularUtils() {
		// Do nothing
	}
	
	public static final String DIGITAL_AND_ENGLISH_LINE = "[\\w_-]*";

	public static boolean isMatched(String pattern, String reg) {
		return Pattern.compile(reg).matcher(pattern).matches();
	}
}
