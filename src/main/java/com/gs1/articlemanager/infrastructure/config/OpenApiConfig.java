// src/main/java/com/gs1/articlemanager/infrastructure/config/OpenApiConfig.java
package com.gs1.articlemanager.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {
    private final Environment environment;

    public OpenApiConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${server.port:8080}") int serverPort) {
        OpenAPI openAPI = new OpenAPI()
            .info(new Info()
                .title("Article Manager API")
                .version("1.0.0")
                .description("API REST Spring Boot pour la gestion d'articles avec authentification JWT")
                .contact(new Contact()
                    .name("GS1 Sénégal")
                    .email("admin@gs1sn.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Entrez votre token JWT")));

        // Configurer les serveurs selon l'environnement
        List<Server> servers = new ArrayList<>();
        
        // Vérifier si on est en production
        boolean isProduction = java.util.Arrays.asList(environment.getActiveProfiles()).contains("production");
        
        if (isProduction) {
            // En production, utiliser HTTPS (Railway)
            // Utiliser une variable d'environnement si disponible, sinon utiliser l'URL par défaut
            String serverUrl = environment.getProperty("RAILWAY_PUBLIC_DOMAIN");
            if (serverUrl == null || serverUrl.isEmpty()) {
                // Si pas de variable, utiliser l'URL par défaut (sera remplacé par l'URL réelle)
                serverUrl = "https://test-technique-back-production.up.railway.app";
            } else {
                // S'assurer que l'URL commence par https://
                if (!serverUrl.startsWith("http://") && !serverUrl.startsWith("https://")) {
                    serverUrl = "https://" + serverUrl;
                } else if (serverUrl.startsWith("http://")) {
                    // Remplacer http par https
                    serverUrl = serverUrl.replace("http://", "https://");
                }
            }
            servers.add(new Server()
                .url(serverUrl)
                .description("Production Server (Railway)"));
        } else {
            // En développement, utiliser localhost
            servers.add(new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server"));
        }
        
        openAPI.setServers(servers);
        return openAPI;
    }
}
