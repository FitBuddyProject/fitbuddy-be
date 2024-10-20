package com.fitbuddy.service.config.enumerations;

import lombok.Getter;



@Getter
public enum Buddy{
    CHICKEN("삐약이", "삐약이"),
    OTTER("수달이", "수달이"),
    MONSTER("이상이", "이상이");

    private String name;
    private String description;


    Buddy(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
