package com.fader.vnote.spring.ioc.cycle;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author FaderW
 * 2019/7/4
 */
@Service
public class ServiceA {

    @Resource
    private ServiceB serviceB;

//    @Setter
//    private ServiceB serviceB;
//
//    public ServiceA(ServiceB serviceB) {
//        this.serviceB = serviceB;
//    }

    @Log
    public void execute() {
        System.out.println("execute sql");
    }

}
