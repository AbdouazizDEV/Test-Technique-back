// src/main/java/com/gs1/articlemanager/application/usecase/auth/RegisterUseCase.java
package com.gs1.articlemanager.application.usecase.auth;

import com.gs1.articlemanager.application.dto.request.RegisterRequest;
import com.gs1.articlemanager.application.dto.response.AuthResponse;
import com.gs1.articlemanager.application.dto.response.UserResponse;
import com.gs1.articlemanager.domain.model.Role;
import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.domain.repository.UserRepository;
import com.gs1.articlemanager.domain.service.UserDomainService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class RegisterUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDomainService userDomainService;

    public RegisterUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDomainService = userDomainService;
    }

    public AuthResponse execute(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_MEMBER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userDomainService.validateUser(user);
        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getEmail(),
            savedUser.getRole().name()
        );

        return new AuthResponse(null, userResponse);
    }
}
