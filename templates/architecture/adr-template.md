# Architecture Decision Record Template

File as `architecture/adrs/ADR-NNN-[short-title].md`. Number sequentially. Never delete — deprecate instead.

---

# ADR-[NNN]: [Decision Title]

**Status:** Proposed | Accepted | Deprecated | Superseded by ADR-NNN  
**Date:** [date]  
**Deciders:** [Lead Architect, Lead Engineer, CTO — whoever was involved]  
**Tags:** [e.g., data-storage, messaging, security, scalability]

---

## Context

[2-4 sentences: What is the situation? What is driving the need for this decision? What constraints exist?]

*Example: The authorization service needs to store card transaction data. We have an existing Oracle 19c enterprise license, but the team has stronger PostgreSQL expertise and the new cloud environment supports both. The data volume is estimated at 50M rows/year with a 7-year retention requirement.*

---

## Decision Drivers

- [Driver 1 — e.g., Team expertise in PostgreSQL reduces operational risk]
- [Driver 2 — e.g., Cost — Oracle licensing adds $X/year to operating cost]
- [Driver 3 — e.g., Cloud-native tools (RDS, Aurora) align with existing infrastructure]
- [Driver 4 — e.g., PCI-DSS compliance — both options support TDE encryption]

---

## Options Considered

### Option A: [Name]

[3-5 sentences describing the option.]

**Pros:**
- [Pro 1]
- [Pro 2]

**Cons:**
- [Con 1]
- [Con 2]

**Estimated effort:** [Small / Medium / Large]  
**Risk:** [Low / Medium / High — reason]

---

### Option B: [Name]

[Description]

**Pros:**
- [Pro 1]

**Cons:**
- [Con 1]

**Estimated effort:**  
**Risk:**

---

### Option C: [Name — if applicable]

[Description or "Not evaluated — outside project constraints because [reason]"]

---

## Decision

**Chosen option: Option [A/B/C] — [Name]**

[2-3 sentences: Why this option was chosen. What was the deciding factor?]

*Example: PostgreSQL via AWS RDS Aurora was chosen because it eliminates Oracle licensing cost ($X/year), the team has operational expertise, and AWS Aurora provides automated failover for our 99.95% availability NFR. The migration effort from Oracle is accepted as a one-time cost.*

---

## Consequences

### Positive
- [Benefit 1]
- [Benefit 2]

### Negative / Trade-offs
- [Trade-off 1 — e.g., One-time migration from Oracle adds 2 sprints to schedule]
- [Trade-off 2 — e.g., Some Oracle-specific features (PL/SQL packages) must be rewritten]

### Risks
- [Risk 1 — e.g., Schema migration data loss risk → Mitigated by: dual-write + reconciliation]
- [Risk 2]

---

## Business Rules Affected

| Rule | Impact |
|---|---|
| BR-NNN | [How this decision affects the implementation of this rule] |

---

## Implementation Notes

[Optional: Any specific implementation guidance that came out of this decision.]

- [Note 1]
- [Note 2]

---

## Review

**Review by:** [role] by [date]  
**Status changed to Accepted by:** [name] on [date]  
**Superseded by:** [ADR-NNN — if deprecated]
