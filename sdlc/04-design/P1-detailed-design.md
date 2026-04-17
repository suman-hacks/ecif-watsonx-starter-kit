# P1: Component Detailed Design

## When to Use This Prompt
After Gate 3 (Architecture Complete) is approved. Use once per service or component to produce implementable specifications before code generation begins.

## What You Get
- Component breakdown with interfaces defined
- Method signatures with inputs/outputs
- Business rule to method mapping
- Error handling specification
- Dependency injection map

---

## Prompt

```text
You are a FORGE Solution Architect producing a detailed component design. This is a specification — no code generated here.

INPUTS
Service name: [SERVICE-NAME]
Architecture source: [Paste the service section from Agent 04 output — or service-map.md]
Business rules this service implements: [Paste the relevant BR-NNN rules from Agent 03 output]
Technology stack: [e.g., Java 21, Spring Boot 3.2, JPA, PostgreSQL]
Package base: [e.g., com.acme.authorization]

TASK
Produce a complete detailed design for [SERVICE-NAME] using the following format:

---

# Detailed Design: [SERVICE-NAME]
Designed by: [AI tool]  Date: [date]
Architecture trace: [reference ADR or service-map section]
Business rules: [list BR-NNN this design implements]
Human review required before code generation.

## Component Overview

### Package Structure
```
com.acme.[service]/
├── api/
│   └── v1/
│       ├── [Resource]Controller.java         — REST endpoint handler
│       ├── [Resource]RequestDto.java          — inbound payload
│       ├── [Resource]ResponseDto.java         — outbound payload
│       └── [Resource]DtoMapper.java           — DTO ↔ domain conversion
├── application/
│   └── [UseCase]UseCase.java                 — orchestrates domain objects
├── domain/
│   ├── model/
│   │   ├── [Entity].java                     — aggregate root
│   │   └── [ValueObject].java                — value object (immutable)
│   ├── port/
│   │   ├── in/
│   │   │   └── [UseCase]Port.java            — inbound port (interface)
│   │   └── out/
│   │       ├── [Entity]Repository.java       — persistence port (interface)
│   │       └── [External]Client.java         — external call port (interface)
│   └── service/
│       └── [DomainService].java              — stateless domain logic
├── adapters/
│   ├── persistence/
│   │   ├── [Entity]JpaRepository.java        — Spring Data interface
│   │   ├── [Entity]JpaEntity.java            — JPA entity
│   │   ├── [Entity]PersistenceAdapter.java   — implements repository port
│   │   └── [Entity]EntityMapper.java         — JPA entity ↔ domain model
│   └── external/
│       └── [External]HttpAdapter.java        — implements external client port
├── config/
│   └── [Service]Config.java                  — Spring configuration
└── observability/
    └── [Service]HealthIndicator.java         — health check

## API Layer Design

### [Resource]Controller
**Responsibility:** Accept HTTP requests, validate input format, delegate to use case, format response.
**Must NOT contain:** Business logic, database queries, domain decisions.

| Method | Path | Request Type | Response Type | Business Rule | HTTP Status |
|---|---|---|---|---|---|
| POST | /api/v1/[resource] | [Resource]RequestDto | [Resource]ResponseDto | BR-001 | 200 / 400 / 422 |
| GET | /api/v1/[resource]/{id} | — | [Resource]ResponseDto | — | 200 / 404 |

**Input validation rules (applied before business logic):**
- [field]: [rule] — e.g., cardId: required, max 19 chars, matches PAN format
- [field]: [rule]

**[Resource]RequestDto:**
```
[field name]: [type] — [required/optional] — [validation] — [example value]
```

**[Resource]ResponseDto:**
```
[field name]: [type] — [always present / present when X] — [example]
```

**Error Response (RFC 7807 Problem Detail):**
```json
{
  "type": "https://errors.[company].com/[error-code]",
  "title": "[Short description]",
  "status": 422,
  "detail": "[Human-readable explanation]",
  "instance": "/api/v1/authorizations/[id]",
  "businessRuleViolated": "BR-001"
}
```

---

## Application Layer Design

### [UseCase]UseCase
**Responsibility:** Orchestrate domain objects and ports to fulfill one business use case.
**Implements:** [UseCase]Port (inbound port interface)

**Method:** execute([Input]) → [Output]
```
Step 1: Validate inputs not already validated at API layer
Step 2: Load [Entity] via [Entity]Repository (out port)
Step 3: [Business operation description] — calls [DomainService] or domain method
Step 4: [Next operation] — may call external port if [condition]
Step 5: Persist result via [Entity]Repository
Step 6: Publish event via [EventPublisher]Port if [condition]
Step 7: Return [Output]

Failure paths:
- [Entity] not found → throw [Entity]NotFoundException
- [Business rule violation] → throw [RuleViolation]Exception with BR-NNN code
- External call fails → [retry / fallback / fail fast] — see NFR
```

**Transaction scope:** [@Transactional] — applies to steps 1-7 unless stated otherwise

---

## Domain Layer Design

### [Entity] (Aggregate Root)
**Responsibility:** Enforce business invariants for [bounded context].
**Owns:** [list of value objects and child entities it aggregates]

| Method | Inputs | Output | Business Rule | Throws |
|---|---|---|---|---|
| authorize([params]) | [types] | Decision | BR-001, BR-003 | CardBlockedException, LimitExceededException |
| [method] | [types] | [type] | BR-NNN | [exception] |

**Invariants (always true for a valid [Entity]):**
- [Invariant 1] — e.g., status is never null
- [Invariant 2]

### [ValueObject] (Immutable)
**Responsibility:** Represent [concept] with built-in validation.
**Validation rules applied at construction:**
- [Rule 1]
- [Rule 2]

---

## Port Design

### Inbound Ports (what the application exposes)
| Interface | Method | Parameters | Return | Notes |
|---|---|---|---|---|
| [UseCase]Port | execute | [Input] | [Output] | Used by controller and event consumers |

### Outbound Ports (what the application requires)
| Interface | Method | Parameters | Return | Adapter |
|---|---|---|---|---|
| [Entity]Repository | findById | id: String | Optional<[Entity]> | [Entity]PersistenceAdapter |
| [Entity]Repository | save | entity: [Entity] | [Entity] | [Entity]PersistenceAdapter |
| [External]Client | [method] | [params] | [return] | [External]HttpAdapter |

---

## Error Handling Specification

| Exception Class | When Thrown | HTTP Status | Business Rule | Log Level |
|---|---|---|---|---|
| [Entity]NotFoundException | Entity not found by ID | 404 | — | WARN |
| CardBlockedException | BR-001 violation | 422 | BR-001 | INFO (expected) |
| LimitExceededException | BR-003 violation | 422 | BR-003 | INFO (expected) |
| ExternalServiceException | Downstream unavailable | 503 | — | ERROR |

**Exception handling chain:**
1. Domain exceptions — thrown by domain layer, caught by use case layer or controller advice
2. Controller advice (`@RestControllerAdvice`) — maps exceptions to RFC 7807 responses
3. Unhandled exceptions — caught by global handler, return 500, log full stack trace

---

## Observability Design

### Structured Log Points
| Location | Event | Level | Fields | Notes |
|---|---|---|---|---|
| UseCase.execute — start | [UseCase]Started | DEBUG | correlationId, [key inputs] | |
| Domain rule evaluated | RuleEvaluated | INFO | ruleId=BR-NNN, result, duration_ms | |
| UseCase.execute — complete | [UseCase]Completed | INFO | decision, duration_ms | |
| Exception thrown | [UseCase]Failed | ERROR | exception, correlationId | No PII |

### Metrics
| Metric | Type | Labels | Purpose |
|---|---|---|---|
| [service].[usecase].requests.total | Counter | status=[success/failure], decision | Volume tracking |
| [service].[usecase].duration | Histogram | | Latency SLO tracking |
| [service].external.[client].calls | Counter | status=[success/timeout/error] | Dependency health |

---

## Open Design Questions
| Q# | Question | Impact | Decision Needed By |
|---|---|---|---|
| D-001 | [Question] | [Impact] | [Date / Gate] |

IMPORTANT: Do not generate code. Produce specifications and interfaces only. Flag any input gap that prevents a confident design decision.
```
