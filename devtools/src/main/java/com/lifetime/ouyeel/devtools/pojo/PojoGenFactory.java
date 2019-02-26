package com.lifetime.ouyeel.devtools.pojo;

import com.lifetime.ouyeel.devtools.pojo.impl.PojoGenImpl;

/**
 * @author lifetime
 *
 */
public class PojoGenFactory {
	private static IPojoGen pojoGen;

	public static IPojoGen getGen(String type) {
		if (pojoGen == null) {
			pojoGen = new PojoGenImpl();
		}
		return pojoGen;
	}
	
	public static IPojoGen getGen() {
		return getGen(null);
	}
}
