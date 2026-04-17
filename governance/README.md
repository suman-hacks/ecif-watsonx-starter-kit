# FORGE Governance

Governance ensures AI-assisted development maintains quality, traceability, and compliance throughout the SDLC.

## The Three Governance Pillars

1. **Traceability** — every output traces to its inputs (business requirements → code → tests)
2. **Approval** — human review and sign-off at defined gates before proceeding
3. **Audit** — records of AI-assisted decisions for compliance and retrospective

## Governance Files

| File | Purpose |
|---|---|
| `review-gates.md` | Defines all approval gates with criteria |
| `handoff-contracts/stage-handoffs.md` | What must be produced at each stage transition |
| `audit-trail-template.md` | Template for recording AI-assisted decisions |

## Governance in Practice

**For small teams (1-3 engineers):**
- Combine PM + PO + BA reviews into one person
- Still require technical peer review of all AI-generated code
- Keep the audit trail — even a simple log in a shared doc

**For enterprise programs:**
- Enforce all gates via CI/CD pipeline where possible
- Use the handoff contracts as pipeline artifacts
- Integrate with your existing JIRA/Azure DevOps/ServiceNow workflows

## What Governance Does NOT Mean

- It does not mean slow. Gates should be fast (1-4 hours, not 2-week reviews)
- It does not mean bureaucracy. It means discipline
- It does not mean distrusting AI. It means verifying AI outputs appropriately
