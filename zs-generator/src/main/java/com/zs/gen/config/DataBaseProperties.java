package com.zs.gen.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.master")
@Data
public class DataBaseProperties {

    private String url;
    private String username;
    private String password;
}
