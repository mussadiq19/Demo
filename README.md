# ⚡ Foresight — Intelligent Business Operations

> AI-powered B2B SaaS platform that keeps businesses competitive and workforces employed.  
> Built with Spring Boot 3.3 · React 18 · MariaDB · OpenAI GPT-4o

---

## What is Foresight?

Foresight is a dual-product platform that gives companies a live view of two critical blind spots:

**1. Business Risk Scanner**  
Continuously monitors your tech stack, supply chain, and market trends. Surfaces risks before they become crises — with AI-generated mitigation steps, not just alerts.

**2. Skills Gap Analyzer + Upskilling Roadmap**  
Maps your workforce's current skills against emerging industry demands. Auto-generates a personalised upskilling roadmap for every employee. This is a tool for human empowerment — not replacement.

Both products are powered by **OpenAI GPT-4o** via a clean adapter layer, run on a **Java 21 + Spring Boot 3.3** backend, and delivered through a **React 18** dashboard.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend runtime | Java 21 (virtual threads enabled) |
| Backend framework | Spring Boot 3.3 + Gradle Groovy |
| API style | REST + Spring Web |
| Security | Spring Security + JWT (JJWT 0.12.5) |
| Database | MariaDB (via Spring Data JPA + Hibernate) |
| Migrations | Flyway |
| Scheduler | Quartz (background AI scan jobs) |
| AI provider | OpenAI GPT-4o via WebClient |
| Frontend | React 18 + Vite 5 |
| State | Zustand (auth) + TanStack Query v5 (server state) |
| Forms | React Hook Form + Zod |
| Styling | Tailwind CSS + Inter font |
| Charts | Recharts |
| HTTP client | Axios with JWT interceptors |

---

## Project Structure

```
foresight/
├── backend/                          # Spring Boot application
│   ├── build.gradle                  # Gradle Groovy DSL
│   ├── settings.gradle
│   └── src/main/java/com/example/sovaibackend/
│       ├── config/                   # Security, JWT, WebClient, Quartz, OpenAPI
│       ├── common/                   # ApiResponse, exceptions, audit, UserPrincipal
│       └── domain/
│           ├── auth/                 # Login, register, JWT issuance
│           ├── company/              # Company profile + dashboard aggregation
│           ├── risk/                 # Risk scanner, AI scan job, risk CRUD
│           ├── skills/               # Skills upload, gap analysis, roadmap generation
│           └── notification/         # In-app notifications
│       └── infrastructure/
│           └── ai/                   # OpenAI WebClient adapter + prompt builders
│
└── frontend/                         # React + Vite application
    ├── index.html
    ├── tailwind.config.js
    └── src/
        ├── config/                   # Route constants, React Query key factory
        ├── lib/                      # Axios instance, QueryClient, Zod schemas
        ├── store/                    # Zustand: auth, notifications
        ├── services/                 # Pure HTTP functions (no logic)
        ├── hooks/                    # useDashboard, useRecentRisks, useSkillsGap, useRoadmap
        ├── components/               # Shared UI: Button, Card, Badge, Skeleton, EmptyState
        ├── features/                 # Domain UI: dashboard, risk, skills, roadmap, auth
        └── pages/                    # Thin route wrappers
```

---

## Getting Started

### Prerequisites

- Java 21 JDK
- Node.js 18+
- MariaDB running locally
- OpenAI API key (get one at platform.openai.com)

---

### 1. Clone the repo

```bash
git clone https://github.com/your-org/foresight.git
cd foresight
```

---

### 2. Configure environment

Create `backend/.env`:

```env
# MariaDB
DB_HOST=localhost
DB_PORT=3306
DB_NAME=foresight_db
DB_USER=your_db_user
DB_PASS=your_db_password

# OpenAI
AI_API_URL=https://api.openai.com/v1
AI_API_KEY=sk-proj-your-openai-key-here
AI_MODEL=gpt-4o

# JWT — generate with: openssl rand -base64 64
JWT_SECRET=your_generated_jwt_secret_here

# Monitoring (optional)
SENTRY_DSN=
```

Create the database:

```sql
CREATE DATABASE foresight_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

### 3. Run the backend

```bash
cd backend
./gradlew bootRun
```

Flyway will automatically create all tables on first run.  
Swagger UI available at: `http://localhost:8080/swagger-ui.html`

---

### 4. Run the frontend

```bash
cd frontend
npm install
npm run dev
```

App available at: `http://localhost:5173`

---

## Environment Variables Reference

| Variable | Description | Example |
|---|---|---|
| `DB_HOST` | MariaDB host | `localhost` |
| `DB_PORT` | MariaDB port | `3306` |
| `DB_NAME` | Database name | `foresight_db` |
| `DB_USER` | Database user | `root` |
| `DB_PASS` | Database password | `yourpassword` |
| `AI_API_URL` | OpenAI base URL | `https://api.openai.com/v1` |
| `AI_API_KEY` | OpenAI API key | `sk-proj-...` |
| `AI_MODEL` | Model to use | `gpt-4o` |
| `JWT_SECRET` | Base64 JWT signing secret (min 256-bit) | `K9mX2vP...` |
| `SENTRY_DSN` | Sentry error tracking (optional) | leave blank for dev |

---

## API Endpoints

### Auth
```
POST /api/auth/register       Register company + admin user
POST /api/auth/login          Returns JWT token
POST /api/auth/refresh        Refresh expired token
```

### Company
```
GET  /api/companies/{id}              Company profile
PUT  /api/companies/{id}              Update tech stack / supply chain info
GET  /api/companies/{id}/dashboard    Aggregated dashboard stats
```

### Risk Scanner
```
GET  /api/risks                       Paginated risk list (filter by severity, status)
GET  /api/risks/{id}                  Risk detail
POST /api/risks/scan                  Trigger on-demand AI scan
PUT  /api/risks/{id}/acknowledge      Mark as acknowledged
PUT  /api/risks/{id}/resolve          Mark as resolved
GET  /api/risks/stats                 Count by severity and category
```

### Skills Gap
```
POST /api/skills/upload               Bulk upload employee skills (CSV/JSON)
GET  /api/skills/gaps?companyId=      Company-wide gap analysis by department
GET  /api/skills/gaps/employee/{id}   Per-employee gap report
```

### Roadmap
```
POST /api/roadmaps/generate/{userId}  Generate AI upskilling roadmap
GET  /api/roadmaps/{userId}           Get roadmap with steps
PUT  /api/roadmaps/steps/{id}/complete Mark step as complete
```

### Notifications
```
GET  /api/notifications               User's notifications
PUT  /api/notifications/{id}/read     Mark as read
```

---

## How the AI Works

### Risk Scanner
Every 6 hours (or on-demand), a Quartz job collects the company's tech stack, supply chain info, and industry — and sends it to GPT-4o with this instruction:

> *"You are an enterprise risk intelligence engine. Help businesses PREPARE and PREVENT. Return only valid JSON: risks with title, description, severity, source, and mitigation."*

Results are persisted to the `risks` table and shown live on the dashboard.

### Skills Gap Analyzer
When triggered, the system collects each employee's current skills and proficiency levels, compares them against emerging industry demands, and asks GPT-4o to:

> *"Generate a skills gap analysis and upskilling roadmap. Keep employees skilled and employed. Return only valid JSON: gaps and a step-by-step roadmap."*

Each employee gets a personalised roadmap stored in the `roadmaps` and `roadmap_steps` tables.

---

## User Roles

| Role | Permissions |
|---|---|
| `ADMIN` | Full platform access |
| `COMPANY_ADMIN` | Trigger scans, upload skills, view company dashboard |
| `EMPLOYEE` | View own roadmap and skills gap only |

---

## Background Jobs

| Job | Schedule | What it does |
|---|---|---|
| `RiskScannerJob` | Every 6 hours | Runs AI risk scan for all companies |
| `SkillsAnalysisJob` | Every Monday 9am | Re-analyses skills gaps company-wide |

Both jobs can also be triggered on-demand via API.

---

## Database Schema

Core tables:

```
users · roles · companies
risks · risk_categories
skills · employee_skills
roadmaps · roadmap_steps
notifications · ai_cache
```

All schema changes are managed by Flyway migrations in:
```
backend/src/main/resources/db/migration/
```

Never change `spring.jpa.hibernate.ddl-auto` from `validate` — Flyway owns the schema.

---

## Design Principles

This codebase follows strict low-level design principles:

- **Controllers** — HTTP only. Return `ApiResponse<T>` always. Zero business logic.
- **Services** — All business logic. Inject interfaces, not implementations.
- **Repositories** — `JpaRepository` only. Custom queries use JPQL `@Query`.
- **DTOs** — All Java 21 records. Never expose JPA entities to the API layer.
- **MapStruct** — All entity ↔ DTO mapping. No manual mapping in services.
- **Virtual threads** — Enabled via `spring.threads.virtual.enabled=true`. No `@Async` anywhere.

---

## Contributing

1. Fork the repo
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a pull request

---

## License

MIT License — see `LICENSE` for details.

---

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://react.dev)
- [OpenAI API](https://platform.openai.com)
- [MariaDB](https://mariadb.org)
- [Tailwind CSS](https://tailwindcss.com)
- [TanStack Query](https://tanstack.com/query)

---

> Foresight is built on the belief that AI should make businesses stronger and people more valuable — not replace them.
