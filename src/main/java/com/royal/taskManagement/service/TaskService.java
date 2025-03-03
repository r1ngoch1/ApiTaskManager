package com.royal.taskManagement.service;

import com.royal.taskManagement.dto.TaskDTO;
import com.royal.taskManagement.entity.Task;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.entity.enums.TaskPriority;
import com.royal.taskManagement.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Сервис для работы с задачами.
 * Предоставляет методы для создания, обновления, удаления задач,
 * а также для получения информации о задачах по различным критериям.
 */
public interface TaskService {

    /**
     * Создает новую задачу.
     *
     * @param taskDTO объект DTO, содержащий информацию о задаче.
     * @param author  пользователь, создающий задачу.
     * @return объект DTO, представляющий созданную задачу.
     */
    TaskDTO createTask(TaskDTO taskDTO, User author);

    /**
     * Получает задачу по ID.
     *
     * @param id   ID задачи.
     * @param user пользователь, запрашивающий задачу.
     * @return объект DTO с информацией о задаче.
     */
    TaskDTO getTaskById(Long id, User user);

    /**
     * Обновляет информацию о задаче.
     *
     * @param id      ID задачи, которую нужно обновить.
     * @param taskDTO объект DTO с обновленными данными задачи.
     * @param user    пользователь, обновляющий задачу.
     * @return объект DTO, представляющий обновленную задачу.
     */
    TaskDTO updateTask(Long id, TaskDTO taskDTO, User user);

    /**
     * Удаляет задачу по ID.
     *
     * @param id   ID задачи, которую нужно удалить.
     * @param user пользователь, удаляющий задачу.
     */
    void deleteTask(Long id, User user);

    /**
     * Назначает задачу конкретному исполнителю.
     *
     * @param id         ID задачи.
     * @param assigneeId ID пользователя-исполнителя.
     * @param user       пользователь, назначающий исполнителя.
     * @return объект DTO, представляющий задачу с обновленным исполнителем.
     */
    TaskDTO assignTask(Long id, Long assigneeId, User user);

    /**
     * Обновляет статус задачи.
     *
     * @param id     ID задачи.
     * @param status новый статус задачи.
     * @param user   пользователь, обновляющий статус.
     * @return объект DTO, представляющий задачу с обновленным статусом.
     */
    TaskDTO updateTaskStatus(Long id, TaskStatus status, User user);

    /**
     * Обновляет приоритет задачи.
     *
     * @param id       ID задачи.
     * @param priority новый приоритет задачи.
     * @param user     пользователь, обновляющий приоритет.
     * @return объект DTO, представляющий задачу с обновленным приоритетом.
     */
    TaskDTO updateTaskPriority(Long id, TaskPriority priority, User user);

    /**
     * Получает задачи, автором которых является указанный пользователь.
     *
     * @param author   пользователь, который является автором задач.
     * @param pageable параметры для постраничного отображения задач.
     * @return страница задач, созданных указанным пользователем.
     */
    Page<TaskDTO> getTasksByAuthor(User author, Pageable pageable);

    /**
     * Получает задачи, назначенные указанному пользователю.
     *
     * @param assignee пользователь, которому назначены задачи.
     * @param pageable параметры для постраничного отображения задач.
     * @return страница задач, назначенных указанному пользователю.
     */
    Page<TaskDTO> getTasksByAssignee(User assignee, Pageable pageable);

    /**
     * Получает задачи, автором или исполнителем которых является указанный пользователь.
     *
     * @param author   пользователь, который является автором задач.
     * @param assignee пользователь, которому назначены задачи.
     * @param pageable параметры для постраничного отображения задач.
     * @return страница задач, где пользователь является автором или исполнителем.
     */
    Page<TaskDTO> getTasksByAuthorOrAssignee(User author, User assignee, Pageable pageable);

    /**
     * Преобразует сущность задачи в объект DTO.
     *
     * @param task сущность задачи.
     * @return объект DTO, представляющий задачу.
     */
    TaskDTO convertToDTO(Task task);
}
