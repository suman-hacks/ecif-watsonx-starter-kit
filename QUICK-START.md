# FORGE Quick Start — 5 Steps to Your AI Engineering Environment

> Get FORGE configured and generating production-ready code in under 15 minutes.

---

## What You Need Before Starting

- [ ] A project repository (local or on GitHub/GitLab/Bitbucket)
- [ ] At least one AI tool installed (see Step 2 for options)
- [ ] Your project type identified: Greenfield | Brownfield | Mainframe Modernization | Cloud Migration | API Modernization
- [ ] Basic knowledge of your target tech stack (Java 17+ / Spring Boot 3.x for ATOM projects)

---

## Step 1: Copy the FORGE Context Engine Into Your Project

The `.context/` folder is the core of FORGE. It tells any AI tool what your standards, ATOM patterns, and migration rules are.

```bash
# From your project repository root:
cp -r /path/to/forge/.context ./

# Verify:
ls .context/
# CORE_SKILLS.md   ATOM_CHASSIS.md   MODERNIZATION.md   .aiconfig
```

**What each file does:**

| File | Load it when... |
|---|---|
| `CORE_SKILLS.md` | Every session — universal security and quality guardrails |
| `ATOM_CHASSIS.md` | Building or reviewing any ATOM microservice |
| `MODERNIZATION.md` | Migrating COBOL/PL1/TIBCO to Java |
| `COBOL_READING_GUIDE.md` | Analyzing or transforming COBOL source code |
| `PAYMENTS_DOMAIN.md` | Any work involving card payments, ISO 8583, or PCI-DSS scope |

**These files are templates.** Open them and customize:
- `ATOM_CHASSIS.md` → update with your organization's specific ATOM version, registry URL, and any custom annotations
- `CORE_SKILLS.md` → add any org-specific security policies or naming conventions

---

## Step 2: Configure Your AI Tool

Choose the tool(s) your team uses. You don't need all of them.

---

### Option A: Claude Code (VS Code or Terminal)

**Best for:** Agentic multi-file analysis, legacy code discovery, complex code generation.

**Step-by-step:**

1. Install Claude Code:
   ```bash
   npm install -g @anthropic-ai/claude-code
   # or install the VS Code extension: "Claude Code" by Anthropic
   ```

2. Copy the CLAUDE.md template to your project root:
   ```bash
   cp /path/to/forge/tool-setup/claude-code/CLAUDE.md ./CLAUDE.md
   ```

3. Open `CLAUDE.md` and fill in every `[USER FILLS]` section:
   - Project name and type
   - Tech stack (Java version, Spring Boot version, database)
   - Architectural patterns (package structure, dependency direction)
   - Repository structure
   - Domain vocabulary (avoid mixing business terms)

4. Copy the Skills catalog to your tool-setup:
   ```bash
   cp /path/to/forge/tool-setup/claude-code/SKILLS.md ./tool-setup/claude-code/SKILLS.md
   # Reference it from CLAUDE.md (already done in the template)
   ```

5. (Optional but recommended) Configure MCP servers to connect Claude Code to Jira, Confluence, and your database:
   ```bash
   # See tool-setup/mcp-servers/setup-guide.md for full configuration
   # Minimum: copy .mcp.json template and add your API tokens
   ```

6. Start Claude Code from your project root:
   ```bash
   claude
   ```

7. Verify setup:
   ```
   Summarize the FORGE constitution rules you are operating under for this project.
   ```
   Claude should describe the 12 principles from `constitution/01-core-principles.md` and your project-specific rules from `CLAUDE.md`.

8. Try your first FORGE skill:
   ```
   /analyze-legacy [paste a COBOL file or describe a legacy component]
   ```

**Commit `CLAUDE.md` and `.mcp.json` (without tokens) to your repository** so all team members get the same Claude behavior automatically.

---

### Option B: VS Code + GitHub Copilot (Chat + Agent Mode)

**Best for:** Inline code completion, chat, and autonomous multi-file generation (Agent Mode). Works alongside Claude Code.

**Step-by-step:**

1. Install GitHub Copilot extension in VS Code (search `GitHub Copilot` in Extensions).

2. Ensure you have a GitHub Copilot license active.

3. Create the `.github/` directory in your project root (if it doesn't exist):
   ```bash
   mkdir -p .github
   ```

4. Copy the FORGE Copilot instructions:
   ```bash
   cp /path/to/forge/tool-setup/github-copilot/copilot-instructions.md .github/copilot-instructions.md
   ```

5. Edit `.github/copilot-instructions.md` — fill in:
   - `## Project Identity` — project name, type, language, framework
   - `## Architectural Patterns` — package structure, data access, error handling
   - `## Domain Context` — business domain, key concepts, key business rules

6. In VS Code Copilot Chat, reference context files:
   ```
   Using #file:.context/ATOM_CHASSIS.md, create a new service class for payment authorization.
   ```

7. For multi-file autonomous generation, switch to **Agent Mode** in the Copilot Chat dropdown (`Ask` → `Agent`). See [tool-setup/github-copilot/setup-guide.md](tool-setup/github-copilot/setup-guide.md) for Agent Mode and Copilot Workspace workflow.

8. Verify:
   ```
   @workspace What FORGE rules are you operating under for this project?
   ```

**Commit `.github/copilot-instructions.md`** so every team member automatically gets the same Copilot behavior.

---

### Option C: IntelliJ IDEA / JetBrains AI Assistant

**Best for:** Java development in IntelliJ IDEA. Full IDE integration for ATOM service generation.

**Step-by-step:**

1. Install the **JetBrains AI Assistant** plugin:
   - IntelliJ IDEA: **File → Settings → Plugins** → search "AI Assistant" → Install
   - Or: Install the **GitHub Copilot** plugin for IntelliJ

2. Configure AI context (AI Assistant):
   - Open **AI Assistant** panel (right sidebar)
   - Click the **gear icon → Customize AI Assistant**
   - Paste the contents of `.context/CORE_SKILLS.md` into the **System Prompt** field
   - Add to the system prompt:
     ```
     Also apply the ATOM chassis patterns from .context/ATOM_CHASSIS.md.
     ```

3. Configure Copilot for IntelliJ (alternative):
   - Install the GitHub Copilot plugin
   - The `.github/copilot-instructions.md` file from Step B is read automatically
   - In Copilot Chat, reference context with: `#file:.context/ATOM_CHASSIS.md`

4. For Claude Code + IntelliJ (recommended setup):
   - Install the **Claude Code** VS Code extension — OR —
   - Run `claude` from the terminal in your project root alongside IntelliJ
   - Use Claude Code in the terminal for complex analysis; IntelliJ AI for in-IDE completions

5. Verify in the AI Assistant chat:
   ```
   I am working on an ATOM Spring Boot service. What architecture layers should I use
   and what annotations are required? Reference .context/ATOM_CHASSIS.md.
   ```

See the full JetBrains setup guide: [tool-setup/jetbrains-ai/setup-guide.md](tool-setup/jetbrains-ai/setup-guide.md)

---

### Option D: Cursor

**Best for:** Engineers who prefer Cursor as their primary IDE.

**Step-by-step:**

1. Install [Cursor](https://cursor.sh) and open your project.

2. Copy the FORGE Cursor rules:
   ```bash
   cp /path/to/forge/tool-setup/cursor/cursorrules.md .cursorrules
   ```

3. Edit `.cursorrules` — fill in project-specific sections.

4. In Cursor Composer (Cmd/Ctrl+I), reference context:
   ```
   @.context/ATOM_CHASSIS.md Generate a new ATOM service for fraud scoring.
   ```

5. Verify:
   ```
   What FORGE and ATOM rules are you applying to this project?
   ```

---

### Option E: IBM watsonx Code Assist

**Best for:** IBM-licensed shops, especially for COBOL/z/OS analysis with watsonx for Z.

**For distributed (Java/Spring) projects:**
1. Install the IBM watsonx Code Assist extension in VS Code
2. Open extension settings → **Custom Instructions**
3. Paste the contents of `.context/CORE_SKILLS.md` and `.context/ATOM_CHASSIS.md` into the Custom Instructions field
4. For each new prompt, begin with: `Apply the ATOM and FORGE standards I provided in your custom instructions.`

**For mainframe (COBOL/z/OS) projects:**
Follow the detailed guide: [tool-setup/watsonx-code-assist/for-z-mainframe/setup-guide.md](tool-setup/watsonx-code-assist/for-z-mainframe/setup-guide.md)

---

### Option F: Web Portal (No IDE Required)

**Best for:** Product managers, business analysts, workshop facilitators, and anyone who doesn't use an IDE.

```bash
open /path/to/forge/web-ui/index.html
```

Or deploy to GitHub Pages for team-wide browser access.

See: [web-ui/README.md](web-ui/README.md)

---

## Step 3: Identify Your Project Type and Load Context

Run this decision tree to identify the right project context files to load:

```
Does your project have existing legacy code to preserve or transform?
│
├── YES
│   ├── Is the code COBOL, PL/1, Assembler, or JCL on z/OS (mainframe)?
│   │   ├── YES → MAINFRAME MODERNIZATION
│   │   │         Load: .context/MODERNIZATION.md + .context/ATOM_CHASSIS.md
│   │   │               + .context/COBOL_READING_GUIDE.md
│   │   │         If payments domain: + .context/PAYMENTS_DOMAIN.md
│   │   │         Context: project-contexts/mainframe-modernization/
│   │   └── NO  → Is the goal to move it to a cloud platform?
│   │             ├── YES → CLOUD MIGRATION
│   │             │         Load: .context/CORE_SKILLS.md + .context/ATOM_CHASSIS.md
│   │             │         Context: project-contexts/cloud-migration/
│   │             └── NO  → BROWNFIELD
│   │                       Load: .context/CORE_SKILLS.md + .context/ATOM_CHASSIS.md
│   │                       Context: project-contexts/brownfield/
└── NO (starting fresh)
    ├── Is this a payments / card / PCI-DSS project?
    │   ├── YES → load .context/PAYMENTS_DOMAIN.md (regardless of project type)
    ├── Exposing APIs over legacy data?
    │   ├── YES → API MODERNIZATION
    │   │         Load: .context/CORE_SKILLS.md + .context/ATOM_CHASSIS.md
    │   └── NO  → GREENFIELD
    │             Load: .context/CORE_SKILLS.md + .context/ATOM_CHASSIS.md
    │             Context: project-contexts/greenfield/
```

Add the relevant project context to your `CLAUDE.md` or `.github/copilot-instructions.md`:
```bash
# Add to your AI tool config file:
# "For project-specific patterns, see project-contexts/mainframe-modernization/context.md"
```

---

## Step 4: Load the FORGE Constitution

**Mandatory for every AI session.**

The constitution (`constitution/01-core-principles.md`) defines 12 rules that prevent the most expensive AI mistakes.

**Claude Code:** The constitution is embedded in `CLAUDE.md` — loaded automatically every session.

**GitHub Copilot:** The key rules are in `.github/copilot-instructions.md` — loaded automatically.

**Cursor:** Loaded via `.cursorrules`.

**Other tools / web sessions:** Open `constitution/01-core-principles.md` and paste as the first message:
```
Apply these 12 principles to every response in this session:
[paste constitution content]
```

---

## Step 5: Run Your First FORGE Workflow

### For Claude Code users:

```bash
claude
```
Then type one of these to verify everything is working:
```
/analyze-legacy [paste a COBOL snippet or describe a legacy program]
```
```
/generate-atom-service
Service name: TransactionLimitService
Business purpose: Checks whether a transaction amount exceeds configured spending limits
API: POST /v1/limits/check — accepts TransactionRequest, returns LimitCheckResult
Integrations: Reads limit config from PostgreSQL via JPA
```

### For GitHub Copilot / JetBrains users:

Open the Chat panel and type:
```
Using #file:.context/ATOM_CHASSIS.md and #file:.context/CORE_SKILLS.md,
generate a Spring Boot service class for checking transaction spending limits.
The service should use @AtomService, @CircuitBreaker for downstream calls,
and structured logging. Include unit tests.
```

### For web portal users:

1. Open `web-ui/index.html` in your browser
2. Navigate to **Developer → Generate ATOM Service from Spec**
3. Click **Copy Prompt**
4. Paste into Claude.ai, watsonx, or any AI chat interface
5. Fill in the `[BRACKETED]` sections

---

## First Session Template

Use this opening message for any new AI session (works in all tools):

```
You are operating as an AI engineering assistant under the FORGE framework.

CONTEXT FILES LOADED:
- .context/CORE_SKILLS.md — engineering guardrails (security, quality, testing)
- .context/ATOM_CHASSIS.md — ATOM microservices chassis patterns
[- .context/MODERNIZATION.md — if this is a legacy modernization project]
[- .context/COBOL_READING_GUIDE.md — if analyzing COBOL source code]
[- .context/PAYMENTS_DOMAIN.md — if this is a payments/card/PCI-DSS project]

PROJECT CONTEXT:
- Project name: [PROJECT NAME]
- Project type: [Greenfield | Brownfield | Mainframe Modernization | Cloud Migration]
- Primary language: Java 17 / Spring Boot 3.x (ATOM)
- Current SDLC stage: [Stage N: STAGE NAME]
- My role: [Developer | Architect | QA | DevSecOps | etc.]

FORGE CONSTITUTION: Apply all 12 principles from constitution/01-core-principles.md.
Especially: never invent business rules, separate facts from assumptions,
flag ambiguity before generating code, and always generate tests alongside code.

TASK:
[Describe what you need to accomplish in this session. Be specific.
Reference any legacy source files, business rules, or specifications by name.]

Confirm you understand the rules and ask any clarifying questions before starting.
```

---

## Common Setup Mistakes

| Mistake | Consequence | Fix |
|---|---|---|
| Not copying `.context/` into the project repo | AI generates generic code, ignores ATOM patterns | Copy `.context/` first — always |
| Skipping `[USER FILLS]` sections in CLAUDE.md | AI uses wrong package names, patterns | Fill in all sections before first use |
| Not committing config files | Other team members get different AI behavior | Commit `CLAUDE.md`, `.github/copilot-instructions.md` |
| Loading context once then skipping it | Each session starts without FORGE guardrails | Load constitution at the start of every new session |
| Generating code before analysis | Misses embedded business rules | Complete Stage 1 (Discovery/Analysis) before Stage 5 (Development) |
| Pasting production data into prompts | Data leakage, compliance violation | Anonymize all data. See `governance/ai-usage-policy.md` — never use real PAN, CVV, or customer records |
| Putting API tokens directly in `.mcp.json` | Credentials committed to git | Always use `${ENV_VAR}` references — put actual values in `.env.local` which is git-ignored |

---

## Next Steps

1. **Run the pre-engagement analysis** on your target codebase — [sdlc/00-pre-engagement/](sdlc/00-pre-engagement/README.md)
2. **Pick your persona** — open your role-specific prompt pack from [personas/](personas/README.md)
3. **Save outputs as artifacts** — use templates in [templates/](templates/) to capture assumptions, decisions, traceability
4. **Review before merge** — use checklists in [governance/review-gates.md](governance/review-gates.md)
5. **Set up MCP servers** — connect Claude Code to Jira, Confluence, and your database for live context — [tool-setup/mcp-servers/setup-guide.md](tool-setup/mcp-servers/setup-guide.md)
6. **Read the AI usage policy** — understand data classification, code disclosure rules, and compliance requirements — [governance/ai-usage-policy.md](governance/ai-usage-policy.md)
7. **Customize ATOM_CHASSIS.md** — add your organization's specific ATOM version, registry URL, and custom annotations

---

*FORGE v2.0 — Quick Start Guide*
