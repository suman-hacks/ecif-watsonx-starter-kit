# GitHub Copilot Custom Instructions — FORGE v2.0 + ATOM
#
# SETUP INSTRUCTIONS:
# 1. Copy this file to `.github/copilot-instructions.md` in your project repository
# 2. Fill in every [USER FILLS] section with your project details
# 3. Commit the file — GitHub Copilot reads it automatically in VS Code and JetBrains
# 4. In Copilot Chat, reference context files with: #file:.context/ATOM_CHASSIS.md
#
# IMPORTANT: This file works best when your project also has:
# - .context/CORE_SKILLS.md     (universal guardrails)
# - .context/ATOM_CHASSIS.md    (ATOM chassis patterns)
# - .context/MODERNIZATION.md   (legacy migration rules, if applicable)

---

## Project Identity

**Project:** [USER FILLS — project name]
**Type:** [USER FILLS — Greenfield | Brownfield | Mainframe Modernization | Cloud Migration | API Modernization]
**Primary language:** Java 17
**Framework:** Spring Boot 3.x (ATOM chassis)
**Team:** [USER FILLS — team name or department]

---

## FORGE Framework Rules — Always Apply

You are assisting an engineering team operating under the FORGE framework with the ATOM microservices chassis. Apply these rules to every code suggestion, explanation, and chat response.

**1. Never invent business logic.**
If a business rule is needed but not present in the provided code or context, do not assume it. Flag the gap:
`// FORGE [BR-GAP]: Business rule not found in context — verify before using.`

**2. Preserve existing behavior in modernization.**
When completing or modifying existing code that originated from legacy systems, do not silently change behavior. If a behavioral change is required, annotate:
`// FORGE [BEHAVIORAL CHANGE: CBD-NNN]: Legacy did X; modern does Y. Requires approval.`

**3. Separate facts from assumptions.**
In chat responses, label: `FACT` (from provided code/context), `ASSUMPTION` (inferred), `RECOMMENDATION` (suggested approach).

**4. Generate tests alongside code.**
For every non-trivial class or function you generate, include or suggest corresponding unit tests covering: happy path, boundary values, all business rules, and error cases.

**5. Protect sensitive data.**
Never suggest code that: logs PAN, CVV, SSN, credentials, or tokens; transmits sensitive data without encryption; stores credentials in code or configuration files.
Flag violations: `// FORGE [SECURITY: SG-N] — [describe the concern]`

**6. Follow ATOM architectural patterns.**
See `## ATOM Patterns` section. Never suggest patterns that contradict ATOM architecture. If ATOM doesn't cover the scenario, note: `// FORGE [ARCH-GAP]: Consider an ADR for this pattern.`

**7. Prefer constructor injection.**
Always use constructor injection (Lombok `@RequiredArgsConstructor`). Never suggest `@Autowired` field injection in production code.

**8. Structured logging only.**
Always log with structured key-value pairs. Never `log.info("Processing " + id)`. Always:
`log.info("Processing payment", "operation", "processPayment", "transactionId", id)`
Never log PAN, CVV, passwords, or tokens.

---

## ATOM Patterns — Required for All Generated Code

When generating or completing Java code, always apply these patterns.

### Required Annotations
```java
// Application service — always @AtomService, not @Service
@AtomService
@Slf4j
@RequiredArgsConstructor
public class PaymentAuthorizationService {
    private final AccountRepository accountRepository;  // constructor injection
    // ...
}

// Repository — always @AtomRepository, not @Repository
@AtomRepository
public class JpaAccountRepository implements AccountRepository { }

// Controller — always returns ResponseEntity<ApiResponse<T>>
@RestController
@RequestMapping("/v1")
public class AuthorizationController {

    @PostMapping("/authorizations")
    public ResponseEntity<ApiResponse<AuthorizationResponse>> authorize(
            @RequestBody @AtomValidated AuthorizationRequest request) {
        // ...
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
```

### Circuit Breaker — Required for All Downstream Calls
```java
@CircuitBreaker(name = "fraud-scoring-service", fallbackMethod = "fraudScoringFallback")
@Retry(name = "fraud-scoring-service")
public FraudScore score(FraudScoringRequest request) {
    return fraudClient.score(request);
}

// Fallback must never throw — return safe default
private FraudScore fraudScoringFallback(FraudScoringRequest request, Exception ex) {
    log.warn("Fraud scoring unavailable — using fallback",
        "operation", "fraudScoringFallback", "reason", ex.getMessage());
    return FraudScore.defaultScore();
}
```

### Package Structure (Required)
```
com.[org].[domain].[service]
├── api/controller/     ← REST controllers
├── api/dto/            ← Immutable DTOs (@Value)
├── api/mapper/         ← MapStruct mappers
├── application/service/ ← @AtomService classes
├── domain/model/        ← Entities, value objects
├── domain/port/         ← Interface definitions only
├── infrastructure/persistence/ ← @AtomRepository
└── infrastructure/client/     ← External HTTP clients
```

**Domain layer rule:** Zero framework imports (no Spring, JPA, Kafka) in `domain/`.

### Logging (Required)
```java
// CORRECT — structured key-value pairs
log.info("Authorization processed",
    "operation", "authorize",
    "transactionId", request.getTransactionId(),
    "decision", result.getDecision(),
    "durationMs", elapsed);

// WRONG — never do this
log.info("Authorized transaction " + txId + " with result " + result);
log.info("Card: " + pan);  // NEVER log PAN
```

---

## Code Generation Preferences

### Style
- Java 17 LTS — use records, text blocks, sealed interfaces, pattern matching where appropriate
- Google Java Style Guide — 120-char max line length
- Lombok: `@Value` for immutable classes, `@Builder` for complex construction, `@Slf4j` for logging, `@RequiredArgsConstructor` for DI
- `final` on all fields, parameters, and local variables that are not reassigned
- Method length: max 30 lines. Extract logic into well-named private methods.

### Naming
- Classes: PascalCase — suffixes: `Service`, `DomainService`, `Repository`, `Controller`, `Client`, `Adapter`, `Mapper`, `Event`, `Command`, `Query`, `Config`
- Methods: camelCase — commands verb-first (`processAuthorization`), queries noun or `get`/`find`-prefixed
- Variables: camelCase — booleans with `is`/`has`/`can` prefix
- Constants: `SCREAMING_SNAKE_CASE` in dedicated `Constants` class
- Tables: snake_case, domain-prefixed: `authz_transactions`
- Kafka topics: kebab-case, env-prefixed: `prod.authz.transaction-authorized`

### Testing (Required with Generated Code)
- Framework: JUnit 5 + Mockito 5 + AssertJ
- Naming: `methodName_scenario_expectedBehavior`
- Structure: Arrange / Act / Assert with explicit AAA comments
- Always cover: happy path, all documented business rules, boundary values, error paths
- Integration: @SpringBootTest + Testcontainers (never mock the database)
- Do NOT use: PowerMock, JUnit 4, Hamcrest

---

## What Copilot Must Never Do in This Project

- Generate code with hardcoded credentials, API keys, passwords, or URLs
- Suggest logging PAN, CVV, authentication tokens, passwords, or sensitive PII
- Generate code that bypasses authentication or authorization checks "for simplicity"
- Introduce new third-party libraries without noting that an ADR is required
- Suggest raw SQL outside of repository interfaces
- Generate `catch (Exception e) {}` — always log or rethrow with context
- Use `@Autowired` field injection — always constructor injection
- Return raw DTOs from controllers — always `ResponseEntity<ApiResponse<T>>`
- Generate code in the domain layer that imports Spring, JPA, or Kafka
- Create hardcoded configuration — always use environment variables via `@Value("${...}")`
- [USER FILLS: add project-specific prohibitions]

---

## How to Use Context Files in Copilot Chat

In VS Code Copilot Chat:
```
# Reference specific context files:
Using #file:.context/ATOM_CHASSIS.md, generate a new ATOM service for fraud detection.

# Reference the whole project:
@workspace What FORGE rules are you following for this project?

# Reference existing code:
Using #file:src/main/java/com/example/service/AuthorizationService.java as a model,
generate a similar service for limit checking. Apply #file:.context/ATOM_CHASSIS.md.
```

In JetBrains (IntelliJ IDEA + GitHub Copilot plugin):
- Open the Copilot Chat panel
- The `.github/copilot-instructions.md` is automatically loaded
- For additional context, paste content from `.context/ATOM_CHASSIS.md` into the chat

---

## Domain Context

[USER FILLS — This section has the most impact on code quality. Be specific.]

### Business Domain
[USER FILLS: e.g., "Payment card authorization service. Receives ISO 8583 authorization requests from Visa/Mastercard networks via IBM MQ, applies spending limits and business rules, and returns approve/decline within 150ms."]

### Key Domain Concepts
[USER FILLS: e.g.,
- **Authorization** — A real-time approve/decline decision on a card transaction
- **PAN** — Primary Account Number (16-digit card number). Always mask in logs (last 4 only).
- **MCC** — Merchant Category Code (4-digit). Used in spending restrictions.
- **Stand-in** — Processing when the issuer host is unavailable. Fallback rules apply.
- **Velocity limit** — Rule based on transaction frequency (e.g., max 5 transactions per hour)
]

### Key Business Rules (Summary)
[USER FILLS: List 5–10 most important business rules so Copilot validates generated code.
e.g.,
- An authorization must be declined if available balance < transaction amount (BR-001)
- Daily spend limits reset at 00:00 UTC (BR-012)
- Contactless transactions above $100 require PIN (BR-045)
- International transactions require country code validation (BR-023)
]

### External Integrations
[USER FILLS: e.g.,
- **Visa/MC Network** — ISO 8583 requests via IBM MQ (inbound)
- **Core Banking** — Synchronous REST for real-time balance (latency target: < 50ms)
- **Fraud Service** — Asynchronous Kafka event, does not block authorization
- **Notification Service** — Asynchronous Kafka event, does not block authorization
]

---

## Key Directories

| Path | Purpose |
|---|---|
| `.context/` | FORGE Context Engine — load in every AI session |
| `src/main/java/.../api/` | Controllers and DTOs — no business logic |
| `src/main/java/.../application/` | ATOM services — business logic orchestration |
| `src/main/java/.../domain/` | Domain model — zero framework dependencies |
| `src/main/java/.../infrastructure/` | DB, messaging, external client adapters |
| `docs/business-rules/` | Extracted BR-NNN register — reference before coding |
| `docs/architecture/` | ADRs — reference before adding new patterns |
| `legacy-artifacts/` | READ ONLY — source COBOL. Never generate code that modifies this. |
| `traceability/` | Assumption register and decision log |

---

## Pre-Generation Checklist

Before accepting any Copilot suggestion for production code:

- [ ] No hardcoded credentials or URLs anywhere
- [ ] No PAN, CVV, SSN, or tokens logged
- [ ] `@AtomService` used (not plain `@Service`)
- [ ] `@AtomRepository` used (not plain `@Repository`)
- [ ] Controller returns `ResponseEntity<ApiResponse<T>>`
- [ ] `@CircuitBreaker` on all downstream HTTP calls
- [ ] Constructor injection only (no `@Autowired`)
- [ ] Domain layer has zero Spring/JPA/Kafka imports
- [ ] Structured logging with correlation ID context
- [ ] Unit tests included with business rule coverage
- [ ] Any behavioral change from legacy is annotated `[BEHAVIORAL CHANGE: CBD-NNN]`
