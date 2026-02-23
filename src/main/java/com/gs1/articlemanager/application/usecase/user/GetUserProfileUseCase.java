// src/main/java/com/gs1/articlemanager/application/usecase/user/GetUserProfileUseCase.java
package com.gs1.articlemanager.application.usecase.user;

import com.gs1.articlemanager.application.dto.response.UserResponse;
import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class GetUserProfileUseCase {
    private final UserRepository userRepository;

    public GetUserProfileUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse execute(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }

        User user = userOpt.get();
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole().name()
        );
    }
}
