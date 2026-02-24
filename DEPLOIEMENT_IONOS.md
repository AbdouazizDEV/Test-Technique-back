# 🌐 Déploiement sur IONOS

## ⚠️ Limitations d'IONOS pour Spring Boot

### Hébergement Web Partagé (Classique)

IONOS propose principalement un **hébergement web partagé** qui est conçu pour :
- ✅ Sites web statiques (HTML, CSS, JavaScript)
- ✅ Applications PHP
- ✅ MySQL/MariaDB
- ✅ SFTP/FTP pour upload de fichiers

**Mais ne supporte PAS** :
- ❌ Applications Java/Spring Boot
- ❌ Exécution de JAR files
- ❌ Docker
- ❌ Applications long-running (serveurs)
- ❌ PostgreSQL (seulement MySQL/MariaDB)

### Solutions IONOS possibles

#### Option 1 : VPS IONOS (Virtual Private Server)

Si vous avez accès à un **VPS IONOS**, vous pouvez déployer Spring Boot :

**Avantages** :
- ✅ Contrôle total du serveur
- ✅ Installation de Java, PostgreSQL, Docker
- ✅ Support des applications Spring Boot
- ✅ Votre sous-domaine peut pointer vers le VPS

**Inconvénients** :
- ⚠️ Configuration manuelle requise
- ⚠️ Gestion du serveur (sécurité, mises à jour)
- ⚠️ Plus complexe que Render

**Configuration requise** :
1. Installer Java 17
2. Installer PostgreSQL
3. Configurer le firewall
4. Configurer le reverse proxy (Nginx/Apache)
5. Configurer SSL (Let's Encrypt)
6. Déployer l'application

#### Option 2 : Utiliser Render (Recommandé)

**Avantages de Render** :
- ✅ Configuration automatique (Docker)
- ✅ PostgreSQL inclus
- ✅ SSL automatique
- ✅ Déploiement automatique depuis GitHub
- ✅ Monitoring et logs
- ✅ Déjà configuré dans votre projet

**Votre sous-domaine IONOS peut pointer vers Render** :
- Configurez un enregistrement CNAME dans IONOS
- Pointez `test.abdouazizdiop.com` vers `article-manager-api.onrender.com`

## 🔄 Configuration : IONOS + Render

### Utiliser votre sous-domaine IONOS avec Render

1. **Dans Render Dashboard** :
   - Allez dans votre service web
   - Section "Custom Domains"
   - Ajoutez `test.abdouazizdiop.com`

2. **Dans IONOS** :
   - Allez dans la gestion DNS de votre domaine
   - Créez un enregistrement CNAME :
     ```
     Nom : test
     Type : CNAME
     Valeur : article-manager-api.onrender.com
     TTL : 3600
     ```

3. **SSL automatique** :
   - Render générera automatiquement un certificat SSL pour votre domaine

## 📋 Comparaison : IONOS vs Render

| Fonctionnalité | IONOS (Hébergement Web) | IONOS (VPS) | Render |
|----------------|------------------------|-------------|--------|
| Spring Boot | ❌ Non | ✅ Oui | ✅ Oui |
| Docker | ❌ Non | ✅ Oui | ✅ Oui |
| PostgreSQL | ❌ Non (MySQL seulement) | ✅ Oui | ✅ Oui |
| Configuration | ❌ Limitée | ⚠️ Manuelle | ✅ Automatique |
| SSL | ✅ Oui | ⚠️ Manuel | ✅ Automatique |
| Déploiement | ❌ Manuel (FTP) | ⚠️ Manuel | ✅ Automatique (GitHub) |
| Monitoring | ⚠️ Limité | ⚠️ Limité | ✅ Complet |
| Coût | 💰 Économique | 💰💰 Moyen | 💰 Gratuit (starter) |

## 🚀 Recommandation

### Option A : Render + Sous-domaine IONOS (Recommandé)

**Pourquoi** :
- ✅ Votre application est déjà configurée pour Render
- ✅ Déploiement automatique depuis GitHub
- ✅ PostgreSQL inclus
- ✅ SSL automatique
- ✅ Utilisez votre sous-domaine IONOS avec un CNAME

**Étapes** :
1. Déployez sur Render (déjà en cours)
2. Configurez le CNAME dans IONOS
3. Ajoutez le domaine dans Render
4. SSL automatique

### Option B : VPS IONOS (Si disponible)

Si vous avez un VPS IONOS et préférez l'utiliser :

1. **Prérequis** :
   ```bash
   # Installer Java 17
   sudo apt update
   sudo apt install openjdk-17-jdk
   
   # Installer PostgreSQL
   sudo apt install postgresql postgresql-contrib
   
   # Installer Nginx (reverse proxy)
   sudo apt install nginx
   ```

2. **Déployer l'application** :
   ```bash
   # Cloner le repository
   git clone https://github.com/AbdouazizDEV/Test-Technique-back.git
   cd Test-Technique-back/backend
   
   # Build l'application
   mvn clean package -DskipTests
   
   # Créer un service systemd
   sudo nano /etc/systemd/system/article-manager.service
   ```

3. **Configuration Nginx** :
   ```nginx
   server {
       listen 80;
       server_name test.abdouazizdiop.com;
       
       location / {
           proxy_pass http://localhost:8080;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
       }
   }
   ```

4. **SSL avec Let's Encrypt** :
   ```bash
   sudo apt install certbot python3-certbot-nginx
   sudo certbot --nginx -d test.abdouazizdiop.com
   ```

## 📝 Conclusion

**Pour votre application Spring Boot** :

1. **IONOS Hébergement Web** : ❌ **Non adapté** (pas de support Java/Spring Boot)
2. **IONOS VPS** : ✅ **Possible** mais configuration manuelle complexe
3. **Render** : ✅ **Recommandé** (déjà configuré, automatique, gratuit)

**Meilleure solution** : Utiliser **Render pour l'application** et **votre sous-domaine IONOS** pour pointer vers Render via un CNAME.

## 🔗 Ressources

- [Documentation Render - Custom Domains](https://render.com/docs/custom-domains)
- [IONOS VPS Documentation](https://www.ionos.fr/assistance/serveurs/vps/)
- [Guide Nginx Reverse Proxy](https://nginx.org/en/docs/http/ngx_http_proxy_module.html)
