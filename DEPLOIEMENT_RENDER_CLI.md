# 🚀 Déploiement avec Render CLI

Ce guide vous explique comment déployer l'application Article Manager API sur Render en utilisant le **Render CLI** (ligne de commande).

## 📋 Prérequis

1. Un compte Render (gratuit sur [render.com](https://render.com))
2. Un repository GitHub avec le code
3. Le Render CLI installé sur votre machine

## 🔧 Installation du Render CLI

### Linux / macOS

```bash
# Télécharger et installer le Render CLI
curl -fsSL https://render.com/install.sh | bash
```

### Alternative : Installation manuelle

```bash
# Télécharger la dernière version
curl -L https://github.com/renderinc/cli/releases/latest/download/render-linux-amd64 -o render

# Rendre exécutable
chmod +x render

# Déplacer dans le PATH (optionnel)
sudo mv render /usr/local/bin/render
```

### Vérifier l'installation

```bash
render --version
```

## 🔐 Authentification

### 1. Obtenir votre API Key

1. Connectez-vous à [Render Dashboard](https://dashboard.render.com)
2. Allez dans **Account Settings** → **API Keys**
3. Cliquez sur **"Create API Key"**
4. Copiez la clé (elle ne sera affichée qu'une seule fois !)

### 2. Se connecter avec le CLI

```bash
# Se connecter avec l'API Key
render auth login

# Ou définir la variable d'environnement
export RENDER_API_KEY="votre-api-key-ici"
```

**Alternative** : Créer un fichier `~/.render/api_key` :

```bash
mkdir -p ~/.render
echo "votre-api-key-ici" > ~/.render/api_key
chmod 600 ~/.render/api_key
```

## 🚀 Déploiement avec render.yaml

### Option 1 : Déploiement automatique (Blueprint)

Le fichier `render.yaml` définit tous les services nécessaires :

```bash
# Depuis le répertoire backend
cd backend

# Déployer tous les services définis dans render.yaml
render blueprint launch

# Ou spécifier le fichier explicitement
render blueprint launch --file render.yaml
```

Cette commande va :
1. ✅ Créer la base de données PostgreSQL (`article-manager-db`)
2. ✅ Créer le service web (`article-manager-api`)
3. ✅ Configurer les variables d'environnement
4. ✅ Déployer l'application

### Option 2 : Déploiement manuel étape par étape

#### 1. Créer la base de données PostgreSQL

```bash
render postgres create \
  --name article-manager-db \
  --database article_manager \
  --user article_manager_user \
  --plan starter \
  --region frankfurt
```

**Notez l'URL interne** de la base de données (affichée après la création).

#### 2. Créer le service web

```bash
render services create web \
  --name article-manager-api \
  --repo https://github.com/AbdouazizDEV/Test-Technique-back.git \
  --branch main \
  --root-dir backend \
  --dockerfile-path ./Dockerfile \
  --docker-context . \
  --plan starter \
  --region frankfurt
```

#### 3. Configurer les variables d'environnement

```bash
# Obtenir l'ID du service (remplacez SERVICE_ID par l'ID réel)
SERVICE_ID="srv-xxxxx"

# Définir les variables d'environnement
render env:set \
  --service-id $SERVICE_ID \
  SPRING_PROFILES_ACTIVE=production \
  DB_URL="postgresql://user:password@host/database" \
  DB_USERNAME="article_manager_user" \
  DB_PASSWORD="votre-mot-de-passe" \
  JWT_SECRET="U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo=" \
  JWT_EXPIRATION=86400000 \
  PORT=8080
```

**⚠️ IMPORTANT** : 
- Utilisez l'**URL INTERNE** de la base de données (format : `postgresql://user:password@host/database`)
- L'`EnvironmentPostProcessor` normalisera automatiquement l'URL en format JDBC

#### 4. Lier la base de données au service

```bash
# Lier la base de données au service web
render services:link-database \
  --service-id $SERVICE_ID \
  --database-id $DB_ID
```

## 📝 Commandes utiles du Render CLI

### Lister les services

```bash
# Lister tous les services
render services list

# Détails d'un service spécifique
render services show --service-id $SERVICE_ID
```

### Gérer les variables d'environnement

```bash
# Lister les variables d'environnement
render env:list --service-id $SERVICE_ID

# Définir une variable
render env:set --service-id $SERVICE_ID KEY=value

# Supprimer une variable
render env:unset --service-id $SERVICE_ID KEY
```

### Gérer les déploiements

```bash
# Lister les déploiements
render deploys list --service-id $SERVICE_ID

# Détails d'un déploiement
render deploys show --deploy-id $DEPLOY_ID

# Redéployer manuellement
render deploys create --service-id $SERVICE_ID
```

### Voir les logs

```bash
# Logs en temps réel
render logs tail --service-id $SERVICE_ID

# Logs avec filtres
render logs tail --service-id $SERVICE_ID --follow
```

### Gérer les bases de données

```bash
# Lister les bases de données
render postgres list

# Détails d'une base de données
render postgres show --database-id $DB_ID

# Obtenir l'URL de connexion
render postgres connection-string --database-id $DB_ID
```

## 🔄 Workflow de déploiement complet

### 1. Préparer l'environnement

```bash
# Se connecter
render auth login

# Vérifier la connexion
render whoami
```

### 2. Déployer avec render.yaml

```bash
cd backend

# Déployer tous les services
render blueprint launch

# Suivre le déploiement
render logs tail --service article-manager-api
```

### 3. Initialiser la base de données

Une fois la base de données créée, initialisez les tables :

```bash
# Obtenir l'URL de connexion
DB_URL=$(render postgres connection-string --database article-manager-db)

# Se connecter et exécuter les scripts SQL
psql "$DB_URL" -f src/main/resources/db/migration/schema.sql
psql "$DB_URL" -f src/main/resources/db/migration/seed.sql
```

**Alternative** : Utiliser le shell Render

```bash
# Ouvrir un shell sur la base de données
render postgres shell --database article-manager-db

# Puis exécuter les scripts SQL
\i src/main/resources/db/migration/schema.sql
\i src/main/resources/db/migration/seed.sql
```

## ✅ Vérification du déploiement

### Vérifier le statut

```bash
# Statut du service
render services show --service article-manager-api

# Vérifier les health checks
curl https://article-manager-api.onrender.com/api-docs
```

### Tester l'API

```bash
# Test de l'endpoint de documentation
curl https://article-manager-api.onrender.com/api-docs

# Test de l'endpoint Swagger
curl https://article-manager-api.onrender.com/swagger-ui.html
```

## 🔍 Dépannage

### Problème : Authentification échouée

```bash
# Vérifier la clé API
echo $RENDER_API_KEY

# Se reconnecter
render auth login
```

### Problème : Service ne démarre pas

```bash
# Voir les logs en temps réel
render logs tail --service article-manager-api --follow

# Vérifier les variables d'environnement
render env:list --service article-manager-api
```

### Problème : Erreur de connexion à la base de données

```bash
# Vérifier l'URL de la base de données
render postgres connection-string --database article-manager-db

# Vérifier que DB_URL utilise l'URL INTERNE
render env:list --service article-manager-api | grep DB_URL
```

## 🔄 Mise à jour de l'application

### Mise à jour automatique

Si vous avez configuré l'auto-deploy sur GitHub :

```bash
# Pousser les changements
git push origin main

# Render redéploiera automatiquement
# Suivre les logs
render logs tail --service article-manager-api --follow
```

### Mise à jour manuelle

```bash
# Redéployer manuellement
render deploys create --service article-manager-api
```

## 🗑️ Supprimer les services

```bash
# Supprimer le service web
render services delete --service article-manager-api

# Supprimer la base de données
render postgres delete --database article-manager-db

# Supprimer tous les services d'un blueprint
render blueprint destroy
```

## 📚 Ressources

- [Documentation Render CLI](https://render.com/docs/cli)
- [Render CLI GitHub](https://github.com/renderinc/cli)
- [Guide render.yaml](https://render.com/docs/blueprint-spec)
- [Variables d'environnement](https://render.com/docs/environment-variables)

## 🎯 Commandes rapides

```bash
# Déploiement complet en une commande
cd backend && render blueprint launch

# Voir les logs
render logs tail --service article-manager-api

# Redéployer
render deploys create --service article-manager-api

# Vérifier le statut
render services show --service article-manager-api
```
