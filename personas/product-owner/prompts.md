# FORGE Prompts — Product Owner

These prompts are designed for use with GitHub Copilot Chat, Claude Code, watsonx Code Assist, or Cursor. Copy the prompt text block, replace all `[PLACEHOLDER]` values with your actual content, and paste into your AI tool.

---

## Prompt 1: Epic Decomposition

### Epic Decomposition
**When to use:** When a large feature, initiative, or capability arrives from a PRD or business stakeholder and needs to be broken down into user stories that a development team can estimate and deliver in 1–2 sprints.

**Inputs needed:**
- Epic title and description
- Relevant PRD excerpt or business objective
- Technical context (what system/stack is involved)
- Any known constraints (must use existing API, must not break legacy integration, etc.)
- Target user personas

**Output:** A structured set of user stories derived from the epic, each with a title, user story statement, and initial acceptance criteria. Plus an epic summary card and story map.

**Tool notes:** In GitHub Copilot Chat, prefix with `@workspace` if related code already exists. In Claude Code, paste the PRD section directly. In watsonx Code Assist, use chat mode.

**Prompt:**
```text
You are an experienced Product Owner decomposing a large product epic into sprint-ready user stories.

EPIC INFORMATION:
- Epic Title: [EPIC TITLE]
- Epic Description: [2-5 sentence description of what this epic delivers]
- Business Objective: [What business goal does this epic serve?]
- Target User Personas: [List the user roles who will use this functionality]
- System Context: [What application, service, or platform does this epic belong to?]
- Known Technical Constraints: [Any architectural constraints, integration requirements, or technology mandates]
- Definition of Done for this project: [Paste your DoD or write "standard" if not yet defined]

INSTRUCTIONS:
Decompose this epic into the smallest set of user stories that collectively deliver the full epic scope. Apply the INVEST criteria to every story (Independent, Negotiable, Valuable, Estimable, Small, Testable).

For each user story, produce:

---
**Story [N]: [Story Title]**
**Type:** [Feature / Enabler / Spike / Bug Fix]
**User Story:**
As a [specific user persona],
I want to [specific action or capability],
So that [specific business value or outcome].

**Acceptance Criteria (Given/When/Then):**
- Given [precondition], When [action], Then [observable outcome]
- Given [precondition], When [action], Then [observable outcome]
[Include at least 3 scenarios: happy path, a sad path, and one edge case]

**Out of Scope for this story:**
[Explicitly state what is NOT included to prevent story inflation]

**Dependencies:**
[Other stories, services, or teams this story depends on]

**Estimated Size:** [S / M / L / XL — rough guide only, team will re-estimate]

**Definition of Done Checklist for this story:**
[ ] Unit tests written and passing
[ ] Integration tests written and passing
[ ] Code reviewed and approved
[ ] Acceptance criteria verified by PO
[ ] Documentation updated
[ ] [Add any story-specific DoD items]
---

After all stories, produce:

EPIC SUMMARY CARD:
- Total stories: N
- Suggested delivery sequence (which stories must precede which)
- Stories that can be delivered in parallel
- Minimum viable slice: which subset of stories delivers the smallest working increment of value?
- Recommended sprint allocation at a high level

STORY MAP:
Present a text-based story map:
User Goal → [Epic] → [Story 1] [Story 2] [Story 3] (Release 1 / MVP)
                   → [Story 4] [Story 5] (Release 2)
                   → [Story 6] [Story 7] (Release 3 / Full Feature)

Flag any stories that appear too large (L or XL) and suggest how to split them further.
```

**Review checklist:**
- [ ] Every story is independently deployable (can go to production without requiring another story first)
- [ ] No story is purely technical with no user-visible value (technical enablers are labeled as Enabler type and explained)
- [ ] At least one sad-path acceptance criterion per story
- [ ] Minimum viable slice has been validated with the Product Manager and business stakeholder
- [ ] L and XL stories have been reviewed for further splitting
- [ ] Dependency chain has been reviewed with the Lead Engineer

---

## Prompt 2: User Story Writing

### User Story Writing
**When to use:** Writing a single new user story when you have a clear feature request, business need, or task that needs to go into the backlog. Use for any story that needs complete, quality acceptance criteria before refinement.

**Inputs needed:**
- Feature request or business need description
- User persona
- Any technical context
- Business rules that apply
- Any edge cases or exception scenarios you know about

**Output:** A single, production-quality user story with full acceptance criteria, edge cases, non-functional requirements, and out-of-scope statements.

**Tool notes:** This prompt produces the most value when you paste in the relevant business rule or process description. The richer your input, the fewer rounds of refinement needed.

**Prompt:**
```text
You are an experienced Product Owner writing a production-quality user story for an agile delivery team.

CONTEXT:
- Product / System: [PRODUCT OR SYSTEM NAME]
- Feature Request or Business Need: [DESCRIBE WHAT IS NEEDED IN YOUR OWN WORDS]
- Primary User Persona: [WHO IS THE MAIN USER? e.g., "Loan Officer", "Branch Manager", "API Consumer"]
- Secondary Personas Affected: [ANY OTHER USERS IMPACTED]
- Business Rules That Apply: [PASTE RELEVANT BUSINESS RULES OR POLICIES]
- Technical Context: [RELEVANT ARCHITECTURE — e.g., "REST API consumed by React frontend", "COBOL batch process", "event-driven microservice"]
- Known Edge Cases or Exceptions: [LIST ANY EXCEPTIONS, ERROR CONDITIONS, OR BOUNDARY CASES YOU ARE AWARE OF]
- Related Stories or Epics: [EPIC NAME AND/OR RELATED STORY IDs]

INSTRUCTIONS:
Write a single, complete, INVEST-compliant user story. Apply the following quality standards:
- Use "As a [specific role], I want [specific capability], So that [specific value]" format strictly
- Acceptance criteria must use Given/When/Then (Gherkin-style) format
- Every AC must be observable and testable by a QA engineer without further clarification
- Include BOTH functional AND non-functional acceptance criteria
- Non-functional criteria must be specific (e.g., "response time < 2 seconds at 95th percentile under 500 concurrent users" — not "the system must be fast")

OUTPUT FORMAT:

**Story Title:** [Short, action-verb-first title, e.g., "Submit Loan Application via Mobile App"]

**User Story:**
As a [specific persona],
I want [specific capability],
So that [specific measurable value].

**Background / Context:**
[2-3 sentences of business context that helps a developer understand WHY this exists. Include references to upstream or downstream processes.]

**Functional Acceptance Criteria:**

Scenario 1 — Happy Path: [Descriptive name]
- Given [specific starting state]
- When [specific user action or system event]
- Then [specific, observable, verifiable outcome]
- And [additional outcomes if needed]

Scenario 2 — Sad Path / Error Condition: [Descriptive name]
- Given [precondition that sets up the error]
- When [action that triggers the error]
- Then [expected error behavior — message, state, logging]

Scenario 3 — Edge Case: [Descriptive name]
- Given [boundary or unusual condition]
- When [action]
- Then [expected behavior at the boundary]

[Add scenarios 4-6 if needed for additional business rules]

**Non-Functional Acceptance Criteria:**
- Performance: [Specific threshold — response time, throughput, etc.]
- Security: [Authentication requirement, authorization rule, data masking requirement]
- Accessibility: [WCAG level if UI story]
- Audit/Logging: [What events must be logged and at what level]
- Data Retention: [If data is created or modified]

**Out of Scope for this story:**
[Numbered list — explicitly call out the 3-5 most likely scope creep attempts]

**Definition of Done (story-specific additions):**
[Any DoD items beyond the team standard that apply specifically to this story]

**Open Questions:**
[Number each question. These must be resolved before the story can be accepted into a sprint.]
Q1. [Question] — Owner: [Who can answer this?]
Q2. ...

**Dependencies:**
- Blocked by: [Story IDs or services that must be done first]
- Blocks: [Story IDs that cannot start until this is done]
- Coordination needed with: [Other teams or system owners]
```

**Review checklist:**
- [ ] The "So that" clause states a real business value, not a technical outcome
- [ ] Every AC scenario is independently testable without developer explanation
- [ ] Non-functional criteria all have numbers (no qualitative NFRs)
- [ ] Out-of-scope section explicitly names the most likely scope creep vectors
- [ ] All open questions have named owners — follow up before sprint planning
- [ ] Story has been reviewed by at least one developer for technical completeness
- [ ] Story has been reviewed by a business stakeholder for accuracy

---

## Prompt 3: Backlog Refinement

### Backlog Refinement
**When to use:** 1-2 weeks before a sprint planning session, to audit the stories at the top of the backlog and ensure they are sprint-ready. Use this as a quality gate before the team sees the stories.

**Inputs needed:**
- List of stories to review (paste story titles, descriptions, and acceptance criteria)
- Team's Definition of Done
- Any sprint context (what theme/goal is the upcoming sprint targeting?)

**Output:** A story-by-story quality report rating each story against INVEST criteria, flagging issues, and providing specific improvement suggestions.

**Tool notes:** Paste the raw story text from Jira, Azure DevOps, or your backlog tool. Claude Code handles large batches well. For Copilot Chat, review 5-10 stories at a time.

**Prompt:**
```text
You are a senior Product Owner coach reviewing a backlog of user stories for sprint readiness.

CONTEXT:
- Product: [PRODUCT NAME]
- Upcoming Sprint Theme / Goal: [WHAT IS THIS SPRINT TRYING TO ACHIEVE?]
- Team Definition of Done: [PASTE YOUR DOD or write "standard software delivery DoD"]
- Sprint Capacity: [APPROXIMATE STORY POINTS OR NUMBER OF STORIES THE TEAM CAN TAKE]

STORIES TO REVIEW:
[PASTE EACH STORY WITH ITS TITLE, DESCRIPTION, AND ACCEPTANCE CRITERIA]

---
Story 1: [Title]
Description: [...]
Acceptance Criteria: [...]

Story 2: [Title]
...
---

INSTRUCTIONS:
Review each story against the INVEST criteria and our quality standards. For each story, produce a structured analysis.

INVEST CRITERIA DEFINITIONS (apply these strictly):
- Independent: Can be developed and deployed without requiring another story to be done first?
- Negotiable: Is the story a conversation-starter, not a contract? Is there flexibility in how it's implemented?
- Valuable: Does it deliver value to an end user or the business (not just technical work)?
- Estimable: Is there enough detail for the team to estimate it? (Can a developer give a point estimate right now?)
- Small: Can it realistically be completed in one sprint (≤5 days work)?
- Testable: Can a QA engineer write tests without asking for clarification?

FOR EACH STORY OUTPUT:

**[Story Title]**
| INVEST Criteria | Rating | Findings |
|----------------|--------|----------|
| Independent | PASS / FAIL / CONCERN | [Specific finding] |
| Negotiable | PASS / FAIL / CONCERN | |
| Valuable | PASS / FAIL / CONCERN | |
| Estimable | PASS / FAIL / CONCERN | |
| Small | PASS / FAIL / CONCERN | |
| Testable | PASS / FAIL / CONCERN | |

**Overall Rating:** SPRINT READY / NEEDS WORK / NOT READY

**Issues Found:**
1. [Specific issue — be direct, not generic]
2. ...

**Specific Improvements Required:**
For each FAIL or CONCERN item, provide the exact text change or addition needed.

**Suggested Rewrite (if significant changes needed):**
[Rewrite the story or specific sections if the issues are substantial]

---

After reviewing all stories, produce:

BACKLOG HEALTH SUMMARY:
- Sprint Ready: N stories
- Needs Work: N stories (list them)
- Not Ready: N stories (list them — do not include in sprint planning until fixed)

TOP 3 SYSTEMIC ISSUES:
[Issues that appear across multiple stories — indicates a pattern to address in your story-writing process]

PRIORITIZED REFINEMENT ACTIONS:
[Ordered list of what to fix first before the sprint planning session]

SPRINT RECOMMENDATION:
Given the current backlog health and sprint capacity of [X], which stories would you recommend including in the sprint? Which should be deferred?
```

**Review checklist:**
- [ ] All "Not Ready" stories have been either fixed or explicitly deferred with a reason
- [ ] "Needs Work" stories have had the suggested improvements applied
- [ ] Systemic issues have been noted for discussion in the team retrospective
- [ ] Final sprint backlog has been reviewed with the Lead Engineer for technical feasibility
- [ ] Stories accepted for the sprint have complete ACs — no "TBD" items

---

## Prompt 4: Definition of Done

### Definition of Done
**When to use:** Project kickoff, when onboarding a new team, when the team's quality standards need to be refreshed, or when there is disagreement about what "done" means. Create once per project; update at retrospectives.

**Inputs needed:**
- Technology stack (language, framework, cloud platform)
- Team size and structure
- Regulatory or compliance context
- Current quality pain points (optional but improves output)
- Deployment model (CI/CD, manual release, etc.)

**Output:** A comprehensive, team-specific Definition of Done covering code, test, documentation, security, and deployment gates.

**Tool notes:** Works in all tools. Run once, then store in your team wiki and Jira/ADO configuration. Review at each retrospective.

**Prompt:**
```text
You are an agile delivery expert creating a Definition of Done (DoD) for a software delivery team.

TEAM CONTEXT:
- Project Name: [PROJECT NAME]
- Technology Stack: [e.g., "Java 17 / Spring Boot / React / PostgreSQL / AWS EKS"]
- Team Size: [NUMBER AND ROLES, e.g., "6 developers, 2 QA, 1 BA, 1 DevOps"]
- Regulatory / Compliance Context: [e.g., "PCI-DSS Level 1", "SOX", "HIPAA", "no specific regulation"]
- Current Quality Pain Points: [e.g., "bugs found in UAT", "tests not covering edge cases", "deployments break integration"]
- Deployment Model: [e.g., "fully automated CI/CD to AWS", "manual deployment to on-premise", "IBM z/OS batch release"]
- Sprint Length: [1 week / 2 weeks / 3 weeks]

INSTRUCTIONS:
Create a comprehensive Definition of Done that sets a shared quality standard for the team. The DoD must be:
- Specific and binary (each item is either done or not done — no partial credit)
- Achievable within a sprint (if something can't be done in a sprint, it belongs in a release DoD, not story DoD)
- Enforceable — a developer or QA engineer can verify each item without subjective judgment

Organize the DoD across three levels:

LEVEL 1 — STORY DONE (must be true before a story can be accepted by the PO)

Code Quality:
[ ] [Specific item relevant to this team]
[ ] ...

Testing:
[ ] [Specific test coverage requirement with threshold]
[ ] ...

Security:
[ ] [Specific security gate]
[ ] ...

Documentation:
[ ] [Specific documentation requirement]
[ ] ...

Review and Approval:
[ ] [Specific review requirement]
[ ] ...

LEVEL 2 — SPRINT DONE (must be true before the sprint can be called complete)

Integration:
[ ] [Specific integration requirement]
[ ] ...

Regression:
[ ] [Specific regression test requirement]
[ ] ...

Environment:
[ ] [Specific environment requirement]
[ ] ...

LEVEL 3 — RELEASE DONE (must be true before a release to production)

Production Readiness:
[ ] [Specific production readiness check]
[ ] ...

Compliance:
[ ] [Specific compliance verification]
[ ] ...

Observability:
[ ] [Specific monitoring/alerting requirement]
[ ] ...

Documentation and Communication:
[ ] [Release notes, runbook, on-call handover]
[ ] ...

After the DoD, provide:

IMPLEMENTATION GUIDANCE:
- How to use this DoD in sprint planning (who checks what, when)
- How to handle exceptions (what to do when an item legitimately can't be met in a sprint)
- Review schedule: recommend when to revisit this DoD (suggest every 4-6 sprints)

ANTI-PATTERNS TO AVOID:
- List 3-5 common DoD anti-patterns and how to avoid them in this team's context

Tailor all items to the stated technology stack — do not include generic items that don't apply to [STACK].
```

**Review checklist:**
- [ ] Every DoD item is binary (pass/fail) — remove any that require judgment calls
- [ ] DoD has been reviewed by the Lead Engineer for technical accuracy
- [ ] DoD has been reviewed by the QA lead for test coverage items
- [ ] DoD has been reviewed by DevSecOps for security and compliance gates
- [ ] Team has agreed to the DoD in a working session (not just sent over email)
- [ ] DoD is published in team wiki and linked from sprint board
- [ ] Review date is calendared

---

## Prompt 5: Sprint Goal Definition

### Sprint Goal Definition
**When to use:** Preparing for sprint planning when you need to articulate a clear, motivating sprint goal from the stories the team is about to pick up. A good sprint goal gives the team a decision-making anchor when the sprint doesn't go as planned.

**Inputs needed:**
- List of stories being considered for the sprint
- Product goal or OKR the sprint contributes to
- Any constraints or dependencies for the sprint

**Output:** A concise sprint goal statement with a rationale, plus sprint planning talking points.

**Tool notes:** Quick prompt, works in all tools. Best run right before sprint planning after you've selected the candidate stories.

**Prompt:**
```text
You are a Product Owner preparing for a sprint planning session.

SPRINT CONTEXT:
- Sprint Number: [SPRINT NUMBER OR NAME]
- Sprint Dates: [START DATE] to [END DATE]
- Team Capacity: [AVAILABLE STORY POINTS OR DAYS]
- Product Goal / OKR this sprint contributes to: [PRODUCT GOAL]
- Any special constraints or context: [e.g., "Release candidate must be ready by end of sprint", "Team is missing 2 developers this week"]

CANDIDATE STORIES FOR THIS SPRINT:
[LIST EACH STORY TITLE AND A ONE-LINE DESCRIPTION]
1. [Story Title] — [One-line description]
2. ...

INSTRUCTIONS:
Analyze the candidate stories and produce the following.

1. SPRINT GOAL STATEMENT
Write 1-3 sentences that:
- State what the team will achieve by end of sprint (the outcome, not the list of stories)
- Connect to the product goal
- Are specific enough to be measurable — the team can say "yes we achieved this" or "no we didn't"
- Are motivating enough to guide decisions when stories are dropped or changed during the sprint

Apply these quality checks:
- NOT a list of stories ("we will complete stories 1, 2, 3")
- NOT a technical goal ("we will refactor the service layer")
- YES an outcome statement ("customers will be able to submit applications without calling support")
- YES measurable ("the loan officer's daily report generation time will drop from 45 minutes to under 5 minutes")

2. SPRINT GOAL RATIONALE
2-3 sentences explaining why this goal, and why this sprint.

3. STORIES ALIGNED TO GOAL
Map each story to the sprint goal:
| Story | Contribution to Sprint Goal | Priority if sprint is under threat |
|-------|--------------------------|-----------------------------------|
| [Story 1] | [How it contributes] | P1 / P2 / P3 |

4. SPRINT PLANNING TALKING POINTS
Write 4-5 bullet points you can use to open sprint planning and get the team aligned on the goal.

5. SPRINT RISK STATEMENT
Identify the single biggest risk to achieving this sprint goal and how to monitor it.

6. ALTERNATIVE GOALS CONSIDERED
List 2 alternative sprint goal framings you rejected and why.
```

**Review checklist:**
- [ ] Sprint goal can be read to a business stakeholder who doesn't know Jira and they understand it
- [ ] Sprint goal is measurable — the team can declare success or failure objectively
- [ ] All stories in the sprint connect to the goal (stories that don't connect should be questioned)
- [ ] Goal has been shared with the Lead Engineer before sprint planning starts
- [ ] Goal has been approved by the Product Manager or relevant stakeholder

---

## Prompt 6: Story Point Estimation Guide

### Story Point Estimation Guide
**When to use:** Team kickoff, when onboarding new engineers, or when the team's estimation is inconsistent and you need to recalibrate shared understanding. Use as a reference document during planning poker sessions.

**Inputs needed:**
- Technology stack
- Team composition (senior/mid/junior ratio)
- Typical story types for this team (UI, API, batch, integration, etc.)
- Any calibration stories the team has already agreed on (optional)

**Output:** A detailed estimation reference card with point definitions, example stories at each level, and calibration guidance.

**Tool notes:** Generate once, store in team wiki. Review and update after every 5–6 sprints or when velocity is inconsistent.

**Prompt:**
```text
You are an agile delivery expert creating a story point estimation reference guide for a development team.

TEAM CONTEXT:
- Team Name / Project: [NAME]
- Technology Stack: [e.g., "Java Spring Boot REST APIs, React UI, PostgreSQL, deployed on AWS"]
- Team Composition: [e.g., "3 senior engineers, 2 mid-level engineers, 1 junior engineer"]
- Story Types Common to This Team: [e.g., "CRUD API endpoints, UI form screens, batch jobs, external system integrations, data migrations"]
- Fibonacci Scale Being Used: [1, 2, 3, 5, 8, 13, 21 OR 1, 2, 4, 8, 16 OR T-shirt XS/S/M/L/XL]
- Team Velocity Range (if known): [e.g., "40-50 points per sprint" or "unknown — new team"]

INSTRUCTIONS:
Create a story point estimation reference guide for this specific team. Story points represent RELATIVE COMPLEXITY, not time. They account for: code complexity, degree of uncertainty, integration risk, testing effort, and cross-cutting concerns.

For each point value on the scale, provide:

## [Point Value] Points — [Label, e.g., "Trivial" / "Small" / "Medium" / "Large" / "XL" / "Epic — split required"]

**Definition:** [What complexity profile earns this score?]

**Characteristics:**
- Code changes: [typical scope, e.g., "1-2 classes, no new patterns"]
- Testing: [typical test scope]
- Integration: [internal/external integration requirements]
- Uncertainty: [how well understood is this?]
- Risk: [deployment/data risk level]

**Example stories at this level (for this tech stack):**
1. [Concrete example 1 — be specific to the stated stack]
2. [Concrete example 2]
3. [Concrete example 3]

**Common mistakes that inflate this score:**
[What makes teams over-estimate at this level?]

**Common mistakes that deflate this score:**
[What makes teams under-estimate at this level?]

---

After all point values, add:

CALIBRATION STORIES
Suggest 5 "anchor stories" — one at 1, 2, 3, 5, and 8 points — that the team can use as permanent calibration references. These should be simple enough for all team members to understand.

ESTIMATION ANTI-PATTERNS TO AVOID:
1. [Anti-pattern name]: [Description] → [Correction]
2. ...
List at least 5.

PLANNING POKER FACILITATION GUIDE:
- How to run a 60-minute estimation session
- What to do when estimates diverge by more than 2 steps
- When to split vs. when to just accept a large estimate
- How to handle stories estimated as 13+ (should be split)

VELOCITY AND CAPACITY NOTES:
- How to use this guide with the team's velocity range
- How many [X]-point stories fit in a sprint of [team capacity]
```

**Review checklist:**
- [ ] Examples are specific to the stated technology stack (not generic)
- [ ] Team has walked through the reference guide together in a calibration session
- [ ] Anchor stories are agreed upon and stored in the backlog as permanent reference items
- [ ] Anti-patterns list reflects issues the team has actually experienced
- [ ] Guide has been added to team onboarding materials

---

## Prompt 7: Dependency Mapping

### Dependency Mapping
**When to use:** Release planning, PI (Program Increment) planning, or when you have a set of stories that may have ordering constraints, cross-team dependencies, or shared component requirements. Use before committing to a sprint sequence.

**Inputs needed:**
- List of stories with descriptions and acceptance criteria
- Known team or system boundaries
- Target release or delivery date

**Output:** A dependency map identifying blocking relationships, parallel tracks, critical path, and sequencing recommendations.

**Tool notes:** For large sets (20+ stories), Claude Code handles the analysis best. In Copilot Chat, process in batches of 10. Paste the full story descriptions for accuracy.

**Prompt:**
```text
You are an experienced Product Owner and delivery planner mapping dependencies across a set of user stories.

CONTEXT:
- Product / Release: [PRODUCT AND RELEASE NAME]
- Target Delivery Date: [DATE]
- Team Structure: [e.g., "1 team of 8", "3 squads: Platform, UI, Integrations"]
- Systems Involved: [List the systems, APIs, or services that these stories touch]
- Sprint Length: [LENGTH]
- Available Sprints Until Delivery: [NUMBER]

STORIES TO MAP:
[List each story with enough detail to identify dependencies]
Story ID | Story Title | Brief Description | Team/Component
[e.g., S1 | User Login | Authenticate user via LDAP | Platform Team]
[e.g., S2 | Dashboard | Show user profile on landing page | UI Team — requires S1]
...

INSTRUCTIONS:
Analyze the stories and identify all dependency relationships. Be explicit about the TYPE of dependency for each.

DEPENDENCY TYPES:
- Blocking (hard): Story B cannot start until Story A is DONE (deployed, not just coded)
- Technical (soft): Story B is easier/faster if Story A is done first, but could start independently
- Data: Story B requires data created by Story A
- API/Contract: Story B consumes an interface that Story A must publish
- Cross-team: Stories on different teams that need coordination
- External: Story depends on something outside this team's control

OUTPUT:

1. DEPENDENCY TABLE
| Story | Depends On | Dependency Type | Notes |
|-------|-----------|-----------------|-------|
| [S2] | [S1] | Blocking | UI cannot render profile without auth token from S1 |
...

2. DEPENDENCY DIAGRAM (text-based)
Use an ASCII dependency graph:
S1 ──(blocks)──► S2 ──(blocks)──► S5
S3 ──────────────────────────────► S5
S4 ──(data)──► S6

3. CRITICAL PATH
Identify the longest chain of blocking dependencies — this is your critical path.
Critical Path: [S1] → [S2] → [S5] → [S8]
Minimum sprints required: [N] (assuming no delays)

4. PARALLEL TRACKS
Identify stories that can be developed concurrently:
Track A: S1, S2, S5
Track B: S3, S4
Track C: S6, S7

5. SEQUENCING RECOMMENDATION
Recommend the order of stories across sprints:
Sprint 1: [Stories] — Rationale: [why these first]
Sprint 2: [Stories]
...

6. RISK ASSESSMENT
| Dependency | Risk | Impact if delayed | Mitigation |
|-----------|------|------------------|------------|
...

7. EXTERNAL DEPENDENCY TRACKER
| External Dependency | Owner | Required By | Status | Escalation Path |
|---------------------|-------|-------------|--------|-----------------|
...

8. RECOMMENDATIONS
- Stories that should be split to reduce dependency risk
- Stories that should be re-assigned to a different team to reduce cross-team coordination
- Contract-first or API-first approaches recommended to unblock parallel development
- Stories where a mock/stub can enable parallel development before the real dependency is ready
```

**Review checklist:**
- [ ] All external dependencies have named owners and required-by dates
- [ ] Critical path has been reviewed with the Lead Engineer for technical accuracy
- [ ] Cross-team dependencies have been communicated to the other team leads
- [ ] External dependencies have been logged in the project risk register
- [ ] Parallel tracks have been validated for technical feasibility (can they truly run in parallel?)
- [ ] Project Manager has received the dependency map for inclusion in the project plan

---

*FORGE — Framework for Orchestrated AI-Guided Engineering | Product Owner Prompt Pack v1.0*
