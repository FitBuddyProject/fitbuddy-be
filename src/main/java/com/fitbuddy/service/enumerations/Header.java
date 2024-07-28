package com.fitbuddy.service.enumerations;

import lombok.Getter;

@Getter
public enum Header {
    ACCESS_TOKEN("Authorization"),
    REFRESH_TOKEN("Authorization_refresh");
    private String value;

    Header(String value) {
        this.value = value;
    }
}
