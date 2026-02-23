// src/main/java/com/gs1/articlemanager/application/dto/response/ArticleResponse.java
package com.gs1.articlemanager.application.dto.response;

import java.time.LocalDateTime;

public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Long authorId;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;

    public ArticleResponse() {
    }

    public ArticleResponse(Long id, String title, String content, String authorName,
                          Long authorId, LocalDateTime publishedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.authorId = authorId;
        this.publishedAt = publishedAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
