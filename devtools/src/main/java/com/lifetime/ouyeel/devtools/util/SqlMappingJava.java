package com.lifetime.ouyeel.devtools.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author lifetime
 *
 */
public class SqlMappingJava {
	private static Map<String, String> Mappings = new HashMap<>();

	static {
		Mappings.put("VARCHAR", "String");
		Mappings.put("CHAR", "String");
		Mappings.put("INTEGER", "Integer");
		Mappings.put("int", "Integer");
		Mappings.put("datetime", "Date");
		Mappings.put("TINYINT", "Integer");
		Mappings.put("DECIMAL", "BigDecimal");
		Mappings.put("DATE", "Date");
		Mappings.put("DATETIME", "Date");
		Mappings.put("TIMESTAMP", "Date");
	}

	public static String get(String type) {
		for (Iterator<Entry<String, String>> it = Mappings.entrySet().iterator(); it.hasNext();) {
			Entry<String, String> entry = it.next();
			if (entry.getKey().equalsIgnoreCase(type)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
}
