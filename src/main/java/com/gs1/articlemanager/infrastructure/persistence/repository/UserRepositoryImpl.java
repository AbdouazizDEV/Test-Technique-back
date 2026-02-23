// src/main/java/com/gs1/articlemanager/infrastructure/persistence/repository/UserRepositoryImpl.java
package com.gs1.articlemanager.infrastructure.persistence.repository;

import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.domain.repository.UserRepository;
import com.gs1.articlemanager.infrastructure.persistence.entity.UserEntity;
import com.gs1.articlemanager.infrastructure.persistence.mapper.UserEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return UserEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
            .map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
            .map(UserEntityMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
            .map(UserEntityMapper::toDomain)
            .collect(java.util.stream.Collectors.toList());
    }
}
