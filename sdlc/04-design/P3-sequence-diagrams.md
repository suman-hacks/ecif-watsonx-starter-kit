# P3: Sequence Diagrams

## When to Use This Prompt
During Stage 04 (Detailed Design), after component design (P1) is complete. Sequence diagrams show how components collaborate across a business flow — critical input for developers and testers.

---

## Prompt

```text
You are a FORGE Solution Architect producing sequence diagrams. Use Mermaid sequence diagram syntax for all diagrams.

INPUTS
Component design: [Paste P1 detailed design output for the relevant services]
Business flows to diagram: [List the flows — e.g., "Happy path card authorization", "Card blocked decline", "Daily limit exceeded"]
API contracts: [Paste relevant sections from Agent 04 API contracts]

TASK
Produce sequence diagrams for each business flow, then a complete interaction inventory.

---

# Sequence Diagrams: [SERVICE/DOMAIN NAME]
Produced by: [AI tool]  Date: [date]
Business flows covered: [list]
Human review required before development begins.

---

## Flow 1: [HAPPY PATH NAME — e.g., Successful Card Authorization]

**Business rule context:** BR-001, BR-002, BR-003 (all pass → APPROVED)
**Trigger:** Customer initiates a point-of-sale transaction

```mermaid
sequenceDiagram
    autonumber
    actor Customer
    participant POS as POS Terminal
    participant API as AuthorizationController
    participant UC as AuthorizeCardUseCase
    participant CardSvc as CardDomainService
    participant CardRepo as CardRepository
    participant LimitSvc as LimitEvaluator
    participant LimitRepo as LimitRepository
    participant AuditPub as AuditEventPublisher
    participant DB as PostgreSQL

    Customer->>POS: Initiate payment (amount, card)
    POS->>API: POST /api/v1/authorizations
    Note over API: Validate request format (not business logic)
    API->>UC: authorize(AuthorizationRequest)
    
    UC->>CardRepo: findById(cardId)
    CardRepo->>DB: SELECT * FROM card WHERE id = ?
    DB-->>CardRepo: Card record
    CardRepo-->>UC: Card domain object
    
    UC->>CardSvc: validateCardEligibility(card)
    Note over CardSvc: BR-001: Check BLOCKED status
    Note over CardSvc: BR-002: Check EXPIRED status
    CardSvc-->>UC: CardEligible (all checks pass)
    
    UC->>LimitRepo: findDailyUsage(cardId, today)
    LimitRepo->>DB: SELECT SUM(amount) FROM authorization WHERE card_id = ? AND date = today
    DB-->>LimitRepo: Current daily total
    LimitRepo-->>UC: DailyUsage
    
    UC->>LimitSvc: evaluateLimit(card, amount, dailyUsage)
    Note over LimitSvc: BR-003: Check daily limit
    LimitSvc-->>UC: WithinLimit
    
    UC->>DB: INSERT INTO authorization (id, card_id, amount, status='APPROVED', ...)
    DB-->>UC: Saved
    
    UC->>AuditPub: publish(AuthorizationApprovedEvent)
    Note over AuditPub: Async — does not block response
    
    UC-->>API: AuthorizationResult(APPROVED, authorizationId)
    API-->>POS: 200 OK {decision: "APPROVED", authorizationId: "..."}
    POS-->>Customer: Payment approved
```

**Notes on this flow:**
- Step 8 (audit event) is async — failure here does NOT fail the authorization
- Database insert (step 10) is within the same transaction as limit evaluation
- Card lookup is cached in Redis with TTL=60s — not shown to keep diagram clean

---

## Flow 2: [DECLINE PATH — e.g., Card Blocked]

**Business rule context:** BR-001 fails → DECLINED immediately (no further evaluation)
**Trigger:** Customer initiates transaction; card is in BLOCKED status

```mermaid
sequenceDiagram
    autonumber
    actor Customer
    participant POS as POS Terminal
    participant API as AuthorizationController
    participant UC as AuthorizeCardUseCase
    participant CardSvc as CardDomainService
    participant CardRepo as CardRepository
    participant AuditPub as AuditEventPublisher
    participant DB as PostgreSQL

    Customer->>POS: Initiate payment
    POS->>API: POST /api/v1/authorizations
    API->>UC: authorize(AuthorizationRequest)
    
    UC->>CardRepo: findById(cardId)
    CardRepo->>DB: SELECT * FROM card WHERE id = ?
    DB-->>CardRepo: Card record (status = 'BLOCKED')
    CardRepo-->>UC: Card(status=BLOCKED)
    
    UC->>CardSvc: validateCardEligibility(card)
    Note over CardSvc: BR-001: status = BLOCKED → throw CardBlockedException
    CardSvc-->>UC: throws CardBlockedException(BR-001)
    
    UC->>DB: INSERT INTO authorization (status='DECLINED', decline_reason='CARD_BLOCKED')
    DB-->>UC: Saved
    
    UC->>AuditPub: publish(AuthorizationDeclinedEvent{reason: "CARD_BLOCKED", rule: "BR-001"})
    
    UC-->>API: AuthorizationResult(DECLINED, "CARD_BLOCKED")
    API-->>POS: 200 OK {decision: "DECLINED", declineReason: "CARD_BLOCKED"}
    POS-->>Customer: Payment declined
```

**Notes:**
- Response is still HTTP 200 — "declined" is a valid business outcome, not an error
- Decline reason code "CARD_BLOCKED" maps to a customer-facing message at POS level (not in this service)

---

## Flow 3: [ERROR PATH — e.g., Downstream Service Unavailable]

**Business rule context:** External fraud service times out
**Trigger:** Authorization request; fraud service does not respond within SLA

```mermaid
sequenceDiagram
    autonumber
    participant API as AuthorizationController
    participant UC as AuthorizeCardUseCase
    participant FraudAdapter as FraudHttpAdapter
    participant FraudSvc as External Fraud Service

    API->>UC: authorize(AuthorizationRequest)
    Note over UC: BR-001, BR-002, BR-003 all pass
    
    UC->>FraudAdapter: assessRisk(FraudRequest)
    FraudAdapter->>FraudSvc: POST /fraud/v2/assess [timeout: 3s]
    
    Note over FraudSvc: Service unavailable / timeout
    FraudSvc-->>FraudAdapter: Timeout after 3000ms
    
    FraudAdapter-->>UC: throws ExternalServiceException("FRAUD_SERVICE_UNAVAILABLE")
    
    Note over UC: Circuit breaker: OPEN state reached
    Note over UC: Fallback: apply conservative decline OR proceed with MANUAL_REVIEW flag
    Note over UC: [Decision: per ADR-007 — decline when fraud service unavailable]
    
    UC->>API: AuthorizationResult(DECLINED, "SERVICE_UNAVAILABLE")
    API-->>POS: 503 {title: "Service temporarily unavailable", detail: "Authorization cannot be processed. Please retry."}
```

**Notes:**
- ADR-007 must document the fail-open vs fail-closed decision explicitly
- Circuit breaker configuration: 5 failures in 60s → OPEN; retry after 30s

---

## Component Interaction Inventory

Complete list of all interactions in the system — useful for integration testing and deployment planning.

| From | To | Mechanism | Protocol | Timeout | Retry? | Circuit Breaker? |
|---|---|---|---|---|---|---|
| AuthorizationController | AuthorizeCardUseCase | In-process | Java | N/A | N/A | N/A |
| AuthorizeCardUseCase | CardRepository | In-process | Java | N/A | N/A | N/A |
| CardRepository | PostgreSQL | JDBC | TCP | 5s query | No | No |
| AuthorizeCardUseCase | FraudHttpAdapter | In-process | Java | N/A | N/A | N/A |
| FraudHttpAdapter | External Fraud API | HTTP | HTTPS | 3s | 1 retry | Yes — Resilience4j |
| AuthorizeCardUseCase | AuditEventPublisher | In-process | Java | N/A | N/A | N/A |
| AuditEventPublisher | Kafka | Async | TCP | 1s publish | Yes — 3 retries | No |

---

## Mermaid Rendering

To render these diagrams:
1. **VS Code:** Install "Markdown Preview Mermaid Support" extension
2. **GitLab / GitHub:** Supported natively in markdown preview
3. **Confluence:** Use "Mermaid Diagrams" app
4. **Live:** https://mermaid.live

IMPORTANT: Do not generate code. These are design specifications only. Every decision about error handling or fallback behavior must reference an ADR or be flagged as an open design question.
```
