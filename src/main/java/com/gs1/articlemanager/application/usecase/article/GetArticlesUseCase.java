// src/main/java/com/gs1/articlemanager/application/usecase/article/GetArticlesUseCase.java
package com.gs1.articlemanager.application.usecase.article;

import com.gs1.articlemanager.application.dto.response.ArticleResponse;
import com.gs1.articlemanager.domain.model.Article;
import com.gs1.articlemanager.domain.repository.ArticleRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GetArticlesUseCase {
    private final ArticleRepository articleRepository;

    public GetArticlesUseCase(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleResponse> execute(String titleFilter) {
        List<Article> articles;
        if (titleFilter != null && !titleFilter.trim().isEmpty()) {
            articles = articleRepository.findByTitleContaining(titleFilter);
        } else {
            articles = articleRepository.findAll();
        }

        return articles.stream()
            .map(article -> new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthorName(),
                article.getAuthorId(),
                article.getPublishedAt(),
                article.getUpdatedAt()
            ))
            .collect(Collectors.toList());
    }
}
