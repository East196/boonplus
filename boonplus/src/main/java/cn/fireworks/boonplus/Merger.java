package cn.fireworks.boonplus;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.boon.Sets;
import org.boon.core.reflection.BeanUtils;
import org.boon.core.reflection.Reflection;
import org.boon.core.reflection.fields.FieldAccess;

public class Merger {
	
	public static <T> T mergeNotNullToFirst(Class<T> destKlass, Object... srcs) {
		T dest = Reflection.newInstance(destKlass);
		return mergeNotNullToFirstUnSafe(dest, srcs);
	}

	public static <T> T mergeNotNullToFirst(T template, Object... srcs) {
		T dest = BeanUtils.copy(template);
		return mergeNotNullToFirstUnSafe(dest, srcs);
	}

	private static <T> T mergeNotNullToFirstUnSafe(T dest, Object... srcs) {
		for (Object src : srcs) {
			Set<String> ignores = Sets.set();
			Map<String, FieldAccess> fieldMap = BeanUtils.getFieldsFromObject(src);
			for (Entry<String, FieldAccess> entry : fieldMap.entrySet()) {
				System.out.println(entry);
				if (entry.getValue().getObject(src) == null) {
					ignores.add(entry.getKey());
				}
			}
			BeanUtils.copyProperties(src, dest, ignores);
		}
		return dest;
	}

	public static <T> T mergeToFirst(T template, Object... srcs) {
		T dest = BeanUtils.copy(template);
		for (Object src : srcs) {
			BeanUtils.copyProperties(src, dest);
		}
		return dest;
	}

}
