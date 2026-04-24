# FORGE — AI-Powered Framework for Orchestrated, Reliable Guided Engineering

> **The Enterprise AI Engineering Framework**
> AI-tool agnostic. ATOM-native. Production-ready prompts from requirement to release.

---

## What is FORGE?

FORGE is a structured, enterprise-grade framework that provides:

- **A Context Engine** — drop `.context/` into any project repo to instantly ground any AI tool in your standards, ATOM patterns, and migration rules
- **SDLC-stage prompts** — production-ready prompts for every phase: pre-engagement discovery through operations
- **Role-specific guidance** — prompts tailored to each engineering persona
- **Tool configurations** — ready-to-use setup files for Claude Code, GitHub Copilot, JetBrains AI, Cursor, watsonx, and AWS Q Developer
- **Claude Skills** — slash commands that trigger FORGE workflows with a single command
- **A web portal** — browser-based prompt access for non-IDE personas and workshops
- **Governance artifacts** — constitution, review gates, and audit trail templates

FORGE does not replace your engineers or your tools. It provides the structure that prevents AI from guessing, hallucinating, and short-cutting — and channels it toward precise, traceable, production-quality output.

---

## How FORGE and ATOM Work Together

FORGE is the **process and prompt framework** — it defines *how* engineers use AI.
ATOM is the **microservices chassis** — it defines *what* engineers build.

The `.context/` folder is the bridge:

```
Your Project Repository
├── .context/
│   ├── CORE_SKILLS.md     ← Universal engineering guardrails (security, quality, testing)
│   ├── ATOM_CHASSIS.md    ← ATOM annotations, layers, patterns, config templates
│   └── MODERNIZATION.md   ← COBOL→Java mapping, Strangler Fig, behavioral preservation
├── CLAUDE.md              ← Claude Code configuration (from FORGE tool-setup/)
├── .github/
│   └── copilot-instructions.md  ← GitHub Copilot configuration (from FORGE)
└── ... your service code ...
```

When any AI tool is asked to generate or review code:
1. It reads your `.context/` files — understanding ATOM patterns, guardrails, migration rules
2. It generates code that uses `@AtomService`, `@CircuitBreaker`, `ApiResponse<T>`, structured logging
3. It flags security violations, missing tests, and behavioral deviations automatically

**Result:** Every engineer — regardless of IDE or AI tool — produces code that conforms to ATOM and passes FORGE review gates.

---

## Repository Structure

```
forge/
├── README.md                     ← This file — framework overview
├── QUICK-START.md                ← 5-step setup guide
│
├── .context/                     ← THE CONTEXT ENGINE — copy into every project
│   ├── CORE_SKILLS.md            ← Universal security, quality, and testing standards
│   ├── ATOM_CHASSIS.md           ← ATOM microservices chassis reference
│   ├── MODERNIZATION.md          ← Legacy-to-modern migration patterns
│   ├── COBOL_READING_GUIDE.md    ← COBOL analysis guide (mainframe projects)
│   ├── PAYMENTS_DOMAIN.md        ← Payments domain glossary, ISO 8583, PCI-DSS rules
│   └── .aiconfig                 ← JSON config for context file auto-loading
│
├── constitution/
│   └── 01-core-principles.md    ← 12 non-negotiable AI behavior rules (load every session)
│
├── sdlc/                         ← Prompts organized by SDLC stage
│   ├── 00-pre-engagement/        ← 5-task AI-accelerated legacy discovery workflow
│   ├── 01-discovery/             ← Stakeholder interviews, system discovery
│   ├── 02-requirements/          ← User stories, acceptance criteria, NFRs
│   ├── 03-architecture/          ← ADRs, service decomposition, API design
│   ├── 04-design/                ← Detailed design, data models, sequence diagrams
│   ├── 05-development/           ← Code generation, code review, legacy analysis
│   ├── 06-testing/               ← Test strategy, test generation, UAT
│   ├── 07-security/              ← Threat modeling, security review, compliance
│   ├── 08-deployment/            ← CI/CD, IaC, rollback strategy
│   └── 09-operations/            ← Runbooks, incident analysis, observability
│
├── personas/                     ← Role-specific prompt packs
│   ├── developer/
│   ├── solution-architect/
│   ├── qa-engineer/
│   ├── devsecops/
│   ├── product-manager/
│   ├── business-analyst/
│   └── lead-engineer/
│
├── project-contexts/             ← Project-type specific context files
│   ├── greenfield/
│   ├── brownfield/
│   ├── mainframe-modernization/
│   ├── cloud-migration/
│   └── api-modernization/
│
├── tool-setup/                   ← AI tool configuration files and setup guides
│   ├── README.md                 ← Master IDE + AI tool integration guide
│   ├── claude-code/
│   │   ├── CLAUDE.md             ← Template: copy to project root as CLAUDE.md
│   │   └── SKILLS.md             ← 13 Claude slash commands catalog
│   ├── github-copilot/
│   │   ├── copilot-instructions.md  ← Template: copy to .github/copilot-instructions.md
│   │   └── setup-guide.md        ← Chat, Agent Mode, and Copilot Workspace setup
│   ├── mcp-servers/
│   │   └── setup-guide.md        ← Connect Claude Code to Jira, Confluence, GitHub, Postgres
│   ├── cursor/
│   │   └── cursorrules.md        ← Template: copy to .cursorrules
│   ├── jetbrains-ai/
│   │   └── setup-guide.md        ← Step-by-step JetBrains AI + ATOM setup
│   └── watsonx-code-assist/
│       ├── for-distributed/
│       └── for-z-mainframe/
│
├── governance/                   ← Review gates, audit trail, AI usage policy
├── templates/                    ← ADR, OpenAPI spec, user story, test plan templates
├── web-ui/                       ← Browser-based prompt portal (no IDE required)
│   ├── index.html                ← Self-contained web app — open in any browser
│   └── README.md                 ← Deployment guide (local / GitHub Pages / internal)
└── repo-template/                ← Starter structure for a new ATOM modernization project
```

---

## Quick Navigation

| I am a... | Go here |
|---|---|
| **Developer** — generating ATOM services or analyzing legacy code | [personas/developer/](personas/developer/README.md) |
| **Solution Architect** — designing target-state or writing ADRs | [personas/solution-architect/](personas/solution-architect/README.md) |
| **Business Analyst** — extracting rules or writing user stories | [personas/business-analyst/](personas/business-analyst/README.md) |
| **QA Engineer** — building test strategy or generating tests | [personas/qa-engineer/](personas/qa-engineer/README.md) |
| **DevSecOps** — threat modeling or security review | [personas/devsecops/](personas/devsecops/README.md) |
| **Product Manager** — defining features or communicating roadmap | [personas/product-manager/](personas/product-manager/README.md) |
| **Lead Engineer** — review gates, governance, ADR approval | [personas/lead-engineer/](personas/lead-engineer/README.md) |
| **Running a discovery workshop** | [sdlc/00-pre-engagement/](sdlc/00-pre-engagement/README.md) |
| **Setting up VS Code with Claude Code** | [tool-setup/README.md](tool-setup/README.md#vs-code--claude-code) |
| **Setting up VS Code with GitHub Copilot** | [tool-setup/github-copilot/setup-guide.md](tool-setup/github-copilot/setup-guide.md) |
| **Using Copilot Agent Mode or Workspace** | [tool-setup/github-copilot/setup-guide.md](tool-setup/github-copilot/setup-guide.md) |
| **Setting up IntelliJ / JetBrains AI** | [tool-setup/jetbrains-ai/setup-guide.md](tool-setup/jetbrains-ai/setup-guide.md) |
| **Connecting Claude Code to Jira / Confluence / DB** | [tool-setup/mcp-servers/setup-guide.md](tool-setup/mcp-servers/setup-guide.md) |
| **Using the web portal (no IDE)** | [web-ui/index.html](web-ui/index.html) |
| **Starting a new ATOM project** | [QUICK-START.md](QUICK-START.md) |
| **Modernizing COBOL/mainframe** | [project-contexts/mainframe-modernization/](project-contexts/mainframe-modernization/README.md) |
| **AI usage policy and code disclosure** | [governance/ai-usage-policy.md](governance/ai-usage-policy.md) |

---

## Supported AI Tools

FORGE works with all major AI coding assistants. You do not need all of them — use the tool(s) your team has licensed.

| Tool | IDE Support | Config File | Setup Guide |
|---|---|---|---|
| **Claude Code** | VS Code, JetBrains, Terminal | `CLAUDE.md` + `SKILLS.md` | [tool-setup/claude-code/](tool-setup/claude-code/) |
| **Claude Code + MCP** | Any (via terminal) | `.mcp.json` | [tool-setup/mcp-servers/setup-guide.md](tool-setup/mcp-servers/setup-guide.md) |
| **GitHub Copilot** (Chat + Agent Mode) | VS Code, JetBrains, Neovim | `.github/copilot-instructions.md` | [tool-setup/github-copilot/setup-guide.md](tool-setup/github-copilot/setup-guide.md) |
| **JetBrains AI Assistant** | IntelliJ, PyCharm, WebStorm | AI Instructions file | [tool-setup/jetbrains-ai/setup-guide.md](tool-setup/jetbrains-ai/setup-guide.md) |
| **Cursor** | Cursor IDE | `.cursorrules` | [tool-setup/cursor/](tool-setup/cursor/) |
| **IBM watsonx Code Assist** | VS Code, Eclipse | Custom Instructions field | [tool-setup/watsonx-code-assist/](tool-setup/watsonx-code-assist/) |
| **AWS Q Developer** | VS Code, JetBrains | System message | [tool-setup/aws-q-developer/setup-guide.md](tool-setup/aws-q-developer/setup-guide.md) |
| **Claude.ai / ChatGPT (web)** | Browser | Copy prompt + context | [web-ui/index.html](web-ui/index.html) |

---

## The FORGE + ATOM + IDE Integration Model

This diagram shows how everything connects in a developer's environment:

```
┌─────────────────────────────────────────────────────────────────────┐
│                        FORGE FRAMEWORK                               │
│                                                                      │
│  .context/              sdlc/               personas/               │
│  ├─ CORE_SKILLS.md      ├─ 00-pre-engage    ├─ developer/           │
│  ├─ ATOM_CHASSIS.md     ├─ 01-discovery     ├─ architect/           │
│  ├─ MODERNIZATION.md    └─ 05-development   └─ qa-engineer/         │
│  ├─ COBOL_READING_GUIDE.md                                          │
│  └─ PAYMENTS_DOMAIN.md                                              │
│           │                    │                    │                │
└───────────┼────────────────────┼────────────────────┼───────────────┘
            │                    │                    │
            ▼                    ▼                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     YOUR PROJECT REPOSITORY                          │
│                                                                      │
│  .context/          ← copied from FORGE                             │
│  CLAUDE.md          ← copied from FORGE tool-setup/claude-code/     │
│  .github/           ← copilot-instructions.md from FORGE            │
│  .cursorrules       ← from FORGE tool-setup/cursor/                 │
└───────────────────────────────┬─────────────────────────────────────┘
                                │  loaded automatically by:
            ┌───────────────────┼───────────────────────┐
            ▼                   ▼                        ▼
     ┌─────────────┐   ┌──────────────┐        ┌─────────────────┐
     │ Claude Code │   │ GitHub       │        │ JetBrains AI    │
     │ + MCP       │   │ Copilot      │        │ Assistant       │
     │ (VS Code /  │   │ Chat +       │        │ (IntelliJ /     │
     │  Terminal)  │   │ Agent Mode   │        │  PyCharm)       │
     │             │   │              │        │                 │
     │ /analyze-   │   │ #file:       │        │ Context pane    │
     │  legacy     │   │  .context/   │        │ + prompt paste  │
     │ /generate-  │   │  ATOM_CHASSIS│        │                 │
     │  service    │   │  .md         │        │                 │
     └──────┬──────┘   └──────┬───────┘        └────────┬────────┘
            │                 │                          │
            │  ┌──────────────────────────────┐          │
            └─►│ MCP Servers (Claude Code)    │          │
               │ • GitHub — repos, PRs        │          │
               │ • Jira — tickets, criteria   │◄─────────┘
               │ • Confluence — architecture  │
               │ • PostgreSQL — live schema   │
               └──────────────┬───────────────┘
                              │
                              ▼
              ┌───────────────────────────┐
              │  Generated Code           │
              │  • @AtomService           │
              │  • @CircuitBreaker        │
              │  • ApiResponse<T>         │
              │  • Structured logging     │
              │  • Unit tests included    │
              │  • No hardcoded secrets   │
              └───────────────────────────┘
```

---

## Getting Started in 5 Steps

See [QUICK-START.md](QUICK-START.md) for the complete setup guide.

**Summary:**

1. **Copy `.context/`** into your project repository root
2. **Configure your AI tool** using the matching file from `tool-setup/`
3. **Load the constitution** at the start of every session (`constitution/01-core-principles.md`)
4. **Pick your SDLC stage** and open the matching prompts from `sdlc/`
5. **Generate, review, ship** — with FORGE guardrails applied automatically

---

## The FORGE Constitution

The constitution defines 12 non-negotiable rules that govern AI behavior in every session:

| # | Principle | Core Protection |
|---|---|---|
| 1 | Source-Grounded Analysis | Prevents hallucinated system behavior |
| 2 | Facts vs. Assumptions | Prevents invisible assumption propagation |
| 3 | Staged Progression | Prevents build-before-understand failures |
| 4 | Ambiguity First | Prevents silent wrong interpretations |
| 5 | Behavior Preservation | Prevents unintentional logic changes in modernization |
| 6 | Traceability by Default | Enables impact analysis and audit |
| 7 | Test Alongside Code | Prevents untested AI-generated code |
| 8 | Human Approval Gates | Ensures human oversight of critical decisions |
| 9 | Incremental Scope | Prevents invisible scope creep |
| 10 | Secure by Default | Prevents security vulnerabilities in generated code |
| 11 | Observable Systems | Prevents invisible production systems |
| 12 | Honest Uncertainty | Prevents confident wrong answers |

Full text: [constitution/01-core-principles.md](constitution/01-core-principles.md)

---

## The FORGE SDLC

```
Stage 0: Pre-Engagement Analysis
  ↓ 5-task AI discovery from codebase evidence before any workshop
Stage 1: Discovery
  ↓ Stakeholder interviews, legacy system discovery, current-state assessment
Stage 2: Requirements
  ↓ User stories, acceptance criteria, NFRs, requirements traceability
Stage 3: Architecture
  ↓ ADRs, service decomposition, API contract design
Stage 4: Design
  ↓ Detailed design, data models, sequence diagrams
Stage 5: Development
  ↓ ATOM service generation, legacy code analysis, code review
Stage 6: Testing
  ↓ Test strategy, unit/integration/UAT test generation, parallel run
Stage 7: Security Review
  ↓ Threat modeling, security code review, compliance check
Stage 8: Deployment
  ↓ CI/CD pipeline, IaC, deployment plan, rollback strategy
Stage 9: Operations
  ↓ Runbooks, incident analysis, observability review
```

**The iron rule:** No stage is skipped. No code is generated before analysis is complete and reviewed.

---

## Claude Skills

Claude Code users get slash commands that trigger FORGE workflows:

| Skill | What It Does |
|---|---|
| `/analyze-legacy` | Analyze COBOL/legacy code → structured understanding doc |
| `/extract-rules` | Extract and number every business rule from source |
| `/generate-service` | Generate a complete ATOM service from a spec |
| `/generate-atom-service` | Scaffold a new ATOM service from scratch |
| `/review-code` | Review code against FORGE + ATOM standards |
| `/create-tests` | Generate JUnit 5 + Testcontainers test suite |
| `/map-data` | Map COBOL copybooks → Java domain model + MapStruct |
| `/create-adr` | Generate Architecture Decision Record |
| `/generate-openapi-spec` | Generate an OpenAPI 3.1 spec from a Stage 2 design |
| `/pre-engagement` | Run full 5-task pre-engagement analysis |
| `/explain-legacy` | Explain legacy code in plain English |
| `/runbook` | Generate operational runbook |
| `/package-delivery` | Package stage artifacts into a traceable delivery package |

Full catalog: [tool-setup/claude-code/SKILLS.md](tool-setup/claude-code/SKILLS.md)

---

## Web Portal (No IDE Required)

For engineers and stakeholders who don't use an IDE, FORGE provides a browser-based prompt portal:

```bash
open web-ui/index.html   # macOS
start web-ui/index.html  # Windows
```

Or deploy to GitHub Pages for team-wide access.

The portal provides all FORGE prompts organized by persona and SDLC stage, with one-click copy and paste into any AI tool — Claude.ai, watsonx, ChatGPT, Copilot chat.

See [web-ui/README.md](web-ui/README.md) for deployment options.

---

## Contributing and Customization

FORGE is designed to be forked and customized for your organization:

1. **Fork this repository** to your internal GitHub/GitLab
2. **Update `.context/ATOM_CHASSIS.md`** with your specific ATOM version, annotations, and conventions
3. **Update `.context/CORE_SKILLS.md`** with your organization's specific security policies
4. **Add organization-specific prompts** to the relevant `personas/` or `sdlc/` directories
5. **Update the web portal** (`web-ui/index.html`) with your custom prompts

Every team, project, and persona should feel that FORGE speaks to their specific context — not generic AI advice.

---

*FORGE v2.0 — Framework for Orchestrated AI-Guided Engineering*
*Built for ATOM-native engineering teams. Works with any AI coding assistant.*
