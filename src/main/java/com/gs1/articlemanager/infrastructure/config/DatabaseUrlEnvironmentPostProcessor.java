package com.gs1.articlemanager.infrastructure.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * EnvironmentPostProcessor pour normaliser l'URL PostgreSQL de Render
 * S'exécute très tôt dans le cycle de vie de Spring Boot, avant la création du DataSource
 */
public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {
    
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String dbUrl = environment.getProperty("spring.datasource.url");
        
        // Normaliser l'URL PostgreSQL pour Render
        // Render fournit: postgresql://user:password@host/database
        // JDBC attend: jdbc:postgresql://host:port/database (avec credentials séparés)
        if (dbUrl != null && !dbUrl.startsWith("jdbc:")) {
            String normalizedUrl = normalizePostgresUrl(dbUrl);
            Map<String, Object> source = new HashMap<>();
            source.put("spring.datasource.url", normalizedUrl);
            environment.getPropertySources().addFirst(new MapPropertySource("normalizedDbUrl", source));
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
            // Parser manuellement l'URL au format: postgresql://user:password@host:port/database
            
            // Enlever le préfixe "postgresql://" ou "postgres://"
            String urlWithoutScheme = url;
            if (url.startsWith("postgresql://")) {
                urlWithoutScheme = url.substring("postgresql://".length());
            } else if (url.startsWith("postgres://")) {
                urlWithoutScheme = url.substring("postgres://".length());
            }
            
            // Extraire les credentials et le reste
            String hostPart;
            String database;
            int atIndex = urlWithoutScheme.indexOf('@');
            
            if (atIndex > 0) {
                // Il y a des credentials: user:password@host/database
                hostPart = urlWithoutScheme.substring(atIndex + 1);
            } else {
                // Pas de credentials: host/database
                hostPart = urlWithoutScheme;
            }
            
            // Extraire host:port et database
            int slashIndex = hostPart.indexOf('/');
            if (slashIndex > 0) {
                database = hostPart.substring(slashIndex + 1);
                hostPart = hostPart.substring(0, slashIndex);
            } else {
                database = "";
            }
            
            // Extraire host et port
            String host;
            int port = -1;
            int colonIndex = hostPart.indexOf(':');
            if (colonIndex > 0) {
                host = hostPart.substring(0, colonIndex);
                try {
                    port = Integer.parseInt(hostPart.substring(colonIndex + 1));
                } catch (NumberFormatException e) {
                    // Le port n'est pas un nombre valide, ignorer
                    port = -1;
                }
            } else {
                host = hostPart;
            }
            
            // Construire l'URL JDBC sans les credentials (ils sont dans DB_USERNAME/DB_PASSWORD)
            StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://");
            jdbcUrl.append(host != null && !host.isEmpty() ? host : "localhost");
            
            // Ajouter le port si spécifié, sinon utiliser le port par défaut PostgreSQL (5432)
            if (port > 0) {
                jdbcUrl.append(":").append(port);
            } else {
                jdbcUrl.append(":5432");
            }
            
            if (database != null && !database.isEmpty()) {
                jdbcUrl.append("/").append(database);
            }
            
            return jdbcUrl.toString();
        } catch (Exception e) {
            // En cas d'erreur de parsing, essayer simplement d'ajouter "jdbc:" devant
            // mais cela ne résoudra pas le problème des credentials
            String fallback = url;
            if (fallback.startsWith("postgresql://")) {
                fallback = fallback.replace("postgresql://", "jdbc:postgresql://");
            } else if (fallback.startsWith("postgres://")) {
                fallback = fallback.replace("postgres://", "jdbc:postgresql://");
            } else if (!fallback.startsWith("jdbc:")) {
                fallback = "jdbc:" + fallback;
            }
            return fallback;
        }
    }
}
