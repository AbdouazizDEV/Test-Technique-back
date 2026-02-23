// src/main/java/com/gs1/articlemanager/domain/service/ArticleDomainService.java
package com.gs1.articlemanager.domain.service;

import com.gs1.articlemanager.domain.model.Article;
import com.gs1.articlemanager.domain.model.User;

public class ArticleDomainService {
    public void validateArticle(Article article) {
        if (article.getTitle() == null || article.getTitle().trim().length() < 3) {
            throw new IllegalArgumentException("Le titre doit contenir au moins 3 caractères");
        }
        if (article.getContent() == null || article.getContent().trim().length() < 10) {
            throw new IllegalArgumentException("Le contenu doit contenir au moins 10 caractères");
        }
        if (article.getAuthorId() == null) {
            throw new IllegalArgumentException("L'auteur de l'article est requis");
        }
    }

    public boolean canModifyArticle(Article article, User user) {
        if (article == null || user == null) {
            return false;
        }
        return article.getAuthorId().equals(user.getId()) ||
               user.getRole() == com.gs1.articlemanager.domain.model.Role.ROLE_ADMIN;
    }
}
