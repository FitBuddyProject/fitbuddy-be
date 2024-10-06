package com.fitbuddy.service.config.modelMapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "model-mapper-configuration")
public class ModelMapperConfiguration {
    private ModelMapper modelMapper;

    private void strictStrategy() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
    private void useReflection() {
        this.modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
    }
    @Bean
    public ModelMapper mapper () {
        modelMapper = new ModelMapper();

        this.strictStrategy();
        this.useReflection();

        return modelMapper;
    }
}
