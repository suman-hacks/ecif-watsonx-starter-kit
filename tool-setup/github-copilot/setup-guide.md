# GitHub Copilot Setup Guide — FORGE v2.0 + ATOM

> This guide covers all three Copilot modes: **Chat** (standard Q&A), **Agent Mode** (autonomous multi-step coding), and **Copilot Workspace** (task-based planning and implementation). Each mode has a different role in FORGE workflows.

---

## Quick Decision: Which Mode to Use

| Task | Best Mode | Why |
|---|---|---|
| Generate a single class or method | Copilot Chat / Inline | Quick, targeted, single-output |
| Implement a full FORGE Stage 3 service | **Agent Mode** | Multi-file, autonomous, runs tests |
| Complex refactor spanning many files | **Agent Mode** | Plans first, executes across the codebase |
| Understand a legacy file | Copilot Chat | Exploratory — you direct the analysis |
| Review a PR | Copilot Chat | Targeted questions about specific diffs |
| Plan a new feature from a Jira ticket | **Copilot Workspace** | Produces a plan you approve before code is written |
| Complex COBOL analysis / FORGE Stage 1 | Claude Code | Better for deep analysis and structured outputs |

---

## Part 1: Standard Setup (All Modes)

### Step 1: Install the Extension

**VS Code:**
1. Extensions panel (Ctrl+Shift+X)
2. Search: `GitHub Copilot`
3. Install: **GitHub Copilot** (the base extension)
4. Install: **GitHub Copilot Chat** (required for Chat and Agent modes)
5. Sign in: VS Code will prompt you to authenticate with your GitHub account

**JetBrains (IntelliJ IDEA):**
1. Settings → Plugins → Marketplace
2. Search: `GitHub Copilot`
3. Install and restart
4. Sign in via the Copilot icon in the status bar

**Verify:**
```
# In VS Code, open Command Palette (Ctrl+Shift+P):
GitHub Copilot: Check Status
# Should show: Connected and ready
```

### Step 2: Configure Custom Instructions (FORGE)

Copy `tool-setup/github-copilot/copilot-instructions.md` from this FORGE repository to your project repository:

```bash
mkdir -p .github
cp <forge-repo>/tool-setup/github-copilot/copilot-instructions.md .github/copilot-instructions.md
```

Fill in the `[USER FILLS]` sections for your project. Commit the file — Copilot reads it automatically.

### Step 3: Copy the Context Engine

```bash
cp -r <forge-repo>/.context/ ./.context/
```

In Copilot Chat, reference context files explicitly:
```
Using #file:.context/ATOM_CHASSIS.md and #file:.context/CORE_SKILLS.md,
generate a CardStatusValidator service following FORGE standards.
```

---

## Part 2: Copilot Agent Mode

### What is Agent Mode?

Agent Mode (available in VS Code with Copilot Chat extension v1.250+) lets Copilot operate autonomously across multiple files. Instead of generating one response, it:

1. Plans the task (shows you a list of files it intends to create or modify)
2. Executes across multiple files in sequence
3. Runs terminal commands (tests, builds) and reads the output
4. Fixes errors it encounters without you prompting it again
5. Presents the completed result for your review

This is the equivalent of Claude Code's agentic mode — the right tool for FORGE Stage 3 code generation.

### Enabling Agent Mode

**VS Code:**
1. Open the Copilot Chat panel (Ctrl+Shift+I)
2. In the chat dropdown at the top-left, switch from `Ask` to `Agent`
3. The chat input will show agent-mode indicators (file edit icons, terminal icon)

**Verify agent mode is active:** Type a test prompt. Copilot should respond with a plan listing files it will create/modify before executing.

### Agent Mode with FORGE — Step by Step

#### Before starting an agent session

Ensure your project has:
```
.github/copilot-instructions.md   ← FORGE + ATOM rules (auto-loaded)
.context/ATOM_CHASSIS.md          ← ATOM patterns
.context/CORE_SKILLS.md           ← Security + quality guardrails
analysis/business-rules-register.md  ← Stage 1 output (required for Stage 3)
```

#### FORGE Stage 3 — Full Service Generation in Agent Mode

```
[Agent Mode prompt]

Generate the PaymentAuthorizationService following the FORGE Stage 3 workflow.

Context files to load:
- #file:.context/ATOM_CHASSIS.md
- #file:.context/CORE_SKILLS.md
- #file:analysis/business-rules-register.md

Service specification:
- Name: PaymentAuthorizationService
- Purpose: Evaluates authorization requests and returns approve/decline decisions
- Business rules: BR-001 (card status), BR-002 (expiry), BR-003 (daily limit), BR-004 (velocity)
- Input: POST /v1/authorizations — AuthorizationRequest DTO
- Output: ResponseEntity<ApiResponse<AuthorizationResponse>>
- Downstream calls: cardRepository (DB), limitService (REST, circuit breaker required)

Generate:
1. Full package structure under com.[org].payments.authorization
2. Controller, Application Service, Domain model, Repository interface + JPA implementation
3. Circuit breaker config for limitService in application.yml
4. Unit tests for all 4 business rules (JUnit 5 + Mockito + AssertJ)
5. Testcontainers integration test for the repository

Apply all rules from .github/copilot-instructions.md. After generating, run the tests.
```

Copilot Agent will:
- Show you the plan (file list) before executing — review it
- Create all files in sequence
- Run `./mvnw test` or `./gradlew test` and fix failures automatically
- Present the final diff for your review

#### Reviewing Agent Mode Output

Agent mode output requires the same review gates as any AI-generated code. Before approving the agent's changes:

- [ ] Run `/review-code` in Claude Code against the generated files for a cross-tool second opinion
- [ ] Verify every business rule (BR-NNN) from the register has a corresponding implementation and test
- [ ] Check that all ATOM annotations are applied (`@AtomService`, `@AtomRepository`, etc.)
- [ ] Confirm no security violations (`[SECURITY: SG-N]` annotations from FORGE rules)
- [ ] Verify `@CircuitBreaker` is on every downstream HTTP call

### Agent Mode Limitations vs Claude Code

| Capability | Copilot Agent Mode | Claude Code |
|---|---|---|
| Multi-file generation | Yes | Yes |
| Runs tests and fixes failures | Yes | Yes |
| Custom slash command workflows | No | Yes (FORGE Skills) |
| Structured FORGE stage outputs | Partial — via instructions | Full — via Skills |
| Deep COBOL/legacy analysis | Limited | Strong (COBOL Reading Guide) |
| MCP server integration | No (as of 2026) | Yes — Jira, Confluence, Postgres |
| Reading files > 100KB | Limited | Full |

**Recommended split:** Use Copilot Agent Mode for Stage 3 code generation on well-defined tasks. Use Claude Code for Stage 0/1/2 analysis, complex legacy understanding, and any task requiring MCP server context.

---

## Part 3: Copilot Workspace

### What is Copilot Workspace?

Copilot Workspace (available at github.com — opens from any issue or PR) is a task-based environment that turns a Jira/GitHub issue into a step-by-step implementation plan, then generates code from the plan. Unlike Agent Mode, you review and edit the plan before any code is written.

**Access:**
- GitHub issue page → `Open in Workspace` button (requires Copilot Enterprise or Workspace preview)
- Or directly: github.com/features/copilot/workspace

### FORGE Workflow with Copilot Workspace

#### Step 1: Start from a GitHub Issue

Copilot Workspace works best when the GitHub issue has clear acceptance criteria. Map your Jira tickets to GitHub issues, or write the issue with:

```markdown
## Context
[Brief description of what needs to be built]

## Acceptance Criteria
- [ ] AC-01: The service must validate card status before processing (BR-001)
- [ ] AC-02: Declined authorizations must be persisted for audit trail (BR-005)
- [ ] AC-03: Response time P99 < 150ms
- [ ] AC-04: All amounts stored as minor units (integer cents)

## FORGE Context
- ATOM chassis patterns apply (see .context/ATOM_CHASSIS.md)
- Business rules register: analysis/business-rules-register.md BR-001 through BR-006
- Target package: com.org.payments.authorization
```

#### Step 2: Review the Workspace Plan

Copilot Workspace generates a plan — a list of files to create or modify with a description of each change. **Review this plan carefully before proceeding:**

- Does the plan match the ATOM package structure?
- Are all business rules from the register represented?
- Does the plan include test files?
- Is the plan missing any integration or infrastructure components?

Edit the plan in Workspace before generating code. This is the design review gate equivalent for Copilot Workspace.

#### Step 3: Generate and Validate

After approving the plan, generate the code. Workspace opens a PR-preview with all changes. Before raising the actual PR:

1. Pull the branch locally: `git fetch && git checkout copilot-workspace/[branch]`
2. Run `/review-code` in Claude Code against the generated files
3. Ensure FORGE review gate (governance/review-gates.md) is satisfied
4. Add the `[AI-assisted]` label and AI disclosure to the PR description (per governance/ai-usage-policy.md)

---

## Part 4: Copilot Chat — FORGE Reference Patterns

These are the most effective Copilot Chat patterns for FORGE workflows.

### Loading Context

```
# Load ATOM patterns at the start of a session
I'm working under the FORGE framework. Please load and apply:
#file:.github/copilot-instructions.md
#file:.context/ATOM_CHASSIS.md
#file:.context/CORE_SKILLS.md

Confirm you understand the ATOM annotations and package structure before we begin.
```

### Generating ATOM-Compliant Code

```
Using #file:.context/ATOM_CHASSIS.md, generate a Spring Boot @AtomService class for
limit checking. The service must:
- Accept a LimitCheckRequest and return LimitCheckResult
- Use constructor injection with @RequiredArgsConstructor
- Log with structured key-value pairs using @Slf4j
- Be annotated with @AtomService (not plain @Service)
```

### Code Review

```
Review #file:src/main/java/com/org/payments/AuthorizationService.java against:
- ATOM patterns in #file:.context/ATOM_CHASSIS.md
- Security guardrails in #file:.context/CORE_SKILLS.md
Report findings as CRITICAL / MAJOR / MINOR with line references.
```

### Legacy Explanation (for BAs and PMs)

```
Explain what #file:legacy-artifacts/cobol/AUTHZ0100.cbl does in plain English.
Audience: Business Analyst who understands payments domain but not COBOL.
Focus on: what business decisions the program makes, not the technical implementation.
```

---

## Troubleshooting

| Problem | Fix |
|---|---|
| Copilot not following ATOM patterns | Verify `.github/copilot-instructions.md` is committed and the extension is updated |
| Agent mode not available | Update Copilot Chat extension to v1.250+; ensure Copilot Enterprise or Individual subscription |
| Agent mode creates files in wrong package | Add explicit package name to the prompt: `Target package: com.org.payments.authorization` |
| Copilot ignoring `copilot-instructions.md` | Check file is exactly at `.github/copilot-instructions.md` — not a subfolder |
| Context file not loading | Use `#file:` prefix in Copilot Chat — Copilot does not auto-load `.context/` files |
| Workspace plan doesn't match ATOM structure | Edit the plan in Workspace before generating — add the correct package structure explicitly |
| Agent mode modifying unintended files | Start agent mode with `@workspace` scoped to specific directories: `In src/main/java/com/org/payments/authorization only, ...` |

---

*FORGE GitHub Copilot Setup Guide | Tool Integration | Version 2.0*
*Covers: Copilot Chat, Agent Mode, Copilot Workspace*
