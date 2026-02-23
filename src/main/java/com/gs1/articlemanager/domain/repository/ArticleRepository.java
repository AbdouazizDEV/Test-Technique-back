// src/main/java/com/gs1/articlemanager/domain/repository/ArticleRepository.java
package com.gs1.articlemanager.domain.repository;

import com.gs1.articlemanager.domain.model.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Article save(Article article);
    Optional<Article> findById(Long id);
    List<Article> findAll();
    List<Article> findByTitleContaining(String title);
    void deleteById(Long id);
    boolean existsById(Long id);
}
