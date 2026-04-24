# FORGE Tool Setup — AI Tool Selection and IDE Integration Guide

> Choose your AI tool(s), configure FORGE guardrails and ATOM patterns, and start generating production-ready code.

---

## Quickest Path: Pick Your IDE

| My IDE | My AI Tool | Start Here |
|---|---|---|
| **VS Code** | Claude Code | [Option 1](#option-1-vs-code--claude-code) |
| **VS Code** | GitHub Copilot | [Option 2](#option-2-vs-code--github-copilot) |
| **VS Code** | Both (recommended) | [Options 1+2 together](#using-claude-code--copilot-together) |
| **IntelliJ IDEA** | JetBrains AI | [Option 3](#option-3-intellij--jetbrains-ai-assistant) |
| **IntelliJ IDEA** | GitHub Copilot | [Option 4](#option-4-intellij--github-copilot-plugin) |
| **IntelliJ IDEA** | Claude Code (terminal) | [Option 5](#option-5-any-ide--claude-code-terminal) |
| **Any IDE** | Claude Code (terminal) | [Option 5](#option-5-any-ide--claude-code-terminal) |
| **Cursor** | Cursor built-in | [Option 6](#option-6-cursor) |
| **No IDE (browser)** | Claude.ai / watsonx | [Web Portal](#option-7-web-portal-no-ide-required) |

---

## How Every Option Works

Regardless of which tool you choose, the setup follows the same pattern:

```
Step 1: Copy .context/ into your project repo
Step 2: Copy the tool-specific config file into your project repo
Step 3: Fill in [USER FILLS] sections with your project details
Step 4: Commit both files to git (whole team benefits automatically)
Step 5: Open your IDE/tool — context is loaded automatically
```

The `.context/` folder is the core — it teaches any AI tool about ATOM patterns, security guardrails, and migration rules. The tool-specific config file (CLAUDE.md, copilot-instructions.md, .cursorrules) is the delivery mechanism.

---

## Option 1: VS Code + Claude Code

**Best for:** Deep codebase analysis, multi-file generation, FORGE Skills (/analyze-legacy, /generate-service, etc.)

**What you get:**
- FORGE slash commands that trigger structured workflows
- Automatic CLAUDE.md loading with ATOM patterns
- Multi-file analysis and generation
- Full project context awareness

**Setup:**
```bash
# 1. Install Claude Code
npm install -g @anthropic-ai/claude-code
# or install "Claude Code" VS Code extension by Anthropic

# 2. Copy FORGE config to project root
cp /path/to/forge/tool-setup/claude-code/CLAUDE.md ./CLAUDE.md
cp -r /path/to/forge/.context ./

# 3. Fill in [USER FILLS] sections in CLAUDE.md
code CLAUDE.md

# 4. Commit both files
git add CLAUDE.md .context/ .github/
git commit -m "Add FORGE AI engineering configuration"

# 5. Start Claude Code from project root
claude
```

**Verify:** Type: `Summarize the FORGE constitution rules you are operating under.`

**Config file:** [claude-code/CLAUDE.md](claude-code/CLAUDE.md)
**Skills catalog:** [claude-code/SKILLS.md](claude-code/SKILLS.md)

---

## Option 2: VS Code + GitHub Copilot

**Best for:** Inline code completions and Copilot Chat while editing in VS Code.

**What you get:**
- Inline suggestions that follow ATOM patterns
- Copilot Chat with FORGE rules pre-loaded
- `#file:` references to `.context/` files for targeted generation

**Setup:**
```bash
# 1. Install GitHub Copilot extension in VS Code
# Extensions → search "GitHub Copilot" → Install

# 2. Copy FORGE config to project
mkdir -p .github
cp /path/to/forge/tool-setup/github-copilot/copilot-instructions.md \
   .github/copilot-instructions.md
cp -r /path/to/forge/.context ./

# 3. Fill in [USER FILLS] sections
code .github/copilot-instructions.md

# 4. Commit
git add .github/ .context/
git commit -m "Add FORGE Copilot configuration"
```

**Using in Copilot Chat (VS Code):**
```
# Reference context files in your request:
Using #file:.context/ATOM_CHASSIS.md, generate a new @AtomService for payment limits.

# Check what rules are loaded:
@workspace What FORGE rules are you following for this project?
```

**Verify:** Open Copilot Chat → ask: `What ATOM annotations must I use for a service class?`

**Agent Mode:** For autonomous multi-file code generation (FORGE Stage 3), switch to Agent Mode in the Copilot Chat panel. See [github-copilot/setup-guide.md](github-copilot/setup-guide.md) for the full Agent Mode + Copilot Workspace guide.

**Config file:** [github-copilot/copilot-instructions.md](github-copilot/copilot-instructions.md) | **Full guide:** [github-copilot/setup-guide.md](github-copilot/setup-guide.md)

---

## Using Claude Code + Copilot Together

This is the recommended setup for most VS Code engineers on ATOM projects:

```
VS Code
├── Claude Code extension (or run `claude` in integrated terminal)
│   → Complex analysis, legacy discovery, full service generation
│   → FORGE Skills: /analyze-legacy, /generate-service, /review-code
└── GitHub Copilot extension
    → Inline completion while typing
    → Quick questions in Copilot Chat
    → #file: references for targeted context
```

Both tools read from the same `.context/` files and respect the same FORGE rules. There is no conflict.

---

## Option 3: IntelliJ + JetBrains AI Assistant

**Best for:** Java engineers using IntelliJ IDEA who have a JetBrains AI subscription.

**What you get:**
- Native IntelliJ AI integration
- Code completion with FORGE + ATOM guardrails
- AI chat panel in the IDE

**Setup:**
```
1. Install: File → Settings → Plugins → search "AI Assistant" → Install
2. Configure system prompt: AI Assistant panel → ⚙ → Modify AI Instructions
   (Paste the FORGE + ATOM system prompt from the JetBrains setup guide)
3. Copy .context/ into your project root
4. At the start of each session: paste .context/ATOM_CHASSIS.md into the chat
```

**Full guide:** [jetbrains-ai/setup-guide.md](jetbrains-ai/setup-guide.md)

---

## Option 4: IntelliJ + GitHub Copilot Plugin

**Best for:** IntelliJ engineers with GitHub Copilot licenses.

**Setup:**
```
1. Install: File → Settings → Plugins → search "GitHub Copilot" → Install
2. Copy .github/copilot-instructions.md (FORGE template) into your project
3. The plugin reads it automatically — no further setup needed
```

**Full guide:** [jetbrains-ai/setup-guide.md](jetbrains-ai/setup-guide.md)

---

## Option 5: Any IDE + Claude Code (Terminal)

**Best for:** Engineers using IntelliJ, PyCharm, WebStorm, or any other IDE who want FORGE Skills.

Claude Code runs in the terminal alongside any IDE — no editor integration needed.

```bash
# In a terminal, from your project root:
claude

# Available FORGE Skills:
/analyze-legacy    # Analyze COBOL/legacy code
/generate-service  # Generate complete ATOM service + tests
/review-code       # Review code against FORGE + ATOM standards
/create-tests      # Generate JUnit 5 + Testcontainers test suite
/map-data          # COBOL → Java domain model mapping
/create-adr        # Generate Architecture Decision Record
```

**Workflow:** Run Claude Code in a terminal split-screen alongside your IDE. Claude generates files; your IDE is used for browsing, running tests, and debugging.

**Config file:** [claude-code/CLAUDE.md](claude-code/CLAUDE.md)
**Skills catalog:** [claude-code/SKILLS.md](claude-code/SKILLS.md)

---

## Option 6: Cursor

**Best for:** Engineers who use Cursor as their primary IDE.

**Setup:**
```bash
# Install Cursor from https://cursor.sh

# Copy FORGE config
cp /path/to/forge/tool-setup/cursor/cursorrules.md .cursorrules
cp -r /path/to/forge/.context ./

# Fill in [USER FILLS] sections
open .cursorrules

# In Cursor Composer (Cmd+I):
@.context/ATOM_CHASSIS.md Generate a new ATOM service for fraud scoring.
```

**Config file:** [cursor/cursorrules.md](cursor/cursorrules.md)

---

## Option 7: Web Portal (No IDE Required)

**Best for:** Product managers, business analysts, workshop facilitators, and non-IDE users.

```bash
open /path/to/forge/web-ui/index.html
```

Or deploy to GitHub Pages for team-wide access — see [web-ui/README.md](../web-ui/README.md).

The portal provides all FORGE prompts organized by persona and SDLC stage, with one-click copy to paste into any web-based AI tool (Claude.ai, ChatGPT, watsonx Prompt Lab).

---

## IBM watsonx Code Assist

**For distributed (Java/Spring Boot) projects:**

```
1. Install: VS Code → Extensions → search "IBM watsonx Code Assist" → Install
2. Open extension settings → "Custom Instructions"
3. Paste content from .context/CORE_SKILLS.md + .context/ATOM_CHASSIS.md
4. For each prompt, begin with: "Apply the FORGE and ATOM standards I provided."
```

**For mainframe (COBOL/z/OS) projects:**
Full guide: [watsonx-code-assist/for-z-mainframe/setup-guide.md](watsonx-code-assist/for-z-mainframe/setup-guide.md)

---

## AWS Q Developer

**Setup:**
```
1. Install: VS Code/IntelliJ → Extensions → search "AWS Q Developer"
2. Authenticate with AWS Builder ID or IAM Identity Center
3. In Q Chat, paste .context/CORE_SKILLS.md as the opening message
4. Load cloud migration context from project-contexts/cloud-migration/
```

**Config guide:** [aws-q-developer/setup-guide.md](aws-q-developer/setup-guide.md)

---

## Tool Capability Comparison

| Capability | Claude Code | GitHub Copilot | JetBrains AI | Cursor | watsonx |
|---|---|---|---|---|---|
| FORGE slash commands (`/analyze-legacy`) | ✅ Native | ❌ | ❌ | ❌ | ❌ |
| Auto-loads CLAUDE.md / instructions file | ✅ | ✅ | ⚠️ Manual | ✅ | ⚠️ Manual |
| Full repo context | ✅ | ✅ (@workspace) | ⚠️ Limited | ✅ (@codebase) | ⚠️ Limited |
| COBOL/z/OS analysis | ✅ | ⚠️ Limited | ❌ | ⚠️ Limited | ✅ (watsonx Z) |
| Multi-file generation | ✅ | ⚠️ Limited | ⚠️ Limited | ✅ | ❌ |
| In-IDE inline completion | ✅ (VS Code) | ✅ | ✅ | ✅ | ✅ |
| Enterprise security/air-gap | ⚠️ Check | ⚠️ Check | ⚠️ Check | ⚠️ Check | ✅ IBM |

---

## Files Required in Your Project Repository

After setup, your project repo should have:

```
your-project/
├── .context/                    ← From FORGE (copy entire folder)
│   ├── CORE_SKILLS.md
│   ├── ATOM_CHASSIS.md
│   ├── MODERNIZATION.md        ← Add if migrating from legacy
│   ├── COBOL_READING_GUIDE.md  ← Add if working with COBOL
│   └── PAYMENTS_DOMAIN.md      ← Add if working with payments/cards
├── CLAUDE.md                   ← If using Claude Code
├── .mcp.json                   ← If using MCP servers with Claude Code (see mcp-servers/)
├── .github/
│   └── copilot-instructions.md ← If using GitHub Copilot
└── .cursorrules                ← If using Cursor
```

**Commit all of these files.** The whole team gets the same AI behavior automatically.

> **MCP Servers (Claude Code):** Connect Claude Code directly to Jira, Confluence, GitHub, and your database. See [mcp-servers/setup-guide.md](mcp-servers/setup-guide.md) — this is the highest-leverage setup for teams doing modernization work.

---

*FORGE v2.0 — Tool Setup Guide*
*For detailed guides: see the subdirectory for your specific tool.*
