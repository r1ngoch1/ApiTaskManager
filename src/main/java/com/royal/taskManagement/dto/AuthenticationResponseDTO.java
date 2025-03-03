package com.royal.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Этот класс содержит JSON Web Token (JWT), который используется для аутентификации пользователя.
 */
@Schema(description = "Ответ с JSON Web Token (JWT), который предоставляется пользователю после успешной аутентификации.")
public class AuthenticationResponseDTO {

    /**
     * JSON Web Token (JWT), предоставленный пользователю после успешной аутентификации.
     */
    @Schema(description = "JSON Web Token пользователя, предоставляемый после успешной аутентификации", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String jwt;

    /**
     * Конструктор для создания объекта с JWT.
     *
     * @param jwt JSON Web Token, который будет предоставлен пользователю.
     */
    public AuthenticationResponseDTO(String jwt) {
        this.jwt = jwt;
    }

    /**
     * Геттеры и сеттеры
     */
    public String getJwt() {
        return jwt;
    }


    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
