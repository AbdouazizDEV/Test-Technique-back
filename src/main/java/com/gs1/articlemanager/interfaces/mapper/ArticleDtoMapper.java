// src/main/java/com/gs1/articlemanager/interfaces/mapper/ArticleDtoMapper.java
package com.gs1.articlemanager.interfaces.mapper;

import com.gs1.articlemanager.application.dto.response.ArticleResponse;
import com.gs1.articlemanager.domain.model.Article;

public class ArticleDtoMapper {
    public static ArticleResponse toResponse(Article article) {
        if (article == null) {
            return null;
        }
        return new ArticleResponse(
            article.getId(),
            article.getTitle(),
            article.getContent(),
            article.getAuthorName(),
            article.getAuthorId(),
            article.getPublishedAt(),
            article.getUpdatedAt()
        );
    }
}
