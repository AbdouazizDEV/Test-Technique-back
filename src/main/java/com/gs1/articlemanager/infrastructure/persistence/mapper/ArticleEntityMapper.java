// src/main/java/com/gs1/articlemanager/infrastructure/persistence/mapper/ArticleEntityMapper.java
package com.gs1.articlemanager.infrastructure.persistence.mapper;

import com.gs1.articlemanager.domain.model.Article;
import com.gs1.articlemanager.infrastructure.persistence.entity.ArticleEntity;

public class ArticleEntityMapper {
    public static Article toDomain(ArticleEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Article(
            entity.getId(),
            entity.getTitle(),
            entity.getContent(),
            entity.getAuthor() != null ? entity.getAuthor().getId() : null,
            entity.getAuthor() != null ? entity.getAuthor().getName() : null,
            entity.getPublishedAt(),
            entity.getUpdatedAt()
        );
    }

    public static ArticleEntity toEntity(Article domain) {
        if (domain == null) {
            return null;
        }
        ArticleEntity entity = new ArticleEntity();
        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setContent(domain.getContent());
        entity.setPublishedAt(domain.getPublishedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
