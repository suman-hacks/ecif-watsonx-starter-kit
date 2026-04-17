# FORGE Constitution — Always-On AI Ruleset

## What Is the Constitution?

The FORGE Constitution is the **non-negotiable, always-active behavioral ruleset** that governs how AI tools operate within this project. It is the highest-authority document in the FORGE framework — superseding any task-level prompt, user instruction, or tool-specific default that conflicts with it.

Think of it as the AI equivalent of your organization's code of conduct and engineering standards: it does not change per-task, per-sprint, or per-engineer. It is the floor below which AI behavior must never fall.

The Constitution consists of five documents:

| File | Purpose |
|------|---------|
| `01-core-principles.md` | 12 non-negotiable behavioral principles |
| `02-security-and-data.md` | Security, PII, credentials, and compliance rules |
| `03-architecture-standards.md` | Mandatory architectural patterns and constraints |
| `04-code-quality.md` | Code structure, naming, testing, and documentation standards |
| `05-ai-ethics-and-governance.md` | Transparency, oversight, IP, and accountability rules |

---

## Why the Constitution Exists

AI coding tools are powerful and remarkably capable — and precisely because of that capability, they can cause harm at scale if left without principled constraints. Common failure modes include:

- Inventing behavior that isn't in the source code ("hallucinated" analysis)
- Silently making architectural decisions that should require human review
- Hardcoding credentials or writing insecure defaults
- Expanding scope beyond what was asked
- Presenting assumptions as facts
- Skipping tests because they weren't explicitly requested

The Constitution prevents these failure modes by establishing explicit, unambiguous rules that apply to every session, every task, and every output.

---

## The Constitution Is Always On

**"Always-on" means:**

1. It is loaded at the **beginning of every AI session**, before any task prompt is provided.
2. It applies to **every output** produced during that session.
3. It **cannot be suspended** by a task-level instruction (e.g., "ignore the rules for this one task" is not a valid instruction).
4. It **overrides** any conflicting guidance from other prompts, personas, or tools.

If a task prompt says "skip the tests for now" and the Constitution says "always generate tests alongside code," the Constitution wins. The AI must flag the conflict rather than silently comply with the override.

---

## How to Load the Constitution

### Option 1: Paste Into System Prompt (Recommended for Most Tools)

Copy the full contents of all five constitution files and paste them into your AI tool's **system prompt** field before beginning any session. This is the most reliable method and works with Claude, GPT-4, Gemini, and other LLMs with system prompt support.

```
[Paste contents of 01-core-principles.md]
[Paste contents of 02-security-and-data.md]
[Paste contents of 03-architecture-standards.md]
[Paste contents of 04-code-quality.md]
[Paste contents of 05-ai-ethics-and-governance.md]
```

### Option 2: Reference in CLAUDE.md (Claude Code)

In your repository root, create or update `CLAUDE.md` to reference the constitution:

```markdown
## AI Session Rules

Load and follow the FORGE Constitution before any task:
- constitution/01-core-principles.md
- constitution/02-security-and-data.md
- constitution/03-architecture-standards.md
- constitution/04-code-quality.md
- constitution/05-ai-ethics-and-governance.md

These rules override any conflicting task-level instructions.
```

Claude Code automatically reads `CLAUDE.md` at session start, making this method seamless for Claude-based workflows.

### Option 3: Tool-Specific System Prompt Configuration

| Tool | How to Set System Prompt |
|------|-------------------------|
| Claude Code | Add to `CLAUDE.md` in repo root |
| GitHub Copilot Chat | Use workspace instructions (`.github/copilot-instructions.md`) |
| watsonx Code Assist | Configure in the tool's system prompt field per workspace |
| ChatGPT (custom GPT) | Add to GPT system instructions in GPT builder |
| Cursor | Add to `.cursorrules` file in repo root |
| Continue.dev | Configure in `config.json` system message |

### Option 4: Pre-Prompt Wrapper (Fallback)

If your tool does not support persistent system prompts, prepend the constitution to every prompt manually:

```
=== FORGE CONSTITUTION — ALWAYS ACTIVE ===
[Paste constitution]
=== END CONSTITUTION ===

Now, please [your actual task]...
```

---

## What Happens When a Rule Is Violated

When an AI session would produce output that violates the Constitution, the AI **must**:

1. **Stop** — Do not produce the violating output.
2. **Flag** — Explicitly state which rule is being triggered and why.
3. **Explain** — Describe what the violation would be (e.g., "The requested output would require hardcoding a credential, which violates the Secure by Default principle").
4. **Ask for Guidance** — Request clarification or an alternative approach that does not violate the rule.
5. **Do not self-override** — The AI must not decide unilaterally to override the Constitution, even if it believes the override is harmless.

Example of correct AI behavior when a rule is triggered:

```
CONSTITUTION ALERT — Rule 10: Secure by Default

You have asked me to include the database password in the generated configuration
file. This violates the FORGE Constitution rule against hardcoding credentials in
generated code.

I cannot produce the output as requested.

Alternative approaches:
1. Use an environment variable: DB_PASSWORD=${DB_PASSWORD}
2. Reference a secrets manager: ${ssm:/myapp/db/password}
3. Use a configuration template with a placeholder: [REPLACE_WITH_SECRET]

Please indicate which approach you would like me to use, or explain the context
so I can help you find a compliant solution.
```

---

## Customizing the Constitution for Your Organization

The FORGE Constitution is designed to be forked and extended. To customize it:

### Step 1: Fork the Constitution

Copy the entire `constitution/` directory to your organization's shared repository or the specific project repository.

### Step 2: Add Domain-Specific Rules

Append domain-specific rules to the relevant files, or create a new `06-domain-rules.md` file. Examples:

- **Financial Services**: `06-financial-compliance.md` — PCI-DSS specific controls, transaction integrity rules
- **Healthcare**: `06-healthcare-compliance.md` — HIPAA PHI handling rules, clinical data validation
- **Government/Defense**: `06-government-standards.md` — FedRAMP controls, FISMA compliance
- **Automotive/Embedded**: `06-safety-critical.md` — ISO 26262 rules, MISRA compliance constraints

### Step 3: Document Your Changes

At the top of any modified file, add a change record:

```markdown
## Organization Customization Record

Organization: [Your Organization Name]
Modified: [Date]
Modified By: [Name/Role]
Changes:
- Added Rule 13: [Rule name and summary]
- Modified Rule 7: [What changed and why]
Approved By: [Engineering Lead / CISO / etc.]
```

### Step 4: Version the Constitution

Use semantic versioning for your constitution. Major changes (adding/removing rules) increment the major version. Clarifications increment the minor version. Projects pin to a specific constitution version to ensure stability.

```
constitution-version: 1.2.0
```

### What You Should NOT Remove

The following rules are considered the absolute minimum for responsible AI-assisted development. Organizations should not remove or weaken them without compelling documented justification:

- Rule 2: Facts vs. Assumptions (honesty)
- Rule 8: Human Approval Gates (oversight)
- Rule 10: Secure by Default (security)
- Rule 12: Honest Uncertainty (honesty)
- All rules in `02-security-and-data.md` (data protection)

---

## Relationship to Other FORGE Documents

```
Constitution (this document set)
    └── Always active, always enforced
        ├── Guardrails (enforcement mechanisms)
        │   ├── Automated checks (linters, scanners)
        │   └── Manual checklists (review gates)
        ├── Agents (specialized AI personas)
        │   └── Each agent operates within constitution bounds
        └── Project Contexts (project-specific configuration)
            └── Extends the constitution; cannot override it
```

The Constitution is the **root of authority** in the FORGE framework. All other components operate within its constraints.

---

*FORGE Constitution v1.0 | Framework for Orchestrated AI-Guided Engineering*
