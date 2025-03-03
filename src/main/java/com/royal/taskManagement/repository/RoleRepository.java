package com.royal.taskManagement.repository;

import com.royal.taskManagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link Role}.
 * Этот интерфейс расширяет {@link JpaRepository} и предоставляет методы для работы с ролями пользователей в базе данных.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по ее имени.
     *
     * @param name название роли.
     * @return роль с указанным именем, или null, если роль не найдена.
     */
    Role findByName(String name);
}
