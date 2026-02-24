# 📦 Docker Registry - GitHub Container Registry (ghcr.io)

## 🎯 Où est publiée l'image Docker ?

L'image Docker est publiée sur **GitHub Container Registry (ghcr.io)**.

### URL de l'image

```
ghcr.io/AbdouazizDEV/article-manager-api:latest
```

### Tags disponibles

- `latest` : Dernière version sur la branche `main`
- `main` : Version de la branche main
- `main-<sha>` : Version avec le SHA du commit
- `develop` : Version de la branche develop (si applicable)

## 🚀 Utilisation de l'image

### Pull l'image

```bash
docker pull ghcr.io/AbdouazizDEV/article-manager-api:latest
```

### Run l'image

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e DB_URL=jdbc:postgresql://host:5432/article_manager \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  -e JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo= \
  ghcr.io/AbdouazizDEV/article-manager-api:latest
```

### Utilisation dans docker-compose.yml

```yaml
services:
  backend:
    image: ghcr.io/AbdouazizDEV/article-manager-api:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - DB_URL=jdbc:postgresql://db:5432/article_manager
      - DB_USERNAME=postgres
      - DB_PASSWORD=postgres
```

## 🔐 Authentification

### Pull public

Si le repository est public, l'image est accessible publiquement :

```bash
docker pull ghcr.io/AbdouazizDEV/article-manager-api:latest
```

### Pull privé

Si le repository est privé, vous devez vous authentifier :

```bash
# Créer un Personal Access Token (PAT) avec permission 'read:packages'
echo $GITHUB_TOKEN | docker login ghcr.io -u USERNAME --password-stdin

# Puis pull
docker pull ghcr.io/AbdouazizDEV/article-manager-api:latest
```

## 🔄 Publication automatique

L'image est automatiquement publiée via GitHub Actions lors de chaque push sur `main` :

1. Le workflow `ci-cd.yml` build l'image
2. L'image est taguée automatiquement
3. L'image est pushée sur `ghcr.io`
4. Les tags sont mis à jour automatiquement

## 📋 Configuration dans le workflow

Le workflow utilise :
- `docker/login-action@v3` : Authentification avec `GITHUB_TOKEN`
- `docker/metadata-action@v5` : Génération automatique des tags
- `docker/build-push-action@v5` : Build et push de l'image

## 🌐 Alternatives

### Docker Hub

Si vous préférez Docker Hub, vous pouvez :

1. Créer un compte sur [hub.docker.com](https://hub.docker.com)
2. Modifier le workflow pour utiliser `docker.io` :

```yaml
- name: Log in to Docker Hub
  uses: docker/login-action@v3
  with:
    username: ${{ secrets.DOCKER_USERNAME }}
    password: ${{ secrets.DOCKER_PASSWORD }}
```

### Autres registries

- **AWS ECR** : Pour déploiement AWS
- **Google Container Registry** : Pour déploiement GCP
- **Azure Container Registry** : Pour déploiement Azure

## 📚 Ressources

- [GitHub Container Registry Documentation](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
- [Docker Hub Documentation](https://docs.docker.com/docker-hub/)
- [GitHub Actions Docker Actions](https://github.com/docker/build-push-action)
