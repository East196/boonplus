package cn.fireworks.boonplus.mapper;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import io.advantageous.boon.core.Conversions;
import io.advantageous.boon.core.Exceptions;
import io.advantageous.boon.core.Str;
import io.advantageous.boon.core.Typ;
import io.advantageous.boon.core.TypeType;
import io.advantageous.boon.core.reflection.BeanUtils;
import io.advantageous.boon.core.reflection.Reflection;
import io.advantageous.boon.core.reflection.fields.FieldAccess;

public class Mapper {

	public static <T> T map(Object src, Class<T> destKlass) {
		T dest = Reflection.newInstance(destKlass);
		map(src, dest);
		return dest;
	}

	public static void map(Object src, Object dst) {
		final Class<?> srcClass = src.getClass();
		Map<String, FieldAccess> srcFields = Reflection.getAllAccessorFields(srcClass);
		final Class<?> dstClass = dst.getClass();
		Map<String, FieldAccess> dstFields = Reflection.getAllAccessorFields(dstClass);

		for (FieldAccess srcField : srcFields.values()) {
			FieldAccess dstField = dstFields.get(srcField.name());
			try {
				if (dstField == null) {
					if (srcField.hasAnnotation("Mapping")) {
						Map<String, Object> aliasD = srcField.getAnnotationData("Mapping");
						String alias = (String) aliasD.get("value");
						dstField = dstFields.get(alias);
					}
				}
				if (dstField == null) {
					for (FieldAccess option : dstFields.values()) {
						if (option.hasAnnotation("Mapping")) {
							Map<String, Object> aliasD = option.getAnnotationData("Mapping");
							String alias = (String) aliasD.get("value");
							if (srcField.name().equals(alias)) {
								dstField = option;
								break;
							}
						}
					}
				}
				copySrcFieldToDestField(src, dst, dstField, srcField, null);
			} catch (Exception ex) {
				Exceptions.handle(
						Str.sputs("copying field", srcField.name(), srcClass, " to ", dstField.name(), dstClass), ex);
			}
		}
	}

	private static void fieldByFieldCopy(Object src, Object dst, Set<String> ignore) {

		final Class<?> srcClass = src.getClass();
		Map<String, FieldAccess> srcFields = Reflection.getAllAccessorFields(srcClass);

		final Class<?> dstClass = dst.getClass();
		Map<String, FieldAccess> dstFields = Reflection.getAllAccessorFields(dstClass);

		for (FieldAccess srcField : srcFields.values()) {

			if (ignore.contains(srcField.name())) {
				continue;
			}

			FieldAccess dstField = dstFields.get(srcField.name());
			try {

				copySrcFieldToDestField(src, dst, dstField, srcField, ignore);

			} catch (Exception ex) {
				Exceptions.handle(
						Str.sputs("copying field", srcField.name(), srcClass, " to ", dstField.name(), dstClass), ex);
			}
		}
	}

	private static void fieldByFieldCopy(Object src, Object dst) {

		final Class<?> srcClass = src.getClass();
		Map<String, FieldAccess> srcFields = Reflection.getAllAccessorFields(srcClass);

		final Class<?> dstClass = dst.getClass();
		Map<String, FieldAccess> dstFields = Reflection.getAllAccessorFields(dstClass);

		for (FieldAccess srcField : srcFields.values()) {

			FieldAccess dstField = dstFields.get(srcField.name());
			try {

				copySrcFieldToDestField(src, dst, dstField, srcField, null);

			} catch (Exception ex) {
				Exceptions.handle(
						Str.sputs("copying field", srcField.name(), srcClass, " to ", dstField.name(), dstClass), ex);
			}
		}
	}

	private static void copySrcFieldToDestField(Object src, Object dst, FieldAccess dstField, FieldAccess srcField,
			Set<String> ignore) {
		if (srcField.isStatic()) {
			return;
		}

		if (dstField == null) {
			return;
		}

		/* If its primitive handle it. */
		if (srcField.isPrimitive()) {
			dstField.setValue(dst, srcField.getValue(src));
			return;
		}

		Object srcValue = srcField.getObject(src);

		/* if value is null then handle it unless it is primitive. */
		if (srcValue == null) {
			if (!dstField.isPrimitive()) {
				dstField.setObject(dst, null);
			}
			return;
		}

		/* Basic type. */
		if (Typ.isBasicType(srcField.type())) {
			/* Handle non primitive copy. */
			Object value = srcField.getObject(src);
			dstField.setValue(dst, value);
			return;
		}

		/* Types match and not a collection so just copy. */
		if ((!(srcValue instanceof Collection) && (dstField.type() == srcValue.getClass()))
				|| Typ.isSuperType(dstField.type(), srcValue.getClass())) {
			dstField.setObject(dst, BeanUtils.copy(srcField.getObject(src)));
			return;
		}

		/* Collection field copy. */
		if ((srcValue instanceof Collection) && (dstField.getComponentClass() != null)
				&& Typ.isCollection(dstField.type())) {
			handleCollectionFieldCopy(dst, dstField, (Collection<?>) srcValue);
			return;

		}

		/* Non identical object copy. */
		if ((dstField.typeEnum() == TypeType.ABSTRACT) || (dstField.typeEnum() == TypeType.INTERFACE)) {
			// no op
		} else {
			Object newInstance = Reflection.newInstance(dstField.type());
			if (ignore == null) {
				fieldByFieldCopy(srcField.getObject(src), newInstance);
			} else {
				fieldByFieldCopy(srcField.getObject(src), newInstance, ignore);
			}
			dstField.setObject(dst, newInstance);
		}
	}

	private static void handleCollectionFieldCopy(Object dst, FieldAccess dstField, Collection<?> srcValue) {
		if (dstField.getComponentClass() != Typ.string) {
			Collection<Object> dstCollection = Conversions.createCollection(dstField.type(), srcValue.size());
			for (Object srcComponentValue : srcValue) {
				Object newInstance = Reflection.newInstance(dstField.getComponentClass());
				fieldByFieldCopy(srcComponentValue, newInstance);
				dstCollection.add(newInstance);
			}
			dstField.setObject(dst, dstCollection);
		} else {
			Collection<Object> dstCollection = Conversions.createCollection(dstField.type(), srcValue.size());
			for (Object srcComponentValue : srcValue) {
				if (srcComponentValue != null) {
					dstCollection.add(srcComponentValue.toString());
				}
			}
			dstField.setObject(dst, dstCollection);
		}
	}

}
