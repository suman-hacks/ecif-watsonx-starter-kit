---
context: "API Modernization"
use-when: "Replacing or exposing legacy functionality via modern REST/GraphQL/gRPC APIs; retiring SOAP or proprietary protocols"
load-with: "constitution/01-core-principles.md + constitution/03-architecture-standards.md"
---

# Project Context: API Modernization

## What This Context Is For

API modernization covers projects where the goal is to:
- Expose legacy functionality (COBOL, CICS, EJB, SOAP) via modern REST or GraphQL APIs
- Retire internal RPC calls, MQ-based integrations, or direct DB access in favor of API contracts
- Introduce API gateways, versioning, and self-service developer experience
- Create a façade/anti-corruption layer over a legacy system being replaced incrementally

This is distinct from full greenfield (no legacy) and full mainframe modernization (replacing the entire program). Here the legacy system **stays** and an API layer is built on top of or around it.

---

## AI Behavior for This Context

### What AI Must Do

1. **Understand the existing behavior first** — before designing any API contract, the legacy behavior must be analyzed (Stage 01–02)
2. **Design APIs to be consumer-first** — APIs are designed for the consumer's use case, not as a direct mapping of the legacy data model
3. **Separate the API contract from the implementation** — the API contract must be stable even if the backend changes
4. **Version from day one** — every API starts at `/v1/`; breaking changes increment the major version
5. **Anti-corruption layer pattern** — the new API translates between modern consumer semantics and legacy system semantics; these must not leak through

### What AI Must Not Do

- Map legacy field names directly to API fields (e.g., `WS-TXN-AMT` should become `amount`, not `wsTxnAmt`)
- Expose legacy response codes directly without mapping (ISO 8583 codes must be translated to domain-meaningful codes)
- Design APIs that require consumers to know about the legacy architecture
- Skip the contract design phase and go straight to implementation

---

## Typical API Modernization Architecture

```
Consumer (mobile app / partner / internal service)
         │
         ▼
    API Gateway (auth, rate limiting, routing, logging)
         │
         ▼
    API Service (new — Spring Boot / Node / FastAPI)
    ├── Request validation and transformation
    ├── Business logic (where applicable)
    └── Legacy Adapter
              │
              ▼
    Legacy System (COBOL/CICS, SOAP, EJB, IBM MQ, direct DB)
```

**Key principle:** The legacy system is a black box to the API consumer. All translation happens in the adapter layer.

---

## Anti-Corruption Layer (ACL) Design

The ACL translates between:

| Consumer Layer (modern) | Legacy Layer |
|---|---|
| `cardId` (UUID string) | `CARD-NUMBER` (PIC X(19) COBOL field) |
| `decision: "APPROVED"` | `WS-RESPONSE-CODE = '00'` |
| `declineReason: "CARD_BLOCKED"` | `WS-RESPONSE-CODE = '62'` |
| `amount: 150.00` (decimal) | `WS-TXN-AMOUNT` (COMP-3 packed decimal, in cents) |
| `timestamp: ISO8601` | `WS-DATE` + `WS-TIME` (separate COBOL fields) |

ACL responsibilities:
- Request translation: modern DTO → legacy COMMAREA/payload
- Response translation: legacy response → modern DTO
- Error mapping: legacy error codes → business-meaningful error codes
- Protocol bridge: REST → CICS LINK, REST → IBM MQ, REST → SOAP

---

## API Design Rules for This Context

### Consumer-First Naming
- Use business domain language, not legacy system language
- Field names reflect what the data means, not where it comes from
- Verify naming with the API consumer team before finalizing

### Stability Commitment
- The published API contract is a commitment to consumers
- Breaking changes require a new major version AND a migration period
- Minimum support for previous version: 12 months after new version GA
- Deprecation headers must be set before a version is retired

### Documentation Requirements
- Every endpoint has an OpenAPI spec (use `sdlc/03-architecture/P3-api-contract-design.md`)
- Every field has a description explaining its business meaning
- Every error code has a description explaining what the consumer should do
- Developer portal or README with getting-started guide

---

## SDLC Emphasis for API Modernization

| Stage | Emphasis | Key Output |
|---|---|---|
| 00 Pre-Engagement | Map all existing consumers + integration protocols | Integration catalog |
| 01 Discovery | Deep legacy behavioral analysis | Which endpoints to expose, which to redesign |
| 02 Requirements | Consumer interviews — what do they actually need? | API requirements from consumer perspective |
| 03 Architecture | ACL design, API gateway selection, versioning strategy | API contract v1 + ACL design |
| 04 Detailed Design | Field-by-field ACL mapping, error code mapping | ACL mapping table |
| 05 Development | ACL implementation + contract tests | Working API + contract test suite |
| 06 Testing | Contract tests (consumer-driven), integration tests | All consumers verified against contracts |
| 07 Security | API security review (auth, rate limiting, injection) | Security sign-off |
| 08 Deployment | Blue-green deployment (no downtime for consumers) | Zero-downtime cutover plan |
| 09 Operations | API analytics, consumer adoption tracking | Usage dashboards |

---

## Anti-Patterns to Flag

| Anti-Pattern | Why It's a Problem | FORGE Response |
|---|---|---|
| Exposing legacy field names in API | Creates tight coupling to legacy internals | ACL must translate all field names |
| One API endpoint per legacy function | Leads to chatty APIs; bad developer experience | Design around consumer use cases |
| No versioning from day one | Breaking consumers on any change | Start with `/v1/` always |
| SOAP-to-REST wrapper with no redesign | Just adds a layer; doesn't improve usability | Redesign the API contract for REST semantics |
| Skipping contract tests | Consumer integration breaks undetected | Consumer-driven contract tests are mandatory |
| Direct DB access bypass | Bypasses the business logic layer | All access via API — no direct DB shortcuts |

---

## Tools for API Modernization

| Tool | Use |
|---|---|
| Claude Code | ACL design, contract design, code generation |
| GitHub Copilot | Controller implementation, DTO generation |
| Postman / Bruno | API contract testing and documentation |
| Pact | Consumer-driven contract tests |
| AWS API Gateway / Apigee / IBM API Connect | API gateway runtime |
| Swagger/OpenAPI Generator | Generate client SDKs from contract |
| WCA4Z | Legacy analysis of COBOL/CICS that API will expose |

---

## Activation: Load Into AI Session

```text
PROJECT CONTEXT: API Modernization

I am modernizing a legacy [COBOL/SOAP/EJB] system by exposing its functionality through a modern REST API.
The legacy system WILL NOT be replaced in this project — only wrapped with an API layer.

Key rules for this project:
1. Design APIs consumer-first — do not mirror the legacy data model
2. All legacy-to-modern translation happens in an Anti-Corruption Layer (ACL)
3. No legacy field names, error codes, or architecture details should leak into API contracts
4. Every API starts versioned at /v1/
5. Contract is a commitment — breaking changes require a new major version with 12-month support overlap
6. Consumer-driven contract tests are mandatory before any API goes to production

Legacy system: [describe briefly]
Primary consumers: [list — internal services / mobile apps / partner APIs]
Current integration protocol: [CICS LINK / IBM MQ / SOAP / direct DB]
```
