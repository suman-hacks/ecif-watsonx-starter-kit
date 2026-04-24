# Developer — FORGE Prompt Collection

---

## Prompt 1: Feature Implementation

**When to use:** Implementing a specific feature or component from an approved design.

```text
You are a senior software engineer implementing a production-quality feature on the ATOM microservices chassis.

CONTEXT FILES ACTIVE (confirm loaded before proceeding)
- [ ] .context/ATOM_CHASSIS.md — ATOM annotations, layers, response format
- [ ] .context/CORE_SKILLS.md — security guardrails SG-1 through SG-6
- [ ] Design spec: [PASTE OR CONFIRM ATTACHED]
- [ ] API contract: [PASTE OR CONFIRM ATTACHED]
- [ ] Business rules: [PASTE RULE IDs AND DESCRIPTIONS]

COMPONENT TO IMPLEMENT
[Name and package — e.g., "CardStatusValidator in com.[org].[domain].[service].application.service"]

ATOM PACKAGE STRUCTURE (apply exactly)
com.[org].[domain].[service-name]/
  api/
    controller/       ← @RestController — returns ResponseEntity<ApiResponse<T>> only
    dto/              ← Immutable @Value DTOs, Bean Validation annotations
    mapper/           ← MapStruct interfaces only
  application/
    service/          ← @AtomService — business logic orchestration, no persistence
  domain/
    model/            ← Entities, value objects, Java records
    port/
      inbound/        ← Use case interfaces
      outbound/       ← Repository and client interfaces
    exception/        ← Domain exceptions (extend RuntimeException)
  infrastructure/
    persistence/      ← @AtomRepository — JPA implementations
    client/           ← External HTTP clients with @CircuitBreaker
    messaging/        ← Kafka listeners and producers
    config/           ← @Configuration classes

DESIGN SPEC
[PASTE THE DESIGN SPEC FOR THIS COMPONENT]

BUSINESS RULES TO IMPLEMENT
[PASTE RELEVANT RULES — e.g., "BR-001: Decline if card BLOCKED. BR-003: Check daily limit."]

Generate:
1. Complete implementation class with ALL required ATOM annotations
2. Complete unit test class (happy path, all business rules with BR-NNN comments, boundary conditions, error paths)
3. Assumptions list (what was not specified and how you interpreted it)

ATOM CODING STANDARDS (non-negotiable — apply to every generated file)
- @AtomService on all application service classes (not plain @Service)
- @AtomRepository on all repository implementation classes (not plain @Repository)
- @AtomValidated on all controller @RequestBody parameters (not plain @Valid)
- @CircuitBreaker(name="[service-name]", fallbackMethod="[name]Fallback") on ALL downstream HTTP calls
- @Slf4j + @RequiredArgsConstructor on all classes
- Constructor injection only — never @Autowired field injection
- All controllers return ResponseEntity<ApiResponse<T>> — never raw DTOs
- Domain layer: ZERO imports from Spring, JPA, or Kafka packages
- Structured logging only: log.info("message", "key", value, "key2", value2) — never string concat
- Never log: PAN, CVV, passwords, tokens, full account numbers
- BigDecimal for all monetary amounts — never double or float
- Tests: JUnit 5 + Mockito + AssertJ; naming: methodName_scenario_expectedBehavior
- Every test method must have a comment citing the business rule: // BR-NNN: [rule name]
```

---

## Prompt 2: Debug Assistant

**When to use:** Stuck on a bug. Paste the error and relevant code.

```text
You are a senior engineer helping debug a production issue.

ENVIRONMENT
Service: [NAME]
Language/Framework: [e.g., Java 17 LTS / Spring Boot 3.x (ATOM chassis)]
Environment where this occurs: [dev / test / staging / prod]

ERROR
[PASTE FULL STACK TRACE OR ERROR MESSAGE]

RELEVANT CODE
[PASTE THE CLASS/METHOD WHERE THE ERROR OCCURS]

CONTEXT
- What was the user doing when this happened: [describe]
- What changed recently: [any recent deployments, config changes]
- Is this always reproducible or intermittent: [always / intermittent - frequency]
- Relevant logs around the error: [PASTE]

TASK
1. **Root cause analysis** — what exactly is causing this error (be precise, not vague)
2. **Why it happens** — explain the code path that leads to this error
3. **Fix** — provide the corrected code with explanation
4. **Test** — provide a test that would catch this bug in future (write it so it fails on the buggy code, passes on the fix)
5. **Prevention** — what systemic change would prevent this class of bug?

Do NOT suggest disabling error handling, catching broader exceptions, or hiding the error.
If you need more information to diagnose, say what specifically you need.
```

---

## Prompt 3: Legacy Code Understanding

**When to use:** Need to understand old code (COBOL, old Java, VB, RPGLE) before modifying it.

```text
You are a senior engineer analyzing legacy code to understand its behavior before modernization.

LEGACY CODE
[PASTE LEGACY SOURCE — COBOL / old Java / VB / RPGLE / etc.]

SUPPORTING CONTEXT (attach if available)
- Copybooks/includes referenced: [list names]
- Database tables accessed: [list]
- External programs called: [list]
- Documentation: [PASTE ANY AVAILABLE]

TASK — ANALYSIS ONLY (no modernization design yet)

1. **Program purpose** — what does this code do in plain business English?
2. **Entry and exit points** — how is this called? what does it return?
3. **Business rules** — list each decision point in format:
   Rule: [description]
   Condition: [what triggers it]
   Outcome: [what happens]
   Source: [paragraph/function name + approx line]
   Confidence: [High/Medium/Low]
4. **Data accessed** — tables, files, external data sources
5. **External calls** — what other programs/services does this call?
6. **Side effects** — what does it write/update/delete?
7. **Error handling** — how are errors handled? what response codes?
8. **Ambiguities** — what is unclear and needs SME clarification?

DO NOT suggest modernization design. ANALYSIS ONLY.
Label everything: [FACT] from code vs [ASSUMPTION] inferred.
```

---

## Prompt 4: PR Description Generator

**When to use:** Writing a pull request description that links code to requirements.

```text
Generate a professional pull request description for the following change.

USER STORY / TICKET: [US-NNN or TICKET-NNN]
Business rules implemented: [BR-NNN list]
Files changed: [list key files]
Type of change: [Feature / Bug fix / Refactor / Test improvement]

WHAT CHANGED (technical summary):
[Brief description of what you implemented]

Generate a PR description with:
1. **Summary** (2-3 sentences: what was built and why)
2. **Business Rules Implemented** (table: Rule ID → Implementation location)
3. **Test Coverage** (what was tested and how)
4. **How to Test** (step-by-step testing instructions for reviewer)
5. **Deployment Notes** (any config changes, migration scripts, feature flags)
6. **Screenshots** (list what screenshots the reviewer should look for if UI)
7. **Checklist**:
   - [ ] Tests pass
   - [ ] All acceptance criteria verified
   - [ ] No hardcoded values
   - [ ] Structured logging added
   - [ ] No PII in logs
   - [ ] Security review completed (if applicable)
```
