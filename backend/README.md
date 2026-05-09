# SovAI Backend (Spring Boot 3.2 + Gradle + MariaDB)

This repository contains the backend MVP for the SovAI Platform—a dual-product B2B SaaS solution featuring:

- **AI Business Risk Scanner**: Continuously monitors supply chain, tech stack, and market trends; flags risks in real time.
- **Skills Gap Analyzer**: Maps workforce skills against emerging industry demands; auto-generates per-employee upskilling roadmaps.

## Tech Stack

- Java 17 + Spring Boot 3.2
- Gradle (Groovy)
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- Quartz Scheduler
- MariaDB (latest stable)
- Flyway (DB migrations)
- MapStruct + ModelMapper
- OpenAPI 3 / Springdoc

## Prerequisites

- JDK 17+
- MariaDB 10.5+
- Gradle 7.0+ (or use the bundled wrapper)

## Setup

### 1. Configure Database

Create a MariaDB database and user:

```sql
CREATE DATABASE sovai_db;
CREATE USER 'dev'@'localhost' IDENTIFIED BY 'devpass';
GRANT ALL PRIVILEGES ON sovai_db.* TO 'dev'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Environment Variables

Copy `.env.example` to `.env` and update values:

```bash
cp .env.example .env
```

Update `DB_HOST`, `DB_USER`, `DB_PASS`, `SOVAI_API_URL`, `SOVAI_API_KEY`, and `JWT_SECRET` in your `.env` file.

### 3. Build and Run

Using installed Gradle:

```bash
gradle clean build

gradle bootRun

# Run with custom port
gradle bootRun --args='--server.port=9090'
```

## API Documentation

Once running, access Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

## Database Migrations

Flyway migrations are automatically executed on application startup. Migration files are located at:

```
src/main/resources/db/migration/
```

Current migrations:
- `V1__init_schema.sql` - Core tables (users, companies, risks, skills, roadmaps, notifications)
- `V2__seed_roles_and_categories.sql` - Pre-seed roles and risk categories
- `V3__add_roadmap_steps.sql` - Roadmap step relationship table

## Project Structure

```
src/main/java/com/sovai/platform/
├── SovaiPlatformApplication.java
├── config/
│   ├── SecurityConfig.java
│   ├── JwtConfig.java
│   ├── OpenApiConfig.java
│   ├── QuartzConfig.java
│   └── SovAIClientConfig.java
├── common/
│   ├── exception/
│   ├── response/
│   ├── audit/
│   ├── security/
│   └── util/
├── domain/
│   ├── auth/
│   ├── company/
│   ├── risk/
│   ├── skills/
│   └── notification/
└── infrastructure/
    └── sovai/
```

## Key Endpoints

### Authentication
- `POST /api/auth/register` - Register company + admin user
- `POST /api/auth/login` - Login and get JWT

### Risk Scanner
- `GET /api/risks` - List risks
- `POST /api/risks/scan` - Trigger on-demand scan
- `PUT /api/risks/{id}/acknowledge` - Mark risk acknowledged

### Skills Gap Analyzer
- `POST /api/skills/upload` - Bulk upload employee skills
- `GET /api/skills/gaps` - Get gap analysis for company
- `POST /api/roadmaps/generate/{userId}` - Generate AI roadmap

## Testing

Run unit tests:

```bash
gradle test
```

## Design Principles

- **Single Responsibility**: Controllers handle HTTP, services handle business logic, repositories handle data access.
- **Open/Closed**: New analyzers extend common interfaces without modifying existing code.
- **Interface Segregation**: Separate interfaces for different service concerns (e.g., `RiskService` vs `RiskScannerJobService`).
- **Dependency Inversion**: All services inject interfaces, enabling easy mocking and testing.

## Contributing

Follow the design principles outlined above. Use DTOs for API contracts, MapStruct for entity mapping, and keep business logic in services.

## License

Proprietary - SovAI Platform
