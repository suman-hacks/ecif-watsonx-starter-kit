# T5: POC Option Analysis

**Task:** 5 of 5  
**Output File:** `TX_POC_OPTIONS.md`  
**Who Runs It:** Lead Engineer + Business Analyst (jointly)  
**Time Required:** 10–20 minutes (synthesizes T1–T4)  
**Prerequisite:** ALL of T1–T4 completed and reviewed. TX_ARCH.md, TX_DECISIONS.md, TX_INTEGRATIONS.md, TX_RISK_MAP.md must be available for the AI to reference.

---

## Stage Overview

This task synthesizes the findings from T1–T4 into a set of concrete, scoped POC options for the discovery workshop to evaluate. Each option is grounded in actual codebase findings — not invented use cases — and includes a rough order-of-magnitude effort estimate.

The output document is the primary decision-making artifact for the workshop. The workshop team selects one option (or defines a hybrid) and the selected POC becomes the input to Stage 01 (Discovery).

## Workshop Connection

`TX_POC_OPTIONS.md` is the centrepiece of the workshop's decision session. The facilitator walks the group through each option, uses the comparison matrix to structure the debate, and records the group's decision. After the workshop, the chosen option's scope boundary becomes the input brief for Stage 01 Discovery.

## Tool Guidance

| Tool | Instructions |
|---|---|
| **GitHub Copilot** | Paste T1–T4 summaries directly into the prompt context. Copilot works best with focused context |
| **Claude Code** | Claude can reference T1–T4 documents if they are in the working directory. Mention file names explicitly |
| **watsonx Code Assist** | Open all four T1–T4 output documents before running this prompt |
| **Cursor** | Use `@file` references for each T1–T4 document in Composer mode |

**Preparation step:** Before running this prompt, prepare a 2–3 sentence summary of the workshop's business objective (e.g., "The bank wants to reduce false positive fraud declines by 15% while maintaining the current false negative rate. They are open to ML-based decisioning but must maintain ISO 8583 compatibility and sub-200ms latency.").

---

## The Prompt

Copy everything between the `---PROMPT START---` and `---PROMPT END---` markers.

---PROMPT START---

You are a senior solution architect and technical pre-sales consultant. Using the pre-engagement analysis documents from T1–T4, generate a set of concrete, feasible POC options for a discovery workshop to evaluate. Each option must be grounded in specific findings from T1–T4 — not generic consulting suggestions.

**Business objective of the engagement:** [INSERT: 2–3 sentences describing what the client/stakeholder wants to achieve]

**Key findings summary from T1–T4:**
- T1 ARCH summary: [INSERT: paste the Service/Module Map table and Infrastructure Indicators from TX_ARCH.md]
- T2 DECISIONS summary: [INSERT: paste section 5 "Top 10 Most Complex Decision Paths" from TX_DECISIONS.md]
- T3 INTEGRATIONS summary: [INSERT: paste the Latency Budget table and Stand-In Risk Assessment from TX_INTEGRATIONS.md]
- T4 RISK MAP summary: [INSERT: paste the Risk Heat Map and Top 5 Risks from TX_RISK_MAP.md]

**Constraints provided by the client:**
- Timeline constraint: [INSERT: e.g., "Must show results within 8 weeks"]
- Data availability: [INSERT: e.g., "Production data not available; anonymized sample of 6 months available"]
- Infrastructure constraint: [INSERT: e.g., "Must run on IBM Cloud; no AWS"]
- Team constraint: [INSERT: e.g., "2 engineers available from client side, 2 from vendor side"]
- Integration constraint: [INSERT: e.g., "Cannot modify the mainframe during POC; must integrate via existing MQ interface"]
- Compliance constraint: [INSERT: e.g., "No PII in POC environment"]

**Your task:** Generate between 3 and 5 distinct POC options that address the business objective within the constraints. For each option, produce the full structure below. After all options, produce a comparison matrix and an opinionated recommendation.

---

## POC Option [N]: [Option Name]

*(Repeat this entire structure for each option)*

### Summary
[2–3 sentences describing the POC in plain English. What will be built? What will be demonstrated?]

### Problem It Addresses
Cite specific findings from T1–T4 that this option addresses. Use references like:
- "T2 identified [X decision logic] as hardcoded at [file/line] with no configurable override — this POC would..."
- "T3 found the latency budget is consumed by [dependency] at [Xms timeout] — this POC would..."
- "T4 flagged [component] as HIGH complexity with LOW test coverage — this POC takes a safe approach by..."

Do not describe a generic problem. Connect directly to evidence.

### What the POC Would Demonstrate
List 3–5 concrete, demonstrable outcomes. Each must be:
- Observable (someone can see it happen in a demo)
- Measurable (there is a metric or acceptance criterion)
- Achievable within the stated timeline

Format:
1. **[Outcome Name]:** [Description] — measurable as: [metric and target]
2. ...

### Scope Boundary

**In Scope:**
- [List each component, capability, or integration that IS part of the POC]

**Out of Scope:**
- [List each component, capability, or integration that is explicitly NOT part of the POC]

**Reference Architecture:**
Describe the target architecture for the POC in 1 paragraph. Identify which components from T1 are reused, which are replaced, and which are new.

**Integration Points Required:**
List which integrations from T3 must be functional for this POC (even in simulated form). For each: [integration name] — [real integration or simulation/stub] — [justification].

### Data and System Requirements

Produce a table with columns: **Data/System | What Is Needed | Availability Assessment | Risk Level**

**Availability Assessment options:**
- `AVAILABLE NOW` — exists and accessible
- `NEEDS ANONYMIZATION` — exists but requires PII removal or masking
- `NEEDS EXTRACTION` — exists in system but needs export/ETL work
- `NEEDS GENERATION` — does not exist; synthetic data must be created
- `HARD TO GET` — access is blocked by compliance, contractual, or technical barriers
- `UNKNOWN` — not assessed yet

**Risk Level:** HIGH (POC cannot proceed without it), MEDIUM (workaround possible), LOW (nice to have)

Flag all `HARD TO GET` items prominently as they are common POC killers.

### Risk Assessment

Using the Risk Map from T4, assess this specific POC's risk profile:

**Technical Risks:**
| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|
| [Risk from T4 relevant to this option] | H/M/L | H/M/L | [specific mitigation] |

**Integration Risks:**
| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|

**Data Risks:**
| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|

**Overall POC Risk Level:** HIGH | MEDIUM | LOW  
**Risk Rationale:** [1–2 sentences explaining the overall assessment]

### Evaluation Criteria

Define 2–3 measurable success criteria for this POC. The workshop team will use these to decide if the POC is successful.

Format:
| Criterion | How Measured | Target Value | Minimum Acceptable Value |
|---|---|---|---|
| [e.g., Latency] | [e.g., p99 response time under load test] | [e.g., < 150ms] | [e.g., < 200ms] |
| [e.g., Accuracy] | [e.g., Precision/Recall on test set] | [e.g., Precision > 85%] | [e.g., Precision > 75%] |
| [e.g., Integration] | [e.g., All ISO 8583 response codes correctly mapped in demo] | [e.g., 100% of codes] | [e.g., 100% of codes — no partial credit] |

### Estimated Effort

Produce estimates for three timeline options:

| Timeline | Team Composition | What Is Delivered | What Is Not Delivered | Confidence |
|---|---|---|---|---|
| **2 Weeks** | [e.g., 2 engineers] | [scope] | [exclusions] | HIGH/MEDIUM/LOW |
| **4 Weeks** | [e.g., 3 engineers] | [scope] | [exclusions] | HIGH/MEDIUM/LOW |
| **8 Weeks** | [e.g., 4 engineers] | [scope] | [exclusions] | HIGH/MEDIUM/LOW |

**Team Composition for Recommended Timeline:**
- [Role]: [responsibilities]
- [Role]: [responsibilities]

**Dependencies That Must Be Resolved Before Day 1:**
- [ ] [Dependency 1]
- [ ] [Dependency 2]

---

*(End of individual option structure — repeat for each option)*

---

## Comparison Matrix

After all options are defined, produce a comparison matrix. Rows = options, columns = key evaluation dimensions.

| Dimension | Option 1: [Name] | Option 2: [Name] | Option 3: [Name] | Option 4: [Name] | Option 5: [Name] |
|---|---|---|---|---|---|
| **Business Objective Coverage** | HIGH/MED/LOW | | | | |
| **Technical Risk** | HIGH/MED/LOW | | | | |
| **Data Availability** | HIGH/MED/LOW | | | | |
| **Integration Complexity** | HIGH/MED/LOW | | | | |
| **Effort (2-week sprint)** | Feasible/Stretch/Not feasible | | | | |
| **Effort (4-week sprint)** | Feasible/Stretch/Not feasible | | | | |
| **Effort (8-week sprint)** | Feasible/Stretch/Not feasible | | | | |
| **Demonstrability** (can be shown in a demo) | HIGH/MED/LOW | | | | |
| **Productionizability** (path to production is clear) | HIGH/MED/LOW | | | | |
| **Client Team Enablement** (can client team own it after POC?) | HIGH/MED/LOW | | | | |

---

## Recommendation

**Recommended Option:** [Option N: Name]

**Recommended Timeline:** [2/4/8 weeks]

**Rationale:** [3–5 sentences explaining why this option best addresses the business objective given the constraints, risks, and data availability. Reference specific T1–T4 findings. Acknowledge the primary risk of this choice and why it is manageable.]

**Condition for Recommendation:** [State any precondition that must be true for this recommendation to hold, e.g., "This recommendation assumes production data can be anonymized and made available within Week 1. If that is not achievable, Option 2 is the fallback."]

**What We Are Trading Off:** [Explicitly name what the recommended option does NOT achieve that other options would. Transparency about trade-offs builds trust.]

**Alternative If Recommended Option Is Rejected:** [Option N: Name] — brief rationale for this as second choice.

---

**Output format requirements:**
- Use Markdown with clear heading hierarchy (H2 for each option, H3 for sections within)
- All tables must be properly formatted Markdown tables
- Findings citations must reference specific T1–T4 sections
- Effort estimates must be expressed as ranges, not point estimates
- The recommendation section must be opinionated — do not hedge by saying "it depends"
- Include uncertainty acknowledgments where honest, but always conclude with a clear position

---PROMPT END---

---

## Output Template

Save the AI's output as `TX_POC_OPTIONS.md` in the `pre-engagement/` folder. The file should begin with:

```markdown
# POC Option Analysis
**Project:** [Project Name]
**Date:** [YYYY-MM-DD]
**Prepared By:** [Engineer + BA Names]
**AI Tool Used:** [Tool Name + version]
**Based On:** TX_ARCH.md, TX_DECISIONS.md, TX_INTEGRATIONS.md, TX_RISK_MAP.md
**Workshop Date:** [Date of discovery workshop]
**Review Status:** [ ] Draft | [ ] Reviewed | [ ] Approved for Workshop

---
```

## Completion Checklist

- [ ] Between 3 and 5 POC options are defined
- [ ] Every option cites specific T1–T4 findings (not generic observations)
- [ ] Every option has measurable evaluation criteria
- [ ] Every option has effort estimates for 2/4/8 week timelines
- [ ] Data and system requirements are assessed for availability (no `UNKNOWN` items for critical data)
- [ ] Comparison matrix is complete
- [ ] A clear, unhedged recommendation is made
- [ ] Pre-workshop review completed by: Lead Engineer, Business Analyst, Workshop Facilitator
- [ ] Document shared at least 24 hours before the workshop
- [ ] Workshop facilitator has prepared questions based on the recommendation

## Common Pitfalls

**Options that are too similar.** If all three options are variants of "add ML to decision X," you have not generated genuine alternatives. Options should cover different value dimensions: accuracy improvement, latency reduction, developer productivity, risk reduction, customer experience improvement.

**Effort estimates that ignore Day 0 dependencies.** The most common POC failure is underestimating data acquisition and environment setup time. Ensure the effort estimate explicitly accounts for the "Before Day 1" dependencies and adds at least 20% buffer for environment setup.

**Missing data showstoppers.** If the most compelling option requires production data that cannot be obtained, it is not a real option. Flag data blockers prominently in the comparison matrix and in the recommendation.

**Overly technical evaluation criteria.** Criteria like "model F1 score > 0.82" are meaningless to business stakeholders. Express criteria in business terms: "95% of transactions that should be approved are approved" rather than "precision > 0.95."

**Weak recommendation.** A recommendation that says "Option 2 or 3 depending on data availability" is not a recommendation — it is a non-answer. Make a clear recommendation, state your assumption, and give the contingency explicitly.

**Ignoring the risk map.** If T4 flagged a component as DANGER ZONE, no POC option should include modifying that component unless the option explicitly includes a hardening sprint first. Cross-reference T4 risks with the scope of each option.

---

*Previous Task: T4 — Complexity and Risk Map | Next Stage: 01-Discovery*
