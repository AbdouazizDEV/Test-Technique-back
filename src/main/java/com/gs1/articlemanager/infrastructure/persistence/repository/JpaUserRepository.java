// src/main/java/com/gs1/articlemanager/infrastructure/persistence/repository/JpaUserRepository.java
package com.gs1.articlemanager.infrastructure.persistence.repository;

import com.gs1.articlemanager.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
