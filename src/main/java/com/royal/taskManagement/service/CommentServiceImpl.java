package com.royal.taskManagement.service;

import com.royal.taskManagement.dto.CommentDTO;
import com.royal.taskManagement.entity.Comment;
import com.royal.taskManagement.entity.Task;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.exception.CustomServiceException;
import com.royal.taskManagement.repository.CommentRepository;
import com.royal.taskManagement.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public CommentDTO addCommentToTask(Long taskId, Comment comment, User currentUser) {
        try {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new CustomServiceException("Task not found"));

            if (!isUserAuthorizedToComment(task, currentUser)) {
                throw new CustomServiceException("Only the author, assignee, or admin can add comments");
            }

            comment.setAuthor(currentUser);
            comment.setTask(task);

            Comment savedComment = commentRepository.save(comment);
            return new CommentDTO(savedComment);
        } catch (Exception e) {
            LOGGER.error("Error adding comment to task {}: {}", taskId, e.getMessage());
            throw new CustomServiceException("Failed to add comment", e);
        }
    }

    @Override
    public List<CommentDTO> getCommentsByTaskId(Long taskId, User currentUser) {
        try {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new CustomServiceException("Task not found"));

            if (!isUserAuthorizedToComment(task, currentUser)) {
                throw new CustomServiceException("Only the admin or assignee can view comments");
            }

            List<Comment> comments = commentRepository.findByTaskId(taskId);
            return comments.stream().map(CommentDTO::new).toList();
        } catch (Exception e) {
            LOGGER.error("Ошибка получения комментариев к задаче {}: {}", taskId, e.getMessage());
            throw new CustomServiceException("Ошибка получения комментариев к задаче", e);
        }
    }

    @Override
    public boolean isUserAuthorizedToComment(Task task, User currentUser) {
        return currentUser.getId().equals(task.getAuthor().getId()) ||
                (task.getAssignee() != null && currentUser.getId().equals(task.getAssignee().getId())) ||
                currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
    }
}
