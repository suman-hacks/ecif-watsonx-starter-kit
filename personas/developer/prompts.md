# Developer — FORGE Prompt Collection

---

## Prompt 1: Feature Implementation

**When to use:** Implementing a specific feature or component from an approved design.

```text
You are a senior software engineer implementing a production-quality feature.

VERIFIED INPUTS (confirm each exists before proceeding)
- [ ] Design spec: [PASTE OR CONFIRM ATTACHED]
- [ ] API contract: [PASTE OR CONFIRM ATTACHED]
- [ ] Business rules: [PASTE RULE IDs AND DESCRIPTIONS]
- [ ] Coding standards: [Java 21 / Spring Boot 3.3 / hexagonal architecture]

COMPONENT TO IMPLEMENT
[Name and package — e.g., "AuthorizationApplicationService in com.bank.authorization.application"]

DESIGN SPEC
[PASTE THE DESIGN SPEC FOR THIS COMPONENT]

BUSINESS RULES TO IMPLEMENT
[PASTE RELEVANT RULES — e.g., "BR-001: Decline if card BLOCKED. BR-007: Check daily limit."]

Generate:
1. Complete implementation class (all imports, constructor injection, error handling, logging)
2. Complete unit test class (happy path, all error paths, boundary conditions, BR-NNN annotations)
3. Assumptions list (what was not specified and how you interpreted it)

Follow these standards exactly:
- Package: [com.bank.domain.service]/[api|application|domain|adapters|config]
- Logging: SLF4J + MDC with fields: correlationId, [entityId], operation, outcome
- Error handling: catch infrastructure exceptions, wrap in domain exceptions
- Monetary values: BigDecimal only
- Tests: JUnit 5, Mockito, AssertJ; naming: given_X_when_Y_then_Z
- Every test method comment cites the business rule being verified
```

---

## Prompt 2: Debug Assistant

**When to use:** Stuck on a bug. Paste the error and relevant code.

```text
You are a senior engineer helping debug a production issue.

ENVIRONMENT
Service: [NAME]
Language/Framework: [e.g., Java 21 / Spring Boot 3.3]
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
