package com.royal.taskManagement.dto;

import com.royal.taskManagement.entity.enums.TaskPriority;
import com.royal.taskManagement.entity.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Этот класс используется для передачи информации о задаче, включая заголовок, описание, статус,
 * приоритет, информацию об авторе, исполнителе и комментариях.
 */
@Schema(description = "DTO для задачи, включая заголовок, описание, статус, приоритет, информацию о пользователе и комментариях.")
public class TaskDTO {

    /**
     * Идентификатор задачи.
     */
    @Schema(description = "Идентификатор задачи", example = "1")
    private Long id;

    /**
     * Заголовок задачи.
     */
    @NotBlank(message = "Заголовок не может быть пустым.")
    @Size(max = 255, message = "Заголовок не может быть длиннее 255 символов.")
    @Schema(description = "Заголовок задачи", example = "Создание новой задачи")
    private String title;

    /**
     * Описание задачи.
     */
    @NotBlank(message = "Описание не может быть пустым.")
    @Schema(description = "Описание задачи", example = "Задача для создания нового API эндпоинта.")
    private String description;

    /**
     * Статус задачи.
     */
    @NotNull(message = "Статус задачи не может быть пустым.")
    @Schema(description = "Статус задачи", example = "IN_PROGRESS")
    private TaskStatus status;

    /**
     * Приоритет задачи.
     */
    @NotNull(message = "Приоритет задачи не может быть пустым.")
    @Schema(description = "Приоритет задачи", example = "HIGH")
    private TaskPriority priority;

    /**
     * Идентификатор автора задачи.
     */
    @NotNull(message = "Автор задачи должен быть указан.")
    @Schema(description = "Идентификатор автора задачи", example = "123")
    private Long authorId;

    /**
     * Идентификатор исполнителя задачи.
     */
    @NotNull(message = "Исполнитель задачи должен быть указан.")
    @Schema(description = "Идентификатор исполнителя задачи", example = "456")
    private Long assigneeId;

    /**
     * Список комментариев, привязанных к задаче.
     */
    @Schema(description = "Список комментариев, привязанных к задаче")
    private List<CommentDTO> comments;

    /**
     * Конструктор без параметров.
     * Используется для создания пустого объекта DTO.
     */
    public TaskDTO() {

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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}
