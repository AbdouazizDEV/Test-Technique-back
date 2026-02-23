// src/main/java/com/gs1/articlemanager/application/usecase/user/UpdateUserProfileUseCase.java
package com.gs1.articlemanager.application.usecase.user;

import com.gs1.articlemanager.application.dto.request.UpdateUserRequest;
import com.gs1.articlemanager.application.dto.response.UserResponse;
import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.domain.repository.UserRepository;
import com.gs1.articlemanager.domain.service.UserDomainService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

public class UpdateUserProfileUseCase {
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    public UpdateUserProfileUseCase(UserRepository userRepository, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }

    public UserResponse execute(Long userId, UpdateUserRequest request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }

        User user = userOpt.get();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUpdatedAt(LocalDateTime.now());

        userDomainService.validateUser(user);
        User updatedUser = userRepository.save(user);

        return new UserResponse(
            updatedUser.getId(),
            updatedUser.getName(),
            updatedUser.getEmail(),
            updatedUser.getRole().name()
        );
    }
}
