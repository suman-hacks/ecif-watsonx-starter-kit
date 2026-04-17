# P4 — UAT / Business Readiness Test Scenario Generation

**Stage:** 06 — Testing  
**Persona:** UAT Coordinator, Business Analyst, Product Owner  
**Output file:** `testing/uat/[feature]-uat-scripts.md`

---

## The Prompt

```text
You are a UAT coordinator writing test scripts for business users. 
These scripts are for NON-TECHNICAL stakeholders — no code, no technical terms, no jargon.

CONTEXT
System being tested: [NAME AND PURPOSE IN BUSINESS TERMS]
Business users who will run these tests: [ROLES — e.g., "Card Operations team, Fraud Operations team"]
User stories being validated: [PASTE STORY IDs AND TITLES]
Acceptance criteria: [PASTE GHERKIN ACCEPTANCE CRITERIA]
Business glossary: [PASTE KEY TERMS — e.g., "Authorization = real-time decision to approve or decline a card transaction"]

TASK
Generate UAT test scripts for each user story. For EACH test scenario:

---
## Test Scenario: [TS-NNN] — [Plain English Title]

**User Story:** [US-NNN title]
**Business Area:** [e.g., Card Authorization, Account Management]
**Who runs this test:** [role — e.g., "Card Operations Analyst"]
**Estimated time:** [minutes]
**Priority:** [Critical / High / Medium]

**Purpose (plain English):**
[1-2 sentences explaining what business behavior this test is verifying and WHY it matters]

**Pre-Conditions (what must be true before starting):**
- [ ] [precondition 1 in plain language — e.g., "A test account with card number ending in 1234 exists and is in Active status"]
- [ ] [precondition 2]

**Test Data:**
| Field | Value | Notes |
|---|---|---|
| Account Number | TEST-ACC-001 | Use provided test account only |
| Card Number | Ending in 1234 | Full test card number provided in test data pack |
| Amount | $50.00 | |

**Steps:**
| Step | Action | Expected Result | Pass? |
|---|---|---|---|
| 1 | [Plain English action — e.g., "Open the Card Management screen and search for account TEST-ACC-001"] | [What should appear] | ☐ |
| 2 | [Next action] | [Expected result] | ☐ |
| 3 | [Continue...] | | ☐ |

**Expected Final Outcome:**
[Plain English description of what success looks like — what the user should see]

**What Failure Looks Like:**
[What to look for if the test fails — what the WRONG outcome would be]

**If This Test Fails:**
1. Take a screenshot
2. Note the exact error message or unexpected behavior
3. Record in the defect log with: Test ID, Step that failed, Expected vs Actual result
4. Escalate to: [contact name/role]

**Notes for Tester:**
[Any business context, special instructions, or things to watch for]

---

After all scenarios, produce:

## UAT Test Pack Summary

| Test ID | Title | User Story | Priority | Tester | Status |
|---|---|---|---|---|---|

## Test Data Pack
[Complete list of all test accounts, cards, and data needed — NO real customer data]

## Sign-Off Criteria
The system is ready for go-live when:
- [ ] All Critical and High priority tests pass
- [ ] All defects rated Critical or High are resolved
- [ ] Business owner has reviewed and accepted Medium/Low open defects
- [ ] [Specific business metric is verified — e.g., "Authorization approval rate matches expected baseline within 2%"]

## UAT Sign-Off Record
Tested by: _______________  
Date: _______________  
Result: PASS / CONDITIONAL PASS / FAIL  
Conditions (if conditional): _______________  
Business Owner Approval: _______________  

RULES
- Never use technical language (no HTTP status codes, no class names, no SQL)
- Use the business glossary terms consistently
- Steps must be specific enough that someone unfamiliar with the system can follow them
- Expected results must be observable (what appears on screen, what message shows)
- Include both positive tests (it works) and negative tests (it correctly refuses)
```
