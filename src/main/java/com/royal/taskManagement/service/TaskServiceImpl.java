package com.royal.taskManagement.service;

import com.royal.taskManagement.dto.CommentDTO;
import com.royal.taskManagement.dto.TaskDTO;
import com.royal.taskManagement.entity.Comment;
import com.royal.taskManagement.entity.Task;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.entity.enums.TaskPriority;
import com.royal.taskManagement.entity.enums.TaskStatus;
import com.royal.taskManagement.exception.CustomServiceException;
import com.royal.taskManagement.repository.TaskRepository;
import com.royal.taskManagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, User author) {
        try {
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setStatus(taskDTO.getStatus());
            task.setPriority(taskDTO.getPriority());
            task.setAuthor(author);

            if (taskDTO.getAssigneeId() != null) {
                User assignee = userRepository.findById(taskDTO.getAssigneeId())
                        .orElseThrow(() -> new CustomServiceException("Исполнитель не найден"));
                task.setAssignee(assignee);
            }

            Task savedTask = taskRepository.save(task);
            return convertToDTO(savedTask);
        } catch (Exception e) {
            logger.error("Ошибка создания задачи: {}", e.getMessage());
            throw new CustomServiceException("Ошибка при создании задачи", e);
        }
    }

    @Override
    public TaskDTO getTaskById(Long id, User currentUser) {
        try {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new CustomServiceException("Задача не найдена"));

            if (!isUserAuthorizedToViewTask(task, currentUser)) {
                throw new CustomServiceException("Вы не имеете роли для просмотра этой задачи");
            }

            return convertToDTO(task);
        } catch (Exception e) {
            logger.error("Ошибка при выборе задачи по id {}: {}", id, e.getMessage());
            throw new CustomServiceException("Ошибка при выборе задачи по id", e);
        }
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO, User user) {
        try {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new CustomServiceException("Задача не найдена"));

            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setStatus(taskDTO.getStatus());
            task.setPriority(taskDTO.getPriority());

            if (taskDTO.getAssigneeId() != null) {
                User assignee = userRepository.findById(taskDTO.getAssigneeId())
                        .orElseThrow(() -> new CustomServiceException("Исполнитель не найден"));
                task.setAssignee(assignee);
            }

            Task updatedTask = taskRepository.save(task);
            return convertToDTO(updatedTask);
        } catch (Exception e) {
            logger.error("Ошибка обновления задачи {}: {}", id, e.getMessage());
            throw new CustomServiceException("Ошибка обновления задачи", e);
        }
    }

    @Override
    public void deleteTask(Long id, User user) {
        try {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new CustomServiceException("Задача не найдена"));
            taskRepository.delete(task);
        } catch (Exception e) {
            logger.error("Error deleting task {}: {}", id, e.getMessage());
            throw new CustomServiceException("Failed to delete task", e);
        }
    }

    @Override
    public TaskDTO assignTask(Long id, Long assigneeId, User user) {
        try {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new CustomServiceException("Задача не найдена"));

            User assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new CustomServiceException("Исполнитель не найден"));
            task.setAssignee(assignee);

            Task updatedTask = taskRepository.save(task);
            return convertToDTO(updatedTask);
        } catch (Exception e) {
            logger.error("Ошибка при назначении задачи {}: {}", id, e.getMessage());
            throw new CustomServiceException("Ошибка при назначении задачи", e);
        }
    }

    @Override
    public TaskDTO updateTaskStatus(Long id, TaskStatus status, User user) {
        try {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new CustomServiceException("Задача не найдена"));
            task.setStatus(status);

            Task updatedTask = taskRepository.save(task);
            return convertToDTO(updatedTask);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении статуса задачи {}: {}", id, e.getMessage());
            throw new CustomServiceException("Ошибка при обновлении статуса задачи", e);
        }
    }

    @Override
    public TaskDTO updateTaskPriority(Long id, TaskPriority priority, User user) {
        try {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new CustomServiceException("Задача не найдена"));
            task.setPriority(priority);

            Task updatedTask = taskRepository.save(task);
            return convertToDTO(updatedTask);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении приоритета задачи {}: {}", id, e.getMessage());
            throw new CustomServiceException("Ошибка при обновлении приоритета задачи", e);
        }
    }

    @Override
    public Page<TaskDTO> getTasksByAuthor(User author, Pageable pageable) {
        return taskRepository.findByAuthor(author, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<TaskDTO> getTasksByAssignee(User assignee, Pageable pageable) {
        return taskRepository.findByAssignee(assignee, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<TaskDTO> getTasksByAuthorOrAssignee(User author, User assignee, Pageable pageable) {
        if (author.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
            return taskRepository.findAll(pageable).map(this::convertToDTO);
        }

        return taskRepository.findByAuthorOrAssignee(author, assignee, pageable).map(this::convertToDTO);
    }

    @Override
    public TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setAuthorId(task.getAuthor().getId());
        dto.setAssigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null);

        List<CommentDTO> commentDTOs = task.getComments().stream()
                .map(this::convertCommentToDTO)
                .toList();
        dto.setComments(commentDTOs);

        return dto;
    }

    public CommentDTO convertCommentToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setTaskId(comment.getTask().getId());
        dto.setAuthorId(comment.getAuthor().getId());
        return dto;
    }

    private boolean isUserAuthorizedToViewTask(Task task, User currentUser) {
        return currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")) ||
                currentUser.getId().equals(task.getAuthor().getId()) ||
                (task.getAssignee() != null && currentUser.getId().equals(task.getAssignee().getId()));
    }
}
