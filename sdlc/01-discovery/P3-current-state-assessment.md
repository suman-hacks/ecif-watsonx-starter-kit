# P3: Current State Assessment

**Prompt:** 3 of 3  
**Output File:** `DISCOVERY_CURRENT_STATE.md`  
**Who Runs It:** Business Analyst + Lead Engineer (joint synthesis)  
**Time Required:** 15–20 minutes  
**Prerequisite:** P1 interviews completed and notes synthesized; P2 legacy analysis completed and reviewed; Stage 00 T1–T5 available

---

## Stage Overview

The Current State Assessment is the synthesis document — it takes all discovery inputs and produces a unified, stakeholder-ready view of where the system and business process stand today. Unlike P2 (which is a detailed technical inventory), this document is designed to be read by executive stakeholders, business owners, and program sponsors who need a clear picture of the current situation before approving the next phase of work.

The document has three objectives:
1. **Confirm shared understanding** — verify that the discovery team and stakeholders agree on what the system currently does
2. **Quantify technical debt** — make visible the cost and risk of the current state
3. **Point toward future state** — provide enough direction to anchor Stage 02 Requirements without pre-emptively constraining architecture

## Workshop Connection

`DISCOVERY_CURRENT_STATE.md` is the formal handoff artifact from Discovery to Requirements. It becomes the primary context document for:
- Stage 02 P1 (User Story Generation) — the "as-is" behavior descriptions
- Stage 02 P3 (NFR Definition) — the current performance baseline
- Stage 03 architecture decisions — the constraints that must be respected

It is also the document shared with executive sponsors to confirm discovery completion and authorize the Requirements phase.

---

## The Prompt

Copy everything between the `---PROMPT START---` and `---PROMPT END---` markers.

---PROMPT START---

You are a senior business analyst and solution architect producing a current-state assessment for executive and business stakeholder review. Synthesize all discovery inputs into a clear, structured assessment that confirms current understanding and establishes the baseline for requirements and architecture work.

This document must be readable by non-technical stakeholders. Use business language in the executive sections and confine technical detail to the appendix sections.

**Project:** [INSERT project name]  
**Business Objective:** [INSERT: the stated goal of the modernization/enhancement/new build]

**Input documents to synthesize:**

*Pre-engagement analysis summary:*
[INSERT: key points from T1–T5 Stage 00 documents]

*Legacy analysis summary (from P2):*
[INSERT: key points from DISCOVERY_LEGACY_ANALYSIS.md — system inventory, pain points, modernization opportunities]

*Stakeholder interview synthesis (from P1):*
[INSERT: consolidated findings from stakeholder interviews — key quotes, confirmed constraints, pain points, priorities]

*Additional context (meeting notes, documents, data samples):*
[INSERT: any additional context, or write "None"]

Produce a document titled "Current State Assessment: [Project/System Name]" with the following sections:

---

## Executive Summary

Write a 3–4 paragraph executive summary suitable for a program sponsor or C-level stakeholder. Include:
- **Paragraph 1:** What the system does in plain business language (no technical jargon)
- **Paragraph 2:** The most important strengths — what the system does well and must be preserved
- **Paragraph 3:** The most significant gaps and pain points — what is limiting business value or creating risk
- **Paragraph 4:** The strategic direction implied by the discovery findings — the "therefore we should..." conclusion that sets up the requirements phase

The executive summary must be accurate and evidence-based, but written for business readers. Cite interview quotes where they illustrate key points (in quotation marks with the stakeholder's role attributed, e.g., "Head of Operations").

---

## 1. System Purpose and Business Context

### 1a. What the System Does
Describe the system's purpose in business language: what transactions it processes, what decisions it makes, who uses it, and what would happen to the business if it were unavailable for 1 hour / 1 day / 1 week.

### 1b. Business Metrics Baseline
Produce a table of current performance metrics serving as the baseline for measuring improvement. Columns: **Metric | Current Value | Source | Measurement Frequency | Business Target (if stated)**

Include:
- Transaction/request volume (daily, peak)
- Processing success rate / error rate
- End-to-end latency (average and p99)
- Availability / uptime
- Manual intervention rate (what % of transactions require human action)
- False positive / false negative rates (if applicable to decisioning)
- Customer-impacting error rate (if measurable)
- Time-to-market for rule/configuration changes

Mark values not available from codebase or interviews as `[NOT MEASURED — baseline to be established]`.

### 1c. Stakeholder Summary
For each stakeholder type interviewed, summarize their perspective in 2–3 sentences. Include their primary concern and their stated priority. Attribute with role only (not name) unless the team has confirmed it is appropriate to name individuals.

---

## 2. Technical Debt Quantification

This section makes technical debt visible and, where possible, quantifies its business impact.

### 2a. Technical Debt Inventory

Produce a structured inventory of technical debt. For each item:

| Debt Item | Category | Severity | Business Impact | Estimated Remediation Effort | Risk of Deferring |
|---|---|---|---|---|---|
| [Specific debt item] | [Category] | HIGH/MED/LOW | [Business terms] | T-shirt: S/M/L/XL | [What happens if not addressed] |

**Category options:** Architecture Debt, Code Quality Debt, Test Coverage Debt, Documentation Debt, Dependency/Upgrade Debt, Security Debt, Observability Debt, Data Quality Debt, Process Debt (manual workarounds)

### 2b. Technical Debt Business Cost

For each HIGH-severity debt item, estimate its ongoing business cost:
- Developer productivity impact (hours/week lost to working around this)
- Incident frequency and MTTR impact (how often does this cause problems and how long to fix)
- Risk exposure (what is the potential business impact of a failure caused by this debt)
- Opportunity cost (what features cannot be built while this debt exists)

### 2c. Debt Age and Trend
Based on git history and code comments, is the debt growing, stable, or being reduced? Provide evidence.

---

## 3. Capability Gap Analysis

Identify the gaps between what the system can do today and what the business needs it to do.

### 3a. Capability Map

Produce a capability heat map showing current capability maturity:

| Capability | Current Maturity | Business Requirement Level | Gap |
|---|---|---|---|
| [Capability name] | NONE / BASIC / PARTIAL / FULL | CRITICAL / IMPORTANT / NICE-TO-HAVE | HIGH / MED / LOW / NONE |

Include capabilities across: real-time decisioning, fraud detection, customer experience, operational efficiency, regulatory compliance, reporting/analytics, self-service configuration, integration flexibility.

### 3b. Priority Gaps

For the top 5 capability gaps (highest priority), provide detail:

#### Gap: [Capability Name]

**Current State:** [How the capability is partially or not supported today]  
**Required State:** [What the business needs — based on interview findings]  
**Gap Description:** [The specific difference between current and required]  
**Business Impact of Gap:** [Revenue, customer experience, operational, or risk impact]  
**Stakeholder Who Named This:** [Role — attributed from interview]  
**Complexity to Close:** HIGH | MEDIUM | LOW  
**Dependencies:** [What other capabilities or system changes must happen first]

---

## 4. Risk Register

A prioritized list of risks facing the current system and any change initiative.

| Risk | Category | Likelihood | Impact | Current Controls | Risk Owner | Priority |
|---|---|---|---|---|---|---|
| [Risk description] | Technical/Business/Compliance/Operational | H/M/L | H/M/L | [What is in place today] | [Role] | H/M/L |

**Category options:** Technical Stability, Security Vulnerability, Compliance/Regulatory, Data Quality, Operational Dependency, Knowledge Concentration (key person), Vendor Dependency, Change Velocity

For each HIGH-priority risk, provide a full risk entry:

#### Risk: [Name]
**Description:** [Full risk description — what could go wrong, under what conditions]  
**Evidence:** [Why this risk is assessed as real — from code analysis or interviews]  
**Business Consequences:** [What happens to the business if this risk is realized]  
**Current Controls:** [What mitigations exist today]  
**Residual Risk:** [Risk level after current controls are accounted for]  
**Recommended Action:** [What should be done about this risk in the next phase]

---

## 5. Future State Direction

This section does NOT define the solution — it defines the direction implied by the discovery findings. It sets the stage for the requirements phase without pre-empting architecture.

### 5a. Strategic Objectives for the Next Phase

Based on discovery findings, define 3–5 strategic objectives for the modernization or enhancement work. Each objective should be:
- Grounded in a specific discovery finding (pain point, capability gap, or risk)
- Expressed as a business outcome (not a technical approach)
- Measurable in principle (there is a metric that could confirm achievement)

Format:
**Objective [N]:** [Objective Statement]  
**Grounded In:** [Specific finding from discovery]  
**Success Metric Candidate:** [How this could be measured]

### 5b. Constraints and Non-Negotiables

Based on stakeholder interviews and technical analysis, document the constraints that ANY future design must respect:

| Constraint | Type | Source | Rationale |
|---|---|---|---|
| [Constraint] | Technical / Regulatory / Organizational / Commercial | [Interview quote or T1–T4 evidence] | [Why this constraint exists] |

**Type options:** HARD CONSTRAINT (cannot be violated under any circumstances), SOFT CONSTRAINT (can be negotiated with the right justification)

### 5c. Design Principles for Future State

Propose 4–6 design principles for the future state, derived directly from discovery findings. Each principle should be:
- Actionable (it provides real guidance when making design decisions)
- Derived from discovery (cite the finding that motivates it)
- Testable (you can assess whether a design follows this principle or not)

Example format:
**Principle:** "All decisioning parameters shall be configurable without a code deployment."  
**Motivated By:** T2 found 73% of business rules are hardcoded (TX_DECISIONS.md §3); stakeholders cited slow time-to-market as their top pain point.  
**How to Apply:** Any parameter that could plausibly change more than once per year must be externalized to configuration.

---

## 6. Discovery Confidence Assessment

Be transparent about the confidence level of this assessment:

| Section | Confidence | Evidence Quality | Key Uncertainty |
|---|---|---|---|
| System Inventory | HIGH/MED/LOW | Strong/Partial/Weak | [what is uncertain] |
| Business Process Coverage | | | |
| Technical Debt Quantification | | | |
| Capability Gap Analysis | | | |
| Risk Register | | | |
| Future State Direction | | | |

**Overall Assessment Confidence:** HIGH | MEDIUM | LOW  
**Primary Reason for Low Confidence (if applicable):** [What would need to be true to increase confidence]  
**Recommended Additional Discovery:** [Any targeted investigation that would significantly increase confidence]

---

## 7. Recommended Next Steps

Produce a concrete, sequenced action plan for the next 30 days:

| Action | Owner | Target Date | Dependencies | Priority |
|---|---|---|---|---|
| [Action] | [Role] | [Date] | [What must be done first] | HIGH/MED/LOW |

Actions should include:
- Any remaining stakeholder interviews needed
- Technical spikes required to resolve unknowns
- Data samples or access to be arranged
- Architecture Decision Records (ADRs) to draft
- Formal sign-off required before Requirements phase begins

---

**Output format requirements:**
- Executive Summary must use business language — no technical acronyms without explanation
- Tables must be properly formatted Markdown tables
- Section 5 (Future State Direction) must not specify implementation approaches — that is for Architecture
- Direct quotes from stakeholders must be in quotation marks with role attribution
- Confidence assessment must be honest — under-confidence is preferable to over-confidence

---PROMPT END---

---

## Output Template

Save as `DISCOVERY_CURRENT_STATE.md`. Begin with:

```markdown
# Current State Assessment
**System/Project:** [Name]
**Date:** [YYYY-MM-DD]
**Prepared By:** [BA + Engineer Names]
**Review Status:** [ ] Draft | [ ] Stakeholder Review | [ ] Approved for Requirements Phase
**Approval Required From:** [List stakeholders who must approve before moving to requirements]
**AI Tool Used:** [Tool Name + version]

---
```

## Completion Checklist

- [ ] Executive summary is readable by a non-technical executive
- [ ] Business metrics baseline is populated (or explicitly marked as not measured)
- [ ] Technical debt inventory has at least 8 items with severity ratings
- [ ] Capability gap analysis covers all relevant capability dimensions
- [ ] Risk register has at least 5 risks with full entries for HIGH-priority items
- [ ] Future state direction has 3–5 strategic objectives (none are implementation prescriptions)
- [ ] Constraints are documented as hard vs. soft
- [ ] Confidence assessment is honest about gaps
- [ ] Next steps have owners and dates
- [ ] Document reviewed by: Lead Engineer, Business Analyst, Workshop Facilitator
- [ ] Primary business stakeholder has reviewed and confirmed the accuracy of sections 1–3
- [ ] Formal sign-off obtained before proceeding to Stage 02 Requirements

## Common Pitfalls

**Pre-empting the solution in the Future State section.** "We should use microservices" is architecture, not discovery. "The system must support independent deployment of the decisioning component" is a requirement derived from discovery. Keep the future state direction at the business outcome level.

**Overstating confidence.** It is tempting to present the assessment as complete to satisfy stakeholders. Honest uncertainty documentation builds more trust than false confidence — and prevents the discovery gaps from becoming architecture bugs.

**Skipping the stakeholder review.** This document must be reviewed by the business stakeholder who commissioned the work before moving to requirements. Misalignments caught here are 10x cheaper to fix than in testing.

**Technical debt without business context.** "The service has 800 lines of cyclomatic complexity" means nothing to a business sponsor. "Engineers report that changes to the pricing module typically cause 2–3 unexpected side effects, requiring an average of 3 extra days of testing per release" is actionable.

---

*Previous Prompt: P2 — Legacy System Discovery | Next Stage: 02-Requirements*
