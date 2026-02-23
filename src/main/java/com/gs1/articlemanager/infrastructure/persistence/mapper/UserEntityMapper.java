// src/main/java/com/gs1/articlemanager/infrastructure/persistence/mapper/UserEntityMapper.java
package com.gs1.articlemanager.infrastructure.persistence.mapper;

import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.infrastructure.persistence.entity.UserEntity;

public class UserEntityMapper {
    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getRole(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setRole(domain.getRole());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
