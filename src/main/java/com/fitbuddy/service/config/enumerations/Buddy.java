package com.fitbuddy.service.config.enumerations;

import lombok.Getter;

@Getter
public enum Buddy {
    CHICKEN("치킨", "치킨");

    private String name;
    private String description;

    Buddy(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
