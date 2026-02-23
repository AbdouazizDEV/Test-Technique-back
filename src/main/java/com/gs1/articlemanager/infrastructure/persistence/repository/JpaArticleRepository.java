// src/main/java/com/gs1/articlemanager/infrastructure/persistence/repository/JpaArticleRepository.java
package com.gs1.articlemanager.infrastructure.persistence.repository;

import com.gs1.articlemanager.infrastructure.persistence.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaArticleRepository extends JpaRepository<ArticleEntity, Long> {
    List<ArticleEntity> findByTitleContainingIgnoreCase(String title);
}
