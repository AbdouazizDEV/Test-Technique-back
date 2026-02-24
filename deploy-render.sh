#!/bin/bash

# Script de déploiement sur Render via l'API REST
# Alternative au Render CLI si celui-ci n'est pas disponible

set -e

# Couleurs pour les messages
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=========================================="
echo "🚀 Déploiement sur Render via API REST"
echo "=========================================="
echo ""

# Vérifier que RENDER_API_KEY est définie
if [ -z "$RENDER_API_KEY" ]; then
    echo -e "${RED}❌ Erreur: RENDER_API_KEY n'est pas définie${NC}"
    echo ""
    echo "Définissez votre API Key :"
    echo "  export RENDER_API_KEY=\"votre-api-key\""
    echo ""
    echo "Ou créez un fichier .render_api_key :"
    echo "  echo \"votre-api-key\" > .render_api_key"
    exit 1
fi

# URL de l'API Render
API_BASE="https://api.render.com/v1"

# Headers pour les requêtes API
HEADERS=(
    -H "Authorization: Bearer $RENDER_API_KEY"
    -H "Accept: application/json"
    -H "Content-Type: application/json"
)

echo -e "${GREEN}✅ API Key trouvée${NC}"
echo ""

# Fonction pour créer un service via Blueprint
deploy_blueprint() {
    echo "📦 Déploiement via Blueprint (render.yaml)..."
    echo ""
    
    # Vérifier que render.yaml existe
    if [ ! -f "render.yaml" ]; then
        echo -e "${RED}❌ Erreur: render.yaml non trouvé${NC}"
        exit 1
    fi
    
    echo -e "${YELLOW}ℹ️  Pour déployer via Blueprint, utilisez le Dashboard Render :${NC}"
    echo ""
    echo "1. Allez sur https://dashboard.render.com"
    echo "2. Cliquez sur 'New +' → 'Blueprint'"
    echo "3. Connectez votre repository GitHub"
    echo "4. Render détectera automatiquement render.yaml"
    echo "5. Configurez les variables d'environnement"
    echo "6. Cliquez sur 'Apply'"
    echo ""
    echo -e "${GREEN}✅ Le render.yaml est déjà configuré !${NC}"
}

# Fonction pour lister les services existants
list_services() {
    echo "📋 Liste des services Render :"
    echo ""
    curl -s "${API_BASE}/services" \
        "${HEADERS[@]}" | \
        jq -r '.[] | "\(.service.name) - \(.service.serviceDetails.url // "N/A")"' 2>/dev/null || \
        echo "Installation de jq recommandée pour un meilleur affichage"
}

# Fonction pour obtenir les logs d'un service
get_logs() {
    if [ -z "$1" ]; then
        echo -e "${RED}❌ Usage: $0 logs <service-name>${NC}"
        exit 1
    fi
    
    SERVICE_NAME="$1"
    echo "📜 Logs du service: $SERVICE_NAME"
    echo ""
    
    # Obtenir l'ID du service
    SERVICE_ID=$(curl -s "${API_BASE}/services" \
        "${HEADERS[@]}" | \
        jq -r ".[] | select(.service.name == \"$SERVICE_NAME\") | .service.id" 2>/dev/null)
    
    if [ -z "$SERVICE_ID" ]; then
        echo -e "${RED}❌ Service '$SERVICE_NAME' non trouvé${NC}"
        exit 1
    fi
    
    echo "Service ID: $SERVICE_ID"
    echo ""
    echo "Pour voir les logs en temps réel, utilisez le Dashboard Render :"
    echo "https://dashboard.render.com/web/$SERVICE_ID/logs"
}

# Menu principal
case "${1:-deploy}" in
    deploy|blueprint)
        deploy_blueprint
        ;;
    list|services)
        list_services
        ;;
    logs)
        get_logs "$2"
        ;;
    *)
        echo "Usage: $0 [deploy|list|logs <service-name>]"
        echo ""
        echo "Commandes:"
        echo "  deploy    - Affiche les instructions pour déployer via Blueprint"
        echo "  list      - Liste les services existants"
        echo "  logs      - Affiche les logs d'un service"
        exit 1
        ;;
esac
