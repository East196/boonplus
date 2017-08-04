package com.github.east196.boonplus.underscore

import java.util.Collection
import io.advantageous.boon.core.Conversions
import io.advantageous.boon.core.Typ
import io.advantageous.boon.primitive.Arry

/** 
 * underscore.java
 * @author east196
 */
class Underscore {
	def static void noop() {
	}

	def static long now() {
		return System.currentTimeMillis()
	}

	def static boolean isEmpty(Object object) {
		return Conversions.len(object) === 0
	}

	def static boolean isArray(Object object) {
		return Typ.isArray(object)
	}

	def static <T> T[] toArray(Collection<T> objects) {
		return Arry.array(objects)
	}
}
