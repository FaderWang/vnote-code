package com.fader.vnote.spring;

import com.fader.vnote.spring.annotation.SingletonComponent;
import com.fader.vnote.spring.config.BaseConfig;
import com.fader.vnote.spring.service.SimpleSingletonService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * @author FaderW
 * 2019/11/6
 */

public class AnnotationUtilDemo {

    public static void utilDemo() {
        Annotation[] annotations = SimpleSingletonService.class.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        SingletonComponent singletonComponent = AnnotationUtils.findAnnotation(SimpleSingletonService.class,
                SingletonComponent.class);

        System.out.println("@SingletonComponent : " + singletonComponent);
        System.out.println("@SingletonComponent value : " + singletonComponent.value());
        System.out.println("@SingletonComponent value : " + AnnotationUtils.getValue(singletonComponent, "value"));

        System.out.println("----------------------------------------------");

        Scope scopeAnnocation = AnnotationUtils.findAnnotation(SimpleSingletonService.class,
                Scope.class);

        System.out.println("@Scope : " + scopeAnnocation);
        System.out.println("@Scope value: " + AnnotationUtils.getValue(scopeAnnocation, "scopeName"));

        System.out.println("----------------------------------------------");

        Component componentAnnotation = AnnotatedElementUtils.findMergedAnnotation(SimpleSingletonService.class,
                Component.class);

        System.out.println("@Component : " + componentAnnotation);
        System.out.println("@Component value: " + AnnotationUtils.getValue(componentAnnotation, "value"));
    }

    static void contextRun() {
        AnnotationConfigApplicationContext cfg = new AnnotationConfigApplicationContext();
        cfg.register(BaseConfig.class);
        cfg.refresh();

    }

    public static void main(String[] args) {
//        utilDemo();
        contextRun();
    }
}
