package com.royal.taskManagement.repository;

import com.royal.taskManagement.entity.Comment;
import com.royal.taskManagement.entity.Task;
import com.royal.taskManagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Task}.
 * Этот интерфейс расширяет {@link JpaRepository} и предоставляет методы для работы с задачами в базе данных.
 * Также включает запросы для извлечения комментариев, связанных с задачами.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Находит страницы задач, автором которых является указанный пользователь.
     *
     * @param author   пользователь, являющийся автором задачи.
     * @param pageable параметры пагинации.
     * @return страница задач, автором которых является указанный пользователь.
     */
    Page<Task> findByAuthor(User author, Pageable pageable);

    /**
     * Находит страницы задач, назначенных на указанного пользователя.
     *
     * @param assignee пользователь, на которого назначены задачи.
     * @param pageable параметры пагинации.
     * @return страница задач, назначенных на указанного пользователя.
     */
    Page<Task> findByAssignee(User assignee, Pageable pageable);

    /**
     * Находит страницы задач, автором которых является один из указанных пользователей
     * или на которых один из них является исполнителем.
     *
     * @param author   пользователь, являющийся автором задачи.
     * @param assignee пользователь, на которого назначены задачи.
     * @param pageable параметры пагинации.
     * @return страница задач, соответствующих критериям.
     */
    Page<Task> findByAuthorOrAssignee(User author, User assignee, Pageable pageable);

    /**
     * Находит комментарии, связанные с задачей по ее идентификатору.
     *
     * @param taskId идентификатор задачи.
     * @return список комментариев, связанных с указанной задачей.
     */
    @Query("SELECT c FROM Comment c WHERE c.task.id = :taskId")
    List<Comment> findCommentsByTaskId(@Param("taskId") Long taskId);
}
