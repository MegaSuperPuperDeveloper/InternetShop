package com.example.enums;

import lombok.Getter;

@Getter
public enum Role {

    ROLE_USER("User", "ROLE_USER", 1),
    ROLE_ADMIN("Admin", "ROLE_ADMIN", 2),
    ROLE_OWNER("Owner", "ROLE_OWNER", 3);

    private final String name;
    private final String systemName;
    private final int hierarchy;

    Role(String name, String systemName, int hierarchy) {
        this.name = name;
        this.systemName = systemName;
        this.hierarchy = hierarchy;
    }

}