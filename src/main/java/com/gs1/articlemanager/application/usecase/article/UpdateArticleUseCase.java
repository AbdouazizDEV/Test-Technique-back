// src/main/java/com/gs1/articlemanager/application/usecase/article/UpdateArticleUseCase.java
package com.gs1.articlemanager.application.usecase.article;

import com.gs1.articlemanager.application.dto.request.UpdateArticleRequest;
import com.gs1.articlemanager.application.dto.response.ArticleResponse;
import com.gs1.articlemanager.domain.model.Article;
import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.domain.repository.ArticleRepository;
import com.gs1.articlemanager.domain.repository.UserRepository;
import com.gs1.articlemanager.domain.service.ArticleDomainService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

public class UpdateArticleUseCase {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleDomainService articleDomainService;

    public UpdateArticleUseCase(ArticleRepository articleRepository, UserRepository userRepository,
                               ArticleDomainService articleDomainService) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleDomainService = articleDomainService;
    }

    public ArticleResponse execute(Long articleId, Long userId, UpdateArticleRequest request) {
        Optional<Article> articleOpt = articleRepository.findById(articleId);
        if (articleOpt.isEmpty()) {
            throw new IllegalArgumentException("Article non trouvé");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }

        Article article = articleOpt.get();
        User user = userOpt.get();

        if (!articleDomainService.canModifyArticle(article, user)) {
            throw new ArticleAccessDeniedException("Vous n'avez pas l'autorisation de modifier cet article");
        }

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        articleDomainService.validateArticle(article);
        Article updatedArticle = articleRepository.save(article);

        return new ArticleResponse(
            updatedArticle.getId(),
            updatedArticle.getTitle(),
            updatedArticle.getContent(),
            updatedArticle.getAuthorName(),
            updatedArticle.getAuthorId(),
            updatedArticle.getPublishedAt(),
            updatedArticle.getUpdatedAt()
        );
    }

    public static class ArticleAccessDeniedException extends RuntimeException {
        public ArticleAccessDeniedException(String message) {
            super(message);
        }
    }
}
