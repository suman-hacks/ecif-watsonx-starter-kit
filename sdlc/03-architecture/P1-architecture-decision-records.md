# P1 — Architecture Decision Records (ADRs)

**Stage:** 03 — Architecture  
**Persona:** Solution Architect, Lead Engineer  
**Output file:** `docs/architecture/decisions/ADR-NNN-[title].md`

---

## The Prompt

```text
You are a principal software architect writing Architecture Decision Records (ADRs) for an enterprise system.

CONTEXT
System: [NAME AND PURPOSE]
Tech stack target: [languages, frameworks, cloud platform]
Key constraints: [NFRs, compliance, organizational standards]
Business requirements driving this decision: [PASTE RELEVANT USER STORIES OR NFRS]

DECISIONS TO DOCUMENT
[List the architectural decisions needing ADRs, e.g.:
- Service decomposition strategy (monolith vs microservices)
- Data consistency model (strong vs eventual)
- Synchronous vs asynchronous communication between services
- Persistence technology selection
- API versioning strategy
- Authentication and authorization mechanism
- Deployment model (containers / serverless / VMs)
- Observability stack]

TASK
For EACH decision listed, produce a complete ADR following this exact format:

---
# ADR-[NNN]: [Decision Title — imperative, e.g., "Use Event Sourcing for Payment Commands"]

**Date:** [YYYY-MM-DD]
**Status:** Proposed
**Deciders:** [list roles — Architect, Lead Engineer, Security, Data]
**FORGE Stage:** 03 — Architecture

## Context
[2-4 sentences describing the situation that forced this decision. What problem exists? What forces are at play? What would happen if no decision were made?]

## Decision Drivers
- [Specific NFR or business requirement — cite the ID, e.g., NFR-007: 99.99% uptime]
- [Second driver]

## Options Considered

### Option A: [Name]
**Description:** [1-2 sentences]
**Pros:**
- [pro 1]
**Cons:**
- [con 1]
**Risk:** [key risk]

### Option B: [Name]
[same structure]

### Option C: [Name] *(if applicable)*
[same structure]

## Decision
**We will implement Option [X]: [Name].**

[3-5 sentences explaining WHY this option was chosen. Reference the decision drivers. Why this over the alternatives?]

## Consequences

**Positive:**
- [benefit 1]

**Negative / Trade-offs:**
- [acknowledged drawback 1]

**Risks and Mitigations:**
- Risk: [describe] → Mitigation: [describe]

## Implementation Notes
[Any guidance that flows directly from this decision — package structure, configuration, patterns to follow]

## Review Date
[Set a date 6-12 months out to revisit this decision]

## Related ADRs
- [ADR-NNN: title — relationship]

---

RULES
- Every ADR must reference at least one NFR or user story that drives it
- Do not write ADRs for trivial decisions (what to name a variable)
- "Consequences" must include at least one negative consequence — every decision has trade-offs
- Status should be "Proposed" — humans change it to "Accepted" after review

Confidence: [High/Medium/Low] | Basis: [sources]
```

---

## ADR Index Template

Maintain an index at `docs/architecture/decisions/README.md`:

```markdown
# Architecture Decision Records

| ID | Title | Status | Date | Impact |
|---|---|---|---|---|
| ADR-001 | [Title] | Accepted | YYYY-MM-DD | High |
```

---

## Common ADR Topics for Enterprise Systems

Start with these if unsure what needs an ADR:
1. Monolith vs. microservices vs. modular monolith
2. Synchronous vs. asynchronous communication between domains
3. Shared database vs. database-per-service
4. Event sourcing vs. CRUD for write-side
5. API gateway strategy
6. Authentication mechanism (OAuth2/OIDC/mTLS)
7. Secrets management approach
8. Observability stack selection
9. CI/CD pipeline and deployment strategy
10. Legacy integration pattern (strangler fig vs. big bang)
