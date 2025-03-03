package com.royal.taskManagement.dto;

import com.royal.taskManagement.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Этот класс используется для передачи данных о комментарии, включая текст, авторство и привязку к задаче.
 */
@Schema(description = "DTO для комментария, включая текст, авторство и привязку к задаче.")
public class CommentDTO {

    /**
     * Идентификатор комментария.
     */
    @Schema(description = "Идентификатор комментария", example = "1")
    private Long id;

    /**
     * Текст комментария.
     */
    @Schema(description = "Текст комментария", example = "Это пример комментария.")
    private String text;

    /**
     * Идентификатор задачи, к которой привязан комментарий.
     */
    @Schema(description = "Идентификатор задачи, к которой привязан комментарий", example = "100")
    private Long taskId;

    /**
     * Идентификатор автора комментария.
     */
    @Schema(description = "Идентификатор автора комментария", example = "50")
    private Long authorId;

    /**
     * Конструктор без параметров.
     * Используется для создания пустого объекта DTO.
     */
    public CommentDTO() {

    }

    /**
     * Конструктор, который создает DTO из сущности Comment.
     *
     * @param comment сущность Comment, из которой будет сконструирован DTO.
     */
    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.authorId = comment.getAuthor() != null ? comment.getAuthor().getId() : null;
    }

    /**
     * Геттеры и сеттеры
     */
    public Long getTaskId() {
        return taskId;
    }


    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }


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


    public Long getAuthorId() {
        return authorId;
    }


    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
