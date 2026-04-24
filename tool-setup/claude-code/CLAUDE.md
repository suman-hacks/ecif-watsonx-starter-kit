# Project: [PROJECT NAME]
# Framework: FORGE v2.0 + ATOM Microservices Chassis

> Copy this file to the root of your project repository as `CLAUDE.md`.
> Fill in every `[USER FILLS]` section. Commit it so the whole team benefits.
> Claude Code reads this file automatically every session.

---

## Project Overview

[USER FILLS: 2–3 sentences. What does this system do? Who uses it? Why does it exist?

Example: "This is the Payment Authorization Service — it processes real-time card authorization
requests from acquiring networks, applies business rules and spending limits, and returns
approve/decline decisions within 150ms. The system is being modernized from COBOL/z/OS
to Java/Spring Boot using the ATOM chassis."]

---

## Project Type

[USER SELECTS ONE — delete the others]
- Greenfield — building a new system from scratch
- Brownfield (Distributed) — extending or modernizing an existing Java/Python/Node codebase
- Mainframe Modernization — transforming COBOL/PL1/Assembler/JCL on z/OS to Java via ATOM
- Cloud Migration — migrating an existing application to cloud
- API Modernization — exposing legacy functionality via REST/AsyncAPI

---

## Tech Stack

### Source / Legacy (if applicable)
[USER FILLS: e.g., COBOL II, CICS TS 5.6, IMS DB 15, DB2 for z/OS 12, JCL, VSAM, IBM MQ 9.3]

### Target (ATOM Chassis)
- **Language:** Java 17 LTS
- **Framework:** Spring Boot 3.x (ATOM chassis)
- **Build tool:** [Maven 3.9 | Gradle 8.x]
- **Databases:** [USER FILLS: e.g., PostgreSQL 16, IBM Db2 LUW 11.5]
- **Messaging:** [USER FILLS: e.g., Apache Kafka 3.7, IBM MQ 9.3]
- **Cloud platform:** [USER FILLS: e.g., AWS EKS, Azure AKS, IBM Cloud]
- **CI/CD:** [USER FILLS: e.g., GitHub Actions, Jenkins, Tekton]
- **Observability:** [USER FILLS: e.g., Splunk (via ATOM structured logging), Prometheus, Datadog]

---

## Context Engine — Always Load

These files are loaded every session. Reference them when making any code or architecture decision.

### `.context/CORE_SKILLS.md`
Universal engineering guardrails — security, code quality, testing, observability standards.
**Always active.** Never violate a rule in this file without flagging it explicitly.

### `.context/ATOM_CHASSIS.md`
ATOM microservices chassis patterns — annotations, layers, response format, resilience config.
**Load for any ATOM service work.** When generating code, always apply ATOM patterns.

### `.context/MODERNIZATION.md`
Legacy modernization rules — COBOL→Java data type mapping, Strangler Fig pattern, behavioral preservation.
**Load for any legacy migration work.**

---

## FORGE Constitution — Always Active

These 12 rules are non-negotiable and apply to every response in every session.

**Rule 1 — Do not invent business logic.**
Never generate code that encodes a business rule not present in a provided source artifact. If a rule is needed but not documented, flag it as an open question.

**Rule 2 — Preserve existing business behavior.**
When modernizing, preserve existing behavior precisely unless a change is explicitly requested and documented in the assumption register.

**Rule 3 — Separate facts from assumptions from recommendations.**
Label every significant claim: `FACT` (from artifact, cite it) | `ASSUMPTION` (stated with confidence) | `RECOMMENDATION` (labeled as such) | `OPEN QUESTION` (needs clarification).

**Rule 4 — Prefer modular, testable, observable code.**
All generated code must be structured for testability (DI, no hidden state, interface-based design). Every generated module includes unit tests. All code emits structured logs.

**Rule 5 — Traceability is mandatory.**
Every generated output must be traceable to: a source artifact, a requirement ID (BR-NNN, US-NNN), or a declared assumption. Never generate output that cannot be justified to a human reviewer.

**Rule 6 — Flag ambiguity before proceeding.**
If a task contains ambiguity or conflicting requirements, stop and ask focused clarifying questions. Do not make silent assumptions about ambiguous inputs.

**Rule 7 — Data protection is absolute.**
Never include, generate, or accept: real customer PII, PAN (card numbers), CVV, passwords, API keys, tokens, or production connection strings. If asked to include such content, refuse and explain.

**Rule 8 — Default to incremental change.**
Prefer reversible, incremental changes over big-bang transformations. For modernization, prefer Strangler Fig over complete replacement. Flag any proposed change that is hard to reverse.

**Rule 9 — Follow approved ATOM architectural patterns.**
All generated code must conform to ATOM patterns in `.context/ATOM_CHASSIS.md`. Do not introduce new patterns or libraries without an ADR. Flag situations where ATOM patterns are insufficient.

**Rule 10 — Produce human-reviewable outputs.**
All generated code must be readable, commented at decision points, and structured so a domain-knowledgeable engineer can verify correctness without running it.

**Rule 11 — Observable systems.**
All generated code must emit structured logs with correlation ID, service ID, operation name, and business-meaningful fields. Never generate code without observability.

**Rule 12 — Honest uncertainty.**
When uncertain, say so explicitly with a confidence level (HIGH/MEDIUM/LOW/NONE) and what source material would resolve the uncertainty. Never present a guess as a fact.

---

## ATOM Architectural Patterns

### Package Structure
```
com.[org].[domain].[service-name]
├── api/
│   ├── controller/          ← @RestController. Returns ResponseEntity<ApiResponse<T>>.
│   ├── dto/                 ← Immutable request/response DTOs (@Value, Bean Validation)
│   └── mapper/              ← MapStruct mappers (DTO ↔ Domain)
├── application/
│   └── service/             ← @AtomService. Business logic. No persistence logic.
├── domain/
│   ├── model/               ← Entities, value objects, aggregates
│   ├── service/             ← Pure domain services (no framework dependencies)
│   ├── port/
│   │   ├── inbound/         ← Use case interfaces
│   │   └── outbound/        ← Repository and external client interfaces
│   └── exception/           ← Domain exceptions (extend RuntimeException)
├── infrastructure/
│   ├── persistence/         ← JPA entities + @AtomRepository implementations
│   ├── client/              ← External HTTP clients (with @CircuitBreaker)
│   ├── messaging/           ← Kafka listeners/producers
│   └── config/              ← @Configuration classes
└── [ServiceName]Application.java
```

### Dependency Direction
Infrastructure → Application → Domain. The domain layer must have ZERO Spring/JPA/Kafka imports.

### ATOM Annotations (Required)
- `@AtomService` — on all application service classes (not plain `@Service`)
- `@AtomRepository` — on all repository implementation classes
- `@AtomValidated` — on controller request body parameters (not plain `@Valid`)
- `@CircuitBreaker(name="service-name")` — on all downstream HTTP calls
- `@Slf4j` — on all classes that log (Lombok)

### Response Format
All controllers return `ResponseEntity<ApiResponse<T>>`. Never return raw DTOs. Never build custom error responses — ATOM's `@ControllerAdvice` handles error mapping.

### Logging (Mandatory)
```java
// Always structured key-value pairs — never string concatenation in logs
log.info("Authorization processed",
    "operation", "authorize",
    "transactionId", txId,
    "decision", decision,
    "durationMs", duration);
```
Never log: PAN, CVV, passwords, tokens, SSN. Log only masked identifiers.

### Resilience
All downstream HTTP calls annotated with `@CircuitBreaker(name="...", fallbackMethod="...")`.
Fallback methods must never throw — return a safe default value.
Config in `application.yml` under `resilience4j.circuitbreaker.instances`.

### Constructor Injection
Always use constructor injection. Never `@Autowired` field injection. Use Lombok `@RequiredArgsConstructor`.

---

## AI Behavior Rules

- **Be source-grounded:** When making claims about the system, cite the source file and line. "According to `AUTHZ0100.cbl` line 245, the timeout is 30 seconds." Not: "The timeout is probably 30 seconds."
- **Behavioral changes:** If generated code behaves differently from legacy code in any way, explicitly call this out: `// [BEHAVIORAL CHANGE: CBD-NNN] Legacy did X; modern does Y. Approval required.`
- **Generate tests alongside code:** For every non-trivial class or function, generate corresponding unit tests in the same response. Tests cover: happy path, all business rules (BR-NNN), boundary values, error conditions.
- **Flag security concerns immediately:** Any security vulnerability gets: `[SECURITY: SG-N] Issue: ... Location: ... Recommendation: ...`
- **Confirm stage gates:** Before generating code, confirm that: Stage 1 (analysis) is complete, business rules are extracted and numbered, and target design is approved.

---

## Code Generation Standards

### Language and Style
- **Language version:** Java 17 LTS (preview features disabled)
- **Style:** Google Java Style Guide — 120-char line limit
- **Formatter:** [USER FILLS: e.g., google-java-format 1.22, enforced in CI]
- **Lombok:** `@Value` (immutable), `@Builder`, `@Slf4j`, `@RequiredArgsConstructor`

### Test Framework
- **Unit tests:** JUnit 5 + Mockito 5 + AssertJ
- **Integration tests:** @SpringBootTest + Testcontainers 3
- **Naming:** `methodName_scenario_expectedBehavior`
- **Structure:** Arrange / Act / Assert (AAA) with explicit comments
- **Coverage:** 80% line, 90% branch on business logic. Enforced by JaCoCo.

### Naming Conventions
- **Classes:** PascalCase. Suffixes: `Service` (application), `DomainService` (domain), `Repository` (port interface), `JpaRepository` (implementation), `Controller`, `Adapter`, `Client`, `Config`, `Event`, `Command`
- **Methods:** camelCase. Commands: verb-first (`processAuthorization`). Queries: `get`/`find`-prefixed
- **Constants:** `SCREAMING_SNAKE_CASE` in dedicated `Constants` class per domain area
- **Database tables:** snake_case, domain-prefixed: `authz_transactions`, `authz_limits`
- **Kafka topics:** kebab-case, environment-prefixed: `prod.authz.transaction-authorized`

### Dependencies
[USER FILLS: List approved dependencies. Example:]
- Spring Boot 3.x BOM (manages versions for starter-web, starter-data-jpa, starter-security, starter-actuator)
- ATOM chassis library: `[org]-atom-chassis:[version]`
- Kafka: `spring-kafka`
- Resilience4j: `resilience4j-spring-boot3`
- PostgreSQL driver: `org.postgresql:postgresql`
- Flyway: `org.flywaydb:flyway-core`
- Lombok: `org.projectlombok:lombok`
- MapStruct: `org.mapstruct:mapstruct`
- Testcontainers: `testcontainers-bom:[version]`

---

## Repository Structure

[USER FILLS: Map key directories to their purpose. Example:]

```
[project-repo]/
├── .context/                      ← FORGE Context Engine (copy from FORGE repo)
│   ├── CORE_SKILLS.md
│   ├── ATOM_CHASSIS.md
│   └── MODERNIZATION.md
├── CLAUDE.md                      ← This file
├── .github/
│   ├── copilot-instructions.md    ← GitHub Copilot configuration
│   └── workflows/                 ← CI/CD pipelines
├── docs/
│   ├── architecture/              ← ADRs (adr-NNN-title.md)
│   ├── business-rules/            ← Extracted and validated BR-NNN register
│   └── runbooks/                  ← Operational runbooks
├── legacy-artifacts/              ← READ ONLY. Never modify.
│   ├── cobol/                     ← Source COBOL programs
│   ├── jcl/                       ← JCL scripts
│   └── copybooks/                 ← COBOL copybooks
├── src/main/java/                 ← Java source
├── src/test/java/                 ← Test source
├── infra/
│   ├── terraform/                 ← Infrastructure as code
│   └── helm/                      ← Kubernetes Helm charts
└── traceability/
    ├── assumption-register.md     ← All declared assumptions (BR-NNN, CBD-NNN)
    └── decision-log.md            ← All architectural decisions
```

---

## FORGE Skills (Slash Commands)

These slash commands trigger FORGE workflows. See `tool-setup/claude-code/SKILLS.md` for full documentation.

| Command | Triggers |
|---|---|
| `/analyze-legacy [file or paste]` | Stage 1 Legacy Understanding workflow |
| `/extract-rules [source]` | Business rule extraction and numbering |
| `/generate-service [spec]` | Stage 3 Code Generation (requires Stages 1+2 complete) |
| `/generate-atom-service` | Scaffold new ATOM service from scratch |
| `/review-code [files]` | Stage 4 Review and Hardening workflow |
| `/create-tests [code or spec]` | Test suite generation |
| `/map-data [source + target]` | COBOL copybook → Java domain model mapping |
| `/create-adr [description]` | Architecture Decision Record generation |
| `/pre-engagement` | Run full 5-task pre-engagement analysis |
| `/explain-legacy [code]` | Plain English explanation for non-technical audience |
| `/runbook [service]` | Operational runbook generation |
| `/generate-openapi-spec [design doc]` | Generate OpenAPI 3.1 spec from Stage 2 design (spec-first) |
| `/package-delivery [stage + artifacts]` | Package stage artifacts into a traceable delivery package |

---

## FORGE Workflow Stages

```
Stage 0: Pre-Engagement Analysis  (/pre-engagement)
  → Analyze codebase before workshop. Produce TX_ARCH.md, TX_DECISION_INVENTORY.md,
    TX_INTEGRATIONS.md, TX_RISK_MAP.md, TX_POC_OPTIONS.md

Stage 1: Legacy Understanding  (/analyze-legacy, /extract-rules)
  → Read and analyze source code. Extract BR-NNN rules. Produce open questions.
  → GATE: All open questions resolved. BA/domain expert validates rules register.

Stage 2: Target Mapping  (/create-adr, /map-data, /generate-openapi-spec)
  → Map legacy components to ATOM services. Define API contracts (spec-first). Write ADRs.
  → GATE: API spec published. Design reviewed and approved by Tech Lead.

Stage 3: Code Generation  (/generate-service)
  → Generate ATOM code from Stage 2 specifications. Generate tests alongside.
  → GATE: All tests pass. /review-code check passes.

Stage 4: Review and Hardening  (/review-code, /package-delivery)
  → Human code review against Stage 2 spec. Security review. Integration testing.
  → GATE: All review-gate items in governance/review-gates.md satisfied.
  → DELIVERY: /package-delivery packages artifacts + traceability matrix for sign-off.
```

**Non-negotiable:** Never generate production code (Stage 3) without completing Stages 1 and 2.

---

## Files to Never Modify

- `legacy-artifacts/` — All legacy source code is read-only. Source of truth for business behavior.
- `traceability/assumption-register.md` — Claude may suggest additions but must not autonomously modify.
- `docs/architecture/adr-*.md` — Approved ADRs are immutable. New decisions = new ADRs.
- `.github/workflows/*.yml` — CI/CD pipelines require DevSecOps approval.
- `infra/terraform/` — Infrastructure changes require explicit approval.

---

## Domain Vocabulary

[USER FILLS: Define project-specific terms. AI will use these precisely. Example:]

| Term | Definition | Do NOT use |
|---|---|---|
| Authorization | A real-time approve/decline decision on a payment transaction | "approval", "auth" |
| PAN | Primary Account Number — the 16-digit card number | "card number" |
| Clearing | Post-authorization reconciliation | "settlement" (different process) |
| Cardholder | The customer who owns the payment card | "user", "customer" |
| Limit | A configurable spending or velocity threshold | "cap", "maximum" |

---

## Known Context

[USER FILLS — list decisions already made, non-negotiables, and rejected options]

### Decisions Already Made
- [USER FILLS: e.g., "Target is Java 17. Python evaluated and rejected in ADR-003."]
- [USER FILLS: e.g., "Database is PostgreSQL on RDS. Oracle rejected for cost (ADR-007)."]
- [USER FILLS: e.g., "Kafka for async messaging. Do not suggest RabbitMQ."]

### Non-Negotiable Constraints
- [USER FILLS: e.g., "Authorization P99 SLA: < 150ms. Generated code must account for this."]
- [USER FILLS: e.g., "PCI DSS Level 1 required. No cardholder data leaves PCI-scoped network."]

### Tried and Rejected
- [USER FILLS: e.g., "Event sourcing evaluated and rejected — ADR-012. Do not suggest."]

---

## Session Log

| Date | Task | Completed | Artifacts |
|---|---|---|---|
| [DATE] | [Brief] | [What was done] | [Files created/updated] |
