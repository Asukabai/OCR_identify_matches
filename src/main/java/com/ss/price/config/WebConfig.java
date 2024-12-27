package com.ss.price.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/table/**")
                .allowedOrigins("http://192.168.65.51:20324")
                .allowedMethods("GET", "POST", "OPTIONS");
    }
}