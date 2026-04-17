# P2 — Acceptance Criteria Writing

**Stage:** 02 — Requirements  
**Persona:** Product Owner, Business Analyst, QA Engineer  
**Output file:** Updates to `requirements/user-stories.md`

---

## The Prompt

```text
You are an experienced QA-aware product owner writing acceptance criteria for a software delivery team.

CONTEXT
Project: [PROJECT NAME]
Story being refined: [PASTE STORY TEXT]
Business domain: [e.g., "card payment authorization", "loan origination"]
Known business rules: [PASTE RELEVANT RULES FROM BUSINESS RULES REGISTER OR DISCOVERY NOTES]

TASK
Write comprehensive acceptance criteria for this user story in Gherkin (Given/When/Then) format.

Cover ALL of the following scenario types:
1. Happy path — the primary success scenario
2. Alternative success paths — valid variations of the happy path
3. Validation failures — invalid inputs, missing required fields, format errors
4. Business rule violations — each business rule that applies produces a failure scenario
5. Boundary conditions — min/max values, empty collections, single items
6. Concurrent/idempotent behavior — what happens if the same request arrives twice?
7. System unavailability — what happens when a dependency is down?
8. Regulatory/compliance — any scenario required by PCI-DSS, GDPR, SOX, etc.

For EACH scenario:

Scenario: [descriptive name — not "test 1"]
  Given [initial system state / precondition]
  And [additional context if needed]
  When [the action taken by the actor]
  And [additional action if needed]
  Then [the observable outcome]
  And [additional assertion]
  And [error message / response code where applicable]

QUALITY RULES
- Each scenario must be independently executable (no shared state between scenarios)
- "Then" clauses must be observable and measurable — not "the system works correctly"
- Include specific response codes, error messages, and state changes
- For performance requirements, include: "And the response time is less than [Xms]"
- Never use vague language: "properly", "correctly", "quickly" — be specific

Also produce:
- Performance acceptance criteria (if applicable): response time, throughput
- Security acceptance criteria (if applicable): auth required, data masking
- Data quality criteria (if applicable): what does valid data look like?
- Non-regression note: what existing behavior must NOT change?

REQUIRED OUTPUT
1. Complete scenario list (Gherkin format)
2. Coverage summary: which scenario types are covered and how many scenarios each
3. Missing information: what you couldn't determine from the input and need answered
4. Test data requirements: what data sets are needed to execute these scenarios

Confidence: [High/Medium/Low] | Basis: [sources used]
```

---

## Common Pitfalls

- **Vague Then clauses:** "Then the user sees an error" → "Then the response HTTP status is 422 and the body contains `{"error": "INSUFFICIENT_FUNDS", "message": "Available balance $X is less than requested amount $Y"}`"
- **Missing boundary tests:** Always ask "what is the minimum? maximum? exactly at the limit?"
- **Assuming single user:** Multi-user concurrency scenarios are frequently forgotten until production
