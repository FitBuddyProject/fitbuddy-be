package com.fitbuddy.service.repository.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActionRequest {
    private int year;
    private int month;
    private String userUuid;
    @Nullable
    private String lastKey;


    @Hidden
    @JsonIgnore
    public LocalDate getStartDate () {
        System.out.println("MONTH" + month);
        return LocalDate.of(year, month, 1);
    }

    @Hidden
    @JsonIgnore
    public LocalDate getEndDate () {
        return LocalDate.of(year, month + 1, 1).minusDays(1);
    }

}
