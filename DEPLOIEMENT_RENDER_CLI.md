# 🚀 Déploiement avec Render CLI

Ce guide vous explique comment déployer l'application Article Manager API sur Render.

## ⚠️ Note importante sur le Render CLI

Le Render CLI officiel peut avoir des problèmes d'installation sur certaines distributions Linux. Nous proposons **3 méthodes alternatives** :

## 📋 Méthode 1 : Dashboard Render (Recommandé - Plus Simple)

### Avantages
- ✅ Interface graphique intuitive
- ✅ Pas d'installation requise
- ✅ Configuration visuelle des variables d'environnement
- ✅ Logs en temps réel

### Étapes

1. **Connectez-vous** à [Render Dashboard](https://dashboard.render.com)

2. **Créez un Blueprint** :
   - Cliquez sur **"New +"** → **"Blueprint"**
   - Connectez votre repository GitHub : `AbdouazizDEV/Test-Technique-back`
   - Render détectera automatiquement le fichier `render.yaml`

3. **Configurez les variables d'environnement** :
   - Dans la section "Environment" du service web
   - Ajoutez les variables suivantes :
     ```
     SPRING_PROFILES_ACTIVE=production
     DB_URL=postgresql://user:password@host/database (URL INTERNE)
     DB_USERNAME=article_manager_user
     DB_PASSWORD=votre-mot-de-passe
     JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo=
     JWT_EXPIRATION=86400000
     PORT=8080
     ```

4. **Cliquez sur "Apply"** pour déployer

5. **Initialisez la base de données** :
   - Utilisez le Shell de la base de données dans Render
   - Exécutez les scripts SQL :
     ```sql
     \i src/main/resources/db/migration/schema.sql
     \i src/main/resources/db/migration/seed.sql
     ```

## 📋 Méthode 2 : Script de déploiement (Alternative)

Si le Render CLI n'est pas disponible, utilisez le script `deploy-render.sh` :

```bash
# Définir votre API Key
export RENDER_API_KEY="rnd_E4PTinLupRovoGcc6ENiQgsgYNKZ"

# Ou créer un fichier .render_api_key
echo "rnd_E4PTinLupRovoGcc6ENiQgsgYNKZ" > .render_api_key
export RENDER_API_KEY=$(cat .render_api_key)

# Exécuter le script
cd backend
./deploy-render.sh deploy
```

## 📋 Méthode 3 : Render CLI (Si disponible)

### Installation

Le Render CLI peut être installé de plusieurs façons :

#### Option A : Script d'installation officiel

```bash
curl -fsSL https://render.com/install.sh | bash
```

#### Option B : Téléchargement manuel

```bash
# Vérifier la dernière version sur GitHub
# https://github.com/renderinc/cli/releases

# Télécharger le binaire pour votre architecture
curl -L -o render https://github.com/renderinc/cli/releases/download/v[VERSION]/render-linux-amd64
chmod +x render

# Installer dans le PATH (nécessite sudo)
sudo mv render /usr/local/bin/render

# Ou utiliser localement
./render --version
```

### Authentification

```bash
# Avec votre API Key
export RENDER_API_KEY="rnd_E4PTinLupRovoGcc6ENiQgsgYNKZ"

# Ou utiliser la commande interactive
render auth login
```

### Déploiement

```bash
cd backend

# Déployer via Blueprint
render blueprint launch

# Ou créer les services manuellement
render services create web \
  --name article-manager-api \
  --repo https://github.com/AbdouazizDEV/Test-Technique-back.git \
  --branch main \
  --root-dir backend \
  --dockerfile-path ./Dockerfile
```

## 🔐 Obtenir votre API Key

1. Connectez-vous à [Render Dashboard](https://dashboard.render.com)
2. Allez dans **Account Settings** → **API Keys**
3. Cliquez sur **"Create API Key"**
4. **Copiez la clé immédiatement** (elle ne sera affichée qu'une seule fois !)
5. Stockez-la de manière sécurisée

## 🚀 Déploiement rapide (Dashboard)

**La méthode la plus simple et recommandée** :

1. ✅ Allez sur https://dashboard.render.com
2. ✅ Cliquez sur **"New +"** → **"Blueprint"**
3. ✅ Connectez `AbdouazizDEV/Test-Technique-back`
4. ✅ Render détectera `render.yaml` automatiquement
5. ✅ Configurez les variables d'environnement
6. ✅ Cliquez sur **"Apply"**

C'est tout ! 🎉

## 📝 Configuration des Variables d'Environnement

### Variables Requises

| Variable | Description | Exemple |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Profil Spring | `production` |
| `DB_URL` | URL PostgreSQL (format Render) | `postgresql://user:pass@host/db` |
| `DB_USERNAME` | Utilisateur PostgreSQL | `article_manager_user` |
| `DB_PASSWORD` | Mot de passe PostgreSQL | `votre-mot-de-passe` |
| `JWT_SECRET` | Clé secrète JWT (Base64) | `U73dsIlmL5e6wfY...` |
| `JWT_EXPIRATION` | Durée validité token (ms) | `86400000` |
| `PORT` | Port d'écoute | `8080` |

**⚠️ IMPORTANT** :
- Utilisez l'**URL INTERNE** de la base de données (onglet "Connect" → "Internal")
- Format Render : `postgresql://user:password@host/database`
- L'`EnvironmentPostProcessor` normalisera automatiquement l'URL

## ✅ Vérification du Déploiement

```bash
# Test de l'API
curl https://article-manager-api.onrender.com/api-docs

# Swagger UI
curl https://article-manager-api.onrender.com/swagger-ui.html
```

## 🔍 Dépannage

### Problème : Render CLI non installable

**Solution** : Utilisez le Dashboard Render (Méthode 1) - c'est la méthode recommandée et la plus simple.

### Problème : Erreur de connexion à la base de données

1. Vérifiez que `DB_URL` utilise l'**URL INTERNE**
2. Vérifiez que le service web et la base de données sont dans la même région
3. Vérifiez les logs dans le Dashboard Render

### Problème : Service ne démarre pas

1. Vérifiez les logs dans le Dashboard Render
2. Vérifiez que toutes les variables d'environnement sont définies
3. Vérifiez que le Dockerfile est correct

## 📚 Ressources

- [Documentation Render](https://render.com/docs)
- [Render Dashboard](https://dashboard.render.com)
- [Guide render.yaml](https://render.com/docs/blueprint-spec)
- [Variables d'environnement](https://render.com/docs/environment-variables)

## 🎯 Recommandation

**Pour la plupart des utilisateurs, la Méthode 1 (Dashboard) est la plus simple et la plus fiable.**

Le Dashboard Render offre :
- ✅ Interface graphique intuitive
- ✅ Configuration visuelle
- ✅ Logs en temps réel
- ✅ Gestion facile des variables d'environnement
- ✅ Pas d'installation requise
