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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {
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
