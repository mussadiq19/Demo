#!/bin/bash

# SovAI Backend - Environment Setup Script
# This script loads environment variables from .env file and runs the application

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$PROJECT_ROOT/.env"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}╔═══════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║   SovAI Backend - Environment Setup      ║${NC}"
echo -e "${GREEN}╚═══════════════════════════════════════════╝${NC}"

# Check if .env file exists
if [ ! -f "$ENV_FILE" ]; then
    echo -e "${RED}✗ .env file not found!${NC}"
    echo -e "${YELLOW}Creating .env from .env.example...${NC}"
    if [ -f "$PROJECT_ROOT/.env.example" ]; then
        cp "$PROJECT_ROOT/.env.example" "$ENV_FILE"
        echo -e "${GREEN}✓ .env file created${NC}"
        echo -e "${YELLOW}  Please edit $ENV_FILE with your configuration${NC}"
        exit 1
    else
        echo -e "${RED}✗ .env.example not found either!${NC}"
        exit 1
    fi
fi

echo -e "${GREEN}✓ .env file found${NC}"

# Load environment variables
echo -e "${YELLOW}Loading environment variables...${NC}"
export $(cat "$ENV_FILE" | grep -v '^#' | grep -v '^$' | xargs)

# Get the profile to use (default: local)
PROFILE="${1:-local}"

# Validate profile
if [ ! -f "$PROJECT_ROOT/src/main/resources/application-$PROFILE.yml" ]; then
    echo -e "${RED}✗ Profile 'application-$PROFILE.yml' not found!${NC}"
    echo -e "${YELLOW}Available profiles:${NC}"
    ls "$PROJECT_ROOT/src/main/resources/application-*.yml" | xargs -n1 basename | sed 's/application-/  - /' | sed 's/.yml//'
    exit 1
fi

echo -e "${GREEN}✓ Profile: $PROFILE${NC}"

# Display current configuration
echo -e "\n${YELLOW}Configuration Summary:${NC}"
echo -e "  Database: ${GREEN}$DB_USER@$DB_HOST:$DB_PORT/$DB_NAME${NC}"
echo -e "  API: ${GREEN}$SOVAI_API_URL${NC}"
echo -e "  JWT Secret: ${GREEN}${#JWT_SECRET} characters${NC}"

# Check if running in dev/test mode
if [ "$PROFILE" == "local" ] || [ "$PROFILE" == "dev" ]; then
    echo -e "  ${YELLOW}Note: SQL Query Logging is ENABLED${NC}"
fi

echo -e "\n${GREEN}Starting application with profile: $PROFILE${NC}"
echo -e "${YELLOW}Press Ctrl+C to stop${NC}\n"

# Run the application
cd "$PROJECT_ROOT"
./gradlew bootRun --args="--spring.profiles.active=$PROFILE"

