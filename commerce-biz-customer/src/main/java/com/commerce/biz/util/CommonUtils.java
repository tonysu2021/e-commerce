package com.commerce.biz.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.commerce.biz.entity.Customer;
import com.commerce.web.domain.CustomerDomain;

public class CommonUtils {

	private CommonUtils() {
		// Do nothing
	}

	public static CustomerDomain convert(Customer entity) {
		CustomerDomain domain = new CustomerDomain();
		domain.setCustomerId(entity.getCustomerId());
		domain.setName(entity.getName());
		domain.setEnabled(entity.getEnabled());
		return domain;
	}
	
	/**
	 * 處理字串樣本format <br>
	 * <br>
	 * formate格式 My name is ${1}. ${0} ${1}." <br>
	 * Map<String, Object> values = new HashMap<String, Object>();
	 * values.put("0", "James");
	 * values.put("1", "Bond");
	 * 
	 * */
	public static String format(String format, Map<String, Object> values) {
	    StringBuilder formatter = new StringBuilder(format);
	    List<Object> valueList = new ArrayList<>();

	    Matcher matcher = Pattern.compile("\\$\\{(\\w+)}").matcher(format);

	    while (matcher.find()) {
	        String key = matcher.group(1);

	        String formatKey = String.format("${%s}", key);
	        int index = formatter.indexOf(formatKey);

	        if (index != -1) {
	            formatter.replace(index, index + formatKey.length(), "%s");
	            valueList.add(values.get(key));
	        }
	    }
	    return String.format(formatter.toString(), valueList.toArray());
	}
}
