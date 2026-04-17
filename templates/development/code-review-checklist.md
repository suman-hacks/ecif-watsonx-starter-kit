# Code Review Checklist

Use for every PR. Complete before requesting review. Reviewer uses same checklist.

---

# Code Review: [SERVICE / PR TITLE]

**PR:** [link]  
**Reviewer:** [name]  
**Date:** [date]  
**Business rules in scope:** [BR-NNN list]  
**Architecture spec:** [link to service design]

---

## Author Pre-Submission Checklist

Complete before marking PR as Ready for Review:

### Basics
- [ ] Code compiles without warnings
- [ ] All existing tests still pass
- [ ] New unit tests added for all new public methods
- [ ] Every business rule (BR-NNN) has at least one named test

### Architecture Conformance
- [ ] No business logic in controllers (`@RestController` / `@Controller`)
- [ ] No direct DB calls from domain layer (only via port interfaces)
- [ ] No cross-service database access
- [ ] All external calls go through adapter interfaces
- [ ] No hardcoded URLs, credentials, or magic numbers

### Security
- [ ] All SQL via ORM or parameterized queries (no string concatenation)
- [ ] Input validation at API boundary
- [ ] No PII or credentials in any log statement
- [ ] No sensitive data in error responses
- [ ] Authentication required on all non-public endpoints

### Observability
- [ ] Structured log statement at completion of every business operation
- [ ] Correlation ID propagated to all downstream calls
- [ ] No PII in any log field
- [ ] New metrics added for any new business operations

### Code Quality
- [ ] No method longer than 30 lines
- [ ] No class longer than 300 lines
- [ ] No commented-out code
- [ ] No TODO/FIXME without a linked ticket

---

## Reviewer Checklist

### Correctness
- [ ] Business rules implemented correctly per BR-NNN register
- [ ] All edge cases handled (null inputs, empty collections, boundary values)
- [ ] Error paths produce correct HTTP status codes and error bodies
- [ ] No race conditions in concurrent scenarios

### Architecture (findings go in review comments with severity)
- [ ] Hexagonal package structure followed
- [ ] No business logic in controllers
- [ ] No infrastructure imports in domain classes
- [ ] Port/adapter pattern used for all external dependencies
- [ ] Data ownership boundaries respected

### Security (any failure = BLOCKER)
- [ ] OWASP A03: No SQL/command injection risk
- [ ] OWASP A01: All endpoints require appropriate authentication
- [ ] OWASP A09: No PII or credentials in logs
- [ ] OWASP A05: No stack traces or internal details in error responses
- [ ] OWASP A07: JWT/token validation correct

### Test Coverage
- [ ] All BR-NNN rules covered by named unit tests
- [ ] AAA pattern used (Arrange / Act / Assert)
- [ ] Tests are independent (no shared mutable state)
- [ ] Testcontainers used for DB tests (not H2)
- [ ] Edge cases covered (boundary values, null inputs)
- [ ] ≥ 90% branch coverage on domain and application layers

### Observability
- [ ] Structured logging (not string concatenation) in all service methods
- [ ] No PII in any log statement
- [ ] Business operation outcomes logged at INFO
- [ ] Errors logged at ERROR with correlation ID
- [ ] Health check covers new dependencies

---

## Findings (Reviewer completes)

### BLOCKERs — must be fixed before merge

| ID | File:Line | Description | Required Fix |
|---|---|---|---|
| B-001 | | | |

### MAJORs — should be fixed before merge

| ID | File:Line | Description | Suggested Fix |
|---|---|---|---|
| M-001 | | | |

### MINORs — track but don't block

| ID | File:Line | Description |
|---|---|---|
| Mi-001 | | |

---

## Business Rule Coverage Summary

| Business Rule | Implemented? | Unit Test? | Test Named? |
|---|---|---|---|
| BR-NNN | Yes / No | Yes / No | Yes / No |

---

## Review Decision

- [ ] **APPROVED** — No blockers, no majors
- [ ] **APPROVED WITH MINOR FINDINGS** — Minor issues tracked; merge allowed
- [ ] **CHANGES REQUIRED** — BLOCKERs or MAJORs must be resolved

**Notes:**
