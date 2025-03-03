package com.royal.taskManagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Сущность комментария, который привязан к задаче и написан автором.
 */
@Entity
@Schema(description = "Сущность комментария, который привязан к задаче и написан пользователем.")
public class Comment {

    /**
     * Идентификатор комментария.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор комментария", example = "1")
    private Long id;

    /**
     * Текст комментария.
     * Не может быть пустым и не может превышать 500 символов.
     */
    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(max = 500, message = "Комментарий должен быть менее 500 символов")
    @Schema(description = "Текст комментария. Максимальная длина 500 символов.", example = "Это важный комментарий к задаче.")
    private String text;

    /**
     * Задача, к которой привязан комментарий.
     * Связь с сущностью Task через отношение многие к одному.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "Задача, к которой привязан комментарий (сущность Task).")
    private Task task;

    /**
     * Автор комментария.
     * Связь с сущностью User через отношение многие к одному.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "Пользователь, оставивший комментарий (сущность User).")
    private User author;

    /**
     * Конструктор без параметров.
     * Используется для создания пустого объекта комментария.
     */
    public Comment() {
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


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }


    public Task getTask() {
        return task;
    }


    public void setTask(Task task) {
        this.task = task;
    }


    public User getAuthor() {
        return author;
    }


    public void setAuthor(User author) {
        this.author = author;
    }
}
