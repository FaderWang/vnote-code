package com.fader.vnote.spring.ioc.cycle;

import java.lang.annotation.*;

/**
 * @author FaderW
 * @Date 2020/12/8 14:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Log {
}
