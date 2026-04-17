# Project Manager — FORGE Prompt Collection

---

## Prompt 1: Status Report Generation

```text
You are a project manager generating a stakeholder status report.

PROJECT: [NAME]
REPORTING PERIOD: [dates]
AUDIENCE: [e.g., "Executive steering committee — non-technical"]

SPRINT/ITERATION OUTCOMES
Completed: [list completed user stories, features, or milestones]
In progress: [what's currently being worked]
Blocked: [blockers and since when]
Not started (planned): [what was planned but not started]

KEY METRICS
Velocity: [story points or features completed vs planned]
Test coverage: [current %]
Open defects: [Critical/High/Medium/Low counts]
Open risks: [count by severity]

Generate a status report with:

## Executive Summary (3-5 sentences for non-technical audience)
[Overall health: GREEN/AMBER/RED | Why | What's needed]

## Milestone Status
| Milestone | Target Date | Status | Notes |
|---|---|---|---|
| [Requirements complete] | [date] | GREEN | On track |

## Accomplishments This Period
[Bullet list — business outcomes, not technical tasks]

## Planned Next Period
[Bullet list — what will be done]

## Risks and Issues
| ID | Description | Severity | Status | Action | Owner |
|---|---|---|---|---|---|

## Decisions Needed (from steering committee)
| Decision | Context | Options | Recommendation | Deadline |
|---|---|---|---|---|

## Financial Summary (if applicable)
[Budget consumed vs planned, forecast]

RAG Status: [RED / AMBER / GREEN] — [one sentence why]
```

---

## Prompt 2: Risk Register Creation

```text
You are a project manager creating a risk register for a software delivery project.

PROJECT: [NAME AND BRIEF DESCRIPTION]
PHASE: [Discovery / Design / Build / Test / Deploy]
TEAM: [size and composition]
TIMELINE: [start → end dates]
KEY DEPENDENCIES: [external systems, vendors, teams]

Identify risks across these categories and produce a risk register:
- Schedule risks (delays, dependencies, resourcing)
- Technical risks (complexity, integration, legacy)
- Business risks (scope creep, stakeholder alignment, priority changes)
- Compliance/regulatory risks (deadline-driven compliance requirements)
- Vendor/partner risks (third-party dependencies)
- Resource risks (key person dependency, skill gaps)

For EACH risk:
| ID | Category | Risk Description | Likelihood (H/M/L) | Impact (H/M/L) | Risk Score | Mitigation Strategy | Contingency | Owner | Status | Review Date |
|---|---|---|---|---|---|---|---|---|---|---|

Include at least 15 risks. Prioritize by risk score.
Flag any risk that, if it materializes, would require escalation to steering committee.
```

---

## Prompt 3: Steering Committee Presentation Outline

```text
You are a project manager creating a steering committee presentation.

PROJECT: [NAME]
MEETING DATE: [date]
AUDIENCE: [roles of steering committee members]
DURATION: [30 / 45 / 60 minutes]

KEY INFORMATION TO CONVEY
Status: [RAG and summary]
Accomplishments: [list]
Issues/risks needing decision: [list]
Asks from steering committee: [decisions, resources, approvals needed]

Generate a presentation outline with:

## Slide 1: Title and Agenda (1 min)

## Slide 2: Executive Summary (2 min)
[Project health, key message, what you need from them today]

## Slide 3: Progress vs Plan (3 min)
[Timeline visual, milestone completion, what's on track vs at risk]

## Slide 4: Key Accomplishments (3 min)
[Business outcomes delivered this period — not technical tasks]

## Slide 5: Risks and Issues (5 min)
[Top 3-5 risks with status and mitigation — focus on what needs steering committee awareness]

## Slide 6: Decisions Required (5-10 min)
[Each decision as: context, options, recommendation, impact of delay]

## Slide 7: Next Period Plan (3 min)
[Key milestones and deliverables for next period]

## Slide 8: Financials (if applicable) (2 min)

## Slide 9: Q&A / Discussion (remaining time)

For each slide, provide: talking points, key messages (1-2 bullets), data to show.
Keep each slide to 3-5 bullet points maximum. Executives read bullets, not paragraphs.
```

---

## Prompt 4: Project Charter Generation

```text
You are a project manager writing a project charter for a new initiative.

PROJECT INFORMATION
Name: [PROJECT NAME]
Sponsor: [EXECUTIVE SPONSOR]
Project Manager: [NAME]
Start Date: [date]
Target Completion: [date]
Budget: [if known]

OBJECTIVES
[What business problem does this solve? What is the desired outcome?]

CONTEXT
[Why is this being done now? What triggered this project?]

Generate a project charter covering:

1. **Executive Summary** (1 page: problem, solution, business value)
2. **Objectives and Success Criteria** (measurable — not "improve performance" but "reduce authorization latency from 500ms to 150ms p99")
3. **Scope**
   - In scope: [explicit list]
   - Out of scope: [explicit list — prevents scope creep]
4. **Assumptions** (what must be true for this project to succeed)
5. **Constraints** (budget, timeline, technology, regulatory)
6. **Risks** (top 5 risks at project start)
7. **Milestones** (key dates and deliverables)
8. **Team Structure** (roles and responsibilities)
9. **Governance** (how decisions are made, escalation path, meeting cadence)
10. **Budget Overview** (high-level cost estimate if known)
11. **Approvals required** (sign-off section)
```
