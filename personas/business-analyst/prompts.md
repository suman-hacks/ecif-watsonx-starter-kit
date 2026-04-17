# FORGE Prompts — Business Analyst

These prompts are designed for use with GitHub Copilot Chat, Claude Code, watsonx Code Assist, or Cursor. Copy the prompt text block, replace all `[PLACEHOLDER]` values with your actual content, and paste into your AI tool.

---

## Prompt 1: Business Rule Extraction

### Business Rule Extraction
**When to use:** When analyzing legacy systems (COBOL, RPG, PL/I, old Java, VB6, etc.), dense policy documents, or SME interview notes to extract business rules in a structured, testable format. Use before any modernization design work begins.

**Inputs needed:**
- Source material: legacy code snippet, policy document excerpt, or SME notes (paste directly)
- Business domain context (what system or process this relates to)
- Any known rule names or IDs already documented

**Output:** A structured business rules catalog with Rule ID, Name, Description, Trigger, Condition, Action, Source reference, and Confidence level.

**Tool notes:** Claude Code handles large code pastes best. For COBOL, paste the full program section including WORKING-STORAGE and PROCEDURE DIVISION. GitHub Copilot with `@workspace` is effective when the legacy code is already in the repo. watsonx Code Assist has strong COBOL comprehension.

**Prompt:**
```text
You are a senior Business Analyst performing business rule extraction from legacy source material.

CONTEXT:
- Business Domain: [e.g., "Commercial Lending", "Claims Processing", "Account Reconciliation", "Payroll"]
- System Name: [LEGACY SYSTEM NAME]
- Source Type: [COBOL Program / RPG Program / Legacy Java / Policy Document / SME Interview Notes / Process Documentation]
- Known Business Context: [DESCRIBE WHAT THIS SYSTEM OR PROCESS IS SUPPOSED TO DO IN 2-3 SENTENCES]

SOURCE MATERIAL (paste below):
[PASTE THE LEGACY CODE, DOCUMENT EXCERPT, OR INTERVIEW NOTES HERE]

INSTRUCTIONS:
Analyze the source material and extract ALL business rules. A business rule is any statement that defines, constrains, or enables a business operation — it is NOT a technical implementation detail (e.g., "use Oracle DB" is not a business rule; "a loan cannot be approved if the applicant has an outstanding delinquency" IS a business rule).

For COBOL/legacy code specifically:
- Look for IF/EVALUATE conditions as decision rules
- Look for COMPUTE statements as calculation rules
- Look for PERFORM loops with conditions as iteration rules
- Look for validation checks before data writes as constraint rules
- Look for special field values (88 levels in COBOL) as classification rules
- Pay attention to WORKING-STORAGE fields that act as flags or accumulators

For each extracted rule, output in this exact format:

---
**Rule ID:** BR-[DOMAIN_ABBREVIATION]-[SEQUENTIAL_NUMBER, e.g., BR-LOAN-001]
**Rule Name:** [Short, human-readable name, e.g., "Minimum Credit Score Threshold"]
**Rule Type:** [Validation / Calculation / Classification / Workflow / Constraint / Derivation]
**Description:** [Plain English description of what the rule does — write for a business stakeholder, not a developer]
**Trigger:** [What event or condition causes this rule to be evaluated? e.g., "When a loan application is submitted", "During end-of-day batch processing"]
**Condition:** [The IF part — what must be true for the rule to apply? State in business terms, not code terms]
**Action:** [The THEN part — what happens when the condition is met? Include the ELSE action if present]
**Calculation (if applicable):** [If a formula is involved, write it in business notation, e.g., "Interest = Principal × Rate × (Days / 365)"]
**Source Reference:** [Line number or section of code/document where this was found]
**Confidence Level:** [HIGH — clearly stated in code/doc | MEDIUM — inferred from logic | LOW — assumed from context]
**SME Validation Required:** [YES / NO — flag any rule where the code logic may not reflect current business intent]
**Notes:** [Any ambiguities, contradictions with other rules, or questions for the SME]
---

After all rules, produce:

RULE SUMMARY TABLE:
| Rule ID | Rule Name | Type | Confidence | SME Validation Needed |
|---------|-----------|------|------------|----------------------|

RULES REQUIRING IMMEDIATE SME CLARIFICATION:
List any rules with LOW confidence or where you detected contradictions or ambiguity.

RULES NOT EXTRACTED (but may exist):
List any business concepts in the code that you could not fully map to a rule due to insufficient context — these are research items for the BA team.

POTENTIAL DEAD CODE / OBSOLETE RULES:
Flag any rules in the source that appear to be unreachable, always-true, or never-triggered — these may be candidates for removal.
```

**Review checklist:**
- [ ] Every LOW confidence rule has been reviewed with a domain SME
- [ ] All "SME Validation Required" rules have been walked through with a subject matter expert
- [ ] Rules marked as "potentially obsolete" have been verified before removing from scope
- [ ] Rule IDs are assigned consistently across all extracted rule sets
- [ ] Rules have been cross-checked against any existing policy documents
- [ ] Business rules catalog has been shared with the development and QA teams
- [ ] QA team has confirmed each rule is testable as written

---

## Prompt 2: Process Flow Documentation

### Process Flow Documentation
**When to use:** When you need to document a business process in a structured, shareable format for use in requirements, design, and testing. Use when a process has been described verbally (in an interview or workshop) or documented in an unstructured format.

**Inputs needed:**
- Process description (can be verbal notes, email descriptions, old documentation, or a rough diagram description)
- Process name and business context
- Actors involved (people, systems, departments)
- Any known exception or error paths

**Output:** A structured BPMN-style process description in text/markdown format, including decision points, swimlanes, exception paths, and process metrics.

**Tool notes:** Works in all tools. Claude Code produces the most complete output for complex processes. If you have a Visio or draw.io description, paste the element list as your input.

**Prompt:**
```text
You are a Business Analyst documenting a business process in a structured, BPMN-inspired format using text and markdown.

PROCESS INFORMATION:
- Process Name: [PROCESS NAME, e.g., "Mortgage Application Review and Approval"]
- Business Domain: [DOMAIN]
- Process Owner: [BUSINESS ROLE WHO OWNS THIS PROCESS]
- Process Trigger: [What starts this process? e.g., "Customer submits application", "End-of-month batch run", "New policy issued"]
- Process End States: [What are the valid terminal states? e.g., "Application Approved", "Application Rejected", "Application Withdrawn"]
- Actors / Participants:
  - Human roles: [LIST EACH ROLE, e.g., "Loan Officer, Underwriter, Branch Manager, Compliance Officer"]
  - Systems: [LIST EACH SYSTEM, e.g., "Loan Origination System, Credit Bureau API, Document Management System"]
  - External parties: [LIST, e.g., "Customer, External Appraiser"]
- Frequency / Volume: [How often does this process run? How many instances per day/month?]
- SLA / Time Constraints: [Any time limits on steps or the overall process]

RAW PROCESS DESCRIPTION:
[PASTE YOUR NOTES, INTERVIEW TRANSCRIPT, OLD DOCUMENTATION, OR STEP LIST HERE]

INSTRUCTIONS:
Transform the raw description into a structured BPMN-style process document. Use text-based swimlane notation.

DOCUMENT STRUCTURE:

PROCESS OVERVIEW
- Purpose: [1-2 sentences — why does this process exist?]
- Scope: [What is included and explicitly excluded from this process]
- Inputs (what enters the process): [List]
- Outputs (what the process produces): [List]
- KPIs / Process Metrics: [How is this process measured? Cycle time, error rate, throughput, etc.]

ACTOR RESPONSIBILITIES
| Actor | Type | Responsibilities in This Process |
|-------|------|----------------------------------|

PROCESS FLOW (SWIMLANE FORMAT)

[ACTOR 1 swimlane]
Step 1.1: [Action name]
- Input: [What is consumed]
- Action: [What happens — be specific]
- Output: [What is produced]
- System used: [If any]
- Duration: [Time estimate if known]

[DECISION POINT — diamond in BPMN]
Decision: [Question that determines flow, e.g., "Is credit score ≥ 720?"]
→ YES path: proceed to Step X.X
→ NO path: proceed to Step Y.Y

[ACTOR 2 swimlane]
Step 2.1: ...

Continue for all steps across all actors.

EXCEPTION PATHS
For each exception:
Exception [N]: [Exception name, e.g., "Incomplete Application"]
- Trigger: [What causes this exception?]
- Handler: [Who/what handles it?]
- Resolution steps: [Numbered list]
- Resolution outcome: [What happens when resolved?]
- Escalation: [What if it cannot be resolved?]

INTEGRATION POINTS
| Step | System | Interaction Type | Data Exchanged | SLA |
|------|--------|-----------------|----------------|-----|

BUSINESS RULES APPLIED IN THIS PROCESS
List all business rules that are applied (use Rule IDs if the BR catalog exists):
| Step | Rule Applied | Rule Description |

PROCESS METRICS AND SLAs
| Metric | Current Baseline | Target | Measurement Point |

KNOWN PAIN POINTS
[List any inefficiencies, bottlenecks, or issues in the CURRENT process that were identified in discovery]

OPEN QUESTIONS
[Number each — assign to an owner for resolution]
```

**Review checklist:**
- [ ] Process has been walked through step-by-step with a process owner or SME
- [ ] All exception paths have been validated — especially rare but critical exceptions
- [ ] Integration points have been validated with the technical team
- [ ] Business rules referenced are in the BR catalog
- [ ] Process metrics have been confirmed as measurable (data exists to track them)
- [ ] Open questions have been assigned and have a target resolution date
- [ ] Process document has been reviewed by the Solution Architect for technical implications

---

## Prompt 3: As-Is to To-Be Mapping

### As-Is to To-Be Mapping
**When to use:** When a modernization or re-engineering initiative requires a clear picture of what changes, what stays the same, and what is being added or removed. Use to scope the transformation and identify change impacts.

**Inputs needed:**
- As-Is process document or description
- To-Be vision or design (from PRD, solution architecture, or business requirements)
- Business context for the transformation

**Output:** A structured transformation map showing what changes, what stays the same, what is removed, and what is added — with business impact assessment for each change.

**Tool notes:** Paste both the As-Is and To-Be descriptions. The AI will produce the gap map. Claude Code handles the most complex scenarios. This prompt works well after running the Process Flow Documentation prompt.

**Prompt:**
```text
You are a Business Analyst producing an As-Is to To-Be transformation map for a business process modernization initiative.

INITIATIVE CONTEXT:
- Initiative Name: [NAME]
- Business Objective: [WHY is this transformation happening?]
- Transformation Scope: [What business functions, systems, or departments are in scope?]
- Out of Scope: [What is explicitly not changing?]

AS-IS PROCESS (Current State):
[PASTE THE CURRENT STATE PROCESS DESCRIPTION, FLOW, OR DOCUMENTATION]

TO-BE PROCESS (Future State):
[PASTE THE FUTURE STATE DESIGN, REQUIREMENTS, OR PROCESS DESCRIPTION]

INSTRUCTIONS:
Produce a comprehensive As-Is to To-Be mapping document.

SECTION 1 — TRANSFORMATION SUMMARY TABLE
| Process Step / Capability | As-Is State | To-Be State | Change Type | Business Impact | Complexity |
|--------------------------|-------------|-------------|-------------|-----------------|------------|
[For each process element]
- Change Types: UNCHANGED | MODIFIED | REMOVED | ADDED | AUTOMATED | SPLIT | MERGED
- Business Impact: HIGH / MEDIUM / LOW (impact on users, revenue, compliance, or operations)
- Complexity: HIGH / MEDIUM / LOW (complexity of the transformation)

SECTION 2 — DETAILED CHANGE ANALYSIS

For each change (MODIFIED, REMOVED, ADDED, AUTOMATED, SPLIT, MERGED):

**Change [N]: [Short name]**
- Change Type: [Type]
- As-Is Description: [How it works today]
- To-Be Description: [How it will work in future]
- Reason for Change: [Why is this change needed?]
- Who Is Affected: [Which users, departments, or systems]
- Transition Requirements: [What must happen to move from As-Is to To-Be? e.g., data migration, training, parallel run]
- Business Rules Affected: [List Rule IDs from the BR catalog]
- Risk of This Change: [What could go wrong during or after transition?]
- Validation Method: [How will we confirm the To-Be state works correctly?]

SECTION 3 — UNCHANGED ELEMENTS
List all process steps and capabilities that are NOT changing. This is important for scoping — the team should not spend time designing what already works.

SECTION 4 — CAPABILITY DELTA TABLE
| Capability | Exists As-Is | Exists To-Be | Action Required |
|-----------|-------------|-------------|----------------|
[Show every capability — even unchanged ones — to confirm full coverage]

SECTION 5 — STAKEHOLDER IMPACT SUMMARY
For each affected user role or department:
| Stakeholder Group | Impact Description | Change Volume (H/M/L) | Training Required | Communication Required |
|------------------|-------------------|----------------------|-------------------|----------------------|

SECTION 6 — DATA TRANSFORMATION REQUIREMENTS
| Data Element | As-Is Format / Source | To-Be Format / Location | Transformation Logic | Migration Required |
|-------------|----------------------|------------------------|---------------------|-------------------|

SECTION 7 — TRANSITION REQUIREMENTS
List everything that must be true BEFORE go-live:
[ ] [Specific transition requirement]
[ ] ...

List everything that must happen DURING cutover:
[ ] ...

List everything that must be true AFTER go-live to confirm success:
[ ] ...

SECTION 8 — RISKS AND DEPENDENCIES
| Risk | Trigger | Business Impact | Mitigation |
|------|---------|----------------|------------|
```

**Review checklist:**
- [ ] Every process step from the As-Is has been accounted for (none silently dropped)
- [ ] All "REMOVED" elements have been validated with the business — confirm they are truly no longer needed
- [ ] "UNCHANGED" elements have been confirmed with SMEs — not assumed
- [ ] Data transformation requirements have been reviewed with the Solution Architect
- [ ] Stakeholder impact table has been reviewed with the change management team
- [ ] Transition requirements are specific and verifiable (no vague items)

---

## Prompt 4: Data Dictionary Creation

### Data Dictionary Creation
**When to use:** When source code, database schemas, flat file layouts, or legacy copybooks need to be translated into business-readable documentation. Use early in discovery to establish shared data language across teams.

**Inputs needed:**
- Source material: SQL DDL, COBOL copybook, JPA entities, flat file layout, JSON schema, or XSD
- Business domain context
- Any known business names for technical fields

**Output:** A business-readable data dictionary with technical and business field information, validation rules, and data lineage.

**Tool notes:** Paste the raw schema or code. Claude Code handles large schemas. For COBOL copybooks, paste the complete 01 level record structure. watsonx Code Assist has strong COBOL and DB2 support for this prompt.

**Prompt:**
```text
You are a Business Analyst creating a business-readable data dictionary from technical source material.

CONTEXT:
- Business Domain: [e.g., "Customer Account Management", "Trade Processing", "Claims Administration"]
- System Name: [SYSTEM NAME]
- Source Type: [SQL DDL / COBOL Copybook / JPA Entity / Flat File Layout / JSON Schema / XSD / API Response]
- Business Context: [DESCRIBE WHAT THIS DATA REPRESENTS IN PLAIN LANGUAGE]

SOURCE MATERIAL (paste below):
[PASTE YOUR SQL DDL, COPYBOOK, ENTITY CLASS, FILE LAYOUT, OR SCHEMA]

INSTRUCTIONS:
Create a comprehensive, business-readable data dictionary from the source material. This document will be used by:
- Business stakeholders to validate that fields meet their needs
- Developers to understand business meaning of technical field names
- QA engineers to create test data
- Data governance teams for lineage and classification

For each field/column/element, produce an entry:

---
**Field ID:** DD-[ENTITY]-[SEQUENTIAL_NUMBER, e.g., DD-ACCT-001]
**Technical Name:** [Exact name from source, e.g., "ACCT_BAL_AMT", "accountBalanceAmount"]
**Business Name:** [Human-readable name, e.g., "Account Balance Amount"]
**Business Definition:** [Plain English: what does this field represent in business terms? Who uses it? In what context?]
**Entity / Table / Record:** [Which table, entity, or record this belongs to]
**Data Type (Technical):** [e.g., "DECIMAL(15,2)", "VARCHAR(50)", "DATE", "PIC 9(7)V99"]
**Data Type (Business):** [e.g., "Currency Amount", "Free text name", "Date", "Numeric count"]
**Length / Precision:** [Field length and decimal places]
**Required:** [Mandatory / Optional / Conditional (if conditional, state the condition)]
**Default Value:** [If any]
**Valid Values / Domain:**
  - If enumerated: list all valid values with business meaning (e.g., "A = Active, I = Inactive, P = Pending")
  - If range: state the valid range (e.g., "0.00 to 999,999,999.99")
  - If format: state the format (e.g., "YYYYMMDD", "ISO 4217 currency code")
**Validation Rules:** [List all business validation rules that apply to this field — reference Rule IDs from BR catalog if available]
**Source System:** [Where does this data originate?]
**Populated By:** [Which process, program, or user populates this field?]
**Used By:** [Which processes, reports, or systems consume this field?]
**PII / Sensitive Data:** [Yes — [classification type, e.g., PII, PCI, PHI] / No]
**Data Lineage Notes:** [Any transformation, derivation, or calculation applied to this field]
**Masking / Anonymization Required:** [Yes / No — if yes, describe how]
**Known Issues / Anomalies:** [Any known data quality issues, legacy quirks, or inconsistencies]
---

After all entries, produce:

DATA DICTIONARY SUMMARY TABLE:
| Field ID | Technical Name | Business Name | Entity | Required | PII | Notes |
|----------|---------------|---------------|--------|----------|-----|-------|

PII / SENSITIVE DATA INVENTORY:
List all fields classified as PII, PCI, PHI, or otherwise sensitive. This feeds the data governance and security review.

KEY RELATIONSHIPS:
Describe important relationships between fields (e.g., "ACCT_STATUS governs which values are valid in ACCT_CLOSE_DATE").

FIELD NAMING ANALYSIS:
Identify any fields whose technical names are ambiguous, misleading, or inconsistent with the rest of the schema. Suggest standardized business names.

DATA QUALITY FLAGS:
List any fields that appear to have data quality issues based on the schema definition (e.g., "nullable foreign keys", "VARCHAR fields storing numeric data", "date fields with no format constraint").
```

**Review checklist:**
- [ ] All PII/sensitive fields have been classified and reviewed with the data governance team
- [ ] Business definitions have been validated by a domain SME (not just written by AI from field names)
- [ ] Valid values for enumerated fields have been confirmed against the actual system data
- [ ] Fields with "Known Issues" have been flagged for data quality remediation
- [ ] Data dictionary has been reviewed by the Developer lead for accuracy
- [ ] Data dictionary has been reviewed by the QA lead for test data implications
- [ ] Masking requirements have been reviewed with the DevSecOps team

---

## Prompt 5: Gap Analysis

### Gap Analysis
**When to use:** When you need to compare the current state (capabilities, processes, or systems) against the desired future state, and produce a prioritized list of what needs to change, build, or remove. Use to scope a modernization program or define a backlog.

**Inputs needed:**
- Current state capabilities description
- Future state requirements or vision
- Business priorities and constraints

**Output:** A structured gap analysis with gap descriptions, business impact, effort estimates, and prioritized recommendations.

**Tool notes:** The more specific your current and future state descriptions, the more actionable the gap analysis. Claude Code handles long input documents. Pair with the As-Is/To-Be mapping prompt for comprehensive coverage.

**Prompt:**
```text
You are a Business Analyst conducting a formal gap analysis to scope a transformation initiative.

CONTEXT:
- Initiative: [INITIATIVE NAME]
- Business Objective: [What business outcome are we trying to achieve?]
- Stakeholders: [Who has a stake in the gap analysis findings?]
- Time Horizon: [What is the target delivery timeframe?]
- Strategic Constraints: [Budget range, technology mandates, regulatory deadlines, etc.]

CURRENT STATE CAPABILITIES:
[Describe what the business / system / process CAN do today. Be specific. Include capacity, performance, quality, and operational measures where known.]

FUTURE STATE REQUIREMENTS:
[Describe what the business NEEDS to be able to do. Reference the PRD, business objectives, or regulatory requirements.]

INSTRUCTIONS:
Produce a comprehensive gap analysis document.

SECTION 1 — CAPABILITY INVENTORY
For each capability area, assess current vs. required state:

| Capability Area | Current State | Required Future State | Gap Exists? | Gap Description |
|----------------|--------------|----------------------|-------------|-----------------|

SECTION 2 — GAP REGISTER (detailed)
For each identified gap, create a detailed entry:

**Gap ID:** GAP-[SEQUENTIAL NUMBER, e.g., GAP-001]
**Gap Name:** [Short, descriptive name]
**Capability Area:** [Which area this gap belongs to]
**Gap Description:** [What is missing, inadequate, or absent?]
**Business Impact of Gap:** [What business problem does this gap cause today? Quantify if possible: cost, time, compliance risk, customer impact]
**Root Cause:** [Why does this gap exist? Technical debt, process design, resource, regulatory change?]
**Business Priority:** [CRITICAL / HIGH / MEDIUM / LOW]
  - CRITICAL: Must be closed for regulatory compliance or business survival
  - HIGH: Significant business impact; creates meaningful risk or cost if not addressed
  - MEDIUM: Operational improvement; notable but manageable
  - LOW: Nice to have; limited business impact
**Effort to Close:** [HIGH / MEDIUM / LOW]
  - HIGH: >6 months, significant investment, multiple teams
  - MEDIUM: 2-6 months, moderate investment, one or two teams
  - LOW: <2 months, minimal investment, one team or individual
**Resolution Approach:** [How should this gap be closed? Build / Buy / Configure / Process Change / Policy Change / Training]
**Dependencies:** [Other gaps that must be closed first]
**Risks if Not Closed:** [What happens if this gap is not addressed in this program?]
**Owner:** [Which team or role is responsible for closing this gap?]

SECTION 3 — GAP PRIORITIZATION MATRIX
Plot all gaps on a 2×2 matrix using text notation:

HIGH IMPACT / LOW EFFORT (Quick Wins):
→ GAP-XXX, GAP-XXX [Address immediately]

HIGH IMPACT / HIGH EFFORT (Strategic Investments):
→ GAP-XXX, GAP-XXX [Plan carefully, may need phasing]

LOW IMPACT / LOW EFFORT (Fill-ins):
→ GAP-XXX, GAP-XXX [Address when capacity allows]

LOW IMPACT / HIGH EFFORT (Deprioritize):
→ GAP-XXX, GAP-XXX [Challenge whether these belong in scope]

SECTION 4 — RECOMMENDED PHASING
Group gaps into implementation phases:
Phase 1 (Foundation / Must-Have): [Gaps] — Rationale
Phase 2 (Core Capabilities): [Gaps] — Rationale
Phase 3 (Enhancement / Full Feature): [Gaps] — Rationale
Out of Scope for This Program: [Gaps] — Rationale

SECTION 5 — GAP SUMMARY STATISTICS
- Total gaps identified: N
- CRITICAL: N (list them)
- HIGH priority: N
- MEDIUM priority: N
- LOW priority: N
- Quick wins (High impact / Low effort): N
- Total estimated effort (rough): [Story points or person-months]

SECTION 6 — DEPENDENCIES AND SEQUENCING
Show which gaps must be closed before others can be addressed.

SECTION 7 — OPEN QUESTIONS
Questions that must be answered before the gap analysis can be finalized:
Q1. [Question] — Owner: [Role] — Required by: [Date]
```

**Review checklist:**
- [ ] All CRITICAL gaps have been validated with the business sponsor
- [ ] Effort estimates have been reviewed with the Lead Engineer or Solution Architect
- [ ] Low impact / High effort gaps have been challenged — confirm whether they're truly in scope
- [ ] Phase 1 is realistically achievable within the program timeline
- [ ] Open questions have been assigned with target resolution dates
- [ ] Gap analysis has been reviewed with the Project Manager for incorporation into the project plan

---

## Prompt 6: BRD (Business Requirements Document)

### BRD (Business Requirements Document)
**When to use:** When a formal business requirements document is needed as the contractual basis for a software delivery engagement, regulatory requirement, or major procurement. Use when a PRD alone is insufficient.

**Inputs needed:**
- Business objectives and problem statement
- Process descriptions or flows
- Stakeholder list
- Existing PRD (if available)
- Regulatory or compliance requirements

**Output:** A complete BRD following standard BA methodology, with business context, functional requirements, and a traceability framework.

**Tool notes:** Claude Code is recommended for this prompt due to document length. Paste your PRD, process descriptions, and business rules as input for a comprehensive output.

**Prompt:**
```text
You are a senior Business Analyst writing a Business Requirements Document (BRD) for an enterprise software project.

DOCUMENT CONTEXT:
- Project Name: [PROJECT NAME]
- Version: [1.0]
- Date: [DATE]
- Prepared By: [BA NAME / TEAM]
- Business Sponsor: [SPONSORING EXECUTIVE OR BUSINESS UNIT]
- Intended Audience: [e.g., "Project Steering Committee, Solution Architect, Development Team, QA Team"]

INPUTS (paste relevant materials):
- Business Objectives: [LIST 3-6 BUSINESS OBJECTIVES]
- Problem Statement: [DESCRIBE THE CURRENT BUSINESS PROBLEM]
- Stakeholders: [LIST STAKEHOLDER GROUPS AND THEIR INTEREST IN THE PROJECT]
- Process Context: [PASTE PROCESS DESCRIPTIONS OR KEY PROCESS STEPS]
- Business Rules: [PASTE KNOWN BUSINESS RULES OR REFERENCE BR CATALOG]
- Regulatory Requirements: [LIST APPLICABLE REGULATIONS OR STANDARDS]
- Constraints: [BUDGET, TIMELINE, TECHNOLOGY, STAFFING CONSTRAINTS]

INSTRUCTIONS:
Write a complete BRD with the following sections:

1. DOCUMENT CONTROL
Version history table, approval signatures table (names/roles to be filled in)

2. EXECUTIVE SUMMARY (half page)
Purpose of this document, project overview, business problem and opportunity, summary of key requirements, expected business benefits

3. BUSINESS CONTEXT
3.1 Business Background: How did we get here? What is the strategic or operational driver?
3.2 Current State Summary: How does the business operate today? What are the pain points?
3.3 Desired Future State: What will the business look like after this project?
3.4 In Scope: What is explicitly included in this engagement?
3.5 Out of Scope: What is explicitly excluded?

4. STAKEHOLDER ANALYSIS
| Stakeholder Group | Role in Project | Business Needs | Level of Influence | Engagement Approach |
|------------------|----------------|----------------|-------------------|---------------------|

5. BUSINESS REQUIREMENTS
Organize as numbered requirements:
**BR-[CATEGORY]-[NUMBER]: [Requirement Name]**
- Statement: [The business MUST/SHOULD/COULD...] — use MoSCoW language
- Rationale: [Why is this required?]
- Source: [Who provided this requirement?]
- Priority: [Must Have / Should Have / Could Have / Won't Have This Release]
- Business Rule Reference: [BR-XXX if applicable]
- Acceptance Criteria: [How will the business validate this requirement is met?]
- Assumptions: [Any assumptions underlying this requirement]

Organize requirements into logical categories:
- Functional Requirements (what the system must do)
- Data Requirements (what data must be captured, stored, processed)
- Process Requirements (what business processes must be supported)
- Reporting and Analytics Requirements
- Integration Requirements (what the system must connect to)
- Non-Functional Business Requirements (performance expectations in business terms, availability expectations, user experience standards)
- Compliance and Regulatory Requirements

6. CONSTRAINTS AND ASSUMPTIONS
6.1 Business Constraints (budget, regulatory, organizational)
6.2 Technical Constraints (must be noted even in a business document)
6.3 Assumptions (numbered list — each assumption should be validated)

7. BUSINESS RULES REFERENCE
Table mapping requirements to business rules:
| Requirement ID | Business Rule(s) Applied |

8. GLOSSARY
Business terms and acronyms used in this document

9. SIGN-OFF PAGE
Stakeholder sign-off table with: Name, Role, Date, Signature

Use professional tone throughout. Number all requirements sequentially. Flag any section where input was insufficient with [REQUIRES INPUT FROM: role].
```

**Review checklist:**
- [ ] All requirements use MoSCoW language (MUST/SHOULD/COULD/WON'T)
- [ ] Every requirement has an acceptance criterion that can be tested
- [ ] All `[REQUIRES INPUT FROM:]` flags have been resolved before distributing
- [ ] Stakeholder sign-off page has been completed with the correct approvers
- [ ] Business rules referenced exist in the BR catalog
- [ ] Document has version control and is stored in the project document repository
- [ ] Out-of-scope section has been reviewed to confirm it accurately reflects agreed boundaries

---

## Prompt 7: Stakeholder Requirements Matrix

### Stakeholder Requirements Matrix
**When to use:** When you need to confirm that all stakeholder needs have been captured in the requirements, and that no stakeholder group has been missed. Use as a traceability tool before finalizing the BRD or backlog.

**Inputs needed:**
- Stakeholder list (roles and their needs)
- List of business requirements or user stories
- Any stakeholder interview notes

**Output:** A matrix mapping each stakeholder need to specific requirements, with coverage gaps highlighted.

**Tool notes:** Works well in all tools. Most effective when you have an existing requirements list to map against. Can also be used to generate stakeholder needs when starting from scratch.

**Prompt:**
```text
You are a Business Analyst creating a Stakeholder Requirements Matrix to validate requirements coverage.

PROJECT: [PROJECT NAME]

STAKEHOLDERS AND THEIR NEEDS:
[For each stakeholder, describe their role and what they need from this system/project]

Stakeholder 1: [Role name]
Needs: [List what they need the system to do for them — in their own terms if you have interview notes]

Stakeholder 2: [Role name]
Needs: [...]

[Continue for all stakeholders]

EXISTING REQUIREMENTS / USER STORIES:
[List your current requirements or user stories]
BR-001: [Requirement description]
BR-002: [...]
[Or paste user story titles]

INSTRUCTIONS:
Produce a Stakeholder Requirements Matrix that:
1. Maps every stakeholder need to the requirement(s) that address it
2. Identifies needs that are NOT addressed by any current requirement (coverage gaps)
3. Identifies requirements that do not map to any stakeholder need (orphan requirements — potential scope creep)

MATRIX FORMAT:

COVERAGE MATRIX:
| Stakeholder | Need # | Need Description | Addressed By (Req ID) | Coverage | Priority |
|------------|--------|-----------------|----------------------|----------|----------|
| [Role] | N1 | [Need] | BR-XXX, BR-YYY | FULL / PARTIAL / NOT COVERED | H/M/L |

COVERAGE GAPS (Needs Not Addressed by Any Requirement):
For each uncovered need:
- Stakeholder: [Who has this need]
- Need: [Description]
- Gap Severity: [HIGH / MEDIUM / LOW — based on who needs it and how critical]
- Recommended Action: [Add new requirement / Clarify existing requirement / De-scope]
- Suggested New Requirement: [Draft the requirement text]

ORPHAN REQUIREMENTS (Requirements With No Stakeholder Need):
For each orphan:
- Requirement: [ID and description]
- Assessment: [Is this a hidden need from an unlisted stakeholder? Is it technical scaffolding? Is it scope creep?]
- Recommendation: [Keep (justify) / Remove / Reassign to stakeholder]

COVERAGE SUMMARY:
| Stakeholder | Total Needs | Fully Covered | Partially Covered | Not Covered | Coverage % |
|------------|-------------|--------------|-------------------|-------------|-----------|

RECOMMENDED ACTIONS:
Prioritized list of actions to achieve full coverage, ordered by business impact.

TRACEABILITY NOTE:
Flag any stakeholder groups who were NOT interviewed or consulted — their needs may be missing entirely from this matrix.
```

**Review checklist:**
- [ ] All stakeholder groups have been included — check for silent stakeholders (support, operations, compliance, audit)
- [ ] Every HIGH severity gap has a proposed new requirement drafted
- [ ] Orphan requirements have been challenged with the Product Owner
- [ ] Traceability flag has been acted on — missing stakeholder groups have been consulted
- [ ] Matrix has been reviewed with the Product Owner before finalizing the backlog
- [ ] Final matrix has been baselined as a traceability artifact for the project

---

*FORGE — Framework for Orchestrated AI-Guided Engineering | Business Analyst Prompt Pack v1.0*
