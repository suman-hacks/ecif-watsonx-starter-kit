---
knowledge-pack: "Payments and Banking Domain"
load-when: "Any work involving card payments, authorizations, settlements, banking accounts, or financial transactions"
tools: "Claude Code, GitHub Copilot, watsonx.ai, WCA4Z"
---

# Knowledge Pack: Payments and Banking Domain

## When to Load This Pack

Load for any project involving:
- Card authorization (credit, debit, prepaid)
- Payment processing (ACH, wire, SEPA, SWIFT)
- Account management (current, savings, loan)
- Settlement and reconciliation
- Fraud detection integration
- Limits management (daily, transaction, velocity)
- PCI-DSS scope

## Domain Glossary

```text
PAYMENTS DOMAIN GLOSSARY — inject into AI session

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
  ACTIVE: Normal transacting status
  BLOCKED: Permanently blocked — cannot transact (fraud, loss, regulatory)
  SUSPENDED: Temporarily restricted (usually reversible — delinquency, customer request)
  EXPIRED: Past expiry date
  STOLEN: Reported stolen — always decline, may trigger fraud alert
  LOST: Reported lost — always decline
  CLOSED: Account closed — no transactions
  
PCI-DSS SCOPE — fields that are in scope:
  In scope: PAN, expiry date, cardholder name, CVV/CVV2/CVC (in transit only), PIN block
  Restricted: CVV/CVV2 must not be stored after authorization
  Must be encrypted at rest: PAN, expiry, cardholder name
  Must be masked in logs/UI: PAN (last 4 only)
```

## Domain Constraints for AI

```text
AI BEHAVIORAL RULES FOR PAYMENTS DOMAIN

Data handling:
1. NEVER generate or process real card numbers — use test PANs:
   - Visa: 4111 1111 1111 1111 (always approved in test)
   - Mastercard: 5500 0000 0000 0004
   - Amex: 3714 496353 98431
   - Declined test: 4000 0000 0000 0002
2. NEVER store CVV — in tests use "123" and explicitly comment that it's not stored
3. All amounts in minor currency units (cents) in storage: $10.50 stored as 1050
4. Currency: always ISO 4217 three-letter code (USD, EUR, GBP)
5. PAN masking: function that returns "XXXX-XXXX-XXXX-" + last4
6. Timestamps: always UTC; never local time; always TIMESTAMPTZ in PostgreSQL

Authorization logic ordering (standard):
1. Card existence check (card not found → decline code 14)
2. Card status check (blocked/stolen → decline code 62; expired → 54)
3. Card velocity check (too many transactions in time window → 65)
4. Transaction amount limit check (daily limit → 61)
5. Fraud score evaluation (risk above threshold → 05)
6. Approval (response code 00)

Do NOT change this order without explicit SME/architect instruction.
Earlier checks short-circuit — do not evaluate later checks if earlier ones fail.

Business invariants:
- An authorization decision is immutable once made (do not allow updates)
- Declined authorizations are recorded, not discarded (audit trail requirement)
- Amount must be positive (> 0)
- Currency must match card currency or FX rules must be applied
- Authorization has a timeout window (typically 7 days) — after which it expires
```

## Legacy COBOL Patterns in Payments Systems

Common patterns in ICS/authorization COBOL code:

```cobol
* Card status check — maps to BR-001 in modern systems
EVALUATE CARD-STATUS
  WHEN 'BLK'  SET WS-DECLINE-REASON TO 'CARD_BLOCKED'
               MOVE 62 TO WS-ISO-RESPONSE-CODE
               SET WS-DECLINE TO TRUE
  WHEN 'EXP'  SET WS-DECLINE-REASON TO 'CARD_EXPIRED'
               MOVE 54 TO WS-ISO-RESPONSE-CODE
               SET WS-DECLINE TO TRUE
  WHEN 'STL'  SET WS-DECLINE-REASON TO 'CARD_STOLEN'
               MOVE 62 TO WS-ISO-RESPONSE-CODE
               SET WS-DECLINE TO TRUE
  WHEN 'ACT'  CONTINUE  * Active — proceed to next check
  WHEN OTHER  SET WS-DECLINE-REASON TO 'UNKNOWN_STATUS'
               MOVE 05 TO WS-ISO-RESPONSE-CODE
               SET WS-DECLINE TO TRUE
END-EVALUATE.

* Daily limit check — maps to BR-003
COMPUTE WS-NEW-DAILY-TOTAL = WS-CURR-DAILY-TOTAL + WS-TXN-AMOUNT
IF WS-NEW-DAILY-TOTAL > WS-DAILY-LIMIT
   MOVE 61 TO WS-ISO-RESPONSE-CODE
   SET WS-DECLINE TO TRUE
   SET WS-LIMIT-EXCEEDED TO TRUE
END-IF.
```

**Modernization notes:**
- `CARD-STATUS` field values map to `CardStatus` enum — verify all possible values with SME
- `WS-ISO-RESPONSE-CODE` maps to the ISO 8583 response code in the modern API response
- `WS-DAILY-LIMIT` is populated from a table (often DB2) — find the table, not just the field
- `COMP-3` packed decimal fields → `BigDecimal` in Java (never `double` or `float` for money)
- COMMAREA layout → Java DTO — each field maps to a named, typed field

## PCI-DSS Compliance Checklist for Code Review

Before any code with PCI scope merges:
- [ ] No PAN stored unencrypted (TDE or column encryption)
- [ ] No CVV stored at all (not even encrypted)
- [ ] PAN masked in all log output (last 4 only)
- [ ] PAN not present in any URL (query params or path)
- [ ] All transmission over TLS 1.2+
- [ ] Access to cardholder data logged (who accessed what, when)
- [ ] Tokenization used where possible (store token, not PAN)
