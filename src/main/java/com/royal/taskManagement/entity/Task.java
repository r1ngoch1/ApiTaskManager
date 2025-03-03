package com.royal.taskManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.royal.taskManagement.entity.enums.TaskPriority;
import com.royal.taskManagement.entity.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность для представления задачи в системе.
 * Этот класс содержит информацию о задаче, такую как название, описание, статус, приоритет,
 * автор задачи, исполнитель и связанные с задачей комментарии.
 */
@Entity
@Schema(description = "Сущность задачи, содержащая информацию о названии, описании, статусе, приоритете, авторе, исполнителе и комментариях.")
public class Task {

    /**
     * Идентификатор задачи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор задачи.", example = "1")
    private Long id;

    /**
     * Заголовок задачи.
     * Не может быть пустым и не может превышать 100 символов.
     */
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(max = 100, message = "Заголовок должен быть менее 100 символов")
    @Schema(description = "Заголовок задачи.", example = "Разработать API для управления задачами")
    private String title;

    /**
     * Описание задачи.
     * Не может превышать 1000 символов.
     */
    @Size(max = 1000, message = "Описание должно быть меньше 1000 символов")
    @Schema(description = "Описание задачи.", example = "Разработать REST API с возможностью управления задачами.")
    private String description;

    /**
     * Статус задачи.
     * Не может быть null.
     */
    @NotNull(message = "Статус не может быть null")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Статус задачи.", example = "IN_PROGRESS")
    private TaskStatus status;

    /**
     * Приоритет задачи.
     * Не может быть null.
     */
    @NotNull(message = "Приоритет не может быть null")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Приоритет задачи.", example = "HIGH")
    private TaskPriority priority;

    /**
     * Автор задачи.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Schema(description = "Автор задачи (пользователь, создавший задачу).")
    private User author;

    /**
     * Исполнитель задачи.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Schema(description = "Исполнитель задачи (назначенный пользователь).")
    private User assignee;

    /**
     * Список комментариев, связанных с задачей.
     */
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Schema(description = "Список комментариев, связанных с задачей.")
    private List<Comment> comments = new ArrayList<>();

    /**
     * Конструктор без параметров.
     * Используется для создания пустого объекта задачи.
     */
    public Task() {

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


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public TaskStatus getStatus() {
        return status;
    }


    public void setStatus(TaskStatus status) {
        this.status = status;
    }


    public TaskPriority getPriority() {
        return priority;
    }


    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }


    public User getAuthor() {
        return author;
    }


    public void setAuthor(User author) {
        this.author = author;
    }


    public User getAssignee() {
        return assignee;
    }


    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }


    public List<Comment> getComments() {
        return comments;
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
