# 🚂 Configuration Railway - Guide Complet

## 📋 Checklist de Configuration

### 1. ✅ Source Repository (Déjà configuré)
- **Repository** : `AbdouazizDEV/Test-Technique-back`
- **Branch** : `main`
- **Root Directory** : Laisser vide (racine du repo)

### 2. 🔧 Variables d'Environnement

Dans l'onglet **Variables** de votre service Railway, ajoutez :

```bash
SPRING_PROFILES_ACTIVE=production
JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo=
JWT_EXPIRATION=86400000
```

**Note** : `DATABASE_URL` et `PORT` sont automatiquement fournis par Railway.

### 3. 🗄️ Base de Données PostgreSQL

1. Dans votre projet Railway, cliquez sur **"+ New"**
2. Sélectionnez **"Database"** → **"Add PostgreSQL"**
3. Railway créera automatiquement :
   - Une base de données PostgreSQL
   - La variable `DATABASE_URL` (format: `postgresql://user:pass@host:port/db`)
   - L'application détectera et utilisera automatiquement cette variable

### 4. 🚀 Build Configuration

Railway détectera automatiquement :
- ✅ `Dockerfile` à la racine
- ✅ `railway.json` pour la configuration
- ✅ Build via Docker

**Settings → Build** :
- **Builder** : Dockerfile (automatique)
- **Dockerfile Path** : `Dockerfile` (par défaut)

### 5. 🌐 Networking

**Settings → Networking** :
- ✅ Activez **"Public Networking"**
- Railway générera une URL publique automatiquement (ex: `https://your-app.up.railway.app`)

**⚠️ Important** : 
- Ne pas utiliser `interchange.proxy.rlwy.net` (proxy interne, erreur SSL normale)
- Utilisez l'URL publique générée par Railway (format `*.up.railway.app`)
- L'URL publique est visible dans Settings → Networking après activation

### 6. ⚙️ Deploy Settings

**Settings → Deploy** :
- **Start Command** : `java -jar app.jar` (déjà dans railway.json)
- **Restart Policy** : ON_FAILURE (déjà configuré)

## 🔍 Vérification du Déploiement

### Logs Railway

1. Allez dans l'onglet **"Deployments"**
2. Cliquez sur le dernier déploiement
3. Vérifiez les logs pour :
   - ✅ Build Docker réussi
   - ✅ Application démarrée
   - ✅ Connexion à PostgreSQL réussie

### Test de l'API

Une fois déployé, testez :
```bash
# Utilisez HTTPS, pas HTTP
curl https://your-app.up.railway.app/api-docs
curl https://your-app.up.railway.app/swagger-ui.html
```

**⚠️ Important** : Railway utilise **HTTPS** par défaut. Si vous utilisez `http://`, cela ne fonctionnera pas.

## 🐛 Dépannage

### Erreur : URL ne fonctionne pas (404, connexion refusée, erreur SSL)

**Solutions** :
1. **N'utilisez PAS `interchange.proxy.rlwy.net`** :
   - ❌ `https://interchange.proxy.rlwy.net/api-docs` (proxy interne, erreur SSL normale)
   - ✅ Utilisez l'URL publique Railway (format `*.up.railway.app`)

2. **Trouvez l'URL publique** :
   - Service Spring Boot → Settings → Networking
   - Activez "Public Networking" si ce n'est pas fait
   - Railway générera une URL publique (ex: `https://test-technique-back-production-xxxx.up.railway.app`)
   - Utilisez cette URL pour accéder à l'API

3. **Vérifiez que le service Spring Boot est déployé** :
   - Service doit être ACTIF (pas offline)
   - Vérifiez l'onglet "Deployments"

4. **Vérifiez les logs du service** :
   - Onglet "Deployments" → Dernier déploiement → "View logs"
   - Cherchez les erreurs de démarrage

### Erreur : Build échoue

**Solution** :
1. Vérifiez les logs de build
2. Assurez-vous que le Dockerfile est à la racine
3. Vérifiez que `railway.json` est correct
4. Assurez-vous que Railway utilise Docker, pas Maven

### Erreur : Schema-validation: missing table [articles]

**Symptômes** :
- `Schema-validation: missing table [articles]`
- `Schema-validation: missing table [users]`
- Application crash au démarrage après connexion réussie à PostgreSQL

**Solution** :
1. **Option rapide (déjà appliquée)** :
   - `ddl-auto` est maintenant à `update` dans `application-production.yml`
   - Hibernate créera automatiquement les tables au démarrage
   - ✅ **Déjà configuré** - Les tables seront créées au prochain redéploiement

2. **Option manuelle (alternative)** :
   - Si vous préférez créer les tables manuellement :
     - Railway → Service PostgreSQL → Onglet **Data** → **Query**
     - Copiez le contenu de `src/main/resources/db/migration/schema-railway.sql`
     - Collez et exécutez dans l'éditeur SQL
     - Puis exécutez `seed-railway.sql` pour insérer les données de test
   - Remettez `ddl-auto: validate` dans `application-production.yml`

**Note** : Avec `ddl-auto: update`, les tables seront créées automatiquement. Pour insérer les données de test, exécutez `seed-railway.sql` manuellement dans Railway.

### Erreur : Connexion à la base de données (Connection refused)

**Symptômes** :
- `java.net.ConnectException: Connection refused`
- `FATAL: password authentication failed`
- Application crash au démarrage

**Solutions** :

1. **Vérifiez que PostgreSQL est ACTIF** :
   - Dans Railway, vérifiez que le service PostgreSQL est **ACTIF** (pas offline)
   - Si offline, redémarrez-le ou recréez-le

2. **Vérifiez que `DATABASE_URL` est présent** :
   - Service Spring Boot → Onglet **Variables**
   - `DATABASE_URL` doit être présent (Railway l'ajoute automatiquement si PostgreSQL est dans le même projet)
   - Si absent :
     - Allez dans le service PostgreSQL → Onglet **Variables**
     - Copiez la valeur de `DATABASE_URL`
     - Collez-la dans les variables du service Spring Boot

3. **Vérifiez que les services sont dans le même projet** :
   - Les deux services (Spring Boot + PostgreSQL) doivent être dans le **même projet Railway**
   - Si PostgreSQL est dans un autre projet, copiez manuellement `DATABASE_URL`

4. **Vérifiez les logs de débogage** :
   - Les logs affichent maintenant :
     - `[DatabaseUrlEnvironmentPostProcessor] DATABASE_URL détecté: ...`
     - `[DatabaseUrlEnvironmentPostProcessor] URL normalisée: ...`
     - `[DatabaseUrlEnvironmentPostProcessor] Username: ...`
   - Vérifiez que l'URL normalisée est correcte (format: `jdbc:postgresql://host:port/database`)
   - Vérifiez que le host et le port sont corrects

5. **Vérifiez le format de l'URL** :
   - Railway fournit `DATABASE_URL` au format : `postgresql://user:password@host:port/database`
   - L'application le convertit automatiquement en format JDBC
   - Si vous voyez une erreur de parsing dans les logs, vérifiez le format

6. **Redémarrez le service Spring Boot** :
   - Après avoir ajouté/modifié `DATABASE_URL`, redémarrez le service
   - Railway → Service Spring Boot → Menu (⋮) → Restart

### Erreur : Port non trouvé

**Solution** :
- Railway définit automatiquement `PORT`
- L'application utilise `${PORT:8080}` dans `application-production.yml`

## 📝 Notes Importantes

- ✅ `DATABASE_URL` est automatiquement détecté et normalisé
- ✅ Les credentials (username/password) sont extraits automatiquement
- ✅ `PORT` est défini automatiquement par Railway
- ✅ Le build utilise Docker (multi-stage)
- ✅ L'application démarre avec `java -jar app.jar`

## 🔗 Ressources

- [Documentation Railway](https://docs.railway.app)
- [Railway PostgreSQL](https://docs.railway.app/databases/postgresql)
- [Railway Environment Variables](https://docs.railway.app/develop/variables)
