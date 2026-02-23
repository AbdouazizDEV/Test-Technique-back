// src/main/java/com/gs1/articlemanager/domain/service/UserDomainService.java
package com.gs1.articlemanager.domain.service;

import com.gs1.articlemanager.domain.model.User;
import java.util.regex.Pattern;

public class UserDomainService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );

    public void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'utilisateur ne peut pas être vide");
        }
        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("L'email doit être valide");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
        }
    }

    public boolean isAdmin(User user) {
        return user != null && user.getRole() == com.gs1.articlemanager.domain.model.Role.ROLE_ADMIN;
    }
}
