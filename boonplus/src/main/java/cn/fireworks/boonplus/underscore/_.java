package cn.fireworks.boonplus.underscore;

import java.lang.reflect.Array;
import java.util.Collection;

import org.boon.Boon;
import org.boon.primitive.Arry;

/**
 * underscore.java
 * 
 * @author tung
 */
public class _ {

	/**
	 * do nothing
	 */
	public static void noop() {

	}

	/**
	 * now mills
	 */
	public static long now() {
		return System.currentTimeMillis();
	}

	public static boolean isEmpty(Object object) {
		return Boon.isEmpty(object);
	}

	public static boolean isArray(Object object) {
		return Boon.isArray(object);
	}

	public static <T> T[] toArray(Collection<T> objects) {
		return Arry.array(objects);
	}

}
