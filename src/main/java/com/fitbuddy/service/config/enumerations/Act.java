package com.fitbuddy.service.config.enumerations;

import lombok.Getter;

public enum Act {
    EXERCISE(0, 0),
    SHOWER(0, 0),
    SLEEP(0, 0),
    TALK(0, 0),
    RECOVER(0, 0),
    PETTING(0, 0);

    @Getter
    private int exp;

    @Getter
    private int tired;

    Act(int exp, int tired) {
        this.exp = exp;
        this.tired = tired;
    }
}
