package com.gs1.articlemanager.infrastructure.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Initialise les données de test (seeders) au démarrage de l'application en production
 */
@Component
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final Environment environment;
    
    @Autowired
    public DataInitializer(JdbcTemplate jdbcTemplate, DataSource dataSource, Environment environment) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.environment = environment;
    }
    
    @PostConstruct
    public void initializeData() {
        // Vérifier si on est en production
        boolean isProduction = Arrays.asList(environment.getActiveProfiles()).contains("production");
        
        if (!isProduction) {
            logger.info("Mode développement détecté - Seeders non exécutés automatiquement");
            return;
        }
        
        try {
            // Vérifier si l'utilisateur admin existe déjà
            Integer adminCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE email = 'admin@gs1sn.com'",
                Integer.class
            );
            
            if (adminCount != null && adminCount > 0) {
                logger.info("✅ Données de seed déjà présentes - Seeders non exécutés");
                return;
            }
            
            logger.info("🌱 Initialisation des données de test (seeders)...");
            
            // Charger et exécuter le script de seed avec ResourceDatabasePopulator
            // Cela gère mieux les scripts SQL multi-statements
            ClassPathResource resource = new ClassPathResource("db/migration/seed-railway.sql");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(resource);
            populator.setContinueOnError(true); // Continuer même en cas d'erreur (ON CONFLICT DO NOTHING)
            populator.setSeparator(";"); // Séparateur de statements
            
            try {
                populator.execute(dataSource);
                logger.info("✅ Script de seed exécuté avec succès");
            } catch (Exception e) {
                // Les erreurs de contrainte sont normales (ON CONFLICT DO NOTHING)
                logger.debug("Note: Certaines insertions peuvent avoir été ignorées (ON CONFLICT DO NOTHING)");
            }
            
            logger.info("✅ Seeders exécutés avec succès - Données de test initialisées");
            
            // Afficher les informations de connexion pour l'admin
            logger.info("📋 Comptes de test créés:");
            logger.info("   👤 Admin: admin@gs1sn.com / Admin@2025");
            logger.info("   👤 Membre: mamadou@gs1sn.com / password123");
            logger.info("   👤 Membre: fatou@gs1sn.com / password123");
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'initialisation des données: {}", e.getMessage(), e);
            // Ne pas faire échouer le démarrage de l'application si le seed échoue
        }
    }
}
