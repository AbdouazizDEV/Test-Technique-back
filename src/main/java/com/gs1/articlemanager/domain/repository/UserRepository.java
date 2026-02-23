// src/main/java/com/gs1/articlemanager/domain/repository/UserRepository.java
package com.gs1.articlemanager.domain.repository;

import com.gs1.articlemanager.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteById(Long id);
    List<User> findAll();
}
