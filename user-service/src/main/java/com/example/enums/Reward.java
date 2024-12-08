package com.example.enums;

import lombok.Getter;

@Getter
public enum Reward {

    USER_HAS_REGISTERED("Пользователь присоединился к нам!"),
    THE_FIRST_USER_BESIDES_OWNERS("Первый пользователь!");

    private final String name;

    Reward(String name) {
        this.name = name;
    }
}