package com.royal.taskManagement.controller;

import com.royal.taskManagement.dto.TaskDTO;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.service.TaskService;
import com.royal.taskManagement.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


/**
 * Контроллер для управления задачами.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param taskService            сервис для работы с задачами
     * @param userDetailsServiceImpl сервис для получения информации о пользователях
     */
    @Autowired
    public TaskController(TaskService taskService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.taskService = taskService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    /**
     * Создание новой задачи.
     *
     * @param taskDTO     данные новой задачи
     * @param userDetails данные аутентифицированного пользователя
     * @return созданная задача
     */
    @Operation(
            summary = "Создание новой задачи",
            description = "Создает новую задачу и привязывает её к автору (пользователю)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно создана",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка создания задачи",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody @Parameter(description = "Данные новой задачи") TaskDTO taskDTO,
                                        @AuthenticationPrincipal @Parameter(description = "Аутентифицированный пользователь") UserDetails userDetails) {
        try {
            User author = userDetailsServiceImpl.findUserFromPrincipal(userDetails);
            TaskDTO createdTask = taskService.createTask(taskDTO, author);
            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            LOGGER.error("Ошибка создания задачи: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка создания задачи: " + e.getMessage());
        }
    }

    /**
     * Получение задачи по ID.
     *
     * @param id          идентификатор задачи
     * @param userDetails данные аутентифицированного пользователя
     * @return найденная задача
     */
    @Operation(
            summary = "Получение задачи по ID",
            description = "Получает задачу по уникальному идентификатору задачи."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно получена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка получения задачи",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id, @AuthenticationPrincipal @Parameter(description = "Аутентифицированный пользователь") UserDetails userDetails) {
        try {
            User user = userDetailsServiceImpl.findUserFromPrincipal(userDetails);
            TaskDTO taskDTO = taskService.getTaskById(id, user);
            return ResponseEntity.ok(taskDTO);
        } catch (Exception e) {
            LOGGER.error("Ошибка при выборе задачи по id: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выборе задачи по id: " + e.getMessage());
        }
    }

    /**
     * Обновление существующей задачи.
     *
     * @param id          идентификатор задачи
     * @param taskDTO     обновленные данные задачи
     * @param userDetails данные аутентифицированного пользователя
     * @return обновленная задача
     */
    @Operation(
            summary = "Обновление задачи",
            description = "Обновляет существующую задачу по ID с новыми данными."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка обновления задачи",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody @Parameter(description = "Обновленные данные задачи") TaskDTO taskDTO,
                                        @AuthenticationPrincipal @Parameter(description = "Аутентифицированный пользователь") UserDetails userDetails) {
        try {
            User user = userDetailsServiceImpl.findUserFromPrincipal(userDetails);
            TaskDTO updatedTask = taskService.updateTask(id, taskDTO, user);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            LOGGER.error("Ошибка обновления задачи: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обновления задачи: " + e.getMessage());
        }
    }

    /**
     * Удаление задачи по ID.
     *
     * @param id          идентификатор задачи
     * @param userDetails данные аутентифицированного пользователя
     * @return статус удаления
     */
    @Operation(
            summary = "Удаление задачи по ID",
            description = "Удаляет задачу по указанному ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "500", description = "Ошибка удаления задачи",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, @AuthenticationPrincipal @Parameter(description = "Аутентифицированный пользователь") UserDetails userDetails) {
        try {
            User user = userDetailsServiceImpl.findUserFromPrincipal(userDetails);
            taskService.deleteTask(id, user);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOGGER.error("Ошибка удаления задачи: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка удаления задачи: " + e.getMessage());
        }
    }

    /**
     * Получение задач пользователя (автор или исполнитель).
     *
     * @param userDetails данные аутентифицированного пользователя
     * @param pageable    параметры пагинации
     * @return список задач
     */
    @Operation(
            summary = "Получение задач пользователя",
            description = "Получает список задач для указанного пользователя (автор или исполнитель)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список задач успешно получен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка получения списка задач",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getTasksByAuthorOrAssignee(@AuthenticationPrincipal @Parameter(description = "Аутентифицированный пользователь") UserDetails userDetails,
                                                        Pageable pageable) {
        try {
            User user = userDetailsServiceImpl.findUserFromPrincipal(userDetails);
            Page<TaskDTO> tasks = taskService.getTasksByAuthorOrAssignee(user, user, pageable);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            LOGGER.error("Ошибка при выборке задач автором или исполнителем: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выборке задач автором или исполнителем: " + e.getMessage());
        }
    }
}
