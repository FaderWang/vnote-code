package com.fader.vnote.spring.ioc.cycle;

import com.fader.vnote.spring.ioc.Car;

/**
 * @author FaderW
 * 2020/2/18
 */

public class BWM implements Car{

    @Override
    public void display() {
        System.out.println("this is BWM");
    }
}
