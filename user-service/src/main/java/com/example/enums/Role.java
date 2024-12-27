package com.example.enums;

import lombok.Getter;

@Getter
public enum Role {

    USER("Пользователь", 1),
    ADMIN("Администратор", 2);

    private final String name;
    private final int hierarchy;

    Role(String name, int hierarchy) {
        this.name = name;
        this.hierarchy = hierarchy;
    }

}