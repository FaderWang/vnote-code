package com.fader.vnote.spring.config;

import com.fader.vnote.spring.ioc.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author FaderW
 * 2020/7/21
 */

@Configuration
public class BaseConfig {

    @Bean
    public Car car() {
        return () -> System.out.println("car");
    }
}
