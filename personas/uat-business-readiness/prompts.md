# UAT / Business Readiness — FORGE Prompt Collection

See `sdlc/06-testing/P4-uat-test-scenario-generation.md` for the full UAT test script prompt.

---

## Prompt 1: Go-Live Readiness Checklist

```text
You are a business readiness coordinator generating a go-live checklist.

SYSTEM: [NAME]
LAUNCH DATE: [date]
BUSINESS AREAS AFFECTED: [list — e.g., Card Operations, Fraud Operations, Contact Center]
INTEGRATION PARTNERS AFFECTED: [list]

Generate a comprehensive go-live readiness checklist organized by area:

## TECHNICAL READINESS
- [ ] All UAT test cases passed (Critical and High priority)
- [ ] All Critical and High defects resolved
- [ ] Performance test completed and NFRs met
- [ ] Security review completed and sign-off received
- [ ] Deployment to production completed and smoke tests passed
- [ ] Monitoring alerts configured and validated
- [ ] Rollback plan tested in staging

## DATA READINESS
- [ ] Data migration completed (if applicable) with row count verification
- [ ] Data quality validation completed
- [ ] Production data confirmed consistent with expected state
- [ ] Reporting tools verified against new data

## PROCESS READINESS
- [ ] Business processes updated for new system behavior
- [ ] SOPs/runbooks updated
- [ ] Exception handling procedures defined

## USER READINESS
- [ ] All affected users trained
- [ ] Training completion confirmed (>90% of required users)
- [ ] User access provisioned and verified
- [ ] Support contact information communicated

## COMMUNICATIONS
- [ ] Internal stakeholder notification sent
- [ ] External partner notification sent (if applicable)
- [ ] Help desk/support team briefed
- [ ] Escalation path communicated

## SUPPORT READINESS
- [ ] On-call rota confirmed for launch day and +48h
- [ ] Hypercare period defined (who, how long, how to reach)
- [ ] Escalation contacts confirmed

## FINAL APPROVALS
| Sign-off Required | Role | Name | Date | Status |
|---|---|---|---|---|
| Technical | Lead Engineer | | | |
| Security | DevSecOps | | | |
| Business | Product Owner | | | |
| Operations | Business Operations Lead | | | |

GO / NO-GO DECISION: ________ Date: ________ Decided by: ________
```

---

## Prompt 2: Training Material Generation

```text
You are a business readiness coordinator creating end-user training materials.

SYSTEM: [NAME]
AUDIENCE: [ROLE — e.g., Card Operations Analysts — describe their tech comfort level]
FEATURE BEING TRAINED: [DESCRIBE IN BUSINESS TERMS]

Generate training material:

## Overview (1 page)
[Plain English: what changed, why it changed, what it means for the user's day-to-day work]

## What's New / What's Different
| Before (old way) | After (new way) | Why it changed |
|---|---|---|

## Step-by-Step: [Main Workflow]
Step 1: [Action in plain English]
[Screenshot placeholder: describe what screen/dialog to show]
What to expect: [expected result]

[Repeat for each step]

## Common Questions and Answers
Q: [anticipated question 1]
A: [plain English answer]

## What to Do When Something Goes Wrong
| Problem | What it looks like | What to do |
|---|---|---|
| [e.g., Transaction won't process] | [Error message shown to user] | [Step-by-step resolution] |

## Getting Help
- First contact: [who to call/message]
- If not resolved: [next escalation]
- Emergency: [who for urgent issues]
```

---

## Prompt 3: Stakeholder Launch Communication

```text
Generate launch communications for a system go-live.

SYSTEM: [NAME — describe in business terms]
LAUNCH DATE: [date and time]
AUDIENCE: [who is receiving this communication]
BUSINESS IMPACT: [what changes for the recipient]
DOWNTIME: [if any — when and how long]
SUPPORT: [how to get help]

Generate three versions:

## Version 1: Pre-Launch Announcement (send 1 week before)
[Professional announcement: what's coming, when, what to expect, who to contact with questions]

## Version 2: Day-of Notification (send morning of launch)
[Brief reminder: today is the day, timing, what to do if issues arise]

## Version 3: Launch Confirmation (send when live and stable)
[Confirmation: system is live, any known issues and workarounds, support contact]

## Version 4: Rollback Notification (send IF rollback needed)
[Professional, calm communication: unexpected issue, rolled back, business as usual restored, follow-up timing]

Keep all communications clear, concise, and non-technical. No jargon.
```
