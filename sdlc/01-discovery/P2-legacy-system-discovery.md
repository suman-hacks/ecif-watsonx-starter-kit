# P2: Legacy System Discovery

**Prompt:** 2 of 3  
**Output File:** `DISCOVERY_LEGACY_ANALYSIS.md`  
**Who Runs It:** Lead Engineer (with domain expert participation)  
**Time Required:** 15–30 minutes (iterative — may require multiple passes)  
**Prerequisite:** Stage 00 T1–T4 documents (these are starting points, not replacements); codebase accessible; stakeholder interviews begun

---

## Stage Overview

Legacy system discovery is deeper, more iterative, and more human-in-the-loop than the pre-engagement scan (Stage 00). Where Stage 00 produces a rapid orientation, this prompt drives a thorough, validated analysis suitable for:
- Migration planning documentation
- Requirements input for a rewrite or re-platform
- Architecture decision-making (what to keep, what to replace, what to wrap)
- Compliance evidence for systems being modernized
- Onboarding documentation for new teams

This analysis is run in collaboration with domain experts and system owners, not just from code alone. The AI does the heavy lifting; humans validate and fill gaps.

## Workshop Connection

`DISCOVERY_LEGACY_ANALYSIS.md` feeds into:
- P3 (Current State Assessment) as the technical foundation
- Stage 02 P1 (User Story Generation) as the source of "current behavior" descriptions
- Stage 03 P2 (Service Decomposition) as the bounded context and data ownership baseline
- Stage 04 P2 (Data Model Design) as the legacy schema to migrate from

## Tool Guidance

| Tool | Instructions |
|---|---|
| **GitHub Copilot** | Run in multiple passes: first `@workspace` overview, then targeted `@file` deep dives per module |
| **Claude Code** | Ideal for this task — can cross-reference Stage 00 documents alongside the codebase |
| **watsonx Code Assist** | Open all relevant files; use for code-grounded analysis sections |
| **Cursor** | Use Composer with `@codebase`. Reference Stage 00 documents with `@file` |

**Iterative approach:** This prompt is designed to be run multiple times as new information emerges from stakeholder interviews. Run it first for an initial baseline, then run targeted follow-up passes focused on specific modules or gaps identified in interviews.

---

## The Prompt

Copy everything between the `---PROMPT START---` and `---PROMPT END---` markers.

---PROMPT START---

You are a senior solution architect performing a comprehensive legacy system discovery. This is a thorough, evidence-based analysis that will serve as the authoritative documentation of how the system currently works. Ground every claim in specific code evidence. Where information is incomplete, document the gap with an investigation path.

**Project context:** [INSERT: same description as prior tasks]

**Stage 00 findings summary:** [INSERT: paste key sections from T1–T4, or summarize the most important findings]

**Additional context from stakeholder interviews so far:** [INSERT: key quotes, confirmed facts, and constraints from P1 interviews, or write "Interviews not yet conducted"]

**Focus areas for this analysis pass:**
[INSERT: list specific modules, subsystems, or questions to prioritize. For a first pass, write "Full system — no specific focus"]

Produce a document titled "Legacy System Discovery — [System Name]" with the following sections:

---

## 1. System Inventory

A complete catalog of every component in the legacy system landscape.

### 1a. Application Components

Produce a table with columns: **Component Name | Type | Technology | Version | Purpose | Health Status | Owner Team**

**Type options:** Web Application, API Service, Batch Job, Message Consumer, Scheduled Task, Shared Library, Data Access Layer, Rules Engine, Report Generator, Admin Interface, Integration Adapter

**Health Status (evidence-based):**
- `ACTIVE` — regularly deployed, actively maintained
- `STABLE` — not frequently changed but active and relied upon
- `LEGACY` — old, difficult to change, but still used
- `DEPRECATED` — officially deprecated; migration in progress
- `UNKNOWN` — status unclear from codebase analysis

For each component, note:
- Last significant code change (from git history if available)
- Number of known open defects (from comments, TODO annotations, or ticket references found in code)
- Whether it has a defined owner (team or individual)

### 1b. Data Stores

Produce a table with columns: **Data Store Name | Type | Technology | Version | Data Held | Size Indicator | Backup Strategy Evidence**

**Type options:** Operational Database, Reporting Database, Data Warehouse, Archive, Cache, Message Queue, File System, Mainframe Dataset (VSAM, IMS, DB2)

For each data store:
- What business entities does it contain?
- Who is the authoritative writer?
- Who are the readers?
- Are there known data quality issues? (from comments, validation code, workarounds)
- Is there a data retention policy? (from config, comments, or scheduled archival jobs)

### 1c. Batch and Scheduled Jobs

Produce a table with columns: **Job Name | Schedule | Purpose | Input | Output | Downstream Dependencies | Failure Handling**

For each job:
- What happens if it fails? (auto-retry, manual restart, sends alert, silently fails)
- What happens if it runs late? (downstream impacts)
- Is there a monitoring alert on it?
- When was it last run successfully? (from logs if available, or `[UNKNOWN]`)

### 1d. External System Integrations

Extend the T3 integration map with:
- Contractual SLA (if found in documentation or comments)
- Vendor contact or support process (if documented)
- Integration age and last update
- Known issues or instability indicators
- Version/API version in use

---

## 2. Business Process Coverage

Map every business process the system supports. This section connects system behavior to business outcomes.

### 2a. Process Catalog

For each major business process, produce:

#### [Process Name]

**Business Description:** [1–2 sentences: what business activity this enables, in non-technical language]  
**Trigger:** [What starts this process? User action? Scheduled time? External event? Another process?]  
**End State:** [What is the defined outcome when the process completes successfully?]  
**Frequency:** [How often does this process run? Volume: transactions/hour, files/day, etc.]  
**Business Criticality:** MISSION CRITICAL | HIGH | MEDIUM | LOW  
**Criticality Rationale:** [Why: revenue impact, customer impact, regulatory requirement, or operational dependency]  
**Current Satisfaction Level:** [From interviews: HIGH (works well), MEDIUM (works but has pain), LOW (significant issues), UNKNOWN]

**System Components Involved:** [List components from section 1a]  
**Data Stores Touched:** [List from section 1b]  
**External Integrations Required:** [List from section 1d]  
**Human Touchpoints:** [Where do humans manually intervene in this process? Include: exceptions handled manually, reports reviewed by humans, decisions requiring human approval]

**Known Issues:** [From interviews, comments, bug reports, or observable workarounds in code]  
**Business Rules Embedded:** [Reference specific rules from T2 DECISIONS that govern this process]

### 2b. Process Coverage Gaps

Identify business processes that exist in the organization but are NOT supported (or poorly supported) by the current system. These are modernization opportunities. For each gap: process description, current workaround (spreadsheet? manual? email?), estimated business impact.

---

## 3. Data Flow Diagram

Produce a comprehensive data flow diagram showing how data moves through the system. Use ASCII art within a code block.

```
[DATA FLOW DIAGRAM]

External Source A        External Source B
      |                        |
      | (format: ___)          | (format: ___)
      v                        v
[Inbound Interface Layer]
      |
      |--- [Validation & Parsing]
      |
      v
[Core Processing Layer]
      |
      |--- reads/writes ---> [Primary Database]
      |
      |--- queries --------> [Reference Data Store]
      |
      |--- calls ----------> [External Decision Service]
      |
      v
[Response / Output Layer]
      |
      |--- (sync response) ---> Original Caller
      |
      |--- (async event) -----> [Message Queue/Topic]
      |                               |
      |                               v
      |                         [Downstream Consumer A]
      |                         [Downstream Consumer B]
      |
      |--- (batch output) ----> [File System / SFTP]
                                      |
                                      v
                               [Downstream Batch System]
```

Adapt this template to the actual system topology. Label every arrow with the data format and protocol. Mark components not in this codebase as `[EXTERNAL]` or `[MAINFRAME]`.

---

## 4. Integration Catalog (Extended)

Build on T3 with a comprehensive integration catalog including business context:

For each integration, produce:

#### Integration: [Name]

**Direction:** Inbound | Outbound | Bidirectional  
**Partner System:** [Name and what it does]  
**Protocol and Format:** [e.g., REST/JSON, ISO 8583/TCP, MQ/JMS+XML, SFTP/CSV]  
**Business Purpose:** [Why this integration exists in business terms]  
**Data Exchanged:** [What data flows, including business-significant fields]  
**Frequency / Volume:** [Synchronous on demand | Batch nightly | Real-time stream | etc.]  
**SLA / Latency Requirement:** [From config, documentation, or `[UNKNOWN]`]  
**Error Rate Indicator:** [From monitoring configs, circuit breaker thresholds, retry counts]  
**Integration Age:** [From git history or comments — when was this integration built?]  
**Known Issues:** [From interviews, comments, or workarounds in code]  
**Migration Complexity:** HIGH | MEDIUM | LOW  
**Migration Rationale:** [Why: protocol complexity, data volume, no test environment, regulatory sensitivity]

---

## 5. Pain Point Identification

Synthesize all evidence — code analysis, interview notes, comments, workaround patterns — into a prioritized pain point catalog.

### 5a. Technical Pain Points

For each technical pain point:

#### [Pain Point Name]

**Category:** Performance | Reliability | Maintainability | Scalability | Security | Testability | Observability | Other  
**Evidence Sources:** Code analysis (cite file/pattern), Interview observations, Operational metrics, Bug patterns  
**Business Impact:** [How does this technical issue manifest as a business problem?]  
**Frequency / Severity:** [How often does this cause problems? How severe?]  
**Current Mitigation:** [What manual or automated workaround exists?]  
**Root Cause Hypothesis:** [Technical explanation of why this exists — not blame, just understanding]  
**Effort to Remediate:** HIGH | MEDIUM | LOW (rough estimate)  
**Risk of Not Addressing:** HIGH | MEDIUM | LOW

### 5b. Business Process Pain Points

Pain points experienced by business users, operations staff, or customers (from interviews):

#### [Pain Point Name]

**Who Experiences It:** [Role or user group]  
**Frequency:** [Daily | Weekly | On peak days | Occasionally]  
**Description:** [What happens from the user's perspective]  
**Downstream Effects:** [What problems does this create downstream?]  
**Business Cost Estimate:** [If stakeholders mentioned time lost, revenue impact, or error rate]  
**Technical Root Cause (if known):** [Link to technical pain point above, or `[UNKNOWN]`]

### 5c. Pain Point Priority Matrix

Produce a 2×2 matrix — Business Impact (high/low) vs. Remediation Effort (low/high) — and place each pain point in the appropriate quadrant:

```
                     LOW EFFORT          HIGH EFFORT
HIGH BUSINESS IMPACT | QUICK WINS      | STRATEGIC BETS  |
LOW  BUSINESS IMPACT | NICE TO HAVE    | AVOID           |
```

---

## 6. Modernization Opportunity Assessment

Based on the full analysis, identify opportunities to modernize, automate, or enhance the system:

For each opportunity:

#### Opportunity: [Name]

**Type:** AI/ML Enhancement | Service Decomposition | Technology Upgrade | Process Automation | Integration Modernization | Data Quality | Other  
**Current State:** [Brief description of how it works today and why that is suboptimal]  
**Future State Vision:** [What it would look like with this improvement applied]  
**Grounded In:** [Cite specific findings from T1–T4 and discovery that support this opportunity]  
**Value Drivers:** [What business benefit: reduced cost, improved accuracy, faster processing, better customer experience, reduced risk]  
**Prerequisites:** [What must be true before this opportunity can be pursued]  
**Opportunity Size:** TRANSFORMATIONAL | SIGNIFICANT | INCREMENTAL  
**Risk Level:** HIGH | MEDIUM | LOW  
**Alignment with Stakeholder Priorities:** [Which stakeholder(s) identified this as a priority in interviews]

---

## 7. Discovery Gaps and Unknowns

Produce a tracked list of everything that remains unknown after this discovery pass:

Columns: **Unknown | Category | Why It Matters | Investigation Method | Owner | Priority | Target Resolution Date**

**Category options:** Technical Architecture, Business Logic, Data, Integration, Compliance, Operational, Organizational

For each unknown, specify exactly how it will be resolved (not just "ask the team" — name the specific person, document, or investigation technique).

---

**Output format requirements:**
- Use Markdown with all sections and subsections clearly headed
- Tables must be properly formatted
- Data flow diagram must be in a code block
- Every pain point must have evidence sources cited
- Every modernization opportunity must cite specific T1–T4 findings
- This document must be readable by non-technical stakeholders (avoid jargon in sections 2, 5, and 6)

---PROMPT END---

---

## Output Template

Save as `DISCOVERY_LEGACY_ANALYSIS.md`. Begin with:

```markdown
# Legacy System Discovery
**System Name:** [System Name]
**Project:** [Project Name]
**Date:** [YYYY-MM-DD]
**Lead Analyst:** [Engineer Name]
**Contributing Experts:** [List of domain experts consulted]
**AI Tool Used:** [Tool Name + version]
**Discovery Pass:** [1 of N — update as more passes are run]
**Review Status:** [ ] Draft | [ ] Reviewed by Tech Lead | [ ] Reviewed by Domain Expert | [ ] Approved

---
```

## Completion Checklist

- [ ] System inventory catalogs all application components, data stores, and batch jobs
- [ ] Business process coverage maps each process to system components
- [ ] Data flow diagram is accurate (validated by system owner)
- [ ] Integration catalog extends T3 with business context
- [ ] At least 5 technical and 3 business pain points are documented with evidence
- [ ] Pain point priority matrix is populated
- [ ] At least 3 modernization opportunities are identified and scoped
- [ ] Discovery gaps have owners and resolution dates
- [ ] Document reviewed by: Lead Engineer, Domain Expert, Business Analyst
- [ ] No contradictions remain between code analysis and interview findings

## Common Pitfalls

**Second-pass discovery is skipped.** The first pass always misses things. Schedule a second analysis pass after the first round of stakeholder interviews — new information always reveals gaps in the initial code analysis.

**Pain points without evidence.** "The system is slow" is not a pain point — "authorization responses exceed 500ms on 5% of transactions, causing network declines (evidence: T3 latency budget, confirmed by Head of Operations in interview)" is. Require specific evidence for each pain point.

**Modernization opportunities that are not grounded.** If an opportunity is not connected to a specific T1–T4 finding or interview observation, it is a wish, not a finding. Be disciplined about the "Grounded In" field.

**Forgetting the human touchpoints.** The most valuable discovery findings are often the manual interventions — the analyst who reviews a daily exception report, the operations manager who overrides a decision in a special admin screen. These reveal requirements the code cannot show.

---

*Previous Prompt: P1 — Stakeholder Interview Preparation | Next Prompt: P3 — Current State Assessment*
