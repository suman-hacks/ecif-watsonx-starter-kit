# P2 — AI-Assisted Code Review

**Stage:** 05 — Development  
**Persona:** Lead Engineer, Developer, DevSecOps  
**Output file:** `reviews/[PR-number]-review.md`

---

## The Prompt

```text
You are a principal engineer and security expert conducting a thorough code review.

CONTEXT FILES ACTIVE
- [ ] .context/ATOM_CHASSIS.md — loaded
- [ ] .context/CORE_SKILLS.md — loaded

CONTEXT
Service/component: [NAME]
Language/Framework: Java 17 / Spring Boot 3.x / ATOM chassis
Business rules this code implements: [PASTE RELEVANT RULE IDs AND DESCRIPTIONS]
NFRs applicable: [latency target, security requirements]

CODE UNDER REVIEW
[PASTE THE CODE — or describe files if using Claude Code with workspace access]

REVIEW DESIGN SPEC
[PASTE THE DESIGN SPEC THIS CODE SHOULD IMPLEMENT]

TASK
Conduct a thorough code review across all dimensions below. 
For EACH finding, use this format:

**[SEVERITY]** `[file]:[line]` — [Category]
> [Description of the issue]
> **Impact:** [what could go wrong]
> **Fix:** [specific corrected code or approach]
> **Business Rule:** [BR-NNN if applicable]

Severity levels:
- **BLOCKER** — Must fix before merge. Security vulnerability, data loss risk, correctness failure, or regulatory violation.
- **MAJOR** — Should fix before merge. Significant quality, performance, or architecture issue.
- **MINOR** — Should fix soon, can merge with tracking. Style, minor performance, missing test coverage.
- **SUGGESTION** — Optional improvement. Readability, future-proofing, best practice.

REVIEW DIMENSIONS

**1. Correctness**
- Does the code implement all business rules in the spec?
- Are there missing edge cases not covered by logic or tests?
- Are boundary conditions handled correctly?
- Does it match the API contract exactly?

**2. Security (OWASP Top 10)**
- Injection: SQL, NoSQL, LDAP, command injection risks?
- Authentication/Authorization: correct enforcement?
- Sensitive data exposure: PII/PAN in logs, responses, error messages?
- Security misconfiguration: insecure defaults, overly permissive?
- Hardcoded credentials or secrets?
- Insecure deserialization?
- Vulnerable dependencies (known CVEs)?
- Missing input validation at boundaries?
- Insecure direct object references (IDOR)?

**3. Performance**
- N+1 database queries in loops?
- Blocking I/O on reactive threads?
- Missing pagination on collection endpoints?
- Unnecessary serialization/deserialization?
- Missing or incorrect caching?
- Large object allocation in hot paths?

**4. ATOM Architecture Conformance**
- Business logic in wrong layer (controller or DTO instead of @AtomService)?
- @Service used instead of @AtomService? @Repository instead of @AtomRepository?
- Controller returning raw DTO instead of ResponseEntity<ApiResponse<T>>?
- @Valid used instead of @AtomValidated on controller input?
- @Autowired field injection instead of constructor injection?
- Domain layer importing Spring, JPA, or Kafka packages?
- Downstream HTTP call missing @CircuitBreaker?
- Fallback method throwing an exception instead of returning safe default?
- Cross-service database access (direct DB call to another service's table)?
- Missing anti-corruption layer for external system or legacy calls?

**5. Testing**
- Missing test for each business rule?
- Tests testing framework behavior instead of business logic?
- Missing boundary condition tests?
- Missing error path tests?
- Test setup so complex it's unclear what's being tested?
- Missing performance/load test for latency-sensitive paths?

**6. Observability**
- Missing correlation ID propagation?
- Missing structured log fields (entityId, operation, outcome)?
- Missing metrics instrumentation?
- Sensitive data in log statements?
- Error logged without sufficient context?

**7. Error Handling**
- Raw framework exceptions leaking to API callers?
- Silent exception swallowing (catch without logging or action)?
- Missing retry logic for transient failures?
- Missing circuit breaker for external calls?
- Timeout not set on external calls?

**8. Operational**
- Missing health check hooks?
- Feature flag support if needed?
- Can this be deployed with zero downtime?
- Can it be rolled back safely?

OUTPUT FORMAT
1. Executive Summary (3-5 sentences: overall quality, key concerns)
2. Blocking Issues (BLOCKER findings — must fix)
3. Major Issues (MAJOR findings — should fix)
4. Minor Issues and Suggestions (MINOR/SUGGESTION)
5. What's Done Well (acknowledge good patterns — this matters for team morale)
6. Testing Coverage Assessment (table: business rule → test exists?)
7. Approval Recommendation: APPROVE / APPROVE WITH CHANGES / REQUEST CHANGES
```
