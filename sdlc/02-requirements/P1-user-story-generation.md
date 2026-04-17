# P1 — User Story Generation

**Stage:** 02 — Requirements  
**Persona:** Product Owner, Business Analyst  
**Output file:** `requirements/user-stories.md`  
**Workshop connection:** Feeds directly into sprint planning and Stage 03 Architecture

---

## When to Use

Use this prompt when you have:
- Discovery findings (current-state system behavior)
- Business objectives or a problem statement
- Stakeholder interview notes

Do NOT use this prompt to generate stories for a system you haven't analyzed — stories written without understanding the current system frequently miss edge cases, regulatory requirements, and integration constraints.

---

## Inputs Needed

Before running this prompt, have ready:
- Business objective: one sentence stating what success looks like
- User personas: who uses this system (list roles)
- Discovery summary: key capabilities and pain points from Stage 01
- Constraints: regulatory, technical, timeline

---

## The Prompt

```text
You are an experienced product owner and business analyst writing user stories for a software delivery team.

CONTEXT
Business objective: [STATE THE OBJECTIVE — e.g., "Modernize the card authorization engine to reduce decline rates and enable real-time rule updates without code deployments"]
System being built/changed: [NAME AND BRIEF DESCRIPTION]
Primary users: [LIST PERSONAS — e.g., "Card Authorization Service (automated), Fraud Operations team, Card Operations team"]
Key constraints: [LIST — e.g., "Must preserve all existing authorization behavior, PCI-DSS compliant, sub-200ms latency"]

DISCOVERY FINDINGS
[PASTE KEY FINDINGS FROM STAGE 01 — capabilities, pain points, integrations, business rules]

TASK
Produce a structured set of user stories covering:
1. All primary user-facing capabilities identified in the discovery findings
2. Operational capabilities (monitoring, alerting, configuration)
3. Integration requirements (each upstream and downstream system)
4. Error and exception handling flows
5. Compliance and regulatory requirements

For EACH user story, produce:

**Story ID:** US-[NNN]
**Epic:** [group name]
**Title:** [short imperative title]

**Story:**
As a [persona],
I want [goal],
So that [business value].

**Acceptance Criteria:**
Scenario: [name]
  Given [precondition]
  When [action]
  Then [expected outcome]
  And [additional assertion]

[Repeat for each scenario — minimum: happy path, primary error path, boundary condition]

**Dependencies:** [other stories this depends on, or "None"]
**Business Rules Referenced:** [rule IDs from discovery, or "TBD"]
**Open Questions:** [anything that needs clarification]
**Estimated Complexity:** [S/M/L/XL with brief rationale]
**Out of Scope:** [explicit statement of what this story does NOT cover]

CONSTRAINTS
- Stories must be independently testable
- Each story must deliver business value on its own (INVEST criteria)
- Do not combine multiple unrelated capabilities in one story — split them
- Flag any story that appears to be a technical task rather than user value (label it [TECHNICAL ENABLER])
- Flag any story that touches PCI-DSS, GDPR, HIPAA, SOX, or other regulatory requirements

REQUIRED STRUCTURE
1. Epic list (group all stories by epic)
2. Story details (one block per story, in priority order)
3. Gaps and open questions (what couldn't be determined from the input)
4. Suggested story map (visual grouping by user journey)

Confidence: [High/Medium/Low] | Basis: [what sources you drew from]
```

---

## Output Template

Save agent output to: `requirements/user-stories.md`

Structure:
```
# User Stories — [Project Name]
Generated: [date] | Tool: [AI tool used] | Reviewed by: [name]

## Epics
- EP-01: [Epic Name]
- EP-02: [Epic Name]

## Stories
[Full story blocks]

## Gaps and Open Questions
[List]

## Story Map
[Visual grouping]
```

---

## Review Checklist

Before marking this output as approved:
- [ ] Every capability from the discovery findings maps to at least one story
- [ ] No story has only one acceptance criterion (that's usually not enough)
- [ ] All acceptance criteria are testable by QA without requiring business explanation
- [ ] All open questions are captured and assigned to someone
- [ ] Stories touching regulated data are flagged
- [ ] PO has reviewed and can prioritize all stories
- [ ] Engineer has reviewed technical enablers and confirmed feasibility

---

## Common Pitfalls

- **Too broad:** "As a user, I want the system to work" — break this down until each story is estimable
- **Missing error paths:** Every story about creating or processing something needs a story about what happens when it fails
- **Implementation in stories:** "...so that we can use Redis for caching" — the story should state the business value, not the implementation
- **Missing compliance stories:** Regulatory requirements often don't surface until an audit — flag them early
