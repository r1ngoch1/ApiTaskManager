package com.royal.taskManagement.service;

import com.royal.taskManagement.dto.AuthenticationRequestDTO;
import com.royal.taskManagement.dto.AuthenticationResponseDTO;
import com.royal.taskManagement.entity.User;

/**
 * Сервис для аутентификации и регистрации пользователей.
 * Предоставляет методы для аутентификации пользователя с помощью логина и пароля,
 * а также для регистрации нового пользователя в системе.
 */
public interface AuthService {

    /**
     * Аутентификация пользователя с использованием логина и пароля.
     * При успешной аутентификации возвращает токен JWT.
     *
     * @param request запрос для аутентификации, содержащий email и пароль.
     * @return объект {@link AuthenticationResponseDTO}, содержащий JWT токен.
     * @throws Exception если произошла ошибка при аутентификации.
     */
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) throws Exception;

    /**
     * Регистрация нового пользователя в системе.
     * После успешной регистрации пользователь может использовать свои данные для входа.
     *
     * @param user объект пользователя, который будет зарегистрирован.
     * @return строка с подтверждением успешной регистрации.
     */
    String register(User user);
}
