# User Story Template

Copy this template for each user story. File in `requirements/user-stories/US-NNN-[short-title].md`.

---

# US-[NNN]: [Short Title]

**Epic:** [EP-NNN — Epic name]  
**Created by:** [name / AI-assisted]  
**Date:** [date]  
**Status:** Draft | In Review | Approved | In Sprint | Done

---

## Story

> As a **[role]**,  
> I want to **[action / capability]**,  
> So that **[business outcome / value]**.

---

## Acceptance Criteria

### Scenario 1: [Happy path name]
```
Given [precondition]
When [action]
Then [expected outcome]
And [additional expected outcome if needed]
```

### Scenario 2: [Alternative / Edge case]
```
Given [precondition]
When [action]
Then [expected outcome]
```

### Scenario 3: [Error / Decline path]
```
Given [precondition]
When [action]
Then [expected decline/error outcome]
```

*Add as many scenarios as needed. Minimum: happy path + one error path.*

---

## Business Rules Referenced

| Rule ID | Description |
|---|---|
| BR-NNN | [Short rule description — link to business-rules-register.md] |
| BR-NNN | |

---

## Non-Functional Requirements

| NFR | Target |
|---|---|
| Response time | [e.g., p99 < 200ms] |
| Availability | [e.g., 99.9%] |
| Other | |

---

## Out of Scope (for this story)

- [Explicitly list what is NOT included to prevent scope creep]
- [e.g., Refund flow — covered in US-042]

---

## Dependencies

| Type | Reference | Notes |
|---|---|---|
| API | [Service / endpoint] | [What it provides] |
| Data | [DB table / entity] | [What data is needed] |
| Story | US-NNN | [Must be completed first] |
| External | [System] | [Dependency description] |

---

## Definition of Done (Story-Specific)

In addition to the team-wide DoD:
- [ ] [Any story-specific completion criterion]
- [ ] Business rule BR-NNN tested by name in unit tests
- [ ] Acceptance criteria 1–[N] verified in integration tests

---

## Estimation

**Story Points:** [1 / 2 / 3 / 5 / 8 / 13]  
**T-Shirt Size:** [XS / S / M / L / XL]  
**Notes:** [Any estimation rationale or risk]

---

## Traceability

| Artifact | Reference |
|---|---|
| Epic | EP-NNN |
| Business rule | BR-NNN |
| Architecture component | [Service / component name] |
| Test cases | [TC-NNN list] |
| PR / branch | [link when implemented] |
