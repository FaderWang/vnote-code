package com.fader.vnote.pattern.builder模式;

import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author FaderW
 * builder模式，适用于bean属性过多时
 * 2019/4/19
 */
public class User {
    String nickName;
    private final String username;
    private final String phone;

    String sex;
    String email;
    String address;

    public User(Builder builder) {
        this.nickName = builder.nickName;
        this.username = builder.username;
        this.phone = builder.phone;
        this.sex = builder.sex;
        this.email = builder.email;
        this.address = builder.address;
    }

    @Setter
    @Accessors(chain = true)
    public static class Builder {
        String nickName;
        final String username;
        final String phone;
        String sex;

        String email;
        String address;

        public Builder(String username, String phone) {
            this.username = username;
            this.phone = phone;
        }

        public User build() {
            return new User(this);
        }
    }

    public static void main(String[] args) {
        User user = new Builder("faderw", "18852951219").build();
    }
}
