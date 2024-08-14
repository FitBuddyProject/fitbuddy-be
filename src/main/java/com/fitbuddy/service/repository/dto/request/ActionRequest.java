package com.fitbuddy.service.repository.dto.request;

import lombok.Data;

@Data
public class ActionRequest {
    private int year;
    private int month;
    private String userUuid;
    private Type mode;

    public enum Type {
        CALENDAR,
        HISTORY
    }

}
