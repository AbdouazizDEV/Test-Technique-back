# Guide de Déploiement sur Render

Ce guide vous explique comment déployer l'application Article Manager API sur Render.

## 📋 Prérequis

1. Un compte Render (gratuit disponible sur [render.com](https://render.com))
2. Un repository GitHub avec le code
3. Les variables d'environnement nécessaires

## 🚀 Déploiement

### Option 0 : Déploiement via Render CLI (Recommandé pour automatisation)

Voir le guide complet : **[DEPLOIEMENT_RENDER_CLI.md](./DEPLOIEMENT_RENDER_CLI.md)**

**Installation rapide** :
```bash
curl -fsSL https://render.com/install.sh | bash
render auth login
cd backend && render blueprint launch
```

### Option 1 : Déploiement via Render Dashboard

#### 1. Créer une Base de Données PostgreSQL

1. Connectez-vous à [Render Dashboard](https://dashboard.render.com)
2. Cliquez sur **"New +"** → **"Postgres"**
3. Configurez :
   - **Name** : `article-manager-db`
   - **Database** : `article_manager`
   - **User** : `   ` (ou laissez par défaut)
   - **Region** : Choisissez la région la plus proche
   - **Plan** : `Free` (pour commencer)
4. Cliquez sur **"Create Database"**
5. **Important** : Notez les informations de connexion (Internal Database URL)

#### 2. Créer le Service Web

1. Dans le Dashboard, cliquez sur **"New +"** → **"Web Service"**
2. Connectez votre repository GitHub
3. Sélectionnez le repository `Test-Technique-back`
4. Configurez le service :
   - **Name** : `article-manager-api`
   - **Region** : Même région que la base de données
   - **Branch** : `main`
   - **Root Directory** : `backend` (si le code est dans un sous-dossier)
   - **Environment** : `Docker` (ou utilisez `render.yaml` qui configure Docker automatiquement)
   - **Dockerfile Path** : `./Dockerfile` (si non détecté automatiquement)
   - **Plan** : `Free` (pour commencer)

#### 3. Configurer les Variables d'Environnement

Dans la section **"Environment"** du service web, ajoutez :

```bash
SPRING_PROFILES_ACTIVE=production
DB_URL=postgresql://article_manager_user:PEutSBPSr7YZXHzOdtAbO7zaZ1ESxHG6@dpg-d6ecl3pr0fns73c97j5g-a/article_manager
DB_USERNAME=article_manager_user
DB_PASSWORD=PEutSBPSr7YZXHzOdtAbO7zaZ1ESxHG6
JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo=
JWT_EXPIRATION=86400000
PORT=8080
```

**⚠️ IMPORTANT** :
- **DB_URL** : Utilisez l'**URL INTERNE** depuis l'onglet "Connect" → "Internal" de votre base de données
- Format Render : `postgresql://user:password@host/database`
- **Ne modifiez PAS l'URL** ! L'`EnvironmentPostProcessor` la normalisera automatiquement en `jdbc:postgresql://host:5432/database`
- Les credentials (`DB_USERNAME` et `DB_PASSWORD`) sont extraits automatiquement de l'URL, mais vous pouvez aussi les définir explicitement

#### 4. Initialiser la Base de Données

1. Connectez-vous à votre base de données PostgreSQL via un client (DBeaver, pgAdmin, etc.) ou via le terminal Render
2. Exécutez le script `src/main/resources/db/migration/schema.sql` pour créer les tables
3. Exécutez le script `src/main/resources/db/migration/seed.sql` pour insérer les données de test

**Alternative** : Utilisez le terminal Render (onglet "Shell" de votre base de données) pour exécuter les scripts SQL avec `psql`.

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
| `DB_URL` | URL de connexion PostgreSQL (format Render) | `postgresql://user:pass@host/db` |
| `DB_USERNAME` | Nom d'utilisateur PostgreSQL | `article_manager_user` |
| `DB_PASSWORD` | Mot de passe PostgreSQL | `votre_mot_de_passe` |
| `JWT_SECRET` | Clé secrète JWT (Base64) | `U73dsIlmL5e6wfY...` |
| `JWT_EXPIRATION` | Durée de validité du token (ms) | `86400000` |
| `PORT` | Port d'écoute (Render définit automatiquement) | `10000` |

### Générer une Nouvelle Clé JWT Secrète

```bash
openssl rand -base64 32
```

## 📝 Scripts SQL à Exécuter

### 1. Créer la Base de Données

La base de données est créée automatiquement par Render. Exécutez ensuite le contenu de `src/main/resources/db/migration/schema.sql` pour créer les tables :

```sql
-- Les tables sont créées automatiquement via le script schema.sql
-- Utilisez psql ou un client PostgreSQL pour exécuter le script
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

1. Vérifiez que `DB_URL` utilise l'**URL INTERNE** (onglet "Connect" → "Internal" de votre base de données)
2. Format attendu : `postgresql://user:password@host/database` (l'`EnvironmentPostProcessor` le normalise automatiquement)
3. Vérifiez que `DB_USERNAME` et `DB_PASSWORD` correspondent aux credentials de l'URL
4. Vérifiez que la base de données est bien créée et accessible
5. Vérifiez que le service web et la base de données sont dans la même région

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
