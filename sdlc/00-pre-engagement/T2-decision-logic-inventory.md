# T2: Decision Logic Inventory

**Task:** 2 of 5  
**Output File:** `TX_DECISIONS.md`  
**Who Runs It:** Lead Engineer or Solution Architect  
**Time Required:** 5–10 minutes  
**Prerequisite:** T1 (TX_ARCH.md) completed. Codebase loaded into AI assistant context.

---

## Stage Overview

This task produces a comprehensive inventory of every business decision embedded in the codebase. It answers: *where does the system decide yes/no/route/flag, what data drives that decision, and how hard would it be to change?*

For AI/ML augmentation projects, this document identifies exactly where a model or rules engine could inject intelligence. For modernization projects, it reveals the rules that must be preserved or re-implemented. For compliance projects, it shows where regulatory logic lives — and whether it is configurable or buried in code.

## Workshop Connection

`TX_DECISIONS.md` drives the "Where Could AI Add Value?" segment of the discovery workshop. Facilitators use it to:
- Identify which decisions are currently rigid rules that could benefit from learned models
- Estimate the complexity of extracting business rules for migration or enhancement
- Assess compliance and audit risks from hardcoded logic

## Tool Guidance

| Tool | Instructions |
|---|---|
| **GitHub Copilot** | Use `@workspace` context. If the codebase is large, also open the key decision files identified in T1 before running |
| **Claude Code** | Paste prompt directly. For very large codebases, run in two passes: first enumerate all decision locations, then detail each |
| **watsonx Code Assist** | Open the files identified in T1 as "Decision Logic Locations" before running this prompt |
| **Cursor** | Use `@codebase` in Composer mode. You can also paste the T1 decision logic table into the prompt for context |

**Important:** Decision logic buried in database stored procedures, Excel spreadsheets, or mainframe COBOL will not be visible to the AI. Document those as `[EXTERNAL DECISION LOGIC — location: ___]`.

---

## The Prompt

Copy everything between the `---PROMPT START---` and `---PROMPT END---` markers.

---PROMPT START---

You are a senior software architect performing a pre-engagement decision logic inventory. Analyze this codebase and produce an exhaustive inventory of all business decision logic. Every claim must be grounded in specific code evidence: cite file paths, class names, method names, and line ranges where relevant.

**Project context:** [INSERT: same description used in T1]

**Known decision locations from T1 analysis:** [INSERT: paste the Decision Logic Locations table from TX_ARCH.md, or write "Unknown — discover from scratch"]

Produce a document titled "Decision Logic Inventory" with the following sections:

---

## 1. Decision Categories

Group all decisions found in the codebase into the following categories. For each category, list every rule/decision found using a table with columns: **Rule Name | Location (file/class/method) | Trigger Condition | Output/Effect | Hardcoded or Configurable?**

### 1a. Amount and Limit Rules
Rules governing monetary amounts, credit limits, velocity limits, and transaction value thresholds.
- Examples: minimum/maximum transaction amount, daily/weekly spending limits, credit limit checks, batch size limits

### 1b. Fraud and Risk Rules
Rules that flag, score, hold, or decline based on risk signals.
- Examples: velocity checks (too many transactions in window X), device fingerprint rules, geolocation anomalies, unusual amount patterns, blacklisted merchants/bins, high-risk country rules

### 1c. Account and Customer Status Rules
Rules based on the state of the account, card, or customer record.
- Examples: account active/frozen/closed, card blocked/expired/not yet activated, KYC status, age restrictions, account type eligibility

### 1d. Geographic and Jurisdictional Rules
Rules based on country, region, or network geography.
- Examples: cross-border restrictions, sanctioned country lists, domestic-only cards, regional product availability, MCC-based geographic restrictions

### 1e. Product and Eligibility Rules
Rules based on the product type, tier, or customer segment.
- Examples: product eligibility (premium card required), merchant category restrictions, reward eligibility, feature flags by product tier

### 1f. Regulatory and Compliance Rules
Rules required by regulation, network mandate, or compliance framework.
- Examples: PCI-DSS data handling rules, AML/BSA holds, EMV 3DS requirements, SCA (Strong Customer Authentication), Reg E error resolution logic, NACHA return code handling

### 1g. Network and Routing Rules
Rules governing which network, processor, or path handles a transaction.
- Examples: card network selection (Visa vs MC), acquirer routing, least-cost routing, fallback routing on decline, PIN vs signature routing, contactless vs contact chip routing

### 1h. Time and Schedule Rules
Rules based on time of day, day of week, or calendar events.
- Examples: batch processing windows, settlement cutoff times, holiday calendars, maintenance blackout windows

### 1i. Other Business Rules
Any rules that don't fit the above categories.

---

## 2. Rules Engine Analysis

If a dedicated rules engine platform is present (Drools, IBM ODM, FICO Blaze, Pega Decision, AWS Fico, custom), analyze it:

**Platform:** [name or "None found"]  
**Evidence:** [config files, dependency entries, class names]

If a rules engine is present, produce a table with columns: **Attribute | Value** covering:
- Rules engine platform and version
- Approximate number of rules/decision tables
- How rules are updated (admin UI, file upload, code deploy, API)
- Versioning mechanism for rule changes
- Audit trail for rule changes
- Performance characteristics visible from config
- Separation between technical rules and business rules

If no dedicated rules engine is found, note what mechanism is used instead (if/else chains, strategy pattern, database configuration tables, feature flags, etc.) and evaluate whether a rules engine would be appropriate.

---

## 3. Hardcoded vs. Configurable Analysis

Produce a summary with:

**Hardcoded vs Configurable Ratio (estimated):** ___% hardcoded | ___% configurable

Then provide:

### Hardcoded Examples (cite at least 5, more if present)
For each: file path, line range, the hardcoded value, and the risk of it being wrong or needing change.

Format:
```
File: src/main/java/com/example/LimitChecker.java, Line 47
Value: if (amount > 5000.00)
Risk: Amount limit appears hardcoded in USD. If currency or business rules change, requires code deploy and full regression.
```

### Configurable Examples (cite at least 3 if present)
For each: config key, where it's loaded from (env var, properties file, database, Vault, etc.), and how it flows through the code.

### Changeable Without Deploy
List every decision or rule that can be changed in production without a code deploy. For each, note: configuration mechanism, who has access, audit trail quality.

### Must-Deploy-to-Change
List every decision or rule that requires a code change + deployment to modify. Flag any that are likely to need frequent changes based on business context (e.g., fraud thresholds, regulatory limits).

---

## 4. Response Code Mapping

Analyze how internal decisions are translated into external response codes. Produce a table with columns: **Internal Decision/State | Response Code | Code Standard | Downstream Effect | Configurable?**

Cover:
- Authorization approval codes
- Decline reason codes (and whether they are specific or generic to the caller)
- Error codes for system failures vs. business declines
- Partial approval handling (if present)
- ISO 8583 field 39 (response code) mapping if applicable
- ISO 20022 transaction status codes if applicable
- Custom/proprietary response codes used internally
- Any response code that is aggregated (multiple internal reasons map to one external code — note this as a transparency risk)

If the system uses a different standard (NACHA return codes, SWIFT reason codes, REST HTTP status codes with error bodies), document that standard's mapping instead.

---

## 5. Top 10 Most Complex Decision Paths

Identify the 10 most complex decision paths in the codebase based on:
- Number of conditions evaluated
- Number of external data sources consulted
- Number of possible outcomes
- Number of exception/edge cases handled
- Business criticality

For each, produce:

### Decision Path #[N]: [Name]

**Trigger:** [What initiates this decision path]  
**Location:** [Primary file/class/method]  
**Complexity Indicators:**
- Cyclomatic complexity estimate (count of branches): [N]
- External data sources consulted: [list]
- Systems involved: [list]
- Possible outcomes: [list all, with codes if applicable]

**Decision Tree Summary:**
```
IF [condition A]
  AND [condition B]
    → Outcome 1 (code: XXX)
  AND NOT [condition B]
    IF [condition C]
      → Outcome 2 (code: YYY)
    ELSE
      → Outcome 3 (code: ZZZ)
ELSE IF [condition D]
  → Outcome 4 (code: WWW)
[continue...]
```

**Risk Level:** HIGH | MEDIUM | LOW  
**Risk Rationale:** [why this path is risky to change or automate]  
**AI Augmentation Potential:** [could ML/AI improve this decision? what would it need?]

---

## 6. Compliance and Audit Observations

Based on the decision logic found:

**Auditability:** Is every decision logged with enough context to reconstruct it later? (yes/partial/no — with evidence)

**Explainability:** For decline decisions, is the reason code sufficiently specific for regulatory or consumer transparency requirements? (yes/partial/no)

**Consistency:** Are the same rules applied consistently across all transaction paths, or are there divergent implementations of the same rule in different modules? List any inconsistencies found.

**Testability:** Are the most complex decision paths unit-testable in isolation, or are they tightly coupled to infrastructure? (evidence-based assessment)

---

**Output format requirements:**
- Use Markdown with clear heading hierarchy
- All tables must be properly formatted Markdown tables
- Hardcoded examples must include actual file paths and line numbers or ranges
- Decision trees must be in code blocks
- Mark any areas where analysis is incomplete: `[EXTERNAL LOGIC — not in repo]`
- Do not make recommendations — analysis only at this stage

---PROMPT END---

---

## Output Template

Save the AI's output as `TX_DECISIONS.md` in the `pre-engagement/` folder. The file should begin with:

```markdown
# Decision Logic Inventory
**Project:** [Project Name]
**Date:** [YYYY-MM-DD]
**Analyzed By:** [Engineer Name]
**AI Tool Used:** [Tool Name + version]
**Codebase Commit/Branch:** [Git SHA or branch name]
**Related Document:** TX_ARCH.md
**Review Status:** [ ] Draft | [ ] Reviewed | [ ] Approved

---
```

## Completion Checklist

- [ ] All 6 sections are populated with actual findings (not just headers)
- [ ] Decision categories cover all applicable types (mark N/A for types not present)
- [ ] At least 5 hardcoded value examples are cited with file + line evidence
- [ ] Response code mapping is complete (or explicitly noted as `[EXTERNAL — not in repo]`)
- [ ] Top 10 complex paths are documented with decision tree sketches
- [ ] Compliance/audit observations are completed
- [ ] All rules stored outside the codebase (DB, mainframe, external system) are marked
- [ ] Document reviewed by a domain expert who can validate the business logic accuracy
- [ ] Saved and shared with workshop facilitator

## Common Pitfalls

**Missing database-driven rules.** Many systems store rules in database tables (e.g., a `RISK_RULES` table queried at runtime). The AI will see the query code but not the actual rule values. Document the table schema and note that rule data lives in the database, not the code.

**Rules engine content invisible.** If using Drools, IBM ODM, or similar — the AI can see the engine integration code but not the rules themselves (which live in .drl files, decision tables, or an external repository). Explicitly note this and arrange access to the rules repository separately.

**Conflating configuration with code.** Feature flags and environment-specific configs are "configurable" but still require access to modify. Distinguish between rules a business analyst can change (admin UI) vs. rules requiring a DevOps engineer (environment variable, Vault secret).

**Severity conflation in response codes.** Systems often return the same generic decline code for very different internal reasons (fraud vs. insufficient funds vs. system error). This is a compliance and auditability risk — flag it clearly.

**Overlooking time-based rules.** Batch cutoff times, settlement windows, and holiday calendars are business-critical but often buried in cron expressions or scheduler configs. Ensure these are included in section 1h.

---

*Previous Task: T1 — System Architecture Analysis | Next Task: T3 — Integration and Latency Map*
