# Guide de Déploiement sur Render

Ce guide vous explique comment déployer l'application Article Manager API sur Render.

## 📋 Prérequis

1. Un compte Render (gratuit disponible sur [render.com](https://render.com))
2. Un repository GitHub avec le code
3. Les variables d'environnement nécessaires

## 🚀 Déploiement

### Option 1 : Déploiement via Render Dashboard (Recommandé)

#### 1. Créer une Base de Données MySQL

1. Connectez-vous à [Render Dashboard](https://dashboard.render.com)
2. Cliquez sur **"New +"** → **"PostgreSQL"** ou **"MySQL"**
3. Sélectionnez **"MySQL"**
4. Configurez :
   - **Name** : `article-manager-db`
   - **Database** : `article_manager`
   - **User** : `gs1user`
   - **Region** : Choisissez la région la plus proche
   - **Plan** : `Free` (pour commencer)
5. Cliquez sur **"Create Database"**
6. **Important** : Notez les informations de connexion (Internal Database URL)

#### 2. Créer le Service Web

1. Dans le Dashboard, cliquez sur **"New +"** → **"Web Service"**
2. Connectez votre repository GitHub
3. Sélectionnez le repository `Test-Technique-back`
4. Configurez le service :
   - **Name** : `article-manager-api`
   - **Region** : Même région que la base de données
   - **Branch** : `main`
   - **Root Directory** : `backend` (si le code est dans un sous-dossier)
   - **Environment** : `Java`
   - **Build Command** : `mvn clean package -DskipTests`
   - **Start Command** : `java -jar target/*.jar`
   - **Plan** : `Free` (pour commencer)

#### 3. Configurer les Variables d'Environnement

Dans la section **"Environment"** du service web, ajoutez :

```bash
SPRING_PROFILES_ACTIVE=production
DB_URL=jdbc:mysql://[HOST]:[PORT]/article_manager?useSSL=true&serverTimezone=UTC
DB_USERNAME=gs1user
DB_PASSWORD=[VOTRE_MOT_DE_PASSE]
JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo=
JWT_EXPIRATION=86400000
PORT=10000
```

**Note** : Remplacez `[HOST]`, `[PORT]` et `[VOTRE_MOT_DE_PASSE]` par les valeurs de votre base de données Render.

#### 4. Initialiser la Base de Données

1. Connectez-vous à votre base de données MySQL via un client (MySQL Workbench, DBeaver, etc.)
2. Exécutez le script `src/main/resources/db/migration/schema.sql`
3. Exécutez le script `src/main/resources/db/migration/seed.sql`

**Alternative** : Utilisez le terminal Render pour exécuter les scripts SQL.

### Option 2 : Déploiement via render.yaml (Automatique)

Si vous avez un fichier `render.yaml` dans votre repository :

1. Dans Render Dashboard, cliquez sur **"New +"** → **"Blueprint"**
2. Connectez votre repository
3. Render détectera automatiquement le fichier `render.yaml`
4. Configurez les variables d'environnement comme ci-dessus
5. Cliquez sur **"Apply"**

## 🔧 Configuration des Variables d'Environnement

### Variables Requises

| Variable | Description | Exemple |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Profil Spring à utiliser | `production` |
| `DB_URL` | URL de connexion MySQL | `jdbc:mysql://...` |
| `DB_USERNAME` | Nom d'utilisateur MySQL | `gs1user` |
| `DB_PASSWORD` | Mot de passe MySQL | `votre_mot_de_passe` |
| `JWT_SECRET` | Clé secrète JWT (Base64) | `U73dsIlmL5e6wfY...` |
| `JWT_EXPIRATION` | Durée de validité du token (ms) | `86400000` |
| `PORT` | Port d'écoute (Render définit automatiquement) | `10000` |

### Générer une Nouvelle Clé JWT Secrète

```bash
openssl rand -base64 32
```

## 📝 Scripts SQL à Exécuter

### 1. Créer la Base de Données

Exécutez le contenu de `src/main/resources/db/migration/schema.sql` :

```sql
CREATE DATABASE IF NOT EXISTS article_manager
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE article_manager;

CREATE TABLE IF NOT EXISTS users (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(100)        NOT NULL,
  email       VARCHAR(150)        NOT NULL UNIQUE,
  password    VARCHAR(255)        NOT NULL,
  role        ENUM('ROLE_ADMIN','ROLE_MEMBER') NOT NULL DEFAULT 'ROLE_MEMBER',
  created_at  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS articles (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  title         VARCHAR(200)        NOT NULL,
  content       TEXT                NOT NULL,
  author_id     BIGINT              NOT NULL,
  published_at  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_article_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_articles_author ON articles(author_id);
CREATE INDEX idx_articles_title  ON articles(title);
```

### 2. Insérer les Données de Test

Exécutez le contenu de `src/main/resources/db/migration/seed.sql` (avec les vrais hash BCrypt).

## ✅ Vérification du Déploiement

Une fois déployé, votre API sera accessible sur :
- **URL de l'API** : `https://article-manager-api.onrender.com` (ou votre URL personnalisée)
- **Swagger UI** : `https://article-manager-api.onrender.com/swagger-ui.html`
- **API Docs** : `https://article-manager-api.onrender.com/api-docs`

### Test Rapide

```bash
curl https://article-manager-api.onrender.com/api-docs
```

## 🔍 Dépannage

### Problème : L'application ne démarre pas

1. Vérifiez les logs dans Render Dashboard
2. Vérifiez que toutes les variables d'environnement sont définies
3. Vérifiez que la base de données est accessible

### Problème : Erreur de connexion à la base de données

1. Vérifiez que `DB_URL` utilise l'URL interne de Render
2. Vérifiez que `DB_USERNAME` et `DB_PASSWORD` sont corrects
3. Vérifiez que la base de données est bien créée

### Problème : Port déjà utilisé

Render définit automatiquement la variable `PORT`. Assurez-vous que votre `application-production.yml` utilise `${PORT}`.

## 📊 Monitoring

Render fournit :
- **Logs en temps réel** : Accessibles dans le Dashboard
- **Métriques** : CPU, RAM, Requêtes
- **Health Checks** : Configurés automatiquement

## 🔐 Sécurité

- ✅ Utilisez des variables d'environnement pour les secrets
- ✅ Ne commitez jamais les mots de passe
- ✅ Utilisez HTTPS (automatique sur Render)
- ✅ Générez une nouvelle clé JWT secrète pour la production

## 🚀 Mise à Jour

Pour mettre à jour l'application :

1. Poussez vos changements sur GitHub
2. Render redéploiera automatiquement
3. Ou déclenchez manuellement un redéploiement depuis le Dashboard

## 📚 Ressources

- [Documentation Render](https://render.com/docs)
- [Déploiement Java sur Render](https://render.com/docs/java)
- [Variables d'environnement Render](https://render.com/docs/environment-variables)
