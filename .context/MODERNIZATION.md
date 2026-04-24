# MODERNIZATION.md — Legacy Modernization Guidelines
# FORGE Context Engine | Drop this file into any legacy-to-modern migration project

> **Purpose:** Load this file whenever analyzing, migrating, or generating modern equivalents of legacy systems (COBOL/PL1/JCL on z/OS, TIBCO BW, legacy Java EE, etc.). It defines the migration patterns, data type mappings, and behavioral preservation rules that AI assistants must follow.

---

## How to Use This File

Load this file alongside `CORE_SKILLS.md` and `ATOM_CHASSIS.md` for any modernization task.

```
I am working on a legacy modernization project. Apply the rules in:
- .context/CORE_SKILLS.md (engineering guardrails)
- .context/ATOM_CHASSIS.md (ATOM microservices patterns)
- .context/MODERNIZATION.md (migration patterns — this file)

My task: [describe what you are migrating]
```

---

## The Prime Directive: Behavior Preservation

> **The goal of modernization is to preserve business behavior, not to improve it.**

Legacy systems contain decades of accumulated business logic — including logic that looks wrong but is intentionally correct. Rules that appear redundant may satisfy regulatory requirements. Code paths that appear dead may be invoked by rarely-occurring but legally-mandated scenarios.

**The AI must never:**
- Silently "clean up" business logic while modernizing
- Remove code that appears unused without explicit confirmation
- Substitute "standard" behavior for non-standard legacy behavior
- Apply Java/Spring idioms that change the computational result (e.g., rounding mode changes)

**Every intentional behavioral change requires:**
1. A `[BEHAVIORAL CHANGE]` annotation in the generated code
2. An entry in the project's `assumption-register.md`
3. Explicit approval from the tech lead or business analyst

---

## Migration Strategy: Strangler Fig Pattern (Default)

The Strangler Fig pattern is the default modernization approach. It avoids big-bang cutover and allows incremental validation.

```
Phase 1: Intercept
  Legacy System ←── All traffic still goes here
       │
  [Proxy/Router Service] ← New ATOM service intercepts calls
       │
  Routes 100% to legacy for now

Phase 2: Parallel Run
  Legacy System ←── Still active (source of truth)
       │
  [Proxy/Router Service]
       ├──→ Legacy System (result used for decision)
       └──→ New ATOM Service (result compared, not used)
  
  Compare results. Fix discrepancies. Build confidence.

Phase 3: Traffic Shift
  [Proxy/Router Service]
       ├──→ Legacy System (N% traffic, shadow/fallback)
       └──→ New ATOM Service ((100-N)% traffic, primary)
  
  Shift traffic incrementally: 5% → 10% → 25% → 50% → 100%

Phase 4: Decommission
  All traffic → New ATOM Service
  Legacy system decommissioned (after validation period)
```

**ATOM Proxy Service pattern:**

```java
@AtomService
@Slf4j
public class AuthorizationProxyService {

    private final LegacyAuthorizationGateway legacyGateway;
    private final ModernAuthorizationService modernService;
    private final TrafficRouter router;

    public AuthorizationResult authorize(AuthorizationRequest request) {
        TrafficDecision traffic = router.decide(request);

        return switch (traffic.mode()) {
            case LEGACY_ONLY -> legacyGateway.authorize(request);
            case PARALLEL_RUN -> runParallelAndCompare(request);
            case MODERN_PRIMARY -> modernService.authorize(request);
        };
    }

    private AuthorizationResult runParallelAndCompare(AuthorizationRequest request) {
        AuthorizationResult legacy = legacyGateway.authorize(request);
        AuthorizationResult modern = modernService.authorize(request);

        if (!legacy.equals(modern)) {
            log.warn("PARALLEL RUN MISMATCH — investigate before shifting traffic",
                "operation", "parallelRunCompare",
                "transactionId", request.getTransactionId(),
                "legacyDecision", legacy.getDecision(),
                "modernDecision", modern.getDecision());
        }

        return legacy; // Legacy is source of truth during parallel run
    }
}
```

---

## COBOL to Java Mapping

### Data Type Mapping

| COBOL Type | Example | Java Type | Notes |
|---|---|---|---|
| `PIC X(n)` | `PIC X(10)` | `String` | Trim trailing spaces. EBCDIC → UTF-8. |
| `PIC 9(n)` | `PIC 9(8)` | `Long` or `Integer` | Check for leading zeros in business meaning |
| `PIC 9(n)V9(m)` | `PIC 9(7)V9(2)` | `BigDecimal` | Use `BigDecimal`, never `double` for money |
| `PIC S9(n)` | `PIC S9(8)` | `Long` | Signed integer |
| `PIC S9(n)V9(m)` | `PIC S9(7)V9(2)` | `BigDecimal` | Signed decimal — use `BigDecimal` |
| `PIC 9(n) COMP` | `PIC 9(8) COMP` | `Integer` or `Long` | Binary (COMP-4). Check field width. |
| `PIC 9(n) COMP-3` | `PIC 9(9) COMP-3` | `BigDecimal` | Packed decimal. Common for amounts. |
| `PIC X(8)` (date) | `YYYYMMDD` | `LocalDate` | Verify format — check for YYMMDD too |
| `PIC X(6)` (time) | `HHMMSS` | `LocalTime` | Verify format |
| `OCCURS n TIMES` | Array | `List<T>` | |
| `REDEFINES` | Overlay | Sealed interface or union type | Document the union carefully |
| `88 level` | Condition name | `enum` | Map each 88-level value to an enum constant |

### Critical: Monetary Amounts
COBOL programs almost always use `COMP-3` (packed decimal) or `PIC 9(n)V9(m)` for monetary amounts. Java must use `BigDecimal` — never `double` or `float` for money. The rounding mode must match the COBOL ROUNDED behavior.

```java
// COBOL: PIC 9(9)V99 COMP-3
// Correct Java mapping:
BigDecimal amount = new BigDecimal("1234567.89");

// If COBOL uses ROUNDED (default = HALF-UP):
BigDecimal result = amount.setScale(2, RoundingMode.HALF_UP);

// IMPORTANT: If COBOL uses non-standard rounding, document it:
// [BEHAVIORAL NOTE] COBOL ROUNDED used here — maps to HALF_UP
// Verify with business if HALF_EVEN (banker's rounding) is acceptable
```

### COBOL Paragraphs → Java Methods

```cobol
* COBOL — PERFORM CALC-INTEREST THRU CALC-INTEREST-END
CALC-INTEREST.
    MULTIPLY BALANCE BY RATE GIVING INTEREST
    ROUNDED.
CALC-INTEREST-END.
    EXIT.
```

```java
// Java equivalent — preserve the behavior, name the method clearly
private BigDecimal calculateInterest(BigDecimal balance, BigDecimal rate) {
    // [BEHAVIORAL PRESERVATION] Mirrors COBOL CALC-INTEREST paragraph
    // Source: ACCTMGMT.cbl, lines 234-241
    return balance.multiply(rate).setScale(2, RoundingMode.HALF_UP);
}
```

### COBOL Working Storage → Java Domain Model

```cobol
01  WS-TRANSACTION.
    05 WS-TRANS-ID        PIC X(12).
    05 WS-AMOUNT          PIC S9(9)V99 COMP-3.
    05 WS-CURRENCY        PIC X(3).
    05 WS-CARD-STATUS     PIC X(1).
       88 CARD-ACTIVE     VALUE 'A'.
       88 CARD-BLOCKED    VALUE 'B'.
       88 CARD-EXPIRED    VALUE 'E'.
       88 CARD-LOST       VALUE 'L'.
    05 WS-MERCHANT-ID     PIC X(15).
```

```java
// Java equivalent — immutable value object
@Value   // Lombok @Value = all-final, all-args constructor, getters
public class Transaction {
    String transactionId;      // PIC X(12)
    BigDecimal amount;         // PIC S9(9)V99 COMP-3
    String currency;           // PIC X(3) — ISO 4217 code
    CardStatus cardStatus;     // 88-level conditions → enum
    String merchantId;         // PIC X(15)
}

// 88-level conditions → enum
public enum CardStatus {
    ACTIVE("A"),     // 88 CARD-ACTIVE
    BLOCKED("B"),    // 88 CARD-BLOCKED
    EXPIRED("E"),    // 88 CARD-EXPIRED
    LOST("L");       // 88 CARD-LOST

    private final String cobolValue;
    CardStatus(String cobolValue) { this.cobolValue = cobolValue; }

    public static CardStatus fromCobol(String value) {
        return Arrays.stream(values())
            .filter(s -> s.cobolValue.equals(value.trim()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown card status: " + value));
    }
}
```

### COBOL Error Response Codes → Java Exceptions

COBOL programs return response codes instead of throwing exceptions. Modern code should map these at the integration boundary (legacy gateway), then use domain exceptions internally.

```java
// At the legacy integration boundary only:
public AuthorizationResult callLegacySystem(AuthorizationRequest request) {
    LegacyResponse response = legacyGateway.call(request);
    
    return switch (response.getResponseCode()) {
        case "00" -> AuthorizationResult.approved(response.getAuthCode());
        case "51" -> AuthorizationResult.declined(DeclineReason.INSUFFICIENT_FUNDS);
        case "62" -> AuthorizationResult.declined(DeclineReason.CARD_RESTRICTED);
        case "96" -> throw new LegacySystemUnavailableException(response.getResponseCode());
        default -> throw new UnknownResponseCodeException(response.getResponseCode());
    };
}
```

---

## TIBCO BusinessWorks to Spring Integration

| TIBCO Concept | Spring/Java Equivalent |
|---|---|
| TIBCO Process | `@Service` method or Spring Integration flow |
| Receive activity | Kafka listener or REST endpoint |
| HTTP Request activity | `RestTemplate` or `WebClient` with `@CircuitBreaker` |
| JDBC Query activity | Spring Data JPA repository |
| Publish to topic | Kafka producer |
| Subscribe to topic | `@KafkaListener` |
| Try/Catch | try/catch + domain exceptions |
| Shared Variable | Spring-managed bean state (avoid stateful shared vars) |
| Group activity (transaction) | `@Transactional` |
| Decision activity | Strategy pattern or rule engine |
| Mapper | MapStruct mapper |
| Schema (XSD) | Java record or DTO with validation annotations |

---

## ISO 8583 Message Handling

For payment authorization systems using ISO 8583:

```java
// Field mapping — always explicit, never generic
public AuthorizationRequest fromIso8583(Map<Integer, String> fields) {
    return AuthorizationRequest.builder()
        .primaryAccountNumber(fields.get(2))      // DE 002
        .processingCode(fields.get(3))             // DE 003
        .transactionAmount(parseAmount(fields.get(4)))  // DE 004 — 12-digit implied decimal
        .transmissionDateTime(parseDateTime(fields.get(7)))  // DE 007
        .merchantCategoryCode(fields.get(26))      // DE 026
        .posEntryMode(fields.get(22))              // DE 022
        .cardholderData(fields.get(35))            // DE 035 — handle PAN masking
        .build();
}

// Amount fields in ISO 8583 have implied decimal — DE 004 is cents
private BigDecimal parseAmount(String field004) {
    // DE 004: 12-digit numeric, last 2 digits are cents
    // "000000012500" = $125.00
    return new BigDecimal(field004).movePointLeft(2);
}
```

---

## Mainframe Integration: z/OS Components

When integrating with mainframe components that are NOT being modernized:

### CICS Transaction Gateway
```java
@AtomService
@Slf4j
public class CicsAuthorizationGateway implements AuthorizationGateway {

    private final CicsConnection cicsConnection;

    @CircuitBreaker(name = "cics-gateway")
    public AuthorizationResult callCics(String transactionId, AuthorizationRequest request) {
        log.info("Calling CICS transaction",
            "operation", "callCics",
            "transactionId", transactionId,
            "cicsTransaction", "AUTHZ001");

        byte[] commArea = buildCommArea(request);
        byte[] response = cicsConnection.execute("AUTHZ001", commArea);
        return parseCommArea(response);
    }

    private byte[] buildCommArea(AuthorizationRequest request) {
        // Build COMMAREA according to the COBOL DFHCOMMAREA copybook
        // [CITE: DFHCOMMAREA copybook location]
        // ...
    }
}
```

### IBM MQ Integration
```java
@Component
@Slf4j
public class IsoMessageListener {

    @JmsListener(destination = "${atom.clients.mq.authorization-queue}")
    public void onAuthorizationRequest(Message message) {
        // Extract correlation ID from MQ message descriptor (MQMD)
        String correlationId = extractCorrelationId(message);
        MDC.put("correlationId", correlationId);

        log.info("ISO 8583 message received", "operation", "onAuthorizationRequest");
        // Process...
    }
}
```

---

## Behavioral Change Documentation

Every time modern code intentionally deviates from legacy behavior, document it immediately:

```java
// [BEHAVIORAL CHANGE: CBD-003]
// Legacy behavior: COBOL rounds HALF-UP at intermediate calculation steps
// Modern behavior: Rounding applied only at final result (Java BigDecimal default)
// Business approval: Required before promoting to production
// Impact: Individual transaction amounts may differ by < $0.01
// Tracking: See assumption-register.md entry CBD-003
BigDecimal fee = amount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
```

---

## Pre-Modernization Analysis Checklist

Before generating any modern code for a legacy component, confirm these are complete:

- [ ] T1 (System Architecture Analysis) — completed and reviewed
- [ ] T2 (Decision Logic Inventory) — all rules extracted and validated
- [ ] T3 (Integration Map) — all inbound/outbound interfaces documented
- [ ] T4 (Risk Map) — complexity hotspots identified
- [ ] Business rules register reviewed by BA or domain expert
- [ ] Data type mapping spec reviewed (especially monetary fields)
- [ ] Behavioral change register started
- [ ] Target architecture design approved by tech lead

**Do not start code generation until all boxes are checked.**

---

*MODERNIZATION.md — FORGE Framework v2.0 | Place in `.context/` at the root of every modernization project*
