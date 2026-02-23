// src/main/java/com/gs1/articlemanager/application/dto/request/UpdateArticleRequest.java
package com.gs1.articlemanager.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateArticleRequest {
    @NotBlank(message = "Le titre est requis")
    @Size(min = 3, max = 200, message = "Le titre doit contenir entre 3 et 200 caractères")
    private String title;

    @NotBlank(message = "Le contenu est requis")
    @Size(min = 10, message = "Le contenu doit contenir au moins 10 caractères")
    private String content;

    public UpdateArticleRequest() {
    }

    public UpdateArticleRequest(String title, String content) {
        this.title = title;
        this.content = content;
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
}
