# FORGE Code Quality Standards

These standards define the non-negotiable baseline for all code generated with AI assistance under the FORGE framework. All standards apply to production-targeted code. Prototype/spike code must be clearly labeled and must be discarded and regenerated to standard before production use.

---

## Section 1: Code Structure

### 1.1 Function and Method Size

**Rule:** No function or method may exceed 30 lines of meaningful code (excluding blank lines and single-line comments).

**Why:** Functions longer than 30 lines are doing more than one thing. They are harder to test, harder to name, and harder to reason about. If you cannot describe what a function does in one sentence, it is too long.

**Enforcement:** Functions that approach the limit should be refactored by extracting coherent sub-operations into well-named private methods.

**What to do when a function naturally requires more logic:**
```java
// Wrong: One 80-line method doing everything
public PaymentResult processPayment(PaymentRequest request) {
    // 80 lines of mixed validation, calculation, persistence, and notification
}

// Correct: Orchestrating method calls well-named single-purpose methods
public PaymentResult processPayment(PaymentRequest request) {
    validatePaymentRequest(request);
    PaymentContext context = buildPaymentContext(request);
    AuthorizationResult auth = authorizePayment(context);
    PaymentRecord record = persistPayment(context, auth);
    publishPaymentEvent(record);
    return buildResult(record, auth);
}
// Each called method is ≤30 lines and does exactly one thing
```

### 1.2 Class Size

**Rule:** No class may exceed 300 lines (excluding blank lines and comments). No class may have more than one clearly stated responsibility.

**Warning thresholds:**
- 200-300 lines: Review whether the class is approaching responsibility creep
- >300 lines: Must be refactored before merge; no exceptions

**Common violations to detect and reject:**
- "Manager" or "Helper" or "Util" classes that accumulate unrelated methods
- Service classes that handle multiple distinct business capabilities
- Entity classes that also contain business logic (mix of data and behavior without domain model intentionality)
- A class with more than 10 public methods usually has too many responsibilities

### 1.3 No God Objects

**Rule:** No single class may serve as a central coordinator for multiple business capabilities, hold all configuration, or provide access to all services.

**God object patterns to reject:**
```java
// Wrong — God object patterns
public class ApplicationManager {
    public void processPayment() { ... }
    public void createCustomer() { ... }
    public void runBatchJob() { ... }
    public void sendNotification() { ... }
    public void generateReport() { ... }
}

public class ApplicationContext {
    public static PaymentService getPaymentService() { ... }
    public static CustomerService getCustomerService() { ... }
    // Service locator antipattern — violates DI and testability
}
```

### 1.4 Dependency Injection

**Rule:** All dependencies must be injected via constructor injection. Field injection and setter injection are not acceptable in production code. The reasoning: constructor injection makes dependencies explicit, makes the class easier to test, and prevents the class from existing in an invalid state.

```java
// Correct — Constructor injection
@Service
public class PaymentProcessingService {
    private final PaymentRepository paymentRepository;
    private final FraudRiskClient fraudRiskClient;
    private final PaymentEventPublisher eventPublisher;
    
    public PaymentProcessingService(
            PaymentRepository paymentRepository,
            FraudRiskClient fraudRiskClient,
            PaymentEventPublisher eventPublisher) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository, "paymentRepository is required");
        this.fraudRiskClient = Objects.requireNonNull(fraudRiskClient, "fraudRiskClient is required");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher is required");
    }
}

// Wrong — field injection (hidden dependencies, not testable without framework)
@Service
public class PaymentProcessingService {
    @Autowired private PaymentRepository paymentRepository;  // VIOLATION
    @Autowired private FraudRiskClient fraudRiskClient;      // VIOLATION
}
```

### 1.5 Immutability by Default

**Rule:** Value objects, DTOs, request/response objects, and event records must be immutable where possible. Use:
- Java: Records (Java 16+), or final fields with no setters
- Python: `@dataclass(frozen=True)` or named tuples
- TypeScript: `readonly` fields and `as const`

```java
// Correct — immutable record (Java 16+)
public record PaymentRequest(
    UUID transactionId,
    BigDecimal amount,
    String currency,
    String merchantId
) {}

// Correct — immutable value object (pre-Java-16)
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;
    
    public Money(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
    }
    
    // No setters
    public BigDecimal getAmount() { return amount; }
    public Currency getCurrency() { return currency; }
}
```

---

## Section 2: Naming Standards

### 2.1 Domain Language First

**Rule:** All significant identifiers (class names, method names, variable names, package names) must use business domain language — the language used by the business stakeholders. This is Ubiquitous Language as defined in Domain-Driven Design.

**Getting domain language right:**
- Use the exact terms the business uses. If the business calls it "settlement," name it `Settlement`, not `FinancialClose` or `EndOfDayProcessing`.
- Ask the business analyst if uncertain about domain terminology.
- Maintain a domain glossary for the project; reference it when naming.

**Examples:**
```java
// Correct — domain language
public class PaymentAuthorization { }
public BigDecimal calculateSettlementAmount(List<Transaction> transactions) { }
public boolean isOverCreditLimit(Money amount, CreditLimit limit) { }

// Wrong — technical language replacing domain language
public class PaymentDTO { }              // DTO suffix hides domain concept
public BigDecimal computeValue() { }    // "value" is vague; of what?
public boolean checkFlag(int val) { }   // completely opaque domain meaning
```

### 2.2 No Unexplained Abbreviations

**Rule:** No abbreviations except universally understood ones. When in doubt, spell it out.

**Approved abbreviations (universally understood):**
`HTTP`, `HTTPS`, `URL`, `URI`, `ID`, `UUID`, `API`, `SQL`, `JSON`, `XML`, `CSV`, `PDF`, `SSN`, `PIN`, `OTP`, `JWT`, `TLS`, `SSL`, `TCP`, `IP`, `DNS`, `ACL`, `IAM`, `AWS`, `GCP`, `UI`, `SDK`, `EOF`

**Not approved (spell these out):**
- `amt` → `amount`
- `acct` → `account`
- `cust` → `customer`
- `proc` → `process` or `procedure`
- `mgr` → `manager`
- `svc` → `service`
- `desc` → `description`
- `calc` → `calculate` or `calculation`
- `auth` → `authorization` or `authentication` (pick the right one!)
- `tmp` → `temporary` or name what it actually is

### 2.3 Naming Patterns by Type

| Identifier Type | Pattern | Examples |
|----------------|---------|----------|
| Class (noun concept) | PascalCase noun phrase | `PaymentAuthorization`, `CustomerProfile`, `FraudRiskScore` |
| Interface | PascalCase noun or adjective | `PaymentRepository`, `Auditable`, `Serializable` |
| Method (action) | camelCase verb phrase | `processPayment()`, `calculateFraudScore()`, `findByAccountId()` |
| Boolean variable | camelCase with `is/has/can` prefix | `isExpired`, `hasOverdraft`, `canRetry` |
| Boolean method | camelCase with `is/has/can` prefix | `isAuthorized()`, `hasActiveSessions()` |
| Constants | UPPER_SNAKE_CASE | `MAX_RETRY_ATTEMPTS`, `DEFAULT_TIMEOUT_MS` |
| Enum value | UPPER_SNAKE_CASE | `PAYMENT_PENDING`, `FRAUD_REVIEW`, `AUTHORIZED` |
| Package | lowercase, domain-structured | `com.company.payment.authorization` |
| Collection variable | Plural of element type | `transactions`, `paymentMethods`, `accounts` |

### 2.4 Avoid Generic and Meaningless Names

**Forbidden variable names in production code:**
- Single letter variables except loop counters (`i`, `j`, `k`) and stream elements (`e` for exception in catch only)
- `data`, `info`, `value`, `result`, `temp`, `obj`, `item`, `thing`, `stuff`
- `helper`, `manager`, `processor`, `handler` as standalone names (they must qualify what they help/manage/process/handle)
- `flag`, `check`, `status` without domain qualification

---

## Section 3: Error Handling

### 3.1 Explicit Error Types

**Rule:** Different failure modes must have different exception/error types. Never use a single generic exception for all error conditions. Error types communicate meaning to the caller and enable selective catch blocks.

**Error type hierarchy pattern:**
```java
// Base domain exception
public abstract class PaymentException extends RuntimeException {
    private final String errorCode;
    private final String correlationId;
    
    protected PaymentException(String errorCode, String message, String correlationId) {
        super(message);
        this.errorCode = errorCode;
        this.correlationId = correlationId;
    }
}

// Specific types for specific failure modes
public class InsufficientFundsException extends PaymentException {
    private final Money available;
    private final Money requested;
    // Constructor, getters...
}

public class CardExpiredException extends PaymentException {
    private final LocalDate expiryDate;
    // Constructor, getters...
}

public class FraudRiskExceededException extends PaymentException {
    private final RiskScore score;
    private final RiskThreshold threshold;
    // Constructor, getters...
}
```

### 3.2 Never Swallow Exceptions

**Rule:** No `catch` block may silently discard an exception. Every caught exception must either: (a) be logged with full context and then handled, (b) be wrapped in a more specific exception and re-thrown, or (c) be converted to an appropriate result type (e.g., `Optional.empty()` for not-found, `Result.failure()` for expected failures).

```java
// VIOLATION — silent swallow
try {
    paymentRepository.save(payment);
} catch (Exception e) {
    // ignore
}

// VIOLATION — log and ignore (still loses the problem)
try {
    paymentRepository.save(payment);
} catch (Exception e) {
    log.error("Error saving payment");  // No context, and processing continues as if successful
}

// Correct — log with full context and handle appropriately
try {
    paymentRepository.save(payment);
} catch (DataAccessException e) {
    log.error("Failed to persist payment record",
        "correlationId", correlationId,
        "transactionId", payment.getTransactionId(),
        "operation", "processPayment",
        "exception", e.getMessage());
    throw new PaymentPersistenceException(
        "PERSISTENCE_FAILURE",
        "Payment record could not be saved; transaction rolled back",
        correlationId,
        e);
}
```

### 3.3 Meaningful API Error Responses

**Rule:** All API error responses must include: (1) an error code (machine-readable, stable across versions), (2) a human-readable message, (3) the correlation ID, and (4) optionally, field-level validation errors.

**Standard error response schema:**
```json
{
  "errorCode": "INSUFFICIENT_FUNDS",
  "message": "The payment amount exceeds the available balance for this account.",
  "correlationId": "7b2f1c8a-3e4d-4a5b-9f2e-1d3c5e7a9b0d",
  "timestamp": "2025-03-15T14:30:00Z",
  "details": [
    {
      "field": "amount",
      "issue": "Requested amount 150.00 USD exceeds available balance 75.32 USD"
    }
  ]
}
```

**Error code standards:**
- Stable: Error codes do not change between API versions
- Unique: Each error code represents exactly one error condition
- Documented: Every error code is documented in the API spec
- Actionable: The error message tells the caller what to do, not just what went wrong

### 3.4 Structured Exception Logging

Every logged exception must include the full context needed to diagnose the issue in production without reproducing it:

```java
log.error("Payment authorization failed",
    "correlationId", correlationId,
    "transactionId", transactionId,
    "merchantId", merchantId,
    "amount", amount,
    "currency", currency,
    "operation", "authorizePayment",
    "stage", "fraud-check",
    "errorCode", e.getErrorCode(),
    "exception", e.getMessage(),
    "cause", e.getCause() != null ? e.getCause().getMessage() : "none");
```

---

## Section 4: Testing Standards

### 4.1 Coverage Requirements

| Scope | Minimum Line Coverage | Minimum Branch Coverage |
|-------|----------------------|------------------------|
| Business logic classes | 80% | 90% |
| API controllers/handlers | 70% | 80% |
| Repository/persistence classes | 60% | 70% |
| Configuration classes | 50% | 50% |
| Generated boilerplate | N/A | N/A |

Coverage thresholds are minimums, not targets. The quality of test cases matters more than the coverage number. 100% coverage with trivial assertions is worse than 80% coverage with meaningful behavioral tests.

### 4.2 Test Naming Convention

Test names must describe the scenario being tested and the expected outcome, not the method under test.

**Pattern:** `[methodUnderTest]_[scenarioDescription]_[expectedOutcome]`

```java
// Correct — describes behavior
@Test void processPayment_withValidCardAndSufficientFunds_shouldReturnAuthorized()
@Test void processPayment_withExpiredCard_shouldThrowCardExpiredException()
@Test void processPayment_withAmountExceedingCreditLimit_shouldReturnDeclined()
@Test void processPayment_whenFraudServiceUnavailable_shouldUseFallbackAndAllow()

// Wrong — describes method, not behavior
@Test void testProcessPayment()
@Test void processPaymentTest()
@Test void shouldWork()
```

### 4.3 Test Structure (Arrange-Act-Assert)

All tests must follow the Arrange-Act-Assert (AAA) pattern with explicit section markers:

```java
@Test
void processPayment_withValidCardAndSufficientFunds_shouldReturnAuthorized() {
    // Arrange
    PaymentRequest request = PaymentRequestBuilder.aValidPayment()
        .withAmount(new Money(BigDecimal.valueOf(50.00), Currency.USD))
        .withCard(TestCards.VALID_VISA)
        .build();
    when(fraudRiskClient.assess(any())).thenReturn(FraudAssessment.LOW_RISK);
    
    // Act
    PaymentResult result = paymentService.processPayment(request, TEST_CORRELATION_ID);
    
    // Assert
    assertThat(result.getStatus()).isEqualTo(PaymentStatus.AUTHORIZED);
    assertThat(result.getAuthorizationCode()).isNotNull();
    verify(paymentRepository).save(argThat(p -> p.getStatus() == PaymentStatus.AUTHORIZED));
}
```

### 4.4 Test Data Management

**Rule:** Tests must use meaningful, realistic test data — not trivial strings like `"test"` and numbers like `1`.

- Use test data builders (also called test object mothers) for complex objects
- Name test data by its scenario: `TestCards.EXPIRED_VISA`, `TestCustomers.OVER_CREDIT_LIMIT`
- Document why specific test values were chosen when the value is not self-explanatory

**Test data builder pattern:**
```java
public class PaymentRequestBuilder {
    private BigDecimal amount = BigDecimal.valueOf(100.00);
    private String currency = "USD";
    private String cardToken = "tok_visa_4242";
    
    public static PaymentRequestBuilder aValidPayment() {
        return new PaymentRequestBuilder();
    }
    
    public PaymentRequestBuilder withAmount(Money money) {
        this.amount = money.getAmount();
        this.currency = money.getCurrency().getCode();
        return this;
    }
    
    public PaymentRequest build() {
        return new PaymentRequest(UUID.randomUUID(), amount, currency, cardToken);
    }
}
```

### 4.5 What Must Be Tested (Non-Negotiable)

For every business logic class, the following must be tested regardless of whether it was explicitly requested:

- [ ] Happy path: expected behavior with valid inputs
- [ ] Each documented error condition (each exception type, each error code)
- [ ] Null inputs for all required parameters
- [ ] Empty collections where collections are parameters
- [ ] Boundary values: zero, one, maximum, minimum
- [ ] Each logical branch in complex conditionals
- [ ] All configured fallback behaviors
- [ ] Event publication (verify the correct event is published with correct data)

### 4.6 Integration Test Requirements

For every external integration (database, message broker, external API), there must be integration tests that run against a real or test-double instance:

- Database: Testcontainers with the actual database type (PostgreSQL, not H2)
- Message broker: Testcontainers with Kafka/RabbitMQ
- External APIs: WireMock for HTTP dependencies; verify request format and response handling
- Legacy systems: Contract tests to verify integration behavior

---

## Section 5: Documentation Standards

### 5.1 Public API Documentation

**Rule:** Every public method and class in a published API (service interface, shared library, published event schema) must have documentation that describes: (1) what it does in business terms, (2) all parameters with type, constraints, and meaning, (3) return value with meaning, (4) all exceptions that can be thrown and under what conditions, and (5) any side effects.

**Java Javadoc example:**
```java
/**
 * Authorizes a payment transaction against the customer's payment method.
 *
 * Checks fraud risk, validates card details, and authorizes the amount
 * against the issuing bank. Returns the authorization result which the
 * caller must use to determine whether to proceed with settlement.
 *
 * @param request The payment request containing amount, currency, and card token.
 *                Amount must be positive. Currency must be an ISO 4217 code.
 * @param correlationId Unique identifier for tracing this request across services.
 *                      Generated by the caller; must be a UUID.
 * @return PaymentResult with status AUTHORIZED, DECLINED, or PENDING_REVIEW.
 *         Never returns null.
 * @throws CardExpiredException if the card token represents an expired card
 * @throws FraudRiskExceededException if the fraud score exceeds the configured threshold
 * @throws PaymentServiceUnavailableException if the authorization service cannot be reached
 *         and the fallback criteria do not allow the transaction to proceed
 */
public PaymentResult authorizePayment(PaymentRequest request, String correlationId) { ... }
```

### 5.2 Complex Algorithm Comments

**Rule:** Complex algorithms, non-obvious business logic, and any code that required special domain knowledge must be commented with an explanation of WHY, not WHAT. The code itself shows what — comments explain the reasoning that cannot be derived from reading the code.

```java
// Wrong — explains what the code does (redundant with the code)
// Multiply rate by balance and divide by 12 for monthly interest
BigDecimal monthlyInterest = rate.multiply(balance).divide(TWELVE, ROUNDING_MODE);

// Correct — explains why this specific approach is used
// Per FED Regulation Z (12 CFR 226.14), periodic interest is calculated using
// the daily periodic rate method: ADB * DPR * days-in-period.
// For monthly billing cycles, we use 30/365 as the period factor per the
// account agreement terms agreed with Compliance on 2024-03-01.
// ASSUMPTION: billing cycle is always 30 days; confirmed by business on 2024-03-01.
BigDecimal periodicRate = annualRate.divide(DAYS_IN_YEAR, CALCULATION_SCALE, HALF_UP);
BigDecimal monthlyInterest = averageDailyBalance.multiply(periodicRate).multiply(BILLING_CYCLE_DAYS);
```

### 5.3 Architecture Decision Records

**Rule:** Every significant architectural decision must be captured in an Architecture Decision Record (ADR). A decision is significant if it: (1) affects multiple services, (2) is difficult to reverse, (3) has meaningful cost or performance implications, or (4) represents a choice between viable alternatives.

**ADR format:**
```markdown
# ADR-{number}: {Title}

**Status:** Proposed | Accepted | Deprecated | Superseded by ADR-{n}
**Date:** YYYY-MM-DD
**Deciders:** [Names/Roles]

## Context
[What is the issue or situation that prompted this decision?]

## Decision
[What was decided?]

## Rationale
[Why was this decision made? What alternatives were considered and rejected?]

## Consequences
### Positive
- [What benefits does this decision bring?]

### Negative / Trade-offs
- [What are the downsides or risks of this decision?]

### Risks
- [What could go wrong, and how will we know?]

## Related Rules
[Which constitution principles or architecture standards informed this decision?]
```

### 5.4 Service README Requirements

Every service or component must have a `README.md` that includes:

- [ ] Service name and one-sentence purpose
- [ ] Business capability owned
- [ ] Team ownership and on-call contact
- [ ] Local development setup (how to run it locally)
- [ ] Key configuration (environment variables, required secrets — names only, not values)
- [ ] API summary (link to OpenAPI spec)
- [ ] Event summary (published events and consumed events — link to AsyncAPI spec)
- [ ] Dependencies (other services, databases, external systems it requires)
- [ ] Health check endpoint
- [ ] Monitoring dashboard link
- [ ] Runbook link (operational procedures)

---

*FORGE Constitution — Code Quality Standards v1.0*
