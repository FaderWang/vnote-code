package com.fader.vnote.tool;

import lombok.Data;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * @author FaderW
 * 2019/10/15
 */

public class BeanConvertUtils {

    private static MapperFactory mapperFactory = null;
    private static MapperFacade mapperFacade = null;

    static {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    public static <S, D> D map(S s, Class<D> dClass) {
        return mapperFacade.map(s, dClass);
    }

    public static <S, D> void map(S s, D d) {
        mapperFacade.map(s, d);
    }


    @Data
    public static class Person {
        private String name;
        private String age;
        private String sex;
    }

    @Data
    public static class User {
        private String name;
        private String age;
        private String sex;
    }



    public static void main(String[] args) {
        Person person = new Person();
        person.setName("FaderW");
        person.setAge("20");
        person.setSex("male");

        User u1 = BeanConvertUtils.map(person, User.class);
        System.out.println(u1.toString());

        User u2 = new User();
        BeanConvertUtils.map(person, u2);
        System.out.println(u2.toString());
    }
}
