package com.fader.vnote.javabase.reflect;

import org.apache.ibatis.annotations.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectSimple {

    public void methodParamAnnotation(@Param("value") String value, String key, @Param("id") Long id) {

    }

    static void mpTest() {
        Method[] methods = ReflectSimple.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().contains("methodParamAnnotation")) {
                Annotation[][] annotations = method.getParameterAnnotations();
                // 注解元数据二位数组长度总是为参数的长度。没有注解的参数注解集合为空数组。
                System.out.println("annotations length : " + annotations.length);
                for (int index = 0; index < annotations.length; index++) {
                    if (null != annotations[index] && annotations[index].length > 0) {
                        Annotation annotation = annotations[index][0];
                        System.out.println(annotation);
                    }
                }
            }
        }
    }

    static void methodNameTest() throws NoSuchMethodException {
        Method method = ReflectSimple.class.getMethod("methodParamAnnotation", String.class, String.class, Long.class);
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            System.out.println(parameter.getName());
        }
    }

    public static void main(String[] args) throws NoSuchMethodException {
//        mpTest();
        methodNameTest();
    }
}
