// src/main/java/com/gs1/articlemanager/interfaces/rest/ArticleController.java
package com.gs1.articlemanager.interfaces.rest;

import com.gs1.articlemanager.application.dto.request.CreateArticleRequest;
import com.gs1.articlemanager.application.dto.request.UpdateArticleRequest;
import com.gs1.articlemanager.application.dto.response.ArticleResponse;
import com.gs1.articlemanager.application.usecase.article.*;
import com.gs1.articlemanager.domain.model.User;
import com.gs1.articlemanager.domain.repository.ArticleRepository;
import com.gs1.articlemanager.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
@Tag(name = "Articles", description = "Endpoints pour la gestion des articles")
@SecurityRequirement(name = "Bearer Authentication")
public class ArticleController {
    private final CreateArticleUseCase createArticleUseCase;
    private final GetArticlesUseCase getArticlesUseCase;
    private final UpdateArticleUseCase updateArticleUseCase;
    private final DeleteArticleUseCase deleteArticleUseCase;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleController(CreateArticleUseCase createArticleUseCase,
                            GetArticlesUseCase getArticlesUseCase,
                            UpdateArticleUseCase updateArticleUseCase,
                            DeleteArticleUseCase deleteArticleUseCase,
                            ArticleRepository articleRepository,
                            UserRepository userRepository) {
        this.createArticleUseCase = createArticleUseCase;
        this.getArticlesUseCase = getArticlesUseCase;
        this.updateArticleUseCase = updateArticleUseCase;
        this.deleteArticleUseCase = deleteArticleUseCase;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Liste tous les articles avec recherche optionnelle par titre")
    public ResponseEntity<ApiResponse<java.util.List<ArticleResponse>>> getArticles(
            @RequestParam(required = false) String title) {
        java.util.List<ArticleResponse> articles = getArticlesUseCase.execute(title);
        return ResponseEntity.ok(ApiResponse.success(articles));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un article par ID")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticleById(
            @PathVariable @Positive Long id) {
        Optional<com.gs1.articlemanager.domain.model.Article> articleOpt = articleRepository.findById(id);
        if (articleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Article non trouvé"));
        }
        com.gs1.articlemanager.domain.model.Article article = articleOpt.get();
        ArticleResponse response = new ArticleResponse(
            article.getId(),
            article.getTitle(),
            article.getContent(),
            article.getAuthorName(),
            article.getAuthorId(),
            article.getPublishedAt(),
            article.getUpdatedAt()
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel article")
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(
            @Valid @RequestBody CreateArticleRequest request,
            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        ArticleResponse response = createArticleUseCase.execute(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Article créé avec succès", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un article")
    public ResponseEntity<ApiResponse<ArticleResponse>> updateArticle(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateArticleRequest request,
            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        ArticleResponse response = updateArticleUseCase.execute(id, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Article mis à jour avec succès", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un article")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(
            @PathVariable @Positive Long id,
            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        deleteArticleUseCase.execute(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Article supprimé avec succès", null));
    }
}
