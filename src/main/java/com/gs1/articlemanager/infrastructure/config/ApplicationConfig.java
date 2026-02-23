// src/main/java/com/gs1/articlemanager/infrastructure/config/ApplicationConfig.java
package com.gs1.articlemanager.infrastructure.config;

import com.gs1.articlemanager.application.usecase.article.*;
import com.gs1.articlemanager.application.usecase.auth.LoginUseCase;
import com.gs1.articlemanager.application.usecase.auth.RegisterUseCase;
import com.gs1.articlemanager.application.usecase.user.GetUserProfileUseCase;
import com.gs1.articlemanager.application.usecase.user.UpdateUserProfileUseCase;
import com.gs1.articlemanager.domain.repository.ArticleRepository;
import com.gs1.articlemanager.domain.repository.UserRepository;
import com.gs1.articlemanager.domain.service.ArticleDomainService;
import com.gs1.articlemanager.domain.service.UserDomainService;
import com.gs1.articlemanager.infrastructure.security.JwtTokenProvider;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment env = event.getEnvironment();
        String dbUrl = env.getProperty("spring.datasource.url");
        
        // Normaliser l'URL PostgreSQL pour Render
        // Render fournit: postgresql://user:password@host/database
        // JDBC attend: jdbc:postgresql://host:port/database (avec credentials séparés)
        if (dbUrl != null && !dbUrl.startsWith("jdbc:")) {
            String normalizedUrl = normalizePostgresUrl(dbUrl);
            java.util.Map<String, Object> source = new java.util.HashMap<>();
            source.put("spring.datasource.url", normalizedUrl);
            env.getPropertySources().addFirst(new MapPropertySource("normalizedDbUrl", source));
        }
    }
    
    /**
     * Normalise une URL PostgreSQL au format Render vers le format JDBC
     * Input: postgresql://user:password@host/database
     * Output: jdbc:postgresql://host:port/database
     * Les credentials sont gérés séparément via DB_USERNAME et DB_PASSWORD
     */
    private String normalizePostgresUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        
        // Si déjà au format JDBC, retourner tel quel
        if (url.startsWith("jdbc:")) {
            return url;
        }
        
        try {
            // Parser l'URL au format URI: postgresql://user:password@host:port/database
            java.net.URI uri = new java.net.URI(url);
            
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
            
            // Si le path commence par "/", l'enlever
            if (path != null && path.startsWith("/")) {
                path = path.substring(1);
            }
            
            // Construire l'URL JDBC sans les credentials (ils sont dans DB_USERNAME/DB_PASSWORD)
            StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://");
            jdbcUrl.append(host != null ? host : "");
            
            // Ajouter le port si spécifié, sinon utiliser le port par défaut PostgreSQL (5432)
            if (port > 0) {
                jdbcUrl.append(":").append(port);
            }
            
            if (path != null && !path.isEmpty()) {
                jdbcUrl.append("/").append(path);
            }
            
            return jdbcUrl.toString();
        } catch (Exception e) {
            // En cas d'erreur de parsing, essayer simplement d'ajouter "jdbc:" devant
            return "jdbc:" + url;
        }
    }
    
    @Bean
    public UserDomainService userDomainService() {
        return new UserDomainService();
    }

    @Bean
    public ArticleDomainService articleDomainService() {
        return new ArticleDomainService();
    }

    @Bean
    public RegisterUseCase registerUseCase(UserRepository userRepository,
                                           PasswordEncoder passwordEncoder,
                                           UserDomainService userDomainService) {
        return new RegisterUseCase(userRepository, passwordEncoder, userDomainService);
    }

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtTokenProvider jwtTokenProvider,
                                     AuthenticationManager authenticationManager) {
        return new LoginUseCase(userRepository, passwordEncoder, jwtTokenProvider, authenticationManager);
    }

    @Bean
    public GetUserProfileUseCase getUserProfileUseCase(UserRepository userRepository) {
        return new GetUserProfileUseCase(userRepository);
    }

    @Bean
    public UpdateUserProfileUseCase updateUserProfileUseCase(UserRepository userRepository,
                                                             UserDomainService userDomainService) {
        return new UpdateUserProfileUseCase(userRepository, userDomainService);
    }

    @Bean
    public CreateArticleUseCase createArticleUseCase(ArticleRepository articleRepository,
                                                     UserRepository userRepository,
                                                     ArticleDomainService articleDomainService) {
        return new CreateArticleUseCase(articleRepository, userRepository, articleDomainService);
    }

    @Bean
    public GetArticlesUseCase getArticlesUseCase(ArticleRepository articleRepository) {
        return new GetArticlesUseCase(articleRepository);
    }

    @Bean
    public UpdateArticleUseCase updateArticleUseCase(ArticleRepository articleRepository,
                                                     UserRepository userRepository,
                                                     ArticleDomainService articleDomainService) {
        return new UpdateArticleUseCase(articleRepository, userRepository, articleDomainService);
    }

    @Bean
    public DeleteArticleUseCase deleteArticleUseCase(ArticleRepository articleRepository,
                                                     UserRepository userRepository,
                                                     ArticleDomainService articleDomainService) {
        return new DeleteArticleUseCase(articleRepository, userRepository, articleDomainService);
    }
}
