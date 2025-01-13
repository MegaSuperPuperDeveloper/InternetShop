package com.example.enums;

import lombok.Getter;

@Getter
public enum Role {

    ROLE_USER("User", 1),
    ROLE_ADMIN("Admin", 2),
    ROLE_OWNER("Owner", 3);

    private final String name;
    private final int hierarchy;

    Role(String name, int hierarchy) {
        this.name = name;
        this.hierarchy = hierarchy;
    }

}