package com.example.enums;

import lombok.Getter;

@Getter
public enum Currency {

    DOLLARS("Dollars", "$"),
    EUROS("Dollars", "€"),
    RUBLES("Rubles", "₽");

    private String name;
    private String icon;

    Currency(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }
}