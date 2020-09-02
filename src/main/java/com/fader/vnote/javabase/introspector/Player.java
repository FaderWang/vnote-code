package com.fader.vnote.javabase.introspector;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Player {

    private int age;

    private String name;

    private Date birth;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void play() {
        System.out.print("do play");
    }

    public void setName(String name) {
        String oldValue = this.name;
        this.name = name;
        firePropertyChange("name", oldValue, name);
    }

    public void addPropertyListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }



    public static void main(String[] args) throws IntrospectionException {
//        BeanInfo beanInfo = Introspector.getBeanInfo(Player.class);
//        // 属性描述符
//        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//        Stream.of(propertyDescriptors).forEach(System.out::println);
//
//        // 方法描述符
//        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
//        Stream.of(methodDescriptors).forEach(System.out::println);
//        configBind();
//        typeHandler();

        Player player = new Player();
        player.setName("curry");
        player.addPropertyListener(System.out::println);
        player.setName("dsy");
        player.setName("dsj");
    }

    /**
     * 配置绑定
     */
    static void configBind() {
        Player player = new Player();
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(player);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("age", 10);
        propertyValues.addPropertyValue("name", "curry");
        beanWrapper.setPropertyValues(propertyValues);

        System.out.print(player);
    }

    /**
     * 类型转换
     * @throws IntrospectionException
     */
    static void typeHandler() throws IntrospectionException {
        Player player = new Player();
        Map<String, Object> properties = ImmutableMap.of("age", 24, "name", "curry", "birth", "2020-08-28");
        BeanInfo beanInfo = Introspector.getBeanInfo(Player.class, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        Stream.of(propertyDescriptors).forEach(propertyDescriptor -> {
            // 获取属性名称
            String name = propertyDescriptor.getName();
            Object value = properties.get(name);
            if (Objects.equals(name, "birth")) {
                propertyDescriptor.setPropertyEditorClass(DatePropertyEditor.class);
                PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(player);
                propertyEditor.addPropertyChangeListener((p) -> {
                    Object convertValue = propertyEditor.getValue();
                    try {
                        propertyDescriptor.getWriteMethod().invoke(player, convertValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
                propertyEditor.setAsText(String.valueOf(value));
                return;
            }
            try {
                propertyDescriptor.getWriteMethod().invoke(player, String.valueOf(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        System.out.print(player);
    }

}
