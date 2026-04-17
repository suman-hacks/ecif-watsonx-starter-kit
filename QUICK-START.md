# FORGE Quick Start Guide

> Get your AI engineering environment configured in 5 minutes.

---

## Prerequisites

Before you begin, confirm you have the following:

- [ ] A git repository for your project (local or hosted on GitHub/GitLab/Bitbucket)
- [ ] At least one supported AI coding tool installed and licensed (see Step 1)
- [ ] A basic understanding of your project type (greenfield, brownfield, mainframe, cloud migration)
- [ ] Read access to any legacy source code or architecture documentation you plan to modernize
- [ ] Familiarity with your team's target language and framework (Java/Spring, Python/FastAPI, Node.js, etc.)

> **Security reminder**: Before you start, identify which data is classified in your organization. Never paste production database contents, customer records, PAN/CVV data, authentication credentials, or internal IP addresses into any AI tool. See `guardrails/prompt-boundary-policy.md` for the full policy.

---

## Step 1: Choose Your AI Tool and Configure It

You need at least one AI tool configured. If your team uses multiple tools (e.g., watsonx Code Assist for Z for COBOL analysis + GitHub Copilot for Java generation), configure each tool you plan to use.

### GitHub Copilot

**What it does**: Provides in-editor code completion and Copilot Chat using your project's custom instructions.

**Setup**:
1. Ensure GitHub Copilot is installed and active in VS Code, JetBrains, or your IDE.
2. In your project repository, create the directory `.github/` if it does not exist.
3. Copy the contents of `tool-setup/github-copilot/copilot-instructions.md` from this FORGE repository into a new file at `.github/copilot-instructions.md` in your project repository.
4. Edit the `[USER FILLS]` sections: project name, tech stack, architectural patterns, key directories.
5. Commit the file. GitHub Copilot will automatically pick up these instructions in your IDE.

**Verification**: Open GitHub Copilot Chat in your IDE and ask: `What are the key rules you are operating under for this project?` — it should reflect your FORGE configuration.

---

### Claude Code

**What it does**: Provides agentic AI coding assistance, multi-file analysis, architecture discussions, and complex code generation. Reads `CLAUDE.md` from your repo root automatically.

**Setup**:
1. Install Claude Code CLI: follow instructions at `https://claude.ai/code`
2. Copy `tool-setup/claude-code/CLAUDE.md` to the root of your project repository as `CLAUDE.md`.
3. Edit the `[USER FILLS]` sections: project name, project type, tech stack, architectural patterns, repository structure, known constraints.
4. Optionally add custom slash commands relevant to your project.
5. Run `claude` from your project root — it will load `CLAUDE.md` automatically.

**Verification**: Run `claude` and ask: `Summarize the FORGE rules you are operating under.` — it should reflect your CLAUDE.md configuration.

---

### watsonx Code Assist for Z (Mainframe)

**What it does**: IBM's purpose-built AI for COBOL, PL/1, JCL, REXX, and Assembler. Understands z/OS constructs, copybooks, and mainframe data types.

**Setup**: Follow the detailed guide at `tool-setup/watsonx-code-assist/for-z-mainframe/setup-guide.md`.

**Summary**:
1. Install VS Code with IBM Z Open Editor extension.
2. Configure your z/OS connection credentials.
3. Load the FORGE mainframe context file from `project-contexts/mainframe-modernization/`.
4. Use the COBOL analysis prompts from `personas/mainframe-engineer/`.

---

### Cursor

**What it does**: AI-native code editor with full-file editing, multi-file changes, and project-wide context. Reads `.cursorrules` from your repo root.

**Setup**:
1. Install Cursor from `https://cursor.sh`
2. Open your project repository in Cursor.
3. Copy the contents of `tool-setup/cursor/cursorrules.md` from this FORGE repository into a new file at `.cursorrules` in your project root.
4. Edit the `[USER FILLS]` sections to reflect your project's language, framework, and conventions.
5. Cursor automatically loads `.cursorrules` when you open the project.

**Verification**: In Cursor Chat (Cmd/Ctrl+L), ask: `What coding conventions are you following for this project?`

---

### AWS Q Developer

**What it does**: AWS's AI coding assistant, optimized for AWS-native workloads, cloud migration, and AWS service integration.

**Setup**: Follow the detailed guide at `tool-setup/aws-q-developer/setup-guide.md`.

**Summary**:
1. Install the AWS Q Developer extension for VS Code or JetBrains.
2. Authenticate with your AWS Builder ID or IAM Identity Center.
3. Load the cloud migration context from `project-contexts/cloud-migration/`.
4. Use the infrastructure and migration prompts from `personas/cloud-engineer/`.

---

### IBM watsonx.ai / BAM

**What it does**: REST API-based access to IBM foundation models. Best for batch analysis, fine-tuned enterprise models, and programmatic prompt execution.

**Setup**: Follow the detailed guide at `tool-setup/watsonx-code-assist/for-distributed/setup-guide.md`.

---

## Step 2: Identify Your Project Type

Use this decision tree to select the right project context.

```
Is there existing code that will be kept or transformed?
├── YES
│   ├── Is the code COBOL, PL/1, Assembler, or JCL running on z/OS?
│   │   ├── YES → Project Type: MAINFRAME MODERNIZATION
│   │   │         Context: project-contexts/mainframe-modernization/
│   │   └── NO  → Is the goal to move it to a cloud platform?
│   │             ├── YES → Project Type: CLOUD MIGRATION
│   │             │         Context: project-contexts/cloud-migration/
│   │             └── NO  → Project Type: BROWNFIELD (DISTRIBUTED)
│   │                       Context: project-contexts/brownfield/
└── NO (starting fresh)
    ├── Is the primary deliverable exposing APIs over legacy data?
    │   ├── YES → Project Type: API MODERNIZATION
    │   │         Context: project-contexts/api-modernization/
    └── NO  → Project Type: GREENFIELD
              Context: project-contexts/greenfield/
```

**Special cases**:
- If you are introducing event streaming (Kafka, MQ) into an existing synchronous system: also load `project-contexts/event-driven/`
- If the project involves significant data migration or schema transformation: also load `project-contexts/data-modernization/`
- Projects can have more than one context. Load all that apply.

---

## Step 3: Load Your Project Context Into Your AI Tool

Each project context file (`project-contexts/<type>/context.md`) contains a structured description of that project type's challenges, patterns, terminology, and AI behavior rules. Load it at the start of every AI session.

**For GitHub Copilot**: Paste the relevant sections of the context file into the `### Domain Context` section of your `.github/copilot-instructions.md`.

**For Claude Code**: Paste the relevant context into the `### Known Context` section of your `CLAUDE.md`, or reference the file explicitly at the start of a Claude session: `Load and apply the context in project-contexts/mainframe-modernization/context.md`

**For Cursor**: Add the key context points to the `## Project Context` section of your `.cursorrules` file.

**For watsonx / AWS Q**: Paste the context as the opening system message in your session or API call.

---

## Step 4: Know Your SDLC Stage

Every prompt in FORGE is organized by SDLC stage. Before opening a prompt, identify which stage you are working in right now.

Open `sdlc/README.md` for a full description of all 10 stages and their prompts.

| If you are doing... | You are in Stage... |
|---|---|
| Assessing a legacy system, defining scope | Stage 1: Discovery |
| Writing user stories, acceptance criteria, requirements | Stage 2: Requirements |
| Designing the system, choosing technologies, writing ADRs | Stage 3: Architecture |
| Designing APIs, data models, component interactions | Stage 4: Design |
| Writing code, reviewing code, transforming legacy code | Stage 5: Development |
| Writing tests, test plans, test automation | Stage 6: Testing |
| Security review, threat modeling, dependency scanning | Stage 7: Security Review |
| CI/CD, deployment pipelines, infrastructure as code | Stage 8: Deployment |
| Monitoring, runbooks, incident response | Stage 9: Operations |
| Post-release review, lessons learned | Stage 10: Retrospective |

---

## Step 5: Pick Your Role-Specific Prompts

Navigate to `personas/` and open the directory for your role. Each persona directory contains:

- A `README.md` describing the prompts available
- Stage-specific prompt files (e.g., `stage-05-development.md`, `stage-02-requirements.md`)
- A `quick-reference.md` for common tasks

If you are a developer working on a mainframe modernization project, load both:
- `personas/developer/` (for code generation patterns)
- `personas/mainframe-engineer/` (for COBOL-specific analysis patterns)

Personas are not mutually exclusive. Tech leads typically combine their own persona with the developer persona.

---

## Step 6: Load the FORGE Constitution

**This step is mandatory for every AI session.**

The FORGE Constitution (`constitution/01-core-principles.md`) contains 10 non-negotiable rules that govern AI behavior. These rules prevent the most common and costly AI engineering mistakes: invented business logic, missed security constraints, untraceable outputs, and unreviewed code.

**For Claude Code**: The constitution rules are embedded in your `CLAUDE.md` under `### FORGE Constitution — Always Active`. They are loaded automatically.

**For GitHub Copilot**: The constitution rules are embedded in your `.github/copilot-instructions.md`. They are loaded automatically.

**For Cursor**: The constitution rules are in your `.cursorrules`. They are loaded automatically.

**For other tools / new sessions**: Open `constitution/01-core-principles.md` and paste its content as the first message in your AI session before any other prompts.

---

## First Prompt Template

Use this template to open any new AI session with FORGE. Fill in the bracketed sections before sending.

```
You are operating as an AI engineering assistant under the FORGE framework 
(Framework for Orchestrated AI-Guided Engineering).

ALWAYS-ON RULES (FORGE Constitution):
1. Do not invent undocumented business logic. If business rules are not 
   explicitly stated in the provided source artifacts, flag the gap rather 
   than assuming.
2. Preserve existing business behavior unless a change is explicitly requested 
   and approved.
3. Separate facts (sourced from artifacts), assumptions (stated explicitly), 
   recommendations (marked as such), and open questions (escalated to the human).
4. Prefer modular, testable, observable code.
5. Every output must be traceable to a source artifact, requirement, or declared 
   assumption.
6. Flag ambiguity before generating high-confidence outputs. Ask focused questions 
   rather than proceeding with guesses.
7. Never include secrets, credentials, customer PII, PAN, CVV, or regulated 
   content in any prompt, output, or artifact.
8. Default to incremental, reversible changes over big-bang transformations.
9. Follow the project's approved architectural patterns and coding standards.
10. Produce outputs that are reviewable and understandable by a human engineer 
    with domain knowledge.

PROJECT CONTEXT:
- Project name: [PROJECT NAME]
- Project type: [Greenfield | Brownfield | Mainframe Modernization | Cloud Migration | API Modernization]
- Tech stack: [LANGUAGES, FRAMEWORKS, DATABASES]
- Current SDLC stage: [Stage N: STAGE NAME]
- My role: [PERSONA/ROLE]

TASK FOR THIS SESSION:
[Describe what you want to accomplish in this session. Be specific. Reference 
source artifacts by name if they exist. State any constraints or non-negotiables.]

Before proceeding, confirm:
1. You understand the FORGE constitution rules above.
2. You understand the project context.
3. You have identified any ambiguities in my task description that need 
   clarification before you begin.
```

---

## Common Setup Mistakes to Avoid

| Mistake | Consequence | Correct Approach |
|---|---|---|
| Skipping the constitution load | AI invents business rules, produces untraceable outputs | Always load constitution first, every session |
| Pasting production data into prompts | Data leakage, compliance violation | Anonymize or synthesize test data. Never use production records. |
| Using AI output without human review | Incorrect code in production | Every AI output must pass a human review gate before merge |
| Skipping legacy analysis before code generation | Generated code misses business rules embedded in legacy code | Complete Stage 1 (Discovery) before Stage 5 (Development) |
| Using one tool for everything | Suboptimal results for specialized tasks | Use watsonx Code Assist Z for COBOL, Copilot/Claude for Java, Q Developer for AWS |
| Not committing `.github/copilot-instructions.md` or `CLAUDE.md` | Team members get different AI behavior | Commit tool config files to the repo so the whole team benefits |

---

## Next Steps

Once your tool is configured and your first session is running:

1. **Run a pilot on one narrow module** — Don't try to modernize everything at once. Pick one COBOL program, one API endpoint, or one service to start.
2. **Save your outputs as artifacts** — Use the templates in `templates/` to capture assumptions, decisions, and traceability links as you go.
3. **Review before you merge** — Use the checklists in `guardrails/review-gates.md` before promoting any AI-generated code.
4. **Update your knowledge pack** — As you discover project-specific patterns and rules, add them to a custom knowledge pack using `templates/knowledge-pack-template.md`.

---

*FORGE v1.0 — For detailed documentation on any topic, see the relevant directory's README.*
