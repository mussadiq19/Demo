#!/bin/bash

# SovAI Backend - Production Deployment Script
# This script builds and runs the application in production mode

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$PROJECT_ROOT/.env"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}╔════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║  SovAI Backend - Production Build & Deploy   ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════╝${NC}"

# Check if .env file exists for production
if [ ! -f "$ENV_FILE" ]; then
    echo -e "${RED}✗ .env file not found!${NC}"
    echo -e "${YELLOW}Please create .env file with production configuration${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Loading environment from .env${NC}"

# Load environment variables
export $(cat "$ENV_FILE" | grep -v '^#' | grep -v '^$' | xargs)

# Build the application
echo -e "\n${YELLOW}Building application...${NC}"
cd "$PROJECT_ROOT"
./gradlew clean build -x test --no-daemon

if [ $? -ne 0 ]; then
    echo -e "${RED}✗ Build failed!${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Build successful${NC}"

# Find the built JAR
JAR_FILE=$(find "$PROJECT_ROOT/build/libs" -name "*.jar" -type f | head -1)

if [ -z "$JAR_FILE" ]; then
    echo -e "${RED}✗ No JAR file found in build/libs!${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Found JAR: $(basename $JAR_FILE)${NC}"

# Display deployment information
echo -e "\n${YELLOW}Deployment Configuration:${NC}"
echo -e "  JAR File: ${GREEN}$JAR_FILE${NC}"
echo -e "  Database: ${GREEN}$DB_USER@$DB_HOST:$DB_PORT/$DB_NAME${NC}"
echo -e "  API URL: ${GREEN}$SOVAI_API_URL${NC}"
echo -e "  Port: ${GREEN}8080${NC}"

# Ask for confirmation
read -p "$(echo -e ${YELLOW})Proceed with production deployment? (yes/no)${NC} " -n 3 -r
echo
if [[ ! $REPLY =~ ^[Yy][Ee][Ss]$ ]]; then
    echo -e "${YELLOW}Deployment cancelled${NC}"
    exit 0
fi

# Run the application
echo -e "\n${GREEN}Starting application in production mode...${NC}"
echo -e "${YELLOW}Press Ctrl+C to stop${NC}\n"

java \
  -Dspring.profiles.active=prod \
  -Dspring.datasource.url="jdbc:mariadb://$DB_HOST:$DB_PORT/$DB_NAME" \
  -Dspring.datasource.username="$DB_USER" \
  -Dspring.datasource.password="$DB_PASS" \
  -Dsovai.api.base-url="$SOVAI_API_URL" \
  -Dsovai.api.api-key="$SOVAI_API_KEY" \
  -Djwt.secret="$JWT_SECRET" \
  -Xms512m \
  -Xmx1024m \
  -jar "$JAR_FILE"

