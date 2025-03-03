package com.royal.taskManagement.service;

import com.royal.taskManagement.dto.AuthenticationRequestDTO;
import com.royal.taskManagement.dto.AuthenticationResponseDTO;
import com.royal.taskManagement.entity.Role;
import com.royal.taskManagement.entity.User;
import com.royal.taskManagement.repository.RoleRepository;
import com.royal.taskManagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtService jwtUtil, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) throws Exception {
        LOGGER.info("Попыткак аутентифицировать пользователя: " + request.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            LOGGER.error("Неверное почта пользователя или пароль", e);
            throw new Exception("Неверное почта пользователя или пароль", e);
        } catch (Exception e) {
            LOGGER.error("Аутентификация провалена", e);
            throw e;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        LOGGER.info("JWT token сгенерирован для: " + request.getEmail());
        return new AuthenticationResponseDTO(jwt);
    }

    @Override
    public String register(User user) {
        if (userDetailsService.userExists(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с такой почтой уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        user.getRoles().add(userRole);
        userRepository.save(user);
        return "Пользователь успешно зарегистрирован";
    }
}
