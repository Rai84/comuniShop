package com.pi.comuniShop.config;

import com.pi.comuniShop.service.UsuarioDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    private CustomSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/painel", "/negocios", "/negocios/salvar", "/usuarios/salvar", "/usuario/home", "/enderecos/**", "/login", "/criar-conta", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/usuario/**").hasRole("USUARIO")
                        .requestMatchers("/usuario/**").hasRole("CLIENTE")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/funcionario/**").hasRole("FUNCIONARIO")
                        .requestMatchers("/cliente/**").hasRole("CLIENTE")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirect", true)
                        .failureUrl("/?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll());

        return http.build();
    }
}
