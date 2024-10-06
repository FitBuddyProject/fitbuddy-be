package com.fitbuddy.service.repository.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ActionRequest {
    private int year;
    private int month;
    private String userUuid;

    private String lastKey;


    public LocalDate getStartDate () {
        return LocalDate.of(year, month, 1);
    }

    public LocalDate getEndDate () {
        return LocalDate.of(year, month + 1, 1).minusDays(1);
    }

}
