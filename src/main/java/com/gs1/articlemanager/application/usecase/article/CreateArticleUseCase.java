// src/main/java/com/gs1/articlemanager/application/usecase/article/CreateArticleUseCase.java
package com.gs1.articlemanager.application.usecase.article;

import com.gs1.articlemanager.application.dto.request.CreateArticleRequest;
import com.gs1.articlemanager.application.dto.response.ArticleResponse;
import com.gs1.articlemanager.domain.model.Article;
import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.domain.repository.ArticleRepository;
import com.gs1.articlemanager.domain.repository.UserRepository;
import com.gs1.articlemanager.domain.service.ArticleDomainService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

public class CreateArticleUseCase {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleDomainService articleDomainService;

    public CreateArticleUseCase(ArticleRepository articleRepository, UserRepository userRepository,
                               ArticleDomainService articleDomainService) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleDomainService = articleDomainService;
    }

    public ArticleResponse execute(Long authorId, CreateArticleRequest request) {
        Optional<User> authorOpt = userRepository.findById(authorId);
        if (authorOpt.isEmpty()) {
            throw new UsernameNotFoundException("Auteur non trouvé");
        }

        User author = authorOpt.get();
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setAuthorId(author.getId());
        article.setAuthorName(author.getName());
        article.setPublishedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        articleDomainService.validateArticle(article);
        Article savedArticle = articleRepository.save(article);

        return new ArticleResponse(
            savedArticle.getId(),
            savedArticle.getTitle(),
            savedArticle.getContent(),
            savedArticle.getAuthorName(),
            savedArticle.getAuthorId(),
            savedArticle.getPublishedAt(),
            savedArticle.getUpdatedAt()
        );
    }
}
