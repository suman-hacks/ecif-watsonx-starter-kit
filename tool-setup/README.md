# FORGE Tool Setup — AI Tool Selection and Configuration Guide

This guide helps you choose the right AI coding tool (or combination of tools) for your project and points you to the configuration setup for each.

---

## Tool Selection Guide

No single AI tool is optimal for every task. FORGE is designed to let you use the best tool for each job. Use this guide to decide which tool to configure first, and whether a multi-tool strategy is right for your project.

### GitHub Copilot

**Best for**: Day-to-day inline code completion and Copilot Chat for developers writing code in their IDE. Works across virtually all programming languages and frameworks. The most widely deployed enterprise AI coding assistant.

**When to choose it**:
- Your team is already licensed for GitHub Copilot Enterprise or Business
- You want seamless IDE-integrated code suggestions as you type
- Your primary use case is code completion, docstring generation, and unit test scaffolding
- You want a tool that works without switching context

**When it is not enough on its own**:
- Deep multi-file analysis or full-codebase reasoning (Claude Code is better)
- COBOL/PL1/z/OS work (watsonx Code Assist for Z is purpose-built)
- AWS-specific service integration guidance (AWS Q Developer is better)
- Large-batch programmatic analysis (IBM watsonx.ai API is better)

**Configuration file**: `.github/copilot-instructions.md` in your project repo
**Setup**: `tool-setup/github-copilot/copilot-instructions.md`

---

### Claude Code

**Best for**: Complex multi-file analysis, architecture discussions, legacy code understanding, agentic task execution (reading many files, writing many files, running tests), and nuanced reasoning about design trade-offs. Excellent for mainframe-to-Java translation logic because it can hold large context windows and reason about complex business rules.

**When to choose it**:
- You need the AI to read and reason about 10–50+ files simultaneously
- You are working through complex architecture or design decisions that require extended reasoning
- You want to run agentic workflows: `analyze this legacy COBOL, extract business rules, generate Java equivalents, write tests`
- You need an AI coding assistant accessible from the command line or in a CI pipeline
- You want an AI that can explain its reasoning and ask clarifying questions before generating code

**When it is not enough on its own**:
- Inline IDE code completion (GitHub Copilot is better for this)
- COBOL-specific syntax analysis and copybook resolution (watsonx Code Assist for Z is purpose-built)
- AWS-specific SDK and service documentation awareness (AWS Q Developer is better)

**Configuration file**: `CLAUDE.md` in your project repo root
**Setup**: `tool-setup/claude-code/CLAUDE.md`

---

### watsonx Code Assist for Z

**Best for**: COBOL, PL/1, JCL, REXX, and Assembler analysis on z/OS. IBM's purpose-built AI trained on mainframe codebases. Understands copybooks, CICS commands, IMS DB/DC constructs, file section layouts, and z/OS system calls. Indispensable for any mainframe modernization project.

**When to choose it**:
- Your source code is COBOL, PL/1, Assembler, REXX, or JCL
- You need accurate analysis of CICS and IMS constructs
- You want AI assistance directly in VS Code or Eclipse while connected to z/OS
- You are extracting business rules from legacy mainframe programs

**When it is not enough on its own**:
- Generating the target-state Java, Python, or Go code (pair with Copilot or Claude Code)
- Cloud migration guidance (pair with AWS Q Developer)
- Architecture-level design and ADR generation (pair with Claude Code)

**Configuration**: See `tool-setup/watsonx-code-assist/for-z-mainframe/setup-guide.md`

---

### IBM watsonx.ai / BAM

**Best for**: Large-batch programmatic analysis, fine-tuned enterprise models, REST API-based prompt execution, and organizations requiring IBM-hosted AI with enterprise data residency guarantees.

**When to choose it**:
- You need to analyze hundreds of COBOL programs or Java classes in batch (via API)
- Your organization requires IBM-governed AI with specific data residency (US, EU, APAC)
- You want to fine-tune a model on your organization's proprietary code and documentation
- You are building an automated pipeline that calls an AI API programmatically

**When it is not enough on its own**:
- Interactive IDE assistance (use Copilot or Claude Code instead)
- Real-time code completion in an IDE
- COBOL-specific syntax understanding (watsonx Code Assist for Z is better)

**Configuration**: See `tool-setup/watsonx-code-assist/for-distributed/setup-guide.md`

---

### Cursor

**Best for**: Greenfield development where full-file and multi-file AI editing is the primary workflow. Cursor's Composer mode can generate or heavily refactor entire files based on natural language instructions, making it excellent for scaffolding new services from scratch.

**When to choose it**:
- Your project is greenfield and you want AI to generate boilerplate, scaffolding, and initial implementations
- You want to make sweeping multi-file changes guided by AI (e.g., rename a domain concept across 20 files)
- You are comfortable with an AI-native editor (as opposed to a plugin in VS Code/IntelliJ)
- You want AI-assisted refactoring across an entire codebase

**When it is not enough on its own**:
- COBOL/mainframe work (watsonx Code Assist for Z is required)
- AWS-specific guidance (AWS Q Developer is better)
- Complex agentic analysis pipelines (Claude Code is better)

**Configuration file**: `.cursorrules` in your project repo root
**Setup**: `tool-setup/cursor/cursorrules.md`

---

### AWS Q Developer

**Best for**: AWS-native workloads, cloud migration to AWS, and engineering teams deeply embedded in the AWS ecosystem. Q Developer has deep awareness of AWS services, SDKs, CloudFormation, CDK, and AWS best practices. It can also scan code for security vulnerabilities and suggest IAM policy fixes.

**When to choose it**:
- Your target platform is AWS (ECS, EKS, Lambda, RDS, etc.)
- You are migrating applications from on-premises or another cloud to AWS
- You want AI assistance with CloudFormation, CDK, or Terraform for AWS resources
- You want AI-powered security scanning aware of AWS IAM and service-specific risks
- Your organization is standardized on AWS tooling

**When it is not enough on its own**:
- Non-AWS cloud targets (Azure, GCP, IBM Cloud — other tools are better)
- COBOL/mainframe work (watsonx Code Assist for Z is required)
- Complex multi-file reasoning and architecture discussions (Claude Code is better)

**Configuration**: See `tool-setup/aws-q-developer/setup-guide.md`

---

## Configuration Quick Reference

| Tool | Config File Location | Format | Approx. Size Limit | Auto-loaded? |
|---|---|---|---|---|
| GitHub Copilot | `.github/copilot-instructions.md` in repo | Markdown | ~8,000 characters | Yes, in IDE |
| Claude Code | `CLAUDE.md` in repo root | Markdown | No hard limit (recommend < 4,000 tokens) | Yes, on `claude` launch |
| Cursor | `.cursorrules` in repo root | Plain text or Markdown | ~10,000 characters | Yes, on project open |
| watsonx Code Assist for Z | VS Code settings + system prompt | JSON + text | Varies by version | Partially (settings file auto-loaded; session context must be pasted) |
| IBM watsonx.ai / BAM | API `system_prompt` parameter | Text | Model-dependent (typically 4K–8K tokens) | No — must be passed each API call |
| AWS Q Developer | Workspace instructions in IDE settings | Text | ~4,000 characters | Yes, in IDE |
| JetBrains AI | IDE settings panel | Text | ~4,000 characters | Yes, in IDE |

---

## Multi-Tool Strategy

For complex enterprise projects — especially mainframe modernization — using a single AI tool is rarely optimal. FORGE is designed to support coordinated multi-tool workflows where each tool handles the tasks it is best suited for.

### Pattern 1: Mainframe Modernization Stack

This is the recommended tool combination for COBOL/PL1-to-Java modernization projects.

```
watsonx Code Assist for Z          → COBOL/PL1 analysis, business rule extraction
         ↓ (outputs: extracted rules, data models, flow diagrams)
Claude Code                         → Architecture design, Java package design, ADR generation
         ↓ (outputs: Java class skeletons, design specifications)
GitHub Copilot                      → Java implementation, unit test generation, Spring Boot wiring
         ↓ (outputs: production-ready Java code with tests)
Claude Code (review)                → Multi-file code review, architectural conformance check
```

**How to coordinate**:
1. Use watsonx Code Assist for Z in VS Code connected to z/OS to analyze each COBOL program. Export your findings (business rules, data structures, flow) to a structured document.
2. Load that document into a Claude Code session along with your target architecture spec. Generate the Java design and ADRs.
3. Use GitHub Copilot in your Java IDE to assist with the actual implementation, referencing the Claude-generated design.
4. Use a final Claude Code session to review the completed module for architectural conformance and completeness.

---

### Pattern 2: Cloud Migration Stack

Recommended for lift-and-shift or re-architecture to AWS.

```
Claude Code                         → Current-state assessment, dependency mapping, risk analysis
         ↓ (outputs: migration inventory, risk register, ADRs)
AWS Q Developer                     → AWS target-state design, CDK/Terraform scaffolding, service selection
         ↓ (outputs: infrastructure code, AWS architecture)
GitHub Copilot                      → Application code adaptation, SDK integration
         ↓ (outputs: cloud-adapted application code)
AWS Q Developer (security scan)     → IAM policy review, security finding remediation
```

---

### Pattern 3: Greenfield API Development Stack

Recommended for building new services from scratch.

```
Claude Code                         → System design, API contract design (OpenAPI), ADRs
         ↓ (outputs: OpenAPI specs, architecture design, domain model)
Cursor                              → Scaffold project from spec, generate service boilerplate
         ↓ (outputs: full project structure, initial implementation)
GitHub Copilot                      → Complete implementation, add unit tests, wire integrations
         ↓ (outputs: production-ready code with full test coverage)
Claude Code (review)                → Final review for security, design conformance, test coverage
```

---

### Avoiding Tool Conflicts

When using multiple tools on the same project, ensure consistency by:

1. **Single source of truth for project rules**: Maintain one master context document (e.g., `docs/project-context.md`) that all tool configs reference. Keep it updated.
2. **Shared naming conventions**: If Claude Code and Copilot both generate code for the same project, they must follow the same naming conventions. Put conventions in all tool config files.
3. **Consistent constitution**: Embed the FORGE Constitution rules in every tool's config file. Do not let one tool operate without the guardrails.
4. **Output staging**: Never directly merge AI output from one tool into code that another tool will analyze without human review. Use review gates (`guardrails/review-gates.md`) between tool handoffs.

---

## Getting Help

- For tool-specific issues: see the individual setup guide in the relevant subdirectory.
- For prompt issues: see `sdlc/` for stage-specific prompts and `personas/` for role-specific prompts.
- For governance and compliance questions: see `guardrails/` and `constitution/`.
