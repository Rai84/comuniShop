package com.pi.comuniShop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desabilita CSRF só para teste
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // tudo liberado
                )
                .formLogin(login -> login.disable()) // sem formulário de login
                .httpBasic(httpBasic -> httpBasic.disable()); // sem basic auth

        return http.build();
    }
}