package com.example.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {

    public SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.build();
    }

}