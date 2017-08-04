package com.github.east196.boonplus

import java.util.Map
import java.util.Map.Entry
import java.util.Set
import io.advantageous.boon.core.Sets
import io.advantageous.boon.core.reflection.BeanUtils
import io.advantageous.boon.core.reflection.Reflection
import io.advantageous.boon.core.reflection.fields.FieldAccess

class Merger {
	def static <T> T mergeNotNullToFirst(Class<T> destKlass, Object... srcs) {
		var T dest = Reflection::newInstance(destKlass)
		return Merger.mergeNotNullToFirstUnSafe(dest, srcs)
	}

	def static <T> T mergeNotNullToFirst(T template, Object... srcs) {
		var T dest = BeanUtils::copy(template)
		return Merger.mergeNotNullToFirstUnSafe(dest, srcs)
	}

	def private static <T> T mergeNotNullToFirstUnSafe(T dest, Object... srcs) {
		for (Object src : srcs) {
			var Set<String> ignores = Sets::set()
			var Map<String, FieldAccess> fieldMap = BeanUtils::getFieldsFromObject(src)
			for (Entry<String, FieldAccess> entry : fieldMap.entrySet()) {
				System::out.println(entry)
				if (entry.getValue().getObject(src) === null) {
					ignores.add(entry.getKey())
				}
			}
			BeanUtils::copyProperties(src, dest, ignores)
		}
		return dest
	}

	def static <T> T mergeToFirst(T template, Object... srcs) {
		var T dest = BeanUtils::copy(template)
		for (Object src : srcs) {
			BeanUtils::copyProperties(src, dest)
		}
		return dest
	}
}
