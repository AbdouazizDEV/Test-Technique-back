# Guide Docker

Ce guide explique comment construire et utiliser l'image Docker de l'application.

## 🐳 Construction de l'Image

### Construction Locale

```bash
cd backend
docker build -t article-manager:latest .
```

### Construction avec Tag

```bash
docker build -t article-manager:1.0.0 .
docker build -t abdouazizdev/article-manager:latest .
```

## 🚀 Exécution avec Docker

### Exécution Simple

```bash
docker run -p 8080:8080 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/article_manager \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=votre_mot_de_passe \
  -e JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo= \
  article-manager:latest
```

### Exécution avec Docker Compose

```bash
# Depuis la racine du projet
docker-compose up -d
```

Cela démarre :
- MySQL 8.0 sur le port 3306
- L'application Spring Boot sur le port 8080

### Arrêter les Services

```bash
docker-compose down
```

### Voir les Logs

```bash
docker-compose logs -f backend
```

## 📦 Push vers Docker Hub

### 1. Se Connecter à Docker Hub

```bash
docker login
```

### 2. Tag l'Image

```bash
docker tag article-manager:latest abdouazizdev/article-manager:latest
```

### 3. Push l'Image

```bash
docker push abdouazizdev/article-manager:latest
```

## 🔍 Vérification

### Lister les Images

```bash
docker images | grep article-manager
```

### Inspecter l'Image

```bash
docker inspect article-manager:latest
```

### Tester l'Image

```bash
docker run --rm -p 8080:8080 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/article_manager \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=votre_mot_de_passe \
  -e JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo= \
  article-manager:latest
```

Puis testez :
```bash
curl http://localhost:8080/api-docs
```

## 🏗️ Structure du Dockerfile

Le Dockerfile utilise un build multi-stage :

1. **Stage Builder** : Compile l'application avec Maven
2. **Stage Runtime** : Image légère avec seulement le JAR

### Optimisations

- ✅ Build multi-stage pour réduire la taille
- ✅ Utilisation d'Alpine Linux (image légère)
- ✅ Utilisateur non-root pour la sécurité
- ✅ Cache des dépendances Maven

## 🔐 Variables d'Environnement

| Variable | Description | Requis |
|----------|-------------|--------|
| `DB_URL` | URL de connexion MySQL | ✅ |
| `DB_USERNAME` | Nom d'utilisateur MySQL | ✅ |
| `DB_PASSWORD` | Mot de passe MySQL | ✅ |
| `JWT_SECRET` | Clé secrète JWT (Base64) | ✅ |
| `JWT_EXPIRATION` | Durée de validité (ms) | ❌ (défaut: 86400000) |
| `PORT` | Port d'écoute | ❌ (défaut: 8080) |
| `SPRING_PROFILES_ACTIVE` | Profil Spring | ❌ (défaut: default) |

## 📝 .dockerignore

Le fichier `.dockerignore` exclut :
- Fichiers de build (`target/`)
- Fichiers Git (`.git/`)
- Documentation de test
- Scripts de test
- Fichiers IDE

## 🚀 Déploiement sur Render

Render peut utiliser directement le Dockerfile pour construire l'image. Assurez-vous que :

1. Le Dockerfile est à la racine du dossier `backend`
2. Les variables d'environnement sont configurées dans Render
3. La base de données MySQL est créée et initialisée

Voir `DEPLOIEMENT_RENDER.md` pour plus de détails.
