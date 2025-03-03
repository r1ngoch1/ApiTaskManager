package com.royal.taskManagement.service;

import com.royal.taskManagement.entity.Role;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.repository.RoleRepository;
import com.royal.taskManagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup roles
        userRole = new Role();
        userRole.setId(1L);
        userRole.setName("USER");

        adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName("ADMIN");

        // Setup user
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
    }

    @Test
    void loadUserByUsername_Success() {
        // Arrange
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());


        verify(userRepository, times(1)).findByEmail("user@example.com");
    }



    @Test
    void addAdminRole_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(adminRole);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userDetailsService.addAdminRole(1L);

        // Assert
        verify(userRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).findByName("ADMIN");
        verify(userRepository, times(1)).save(user);
        assertTrue(user.getRoles().contains(adminRole));
    }

    @Test
    void addAdminRole_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userDetailsService.addAdminRole(999L));
        verify(userRepository, times(1)).findById(999L);
        verify(roleRepository, never()).findByName(anyString());
        verify(userRepository, never()).save(any(User.class));
    }





    @Test
    void findUserByEmail_Success() {
        // Arrange
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Act
        User result = userDetailsService.findUserByEmail("user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    void findUserByEmail_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userDetailsService.findUserByEmail("nonexistent@example.com"));
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void findUserFromPrincipal_Success() {
        // Arrange
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("user@example.com")
                .password("password")
                .authorities("USER")
                .build();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Act
        User result = userDetailsService.findUserFromPrincipal(userDetails);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    void findUserFromPrincipal_UserNotFound_ThrowsException() {
        // Arrange
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("nonexistent@example.com")
                .password("password")
                .authorities("USER")
                .build();
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userDetailsService.findUserFromPrincipal(userDetails));
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}