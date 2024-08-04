package com.fitbuddy.service.etc.validations;

import com.fitbuddy.service.repository.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class Check {
//    args(.., com.fitbuddy.service.repository.dto.UserDto)
    @Before(value = """
                @annotation(org.springframework.web.bind.annotation.PostMapping) &&
                @within( org.springframework.validation.annotation.Validated)
            """)
    public void check(JoinPoint joinPoint) {
        log.error("JOIN {}", joinPoint.getArgs());
    }
}
