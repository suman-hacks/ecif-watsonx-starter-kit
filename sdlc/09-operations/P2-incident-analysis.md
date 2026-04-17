# P2 — Post-Incident Analysis (PIR / RCA)

**Stage:** 09 — Operations  
**Persona:** SRE, Lead Engineer, DevSecOps  
**Output file:** `operations/incidents/PIR-[YYYY-MM-DD]-[title].md`

---

## The Prompt

```text
You are an SRE conducting a blameless post-incident review.

CONTEXT
Incident ID: [INC-NNN]
Date/Time: [when it started and ended]
Duration: [total impact duration]
Severity: [P1/P2/P3]
Service affected: [NAME]
Impact: [who was affected, what they experienced, scale — e.g., "100% of authorization requests failed for 12 minutes affecting ~15,000 transactions"]

INCIDENT TIMELINE
[PASTE RAW TIMELINE — logs, alerts, slack messages, notes from on-call]

METRICS DURING INCIDENT
[PASTE relevant metrics screenshots or data]

TASK
Produce a blameless post-incident report:

## Incident Summary
[3-5 sentence executive summary: what happened, how long, what was impacted, how resolved]

## Timeline (Reconstructed)
| Time (UTC) | Event | Who | Notes |
|---|---|---|---|
| 14:32 | Alert fired: [ALERT_NAME] | PagerDuty | |
| 14:35 | On-call acknowledged, began investigation | [Name] | |
| ... | | | |
| 15:44 | Service restored to normal | [Name] | |

## Root Cause Analysis (5 Whys)

**The incident was:** [1-sentence description of what failed]

**Why 1:** [Immediate technical cause — e.g., "The database connection pool was exhausted"]
**Why 2:** [Why did that happen? — e.g., "A query was not using the index and taking 30 seconds"]
**Why 3:** [Why? — e.g., "A recent migration dropped an index accidentally"]
**Why 4:** [Why? — e.g., "The migration was not reviewed by the database team"]
**Why 5 (Root Cause):** [The systemic root cause — e.g., "The review process does not require database team sign-off on schema migrations"]

## Contributing Factors
[Other factors that made this worse or harder to detect — not the root cause, but important]

## Impact Assessment
| Dimension | Detail |
|---|---|
| Customer impact | [describe] |
| Financial impact | [estimated if applicable] |
| Data integrity | [any data loss or corruption] |
| SLA breach | [Yes/No — how many nines affected] |
| Regulatory | [any compliance implications] |

## What Went Well
[Genuine acknowledgment of what worked — fast detection, good runbooks, quick escalation]

## What Went Poorly
[Honest assessment — slow detection, unclear runbooks, missing metrics]

## Action Items (MANDATORY)
| Priority | Action | Owner | Due Date | Prevents |
|---|---|---|---|---|
| P0 | [Immediate fix — e.g., "Restore dropped index"] | [Name] | [Date] | Recurrence |
| P1 | [Short-term improvement] | [Name] | [Date] | Similar incidents |
| P2 | [Process improvement] | [Name] | [Date] | Detection gap |

**P0 items block the next deployment until complete.**

## Metrics to Add (observability gaps revealed)
[List monitoring or alerting that would have caught this sooner]

## Process Changes
[Any changes to development, review, or deployment processes]

---
*This is a blameless PIR. The goal is system improvement, not individual accountability.*
*Reviewed by: _______________ Date: _______________*
```
