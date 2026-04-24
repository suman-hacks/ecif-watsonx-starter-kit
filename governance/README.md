# FORGE Governance

Governance ensures AI-assisted development maintains quality, traceability, and compliance throughout the SDLC.

---

## Files in This Directory

| File | Purpose |
|---|---|
| [review-gates.md](review-gates.md) | Stage-gate checklists — use before promoting AI-generated code |
| [delivery-package-template.md](delivery-package-template.md) | Full delivery package template with traceability matrix — used by `/package-delivery` skill |
| [ai-usage-policy.md](ai-usage-policy.md) | AI code disclosure policy, data classification rules, IP guidance, and audit requirements |
| [audit-trail-template.md](audit-trail-template.md) | Template for logging AI-assisted decisions and approvals |
| [handoff-contracts/stage-handoffs.md](handoff-contracts/stage-handoffs.md) | What each SDLC stage must deliver before the next stage begins |

---

## The Three Governance Pillars

### 1. Review Gates
Every SDLC stage transition requires a human review gate. The Lead Engineer or Tech Lead runs the checklist from [review-gates.md](review-gates.md) before merging or promoting any AI-generated output.

**Non-negotiable gates:**
- Stage 1 → Stage 2: Business rules register validated by BA/domain expert
- Stage 2 → Stage 3: Target design approved by Tech Lead (ADRs written and accepted)
- Stage 3 → Stage 4: Generated code passes /review-code check and all tests pass
- Stage 4 → Merge: All review-gate items satisfied; no CRITICAL findings outstanding

**Claude Code skill:** `/review-code` runs the Stage 4 review automatically.

### 2. Traceability
Every significant AI-generated output (code, ADR, business rule) must be traceable to:
- A source artifact (COBOL file + line number, requirement ID, user story ID)
- A declared assumption (BR-NNN, CBD-NNN, or OQ-NNN entry in assumption-register.md)

If you cannot trace it, it should not exist in the codebase.

### 3. Human Approval Gates
The following categories require human review and **explicit written approval** before the AI proceeds:
- Completed system analysis (Stage 1 output)
- Extracted business rules register
- Target architecture design and API contracts
- Security-related design decisions
- Compliance-related design decisions

**The AI does not self-approve any of these.** Approval must include: approver name, date, and context.

---

## Prompt Boundary Policy

Before starting any AI session, review what data is safe to share:

| Safe to use | Never use |
|---|---|
| Synthetic or anonymized test data | Real customer names, addresses, account numbers |
| Masked card numbers (last 4 only) | Full PAN (card numbers), CVV codes |
| Placeholder credentials | Production passwords, API keys, private keys |
| Architecture diagrams and schemas | Production database connection strings |
| Publicly known domain concepts | Confidential business rules marked Restricted |
| Legacy COBOL source code (internal) | Source code with embedded credentials |

When in doubt: anonymize first, ask your security team second.

---

## AI-Assisted Decision Log

Every significant decision made with AI assistance should be logged in `traceability/decision-log.md`:

```markdown
| Date | Decision | AI Tool Used | Prompt Summary | Human Reviewer | Approved |
|---|---|---|---|---|---|
| 2025-01-15 | Use Strangler Fig for auth modernization | Claude Code | /create-adr | J. Smith | Yes |
```

This log supports: compliance audits, incident investigations, team onboarding, and knowledge retention.
