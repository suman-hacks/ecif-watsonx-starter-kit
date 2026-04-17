# P1: Test Strategy

## When to Use This Prompt
At the start of Stage 06 (Testing), after Gate 3 (Architecture) is approved and before development begins. The test strategy governs all testing work for the project and is the primary input for QA sprint planning.

---

## Prompt

```text
You are a FORGE QA Engineer producing a comprehensive test strategy document.

INPUTS
Project name and scope: [PROJECT NAME and what is being built/modernized]
Business rules register: [Paste Agent 03 output — or key BR-NNN list]
NFR catalog: [Paste Stage 02 P3 NFR definitions, or list key NFRs]
Architecture overview: [Paste Agent 04 service map and integration inventory]
Technology stack: [e.g., Java 21, Spring Boot, PostgreSQL, Kafka, React 18]
Risk profile: [High/Medium/Low — reason, e.g., "High — financial processing, PCI scope"]
Regulatory context: [e.g., PCI-DSS, HIPAA, SOX, GDPR, or "None identified"]

TASK
Produce the complete test strategy:

---

# Test Strategy: [PROJECT NAME]
Produced by: [AI tool]  Date: [date]
Version: 1.0
Human review required — QA Lead + Lead Engineer approval before development begins.

## Strategy Overview
[2-3 paragraphs: testing philosophy for this project, primary risk drivers, how this strategy addresses them]

---

## Test Scope

### In Scope
| Component | Test Types | Priority | Owner |
|---|---|---|---|
| [AuthorizationService] | Unit, Integration, Contract, Performance | P1 | QA + Dev |
| [API Gateway] | Integration, Security | P2 | QA |
| [Data migration] | Data quality, Reconciliation | P1 | QA + DBA |

### Out of Scope (with reason)
| Component | Reason | Risk Accepted By |
|---|---|---|
| [3rd-party fraud API] | External system — contract testing only | Architect |

---

## Test Types and Coverage Targets

### Unit Testing
**Owner:** Developers  
**When:** Written alongside code (TDD preferred, but minimum: alongside the PR)  
**Framework:** [JUnit 5 + Mockito / pytest / Jest — per tech stack]  
**Coverage Target:** 80% line, 90% branch on all domain and application classes  
**Coverage Tool:** [JaCoCo / coverage.py / Istanbul]  
**Naming Convention:** `[MethodUnderTest]_[Scenario]_[ExpectedOutcome]` or `BR-NNN_[scenario]`

**What must be unit tested:**
- Every public method in domain layer
- Every public method in application (use case) layer  
- Every business rule in the BR register must have at least one named test: `@DisplayName("BR-001: Decline when card is BLOCKED")`
- All boundary conditions (min/max values, null inputs, empty collections)
- All error paths (every thrown exception type)

### Integration Testing
**Owner:** QA + Dev joint  
**When:** Per service, before deployment to any shared environment  
**Framework:** [Testcontainers / docker-compose / Spring Boot Test]  
**Scope:** Service + real database + real message broker (containerized)  
**Coverage Target:** All primary and alternative flows (minimum 1 test per flow in sequence diagrams)

**What must be integration tested:**
- API endpoint → use case → database round trip
- Event production and consumption (at least one producer + one consumer per topic)
- Database transactions (commit, rollback, optimistic locking conflicts)
- External service adapter behavior (with WireMock stubs for external APIs)

### Contract Testing
**Owner:** QA / Architect  
**When:** Before any service-to-service integration  
**Framework:** [Pact / Spring Cloud Contract]  
**Scope:** All synchronous API calls and event contracts between services

**Contract test requirements:**
- Provider verification: run on every PR to the providing service
- Consumer-driven: consumer team writes expectations; provider must pass them
- All events in the event contract inventory must have contract tests

### End-to-End (E2E) Testing
**Owner:** QA Lead  
**When:** On staging environment; before each release candidate  
**Framework:** [Playwright / Selenium / Karate]  
**Scope:** Critical user journeys only — not every permutation  

**E2E test scenarios (must-have):**
| Scenario | Business Rule | Steps | Expected Result |
|---|---|---|---|
| [Happy path authorization] | BR-001,002,003 | [steps] | APPROVED response |
| [Card blocked decline] | BR-001 | [steps] | DECLINED + reason |
| [Limit exceeded] | BR-003 | [steps] | DECLINED + reason |

### Performance Testing
**Owner:** QA Lead + DevSecOps  
**When:** Sprint N-1 before production; repeat after each major change  
**Framework:** [Gatling / k6 / JMeter]  
**Environment:** Performance-dedicated environment with production-like data volumes

**NFR targets to validate:**
| NFR | Target | Test Type | Test Duration | Pass Criteria |
|---|---|---|---|---|
| Authorization p99 latency | < 200ms | Load test | 30 min at steady state | p99 ≤ 200ms |
| Authorization throughput | 5,000 TPS | Load test | 30 min | 0% error rate at 5,000 TPS |
| Authorization peak | 10,000 TPS | Spike test | 5 min | < 1% error rate |
| Recovery time | < 30s | Chaos (pod kill) | N/A | Service recovers in < 30s |

### Security Testing
**Owner:** DevSecOps / QA  
**When:** Before every deployment to staging; full scan before production  
**Tools:** [OWASP ZAP / Burp Suite / Semgrep / Snyk]

**Security test requirements:**
- OWASP Top 10 scan on all API endpoints
- Dependency vulnerability scan (CVE check) on every build
- Authentication and authorization boundary tests (does unauthenticated call fail?)
- PII exposure test (do responses contain unmasked sensitive data?)
- Injection test (SQL, command, LDAP)

### Data Quality / Reconciliation Testing (Modernization only)
**Owner:** QA + DBA  
**When:** During and after data migration  
**Framework:** [Custom SQL scripts + reconciliation job]

**Data quality checks:**
| Check | Query/Method | Threshold | Frequency |
|---|---|---|---|
| Row count match | COUNT(*) legacy vs new | 0 discrepancy | After each batch |
| Amount totals | SUM(amount) by card | < 0.001% discrepancy | After each batch |
| Status mapping | All legacy codes mapped | 0 unmapped | Before cutover |
| Null field check | NOT NULL constraint check | 0 violations | After migration |

---

## Business Rule Coverage Matrix

All business rules must be covered before UAT begins.

| Business Rule | Unit Test | Integration Test | UAT Scenario | Status |
|---|---|---|---|---|
| BR-001: Decline blocked cards | Required | Required | Required | Not started |
| BR-002: Decline expired cards | Required | Required | Required | Not started |
| BR-003: Daily limit exceeded | Required | Required | Required | Not started |

---

## Test Environment Plan

| Environment | Purpose | Data | Refresh Frequency | Who Has Access |
|---|---|---|---|---|
| Dev (local) | Unit + integration | Synthetic, containerized | Per developer | Dev team |
| Dev shared | Integration, contract | Synthetic | Weekly | Dev + QA |
| Staging | E2E, performance, security | Anonymized production-like | Monthly full refresh | QA + Release |
| Production | Smoke tests only | Real | N/A | Release team |

**Test data requirements:**
- Cards in every status: ACTIVE, BLOCKED, EXPIRED, SUSPENDED
- Cards at limit, approaching limit, well under limit
- Cards with no transaction history
- Invalid/malformed card numbers for negative tests
- Volume: [N million records] for performance environment

---

## Defect Management

| Severity | Definition | SLA to Fix | Blocks Release? |
|---|---|---|---|
| Critical | Data loss, security breach, business rule failure | Same day | Yes — always |
| High | Major business flow broken | Within sprint | Yes |
| Medium | Feature gap, edge case failure | Next sprint | No — tracked |
| Low | UI issue, cosmetic | Backlog | No |

**Defect triage:** Daily standup with QA Lead + Lead Engineer

---

## Entry and Exit Criteria

### Entry Criteria (per test phase)
- Unit: Component design (P1) reviewed; code compiled without errors
- Integration: Unit tests passing at coverage target; API contracts defined
- E2E: All integration tests passing; environment provisioned; test data loaded
- Performance: E2E tests passing; performance environment ready with production-like data
- UAT: Performance tests passing; all High+ defects resolved

### Exit Criteria (test phase complete)
- Unit: ≥ 80% line / 90% branch coverage; 0 failing tests; all BR-NNN tests present
- Integration: 0 failing; all flows from sequence diagrams covered
- E2E: All Critical and High scenarios passing; 0 BLOCKER defects
- Performance: All NFR targets met; p99 within target at peak load
- UAT: PO sign-off; all business scenarios signed off; 0 Critical/High open

---

## Roles and Responsibilities

| Role | Responsibility |
|---|---|
| Developer | Unit tests (mandatory, alongside code) |
| QA Engineer | Integration, contract, E2E test design and execution |
| QA Lead | Test strategy, performance, security coordination, sign-off |
| DevSecOps | Security test execution, CI/CD integration |
| Lead Engineer | Coverage gate enforcement, unit test review |
| Product Owner | UAT scenario sign-off |
| SME | Business rule validation in UAT |
```
