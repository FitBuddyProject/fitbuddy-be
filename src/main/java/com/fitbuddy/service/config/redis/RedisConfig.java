package com.fitbuddy.service.config.redis;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.util.StringUtils;

@Configuration
@EnableRedisRepositories
public class RedisConfig implements EnvironmentAware {
    private Environment environment;
    @Override
    public void setEnvironment(Environment environment) {
      this.environment = environment;
    }

    private Integer port (){
        return environment.getProperty("spring.data.redis.port", Integer.class);
    }
    private String host () {
        return environment.getProperty("spring.data.redis.host", String.class);
    }

    private String password () {
        return environment.getProperty("spring.data.redis.password", String.class);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory () {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(this.host());
        redisStandaloneConfiguration.setPort(this.port());

        if(StringUtils.hasText(this.password())) redisStandaloneConfiguration.setPassword(this.password());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
