package com.royal.taskManagement.repository;

import com.royal.taskManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * Этот интерфейс расширяет {@link JpaRepository} и предоставляет методы для работы с пользователями в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по его электронной почте.
     *
     * @param email электронная почта пользователя.
     * @return {@link Optional} с найденным пользователем, если он существует, иначе пустой {@link Optional}.
     */
    Optional<User> findByEmail(String email);

    /**
     * Проверяет, существует ли пользователь с указанной электронной почтой.
     *
     * @param email электронная почта пользователя.
     * @return true, если пользователь с такой электронной почтой существует, иначе false.
     */
    boolean existsByEmail(String email);
}
