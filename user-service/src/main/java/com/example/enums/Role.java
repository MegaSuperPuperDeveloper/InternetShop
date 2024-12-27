package com.example.enums;

import lombok.Getter;

@Getter
public enum Role {

    USER("Пользователь", 1),
    MODERATOR("Модератор", 2),
    OWNER("Владелец", 3);

    private final String name;
    private final int hierarchy;

    Role(String name, int hierarchy) {
        this.name = name;
        this.hierarchy = hierarchy;
    }

}