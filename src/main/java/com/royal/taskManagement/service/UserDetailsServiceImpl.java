package com.royal.taskManagement.service;

import com.royal.taskManagement.entity.Role;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.repository.RoleRepository;
import com.royal.taskManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.info("Поиск пользователя с почтой: {}", email);

        return userRepository.findByEmail(email)
                .map(user -> {
                    LOGGER.info("Пользователь найден: {}", user.getEmail());
                    LOGGER.debug("Роли пользователя: {}", user.getRoles());
                    return new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            user.getRoles()
                    );
                })
                .orElseThrow(() -> {
                    LOGGER.error("Пользователя с такой почтой не найден: {}", email);
                    return new UsernameNotFoundException("Пользователя с такой почтой не найден: " + email);
                });
    }

    @Transactional
    public void addAdminRole(Long userId) {
        LOGGER.info("Добавление роли ADMIN пользователю: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("Пользователь не найден с ID: {}", userId);
                    return new RuntimeException("Пользователь не найден");
                });

        Role adminRole = roleRepository.findByName("ADMIN");


        if (user.getRoles().contains(adminRole)) {
            LOGGER.warn("Пользователь {} уже имеет ADMIN роль", userId);
        } else {
            user.getRoles().add(adminRole);
            userRepository.save(user);
            LOGGER.info("ADMIN роль была добавлена пользователю {}", userId);
        }
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователя с такой почтой не найден: " + email));
    }

    public User findUserFromPrincipal(UserDetails userDetails) {
        String email = userDetails.getUsername();
        return findUserByEmail(email);
    }
}
