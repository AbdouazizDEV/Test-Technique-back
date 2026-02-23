// src/main/java/com/gs1/articlemanager/interfaces/rest/UserController.java
package com.gs1.articlemanager.interfaces.rest;

import com.gs1.articlemanager.application.dto.request.UpdateUserRequest;
import com.gs1.articlemanager.application.dto.response.UserResponse;
import com.gs1.articlemanager.application.usecase.user.GetUserProfileUseCase;
import com.gs1.articlemanager.application.usecase.user.UpdateUserProfileUseCase;
import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs", description = "Endpoints pour la gestion des utilisateurs")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final UserRepository userRepository;

    public UserController(GetUserProfileUseCase getUserProfileUseCase,
                        UpdateUserProfileUseCase updateUserProfileUseCase,
                        UserRepository userRepository) {
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    @Operation(summary = "Obtenir le profil de l'utilisateur connecté")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        UserResponse response = getUserProfileUseCase.execute(user.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/me")
    @Operation(summary = "Mettre à jour le profil de l'utilisateur connecté")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyProfile(
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        UserResponse response = updateUserProfileUseCase.execute(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profil mis à jour avec succès", response));
    }

    @GetMapping
    @Operation(summary = "Liste tous les utilisateurs (Admin uniquement)")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User currentUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (currentUser.getRole() != com.gs1.articlemanager.domain.model.Role.ROLE_ADMIN) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Accès refusé. Seuls les administrateurs peuvent accéder à cette ressource"));
        }

        List<UserResponse> users = userRepository.findAll().stream()
            .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole().name()))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID (Admin uniquement)")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable @Positive Long id,
            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User currentUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (currentUser.getRole() != com.gs1.articlemanager.domain.model.Role.ROLE_ADMIN) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Accès refusé. Seuls les administrateurs peuvent accéder à cette ressource"));
        }

        UserResponse response = getUserProfileUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
