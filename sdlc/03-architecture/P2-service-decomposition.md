# P2 — Service Decomposition

**Stage:** 03 — Architecture  
**Persona:** Solution Architect  
**Output file:** `docs/architecture/service-map.md`

---

## The Prompt

```text
You are a principal software architect designing service boundaries for an enterprise system modernization.

CONTEXT
System being modernized/built: [NAME]
Current architecture: [MONOLITH / MAINFRAME / DISTRIBUTED — brief description]
Business domain: [e.g., "Card Issuing and Servicing"]
Target platform: [e.g., "Spring Boot microservices on Kubernetes, event-driven with Kafka"]
Team structure: [number of teams, rough sizes — Conway's Law applies]

BUSINESS CAPABILITIES / DOMAINS
[PASTE — from discovery findings, business rules register, or domain knowledge packs]
[Example: Account lifecycle, Card lifecycle, Authorization, Payments, Statements, Disputes, Notifications, Limits]

CONSTRAINTS
- [NFRs that affect decomposition — latency, team autonomy, compliance isolation]
- [Organizational constraints — existing teams, shared services]
- [Technical constraints — shared legacy systems that can't yet be decomposed]

TASK
Design the target service decomposition. For EACH service/bounded context, produce:

## Service: [Service Name]

**Bounded Context:** [Domain area this service owns]
**Responsibility (1 sentence):** [What this service does and nothing else]
**Owning Team:** [Team name or TBD]
**SLA:** [Availability %, latency target]

**Owned Data:**
- [Entity 1: description]
- [Entity 2: description]

**APIs Exposed:**
- `POST /[resource]` — [purpose]
- `GET /[resource]/{id}` — [purpose]

**Events Published:**
- `[EventName]` — [when published, what it signals]

**Events Consumed:**
- `[EventName]` (from [ServiceName]) — [why consumed, what action taken]

**Upstream Dependencies:**
- [Service name]: [what is called and why] — Sync/Async

**Downstream Dependents:**
- [Service name]: [what they need from this service]

**Migration Sequence:** [1-N — which services should be built/extracted first]
**Migration Risk:** [High/Medium/Low — rationale]
**Legacy Replacement:** [Which legacy programs/components this replaces]

---

After all services, also produce:

## Service Map Diagram (ASCII)
[Show services as boxes with arrows for sync calls (→) and event flows (⟶)]

## Data Ownership Map
| Entity | Owning Service | Other Services With Read Access | Sharing Mechanism |
|---|---|---|---|

## Communication Patterns
| From Service | To Service | Pattern | Rationale |
|---|---|---|---|
| [ServiceA] | [ServiceB] | Sync REST | User-facing query requiring immediate response |
| [ServiceA] | [ServiceB] | Async Event | Cross-domain command, loose coupling preferred |

## Migration Sequencing
Phase 1 (extract first): [services — rationale]
Phase 2: [services]
Phase N: [services]
Strangler fig boundary: [where to place the anti-corruption layer]

## Risks
| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|

## Source-to-Target Traceability (for modernization)
| Legacy Program/Component | Target Service | Migration Approach | Notes |
|---|---|---|---|

Confidence: [High/Medium/Low] | Basis: [sources]
```

---

## Anti-Patterns to Flag

Instruct the AI to flag these if it detects them:
- **Anemic services:** Service with no data ownership (just passes through calls)
- **God service:** Service responsible for multiple unrelated business capabilities
- **Shared database:** Two services accessing the same database tables
- **Synchronous chains:** Service A calls B calls C calls D synchronously (latency/availability risk)
- **Wrong granularity:** Services that are too fine-grained to own meaningful business behavior
