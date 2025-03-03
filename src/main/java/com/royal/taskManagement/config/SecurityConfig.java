package com.royal.taskManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности приложения.
 * Настроены фильтры для аутентификации с использованием JWT, а также правила доступа к различным эндпоинтам
 * в зависимости от роли пользователя.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param jwtRequestFilter фильтр JWT-аутентификации
     */
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Определяет цепочку фильтров безопасности.
     *
     * @param http объект конфигурации безопасности
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception в случае ошибки конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/{id}/assign").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/tasks/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/{id}/status").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/{id}/priority").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/tasks/author").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/tasks/assignee").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/tasks").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/tasks/{taskId}/comments").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/tasks/{taskId}/comments").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Определяет менеджер аутентификации.
     *
     * @param authenticationConfiguration конфигурация аутентификации
     * @return объект AuthenticationManager
     * @throws Exception в случае ошибки
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Определяет кодировщик паролей.
     *
     * @return объект PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
