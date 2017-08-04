package com.github.east196.boonplus.mapper

import io.advantageous.boon.core.Conversions
import io.advantageous.boon.core.Exceptions
import io.advantageous.boon.core.Str
import io.advantageous.boon.core.Typ
import io.advantageous.boon.core.TypeType
import io.advantageous.boon.core.reflection.BeanUtils
import io.advantageous.boon.core.reflection.Reflection
import io.advantageous.boon.core.reflection.fields.FieldAccess
import java.util.Collection
import java.util.Map
import java.util.Set

class Mapper {
	def static <T> T map(Object src, Class<T> destKlass) {
		var T dest = Reflection.newInstance(destKlass)
		map(src, dest)
		return dest
	}

	def static void map(Object src, Object dst) {
		val Class<?> srcClass = src.getClass()
		var Map<String, FieldAccess> srcFields = Reflection.getAllAccessorFields(srcClass)
		val Class<?> dstClass = dst.getClass()
		var Map<String, FieldAccess> dstFields = Reflection.getAllAccessorFields(dstClass)
		for (FieldAccess srcField : srcFields.values()) {
			var FieldAccess dstField = dstFields.get(srcField.name())
			try {
				if (dstField === null) {
					searchDstFieldBySrcFieldAnnotation(srcField, dstFields)
				}
				if (dstField === null) {
					dstField = searchDstFieldByDstFieldAnnotation(srcField, dstFields)
				}
				copySrcFieldToDestField(src, dst, dstField, srcField, null)
			} catch (Exception ex) {
				Exceptions.handle(
					Str.sputs("copying field", srcField.name(), srcClass, " to ", dstField.name(), dstClass), ex)
			}

		}
	}
	
	protected def static FieldAccess searchDstFieldBySrcFieldAnnotation(FieldAccess srcField, Map<String, FieldAccess> dstFields) {
		if (srcField.hasAnnotation("Mapping")) {
			var Map<String, Object> aliasD = srcField.getAnnotationData("Mapping")
			var String alias = (aliasD.get("value") as String)
			var dstField = dstFields.get(alias)
			return dstField
		}
	}

	def static searchDstFieldByDstFieldAnnotation(FieldAccess srcField, Map<String, FieldAccess> dstFields) {
		for (FieldAccess option : dstFields.values()) {
			if (option.hasAnnotation("Mapping")) {
				var Map<String, Object> aliasD = option.getAnnotationData("Mapping")
				var String alias = (aliasD.get("value") as String)
				if (srcField.name().equals(alias)) {
					var dstField = option
					return dstField
				}
			}
		}
	}

	def private static void fieldByFieldCopy(Object src, Object dst, Set<String> ignore) {
		val Class<?> srcClass = src.getClass()
		var Map<String, FieldAccess> srcFields = Reflection.getAllAccessorFields(srcClass)
		val Class<?> dstClass = dst.getClass()
		var Map<String, FieldAccess> dstFields = Reflection.getAllAccessorFields(dstClass)
		for (FieldAccess srcField : srcFields.values()) {
			if (!ignore.contains(srcField.name())) {
				var FieldAccess dstField = dstFields.get(srcField.name())
				try {
					copySrcFieldToDestField(src, dst, dstField, srcField, ignore)
				} catch (Exception ex) {
					Exceptions.handle(
						Str.sputs("copying field", srcField.name(), srcClass, " to ", dstField.name(), dstClass), ex)
				}
			}
		}
	}

	def private static void fieldByFieldCopy(Object src, Object dst) {
		val Class<?> srcClass = src.getClass()
		var Map<String, FieldAccess> srcFields = Reflection.getAllAccessorFields(srcClass)
		val Class<?> dstClass = dst.getClass()
		var Map<String, FieldAccess> dstFields = Reflection.getAllAccessorFields(dstClass)
		for (FieldAccess srcField : srcFields.values()) {
			var FieldAccess dstField = dstFields.get(srcField.name())
			try {
				copySrcFieldToDestField(src, dst, dstField, srcField, null)
			} catch (Exception ex) {
				Exceptions.handle(
					Str.sputs("copying field", srcField.name(), srcClass, " to ", dstField.name(), dstClass), ex)
			}

		}
	}

	def private static void copySrcFieldToDestField(Object src, Object dst, FieldAccess dstField, FieldAccess srcField,
		Set<String> ignore) {
		if (srcField.isStatic()) {
			return;
		}
		if (dstField === null) {
			return;
		}
		/* If its primitive handle it. */
		if (srcField.isPrimitive()) {
			dstField.setValue(dst, srcField.getValue(src))
			return;
		}
		var Object srcValue = srcField.getObject(src)
		/* if value is null then handle it unless it is primitive. */
		if (srcValue === null) {
			if (!dstField.isPrimitive()) {
				dstField.setObject(dst, null)
			}
			return;
		}
		/* Basic type. */
		if (Typ.isBasicType(srcField.type())) {
			/* Handle non primitive copy. */
			var Object value = srcField.getObject(src)
			dstField.setValue(dst, value)
			return;
		}
		/* Types match and not a collection so just copy. */
		if ((!(srcValue instanceof Collection<?>) && (dstField.type() === srcValue.getClass())) ||
			Typ.isSuperType(dstField.type(), srcValue.getClass())) {
			dstField.setObject(dst, BeanUtils.copy(srcField.getObject(src)))
			return;
		}
		/* Collection field copy. */
		if ((srcValue instanceof Collection<?>) && (dstField.getComponentClass() !== null) &&
			Typ.isCollection(dstField.type())) {
			handleCollectionFieldCopy(dst, dstField, (srcValue as Collection<?>))
			return;
		}
		/* Non identical object copy. */
		if ((dstField.typeEnum() === TypeType.ABSTRACT) || (dstField.typeEnum() === TypeType.INTERFACE)) { // no op
		} else {
			var Object newInstance = Reflection.newInstance(dstField.type())
			if (ignore === null) {
				fieldByFieldCopy(srcField.getObject(src), newInstance)
			} else {
				fieldByFieldCopy(srcField.getObject(src), newInstance, ignore)
			}
			dstField.setObject(dst, newInstance)
		}
	}

	def private static void handleCollectionFieldCopy(Object dst, FieldAccess dstField, Collection<?> srcValue) {
		if (dstField.getComponentClass() !== Typ.string) {
			var Collection<Object> dstCollection = Conversions.createCollection(dstField.type(), srcValue.size())
			for (Object srcComponentValue : srcValue) {
				var Object newInstance = Reflection.newInstance(dstField.getComponentClass())
				fieldByFieldCopy(srcComponentValue, newInstance)
				dstCollection.add(newInstance)
			}
			dstField.setObject(dst, dstCollection)
		} else {
			var Collection<Object> dstCollection = Conversions.createCollection(dstField.type(), srcValue.size())
			for (Object srcComponentValue : srcValue) {
				if (srcComponentValue !== null) {
					dstCollection.add(srcComponentValue.toString())
				}
			}
			dstField.setObject(dst, dstCollection)
		}
	}
}
