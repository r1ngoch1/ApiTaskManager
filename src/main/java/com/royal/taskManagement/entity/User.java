package com.royal.taskManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Сущность для представления пользователя в системе.
 * Этот класс содержит информацию о пользователе, включая его электронную почту, пароль и роли.
 * Пользователь может иметь одну или несколько ролей, которые определяют его права в системе.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Электронная почта пользователя.
     * Должна соответствовать формату email.
     */
    @Email(message = "Неправильный формат ввода почты")
    private String email;

    /**
     * Пароль пользователя.
     * Не может быть null.
     */
    @NotNull(message = "Пароль не может быть пустым")
    private String password;

    /**
     * Роли пользователя.
     * Один пользователь может иметь несколько ролей.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles;

    /**
     * Конструктор без параметров.
     * Используется для создания пустого объекта пользователя.
     */
    public User() {
        this.roles = new ArrayList<>();
    }

    /**
     * Конструктор с параметрами.
     * Создает пользователя с заданными ролями.
     *
     * @param roles коллекция ролей пользователя.
     */
    public User(Collection<Role> roles) {
        this.roles = new ArrayList<>(roles);
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


    public Collection<Role> getRoles() {
        return roles;
    }


    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
