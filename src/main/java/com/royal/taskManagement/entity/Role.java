package com.royal.taskManagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Сущность для представления роли пользователя в системе.
 * Этот класс реализует интерфейс {@link GrantedAuthority}, который используется для
 * предоставления информации о роли пользователя в контексте аутентификации и авторизации.
 */
@Entity
@Schema(description = "Сущность, представляющая роль пользователя в системе.")
public class Role implements GrantedAuthority {

    /**
     * Уникальный идентификатор роли.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор роли.", example = "1")
    private Long id;

    /**
     * Название роли, например, "ADMIN" или "USER".
     */
    @Schema(description = "Название роли в системе.", example = "ADMIN")
    private String name;

    /**
     * Конструктор без параметров.
     * Используется для создания пустого объекта роли.
     */
    public Role() {
    }

    /**
     * Получить строковое представление роли с префиксом "ROLE_",
     * как требуется для работы с Spring Security.
     *
     * @return строковое представление роли с префиксом "ROLE_".
     */
    @Override
    @Schema(description = "Возвращает имя роли с префиксом 'ROLE_', как требует Spring Security.", example = "ROLE_ADMIN")
    public String getAuthority() {
        return "ROLE_" + name;
    }

    /**
     * Геттеры и сеттеры
     */
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
