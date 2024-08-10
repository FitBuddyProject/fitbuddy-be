package com.fitbuddy.service.config.enumerations;

import lombok.Getter;

@Getter
public enum Buddy {
    CHICKEN_LV1("치킨", "치킨", 1),
    CHICKEN_LV2("치킨", "치킨", 2),
    CHICKEN_LV3("치킨", "치킨", 3);

    private String name;
    private String description;
    private int level;

    Buddy(String name, String description, int level) {
        this.name = name;
        this.description = description;
        this.level = level;
    }
}
