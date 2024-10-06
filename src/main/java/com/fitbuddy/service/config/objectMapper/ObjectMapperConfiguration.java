package com.fitbuddy.service.config.objectMapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "object-mapper-configuration")
public class ObjectMapperConfiguration {
    private ObjectMapper objectMapper;

    private void deserializeWhenEmptyCase() {
        this.objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        this.objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }
    private void deserializeWhenUnknownCase() {
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void serializeDateTime() {
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    }
    private void deserializeWhenEnumCase() {
        this.objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        this.objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }
    private void deserializeRegisterJavaTimeModule() {
        objectMapper.registerModule(new JavaTimeModule());
    }
    private void deserializeSettings() {
        this.deserializeWhenEnumCase();
        this.deserializeWhenEmptyCase();
        this.deserializeWhenUnknownCase();
        this.serializeDateTime();
        this.deserializeRegisterJavaTimeModule();
    }

    private void serializeSettings() {
        this.objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING,true);
        this.objectMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL,true);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
    }

    private void setJavaModule() {
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Bean
    public ObjectMapper objectMapper () {
        this.objectMapper = new ObjectMapper();
        this.deserializeSettings();
        this.serializeSettings();
        this.setJavaModule();
        return this.objectMapper;
    }
}
