package com.github.east196.lab;

import java.util.Arrays;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 使用javaassist的反射方法获取方法的参数名
 * 
 */
public class Classes {

    public static void main( String[] args )
    {
        System.out.println( Arrays.toString(Classes.getReflectParamName(Classes.class, "getReflectParamName")));
    }

	public static String[] getReflectParamName(Class<?> clazz, String methodName) {
		String[] paramNames = null;
		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = pool.get(clazz.getName());
			CtMethod cm = cc.getDeclaredMethod(methodName);
			// 使用javaassist的反射方法获取方法的参数名
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
			if (attr == null) {
				// exception
			}
			paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			for (int i = 0; i < paramNames.length; i++)
				paramNames[i] = attr.variableName(i + pos);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return paramNames;
	}
}