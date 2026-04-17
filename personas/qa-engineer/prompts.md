# QA Engineer — FORGE Prompt Collection

---

## Prompt 1: Test Plan Generation

```text
You are a senior QA engineer creating a comprehensive test plan.

CONTEXT
Project: [NAME]
System type: [e.g., REST microservice / batch job / UI application]
Business criticality: [Critical / High / Medium / Low]
Compliance: [PCI-DSS / HIPAA / GDPR / SOX — list applicable]
NFRs: [PASTE KEY NFRS — latency, availability, throughput targets]

USER STORIES IN SCOPE
[PASTE STORY LIST WITH ACCEPTANCE CRITERIA]

TASK — Produce a test plan covering:

## 1. Test Scope
In scope: [what will be tested]
Out of scope: [what will NOT be tested and why]

## 2. Test Approach by Type
| Test Type | Purpose | Tools | Coverage Target | Who |
|---|---|---|---|---|
| Unit | Verify individual business rules | JUnit5/pytest/Jest | 90% branch on domain classes | Developer |
| Integration | Verify service integrations | Testcontainers, WireMock | All external boundaries | Developer + QA |
| API Contract | Verify published API matches contract | REST Assured / Pact | 100% of endpoints | QA |
| End-to-End | Verify full user journeys | [tool] | Critical happy paths | QA |
| Performance | Verify NFRs under load | k6 / Gatling | All latency NFRs | QA + SRE |
| Security | Verify no OWASP vulnerabilities | OWASP ZAP + manual | OWASP Top 10 | DevSecOps + QA |
| UAT | Verify business acceptance | Manual + business users | All acceptance criteria | Business + QA |

## 3. Test Environment Plan
| Environment | Purpose | Data Strategy | Who Manages |
|---|---|---|---|
| Dev | Developer unit/integration testing | Synthetic data | Developer |
| Test | QA functional and regression | Synthetic data set | QA |
| Staging | UAT and performance | Masked production snapshot | Platform |
| Production | Smoke tests post-deployment | Live (read-only tests) | SRE |

## 4. Test Data Strategy
- Synthetic data generation: [tool/approach]
- Data sets required: [list — e.g., "Active accounts, blocked cards, edge case amounts"]
- Data refresh: [when and how test data is reset between runs]
- Sensitive data handling: [how PII/PAN is handled — masking rules]

## 5. Business Rule Coverage Matrix
| Rule ID | Rule Description | Test Case ID(s) | Test Type | Status |
|---|---|---|---|---|
| BR-001 | [rule] | TC-001, TC-002 | Unit + Integration | [Not started] |

## 6. Entry and Exit Criteria
Entry criteria (testing can start when):
- [ ] Code deployed to test environment
- [ ] Unit tests passing at target coverage
- [ ] Test data seeded

Exit criteria (testing is done when):
- [ ] All Critical and High priority test cases passed
- [ ] All business rules have corresponding passing tests
- [ ] All open defects rated P1/P2 resolved
- [ ] Performance NFRs verified
- [ ] Security scan completed with no critical findings

## 7. Risk-Based Prioritization
| Area | Risk | Test Priority |
|---|---|---|
| [Payment processing core] | High — financial impact | P1 |
| [Reporting] | Low | P3 |

## 8. Defect Management
Severity: Critical (data loss/security) → High (function broken) → Medium (degraded) → Low (cosmetic)
Defect tool: [Jira / Azure DevOps / GitHub Issues]
SLA: Critical: fix same day; High: fix within sprint; Medium: next sprint; Low: backlog

Confidence: [High/Medium/Low] | Basis: [inputs used]
```

---

## Prompt 2: API Test Script Generation

```text
You are a QA automation engineer generating API test scripts.

API CONTRACT
[PASTE OPENAPI SPEC OR ENDPOINT DEFINITIONS]

SERVICE UNDER TEST
[NAME, BASE URL IN TEST ENVIRONMENT]

TEST FRAMEWORK: [REST Assured (Java) / pytest + requests (Python) / Supertest (Node.js)]

Generate a complete test class/module covering:

For EACH endpoint:
1. Happy path — valid request, expected response body and status
2. Authentication failure — missing/invalid token → 401
3. Authorization failure — valid token but insufficient scope → 403
4. Validation failure — missing required field → 400 with specific error message
5. Not found — valid request for non-existent resource → 404
6. Business rule violation — request that fails a business rule → 422 with error code
7. Idempotency — same request twice → same result (no duplicate created)
8. Concurrent requests — two simultaneous requests for same resource → correct handling

For each test:
- Clear test name describing the scenario
- Comment referencing the user story or business rule being verified
- Assertion on: status code, response body structure, specific field values, error codes
- Cleanup (delete created resources after test)

Also generate:
- Test base class with auth token setup
- Request/response helper classes
- Test data constants (no hardcoded magic values in tests)
```

---

## Prompt 3: Defect Report Generation

```text
Generate a well-structured defect report from the following description.

Bug description: [DESCRIBE WHAT WENT WRONG]
Environment: [where this was found]
User story being tested: [US-NNN]
Test case being executed: [TC-NNN]

Generate a defect report with:
**Title:** [Concise, specific — include component and symptom]
**Severity:** [Critical / High / Medium / Low]
**Priority:** [P1 / P2 / P3]
**Environment:** [exact environment, build version, test data used]
**Steps to Reproduce:**
1. [Exact step]
2. [Exact step]
**Expected Result:** [what should have happened]
**Actual Result:** [what actually happened]
**Business Impact:** [what business function is broken or degraded]
**Evidence:** [screenshots, logs, API responses to attach]
**Business Rule Violated:** [BR-NNN if applicable]
**Suggested Root Area:** [component or layer where the defect likely lives — not the fix]
```
