package com.fader.vnote.spring.ioc;

import com.fader.vnote.spring.ioc.cycle.ServiceA;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * @author FaderW
 * 2019/6/13
 */

public class ClientService {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//        ClientService clientService = applicationContext.getBean("clientService", ClientService.class);

//        ServiceA serviceA = (ServiceA) applicationContext.getBean("serviceA");
//        ClassPathResource resource = new ClassPathResource("beans.xml");
//        System.out.println(serviceA);
        Display display = (Display) applicationContext.getBean("display");
        display.display();
    }
}
