// src/main/java/com/gs1/articlemanager/application/usecase/article/DeleteArticleUseCase.java
package com.gs1.articlemanager.application.usecase.article;

import com.gs1.articlemanager.domain.model.Article;
import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.domain.repository.ArticleRepository;
import com.gs1.articlemanager.domain.repository.UserRepository;
import com.gs1.articlemanager.domain.service.ArticleDomainService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class DeleteArticleUseCase {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleDomainService articleDomainService;

    public DeleteArticleUseCase(ArticleRepository articleRepository, UserRepository userRepository,
                               ArticleDomainService articleDomainService) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleDomainService = articleDomainService;
    }

    public void execute(Long articleId, Long userId) {
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
            throw new UpdateArticleUseCase.ArticleAccessDeniedException("Vous n'avez pas l'autorisation de supprimer cet article");
        }

        articleRepository.deleteById(articleId);
    }
}
