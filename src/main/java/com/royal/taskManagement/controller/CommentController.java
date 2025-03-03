package com.royal.taskManagement.controller;

import com.royal.taskManagement.dto.CommentDTO;
import com.royal.taskManagement.entity.Comment;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.service.CommentService;
import com.royal.taskManagement.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления комментариями к задачам.
 */
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * Конструктор контроллера комментариев.
     *
     * @param commentService         сервис для работы с комментариями
     * @param userDetailsServiceImpl сервис для работы с данными пользователя
     */
    @Autowired
    public CommentController(CommentService commentService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.commentService = commentService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    /**
     * Добавляет комментарий к задаче.
     *
     * @param taskId      ID задачи
     * @param comment     объект комментария
     * @param userDetails данные аутентифицированного пользователя
     * @return созданный комментарий или ошибка
     */
    @Operation(
            summary = "Добавление комментария к задаче",
            description = "Добавляет новый комментарий к задаче с указанным ID. Требует аутентификацию пользователя."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка при добавлении комментария",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> addCommentToTask(
            @PathVariable Long taskId,
            @RequestBody @Parameter(description = "Комментарий, который необходимо добавить к задаче") Comment comment,
            @AuthenticationPrincipal @Parameter(description = "Аутентифицированный пользователь") UserDetails userDetails) {

        User currentUser = userDetailsServiceImpl.findUserFromPrincipal(userDetails);

        try {
            CommentDTO createdComment = commentService.addCommentToTask(taskId, comment, currentUser);
            return ResponseEntity.ok(createdComment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Получает список комментариев для указанной задачи.
     *
     * @param taskId      ID задачи
     * @param userDetails данные аутентифицированного пользователя
     * @return список комментариев или ошибка
     */
    @Operation(
            summary = "Получение комментариев к задаче",
            description = "Получает все комментарии для задачи с указанным ID. Требует аутентификацию пользователя."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка при получении комментариев",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getCommentsByTaskId(
            @PathVariable Long taskId,
            @AuthenticationPrincipal @Parameter(description = "Аутентифицированный пользователь") UserDetails userDetails) {

        User currentUser = userDetailsServiceImpl.findUserFromPrincipal(userDetails);

        try {
            List<CommentDTO> comments = commentService.getCommentsByTaskId(taskId, currentUser);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
