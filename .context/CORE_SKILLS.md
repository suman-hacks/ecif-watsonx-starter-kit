# CORE_SKILLS.md — Engineering Standards & Guardrails
# FORGE Context Engine | Drop this file into every project repository

> **Purpose:** Load this file at the start of every AI session. It defines universal engineering standards, security guardrails, and code quality rules that apply to every task regardless of project type.

---

## How to Use This File

| Tool | How to Load |
|---|---|
| **GitHub Copilot (VS Code)** | Reference with `#file:.context/CORE_SKILLS.md` in Copilot Chat |
| **GitHub Copilot (JetBrains)** | Paste contents into AI Assistant context pane |
| **Claude Code** | Auto-loaded via `CLAUDE.md` reference; or type `/load .context/CORE_SKILLS.md` |
| **Cursor** | Add `@.context/CORE_SKILLS.md` in Composer; or include in `.cursorrules` |
| **IBM watsonx** | Paste into the "Custom Instructions" field in VS Code extension settings |
| **AWS Q Developer** | Paste as the opening system message in your Q chat session |
| **JetBrains AI Assistant** | Add to the context window via the AI Assistant panel |

**Prompt to activate in any tool:**
```
Apply the engineering standards in .context/CORE_SKILLS.md to every response in this session.
Flag any request that would violate these standards before complying.
```

---

## Security Guardrails — Non-Negotiable

These rules apply to every line of generated code, configuration, and documentation.

### SG-1: No Hardcoded Secrets
Never generate code with hardcoded: API keys, passwords, connection strings, tokens, private keys, or certificate passphrases. Always use environment variables or a secrets manager (e.g., HashiCorp Vault, AWS Secrets Manager, Kubernetes secrets).

```java
// WRONG — never generate this
String dbPassword = "mypassword123";

// RIGHT — always generate this
String dbPassword = System.getenv("DB_PASSWORD");
// or via Spring: @Value("${spring.datasource.password}")
```

**AI flag format:** `[SECURITY: SG-1] Hardcoded credential detected at [location]. Use environment variable instead.`

### SG-2: Input Validation at System Boundaries
All entry points (REST controllers, message listeners, batch inputs) must validate inputs before processing. Use JSR-303 Bean Validation (`@Valid`, `@NotNull`, `@Size`, custom validators). Never trust data from external callers.

### SG-3: No SQL Injection
Always use parameterized queries, PreparedStatements, or ORM repositories (Spring Data JPA). Never build SQL by string concatenation.

```java
// WRONG
String sql = "SELECT * FROM accounts WHERE id = '" + userId + "'";

// RIGHT
Optional<Account> account = accountRepository.findById(userId);
```

### SG-4: No PII/PAN in Logs
Never log: full credit card numbers (PAN), CVV, SSN, passwords, authentication tokens, or customer PII beyond what is explicitly approved for operational logging. Log only masked/tokenized identifiers.

```java
// WRONG
log.info("Processing card: {}", pan);

// RIGHT
log.info("Processing card ending: {}", pan.substring(pan.length() - 4));
```

### SG-5: HTTPS/TLS by Default
All generated infrastructure code (Dockerfiles, YAML configs, Terraform) must use HTTPS/TLS for external communications. Never generate HTTP endpoints for production-targeted services.

### SG-6: Principle of Least Privilege
Generated IAM roles, Kubernetes RBAC, database users, and service accounts must request only the permissions needed for the specific task. Never use admin roles or wildcard permissions.

---

## Code Quality Standards

### CQ-1: Clean Code Principles
- Methods must do one thing and be named to describe that one thing
- Keep methods under 30 lines; extract logic into well-named private methods
- Avoid deep nesting (max 3 levels); extract complex conditionals into named boolean methods
- Follow SOLID principles: Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion

### CQ-2: No Magic Numbers or Strings
All constants must be named and documented.

```java
// WRONG
if (transactionAmount > 10000) { ... }

// RIGHT
private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("10000.00");
if (transactionAmount.compareTo(HIGH_VALUE_THRESHOLD) > 0) { ... }
```

### CQ-3: Dependency Injection Over Static Methods
Use constructor injection (preferred) or field injection (acceptable for tests). Avoid static method calls for logic with external dependencies — they cannot be mocked or tested in isolation.

### CQ-4: Immutability by Default
Prefer immutable objects: final fields, value objects over mutable entities, unmodifiable collections. Mutable state is a concurrency risk.

### CQ-5: Exception Handling
Never swallow exceptions silently. Every catch block must either: rethrow (possibly wrapped in a domain exception), log with full context, or handle definitively with documented rationale. 

```java
// WRONG — never do this
try {
    processPayment(request);
} catch (Exception e) { }

// RIGHT
try {
    processPayment(request);
} catch (PaymentProcessingException e) {
    log.error("Payment processing failed", "correlationId", correlationId,
               "transactionId", transactionId, "error", e.getMessage(), e);
    throw new ServiceException("Payment processing failed", e);
}
```

---

## Testing Standards

### TS-1: Tests Are Not Optional
Every non-trivial class must have unit tests. Tests are generated alongside code — not as a follow-up activity. Minimum coverage requirements:
- Business logic classes: 80% line coverage, 90% branch coverage
- Every public method: at least one test per documented behavior
- Every error/exception path: at least one test that triggers it

### TS-2: Test Structure (AAA Pattern)
All tests follow Arrange / Act / Assert structure with clear comments.

```java
@Test
void processAuthorization_withInsufficientFunds_shouldReturnDecline() {
    // Arrange
    AuthorizationRequest request = buildRequest(amount(500.00), availableBalance(100.00));
    
    // Act
    AuthorizationResult result = authorizationService.process(request);
    
    // Assert
    assertThat(result.getDecision()).isEqualTo(Decision.DECLINE);
    assertThat(result.getDeclineCode()).isEqualTo("INSUFFICIENT_FUNDS");
}
```

### TS-3: Test Naming Convention
`methodName_scenario_expectedBehavior` — The test name is the documentation.

### TS-4: Use Realistic Test Data
Test data must reflect real domain values, not generic placeholders (`"test"`, `1`, `"foo"`). Use domain-correct values (e.g., a 16-digit card number, a valid ISO currency code, a realistic transaction amount).

### TS-5: Integration Tests Use Real Dependencies
For integration tests, use Testcontainers for real database and messaging dependencies. Do not mock the database in integration tests — mocks pass when the real thing would fail.

---

## Observability Standards

### OB-1: Structured Logging
All application code must use structured logging with consistent fields. Use SLF4J. Never use `System.out.println`.

Required fields in every log message:
- `correlationId` — request trace ID propagated from entry point
- `operation` — name of the business operation being performed
- `serviceId` — name of this service (usually from environment)

For financial operations, also include:
- `transactionId`
- `accountId` (masked if needed)
- `amount` (never log PAN/CVV)

```java
log.info("Authorization decision made",
    "correlationId", correlationId,
    "operation", "processAuthorization",
    "transactionId", transactionId,
    "decision", decision,
    "durationMs", duration);
```

### OB-2: Correlation ID Propagation
Every service must: (1) extract the correlation ID from the inbound request header (`X-Correlation-ID`), (2) store it in MDC (Mapped Diagnostic Context), (3) propagate it in all outbound requests. Never generate code that drops the correlation ID.

### OB-3: Metrics at Entry Points
Every service entry point (REST endpoint, message listener, batch trigger) must measure and emit: request rate, error rate, and duration (P50/P95/P99). Use Micrometer with your target metrics backend (Prometheus, Datadog, Splunk).

---

## Documentation Standards

### DS-1: Javadoc for Public APIs
All public classes, interfaces, and methods must have Javadoc that describes: what it does, what parameters mean (not just their type), what the return value represents, and what exceptions mean.

### DS-2: Comment Decision Points, Not Obvious Code
Add comments where the logic is non-obvious, where a business rule is being applied, or where a performance trade-off was made. Do not comment `i++` or `return result`.

### DS-3: README at Every Service Root
Every deployable service must have a README with: purpose, quick-start, API summary, environment variables, dependency services, and runbook link.

---

## Environment & Deployment Standards

### ED-1: Java 17+ and Spring Boot 3.x
All new Java services target Java 17 LTS minimum. Spring Boot 3.x (aligned with Jakarta EE 9+). Use Maven or Gradle (Gradle preferred for multi-module projects).

### ED-2: Twelve-Factor App
Services must be stateless, config-from-environment, and treat backing services as attached resources. No in-process state that cannot be reconstructed.

### ED-3: Health Checks
Every service must expose: `/actuator/health` (liveness + readiness), `/actuator/info`, `/actuator/metrics`. These endpoints must be accessible by the platform without authentication.

---

## AI-Specific Rules for This Session

When generating code under these standards, the AI must:

1. **Flag before violating** — If a request would require violating any rule above, state the conflict explicitly and ask for guidance before generating non-compliant code.
2. **Label assumptions** — If you cannot verify something from the provided context, label it `[ASSUMPTION]` and state your confidence level.
3. **Generate tests alongside code** — Every generated class gets a corresponding test class in the same response.
4. **Cite when referencing code** — When analyzing existing code, cite file names and line numbers.
5. **Raise open questions** — If a business rule is missing or ambiguous, stop and ask rather than inventing logic.

---

*CORE_SKILLS.md — FORGE Framework v2.0 | Place in `.context/` at the root of every project repository*
