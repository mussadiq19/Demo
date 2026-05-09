# 🚀 Quick Start Guide - SovAI Platform

Get the full stack running in 5 minutes!

---

## Prerequisites Check

```bash
# Java 21+
java -version

# MariaDB running
mariadb -u luffy -pPlmoknijb@123 -e "SELECT 1;"

# Node.js 16+
node --version
npm --version
```

---

## Database Setup (Once)

```bash
# Create database and user
mariadb -u root -p <<EOF
CREATE DATABASE IF NOT EXISTS sovai_db;
CREATE USER IF NOT EXISTS 'luffy'@'localhost' IDENTIFIED BY 'Plmoknijb@123';
GRANT ALL PRIVILEGES ON sovai_db.* TO 'luffy'@'localhost';
FLUSH PRIVILEGES;
EOF
```

---

## Start Everything

### Terminal 1: Backend

```bash
cd /home/luffy/IdeaProjects/Demo/backend

# Set JWT secret (in development, can be simple)
export JWT_SECRET="dev-secret-key-change-in-production-make-it-long"

# Run backend
./gradlew bootRun

# Wait for: "Started BackendApplication"
# Should run on http://localhost:8080
```

### Terminal 2: Frontend

```bash
cd /home/luffy/IdeaProjects/Demo/frontend

# Install dependencies (first time only)
npm install

# Start frontend
npm start

# Should automatically open http://localhost:3000
```

---

## Test the App

### 1. Register
- Go to http://localhost:3000
- Click "Sign up here"
- Fill in form:
  - Name: `John Doe`
  - Email: `john@test.com`
  - Password: `TestPass123` (min 8 chars)
- Click "Create Account"
- You should see the Dashboard ✅

### 2. Check Authentication
- Open DevTools: `F12`
- Go to "Application" → "Local Storage"
- You should see: `authToken`, `userId`, `userRole`

### 3. Check Network
- Go to "Network" tab
- Reload page
- Look for `POST /api/auth/register` request
- Response should have `token`, `userId`, `role`

### 4. Test Logout
- Click logout button (bottom left)
- You should go back to login page
- localStorage should be cleared

---

## API Testing with cURL

### Register

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Jane Doe",
    "email": "jane@test.com",
    "password": "JanePass123",
    "companyId": null
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@test.com",
    "password": "TestPass123"
  }'

# Copy the token from response
export TOKEN="eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
```

### Get Current User

```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN"
```

---

## Troubleshooting

### Backend won't start

```bash
# Clear build cache
./gradlew clean

# Kill any existing process
lsof -ti:8080 | xargs kill -9

# Try again
./gradlew bootRun
```

### Frontend can't connect to backend

```bash
# Check if backend is running
curl http://localhost:8080/swagger-ui.html

# Check proxy in package.json
cat frontend/package.json | grep proxy

# Reinstall dependencies
cd frontend
rm -rf node_modules package-lock.json
npm install
```

### Database connection error

```bash
# Verify database exists
mariadb -u luffy -pPlmoknijb@123 -e "SHOW DATABASES;"

# Check migrations ran
mariadb -u luffy -pPlmoknijb@123 sovai_db -e "SHOW TABLES;"
```

### CORS error in browser

```
Access to XMLHttpRequest from origin 'http://localhost:3000' 
has been blocked by CORS policy
```

**Fix:**
1. Ensure backend is running on port 8080
2. Check SecurityConfig has correct origins
3. Restart backend

---

## Port Conflicts

### Backend on different port

```bash
./gradlew bootRun --args='--server.port=9090'
# Then update frontend .env.local
```

### Frontend on different port

```bash
PORT=3001 npm start
# Update .env.local to match
```

---

## What's Working ✅

- ✅ User Registration
- ✅ User Login
- ✅ JWT Token Generation
- ✅ Protected Routes
- ✅ Automatic Logout on Token Expiration
- ✅ CORS Enabled
- ✅ API Service Layer
- ✅ Auth State Management
- ✅ Form Validation
- ✅ Error Handling

---

## Useful Links

| Link | Purpose |
|------|---------|
| http://localhost:3000 | React Frontend |
| http://localhost:8080 | Spring Boot Backend |
| http://localhost:8080/swagger-ui.html | API Docs |
| http://localhost:8080/actuator/health | Backend Health |

---

## Environment Variables

### Backend (.env or export)
```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=sovai_db
DB_USER=luffy
DB_PASS=Plmoknijb@123
JWT_SECRET=dev-jwt-secret-key
JWT_EXPIRATION_MS=86400000
```

### Frontend (.env.local)
```
REACT_APP_API_BASE_URL=http://localhost:8080
REACT_APP_API_TIMEOUT=30000
```

---

## Next Steps

1. ✅ Start backend & frontend
2. ✅ Register new account
3. ✅ Verify token in browser storage
4. ✅ Test logout functionality
5. ✅ Check Network tab in DevTools
6. ✅ Read `INTEGRATION_GUIDE.md` for details

---

## Full Documentation

See `INTEGRATION_GUIDE.md` for:
- Detailed setup instructions
- Deployment guide
- Architecture diagrams
- Production considerations
- Troubleshooting guide

---

## Commands Cheat Sheet

```bash
# Backend
cd backend
./gradlew clean build -x test    # Build
./gradlew bootRun                # Run
./gradlew test                   # Test

# Frontend
cd frontend
npm install                      # Install deps
npm start                        # Dev server
npm run build                    # Production build
npm test                         # Run tests

# Database
mariadb -u luffy -pPlmoknijb@123 sovai_db    # Connect to DB
SHOW DATABASES;                               # List databases
SHOW TABLES;                                  # List tables in sovai_db

# Kill processes
lsof -ti:3000 | xargs kill -9    # Kill frontend
lsof -ti:8080 | xargs kill -9    # Kill backend
lsof -ti:3306 | xargs kill -9    # Kill MariaDB
```

---

## Test Credentials

After registration, you can use:
- **Email**: john@test.com
- **Password**: TestPass123

---

**Ready? Let's go! 🚀**

```bash
# Terminal 1
cd /home/luffy/IdeaProjects/Demo/backend
export JWT_SECRET="dev-secret"
./gradlew bootRun

# Terminal 2
cd /home/luffy/IdeaProjects/Demo/frontend
npm install  # First time only
npm start

# Open http://localhost:3000
```

---

**Happy coding!** 🎉

