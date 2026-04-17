# FORGE Project Context: Greenfield

Load this file alongside `constitution/01-core-principles.md` at the start of every AI session for a greenfield project.

---

## Project Type: Greenfield

**Definition:** A new system being built from scratch with no existing codebase to constrain design choices.

**Key Characteristics:**
- Freedom to choose the right tech stack and architecture patterns
- Responsibility to establish good patterns from the start (they become permanent)
- Risk of over-engineering — resist building for hypothetical future requirements
- No legacy behavior to preserve — but still need to understand the business domain

---

## AI Behavior for Greenfield Projects

**Design philosophy:**
- Start simple, add complexity only when a specific requirement demands it
- Cloud-native by default (containers, managed services, infrastructure as code)
- Security by default (encryption at rest/transit, least-privilege, no open defaults)
- Observable by default (structured logging, metrics, distributed tracing from day 1)
- Testable by default (TDD or at minimum test-alongside-code)

**What to prioritize:**
1. Get the domain model right before any code — understand the business first
2. Establish patterns early (package structure, error handling, logging) — they compound
3. Define API contracts before implementation — contract-first development
4. Automate everything from day 1 — CI/CD, test execution, code quality gates
5. Treat infrastructure as code — no manual cloud console configurations

**What to avoid:**
- YAGNI violations: building for features that aren't required yet
- Gold-plating: over-engineering for scale that may never come
- Missing basics: getting fancy before having solid tests, logging, and health checks
- Monolith avoidance reflex: a well-structured modular monolith beats a poorly designed microservices mess

---

## Default Architecture Pattern

For new services, default to:
```
Hexagonal Architecture (Ports and Adapters):
  api/           - REST controllers (thin, delegates to application layer)
  application/   - Application services (use cases, orchestration)
  domain/        - Business entities, value objects, policies, events
  adapters/      - Database repositories, messaging, external API clients
  config/        - Spring / framework configuration
  observability/ - Health checks, metrics, custom indicators
```

---

## Default Tech Stack (customize for your org)

| Layer | Default Choice | Alternative |
|---|---|---|
| Language | Java 21 (or Python 3.12 / TypeScript 5) | — |
| Framework | Spring Boot 3.3 | FastAPI / NestJS |
| Database | PostgreSQL (RDS Aurora) | MongoDB / DynamoDB |
| Messaging | Apache Kafka (MSK) | RabbitMQ / SQS |
| Container | Docker + Kubernetes (EKS) | ECS Fargate |
| IaC | AWS CDK (TypeScript) | Terraform |
| CI/CD | GitHub Actions | GitLab CI |
| Observability | OpenTelemetry + Grafana | Datadog |

---

## Greenfield SDLC Emphasis

Invest heavily in:
- Stage 02 (Requirements) — get this right; changing requirements mid-build is expensive
- Stage 03 (Architecture) — establish patterns; they'll be copied
- Stage 04 (Design) — detail before implementation

Move quickly through:
- Stage 00 (Pre-Engagement) — skip if no legacy system
- Stage 01 (Discovery) — focus on understanding the business domain, not an existing system

---

## Common Greenfield Anti-Patterns (AI Should Flag)

- Microservices for a team of 3 — use a modular monolith first
- Event sourcing for CRUD — use CRUD until you have a clear need for event sourcing
- GraphQL for a simple API — use REST until clients need flexible queries
- Kubernetes for a single service — use a simpler deployment until you need orchestration
- AI-generated architecture without domain understanding — always understand the business first
