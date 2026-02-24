# 🔧 Correction des Problèmes de Déploiement

## Problèmes identifiés et solutions

### 1. ✅ Erreur de validation render.yaml

**Erreur** :
```
services[0] non-docker runtime cannot have dockerContext
services[0] non-docker runtime cannot have dockerfilePath
services[0].runtime invalid runtime
```

**Solution** : Ajout de `runtime: docker` dans `render.yaml`

```yaml
services:
  - type: web
    name: article-manager-api
    runtime: docker  # ← Ajouté
    dockerfilePath: ./Dockerfile
    dockerContext: .
```

### 2. ✅ Erreur d'authentification PostgreSQL

**Erreur** :
```
FATAL: password authentication failed for user "article_manager_user"
```

**Causes possibles** :
1. Variables d'environnement `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` incorrectes
2. URL de la base de données non configurée (URL INTERNE vs URL EXTERNE)
3. Credentials ne correspondent pas à ceux de la base de données Render

**Solution** :

#### Étape 1 : Obtenir les bonnes informations de connexion

1. Allez sur le Dashboard Render
2. Ouvrez votre base de données PostgreSQL
3. Allez dans l'onglet **"Connect"**
4. **Copiez l'URL INTERNE** (format : `postgresql://user:password@host/database`)

#### Étape 2 : Configurer les variables d'environnement

Dans le Dashboard Render, pour votre service web `article-manager-api`, configurez :

```bash
SPRING_PROFILES_ACTIVE=production
DB_URL=postgresql://article_manager_user:PASSWORD@HOST/article_manager
DB_USERNAME=article_manager_user
DB_PASSWORD=PASSWORD_FROM_RENDER
JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo=
JWT_EXPIRATION=86400000
PORT=8080
```

**⚠️ IMPORTANT** :
- Utilisez l'**URL INTERNE** (pas l'URL externe)
- Remplacez `PASSWORD` et `HOST` par les vraies valeurs de Render
- L'`EnvironmentPostProcessor` normalisera automatiquement l'URL en format JDBC

#### Étape 3 : Vérifier la synchronisation

Si vous utilisez un Blueprint, assurez-vous que les variables `sync: false` sont bien configurées manuellement dans le Dashboard.

### 3. ✅ Correction du workflow CI/CD

**Erreur** :
```
Error: An error occurred trying to start process '/usr/bin/bash' with working directory '/home/runner/work/Test-Technique-back/Test-Technique-back/./backend'. No such file or directory
```

**Solution** : Suppression du `working-directory: ./backend` car le workflow s'exécute déjà dans le bon répertoire.

## 📋 Checklist de déploiement

- [ ] `render.yaml` contient `runtime: docker`
- [ ] Variables d'environnement configurées dans Render Dashboard
- [ ] `DB_URL` utilise l'URL INTERNE de la base de données
- [ ] `DB_USERNAME` et `DB_PASSWORD` correspondent aux credentials Render
- [ ] Base de données PostgreSQL créée sur Render
- [ ] Service web lié à la base de données

## 🔍 Vérification après déploiement

1. **Vérifier les logs** :
   ```bash
   # Dans le Dashboard Render, onglet "Logs"
   # Ou via l'API si vous avez le CLI
   ```

2. **Tester l'API** :
   ```bash
   curl https://article-manager-api.onrender.com/api-docs
   ```

3. **Vérifier la connexion à la base de données** :
   - Les logs ne doivent plus afficher d'erreur d'authentification
   - L'application doit démarrer correctement

## 🚀 Prochaines étapes

1. **Pousser les corrections** :
   ```bash
   git add render.yaml .github/workflows/ci-cd.yml
   git commit -m "fix: Ajout runtime docker et correction CI/CD"
   git push origin main
   ```

2. **Redéployer sur Render** :
   - Le Blueprint se mettra à jour automatiquement
   - Ou redéployez manuellement depuis le Dashboard

3. **Configurer les variables d'environnement** :
   - Utilisez l'URL INTERNE de la base de données
   - Vérifiez que tous les credentials sont corrects

4. **Initialiser la base de données** :
   - Utilisez le Shell de la base de données dans Render
   - Exécutez les scripts SQL :
     ```sql
     \i src/main/resources/db/migration/schema.sql
     \i src/main/resources/db/migration/seed.sql
     ```
