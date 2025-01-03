package com.example.enums;

import lombok.Getter;

@Getter
public enum Role {

    ROLE_USER("Пользователь", 1),
    ROLE_ADMIN("Администратор", 2),
    ROLE_OWNER("Владелец", 3);

    private final String name;
    private final int hierarchy;

    Role(String name, int hierarchy) {
        this.name = name;
        this.hierarchy = hierarchy;
    }

}