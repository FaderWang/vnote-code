package com.fader.vnote.spring.ioc.cycle;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author FaderW
 * 2019/7/4
 */
@Service
public class ServiceB {

//    @Setter
//    private ServiceA serviceA;
//
//    public ServiceB(ServiceA serviceA) {
//        this.serviceA = serviceA;
//    }

    @Resource
    private ServiceA service;
}
