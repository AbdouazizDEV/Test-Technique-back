# Article Manager API

API REST Spring Boot pour la gestion d'articles avec authentification JWT, construite selon les principes de la Clean Architecture.

## 📋 Table des matières

- [Présentation](#présentation)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation locale](#installation-locale)
- [Installation avec Docker](#installation-avec-docker)
- [Variables d'environnement](#variables-denvironnement)
- [Endpoints API](#endpoints-api)
- [Comptes de test](#comptes-de-test)
- [Schéma de la base de données](#schéma-de-la-base-de-données)

## 🎯 Présentation

Cette application permet de gérer des articles avec un système d'authentification JWT. Elle respecte les principes SOLID et la Clean Architecture, garantissant une séparation claire des responsabilités et une maintenabilité optimale.

### Fonctionnalités principales

- **Authentification JWT** : Inscription et connexion sécurisées
- **Gestion des utilisateurs** : Profil utilisateur et administration
- **Gestion des articles** : CRUD complet avec contrôle d'accès
- **Rôles** : ROLE_ADMIN et ROLE_MEMBER avec permissions différenciées
- **Documentation API** : Swagger/OpenAPI intégré

## 🏗️ Architecture

Le projet suit une architecture en couches stricte :

```
src/main/java/com/gs1/articlemanager/
├── domain/              # Couche domaine (logique métier pure)
│   ├── model/          # Entités métier (POJO)
│   ├── repository/     # Interfaces de repository
│   └── service/        # Services métier
├── application/         # Couche application (use cases)
│   ├── usecase/        # Cas d'usage métier
│   └── dto/            # Objets de transfert de données
├── infrastructure/     # Couche infrastructure
│   ├── persistence/    # Implémentation JPA
│   ├── security/       # Configuration sécurité JWT
│   └── config/         # Configuration Spring
└── interfaces/         # Couche présentation
    └── rest/           # Controllers REST
```

### Principes respectés

- **Dependency Inversion** : Les use cases dépendent des interfaces du domaine, pas des implémentations
- **Single Responsibility** : Chaque classe a une responsabilité unique
- **Open/Closed** : Ouvert à l'extension, fermé à la modification
- **Separation of Concerns** : Séparation claire entre les couches

## 📦 Prérequis

### Installation locale

- **Java 17** ou supérieur
- **Maven 3.9+**
- **PostgreSQL 13** ou supérieur
- **Git**

### Installation avec Docker

- **Docker** 20.10+
- **Docker Compose** 2.0+

## 🚀 Installation locale

### 1. Cloner le repository

```bash
git clone <repository-url>
cd "Test Technique B"
```

### 2. Créer la base de données PostgreSQL

Créez la base de données et exécutez les scripts de migration :

```bash
# Créer la base de données
sudo -u postgres psql -c "CREATE DATABASE article_manager;"

# Créer les tables
psql -U postgres -d article_manager -f src/main/resources/db/migration/schema.sql
```

Ou si vous avez un utilisateur avec les droits :

```bash
psql -U votre_utilisateur -d postgres -c "CREATE DATABASE article_manager;"
psql -U votre_utilisateur -d article_manager -f src/main/resources/db/migration/schema.sql
```

### 3. Configurer l'application

Modifiez `src/main/resources/application.yml` ou définissez les variables d'environnement :

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/article_manager
    username: votre_utilisateur
    password: votre_mot_de_passe
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect
```

### 4. Générer les hash BCrypt

Exécutez la classe `PasswordEncoderUtil` pour générer les hash des mots de passe :

```bash
mvn compile exec:java -Dexec.mainClass="com.gs1.articlemanager.infrastructure.util.PasswordEncoderUtil"
```

Copiez les hash générés et mettez à jour `seed.sql` avec les vrais hash.

### 5. Injecter les données de test

Exécutez le script `seed.sql` :

```bash
psql -U postgres -d article_manager -f src/main/resources/db/migration/seed.sql
```

### 6. Lancer l'application

```bash
mvn spring-boot:run
```

L'API sera accessible sur `http://localhost:8080`

## 🐳 Installation avec Docker (Recommandée)

### 1. Préparer le fichier .env

Le fichier `.env` est déjà configuré dans `backend/.env`. Vérifiez les valeurs si nécessaire.

### 2. Générer les hash BCrypt

Avant de lancer Docker, générez les hash BCrypt et mettez à jour `seed.sql` :

```bash
cd backend
mvn compile exec:java -Dexec.mainClass="com.gs1.articlemanager.infrastructure.util.PasswordEncoderUtil"
```

### 3. Lancer avec Docker Compose

Depuis la racine du projet :

```bash
docker-compose up --build
```

Cette commande va :
- Créer et démarrer le conteneur PostgreSQL
- Attendre que PostgreSQL soit prêt (healthcheck)
- Construire et démarrer le backend Spring Boot
- Initialiser la base de données avec les scripts SQL

L'API sera accessible sur `http://localhost:8080`

## 🔐 Variables d'environnement

| Variable | Description | Valeur par défaut |
|----------|-------------|-------------------|
| `DB_URL` | URL de connexion PostgreSQL | `jdbc:postgresql://localhost:5432/article_manager` |
| `DB_USERNAME` | Nom d'utilisateur PostgreSQL | `postgres` |
| `DB_PASSWORD` | Mot de passe PostgreSQL | `postgres` |
| `JWT_SECRET` | Clé secrète JWT (Base64, 256 bits) | (à définir) |
| `JWT_EXPIRATION` | Durée de validité du token (ms) | `86400000` (24h) |

## 📡 Endpoints API

### Authentification

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/api/auth/register` | Inscription | Public |
| POST | `/api/auth/login` | Connexion | Public |

### Utilisateurs

| Méthode | Endpoint | Description | Auth | Rôle |
|---------|----------|-------------|------|------|
| GET | `/api/users/me` | Profil utilisateur connecté | Requis | Tous |
| PUT | `/api/users/me` | Mettre à jour son profil | Requis | Tous |
| GET | `/api/users` | Liste tous les utilisateurs | Requis | Admin |
| GET | `/api/users/{id}` | Détail utilisateur | Requis | Admin |

### Articles

| Méthode | Endpoint | Description | Auth | Rôle |
|---------|----------|-------------|------|------|
| GET | `/api/articles` | Liste tous les articles | Requis | Tous |
| GET | `/api/articles?title=...` | Recherche par titre | Requis | Tous |
| GET | `/api/articles/{id}` | Détail article | Requis | Tous |
| POST | `/api/articles` | Créer un article | Requis | Tous |
| PUT | `/api/articles/{id}` | Modifier un article | Requis | Auteur ou Admin |
| DELETE | `/api/articles/{id}` | Supprimer un article | Requis | Auteur ou Admin |

### Documentation

- **Swagger UI** : `http://localhost:8080/swagger-ui.html`
- **API Docs** : `http://localhost:8080/api-docs`

## 👥 Comptes de test

Après avoir exécuté `seed.sql` avec les vrais hash BCrypt :

### Administrateur

- **Email** : `admin@gs1sn.com`
- **Mot de passe** : `Admin@2025`
- **Rôle** : `ROLE_ADMIN`

### Membres

1. **Mamadou Diallo**
   - Email : `mamadou@gs1sn.com`
   - Mot de passe : `password123`
   - Rôle : `ROLE_MEMBER`

2. **Fatou Ndiaye**
   - Email : `fatou@gs1sn.com`
   - Mot de passe : `password123`
   - Rôle : `ROLE_MEMBER`

> **Important** : Les mots de passe dans `seed.sql` sont des placeholders. Vous devez générer les vrais hash BCrypt avec `PasswordEncoderUtil` et les remplacer dans `seed.sql` avant d'exécuter le script.

## 🗄️ Schéma de la base de données

### Table `users`

| Colonne | Type | Description |
|---------|------|-------------|
| `id` | BIGSERIAL | Identifiant unique (auto-incrémenté) |
| `name` | VARCHAR(100) | Nom de l'utilisateur |
| `email` | VARCHAR(150) | Email (UNIQUE) |
| `password` | VARCHAR(255) | Mot de passe hashé (BCrypt) |
| `role` | VARCHAR(20) | ROLE_ADMIN ou ROLE_MEMBER (CHECK constraint) |
| `created_at` | TIMESTAMP | Date de création |
| `updated_at` | TIMESTAMP | Date de mise à jour |

### Table `articles`

| Colonne | Type | Description |
|---------|------|-------------|
| `id` | BIGSERIAL | Identifiant unique (auto-incrémenté) |
| `title` | VARCHAR(200) | Titre de l'article |
| `content` | TEXT | Contenu de l'article |
| `author_id` | BIGINT | ID de l'auteur (FK vers users.id) |
| `published_at` | TIMESTAMP | Date de publication |
| `updated_at` | TIMESTAMP | Date de mise à jour |

### Index

- `idx_articles_author` sur `articles(author_id)`
- `idx_articles_title` sur `articles(title)`
- Index unique sur `users(email)`

## 🔒 Sécurité

- **JWT** : Tokens d'accès avec expiration configurable (24h par défaut)
- **BCrypt** : Hashage des mots de passe
- **CORS** : Configuré pour `http://localhost:3000`
- **Validation** : Bean Validation sur tous les DTOs
- **Contrôle d'accès** : Vérification des permissions au niveau des use cases

## 🧪 Tests

Pour exécuter les tests :

```bash
mvn test
```

## 📝 Notes importantes

1. **Hash BCrypt** : N'oubliez pas de générer les vrais hash avec `PasswordEncoderUtil` avant d'utiliser `seed.sql`
2. **JWT Secret** : En production, utilisez une clé secrète forte générée aléatoirement (256 bits minimum)
3. **Base de données** : Le mode `ddl-auto: validate` empêche Hibernate de modifier le schéma. Utilisez les scripts SQL fournis.
4. **PostgreSQL** : L'application utilise PostgreSQL 13+ avec des types spécifiques (BIGSERIAL, TIMESTAMP, etc.)
5. **Permissions** : Assurez-vous que l'utilisateur PostgreSQL a les permissions nécessaires sur le schéma `public`

## 📄 Licence

Ce projet est un projet de démonstration technique.Mon premier projet Spring Boot, je suis fiére de moi 'Maa ko fa am', je suis chaud pour en faire d'autres qui seront beaucoup plus serieux.
