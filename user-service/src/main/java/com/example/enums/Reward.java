package com.example.enums;

import lombok.Getter;

@Getter
public enum Reward {

    USER_HAS_REGISTERED("Пользователь присоединился к нам!");

    private final String name;

    Reward(String name) {
        this.name = name;
    }
}