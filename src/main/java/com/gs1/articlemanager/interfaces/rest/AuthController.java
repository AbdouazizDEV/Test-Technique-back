// src/main/java/com/gs1/articlemanager/interfaces/rest/AuthController.java
package com.gs1.articlemanager.interfaces.rest;

import com.gs1.articlemanager.application.dto.request.LoginRequest;
import com.gs1.articlemanager.application.dto.request.RegisterRequest;
import com.gs1.articlemanager.application.dto.response.AuthResponse;
import com.gs1.articlemanager.application.usecase.auth.LoginUseCase;
import com.gs1.articlemanager.application.usecase.auth.RegisterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Endpoints pour l'authentification")
public class AuthController {
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUseCase registerUseCase, LoginUseCase loginUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouvel utilisateur")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = registerUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Inscription réussie", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion d'un utilisateur")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = loginUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success("Connexion réussie", response));
    }
}
