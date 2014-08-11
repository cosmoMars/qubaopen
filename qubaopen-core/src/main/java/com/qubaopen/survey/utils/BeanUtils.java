package com.qubaopen.survey.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

public class BeanUtils extends org.springframework.beans.BeanUtils {

	public static void copyProperties(Object source, Object target) throws BeansException {

		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						readMethod.invoke(source);
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						if (value != null) {
							if (value instanceof String) {
								String v = (String) value;
								if (!StringUtils.isBlank(v)) {
									Method writeMethod = targetPd.getWriteMethod();
									if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
										writeMethod.setAccessible(true);
									}
									writeMethod.invoke(target, v);
								}
							} else if (value instanceof Number) {
								Number v = (Number) value;
								if (!StringUtils.equals(v.toString(), "0")) {
									Method writeMethod = targetPd.getWriteMethod();
									if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
										writeMethod.setAccessible(true);
									}
									writeMethod.invoke(target, v);
								}
							} else if (value instanceof Boolean) {
								Boolean v = (Boolean) value;
								if (v != false) {
									Method writeMethod = targetPd.getWriteMethod();
									if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
										writeMethod.setAccessible(true);
									}
									writeMethod.invoke(target, v);
								}
							}
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}

}
