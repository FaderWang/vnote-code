package com.fader.vnote.collection.queue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FaderW
 * 可以获取最小值的栈
 * 2019/3/22
 */

public class MinStack {

    private List<Integer> data = new ArrayList<>();
    private List<Integer> min = new ArrayList<>();

    public static void main(String[] args) {
        // path不能以'/'开头时，path是指类加载器的加载范围，在资源加载的过程中，使用的逐级向上委托的形式加载的，'/'表示Boot ClassLoader中的加载范围，因为这个类加载器是C++实现的，所以加载范围为null。
        System.out.println(MinStack.class.getClassLoader().getResource("com/fader"));
        System.out.println(MinStack.class.getClassLoader().getResource("/com/fader"));

        // path不以'/'开头时，默认是从此类所在的包下取资源；path以'/'开头时，则是从项目的ClassPath根下获取资源。在这里'/'表示ClassPath
        System.out.println(MinStack.class.getResource("/com/fader"));
        System.out.println(MinStack.class.getResource("MinStack.class"));
        System.out.println(MinStack.class.getName());

    }
}
