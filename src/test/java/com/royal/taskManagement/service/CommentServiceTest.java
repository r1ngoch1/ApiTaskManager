package com.royal.taskManagement.service;

import com.royal.taskManagement.dto.CommentDTO;
import com.royal.taskManagement.entity.Comment;
import com.royal.taskManagement.entity.Role;
import com.royal.taskManagement.entity.Task;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.repository.CommentRepository;
import com.royal.taskManagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User author;
    private User assignee;
    private User admin;
    private User unauthorized;
    private Task task;
    private Comment comment;

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

        unauthorized = new User();
        unauthorized.setId(4L);
        unauthorized.setEmail("unauthorized@example.com");
        unauthorized.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        // Setup task
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setAuthor(author);
        task.setAssignee(assignee);

        // Setup comment
        comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        comment.setTask(task);
        comment.setAuthor(author);
    }

    @Test
    void addCommentToTask_AsAuthor_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentDTO result = commentService.addCommentToTask(1L, comment, author);

        // Assert
        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getAuthor().getId(), result.getAuthorId());

        verify(taskRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addCommentToTask_AsAssignee_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentDTO result = commentService.addCommentToTask(1L, comment, assignee);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addCommentToTask_AsAdmin_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentDTO result = commentService.addCommentToTask(1L, comment, admin);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addCommentToTask_Unauthorized_ThrowsException() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> commentService.addCommentToTask(1L, comment, unauthorized));
        verify(taskRepository, times(1)).findById(1L);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void getCommentsByTaskId_AsAuthor_Success() {
        // Arrange
        List<Comment> comments = Collections.singletonList(comment);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.findByTaskId(1L)).thenReturn(comments);

        // Act
        List<CommentDTO> result = commentService.getCommentsByTaskId(1L, author);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(comment.getId(), result.get(0).getId());
        assertEquals(comment.getText(), result.get(0).getText());

        verify(taskRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).findByTaskId(1L);
    }

    @Test
    void getCommentsByTaskId_AsAssignee_Success() {
        // Arrange
        List<Comment> comments = Collections.singletonList(comment);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.findByTaskId(1L)).thenReturn(comments);

        // Act
        List<CommentDTO> result = commentService.getCommentsByTaskId(1L, assignee);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(taskRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).findByTaskId(1L);
    }

    @Test
    void getCommentsByTaskId_AsAdmin_Success() {
        // Arrange
        List<Comment> comments = Collections.singletonList(comment);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.findByTaskId(1L)).thenReturn(comments);

        // Act
        List<CommentDTO> result = commentService.getCommentsByTaskId(1L, admin);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(taskRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).findByTaskId(1L);
    }

    @Test
    void getCommentsByTaskId_Unauthorized_ThrowsException() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> commentService.getCommentsByTaskId(1L, unauthorized));
        verify(taskRepository, times(1)).findById(1L);
        verify(commentRepository, never()).findByTaskId(anyLong());
    }

    @Test
    void isUserAuthorizedToComment_Author_ReturnsTrue() {
        // Act
        boolean result = commentService.isUserAuthorizedToComment(task, author);

        // Assert
        assertTrue(result);
    }

    @Test
    void isUserAuthorizedToComment_Assignee_ReturnsTrue() {
        // Act
        boolean result = commentService.isUserAuthorizedToComment(task, assignee);

        // Assert
        assertTrue(result);
    }

    @Test
    void isUserAuthorizedToComment_Admin_ReturnsTrue() {
        // Act
        boolean result = commentService.isUserAuthorizedToComment(task, admin);

        // Assert
        assertTrue(result);
    }

    @Test
    void isUserAuthorizedToComment_Unauthorized_ReturnsFalse() {
        // Act
        boolean result = commentService.isUserAuthorizedToComment(task, unauthorized);

        // Assert
        assertFalse(result);
    }
}