# FORGE Delivery Package Template

> Use this template at the end of each FORGE stage to produce a stakeholder-ready delivery package.
> The `/package-delivery` Claude skill populates this template automatically.
>
> **Purpose:** Provide a single document that traces every business requirement to its implementation,
> surfaces all open questions and gaps, confirms the review gate status, and records who approved what.

---

# FORGE Delivery Package: [PROJECT NAME] — [STAGE NAME]

**Packaged by:** [AI tool + engineer name]
**Date:** [date]
**Package version:** v[N.0]
**Status:** `COMPLETE` | `COMPLETE WITH GAPS` | `INCOMPLETE — DO NOT RELEASE`

> **Status guide:**
> - `COMPLETE` — All artifacts present, review gate passed, no blocking gaps.
> - `COMPLETE WITH GAPS` — All mandatory artifacts present, gate passed, but non-blocking gaps are documented and accepted.
> - `INCOMPLETE — DO NOT RELEASE` — Missing mandatory artifacts or blocking gaps outstanding. Do not proceed to next stage.

---

## Package Contents

| Artifact | File / Location | Status | Produced By | Reviewed? |
|---|---|---|---|---|
| Discovery Report | `analysis/discovery-report.md` | Complete \| Partial \| Missing | [Agent/Engineer] | Yes \| Pending \| N/A |
| Legacy Analysis | `analysis/[program]-understanding.md` | Complete \| Partial \| Missing | [Agent/Engineer] | Yes \| Pending |
| Business Rules Register | `analysis/business-rules-register.md` | Complete \| Partial \| Missing | [Agent/Engineer] | SME Pending |
| Architecture Package | `architecture/service-map.md` | Complete \| Partial \| Missing | [Agent/Engineer] | Architect Pending |
| ADRs | `docs/architecture/adr-*.md` | [N] ADRs present | [Agent/Engineer] | [N] approved |
| Implementation | `src/` | Complete \| Partial \| Missing | [Agent/Engineer] | Code review complete |
| Code Review Report | `reviews/[service]-review.md` | Complete \| Partial \| Missing | [Agent/Engineer] | — |
| Test Results | `target/surefire-reports/` | Pass \| Fail \| Not run | CI/CD | — |

---

## Traceability Matrix

Every business rule must trace from source to test. Any `MISSING` entry is a gap that must be resolved or accepted before promotion.

| Business Rule | Requirement / Story | Architecture Component | Implementation File | Test File | Test Status |
|---|---|---|---|---|---|
| BR-001 | US-014 | `AuthorizationService.CardStatusValidator` | `CardStatusValidator.java` | `CardStatusValidatorTest.java` | Passing |
| BR-002 | US-014 | `AuthorizationService.CardStatusValidator` | `CardStatusValidator.java` | `CardStatusValidatorTest.java` | Passing |
| BR-003 | US-015 | `LimitService.DailyLimitEvaluator` | `DailyLimitEvaluator.java` | `DailyLimitEvaluatorTest.java` | Passing |
| BR-008 | US-022 | `RiskService.FraudEvaluator` | `FraudEvaluator.java` | `FraudEvaluatorTest.java` | **MISSING** |

> Add a row for every BR-NNN entry in the business rules register. If a rule has no corresponding implementation file, that is a gap.

---

## Review Gate Status

**Gate evaluated:** Gate [N] — [Gate Name] (from `governance/review-gates.md`)

| Checklist Item | Status | Notes |
|---|---|---|
| [Gate check item 1] | Pass \| Fail \| Pending | [notes] |
| [Gate check item 2] | Pass \| Fail \| Pending | [notes] |
| All mandatory artifacts present | Pass \| Fail | — |
| All open questions resolved or formally accepted | Pass \| Pending | [list outstanding] |
| Human review completed for all required approvals | Pass \| Pending | [list outstanding reviewers] |
| No CRITICAL findings outstanding | Pass \| Fail | [list CRITICALs if any] |

**Gate decision:** `OPEN` | `PASSED` | `FAILED`

**Blocking items:**
- [List items that prevent the gate from passing, or "None"]

> Gate PASSED means all mandatory items above are Pass. Do not mark PASSED if any mandatory item is Pending or Fail.

---

## Open Questions and Gaps

### Unresolved Questions

| Q# | Source | Question | Status | Decision / Owner |
|---|---|---|---|---|
| Q-001 | Legacy Analysis | What is the max daily limit for Premium cards? | Open | Assigned to [SME name] |
| Q-002 | Rules Extraction | Is BLOCKED status the same as SUSPENDED for regulatory holds? | Open | Awaiting legal review |
| A-001 | Architecture | Kafka vs RabbitMQ for event bus — awaiting infrastructure team | Open | [CTO / Architect] decision needed |

> Carry forward all Q# items from prior stage outputs. Mark resolved items as Closed with the decision recorded.

### Gaps in Deliverables

| Gap ID | Artifact | Description | Severity | Accepted Risk? | Owner |
|---|---|---|---|---|---|
| GAP-001 | Implementation | BR-008 (Fraud Evaluator) not implemented | High | No — must resolve | Lead Engineer |
| GAP-002 | Architecture | API contract for `/limits` not reviewed by consumer team | Medium | Accepted — review scheduled | Architect |

> A gap is a finding, not a blocker by default. The business decides whether to accept the risk and proceed, or to resolve it before proceeding. Acceptance must be recorded here with the approver's name.

---

## Decisions Log

| Decision | Made By | Date | Rationale | Impact on Delivery |
|---|---|---|---|---|
| Use hexagonal architecture | Lead Architect | [date] | Enables testability and adapter replacement without core changes | All services follow this structure |
| PostgreSQL over Oracle | ADR-002 | [date] | Cost reduction and operational simplicity | DBA migration team required |
| Strangler Fig for authorization module | ADR-005 | [date] | Minimizes risk during phased migration; allows rollback | Traffic shifting required in Phase 2 |

---

## Sign-Off Requirements

| Sign-Off | Role | Mandatory? | Status | Signed Off By | Date |
|---|---|---|---|---|---|
| Business rules validated | SME / Domain Expert | Yes | Pending | — | — |
| Architecture approved | Lead Architect | Yes | Pending | — | — |
| Security review | Security Engineer | Yes (before UAT) | Not started | — | — |
| Code review | Lead Engineer | Yes | Complete | [name] | [date] |
| UAT sign-off | Product Owner | Yes (before release) | Not this stage | — | — |

---

## Next Stage: Recommended Actions

**Before proceeding to [Next Stage Name]:**
1. [ ] Resolve Q-001 (daily limit for Premium cards) — assign to [SME name], deadline [date]
2. [ ] Implement BR-008 (Fraud Evaluator) — estimated [N] sprints
3. [ ] Obtain Lead Architect sign-off on ADR-001 through ADR-005
4. [ ] Schedule consumer team API contract review for `/limits` endpoint

**Estimated readiness for next stage:** [Sprint N / target date]

---

## Audit Trail

| Event | Date | By | Notes |
|---|---|---|---|
| Stage started | [date] | [name] | — |
| Discovery report produced | [date] | [name/tool] | — |
| Legacy analysis completed | [date] | [name/tool] | Programs analyzed: [list] |
| Business rules extracted | [date] | [name/tool] | [N] rules, [N] pending SME review |
| Architecture designed | [date] | [name/tool] | [N] services, [N] ADRs |
| Code generated | [date] | [name/tool] | [services] complete; [services] partial |
| Code reviewed | [date] | [name] | [N] CRITICAL resolved; [N] MINOR accepted |
| Package assembled | [date] | [name/tool] | This document |

---

## How to Use This Template

**With Claude Code (`/package-delivery` skill):**
```
/package-delivery

Stage: Stage 1 — Legacy Understanding
Project: Payment Authorization Modernization

Artifacts available:
- Discovery report: analysis/discovery-report.md
- Business rules register: analysis/business-rules-register.md
- Open questions: [paste all Q# items from analysis outputs]

Gate to evaluate: Gate 1 (Stage 1 → Stage 2 promotion)
```

**Manually:**
1. Copy this template to `deliveries/stage-[N]-delivery-package.md`
2. Fill in all sections based on your stage outputs
3. Run `/review-code` to populate the Code Review section before the gate
4. Submit for human sign-off using the Sign-Off Requirements table
5. Archive in `traceability/` after sign-off for audit trail

---

*FORGE Delivery Package Template | Governance | Version 2.0*
*Based on FORGE Constitution Rule 5 — Traceability is mandatory.*
