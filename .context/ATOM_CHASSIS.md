# ATOM_CHASSIS.md — ATOM Microservices Framework Reference
# FORGE Context Engine | Drop this file into every ATOM-based project repository

> **Purpose:** Load this file whenever generating, reviewing, or analyzing code built on the ATOM microservices chassis. It defines ATOM-specific patterns, annotations, and conventions that all AI code assistants must follow when working in this ecosystem.

---

## What is ATOM?

ATOM is the internal **Microservices Chassis** for building production-ready Spring Boot APIs. It is a pre-configured, opinionated foundation that provides enterprise cross-cutting concerns out of the box, so engineering teams focus on business logic rather than boilerplate infrastructure.

ATOM provides:
- **`@AtomService`** — Drop-in replacement for `@Service`. Auto-configures security context propagation, structured MDC logging, distributed trace propagation, and request/response audit logging.
- **`@AtomRepository`** — Drop-in replacement for `@Repository`. Auto-configures query logging, connection pool monitoring.
- **`AtomObservability`** — OpenTelemetry-compatible distributed tracing. Inject and use to create custom spans.
- **`AtomSecurityContext`** — Thread-local security context with caller identity, roles, and entitlements.
- **`ApiResponse<T>`** — Standard response envelope for all API endpoints.
- **Resilience4j integration** — Circuit breaker and retry pre-configured via `application.yml`.
- **Structured logging** — Slf4j/Logback pre-wired for Splunk JSON ingestion.
- **Service discovery** — Automatic registration with the internal service registry on startup.

**Rule:** Do not build Spring Boot services without ATOM unless the Platform team has explicitly granted an exception.

---

## Architecture Layers

Every ATOM service follows strict layered architecture:

```
┌─────────────────────────────────────────────────────────────┐
│  API Layer (Controller)                                      │
│  • REST mapping and versioning (e.g., /v1/payments)          │
│  • Input validation (@AtomValidated)                         │
│  • Returns ResponseEntity<ApiResponse<T>>                    │
│  • NO business logic                                         │
├─────────────────────────────────────────────────────────────┤
│  Application Layer (Service)                                 │
│  • Business logic orchestration (@AtomService)               │
│  • Transaction boundary management                           │
│  • NO persistence logic                                      │
│  • NO HTTP concerns                                          │
├─────────────────────────────────────────────────────────────┤
│  Domain Layer                                                │
│  • Core business entities and value objects                  │
│  • Domain rules and validations                              │
│  • NO framework dependencies                                 │
├─────────────────────────────────────────────────────────────┤
│  Infrastructure Layer                                        │
│  • Repository implementations (@AtomRepository)              │
│  • External service clients (wrapped with @CircuitBreaker)   │
│  • Kafka listeners / producers                               │
│  • NO business logic                                         │
└─────────────────────────────────────────────────────────────┘
```

**Dependency direction:** Infrastructure → Application → Domain. The domain layer must have ZERO dependencies on Spring, JPA, Kafka, or any framework.

---

## Standard Package Structure

```
com.{org}.{domain}.{service-name}
├── api/
│   ├── controller/          ← REST controllers (@RestController)
│   ├── dto/                 ← Request/response DTOs (immutable, @Value)
│   └── mapper/              ← DTO ↔ Domain mappers (MapStruct)
├── application/
│   └── service/             ← Application services (@AtomService)
├── domain/
│   ├── model/               ← Entities, value objects, aggregates
│   ├── service/             ← Domain services (pure business logic)
│   ├── port/
│   │   ├── inbound/         ← Use case interfaces
│   │   └── outbound/        ← Repository and external service interfaces
│   └── exception/           ← Domain exceptions
├── infrastructure/
│   ├── persistence/         ← JPA entities, Spring Data repositories
│   ├── client/              ← External HTTP clients
│   ├── messaging/           ← Kafka producers/consumers
│   └── config/              ← Spring @Configuration classes
└── AtomServiceApplication.java
```

---

## Key ATOM Annotations and Their Usage

### `@AtomService`
Apply to all application-layer service classes. **Never use plain `@Service` for business logic.**

```java
@AtomService                      // Not @Service
@Slf4j
public class PaymentAuthorizationService {

    private final AccountRepository accountRepository;
    private final FraudScoringClient fraudClient;
    private final AtomObservability observability;

    // Constructor injection (always preferred over field injection)
    public PaymentAuthorizationService(
            AccountRepository accountRepository,
            FraudScoringClient fraudClient,
            AtomObservability observability) {
        this.accountRepository = accountRepository;
        this.fraudClient = fraudClient;
        this.observability = observability;
    }

    public AuthorizationResult authorize(AuthorizationRequest request) {
        // @AtomService auto-injects correlationId, serviceId into MDC
        // Log with business context — correlationId already in MDC
        log.info("Authorization request received",
            "operation", "authorize",
            "transactionId", request.getTransactionId(),
            "amount", request.getAmount(),
            "currency", request.getCurrency());

        // Business logic here...
    }
}
```

### `@AtomRepository`
Apply to all repository implementation classes.

```java
@AtomRepository               // Not @Repository
public class JpaAccountRepository implements AccountRepository {

    private final AccountJpaRepository jpaRepository;
    private final AccountMapper mapper;

    @Override
    public Optional<Account> findById(AccountId accountId) {
        return jpaRepository.findById(accountId.value())
                            .map(mapper::toDomain);
    }
}
```

### `@AtomValidated`
Apply to request DTO parameters in controller methods. More powerful than plain `@Valid` — integrates with ATOM's error response format.

```java
@PostMapping("/v1/authorizations")
public ResponseEntity<ApiResponse<AuthorizationResponse>> authorize(
        @RequestBody @AtomValidated AuthorizationRequest request) {
    // ...
}
```

### `ApiResponse<T>` — Standard Response Envelope
Every controller method must return `ResponseEntity<ApiResponse<T>>`. Never return raw DTOs.

```java
@PostMapping("/v1/authorizations")
public ResponseEntity<ApiResponse<AuthorizationResponse>> authorize(
        @RequestBody @AtomValidated AuthorizationRequest request) {

    AuthorizationResult result = authService.authorize(toCommand(request));

    return ResponseEntity.ok(
        ApiResponse.success(toResponse(result))
    );
}
```

For errors, ATOM handles the mapping automatically via `@ControllerAdvice`. Do not build custom error response builders.

---

## Resilience Patterns

### Circuit Breaker — Required for All Downstream HTTP Calls

```java
@AtomService
@Slf4j
public class FraudScoringClient {

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "fraud-scoring-service", fallbackMethod = "fraudScoringFallback")
    @Retry(name = "fraud-scoring-service")
    public FraudScore score(FraudScoringRequest request) {
        return restTemplate.postForObject("/v1/score", request, FraudScore.class);
    }

    // Fallback must return a safe default — never throw from fallback
    private FraudScore fraudScoringFallback(FraudScoringRequest request, Exception ex) {
        log.warn("Fraud scoring unavailable — using fallback score",
            "operation", "fraudScoringFallback",
            "reason", ex.getMessage());
        return FraudScore.defaultScore(); // Conservative default
    }
}
```

Circuit breaker config in `application.yml`:
```yaml
resilience4j:
  circuitbreaker:
    instances:
      fraud-scoring-service:
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 10s
        permittedNumberOfCallsInHalfOpenState: 3
```

### Retry — For Transient Failures

```yaml
resilience4j:
  retry:
    instances:
      fraud-scoring-service:
        maxAttempts: 3
        waitDuration: 200ms
        retryExceptions:
          - java.io.IOException
          - org.springframework.web.client.ResourceAccessException
```

---

## Logging Standards (ATOM)

ATOM pre-configures Logback for structured JSON output to Splunk. Follow these rules:

### Correct Logging Pattern

```java
// At service entry — log the intent
log.info("Processing payment authorization",
    "operation", "processAuthorization",
    "transactionId", request.getTransactionId(),
    "merchantId", request.getMerchantId(),
    "amount", request.getAmount());

// At decision points — log the decision with evidence
log.info("Authorization decision made",
    "operation", "processAuthorization",
    "transactionId", transactionId,
    "decision", decision,
    "declineCode", declineCode,   // null if approved
    "ruleApplied", ruleApplied,
    "durationMs", stopwatch.elapsed(MILLISECONDS));

// On error — log with full context
log.error("Authorization processing failed",
    "operation", "processAuthorization",
    "transactionId", transactionId,
    "errorType", e.getClass().getSimpleName(),
    "errorMessage", e.getMessage(), e);
```

### Never Log
- PAN (full card number) — log only last 4 digits
- CVV / CVC
- Passwords, tokens, API keys
- Full account numbers — log masked or tokenized form
- Customer SSN, date of birth, or other regulated PII

---

## Service Discovery

ATOM services auto-register with the internal service registry on startup. **Never hardcode service URLs or hostnames.**

```yaml
# application.yml
atom:
  service:
    name: payment-authorization-service  # Required — used for discovery and logging
    version: 1.0.0
    environment: ${ENVIRONMENT:local}

# Service URL injection — use ATOM discovery, not hardcoded URLs
atom:
  clients:
    fraud-scoring-service:
      url: ${FRAUD_SCORING_SERVICE_URL}   # Injected by platform at deploy time
    core-banking-service:
      url: ${CORE_BANKING_SERVICE_URL}
```

---

## ATOM Configuration Template (`application.yml`)

```yaml
spring:
  application:
    name: ${ATOM_SERVICE_NAME:my-service}
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
      connection-timeout: 3000
      idle-timeout: 300000
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS}
    consumer:
      group-id: ${spring.application.name}
      auto-offset-reset: earliest
    producer:
      acks: all
      retries: 3

atom:
  service:
    name: ${ATOM_SERVICE_NAME}
    version: ${SERVICE_VERSION:1.0.0}
    environment: ${ENVIRONMENT:local}
  observability:
    enabled: true
    sampling-rate: 1.0   # 100% in non-prod; reduce for prod
  security:
    enabled: true
    jwt-issuer: ${JWT_ISSUER}

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true

resilience4j:
  circuitbreaker:
    instances:
      # Define per downstream service
  retry:
    instances:
      # Define per downstream service

logging:
  level:
    root: INFO
    com.{org}: DEBUG
  pattern:
    console: "%d{ISO8601} %5p [%X{correlationId}] [%X{serviceId}] %c{1}: %m%n"
```

---

## ATOM + FORGE: How They Work Together

| Concern | Handled by |
|---|---|
| What to build (requirements, stories, ADRs) | FORGE SDLC prompts |
| How to structure the code | ATOM architecture layers |
| What annotations and patterns to use | ATOM_CHASSIS.md (this file) |
| Engineering guardrails and security | CORE_SKILLS.md |
| Migration from legacy (COBOL/TIBCO) | MODERNIZATION.md |
| AI behavior and governance | FORGE constitution |

**For AI code generation:** When asked to generate an ATOM service, always:
1. Apply `ATOM_CHASSIS.md` patterns (this file)
2. Apply `CORE_SKILLS.md` guardrails
3. Apply `MODERNIZATION.md` if migrating from legacy
4. Follow the FORGE SDLC stage the team is currently in

---

## Code Generation Checklist for AI Assistants

Before finalizing any generated ATOM code, verify:

- [ ] Service class uses `@AtomService`, not plain `@Service`
- [ ] Repository class uses `@AtomRepository`, not plain `@Repository`
- [ ] Controller returns `ResponseEntity<ApiResponse<T>>`
- [ ] Controller uses `@AtomValidated` on request body
- [ ] All downstream HTTP calls have `@CircuitBreaker` and fallback
- [ ] Logging uses structured key-value pairs (not string concatenation)
- [ ] No hardcoded URLs, hostnames, or ports
- [ ] No PAN, CVV, or credentials in any log statement
- [ ] Constructor injection used throughout (not `@Autowired` field injection)
- [ ] Domain layer has zero framework imports (no Spring, JPA, Kafka)
- [ ] Unit tests generated alongside every service and repository class
- [ ] Integration tests use Testcontainers (not mocked databases)

---

*ATOM_CHASSIS.md — FORGE Framework v2.0 | Place in `.context/` at the root of every ATOM project repository*
