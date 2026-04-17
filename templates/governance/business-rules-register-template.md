# Business Rules Register Template

File as `analysis/business-rules-register.md` in your project.  
Populated by Agent 03 (Rule Extractor) and validated by the SME.  
Required for Gate 4 (Business Rules Approved).

---

# Business Rule Register: [MODULE/DOMAIN NAME]

**Extracted from:** [legacy analysis file(s)]  
**Date:** [date]  
**Extracted by:** [AI tool + version]  
**SME reviewer:** [name]  
**Gate 4 status:** Pending Review | Under Review | Approved

---

## Rule Summary

**Total rules extracted:** [N]  
**High confidence:** [N]  
**Medium confidence:** [N]  
**Low confidence (require SME validation):** [N]

**Categories:**
- [Category 1 — e.g., Card Status Validation]: [N rules]
- [Category 2 — e.g., Limit Management]: [N rules]
- [Category 3 — e.g., Fraud Evaluation]: [N rules]

---

## Rules by Category

### Category: [e.g., Card Status Validation]

---

**BR-001: [Rule Title]**

| Field | Value |
|---|---|
| **Description** | [Plain business language — no COBOL, no field names. E.g., "When a cardholder attempts a transaction, the system checks whether the card is in BLOCKED status. If it is, the authorization is declined regardless of other factors."] |
| **Trigger** | [What initiates this rule — e.g., "Any incoming authorization request"] |
| **Condition** | [What is evaluated — e.g., "Card status = BLOCKED"] |
| **Action (Condition True)** | [What happens — e.g., "Decline the authorization with reason CARD_BLOCKED"] |
| **Action (Condition False)** | [What happens — e.g., "Continue to next validation check"] |
| **Source Reference** | [Legacy analysis file, section, paragraph — e.g., "legacy-behavior-summary-AUTHPROG.md, Decision Points, Rule 3 — EVALUATE-CARD-STATUS paragraph"] |
| **Confidence** | High / Medium / Low |
| **Confidence Basis** | [Why — e.g., "Explicit EVALUATE WHEN CARD-STATUS = 'BLK' in source; confirmed by SME"] |
| **SME Validated** | Yes / No / Pending |
| **Status** | Draft / Pending SME Review / Approved / Excluded |

**Open Questions:**
- None / [Question if confidence is Low or Medium]

---

**BR-002: [Rule Title]**

[Repeat format above for each rule]

---

## Open Questions for SME

List all questions requiring SME input. Must be resolved before Gate 4.

| Q# | Rule ID | Question | Why It Matters | Priority | Status | Answer |
|---|---|---|---|---|---|---|
| Q-001 | BR-008 | [Question text] | [Business impact if wrong] | High | Open | |
| Q-002 | BR-012 | | | Medium | Resolved | [Answer] |

---

## Rules Requiring SME Validation (Confidence: Low)

| Rule ID | Title | Reason for Low Confidence | SME Question |
|---|---|---|---|
| BR-008 | [title] | [e.g., Source references WS-DAILY-LIMIT but the table that populates it was not provided] | Q-001 |

---

## Rules Excluded from Scope

List any code paths analyzed but not extracted as business rules, with justification.

| Code Location | Description | Reason Excluded |
|---|---|---|
| CICS ABEND handler | Operational error handling | Not business logic — operational only |
| [Dead code path] | [description] | Never reached in production (conditional on flag always = FALSE) |

---

## SME Sign-Off Record

| Reviewer | Role | Rules Reviewed | Date | Signature/Approval |
|---|---|---|---|---|
| [name] | [role — e.g., Card Operations SME] | BR-001 to BR-015 | | |
| [name] | [role — e.g., Lead Engineer] | All rules (completeness check) | | |

**Gate 4 Decision:** Approved / Pending / Rejected  
**Approved by:** [name] on [date]
