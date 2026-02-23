// src/main/java/com/gs1/articlemanager/infrastructure/util/PasswordEncoderUtil.java
package com.gs1.articlemanager.infrastructure.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String[] passwords = {
            "Admin@2025",
            "password123",
            "password123"
        };
        
        String[] labels = {
            "Admin",
            "Mamadou",
            "Fatou"
        };
        
        System.out.println("=== Génération des hash BCrypt ===\n");
        for (int i = 0; i < passwords.length; i++) {
            String hash = encoder.encode(passwords[i]);
            System.out.println(labels[i] + " (" + passwords[i] + "):");
            System.out.println(hash);
            System.out.println();
        }
    }
}
