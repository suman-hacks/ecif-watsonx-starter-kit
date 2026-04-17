# FORGE Audit Trail Template

Copy this template into each project's `governance/` folder as `audit-trail.md`. Update it throughout the engagement as gates are passed and decisions are made.

---

# Project Audit Trail: [PROJECT NAME]

**Project:** [name]  
**Start date:** [date]  
**Framework version:** FORGE v[version]  
**Lead Engineer:** [name]  
**Architect:** [name]  
**Product Owner:** [name]

---

## Stage Progression Log

| Stage | Start Date | Complete Date | Gate | Gate Signed By | Notes |
|---|---|---|---|---|---|
| 00 — Pre-Engagement | | | Gate 0 | | |
| 01 — Discovery | | | Gate 1 | | |
| 02 — Requirements | | | Gate 2 | | |
| 03 — Architecture | | | Gate 3 | | |
| 04 — Detailed Design | | | — | | |
| 05 — Development | | | Gate 5 | | |
| 06 — Testing | | | Gate 6 | | |
| 07 — Security | | | Gate 7 | | |
| 08 — Deployment | | | Gate 8 | | |
| 09 — Operations | | | Ongoing | | |

---

## AI Tool Usage Log

Track which AI tools produced which artifacts. Required for transparency and reproducibility.

| Date | Tool | Model/Version | Stage | Artifact(s) Produced | Reviewed By | Notes |
|---|---|---|---|---|---|---|
| [date] | Claude Code | claude-sonnet-4-6 | 01 | discovery-report.md | [name] | |
| [date] | WCA4Z | IBM Granite | 02 | legacy-behavior-summary-AUTHPROG.md | [name] | |
| [date] | GitHub Copilot | GPT-4o | 05 | AuthorizationService.java | [name] | |
| [date] | Claude Code | claude-sonnet-4-6 | 06 | auth-service-review.md | [name] | |

---

## Key Decisions Log

| Decision ID | Date | Decision | Made By | Rationale | Impact | ADR Ref |
|---|---|---|---|---|---|---|
| D-001 | | Use hexagonal architecture | Architect | Testability, adapter replacement | All services follow this structure | ADR-001 |
| D-002 | | PostgreSQL over Oracle | Architect + CTO | Cost, operational simplicity | DBA migration required | ADR-002 |
| D-003 | | Kafka for event bus | Architect | Fan-out to 3 consumers; async | Event contracts defined | ADR-003 |

---

## Assumption Register

All assumptions made during the engagement. Each must be resolved before the relevant stage completes.

| Assumption ID | Stage Raised | Description | Source | Status | Resolved By | Resolution Date |
|---|---|---|---|---|---|---|
| A-001 | 02 — Analysis | 'BLK' maps to BLOCKED status | Agent 02 inference | Confirmed | Jane Smith (SME) | [date] |
| A-002 | 02 — Analysis | Daily limit table is ACCT_LIMITS DB2 table | Agent 02 inference | Open — awaiting SME | TBD | |
| A-003 | 03 — Architecture | Fraud service SLA is 200ms | Architecture assumption | Confirmed | Fraud team | [date] |

---

## Open Questions Log

Track all questions raised by agents or team members that require resolution.

| Q# | Raised By | Stage | Question | Why It Matters | Assigned To | Status | Answer |
|---|---|---|---|---|---|---|---|
| Q-001 | Agent 03 | 02 | What is the daily limit for Premium cards? | BR-003 threshold | Jane Smith | Open | |
| Q-002 | Agent 02 | 02 | Is SUSPENDED status the same as regulatory hold? | Rule scoping | Legal team | Resolved | Different — SUSPENDED = customer-requested, HOLD = regulatory |
| Q-003 | Agent 04 | 03 | Does fraud service support circuit breaker pattern? | NFR for resilience | Fraud API team | Open | |

---

## Sensitive Content Review Log

Required for PCI/HIPAA/regulated projects. Record each AI session involving source code.

| Date | Session Type | Reviewer | PII Found? | Action Taken | Sign-off |
|---|---|---|---|---|---|
| [date] | COBOL analysis session | [name] | No | Proceeded | [initials] |
| [date] | Data model design | [name] | Simulated card numbers only | Verified test data only | [initials] |
| [date] | Code review | [name] | No PII in generated code | Confirmed | [initials] |

---

## Change Log

| Date | Change | Requested By | Approved By | Impact |
|---|---|---|---|---|
| [date] | Added fraud service integration to scope | PO | Architect + Lead Eng | 2 new business rules; ADR-007 added |
| [date] | Removed batch reconciliation from MVP scope | PO | Steering committee | Deferred to Phase 2 |

---

## Delivery Milestones

| Milestone | Planned Date | Actual Date | Status | Sign-off |
|---|---|---|---|---|
| Discovery complete | | | | |
| Business rules approved | | | | |
| Architecture approved | | | | |
| Sprint 1 code complete | | | | |
| Security review complete | | | | |
| UAT complete | | | | |
| Go-live | | | | |

---

## Final Sign-off Record

For Gate 8 (Production Deployment Approval):

| Role | Name | Sign-off Date | Notes |
|---|---|---|---|
| Lead Engineer | | | |
| Product Owner | | | |
| DevSecOps | | | |
| Security Engineer | | | |
| Change Manager | | | |
