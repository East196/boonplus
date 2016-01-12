package cn.fireworks.boonplus.underscore;

import java.util.Collection;

import io.advantageous.boon.core.Conversions;
import io.advantageous.boon.core.Typ;
import io.advantageous.boon.primitive.Arry;

/**
 * underscore.java
 * 
 * @author tung
 */
public class _ {

	public static void noop() {

	}

	public static long now() {
		return System.currentTimeMillis();
	}

	public static boolean isEmpty(Object object) {
		return Conversions.len(object) == 0;
	}

	public static boolean isArray(Object object) {
		return Typ.isArray(object);
	}

	public static <T> T[] toArray(Collection<T> objects) {
		return Arry.array(objects);
	}

}
