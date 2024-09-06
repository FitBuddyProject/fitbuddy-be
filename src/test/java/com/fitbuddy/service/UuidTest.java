package com.fitbuddy.service;

import com.fasterxml.uuid.Generators;
import com.fitbuddy.service.etc.uuid.Uuid;
import org.junit.jupiter.api.Test;

public class UuidTest {
    @Test
    public void UuidTest () {
        String uuid = Uuid.generate();
        System.out.println(uuid + " " + uuid.length());
    }

    @Test
    public void uuid () {
        System.out.println(Generators.timeBasedGenerator().generate().toString());
    }
}
