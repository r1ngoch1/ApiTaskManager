package com.royal.taskManagement.repository;

import com.royal.taskManagement.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Comment}.
 * Этот интерфейс расширяет {@link JpaRepository} и предоставляет методы для работы с комментариями в базе данных.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Находит список комментариев по идентификатору задачи.
     *
     * @param taskId идентификатор задачи, для которой нужно получить комментарии.
     * @return список комментариев, связанных с указанной задачей.
     */
    List<Comment> findByTaskId(Long taskId);
}
