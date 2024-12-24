package com.example.enums;

import lombok.Getter;

@Getter
public enum Role {

    USER("Пользователь", 1),
    MODERATOR("Модератор", 2),
    SENIOR_MODERATOR("Старший модератор", 3),
    TYPIST_MODERATOR("Наборщик Модерации", 4),
    LEADER_MODERATOR("Глава Модерации", 5),
    DEVELOPER("Разработчик", 6),
    OWNER("Владелец", 7);

    private final String name;
    private final int hierarchy;

    Role(String name, int hierarchy) {
        this.name = name;
        this.hierarchy = hierarchy;
    }

}