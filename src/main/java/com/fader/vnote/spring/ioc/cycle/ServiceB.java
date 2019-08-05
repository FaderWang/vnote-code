package com.fader.vnote.spring.ioc.cycle;

import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author FaderW
 * 2019/7/4
 */
@NoArgsConstructor
public class ServiceB {

    @Setter
    private ServiceA serviceA;

    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
