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
curl https://your-app.up.railway.app/api-docs
curl https://your-app.up.railway.app/swagger-ui.html
```

## 🐛 Dépannage

### Erreur : Build échoue

**Solution** :
1. Vérifiez les logs de build
2. Assurez-vous que le Dockerfile est à la racine
3. Vérifiez que `railway.json` est correct

### Erreur : Connexion à la base de données

**Solution** :
1. Vérifiez que PostgreSQL est bien créé
2. Vérifiez que `DATABASE_URL` est bien défini (automatique)
3. Vérifiez les logs de l'application

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
