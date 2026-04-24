---
context: "Payments and Banking Domain"
load-when: "Any work involving card payments, authorizations, settlements, banking accounts, or financial transactions"
tools: "Claude Code, GitHub Copilot, JetBrains AI, watsonx"
---

# Context: Payments and Banking Domain

## When to Load This File

Load for any project involving:
- Card authorization (credit, debit, prepaid)
- Payment processing (ACH, wire, SEPA, SWIFT)
- Account management (current, savings, loan)
- Settlement and reconciliation
- Fraud detection integration
- Limits management (daily, transaction, velocity)
- PCI-DSS scope

Add to your AI tool's context using:
- **Claude Code / CLAUDE.md:** `#file:.context/PAYMENTS_DOMAIN.md`
- **GitHub Copilot:** `#file:.context/PAYMENTS_DOMAIN.md` in Copilot Chat
- **JetBrains AI:** Paste this file into the session system prompt

---

## Domain Glossary

```text
PAYMENTS DOMAIN GLOSSARY

AUTHORIZATION: A request from a merchant/POS to approve a transaction before funds move.
  Not the same as settlement. Authorization reserves funds; settlement moves them.

PAN (Primary Account Number): The card number (up to 19 digits). PCI-restricted.
  In prompts and code: always masked to last 4: XXXX-XXXX-XXXX-1234

CVV/CVV2: Card Verification Value. 3-digit security code on card back.
  MUST NEVER be stored — PCI-DSS Req. 3.3 prohibition.

COMMAREA: IBM CICS inter-program communication area (legacy mainframe).
  Maps to DTO in modern systems.

VSAM: Virtual Storage Access Method — IBM mainframe file system.
  Maps to database table or flat file storage in modern systems.

ACQUIRER: The bank that processes card transactions on behalf of a merchant.

ISSUER: The bank that issued the card to the cardholder.
  Authorization decisions are made by the issuer (or their processor).

ICS (Integrated Card System): Common name for legacy mainframe card authorization systems.
  Often COBOL/CICS with DB2 storage.

STAND-IN: Fallback authorization process when the primary authorization system is unavailable.
  Usually approves low-risk transactions up to a limit without real-time issuer approval.

ISO 8583: The financial transaction message format standard used in card networks.
  Field DE 39 = Response Code (00=Approved, 51=Insufficient Funds, 57=Transaction Not Permitted)
  Field DE 48 = Additional Data (varies by implementation)

SCHEME: Card network. Visa, Mastercard, Amex, Discover. Each has its own rules.

CLEARING: The process of transmitting financial transaction data to the acquiring/issuing bank.
  Follows authorization. Settlement follows clearing.

SETTLEMENT: The actual movement of funds between acquiring bank and issuing bank.
  T+1 or T+2 typically. Failure to settle triggers chargeback risk.

CHARGEBACK: A disputed transaction that is reversed. Initiated by cardholder.
  Chargeback cycle: Dispute → Chargeback → Representment → Pre-arbitration → Arbitration

AUTHORIZATION RESPONSE CODES (ISO 8583 DE 39):
  00 — Approved
  01 — Refer to card issuer
  05 — Do not honor (generic decline)
  12 — Invalid transaction
  14 — Invalid card number
  51 — Insufficient funds
  54 — Expired card
  57 — Transaction not permitted to cardholder
  58 — Transaction not permitted to terminal
  61 — Exceeds withdrawal amount limit
  62 — Restricted card
  65 — Exceeds withdrawal frequency limit
  91 — Card issuer or switch inoperative (system unavailable)
  96 — System malfunction

CARD LIFECYCLE STATUSES (common implementations — verify with SME):
  ACTIVE:    Normal transacting status
  BLOCKED:   Permanently blocked — cannot transact (fraud, loss, regulatory)
  SUSPENDED: Temporarily restricted (usually reversible — delinquency, customer request)
  EXPIRED:   Past expiry date
  STOLEN:    Reported stolen — always decline, may trigger fraud alert
  LOST:      Reported lost — always decline
  CLOSED:    Account closed — no transactions

PCI-DSS SCOPE — fields that are in scope:
  In scope:    PAN, expiry date, cardholder name, CVV/CVV2/CVC (in transit only), PIN block
  Restricted:  CVV/CVV2 must not be stored after authorization
  Encrypt at rest: PAN, expiry, cardholder name
  Mask in logs/UI: PAN (last 4 only)
```

---

## AI Behavioral Rules for Payments Domain

```text
DATA HANDLING — AI MUST FOLLOW THESE RULES

1. NEVER generate or process real card numbers — use test PANs only:
   - Visa (approved):    4111 1111 1111 1111
   - Mastercard:         5500 0000 0000 0004
   - Amex:               3714 496353 98431
   - Visa (declined):    4000 0000 0000 0002

2. NEVER store CVV — in tests use "123" and explicitly comment that it is not stored

3. All monetary amounts in minor currency units in storage:
   $10.50 stored as integer 1050 (cents). Use BigDecimal for all arithmetic.
   NEVER use double or float for financial amounts.

4. Currency: always ISO 4217 three-letter code (USD, EUR, GBP, ZAR)

5. PAN masking: implement as a function returning "XXXX-XXXX-XXXX-" + last4
   Never log the full PAN — only the masked representation

6. Timestamps: always UTC; never local time; always TIMESTAMPTZ in PostgreSQL

AUTHORIZATION LOGIC ORDERING — DO NOT CHANGE WITHOUT SME APPROVAL:
  1. Card existence check         (card not found → decline code 14)
  2. Card status check            (blocked/stolen → 62; expired → 54)
  3. Card velocity check          (too many transactions in time window → 65)
  4. Transaction amount limit     (exceeds daily limit → 61)
  5. Fraud score evaluation       (risk above threshold → 05)
  6. Approval                     (response code 00)

Earlier checks short-circuit — do NOT evaluate later checks if an earlier check fails.
This ordering is a business rule. Changing it requires documented approval.

BUSINESS INVARIANTS — NEVER VIOLATE:
  - An authorization decision is immutable once made (do not allow updates to auth records)
  - Declined authorizations are recorded, not discarded (audit trail requirement)
  - Amount must be positive (> 0)
  - Currency must match card currency or FX rules must be applied
  - Authorization has a timeout window (typically 7 days) — after which it expires automatically
```

---

## Legacy COBOL Patterns in Payments Systems

Common patterns found in ICS/authorization COBOL code:

```cobol
* Card status check — maps to BR-001 in modern systems
EVALUATE CARD-STATUS
  WHEN 'BLK'  MOVE 'CARD_BLOCKED'    TO WS-DECLINE-REASON
               MOVE 62 TO WS-ISO-RESPONSE-CODE
               SET WS-DECLINE TO TRUE
  WHEN 'EXP'  MOVE 'CARD_EXPIRED'    TO WS-DECLINE-REASON
               MOVE 54 TO WS-ISO-RESPONSE-CODE
               SET WS-DECLINE TO TRUE
  WHEN 'STL'  MOVE 'CARD_STOLEN'     TO WS-DECLINE-REASON
               MOVE 62 TO WS-ISO-RESPONSE-CODE
               SET WS-DECLINE TO TRUE
  WHEN 'ACT'  CONTINUE               * Active — proceed to next check
  WHEN OTHER  MOVE 'UNKNOWN_STATUS'  TO WS-DECLINE-REASON
               MOVE 05 TO WS-ISO-RESPONSE-CODE
               SET WS-DECLINE TO TRUE
END-EVALUATE.

* Daily limit check — maps to BR-003
COMPUTE WS-NEW-DAILY-TOTAL = WS-CURR-DAILY-TOTAL + WS-TXN-AMOUNT.
IF WS-NEW-DAILY-TOTAL > WS-DAILY-LIMIT
   MOVE 61 TO WS-ISO-RESPONSE-CODE
   SET WS-DECLINE TO TRUE
   SET WS-LIMIT-EXCEEDED TO TRUE
END-IF.
```

**Modernization notes:**
- `CARD-STATUS` field values → `CardStatus` enum (verify ALL possible values with SME — there may be codes not shown in the example)
- `WS-ISO-RESPONSE-CODE` → ISO 8583 DE 39 field in the modern API response
- `WS-DAILY-LIMIT` is populated from a DB2 table — find the table definition, not just the field reference
- `COMP-3` packed decimal fields → `BigDecimal` in Java (never `double` or `float` for money)
- COMMAREA layout → Java DTO — each COBOL field maps to a named, typed Java field

---

## Java Implementation Patterns

### Monetary Amount Handling

```java
// CORRECT — BigDecimal with explicit scale
BigDecimal transactionAmount = new BigDecimal("10.50");
BigDecimal newDailyTotal = currentDailyTotal.add(transactionAmount);
if (newDailyTotal.compareTo(dailyLimit) > 0) {
    return AuthorizationDecision.decline(ResponseCode.EXCEEDS_DAILY_LIMIT);
}

// WRONG — never use double or float for money
double amount = 10.50; // floating-point precision loss
```

### PAN Masking

```java
public static String maskPan(String pan) {
    if (pan == null || pan.length() < 4) return "XXXX";
    return "XXXX-XXXX-XXXX-" + pan.substring(pan.length() - 4);
}
// Always call this before logging or returning PAN in any response
```

### Response Code Enum

```java
public enum ResponseCode {
    APPROVED("00"),
    REFER_TO_ISSUER("01"),
    DO_NOT_HONOR("05"),
    INVALID_TRANSACTION("12"),
    INVALID_CARD_NUMBER("14"),
    INSUFFICIENT_FUNDS("51"),
    EXPIRED_CARD("54"),
    TRANSACTION_NOT_PERMITTED("57"),
    EXCEEDS_AMOUNT_LIMIT("61"),
    RESTRICTED_CARD("62"),
    EXCEEDS_FREQUENCY_LIMIT("65"),
    ISSUER_UNAVAILABLE("91"),
    SYSTEM_MALFUNCTION("96");

    private final String isoCode;
    // constructor, getter
}
```

### Authorization Decision (Immutable)

```java
@Value  // Lombok immutable
public class AuthorizationDecision {
    String authorizationId;
    ResponseCode responseCode;
    String maskedPan;     // XXXX-XXXX-XXXX-1234
    BigDecimal amount;    // in minor units (cents)
    String currency;      // ISO 4217
    Instant decidedAt;    // UTC

    // Decisions are immutable once created — no setters, no update methods
}
```

---

## PCI-DSS Compliance Checklist for Code Review

Before any code with PCI scope merges:

- [ ] No PAN stored unencrypted (TDE or column-level encryption required)
- [ ] No CVV stored at all (not even encrypted — PCI-DSS Req. 3.3)
- [ ] PAN masked in all log output (last 4 only — enforced in logging layer)
- [ ] PAN not present in any URL (not in query params, not in path segments)
- [ ] All transmission over TLS 1.2+ (never HTTP for cardholder data)
- [ ] Access to cardholder data logged (who accessed what, when — audit trail)
- [ ] Tokenization used where possible (store token, not PAN)
- [ ] Test uses only approved test PANs (never real card numbers)
- [ ] Authorization decision records are immutable (no UPDATE on auth table)
- [ ] Declined transactions are persisted (audit trail — not discarded)

---

*FORGE Payments Domain Context | Domain: Payments & Banking | Version 2.0*
