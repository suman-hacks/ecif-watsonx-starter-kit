# T4: Complexity and Risk Map

**Task:** 4 of 5  
**Output File:** `TX_RISK_MAP.md`  
**Who Runs It:** Lead Engineer or Quality Engineering Lead  
**Time Required:** 5–10 minutes  
**Prerequisite:** T1–T3 completed. Codebase loaded into AI assistant context. Git history accessible.

---

## Stage Overview

This task produces a risk-stratified map of the codebase, identifying where change is most likely to break things, where testing is weakest, and where technical debt is most concentrated. It answers: *where are the landmines, and what do we not know that we don't know?*

This document is not about criticizing the existing code. It is about making risk visible so that modernization, AI augmentation, and new development efforts are scoped and sequenced safely.

## Workshop Connection

`TX_RISK_MAP.md` is used in the "What Could Go Wrong?" and "Scope Boundary" segments of the discovery workshop. Facilitators use it to:
- Identify which components must not be touched in a POC
- Understand regression risk before committing to a migration timeline
- Calibrate effort estimates with realistic risk multipliers

## Tool Guidance

| Tool | Instructions |
|---|---|
| **GitHub Copilot** | Use `@workspace`. For git history analysis, you may need to run `git log` commands separately and paste the output into the prompt |
| **Claude Code** | Claude Code can read git history directly. Mention this capability explicitly in the prompt |
| **watsonx Code Assist** | Focus on file content analysis for complexity; run `git log --numstat` separately and paste results |
| **Cursor** | Use `@codebase`. Supplement with git log output pasted into the conversation |

**Git history prerequisite:** Before running this prompt, execute the following and have the output ready to paste:
```bash
# Files changed most in last 90 days
git log --since="90 days ago" --name-only --pretty=format: | sort | uniq -c | sort -rn | head -30

# Files not touched in over a year
git log --before="1 year ago" --name-only --pretty=format: | sort | uniq | head -30
```

---

## The Prompt

Copy everything between the `---PROMPT START---` and `---PROMPT END---` markers.

---PROMPT START---

You are a senior software engineer and quality lead performing a pre-engagement complexity and risk assessment. Analyze this codebase to identify areas of high complexity, technical debt, test coverage gaps, and change risk. Be specific and evidence-based — cite file names, class names, and code patterns. Do not editorialize; report what you find.

**Project context:** [INSERT: same description used in T1]

**Git activity data (paste output of git log analysis here):**
```
[PASTE GIT LOG OUTPUT HERE, or write "Git history not available — skip section 4"]
```

**Key components identified in T1–T3:** [INSERT: paste the Service/Module Map table from TX_ARCH.md]

Produce a document titled "Complexity and Risk Map" with the following sections:

---

## 1. Code Complexity Hotspots

Identify the 10 most complex files or modules in the codebase. Rank by complexity, not by size alone.

For each, produce an entry with:

### [Rank]. [File/Class Name]
**File Path:** [full path]  
**Approximate LOC:** [count]  
**Cyclomatic Complexity Indicators:** [describe: number of if/else/switch branches, nested conditions, loop nesting depth]  
**Role in System:** [what this component does in the overall flow from T1]  
**Complexity Drivers:** [what makes this complex: mixed concerns, many decision branches, deeply nested logic, many dependencies, state management, concurrency]  
**Change Risk Level:** HIGH | MEDIUM | LOW  
**Change Risk Rationale:** [why changes to this file are risky]  
**Dependencies:** [what other components depend on this file/class]  
**Test Coverage Indicator:** [are there tests targeting this class? how comprehensive do they appear?]

---

## 2. Test Coverage Assessment

### 2a. Test Infrastructure

**Test Frameworks Found:** [list frameworks from dependencies — JUnit, TestNG, pytest, Jest, RSpec, etc.]  
**Test Types Present:** Check each that exists with evidence:
- [ ] Unit tests — location: ___
- [ ] Integration tests — location: ___
- [ ] End-to-end / acceptance tests — location: ___
- [ ] Contract tests (Pact, Spring Cloud Contract) — location: ___
- [ ] Performance tests — location: ___
- [ ] Chaos / resilience tests — location: ___

**Test-to-Code Ratio:** Count test files vs. source files. Express as a ratio (e.g., "1 test file per 3 source files"). Note this is a rough proxy, not a coverage metric.

**Test Execution Configuration:** Is there a CI pipeline that runs tests? (cite pipeline config file) How long does the test suite take? (from pipeline logs or config if available)

### 2b. Coverage of Critical Paths

For each critical path identified in T1 (transaction flow) and T2 (top 10 decision paths), assess test coverage:

Produce a table with columns: **Critical Path / Decision | Test Coverage Assessment | Evidence | Coverage Gap Risk**

**Coverage Assessment options:**
- `WELL TESTED` — multiple tests covering happy path, error paths, and boundary conditions
- `PARTIALLY TESTED` — happy path tested, but error paths or boundary conditions missing
- `NOMINALLY TESTED` — tests exist but appear shallow (no boundary conditions, no error paths)
- `UNTESTED` — no tests found for this path
- `UNKNOWN` — cannot determine from static analysis

### 2c. Test Quality Observations

**Mock/Stub Usage:** Are tests isolated (mocked dependencies) or do they require real infrastructure?  
**Test Data Management:** How is test data managed? Hardcoded values? Factories/builders? Database seeds?  
**Flaky Test Indicators:** Are there `@Ignore`, `@Skip`, `@Disabled`, or `retry` annotations suggesting flaky tests?  
**Assertion Quality:** Do tests have meaningful assertions, or are they primarily smoke tests (no exception thrown = pass)?

---

## 3. Regression Risk Assessment

### 3a. Shared Libraries and Tight Coupling

List all internal shared libraries, base classes, utility classes, or shared configuration that are used by multiple components. For each:

**Component:** [name]  
**Used By:** [list of dependent modules/services]  
**Change Impact:** [what breaks if this component changes incorrectly]  
**Isolation Quality:** [are there tests that would catch a regression in this component?]

### 3b. Tightly Coupled Components

Identify pairs or clusters of components where changes to one very likely require changes to another (high afferent/efferent coupling). Evidence: shared database tables, shared in-memory state, direct class references across module boundaries, or circular dependencies.

Produce a table with columns: **Component A | Component B | Coupling Type | Why It Matters | Decoupling Difficulty**

**Coupling Types:** Shared DB Table, Shared In-Memory State, Direct Class Reference, Shared Configuration, Shared API Contract, Circular Dependency

### 3c. Untested High-Risk Areas

List areas that combine HIGH complexity (from section 1) with LOW test coverage. These are the most dangerous areas for regression.

For each: [component] — complexity reason — coverage gap — potential impact of regression

### 3d. Concurrency and State Issues

Look for patterns that suggest concurrency risks:
- Shared mutable state accessed from multiple threads
- Non-atomic operations on shared data
- Missing synchronization on cache updates
- Race conditions in async processing
- Missing idempotency in message handlers

List each finding with: file/class, pattern observed, risk level.

---

## 4. Change Velocity Analysis

*(Based on git history data provided above. If git history not available, note all items as [UNKNOWN — git history required])*

### 4a. High-Churn Files (Last 90 Days)

Produce a table with columns: **File | Change Count (90 days) | Primary Change Type | Stability Concern**

**Primary Change Type** (infer from commit messages if available): Bug Fix, Feature Addition, Refactoring, Configuration Change, Unknown

**Stability Concern:** Files with high churn that are also high-complexity are the highest regression risk. Flag these with `HIGH RISK`.

### 4b. Stale Files (Not Modified in 12+ Months)

Produce a table with columns: **File | Last Modified Estimate | Role | Staleness Risk**

**Staleness Risk:**
- `DORMANT` — feature or integration that is intentionally inactive
- `FORGOTTEN` — appears to be active code that nobody has needed to change (could be stable, or could be neglected)
- `DEAD CODE` — appears to be unreachable or unused (flag for potential removal)
- `LEGACY CORE` — stale but central to the system — high risk to touch

### 4c. Active Refactoring Indicators

Are there signs of an in-progress refactoring effort?
- TODO/FIXME/HACK/REFACTOR comments with patterns suggesting systematic work
- Branch names or PR references visible in comments
- Deprecated annotations on classes/methods
- Parallel implementations (old and new version of the same logic)
- Feature flags gating new implementations

List any active refactoring efforts found, with their current state and risk to a parallel POC effort.

---

## 5. Documentation Gaps

### 5a. What Documentation Exists

Inventory the documentation found in the repository:
- [ ] README or top-level overview (quality: ___/5)
- [ ] API documentation (OpenAPI/Swagger, WSDL, or manual) — completeness: ___
- [ ] Architecture documentation (ADRs, diagrams, wiki links) — completeness: ___
- [ ] Deployment/operations runbooks — completeness: ___
- [ ] Data model documentation — completeness: ___
- [ ] Integration specifications — completeness: ___
- [ ] Business rules documentation — completeness: ___
- [ ] Onboarding documentation — completeness: ___

### 5b. Notable Documentation Gaps

List the most significant documentation gaps and their impact:

For each gap: **What Is Missing | Why It Matters | Risk Level | Recovery Option**

**Recovery Options:** Interview existing team, Analyze code directly, Engage vendor, Recover from runbooks, Unknown

### 5c. Code Comment Quality

**Self-Documenting Code:** Is the code readable without comments (good naming, clear structure)?  
**Comment Coverage:** Are the most complex methods/classes documented inline?  
**Comment Quality:** Are comments accurate and current (not contradicting the code), or are there signs of comment drift?  
**TODO/FIXME Inventory:** Approximate count of TODO/FIXME comments. Note any that appear to describe known defects or deferred work that could affect a POC.

---

## 6. Overall Risk Summary

### Risk Heat Map

Produce a 2×2 risk matrix. For each component identified in T1, place it in the appropriate quadrant:

**High Complexity + Low Coverage = DANGER ZONE** (require dedicated hardening before touch)  
**High Complexity + High Coverage = MANAGEABLE RISK** (change carefully; tests provide safety net)  
**Low Complexity + Low Coverage = WATCH LIST** (low risk now, but risky if complexity grows)  
**Low Complexity + High Coverage = GREEN ZONE** (safe to change and test)

```
                    LOW COVERAGE          HIGH COVERAGE
HIGH COMPLEXITY   | DANGER ZONE        | MANAGEABLE RISK |
LOW  COMPLEXITY   | WATCH LIST         | GREEN ZONE      |
```

Then list each major component in its quadrant.

### Top 5 Risks for Any Change Initiative

Rank the 5 highest risks for a modernization or AI augmentation initiative targeting this codebase. For each:
- Risk description
- Likelihood (HIGH/MEDIUM/LOW)
- Impact if realized (HIGH/MEDIUM/LOW)
- Mitigation approach

---

**Output format requirements:**
- Use Markdown with clear heading hierarchy
- All tables must be properly formatted Markdown tables
- Specific files and classes must be cited for every finding
- The risk heat map must be ASCII-art formatted in a code block
- Analysis only — no architecture recommendations at this stage
- Mark all git-history-dependent sections as `[UNKNOWN — git history required]` if no git data was provided

---PROMPT END---

---

## Output Template

Save the AI's output as `TX_RISK_MAP.md` in the `pre-engagement/` folder. The file should begin with:

```markdown
# Complexity and Risk Map
**Project:** [Project Name]
**Date:** [YYYY-MM-DD]
**Analyzed By:** [Engineer Name]
**AI Tool Used:** [Tool Name + version]
**Codebase Commit/Branch:** [Git SHA or branch name]
**Git History Available:** Yes / No
**Related Documents:** TX_ARCH.md, TX_DECISIONS.md, TX_INTEGRATIONS.md
**Review Status:** [ ] Draft | [ ] Reviewed | [ ] Approved

---
```

## Completion Checklist

- [ ] All 6 sections populated with actual findings
- [ ] Top 10 complexity hotspots are specific files/classes, not vague generalizations
- [ ] Test coverage assessment covers all critical paths from T1 and T2
- [ ] Regression risk section identifies shared components and coupling
- [ ] Change velocity analysis completed (or marked [UNKNOWN] if no git access)
- [ ] Documentation gaps are prioritized by risk
- [ ] Risk heat map is populated with actual component names
- [ ] Top 5 risks have mitigations
- [ ] Document reviewed by QA or quality lead
- [ ] Saved and shared with workshop facilitator

## Common Pitfalls

**Confusing LOC with complexity.** Large files are not always complex — a 500-line data transfer object is simpler than a 50-line method with 20 nested conditions. Emphasize cyclomatic complexity indicators (branches, loops, conditions) over raw line count.

**Test existence does not mean test coverage.** A test file that only tests the happy path of a complex method provides false confidence. The prompt specifically asks for boundary condition and error path coverage — make sure the AI addresses these specifically.

**Git history unavailable.** If the codebase was provided as a zip or the `.git` directory is missing, the entire section 4 will be `[UNKNOWN]`. This is a significant gap — change velocity and churn are strong indicators of risk. Escalate to get git history access.

**Active refactoring not flagged.** If a parallel refactoring is already underway in a long-running branch, a POC that modifies the same files will create a merge conflict minefield. Always ask the team about in-flight work even if it is not visible in the main branch.

**Documentation quality overstated.** AI may find a README and conclude documentation is adequate without reading it critically. Ask specifically whether the README is accurate for the current version of the code.

---

*Previous Task: T3 — Integration and Latency Map | Next Task: T5 — POC Option Analysis*
