// src/main/java/com/gs1/articlemanager/infrastructure/persistence/repository/ArticleRepositoryImpl.java
package com.gs1.articlemanager.infrastructure.persistence.repository;

import com.gs1.articlemanager.domain.model.Article;
import com.gs1.articlemanager.domain.repository.ArticleRepository;
import com.gs1.articlemanager.infrastructure.persistence.entity.ArticleEntity;
import com.gs1.articlemanager.infrastructure.persistence.mapper.ArticleEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ArticleRepositoryImpl implements ArticleRepository {
    private final JpaArticleRepository jpaArticleRepository;
    private final JpaUserRepository jpaUserRepository;

    public ArticleRepositoryImpl(JpaArticleRepository jpaArticleRepository,
                                JpaUserRepository jpaUserRepository) {
        this.jpaArticleRepository = jpaArticleRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public Article save(Article article) {
        ArticleEntity entity = ArticleEntityMapper.toEntity(article);
        if (article.getAuthorId() != null) {
            jpaUserRepository.findById(article.getAuthorId())
                .ifPresent(entity::setAuthor);
        }
        ArticleEntity savedEntity = jpaArticleRepository.save(entity);
        return ArticleEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Article> findById(Long id) {
        return jpaArticleRepository.findById(id)
            .map(ArticleEntityMapper::toDomain);
    }

    @Override
    public List<Article> findAll() {
        return jpaArticleRepository.findAll().stream()
            .map(ArticleEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Article> findByTitleContaining(String title) {
        return jpaArticleRepository.findByTitleContainingIgnoreCase(title).stream()
            .map(ArticleEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaArticleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaArticleRepository.existsById(id);
    }
}
