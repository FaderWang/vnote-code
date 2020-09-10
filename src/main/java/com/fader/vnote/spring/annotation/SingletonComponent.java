package com.fader.vnote.spring.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author FaderW
 * 2019/11/6
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Scope("singleton")
@Component
@Inherited
public @interface SingletonComponent {

    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";
}
