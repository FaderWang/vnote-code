package com.fader.vnote.javabase.loader;

import com.google.common.collect.Sets;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author FaderW
 * 2020/6/23
 */

public class MyClassloader extends ClassLoader {

    private String swapPath;

    private Set<String> toUse;

    public MyClassloader(String swapPath, Set<String> useMyClassLoaderLoad) {
        this.swapPath = swapPath;
        this.toUse = useMyClassLoaderLoad;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);
        if (c == null && toUse.contains(name)) {
            c = findClass(name);
            if (c != null) {
                return c;
            }
        }
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = getClassByte(name);
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] getClassByte(String name) {
        String className = name.substring(name.lastIndexOf('.') + 1, name.length()) + ".class";
        try {
            FileInputStream fileInputStream = new FileInputStream(swapPath + className);
            byte[] buffer = new byte[1024];
            int length = 0;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((length = fileInputStream.read(buffer)) > 0){
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String swapPath = MyClassloader.class.getResource("").getPath() + "swap/";
                String className = "com.fader.vnote.javabase.loader.swap.Test";

                //每次都实例化一个ClassLoader，这里传入swap路径，和需要特殊加载的类名
                MyClassloader myClassLoader = new MyClassloader(swapPath, Sets.newHashSet(className));
                try {
                    //使用自定义的ClassLoader加载类，并调用printVersion方法。
                    Object o = myClassLoader.loadClass(className).newInstance();
                    o.getClass().getMethod("printVersion").invoke(o);
                } catch (InstantiationException |
                        IllegalAccessException |
                        ClassNotFoundException |
                        NoSuchMethodException |
                        InvocationTargetException ignored) {
                }
            }
        }, 0,2000);
    }
}
