package com.fader.vnote.spring.ioc;

import lombok.Getter;
import lombok.Setter;

/**
 * @author FaderW
 * 2019/6/13
 */

public class DefaultServiceLocator {

    @Setter@Getter
    private Person person;

    public ClientService createClientServiceInstance() {
        return new ClientService();
    }
}
