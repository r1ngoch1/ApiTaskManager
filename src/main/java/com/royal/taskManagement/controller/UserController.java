package com.royal.taskManagement.controller;

import com.royal.taskManagement.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDetailsServiceImpl userService;

    /**
     * Конструктор контроллера пользователей.
     *
     * @param userService сервис для управления пользователями
     */
    @Autowired
    public UserController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Назначает пользователю роль администратора.
     *
     * @param userId ID пользователя, которому добавляется роль администратора
     * @return HTTP-ответ с результатом операции
     */
    @Operation(
            summary = "Назначить роль администратора пользователю",
            description = "Этот метод назначает роль администратора пользователю по его ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль администратора успешно добавлена пользователю"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/{userId}/add-admin-role")
    public ResponseEntity<Void> addAdminRole(
            @PathVariable @Parameter(description = "ID пользователя, которому добавляется роль администратора") Long userId) {
        userService.addAdminRole(userId);
        return ResponseEntity.ok().build();
    }
}
