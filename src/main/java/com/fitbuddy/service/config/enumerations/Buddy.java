package com.fitbuddy.service.config.enumerations;

import lombok.Getter;



@Getter
public enum Buddy{
    CHICKEN("치킨", "치킨"),
    RABBIT("토끼", "토끼"),
    DUCK("오리", "오리");

    private String name;
    private String description;


    Buddy(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
