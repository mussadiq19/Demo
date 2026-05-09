# SovAI Platform - Full Stack Setup & Integration Guide

## Overview

This document provides complete instructions for running the SovAI Platform locally with:
- **Backend**: Spring Boot 4.0.6 (Java 21) + Spring Security + JWT
- **Frontend**: React 18 with Axios API client
- **Database**: MariaDB
- **Authentication**: JWT-based with secure token management

---

## Prerequisites

### System Requirements
- **JDK 21+** (for backend): [Download](https://www.oracle.com/java/technologies/downloads/)
- **MariaDB 10.5+**: [Download](https://mariadb.org/download/)
- **Node.js 16+**: [Download](https://nodejs.org/)
- **npm 7+** or **Yarn**

### Verify Installation

```bash
java -version        # Should show Java 21+
mariadb --version    # Should show MariaDB 10.5+
node --version       # Should show Node 16+
npm --version        # Should show npm 7+
```

---

## Database Setup

### 1. Start MariaDB

**Linux/Mac:**
```bash
brew services start mariadb  # If installed via Homebrew
# or
sudo systemctl start mariadb
```

**Windows:**
- Use MariaDB GUI installer or
- Command line: `"C:\Program Files\MariaDB 10.X\bin\mysqld"`

### 2. Create Database and User

```bash
mariadb -u root -p <<EOF
CREATE DATABASE IF NOT EXISTS sovai_db;
CREATE USER IF NOT EXISTS 'luffy'@'localhost' IDENTIFIED BY 'Plmoknijb@123';
GRANT ALL PRIVILEGES ON sovai_db.* TO 'luffy'@'localhost';
FLUSH PRIVILEGES;
EXIT;
EOF
```

Verify connection:
```bash
mariadb -u luffy -pPlmoknijb@123 sovai_db -e "SELECT 1;"
```

---

## Backend Setup

### 1. Navigate to Backend Directory

```bash
cd /home/luffy/IdeaProjects/Demo/backend
```

### 2. Set Environment Variables

**Option A: Using .env file (recommended)**
```bash
# Copy example file
cp .env.example .env

# Edit .env with your values
# Linux/Mac
nano .env

# Windows
notepad .env
```

**Option B: Export environment variables**

```bash
# Linux/Mac
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=sovai_db
export DB_USER=luffy
export DB_PASS=Plmoknijb@123
export JWT_SECRET=your_very_long_secret_key_for_jwt_signing_minimum_256_bits_recommended
export JWT_EXPIRATION_MS=86400000

# Windows (PowerShell)
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="sovai_db"
$env:DB_USER="luffy"
$env:DB_PASS="Plmoknijb@123"
$env:JWT_SECRET="your_very_long_secret_key_for_jwt_signing_minimum_256_bits_recommended"
$env:JWT_EXPIRATION_MS="86400000"
```

### 3. Build the Backend

```bash
# Using Gradle wrapper (recommended)
./gradlew clean build

# Or if Gradle is installed globally
gradle clean build
```

Expected output should end with:
```
BUILD SUCCESSFUL in Xs
```

### 4. Run the Backend

```bash
# Using Gradle wrapper
./gradlew bootRun

# Or directly run the JAR
java -jar build/libs/sovai-backend-0.0.1-SNAPSHOT.jar
```

Expected output:
```
Started BackendApplication in X.XXX seconds (JVM running for X.XXX)
Tomcat started on port(s): 8080 with context path '/'
```

### 5. Verify Backend is Running

Open browser and go to:
```
http://localhost:8080/swagger-ui.html
```

You should see the Swagger API documentation.

---

## Frontend Setup

### 1. Navigate to Frontend Directory

```bash
cd /home/luffy/IdeaProjects/Demo/frontend
```

### 2. Set Environment Variables

**Copy and configure .env.local:**

```bash
# Copy example file
cp .env.example .env.local

# Edit with your values - should contain:
# REACT_APP_API_BASE_URL=http://localhost:8080
# REACT_APP_API_TIMEOUT=30000
```

The `.env.local` file is already created with the correct values.

### 3. Install Dependencies

```bash
npm install
# or
yarn install
```

This will install:
- React Router for navigation
- Axios for API calls
- React 18 and React DOM

### 4. Run the Frontend Development Server

```bash
npm start
# or
yarn start
```

Expected output:
```
Compiled successfully!
On Your Network: http://192.168.x.x:3000
Local: http://localhost:3000
```

The app will automatically open in your browser at `http://localhost:3000`.

---

## Testing the Integration

### 1. Access the Application

Navigate to: `http://localhost:3000`

You should see the login page.

### 2. Register a New Account

1. Click "Sign up here"
2. Fill in the form:
   - Full Name: `John Doe`
   - Email: `john@example.com`
   - Password: `TestPass123` (min 8 chars)
   - Confirm Password: `TestPass123`
3. Click "Create Account"

Expected: You should be redirected to the Dashboard

### 3. Verify Authentication Flow

1. Check browser console (F12 → Console):
   - You should see no CORS errors
   - No 401 errors

2. Check Network tab (F12 → Network):
   - Click login/register
   - Look for `POST /api/auth/register` or `POST /api/auth/login`
   - Response should contain: `token`, `userId`, `role`, `expiresAt`

3. Check localStorage (F12 → Application → localStorage):
   - Should contain: `authToken`, `userId`, `userRole`, `tokenExpiresAt`

### 4. Test Logout

1. Click the logout button in the bottom-left of the navigation
2. You should be redirected to the login page
3. localStorage should be cleared

### 5. Test Token in Requests

1. Register/Login again
2. Open DevTools (F12) → Network tab
3. Any API request should include headers:
   ```
   Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
   ```

---

## Project Structure

### Backend

```
backend/
├── build.gradle                    # Dependencies
├── src/main/
│   ├── java/com/example/sovaibackend/
│   │   ├── BackendApplication.java # Main app
│   │   ├── config/                # Configuration
│   │   │   ├── SecurityConfig.java # CORS + JWT setup
│   │   │   ├── JwtConfig.java     # JWT config
│   │   │   └── ...                # Other configs
│   │   ├── common/                # Common utilities
│   │   │   ├── security/          # JWT + Auth filters
│   │   │   ├── exception/         # Exception handling
│   │   │   └── response/          # API response format
│   │   └── domain/                # Business logic
│   │       ├── auth/              # Authentication
│   │       │   ├── controller/    # Auth endpoints
│   │       │   ├── service/       # Auth logic
│   │       │   ├── dto/           # Request/Response DTOs
│   │       │   ├── entity/        # User, Role entities
│   │       │   └── repository/    # Database access
│   │       └── ...                # Other domains
│   └── resources/
│       ├── application.yml        # Main config
│       ├── application-dev.yml    # Dev config
│       └── db/migration/          # Database migrations
├── .env.example                   # Environment template
└── README.md
```

### Frontend

```
frontend/
├── package.json                   # Dependencies
├── public/
│   └── index.html                # HTML template
├── src/
│   ├── App.js                    # Main app with routing
│   ├── index.js                  # Entry point
│   ├── components/
│   │   ├── Dashboard.js          # Main dashboard
│   │   ├── Login.js              # Login page
│   │   ├── Register.js           # Register page
│   │   ├── ProtectedRoute.js     # Auth guard
│   │   └── views/                # Page components
│   ├── contexts/
│   │   └── AuthContext.js        # Auth state management
│   ├── services/
│   │   ├── api.js                # Axios instance
│   │   └── authService.js        # Auth API calls
│   └── styles/
│       ├── global.css            # Global styles
│       └── auth.css              # Auth page styles
├── .env.local                    # Environment variables
└── .env.example
```

---

## Key Files Modified for Integration

### Backend Changes

1. **`config/SecurityConfig.java`**
   - ✅ Fixed CORS to allow `localhost:3000` with credentials
   - ✅ Configured JWT authentication filter
   - ✅ Set stateless session policy

2. **`domain/auth/controller/AuthController.java`**
   - ✅ Added `/api/auth/me` endpoint (get current user)
   - ✅ Added `/api/auth/logout` endpoint (for completeness)

3. **`common/security/JwtTokenProvider.java`** (existing)
   - JWT token generation and validation

### Frontend Changes

1. **`src/services/api.js`** (new)
   - Axios instance with automatic token injection
   - Response interceptor for token expiration handling

2. **`src/services/authService.js`** (new)
   - Register, login, logout, refresh token methods
   - Token storage in localStorage

3. **`src/contexts/AuthContext.js`** (new)
   - React Context for global auth state
   - useAuth hook for components

4. **`src/components/Login.js`** (new)
   - Login form with validation
   - Redirects to dashboard on success

5. **`src/components/Register.js`** (new)
   - Registration form
   - Password confirmation validation

6. **`src/components/ProtectedRoute.js`** (new)
   - Guards routes requiring authentication
   - Redirects to login if not authenticated

7. **`src/App.js`** (updated)
   - React Router setup
   - AuthProvider wrapper
   - Protected route configuration

8. **`package.json`** (updated)
   - Added dependencies: `react-router-dom`, `axios`

---

## Environment Variables

### Backend (.env or export)

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | MariaDB host | localhost |
| `DB_PORT` | MariaDB port | 3306 |
| `DB_NAME` | Database name | sovai_db |
| `DB_USER` | Database user | luffy |
| `DB_PASS` | Database password | Plmoknijb@123 |
| `JWT_SECRET` | Secret key for signing JWT | (must set) |
| `JWT_EXPIRATION_MS` | Token expiration in ms | 86400000 (24h) |
| `SOVAI_API_URL` | External AI service URL | (optional) |
| `SOVAI_API_KEY` | External AI service key | (optional) |

### Frontend (.env.local)

| Variable | Description | Default |
|----------|-------------|---------|
| `REACT_APP_API_BASE_URL` | Backend API URL | http://localhost:8080 |
| `REACT_APP_API_TIMEOUT` | Request timeout (ms) | 30000 |

---

## Troubleshooting

### Database Connection Error

**Error:**
```
Driver 'org.mariadb.jdbc.Driver' not found
```

**Solution:**
```bash
# Ensure MariaDB is running
mariadb -u luffy -pPlmoknijb@123 -e "SELECT 1;"

# Check database name in application.yml
# Should be: DB_NAME=sovai_db
```

### CORS Error

**Error:**
```
Access to XMLHttpRequest blocked by CORS policy
```

**Solution:**
1. Verify backend CORS config allows `http://localhost:3000`
2. Ensure frontend requests include credentials
3. Backend should send `Allow-Credentials: true`

### JWT Validation Error

**Error:**
```
Cannot set user authentication
```

**Solution:**
```bash
# Verify JWT_SECRET is set (should be >32 characters)
export JWT_SECRET="your_very_long_secret_key..."

# Restart backend
./gradlew bootRun
```

### Port Already in Use

**Error:**
```
Tomcat unable to start on port 8080
```

**Solution:**
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Or use different port
./gradlew bootRun --args='--server.port=9090'
```

### Module Not Found (Frontend)

**Error:**
```
Module not found: 'react-router-dom'
```

**Solution:**
```bash
cd frontend
npm install
npm install react-router-dom axios
```

---

## API Endpoints Reference

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login user | No |
| GET | `/api/auth/me` | Get current user | Yes |
| POST | `/api/auth/refresh` | Refresh token | Yes |
| POST | `/api/auth/logout` | Logout (cleanup) | Yes |

### Example Requests

**Register:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "companyId": null
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'
```

**Get Current User:**
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Production Deployment

### Before Going Live

1. **Set Strong JWT Secret:**
   ```bash
   export JWT_SECRET=$(openssl rand -base64 32)
   ```

2. **Update CORS Origins:**
   Edit `config/SecurityConfig.java`:
   ```java
   .setAllowedOrigins(List.of(
       "https://yourdomain.com",
       "https://www.yourdomain.com"
   ))
   ```

3. **Update Frontend API URL:**
   `.env.production.local`:
   ```
   REACT_APP_API_BASE_URL=https://api.yourdomain.com
   ```

4. **Use HTTPS:**
   - Certificate from Let's Encrypt or CA
   - Update all URLs to https://

5. **Environment-Specific Configs:**
   - Create separate `.env.prod` for backend
   - Create `.env.production.local` for frontend
   - Don't commit secrets to git

---

## Running Both Servers

### Terminal 1 - Backend

```bash
cd backend
export JWT_SECRET="your_jwt_secret_key"
./gradlew bootRun
# Runs on http://localhost:8080
```

### Terminal 2 - Frontend

```bash
cd frontend
npm start
# Runs on http://localhost:3000
```

### Terminal 3 - Database (if needed)

```bash
mariadb -u luffy -pPlmoknijb@123 sovai_db
```

---

## Summary of Integration Features

✅ **User Registration** - Create new account, auto-creates company
✅ **User Login** - JWT token generation and validation
✅ **Token Management** - Automatic token injection in requests
✅ **Protected Routes** - Dashboard requires authentication
✅ **Auto Logout** - Redirect to login on token expiration (401)
✅ **CORS Configured** - Frontend can communicate with backend
✅ **Error Handling** - Validation errors and auth errors displayed
✅ **State Management** - React Context for global auth state
✅ **Security** - JWT, password hashing, CORS enabled

---

## Next Steps

1. ✅ Start backend on port 8080
2. ✅ Start frontend on port 3000
3. ✅ Register a new account
4. ✅ Verify token in localStorage
5. ✅ Test logout functionality
6. ✅ Monitor Network tab in DevTools

Happy coding! 🚀

