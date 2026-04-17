# P1 — Code Generation

**Stage:** 05 — Development  
**Persona:** Developer, Lead Engineer  
**Tool notes:** Works with GitHub Copilot Chat, Claude Code, Cursor, watsonx Code Assist

---

## Pre-Generation Checklist

**Do NOT run this prompt until ALL of these are true:**
- [ ] Stage 03 architecture has been reviewed and accepted
- [ ] API contract for this service is finalized
- [ ] Business rules register exists and is approved
- [ ] Coding standards are defined (language, frameworks, package structure)
- [ ] Data model is defined (Stage 04)

If any are missing, go back and complete them first.

---

## The Prompt

```text
You are a senior software engineer implementing a production-quality service following enterprise coding standards.

PRE-GENERATION VERIFICATION
Before generating any code, confirm:
1. Architecture design is available — [YES: attached below / NO: STOP and request it]
2. API contract is defined — [YES: attached below / NO: STOP and request it]  
3. Business rules are documented — [YES: attached below / NO: STOP and request it]

CONTEXT
Service name: [NAME]
Language/Framework: [e.g., Java 21 / Spring Boot 3.3 / Gradle]
Architecture pattern: [e.g., Hexagonal / Clean / Layered]
Package structure:
  [com.company.domain.service]/
    api/          - REST controllers (thin, no business logic)
    application/  - Application services (orchestration)
    domain/       - Business entities, value objects, policies
    adapters/     - Infrastructure: DB, messaging, external services
    config/       - Spring configuration
    observability/ - Metrics, health checks

APPROVED DESIGN
[PASTE SERVICE DESIGN DOCUMENT FROM STAGE 04]

BUSINESS RULES TO IMPLEMENT
[PASTE RELEVANT RULES FROM BUSINESS RULES REGISTER — cite Rule IDs]

API CONTRACT
[PASTE API CONTRACT]

TASK
Generate the complete implementation for [COMPONENT NAME]. Include:

1. **Implementation class** — full class with all imports, constructor injection, business logic, error handling
2. **Unit tests** — comprehensive test class with:
   - Happy path test for each public method
   - Error path test for each exception/error case
   - Boundary condition tests
   - Each test method annotated with the business rule it verifies: `// BR-NNN: [rule name]`
3. **Integration test stub** — skeleton integration test with TODOs for dependencies
4. **Assumptions list** — explicit list of every assumption made where the spec was silent

CODING STANDARDS (apply to all generated code)
- No business logic in @RestController — controllers delegate immediately to application service
- Constructor injection only — no @Autowired field injection
- All exceptions must be caught and converted to domain exceptions — no raw framework exceptions to callers
- Structured logging: every significant operation logs: correlationId (from MDC), entityId, operation, outcome
- BigDecimal for all monetary amounts — never double/float
- Immutable value objects where possible (Java records)
- Test naming: `given_[condition]_when_[action]_then_[outcome]`
- No magic numbers — extract to named constants with business meaning

SECURITY RULES (apply to all generated code)
- Validate all inputs at the API boundary (@Valid, custom validators)
- Never log full card numbers, SSNs, or passwords
- Use constant-time comparison for security tokens (MessageDigest.isEqual)
- Parameterized queries only — no string concatenation in SQL

OBSERVABILITY (apply to all generated code)
- Log at INFO: business events (request received, decision made, response sent)
- Log at WARN: recoverable failures (retry, circuit open, fallback used)
- Log at ERROR: unrecoverable failures with full context
- Emit metrics: [operation]_requests_total, [operation]_errors_total, [operation]_duration_seconds

OUTPUT FORMAT
For each file, show:
```
// FILE: src/main/java/[package]/[ClassName].java
[complete file content]
```

Then:
## Assumptions Made
| # | Assumption | Impact if Wrong | Validation Needed |
|---|---|---|---|

## Open Items (not implemented — needs clarification)
[List anything explicitly left out and why]

Confidence: [High/Medium/Low] | Basis: [design artifacts used]
```

---

## Post-Generation Checklist

- [ ] Code compiles (no import errors, no missing types)
- [ ] Tests cover all acceptance criteria from user stories
- [ ] Tests are not just testing framework behavior (Spring wiring) but YOUR logic
- [ ] All business rules from the register are tested (with BR-NNN references)
- [ ] No hardcoded values that should be configuration
- [ ] No TODO comments left in production code (only in test stubs where intentional)
- [ ] Run static analysis before PR (SonarQube, Checkstyle, SpotBugs)
