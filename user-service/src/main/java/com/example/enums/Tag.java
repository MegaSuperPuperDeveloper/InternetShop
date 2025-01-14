package com.example.enums;

import lombok.Getter;

@Getter
public enum Tag {
    CLOTHES("Одежда"),
    HOUSE("Дом"),
    FOR_KIDS("Детские товары"),
    BEAUTY("Красота"),
    ELECTRONICS("Электроника"),
    HOME_APPLIANCES("Бытовая техника"),
    FLOWERS("Цветы"),
    DACHA_AND_GROUND("Дача и сад"),
    FOOD_PRODUCTS("Продукты питания"),
    BUILDING_AND_REPAIR("Ремонт"),
    FURNITURE("Мебель"),
    DISCOUNTS_AND_PROMOTIONS("Скидки и акции"),
    CAR_PRODUCTS("Товары для авто"),
    PHARMACY("Аптека"),
    SPORT("Спорт"),
    RELAX("Отдых"),
    IKEA_PRODUCTS("Товары ИКЕА"),
    JEWELLERY("Ювелирные товары"),
    BOOKS("Книги"),
    HOBBY_AND_CREATIVE("Хобби и творчество"),
    LEISURE_AND_ENTERTAINMENT("Досуг и развлечения"),
    FOR_SCHOOL_AND_OFFICE("Для школы и офиса"),
    LAPTOPS_AND_COMPUTERS("Ноутбуки и компьютеры"),
    EVERYTHING_FOR_GAMING("Гейминг"),
    SMART_STATION("Умные колонки"),
    HYGIENE("Гигиена"),
    CHEMISTRY("Химия"),
    ZOO_PRODUCTS("Зоотовары"),
    OPTICS("Оптика");

    private final String name;

    Tag(String name) {
        this.name = name;
    }
}