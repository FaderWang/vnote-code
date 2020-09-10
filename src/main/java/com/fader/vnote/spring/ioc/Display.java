package com.fader.vnote.spring.ioc;

/**
 * @author FaderW
 * 2020/2/18
 */

public abstract class Display {

    void display() {
        getCar().display();
    }

    abstract Car getCar();
}
