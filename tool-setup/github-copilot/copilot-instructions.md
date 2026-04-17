# GitHub Copilot Custom Instructions — FORGE v1.0

> Copy this file to `.github/copilot-instructions.md` in your project repository.
> Edit every section marked [USER FILLS] before committing.
> GitHub Copilot reads this file automatically in VS Code and JetBrains IDEs.

---

## Project Identity

**Project**: [USER FILLS — Project name]
**Type**: [USER FILLS — Greenfield | Brownfield | Mainframe Modernization | Cloud Migration | API Modernization]
**Primary language**: [USER FILLS — Java 17 | Python 3.12 | Go 1.22 | TypeScript 5 | etc.]
**Framework**: [USER FILLS — Spring Boot 3.2 | FastAPI | Express | etc.]
**Team**: [USER FILLS — Team name or department]

---

## FORGE Framework Rules — Always Apply

You are assisting an engineering team operating under the FORGE framework (Framework for Orchestrated AI-Guided Engineering). Apply these rules to every code suggestion, explanation, and chat response.

**1. Never invent business logic.** If a business rule is needed but not present in the provided code or context, do not assume it. Flag the gap in a comment: `// FORGE: Business rule not found in context — verify before using.`

**2. Preserve existing behavior.** When completing or modifying existing code, do not silently change behavior. If a behavioral change is required, add a comment: `// FORGE: BEHAVIORAL CHANGE — [describe what changed and why].`

**3. Separate facts from assumptions.** In Copilot Chat responses, label information as FACT (if sourced from the codebase or context), ASSUMPTION (if inferred), or RECOMMENDATION (if suggested).

**4. Generate tests alongside code.** For every non-trivial class or function you generate, include or suggest corresponding unit tests. Tests must cover: happy path, boundary values, and error cases.

**5. Protect sensitive data.** Never suggest code that: (a) logs PII, PAN, CVV, credentials, or tokens; (b) transmits sensitive data without encryption; (c) stores credentials in code or configuration files checked into source control. Flag violations with: `// FORGE: [SECURITY CONCERN] — [describe the concern].`

**6. Follow architectural patterns.** See the `## Architectural Patterns` section below. Never suggest patterns that contradict the approved architecture. If the architecture is insufficient, note it with: `// FORGE: Architectural gap — consider an ADR for this.`

**7. Prefer modular, testable code.** Avoid static methods for logic that depends on state or external dependencies. Use dependency injection. Avoid deeply nested logic. Prefer small, named, single-purpose methods over anonymous lambdas for complex logic.

**8. Incremental over big-bang.** When suggesting refactors, prefer incremental steps that can be reviewed individually. Do not suggest large rewrites unless explicitly asked.

---

## Architectural Patterns

[USER FILLS — Replace the examples below with your project's actual patterns.]

### Package / Module Structure
```
[USER FILLS — e.g.,
com.example.service
  ├── domain/        ← Entities, value objects, domain services, port interfaces
  ├── application/   ← Use case orchestration, application services
  ├── infrastructure/← Adapters: REST controllers, JPA repositories, Kafka listeners
  └── configuration/ ← Spring @Configuration classes
]
```

### Dependency Direction
[USER FILLS — e.g., "Dependency direction: infrastructure → application → domain. The domain layer must have no dependencies on Spring, JPA, or any infrastructure framework. Use interfaces in the domain layer for all external dependencies."]

### API Design
[USER FILLS — e.g., "REST APIs follow OpenAPI 3.1. Use Spring MVC @RestController. All endpoints return ResponseEntity<>. Error responses use RFC 7807 ProblemDetail format. No business logic in controllers — delegates to application service."]

### Data Access
[USER FILLS — e.g., "All database access goes through Spring Data JPA repositories. No raw SQL outside of @Query annotations in repository interfaces. Entity classes are in infrastructure.persistence.entity — never exposed outside the infrastructure layer."]

### Error Handling
[USER FILLS — e.g., "All service exceptions extend DomainException (unchecked). A global @ControllerAdvice maps exceptions to HTTP responses. Never swallow exceptions silently — always log at WARN or ERROR with correlation ID."]

### Logging
[USER FILLS — e.g., "Use SLF4J with Logback. Structured JSON logging in production. Always include: correlationId, userId (if available), operation name. Never log PAN, CVV, passwords, or tokens. Log at entry and exit of service-layer methods at DEBUG level."]

---

## Code Generation Preferences

### Style
- [USER FILLS — e.g., "Follow Google Java Style Guide. Max line length: 120 characters."]
- [USER FILLS — e.g., "Use `var` for local variables where the type is obvious from the right-hand side."]
- [USER FILLS — e.g., "Prefer immutable objects (final fields, no setters on domain entities). Use Lombok @Value for value objects, @Builder for complex constructors."]

### Naming
- Classes: [USER FILLS — e.g., "PascalCase. Suffix: Service, Repository, Controller, Adapter, Port, Event, Command, Query"]
- Methods: [USER FILLS — e.g., "camelCase. Commands: verb-first (processPayment). Queries: noun or get/find-prefixed (findTransactionById)"]
- Variables: [USER FILLS — e.g., "camelCase. Boolean variables: is/has/can prefix (isAuthorized, hasExpired)"]
- Constants: [USER FILLS — e.g., "SCREAMING_SNAKE_CASE in Constants class per package"]
- Database columns: [USER FILLS — e.g., "snake_case, prefixed by domain area"]

### Testing
- Framework: [USER FILLS — e.g., "JUnit 5 + Mockito 5 + AssertJ"]
- Test naming: [USER FILLS — e.g., "methodName_scenario_expectedBehavior"]
- Test structure: [USER FILLS — e.g., "Given/When/Then comments in test body (AAA pattern)"]
- Integration tests: [USER FILLS — e.g., "Use @SpringBootTest with Testcontainers for database and messaging dependencies"]
- Do NOT use: [USER FILLS — e.g., "PowerMock, JUnit 4, Hamcrest (AssertJ only)"]

---

## What Copilot Should Never Do in This Project

- Never generate code with hardcoded credentials, API keys, passwords, or connection strings
- Never suggest logging PII, PAN, CVV, authentication tokens, or any sensitive customer data
- Never generate code that bypasses the security layer (e.g., skipping authentication checks for "simplicity")
- Never introduce new third-party libraries without a comment noting that an ADR is required
- Never change database schema without a migration script (Flyway/Liquibase)
- Never suggest patterns that violate the dependency direction rules above (e.g., domain layer importing from Spring)
- Never generate code that catches and silently discards exceptions (`catch (Exception e) {}` — always log)
- Never suggest implementing authorization logic inside business service methods — authorization belongs in the security layer
- [USER FILLS — add project-specific prohibitions]

---

## Domain Context

[USER FILLS — Provide project-specific domain knowledge that helps Copilot understand your business domain. This is the most important section for domain-specific code quality. The more you fill in here, the better suggestions you will get.]

### Business Domain
[USER FILLS — e.g., "This is a payment card authorization service. The core function is to receive an authorization request from an acquiring network, apply spending limits, velocity controls, and fraud rules, and return an approve or decline response within 150ms."]

### Key Domain Concepts
[USER FILLS — e.g.,
- **Authorization**: A real-time approve/decline decision on a payment transaction
- **PAN**: Primary Account Number — the card number. Always masked in logs (show last 4 digits only)
- **Clearing**: Post-authorization reconciliation — separate from authorization, handled by a different service
- **Limit**: A configurable threshold (daily spend limit, per-transaction limit, velocity limit)
- **Merchant Category Code (MCC)**: A 4-digit code identifying the type of merchant. Used in business rules.
]

### Key Business Rules
[USER FILLS — List the most important business rules so Copilot can validate generated code against them. e.g.,
- An authorization must be declined if the remaining balance is insufficient
- Daily spend limits reset at midnight UTC
- A transaction may not exceed the single-transaction limit regardless of available balance
- Contactless transactions above [threshold] require PIN verification
]

### External Integrations
[USER FILLS — e.g.,
- **Visa/Mastercard Network**: Receives ISO 8583 authorization requests via IBM MQ
- **Core Banking System**: Provides real-time balance via synchronous REST API (SLA: < 50ms)
- **Fraud Service**: Asynchronous fraud scoring via Kafka — does not block authorization
- **Notification Service**: Asynchronous push notifications via Kafka — does not block authorization
]

---

## Key Directories and Files

[USER FILLS — Map key directories to their purpose so Copilot understands the project structure. Example:]

| Path | Purpose |
|---|---|
| `src/main/java/com/example/domain/` | Domain model and business logic. No framework dependencies. |
| `src/main/java/com/example/application/` | Use case orchestration. Calls domain services and infrastructure ports. |
| `src/main/java/com/example/infrastructure/rest/` | REST controllers. No business logic here. |
| `src/main/java/com/example/infrastructure/persistence/` | JPA entities and repositories. |
| `src/test/java/` | All tests. Mirror the main package structure. |
| `docs/architecture/` | ADRs and architecture diagrams. Reference before adding new patterns. |
| `docs/business-rules/` | Extracted and validated business rules. Reference before coding. |
| `legacy-artifacts/` | Read-only legacy COBOL source. Never generate code that modifies this. |

---

## Dependencies in Use

[USER FILLS — List key approved dependencies so Copilot suggests the right libraries. Example:]

- Spring Boot 3.2 (spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-security, spring-boot-starter-actuator)
- Spring Kafka 3.x for Kafka integration
- PostgreSQL (via spring-boot-starter-data-jpa + postgresql driver)
- Flyway for database migrations
- Lombok for boilerplate reduction (@Value, @Builder, @Slf4j)
- MapStruct for DTO-to-domain mapping
- Testcontainers for integration tests
- Resilience4j for circuit breaking and retry
- [USER FILLS — add your actual dependencies]

---

## Review Checklist

Before accepting any Copilot suggestion for production code, verify:

- [ ] No hardcoded credentials or sensitive data
- [ ] No PII/PAN logged
- [ ] Follows dependency direction (domain ← application ← infrastructure)
- [ ] Exception handling is present and meaningful
- [ ] Unit tests accompany the code (or are suggested)
- [ ] No new libraries introduced without ADR
- [ ] Structured logging with correlation ID
- [ ] Conforms to naming conventions above
