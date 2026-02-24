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
        // Vérifier d'abord DATABASE_URL (format Railway/Heroku)
        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl != null && !databaseUrl.startsWith("jdbc:")) {
            Map<String, Object> normalized = normalizePostgresUrl(databaseUrl);
            if (!normalized.isEmpty()) {
                environment.getPropertySources().addFirst(new MapPropertySource("normalizedDbUrl", normalized));
            }
            return;
        }
        
        // Sinon, vérifier DB_URL (format Render)
        String dbUrl = environment.getProperty("DB_URL");
        if (dbUrl != null && !dbUrl.isEmpty() && !dbUrl.startsWith("jdbc:")) {
            Map<String, Object> normalized = normalizePostgresUrl(dbUrl);
            if (!normalized.isEmpty()) {
                environment.getPropertySources().addFirst(new MapPropertySource("normalizedDbUrl", normalized));
            }
        }
    }
    
    /**
     * Normalise une URL PostgreSQL au format Render/Railway vers le format JDBC
     * Input: postgresql://user:password@host:port/database
     * Output: Map contenant:
     *   - spring.datasource.url: jdbc:postgresql://host:port/database
     *   - spring.datasource.username: user (si présent)
     *   - spring.datasource.password: password (si présent)
     */
    private Map<String, Object> normalizePostgresUrl(String url) {
        Map<String, Object> properties = new HashMap<>();
        
        if (url == null || url.isEmpty()) {
            return properties;
        }
        
        // Si déjà au format JDBC, retourner tel quel
        if (url.startsWith("jdbc:")) {
            properties.put("spring.datasource.url", url);
            return properties;
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
            String userInfo = null;
            String hostPart;
            String database = "";
            int atIndex = urlWithoutScheme.indexOf('@');
            
            if (atIndex > 0) {
                // Il y a des credentials: user:password@host/database
                userInfo = urlWithoutScheme.substring(0, atIndex);
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
            
            // Construire l'URL JDBC sans les credentials (ils seront définis séparément)
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
            
            properties.put("spring.datasource.url", jdbcUrl.toString());
            
            // Extraire username et password des userInfo si présents
            if (userInfo != null && !userInfo.isEmpty()) {
                int userPassSeparator = userInfo.indexOf(':');
                if (userPassSeparator > 0) {
                    properties.put("spring.datasource.username", userInfo.substring(0, userPassSeparator));
                    properties.put("spring.datasource.password", userInfo.substring(userPassSeparator + 1));
                } else {
                    properties.put("spring.datasource.username", userInfo);
                }
            }
            
            return properties;
        } catch (Exception e) {
            // En cas d'erreur de parsing, logguer et retourner l'URL originale avec jdbc:
            System.err.println("Error parsing PostgreSQL URL: " + url + " - " + e.getMessage());
            String fallback = url;
            if (fallback.startsWith("postgresql://")) {
                fallback = fallback.replace("postgresql://", "jdbc:postgresql://");
            } else if (fallback.startsWith("postgres://")) {
                fallback = fallback.replace("postgres://", "jdbc:postgresql://");
            } else if (!fallback.startsWith("jdbc:")) {
                fallback = "jdbc:" + fallback;
            }
            properties.put("spring.datasource.url", fallback);
            return properties;
        }
    }
}
