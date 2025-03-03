package com.royal.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Этот класс содержит информацию, необходимую для аутентификации пользователя.
 */
@Schema(description = "Объект для аутентификации пользователя. Содержит email и пароль для авторизации.")
public class AuthenticationRequestDTO {

    /**
     * Электронная почта пользователя.
     */
    @Schema(description = "Электронная почта пользователя", example = "user@example.com")
    private String email;

    /**
     * Пароль пользователя.
     */
    @Schema(description = "Пароль пользователя", example = "strongPassword123")
    private String password;

    /**
     * Геттеры и сеттеры
     */
    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
