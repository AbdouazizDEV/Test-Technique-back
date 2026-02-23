#!/bin/bash

# Script pour builder et tester l'image Docker de l'application

set -e

echo "=========================================="
echo "🐳 BUILD ET TEST DE L'IMAGE DOCKER"
echo "=========================================="
echo ""

# Couleurs pour les messages
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Vérifier que Docker est installé
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker n'est pas installé${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Docker détecté${NC}"
echo ""

# Vérifier les permissions Docker
if ! docker ps &> /dev/null; then
    echo -e "${YELLOW}⚠️  Problème de permissions Docker${NC}"
    echo ""
    echo "Solutions possibles :"
    echo "  1. Ajouter l'utilisateur au groupe docker :"
    echo "     sudo usermod -aG docker \$USER"
    echo "     newgrp docker"
    echo ""
    echo "  2. Utiliser sudo pour ce script :"
    echo "     sudo ./build-and-test-docker.sh"
    echo ""
    read -p "Voulez-vous continuer avec sudo ? (y/n) " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        DOCKER_CMD="sudo docker"
    else
        echo "Abandon."
        exit 1
    fi
else
    DOCKER_CMD="docker"
    echo -e "${GREEN}✅ Permissions Docker OK${NC}"
    echo ""
fi

# Aller dans le répertoire backend
cd "$(dirname "$0")"

echo "=========================================="
echo "📦 ÉTAPE 1 : BUILD DE L'IMAGE"
echo "=========================================="
echo ""

$DOCKER_CMD build -t article-manager-api:latest .

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ Image buildée avec succès !${NC}"
    echo ""
    echo "Image créée : article-manager-api:latest"
    echo ""
    
    # Afficher la taille de l'image
    echo "Taille de l'image :"
    $DOCKER_CMD images article-manager-api:latest --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}"
    echo ""
    
    echo "=========================================="
    echo "🧪 ÉTAPE 2 : OPTIONS DE TEST"
    echo "=========================================="
    echo ""
    echo "Vous pouvez maintenant :"
    echo ""
    echo "1. Tester avec docker-compose (recommandé) :"
    echo "   cd .."
    echo "   docker-compose up --build"
    echo ""
    echo "2. Tester l'image seule :"
    echo "   $DOCKER_CMD run -p 8080:8080 \\"
    echo "     -e SPRING_PROFILES_ACTIVE=production \\"
    echo "     -e DB_URL=jdbc:postgresql://host.docker.internal:5432/article_manager \\"
    echo "     -e DB_USERNAME=postgres \\"
    echo "     -e DB_PASSWORD=postgres \\"
    echo "     -e JWT_SECRET=U73dsIlmL5e6wfYslsop3TsVLPGxYeE9sDOjxxGXTOo= \\"
    echo "     article-manager-api:latest"
    echo ""
    echo "3. Taguer l'image pour un registry (ex: Docker Hub) :"
    echo "   $DOCKER_CMD tag article-manager-api:latest votre-username/article-manager-api:latest"
    echo "   $DOCKER_CMD push votre-username/article-manager-api:latest"
    echo ""
    echo "4. Vérifier que l'image est prête pour Render :"
    echo "   - L'image utilise Java 17 ✅"
    echo "   - L'image utilise un utilisateur non-root ✅"
    echo "   - Le port 8080 est exposé ✅"
    echo "   - EnvironmentPostProcessor est inclus ✅"
    echo ""
else
    echo ""
    echo -e "${RED}❌ Erreur lors du build${NC}"
    exit 1
fi
