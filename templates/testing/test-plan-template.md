# Test Plan Template

File as `testing/test-plan-[sprint-or-release].md`.  
Produced during Stage 06. Required for Gate 6 (Testing Complete).

---

# Test Plan: [PROJECT NAME] — [Release / Sprint]

**Produced by:** [name / AI-assisted]  
**Date:** [date]  
**QA Lead:** [name]  
**Status:** Draft | Approved | In Execution | Complete

---

## Scope

**In scope:**
- [Service / feature 1]
- [Service / feature 2]

**Out of scope:**
- [What is explicitly not tested here and why]

**Business rules covered:** [BR-NNN list]

---

## Test Types

| Type | Owner | Framework | Environment | Coverage Target |
|---|---|---|---|---|
| Unit | Developers | JUnit 5 / pytest / Jest | Local / CI | 90% branch — domain + application |
| Integration | QA + Dev | Testcontainers | Dev shared | All flows in sequence diagrams |
| Contract | QA | Pact | CI | All service-to-service calls |
| E2E | QA Lead | Playwright / Karate | Staging | Critical user journeys |
| Performance | QA Lead + DevSecOps | Gatling / k6 | Performance | Per NFR catalog |
| Security | DevSecOps | OWASP ZAP / Semgrep | Staging | OWASP Top 10 |
| UAT | Business / QA | Manual / scripted | Staging | PO sign-off scenarios |

---

## Business Rule Coverage Matrix

All rules must be covered before UAT.

| Business Rule | Unit Test | Integration Test | UAT Scenario | Owner | Status |
|---|---|---|---|---|---|
| BR-001 | US-001-TC-001 | IT-001 | UAT-001 | [name] | Not started |
| BR-002 | US-001-TC-002 | IT-002 | UAT-001 | [name] | Not started |
| BR-003 | US-002-TC-001 | IT-003 | UAT-002 | [name] | Not started |

---

## Test Cases

### Unit Tests

| TC ID | BR Reference | Test Name | Expected Result |
|---|---|---|---|
| UT-001 | BR-001 | shouldDeclineWhenCardIsBlocked | ValidationResult.isEligible() = false, reason = CARD_BLOCKED |
| UT-002 | BR-002 | shouldDeclineWhenCardIsExpired | ValidationResult.isEligible() = false, reason = CARD_EXPIRED |

### Integration Tests

| TC ID | BR Reference | Scenario | Setup | Expected Result |
|---|---|---|---|---|
| IT-001 | BR-001 | POST /authorizations — card is blocked | Card with status=BLOCKED in DB | Response: decision=DECLINED, declineReason=CARD_BLOCKED |
| IT-002 | BR-003 | POST /authorizations — limit exceeded | Card at daily limit in DB | Response: decision=DECLINED, declineReason=DAILY_LIMIT_EXCEEDED |

### E2E Test Scenarios

| Scenario ID | Description | Business Rules | Steps | Expected Result | Priority |
|---|---|---|---|---|---|
| E2E-001 | Successful card authorization | BR-001 to BR-005 | [steps] | APPROVED | P1 |
| E2E-002 | Card blocked — decline | BR-001 | [steps] | DECLINED, CARD_BLOCKED | P1 |
| E2E-003 | Daily limit exceeded | BR-003 | [steps] | DECLINED, DAILY_LIMIT_EXCEEDED | P1 |

### Performance Test Scenarios

| Scenario | Load Profile | Duration | Pass Criteria |
|---|---|---|---|
| Steady-state authorization | 3,000 TPS constant | 30 min | p99 < 200ms, 0% error |
| Peak authorization | 10,000 TPS | 5 min | p99 < 500ms, < 1% error |
| Recovery after outage | Ramp from 0 to 5,000 TPS | 10 min | Recovers within 30s |

---

## Test Data Requirements

| Data Set | Description | Volume | How to Create |
|---|---|---|---|
| Active cards | Cards in ACTIVE status with various limits | 1,000 | SQL insert script — `/test-data/active-cards.sql` |
| Blocked cards | Cards in BLOCKED status | 100 | SQL insert script |
| At-limit cards | Cards at 100% of daily limit | 100 | SQL insert script |
| Near-limit cards | Cards at 90% of daily limit | 100 | SQL insert script |

---

## Test Environments

| Environment | Purpose | Database | Refresh | Access |
|---|---|---|---|---|
| Dev local | Unit tests, developer integration | Testcontainers (ephemeral) | Per run | Developer only |
| Dev shared | Integration tests | PostgreSQL — synthetic data | Weekly | Dev + QA |
| Staging | E2E, UAT, security | PostgreSQL — anonymized prod-like | Monthly | QA + Release |
| Performance | Performance tests | PostgreSQL — prod-like volume | Per campaign | QA Lead + DevSecOps |

---

## Entry and Exit Criteria

### Entry Criteria (when testing may begin)

| Phase | Criteria |
|---|---|
| Unit | Code compiles; component design reviewed |
| Integration | Unit tests passing at ≥ 90% branch coverage |
| E2E | Integration tests passing; staging environment provisioned with test data |
| Performance | E2E tests passing; performance environment with production-like data |
| UAT | All High+ defects resolved; PO briefed on test scenarios |

### Exit Criteria (phase is complete)

| Phase | Criteria |
|---|---|
| Unit | ≥ 90% branch coverage; 0 failing tests; all BR-NNN tests present |
| Integration | 0 failing tests; all flows from sequence diagrams covered |
| E2E | All P1 scenarios passing; 0 Critical/High open defects |
| Performance | All NFR targets met; no degradation at peak load |
| UAT | PO sign-off; 0 Critical/High open defects |

---

## Defect Management

| Severity | Blocks Release? | Fix SLA | Escalation |
|---|---|---|---|
| Critical | Yes — always | Same day | QA Lead + Lead Eng + PO immediately |
| High | Yes | Within sprint | QA Lead + Lead Eng daily |
| Medium | No | Next sprint | QA Lead tracks |
| Low | No | Backlog | Tracked |

---

## Sign-off

| Role | Name | Sign-off Date |
|---|---|---|
| QA Lead | | |
| Lead Engineer | | |
| Product Owner (UAT) | | |
