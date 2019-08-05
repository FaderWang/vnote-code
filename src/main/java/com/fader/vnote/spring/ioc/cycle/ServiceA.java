package com.fader.vnote.spring.ioc.cycle;

import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author FaderW
 * 2019/7/4
 */
@NoArgsConstructor
public class ServiceA {

    @Setter
    private ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

}
