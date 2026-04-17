---
context: "CICS Online Transaction Modernization"
parent: "project-contexts/mainframe-modernization/README.md"
use-when: "Modernizing IBM CICS online transactions (EXEC CICS LINK, COMMAREA, BMS maps)"
---

# Project Context: CICS Online Transaction Modernization

## What Makes CICS Different from Batch

CICS (Customer Information Control System) runs **online transactions** — synchronous request/response operations triggered by terminal input, API calls, or inter-program links. Modernizing CICS is different from batch because:

1. **Real-time SLA** — CICS transactions respond in milliseconds; the replacement must too
2. **COMMAREA contracts** — other programs LINK to CICS programs and depend on exact COMMAREA layouts; these are implicit APIs that must be honored or explicitly migrated
3. **Conversation state** — some CICS programs use pseudo-conversational state (multiple screens, one transaction); state management must be redesigned
4. **BMS maps** — CICS terminal screens (BMS) have no equivalent in modern web/API; these require UX redesign, not just code translation
5. **Transaction names** — CICS transactions are identified by 4-character names (e.g., AUTH, CARD); routing table must be preserved or migrated

---

## CICS-to-Modern Mapping

| CICS Concept | Modern Equivalent | Notes |
|---|---|---|
| EXEC CICS LINK | REST API call / gRPC call / in-process method | Synchronous in both cases |
| COMMAREA | Request/Response DTO | Map field-by-field via ACL |
| CONTAINER/CHANNEL | Multi-part request body or header | Used in more modern CICS versions |
| CICS PROGRAM | Microservice or use case class | One CICS program → one service (usually) |
| CICS TRANSACTION (4-char name) | API endpoint path / method name | AUTH → POST /api/v1/authorizations |
| VSAM file | Database table (JPA/JDBC) | File structure → relational schema |
| CICS DB2 | PostgreSQL / DB2 LUW / Aurora | May retain DB2 LUW if on-prem |
| CICS TEMPORARY STORAGE | Redis / in-memory cache | Short-lived; TTL required |
| PSEUDO-CONVERSATIONAL state | Session state / JWT claims | Redesign; do not replicate CICS pseudo-conv |
| BMS MAP (terminal screen) | React/Angular/REST API | Full UX redesign required |
| CICS ABEND handler | Exception handler + error response | Map ABEND codes to business errors |
| EXEC CICS SYNCPOINT | Database transaction | @Transactional in Spring |
| EXEC CICS RETURN | Return statement / HTTP response | End of use case execution |

---

## COMMAREA Analysis Pattern

COMMAREA is the input/output contract for a CICS program. Analyze it carefully — it defines the legacy API.

```cobol
* Typical COMMAREA layout (in LINKAGE SECTION or DFHCOMMAREA):
01 DFHCOMMAREA.
   05 CA-REQUEST-TYPE      PIC X(4).         * 'AUTH' / 'RVSL' / 'STAT'
   05 CA-CARD-NUMBER       PIC X(19).        * PAN — PCI scope
   05 CA-TRANSACTION-AMT   PIC S9(9)V99 COMP-3.  * Packed decimal amount
   05 CA-MERCHANT-ID       PIC X(15).
   05 CA-RESPONSE-CODE     PIC XX.           * ISO 8583 response code
   05 CA-DECLINE-REASON    PIC X(20).
   05 CA-AUTH-ID           PIC X(12).
   05 CA-TIMESTAMP         PIC X(26).        * Mainframe date/time format
```

**Modern DTO equivalent:**
```java
public record AuthorizationRequest(
    @NotNull String cardId,                   // CA-CARD-NUMBER (masked)
    @NotNull @Positive BigDecimal amount,     // CA-TRANSACTION-AMT ÷ 100 (from cents)
    @NotNull String merchantId,               // CA-MERCHANT-ID (trimmed)
    @NotNull String currency                  // NEW — add for internationalization
) {}

public record AuthorizationResponse(
    String authorizationId,                   // CA-AUTH-ID
    Decision decision,                        // Derived from CA-RESPONSE-CODE: 00→APPROVED, else DECLINED
    String declineReason,                     // CA-DECLINE-REASON (mapped to business language)
    Instant timestamp                         // CA-TIMESTAMP (converted from mainframe format)
) {}
```

**Key analysis questions for COMMAREA:**
1. What are all valid values for `CA-REQUEST-TYPE`? (This may represent multiple operations in one program)
2. What are all valid response codes in `CA-RESPONSE-CODE`? (Map every code to a business outcome)
3. Are any fields conditionally present? (Some COMMAREA layouts have overlapping conditions — REDEFINES)
4. What calls this program? (Multiple callers = multiple consumers of the ACL)

---

## CICS REDEFINES Pattern (common in legacy)

```cobol
* REDEFINES — different layouts for different request types
01 COMMAREA-INPUT.
   05 CA-REQUEST-TYPE    PIC X(4).
   05 CA-PAYLOAD         PIC X(100).
   
   05 CA-AUTH-REQUEST REDEFINES CA-PAYLOAD.
      10 CA-CARD-NUMBER  PIC X(19).
      10 CA-AMOUNT       PIC S9(7)V99 COMP-3.
      
   05 CA-STATUS-REQUEST REDEFINES CA-PAYLOAD.
      10 CA-ACCOUNT-ID   PIC X(20).
      10 CA-STATUS-DATE  PIC X(8).
```

**Modern equivalent:** Discriminated union / sealed interface
```java
sealed interface AuthorizationCommand permits 
    AuthorizeCardCommand, 
    CheckCardStatusCommand {}
    
// Discriminated by CA-REQUEST-TYPE:
// 'AUTH' → AuthorizeCardCommand
// 'STAT' → CheckCardStatusCommand
```

---

## CICS ABENDs and Error Handling

CICS ABENDs are abnormal terminations. Map them to modern exceptions:

| CICS ABEND | Meaning | Modern Equivalent |
|---|---|---|
| ASRA | Program check (divide by zero, address exception) | ArithmeticException / NullPointerException |
| AICA | Infinite loop / runaway task | Thread timeout / max iterations guard |
| AKCP | Storage violation | Memory error (rare in modern JVM) |
| Custom (e.g., AUTH) | Business error defined by application | Business exception class |

```cobol
* Legacy ABEND handler
EXEC CICS HANDLE ABEND LABEL(ABEND-HANDLER) END-EXEC.
...
ABEND-HANDLER.
    MOVE 'ABEND' TO CA-RESPONSE-CODE
    EXEC CICS RETURN END-EXEC.
```

```java
// Modern equivalent
@ControllerAdvice
public class AuthorizationExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(AuthorizationSystemException.class)
    public ResponseEntity<ProblemDetail> handleSystemError(AuthorizationSystemException ex) {
        // Log with full context — CICS ABEND had no equivalent logging
        log.error("Authorization system error", kv("correlationId", correlationId), ex);
        return ResponseEntity.status(503).body(
            ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE,
                "Authorization processing temporarily unavailable"));
    }
}
```

---

## Pseudo-Conversational State Migration

CICS pseudo-conversational programming suspends and resumes tasks between user interactions. Modern systems handle this differently:

| CICS Approach | Modern Equivalent |
|---|---|
| COMMAREA passed between screens | Session state in JWT / Redis |
| EXEC CICS RETURN TRANSID → next screen | Frontend navigation / API state |
| CICS TEMPORARY STORAGE for screen data | Redis with TTL / session store |
| BMS MAP fields | Form fields in UI + API request body |

**Migration rule:** Do NOT replicate pseudo-conversational logic in the modern API. Redesign the interaction as a stateless API. The UI (web/mobile) holds user session state. Each API call is complete and independent.

---

## AI Behavior for CICS Projects

Load this alongside `project-contexts/mainframe-modernization/cobol-to-java.md`.

AI must:
1. Map every COMMAREA field to a typed DTO field (with explicit transformation notes)
2. Map every CICS TRANSACTION name to a modern API endpoint
3. Map every ABEND to a modern exception class
4. Flag every REDEFINES as requiring discriminated union design
5. Never replicate pseudo-conversational state — always redesign as stateless
6. Note all callers of the CICS program — they all have COMMAREA dependencies that must be migrated

AI must not:
- Assume the BMS map layout defines the API — the BMS is a terminal UI, not a business interface
- Assume all callers use the same COMMAREA layout — some programs REDEFINE fields differently per caller
- Proceed past COMMAREA analysis if any field's meaning is unclear — flag for SME
