package com.royal.taskManagement;

import com.royal.taskManagement.dto.TaskDTO;
import com.royal.taskManagement.entity.Role;
import com.royal.taskManagement.entity.Task;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.entity.enums.TaskPriority;
import com.royal.taskManagement.entity.enums.TaskStatus;
import com.royal.taskManagement.repository.TaskRepository;
import com.royal.taskManagement.repository.UserRepository;
import com.royal.taskManagement.service.CommentService;
import com.royal.taskManagement.service.CommentServiceImpl;
import com.royal.taskManagement.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private CommentServiceImpl commentServiceImpl;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User author;
    private User assignee;
    private User admin;
    private Task task;
    private TaskDTO taskDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup roles
        Role userRole = new Role();
        userRole.setName("USER");

        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        // Setup users
        author = new User();
        author.setId(1L);
        author.setEmail("author@example.com");
        author.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        assignee = new User();
        assignee.setId(2L);
        assignee.setEmail("assignee@example.com");
        assignee.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        admin = new User();
        admin.setId(3L);
        admin.setEmail("admin@example.com");
        admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));

        // Setup task
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(TaskPriority.MEDIUM);
        task.setAuthor(author);
        task.setAssignee(assignee);
        task.setComments(new ArrayList<>());

        // Setup taskDTO
        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setStatus(TaskStatus.PENDING);
        taskDTO.setPriority(TaskPriority.MEDIUM);
        taskDTO.setAuthorId(1L);
        taskDTO.setAssigneeId(2L);
        taskDTO.setComments(new ArrayList<>());

        // Setup pageable
        pageable = Pageable.unpaged();
    }

    @Test
    void createTask_Success() {
        // Arrange
        when(userRepository.findById(taskDTO.getAssigneeId())).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        TaskDTO result = taskService.createTask(taskDTO, author);

        // Assert
        assertNotNull(result);
        assertEquals(taskDTO.getTitle(), result.getTitle());
        assertEquals(taskDTO.getDescription(), result.getDescription());
        assertEquals(taskDTO.getStatus(), result.getStatus());
        assertEquals(taskDTO.getPriority(), result.getPriority());
        assertEquals(author.getId(), result.getAuthorId());
        assertEquals(assignee.getId(), result.getAssigneeId());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_NoAssignee_Success() {
        // Arrange
        taskDTO.setAssigneeId(null);
        task.setAssignee(null);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        TaskDTO result = taskService.createTask(taskDTO, author);

        // Assert
        assertNotNull(result);
        assertNull(result.getAssigneeId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTaskById_AuthorAccess_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        TaskDTO result = taskService.getTaskById(1L, author);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_AssigneeAccess_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        TaskDTO result = taskService.getTaskById(1L, assignee);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_AdminAccess_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        TaskDTO result = taskService.getTaskById(1L, admin);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_UnauthorizedAccess_ThrowsException() {
        // Arrange
        User unauthorized = new User();
        unauthorized.setId(4L);
        unauthorized.setEmail("unauthorized@example.com");
        unauthorized.setRoles(new HashSet<>(Collections.singletonList(new Role())));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L, unauthorized));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void updateTask_Success() {
        // Arrange
        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setStatus(TaskStatus.IN_PROGRESS);
        updateDTO.setPriority(TaskPriority.HIGH);
        updateDTO.setAssigneeId(2L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        TaskDTO result = taskService.updateTask(1L, updateDTO, author);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void deleteTask_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        // Act
        taskService.deleteTask(1L, author);

        // Assert
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void assignTask_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        TaskDTO result = taskService.assignTask(1L, 2L, author);

        // Assert
        assertNotNull(result);
        assertEquals(assignee.getId(), result.getAssigneeId());
        verify(taskRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTaskStatus_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        TaskDTO result = taskService.updateTaskStatus(1L, TaskStatus.COMPLETED, author);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTaskPriority_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        TaskDTO result = taskService.updateTaskPriority(1L, TaskPriority.HIGH, author);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTasksByAuthor_Success() {
        // Arrange
        List<Task> tasks = Collections.singletonList(task);
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findByAuthor(author, pageable)).thenReturn(taskPage);

        // Act
        Page<TaskDTO> result = taskService.getTasksByAuthor(author, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(taskRepository, times(1)).findByAuthor(author, pageable);
    }

    @Test
    void getTasksByAssignee_Success() {
        // Arrange
        List<Task> tasks = Collections.singletonList(task);
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findByAssignee(assignee, pageable)).thenReturn(taskPage);

        // Act
        Page<TaskDTO> result = taskService.getTasksByAssignee(assignee, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(taskRepository, times(1)).findByAssignee(assignee, pageable);
    }

    @Test
    void getTasksByAuthorOrAssignee_Admin_Success() {
        // Arrange
        List<Task> tasks = Collections.singletonList(task);
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        // Act
        Page<TaskDTO> result = taskService.getTasksByAuthorOrAssignee(admin, admin, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
    void getTasksByAuthorOrAssignee_NonAdmin_Success() {
        // Arrange
        List<Task> tasks = Collections.singletonList(task);
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findByAuthorOrAssignee(author, author, pageable)).thenReturn(taskPage);

        // Act
        Page<TaskDTO> result = taskService.getTasksByAuthorOrAssignee(author, author, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(taskRepository, times(1)).findByAuthorOrAssignee(author, author, pageable);
    }

    @Test
    void convertToDTO_Success() {
        // Act
        TaskDTO result = taskService.convertToDTO(task);

        // Assert
        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getStatus(), result.getStatus());
        assertEquals(task.getPriority(), result.getPriority());
        assertEquals(task.getAuthor().getId(), result.getAuthorId());
        assertEquals(task.getAssignee().getId(), result.getAssigneeId());
    }
}