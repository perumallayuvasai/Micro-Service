package com.microservice.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange()
                // Swagger & OpenAPI endpoints (allow for API exploration)
                .pathMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/webjars/**").permitAll()
                .pathMatchers("/actuator/**", "/actuator/health/**").permitAll()
                // Admin and customer protected endpoints
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/customer/**").hasRole("CUSTOMER")
                //Fallback endpoint
                .pathMatchers("/fallback", "/error").permitAll()
                .anyExchange().authenticated()
            .and()
                .httpBasic()
            .and()
                .csrf().disable();

        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("adminpass"))
                .roles("ADMIN")
                .build();

        UserDetails customer = User.withUsername("customer")
                .password(passwordEncoder().encode("custpass"))
                .roles("CUSTOMER")
                .build();

        return new MapReactiveUserDetailsService(admin, customer);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
