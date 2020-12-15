package com.fader.vnote.spring.ioc.init;

import com.fader.vnote.spring.config.BaseConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author FaderW
 * @Date 2020/12/9 18:55
 */
@Component
public class InitBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor, PriorityOrdered {

    private static boolean match(String beanName) {
        return "initBean".equals(beanName);
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (match(beanName)) {
            System.out.println("InstantiationAwareBeanPostProcessor postProcessBeforeInstantiation执行");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (match(beanName)) {
            System.out.println("InstantiationAwareBeanPostProcessor postProcessAfterInstantiation执行");
            return true;
        }
        return false;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (match(beanName)) {
            System.out.println("InstantiationAwareBeanPostProcessor postProcessProperties执行");
        }
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (match(beanName)) {
            System.out.println("BeanPostProcessor postProcessBeforeInitialization执行");
        }
        // 这里如果返回null 将导致后续其他beanPostProcessor不执行
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (match(beanName)) {
            System.out.println("BeanPostProcessor postProcessAfterInitialization执行");
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
