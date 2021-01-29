package com.fader.vnote.jvm.classloader;

/**
 * @author FaderW
 */
public class CustomClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    public static void main(String[] args) {
        ClassLoader classLoader = new CustomClassLoader();
//        classLoader.loadClass()
    }
}
