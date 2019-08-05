package com.fader.vnote.tool;


import com.fader.vnote.jvm.exception.Hello;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author FaderW
 * 2019/7/9
 * 编译时加上-g 参数，通过asm访问字节码获取方法变量名
 */

public class ASMUtils {

    public static String[] getMethodParameterNames(Method method) throws IOException {
        int counts = method.getParameterCount();
        String[] result = new String[counts];

        ClassReader reader = new ClassReader(method.getDeclaringClass().getName());
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        reader.accept(new ClassAdapter(writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                 MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                 final Type[] argTypes = Type.getArgumentTypes(desc);

                 if (!method.getName().equals(name) | !matchTypes(argTypes, method.getParameterTypes())) {
                    return methodVisitor;
                 }

                 return new MethodAdapter(methodVisitor) {
                     @Override
                     public void visitLocalVariable(String s, String s1, String s2, Label label, Label label1, int i) {
                         int methodParameterIndex = Modifier.isStatic(method.getModifiers()) ? i : i - 1;
                         if (0 <= methodParameterIndex && methodParameterIndex < counts) {
                             result[methodParameterIndex] = name;
                         }
                         super.visitLocalVariable(s, s1, s2, label, label1, i);
                     }
                 };
            }
        }, 0);

        return result;
    }

    private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
        if (types.length != parameterTypes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(parameterTypes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws NoSuchMethodException, IOException {
        Class<?> classz = Hello.class;
        String[] array = getMethodParameterNames(classz.getDeclaredMethod("main", String[].class));
        System.out.println(array[0]);
    }
}
