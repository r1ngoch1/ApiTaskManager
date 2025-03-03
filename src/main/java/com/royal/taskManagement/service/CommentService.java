package com.royal.taskManagement.service;

import com.royal.taskManagement.dto.CommentDTO;
import com.royal.taskManagement.entity.Comment;
import com.royal.taskManagement.entity.Task;
import com.royal.taskManagement.entity.User;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Сервис для работы с комментариями к задачам.
 * Предоставляет методы для добавления комментариев, проверки прав пользователя на добавление комментариев
 * и получения комментариев для задач.
 */
public interface CommentService {

    /**
     * Добавляет комментарий к задаче.
     * Метод транзакционный, чтобы обеспечить сохранение комментария в базе данных.
     *
     * @param taskId      идентификатор задачи, к которой добавляется комментарий.
     * @param comment     комментарий, который будет добавлен.
     * @param currentUser текущий пользователь, который добавляет комментарий.
     * @return объект {@link CommentDTO}, представляющий добавленный комментарий.
     */
    @Transactional
    CommentDTO addCommentToTask(Long taskId, Comment comment, User currentUser);

    /**
     * Проверяет, имеет ли пользователь право добавлять комментарии к задаче.
     *
     * @param task        задача, к которой проверяется право комментирования.
     * @param currentUser текущий пользователь, для которого проверяется право.
     * @return true, если пользователь имеет право добавлять комментарии, иначе false.
     */
    boolean isUserAuthorizedToComment(Task task, User currentUser);

    /**
     * Получает список комментариев для задачи по ее идентификатору.
     *
     * @param taskId      идентификатор задачи, для которой необходимо получить комментарии.
     * @param currentUser текущий пользователь, чьи права будут учитываться при получении комментариев.
     * @return список объектов {@link CommentDTO}, представляющих комментарии к задаче.
     */
    List<CommentDTO> getCommentsByTaskId(Long taskId, User currentUser);
}
