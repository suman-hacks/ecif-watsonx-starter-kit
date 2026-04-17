# T1: System Architecture Analysis

**Task:** 1 of 5  
**Output File:** `TX_ARCH.md`  
**Who Runs It:** Lead Engineer or Solution Architect  
**Time Required:** 5–10 minutes  
**Prerequisite:** Codebase loaded into AI assistant context

---

## Stage Overview

This task produces a factual, evidence-based map of the system's architecture directly from source code. It answers the fundamental question: *what is this system, and how does data flow through it?*

The output is used by workshop facilitators, solution architects, and new team members to establish a shared mental model before any design or modernization work begins.

## Workshop Connection

`TX_ARCH.md` anchors the "Current State" segment of the discovery workshop. Facilitators use it to:
- Confirm or correct the team's understanding of the system
- Identify which components are candidates for AI augmentation
- Ground the conversation about scope and risk in actual code evidence

## Tool Guidance

| Tool | Instructions |
|---|---|
| **GitHub Copilot** | Prefix prompt with `@workspace` to activate workspace-wide analysis |
| **Claude Code** | Paste prompt directly — Claude Code has full codebase access |
| **watsonx Code Assist** | Open key entry-point files before pasting; reference specific paths |
| **Cursor** | Use Composer mode with `@codebase` — ensures full repo indexing |

**Important:** After receiving AI output, review sections 1 and 6 (tech stack and diagram) against what you know to be true. Correct any hallucinations before saving.

---

## The Prompt

Copy everything between the `---PROMPT START---` and `---PROMPT END---` markers. Replace bracketed placeholders before running.

---PROMPT START---

You are a senior software architect performing a pre-engagement codebase analysis. Analyze this entire codebase thoroughly and produce a structured architecture document. Your analysis must be grounded in actual code evidence — cite specific file names, class names, configuration files, and package structures to support every claim. Do not invent components; if something is unclear, mark it as `[UNKNOWN — needs investigation]`.

**Project context:** [INSERT 1–2 SENTENCES: what this system does, e.g. "A payment authorization service that processes card transactions in real time for a retail banking platform."]

**Primary entry points to examine:** [INSERT: e.g. "src/main/java/com/example/AuthorizationService.java, config/application.yml, pom.xml"]

Produce a document titled "System Architecture Analysis" with the following sections:

---

## 1. Tech Stack

Produce a table with columns: **Component | Technology | Version | Role in System**

Rows must cover:
- Primary programming language(s) and runtime version
- Web/application framework
- Database(s) — note if primary vs. read replica vs. cache
- Message broker / event streaming platform (if any)
- Rules engine (if any)
- External API clients (identify the target system, not just the HTTP library)
- Build and dependency management tools
- Containerization and orchestration
- CI/CD tooling (from pipeline config files)
- Observability stack (logging, metrics, tracing)
- Authentication/authorization mechanism
- Any other significant library or platform (identify from dependency manifests, not assumptions)

For each version, extract from lock files, pom.xml, build.gradle, package.json, requirements.txt, or go.mod — do not guess versions.

---

## 2. Transaction / Request Flow

Produce a table with columns: **Step # | Component | What Happens | Latency-Sensitive?**

Trace the primary transaction or request from entry point to response, covering:
- Where the request enters (listener, controller, endpoint)
- Authentication/authorization checks
- Input validation
- Every service, module, or class that processes or transforms the request
- Every database read or write
- Every external service call
- Decision points (approve/decline/route logic)
- Response construction and return

Mark "Latency-Sensitive?" as YES for any step that adds measurable latency (DB call, external API, rule evaluation, cryptographic operation) or NO for pure in-memory logic.

If the system processes multiple transaction types, trace the most complex one and note the others differ at step [X].

---

## 3. Decision Logic Locations

Identify **every location** in the codebase where a consequential business decision is made — an approve/decline/route/flag/score determination. Produce a table with columns: **Decision Type | Location (file/class/method) | Logic Type | Input Data Used | External Dependencies**

Logic Type options: `HARDCODED_RULE | CONFIGURABLE_RULE | RULES_ENGINE | ML_MODEL | EXTERNAL_SERVICE | DATABASE_LOOKUP | HYBRID`

Include:
- Approval/decline decisions
- Risk scoring or fraud scoring
- Routing logic (which processor, which network, which product)
- Fee calculation logic
- Limit checks (credit, velocity, amount)
- Regulatory hold logic
- Status checks (account active, card blocked, etc.)

For each decision, note whether modifying it requires a code deploy or can be done through configuration/admin UI.

---

## 4. Service / Module Map

Produce a table with columns: **Service/Module Name | Primary Responsibility | Language | Approximate LOC | Deployment Unit | Key Dependencies**

- Extract this from the actual package/module/directory structure
- For monoliths, list top-level packages or modules
- For microservices, list each service
- For approximate LOC: count or estimate from file sizes and count
- Deployment Unit: `MONOLITH | MICROSERVICE | LIBRARY | BATCH_JOB | SERVERLESS_FUNCTION | UNKNOWN`

---

## 5. Infrastructure Indicators

Analyze configuration files, deployment descriptors, environment variable references, and comments to determine:

**Processing Model:**
- [ ] Real-time / online transaction processing (OLTP)
- [ ] Batch processing (indicate schedule/trigger)
- [ ] Mixed (real-time + batch — describe both)

**Deployment Environment (evidence-based):**
- [ ] On-premises (evidence: ___)
- [ ] Cloud (provider: ___, evidence: ___)
- [ ] Mainframe (evidence: ___)
- [ ] Hybrid (describe: ___)
- [ ] Unknown (explain: ___)

**Mainframe Indicators:** List any COBOL source files, CICS descriptors, JCL scripts, VSAM file references, MQ Series configurations, or IBM-specific library dependencies found. If none found but the system integrates with mainframe, note the integration points.

**Scalability Model:**
- How are multiple instances handled? (stateless? session affinity? distributed cache?)
- Evidence of horizontal scaling configuration?
- Evidence of connection pooling configuration?

**Notable Infrastructure Constraints:**
List any configurations that suggest fixed infrastructure limits: hardcoded hostnames, port numbers, fixed thread pool sizes, static IP references, license-bound components.

---

## 6. ASCII Architecture Diagram

Produce an end-to-end ASCII diagram showing the primary data flow. Use this format:

```
[Client / Caller]
        |
        | (protocol: e.g. HTTPS/REST, ISO 8583/TCP)
        v
[Entry Point Component]
        |
        |-- [Auth/AuthZ Check]
        |
        v
[Core Processing Component]
        |
        |-- [Rules / Decision Engine]
        |        |-- [Config DB / Rules Store]
        |
        |-- [External Service Call 1] --> [External System Name]
        |
        |-- [Database Read/Write] --> [DB Name/Type]
        |
        v
[Response Construction]
        |
        v
[Client / Caller]
```

Adapt the diagram to the actual system. Show all major components and their connections. Label each arrow with the protocol/format used. If a component is a `[BLACK BOX]` or `[MAINFRAME GAP]`, label it explicitly.

---

## 7. Unknowns and Black Boxes

Produce a table with columns: **Component/Area | What Is Unknown | Why It Matters | Investigation Path**

List every area where the analysis is incomplete:
- Components referenced in code but not in this repository
- External services called whose behavior is not documented in code
- Configuration that references external systems not available for analysis
- Business logic that appears to live outside this codebase (database stored procedures, mainframe, external rules service)
- Any section above where you were forced to write `[UNKNOWN]`

For each unknown, suggest a concrete investigation path (e.g., "Interview the DBA about stored procedures in schema X", "Check the mainframe CICS map for screen BK001", "Review the vendor integration specification for ISO 8583 field 127").

---

**Output format requirements:**
- Use Markdown with clear heading hierarchy
- All tables must be properly formatted Markdown tables
- The ASCII diagram must be in a code block
- Cite evidence for every claim: file paths, class names, config keys
- Mark any invented or assumed content with `[ASSUMED — verify]`
- Mark any gaps with `[UNKNOWN — needs investigation]`
- Do not include recommendations or future state — analysis only

---PROMPT END---

---

## Output Template

Save the AI's output as `TX_ARCH.md` in the `pre-engagement/` folder. The file should begin with:

```markdown
# System Architecture Analysis
**Project:** [Project Name]
**Date:** [YYYY-MM-DD]
**Analyzed By:** [Engineer Name]
**AI Tool Used:** [Tool Name + version]
**Codebase Commit/Branch:** [Git SHA or branch name]
**Review Status:** [ ] Draft | [ ] Reviewed | [ ] Approved

---
```

## Completion Checklist

- [ ] All 7 sections are present and populated (not just headers)
- [ ] Tech stack table has at least 8 rows with actual version numbers
- [ ] Transaction flow table traces the full request lifecycle end-to-end
- [ ] Decision logic locations table covers all approval/decline paths
- [ ] ASCII diagram is accurate (verified by at least one team member who knows the system)
- [ ] Infrastructure indicators are evidence-based, not assumed
- [ ] All `[UNKNOWN]` items are documented with investigation paths
- [ ] File has been saved and shared with workshop facilitator
- [ ] No hallucinated components (team has verified the output)

## Common Pitfalls

**AI invents components that don't exist.** Always verify the ASCII diagram and service map against what your team knows. Ask "does `ServiceX` actually exist in our repo?" before accepting it.

**Version numbers are guessed.** AI will sometimes invent version numbers. Always cross-check against your actual dependency manifest files.

**Mainframe gaps are silently omitted.** If the codebase has MQ or CICS integration points, the AI may describe the Java client but not what's on the other end. Explicitly add `[MAINFRAME GAP]` markers and document what is known from team knowledge.

**Decision logic is incomplete.** Business rules buried in database stored procedures, external rules engines, or configuration files the AI cannot read will be missing. Check with DBAs and business analysts for rules that live outside the codebase.

**The diagram shows "ideal" architecture, not actual.** If the AI draws a clean microservices diagram but you have a monolith, it has hallucinated. Insist on evidence-based output.

---

*Next Task: T2 — Decision Logic Inventory*
