package com.fader.vnote.javabase.clone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FaderW
 * 2020/9/26
 */

public class Children extends Person {
    private static final long serialVersionUID = 4933126480099567595L;

    @Override
    public Integer get() {
        return 1;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add(null);


    }
}
