package com.royal.taskManagement.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Сервис для работы с деталями пользователя в контексте безопасности.
 * Предоставляет методы для загрузки пользователя по имени и назначения ролей.
 */
public interface UserDetailsService {

    /**
     * Загружает пользователя по его имени.
     *
     * @param username имя пользователя.
     * @return объект {@link UserDetails}, представляющий пользователя.
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Добавляет роль администратора пользователю.
     *
     * @param userId ID пользователя, которому нужно назначить роль администратора.
     */
    void addAdminRole(Long userId);
}
