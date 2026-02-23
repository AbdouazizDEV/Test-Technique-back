// src/main/java/com/gs1/articlemanager/interfaces/mapper/UserDtoMapper.java
package com.gs1.articlemanager.interfaces.mapper;

import com.gs1.articlemanager.application.dto.response.UserResponse;
import com.gs1.articlemanager.domain.model.User;

public class UserDtoMapper {
    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole() != null ? user.getRole().name() : null
        );
    }
}
