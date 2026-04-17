# P4: Requirements Traceability Matrix

## When to Use This Prompt
At the end of Stage 02 (Requirements) or any time requirements need to be cross-referenced with their implementation status. Required artifact for Gate 2 (Requirements Complete) and Gate 5 (Code Review Complete).

---

## Prompt

```text
You are a FORGE Business Analyst producing a requirements traceability matrix.

INPUTS
User stories: [Paste all user stories — US-NNN list with acceptance criteria]
Business rules register: [Paste Agent 03 output or business-rules-register.md]
NFR catalog: [Paste NFRs from P3-nfr-definition.md]
Architecture services (if available): [Paste Agent 04 service map or "Not yet designed"]
Test cases (if available): [Paste test plan or "Not yet written"]

TASK
Produce the complete requirements traceability matrix:

---

# Requirements Traceability Matrix: [PROJECT NAME]
Produced by: [AI tool]  Date: [date]
Status: Living document — update at each gate

---

## Coverage Summary

| Category | Total Items | Traced to Story | Traced to Test | Traced to Architecture | Gaps |
|---|---|---|---|---|---|
| Business Rules | [N] | [N] | [N] | [N] | [N] |
| User Stories | [N] | — | [N] | [N] | [N] |
| NFRs | [N] | [N] | [N] | [N] | [N] |

---

## Forward Traceability: Requirements → Implementation

Trace from requirements forward to tests and architecture.

| Requirement ID | Type | Title | User Story | Architecture Component | Test Case(s) | Implementation Status |
|---|---|---|---|---|---|---|
| BR-001 | Business Rule | Decline blocked cards | US-014 | AuthorizationService.CardStatusValidator | UT-001, IT-001, UAT-001 | Not started |
| BR-002 | Business Rule | Decline expired cards | US-014 | AuthorizationService.CardStatusValidator | UT-002, IT-002, UAT-001 | Not started |
| NFR-001 | Performance | p99 latency < 200ms | US-014, US-015 | AuthorizationService | PERF-001 | Not started |
| US-014 | User Story | Authorize card transaction | — | AuthorizationService (full) | UT-001,2, IT-001,2,3 | Not started |

---

## Backward Traceability: Test Cases → Requirements

Trace from test cases back to the requirements they verify.

| Test ID | Test Name | Type | Business Rule | User Story | NFR |
|---|---|---|---|---|---|
| UT-001 | BR-001: shouldDeclineWhenCardIsBlocked | Unit | BR-001 | US-014 | — |
| IT-001 | Card blocked — POST /authorizations returns DECLINED | Integration | BR-001 | US-014 | — |
| PERF-001 | Steady-state 3,000 TPS — p99 < 200ms | Performance | — | US-014 | NFR-001 |

---

## Gap Analysis

Requirements with NO test coverage:

| Requirement | Type | Gap Reason | Action Required | Owner |
|---|---|---|---|---|
| BR-008 | Business Rule | Not yet assigned to a user story | Create user story and test case | BA |
| NFR-003 | Availability NFR | No availability/chaos test defined | Add chaos test to test plan | QA Lead |

Requirements with NO architecture component:

| Requirement | Gap Reason | Action Required |
|---|---|---|
| BR-012 | Architecture not yet designed for reporting component | Include in architecture Stage 03 |

---

## Change Impact Log

When a requirement changes, this log tracks the downstream impact.

| Change Date | Changed Item | Change Description | Impact | Updated Items | Approved By |
|---|---|---|---|---|---|
| [date] | BR-003 | Daily limit threshold changed from $5,000 to $10,000 | LimitEvaluator logic, IT-003 test data, UAT-002 | Updated | [name] |

---

## Traceability Matrix: Compact View

Full matrix for audit purposes.

| | BR-001 | BR-002 | BR-003 | NFR-001 | NFR-002 |
|---|---|---|---|---|---|
| **US-014** | ✓ | ✓ | ✓ | ✓ | |
| **US-015** | | | ✓ | ✓ | |
| **UT-001** | ✓ | | | | |
| **UT-002** | | ✓ | | | |
| **IT-001** | ✓ | | | | |
| **PERF-001** | | | | ✓ | |
| **AuthService** | ✓ | ✓ | ✓ | ✓ | |

Legend: ✓ = traced | blank = no link

INSTRUCTIONS
- Flag every requirement with no test coverage as a gap
- Flag every test with no requirement as untraceable (potentially unnecessary)
- Include a change impact section — requirement changes cascade through the matrix
- Keep this matrix as a living document — update status fields at each sprint end
```
