# ⚠️ Vercel n'est pas adapté pour Spring Boot

## Pourquoi Vercel ne fonctionne pas ?

Vercel est une plateforme conçue pour :
- ✅ Applications frontend (Next.js, React, Vue, etc.)
- ✅ Fonctions serverless (Node.js, Python, Go)
- ✅ Sites statiques

Vercel **ne supporte pas** :
- ❌ Applications Java/Spring Boot
- ❌ Serveurs qui tournent en continu
- ❌ Applications backend avec JAR/WAR

## Solution : Utiliser Render

Votre application Spring Boot est déjà configurée pour Render :

### ✅ Configuration déjà en place :
- `render.yaml` : Configuration automatique
- `Dockerfile` : Image Docker multi-stage
- `EnvironmentPostProcessor` : Normalisation URL PostgreSQL
- Base de données PostgreSQL créée sur Render

### 🚀 Déploiement sur Render

1. **Connectez-vous à Render** : [dashboard.render.com](https://dashboard.render.com)

2. **Créez un nouveau Web Service** :
   - Cliquez sur "New +" → "Web Service"
   - Connectez votre repository GitHub
   - Sélectionnez `Test-Technique-back`
   - Root Directory : `backend`
   - Environment : `Docker`
   - Dockerfile Path : `./Dockerfile`

3. **Configurez les variables d'environnement** :
   ```
   SPRING_PROFILES_ACTIVE=production
   DB_URL=postgresql://article_manager_user:PEutSBPSr7YZXHzOdtAbO7zaZ1ESxHG6@dpg-d6ecl3pr0fns73c97j5g-a/article_manager
   DB_USERNAME=article_manager_user
   DB_PASSWORD=PEutSBPSr7YZXHzOdtAbO7zaZ1ESxHG6
   JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo=
   JWT_EXPIRATION=86400000
   PORT=8080
   ```

4. **Déployez** : Render build automatiquement depuis GitHub

### 📋 Alternative : Déploiement via Blueprint

Si vous avez un fichier `render.yaml` :
1. Cliquez sur "New +" → "Blueprint"
2. Connectez votre repository
3. Render détectera automatiquement `render.yaml`
4. Configurez les variables d'environnement
5. Cliquez sur "Apply"

## 🔄 Autres plateformes alternatives

Si vous préférez une autre plateforme :

### Railway
- Support Docker natif
- PostgreSQL disponible
- Configuration simple

### Heroku
- Support Java/Spring Boot
- PostgreSQL via addon
- Configuration via `Procfile`

### Google Cloud Run
- Support Docker/Container
- Scaling automatique
- Pay-as-you-go

## 📚 Ressources

- [Guide de déploiement Render complet](./DEPLOIEMENT_RENDER.md)
- [Documentation Render](https://render.com/docs)
- [Déploiement Java sur Render](https://render.com/docs/java)
