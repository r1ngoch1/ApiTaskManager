package com.royal.taskManagement.controller;

import com.royal.taskManagement.dto.AuthenticationRequestDTO;
import com.royal.taskManagement.dto.AuthenticationResponseDTO;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки аутентификации и регистрации пользователей.
 */
@RestController
public class AuthController {

    private final AuthService authService;
    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param authService сервис аутентификации и регистрации
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Аутентификация пользователя по email и паролю.
     *
     * @param authenticationRequest объект с учетными данными пользователя
     * @return ответ с JWT-токеном в случае успешной аутентификации или сообщение об ошибке
     */
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Аутентифицирует пользователя по его email и паролю. Возвращает JWT-токен, если аутентификация успешна."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аутентификация успешна",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка аутентификации",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody @Parameter(description = "Объект с учетными данными для аутентификации") AuthenticationRequestDTO authenticationRequest) {

        LOGGER.info("Попытка аутентифицировать пользователя: " + authenticationRequest.getEmail());
        try {
            AuthenticationResponseDTO response = authService.authenticate(authenticationRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error("Аутентификация провалена", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param user объект нового пользователя
     * @return сообщение о результате регистрации
     */
    @Operation(
            summary = "Регистрация пользователя",
            description = "Регистрирует нового пользователя, принимая его данные (email, имя и т.д.)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Регистрация успешна",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Ошибка регистрации",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody @Parameter(description = "Объект нового пользователя для регистрации") User user) {

        LOGGER.info("Попытка зарегистрировать пользователя: " + user.getEmail());
        try {
            String message = authService.register(user);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Регистрация провалена", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
