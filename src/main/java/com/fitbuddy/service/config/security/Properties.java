package com.fitbuddy.service.config.security;

import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@ConfigurationProperties(ignoreInvalidFields = true, value = "security")
@ToString
public class Properties {
    List<String> ignoreJwt;
    List<String> ignoreXss;

    public void setIgnoreJwt(List<String> ignoreJwt) {
        this.ignoreJwt = ignoreJwt;
    }

    public void setIgnoreXss(List<String> ignoreXss) {
        this.ignoreXss = ignoreXss;
    }

    public List<String> getIgnoreJwt() {
        return ignoreJwt;
    }

    public List<String> getIgnoreXss() {
        return ignoreXss;
    }

    public List<String> getAll () {


        List<String> all = new ArrayList<>();
        if(Objects.nonNull(this.ignoreJwt) && !this.ignoreJwt.isEmpty())all.addAll(this.ignoreJwt);
        if(Objects.nonNull(this.ignoreXss) && !this.ignoreXss.isEmpty())all.addAll(this.ignoreXss);

        return all;
    }
}
