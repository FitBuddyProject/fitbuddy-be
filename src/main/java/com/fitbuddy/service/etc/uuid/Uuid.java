package com.fitbuddy.service.etc.uuid;

import com.fasterxml.uuid.Generators;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Uuid {
    public static String generate (){
        String uuid = Generators.timeBasedGenerator().generate().toString();
        byte[] uuidByte = uuid.getBytes(StandardCharsets.UTF_8);
        byte[] hashByte;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            hashByte = messageDigest.digest(uuidByte);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        StringBuilder builder = new StringBuilder();
        for( int i = 0; i < 12; i ++ ) {
            builder.append(String.format("%02x", hashByte[i]));
        }

        return builder.toString();
    }
}
