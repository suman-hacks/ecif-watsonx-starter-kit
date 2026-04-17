# FORGE — Framework for Orchestrated AI-Guided Engineering

> **The Enterprise AI Engineering Framework — From Legacy to Modern, From Requirement to Release**

---

## What is FORGE?

FORGE is a structured, enterprise-grade framework that provides AI prompt packs, tool configurations, governance artifacts, and persona-specific guides for every stage of the software development and product lifecycle. It is AI-tool agnostic — designed to work equally well with GitHub Copilot, Claude Code, IBM watsonx Code Assist, Cursor, AWS Q Developer, and other major AI coding assistants. FORGE covers greenfield development, brownfield modernization, mainframe transformation (COBOL/PL1/JCL on z/OS), cloud migration, and API modernization. Every artifact in FORGE is production-ready: no placeholders, no aspirational stubs — just immediately usable engineering guidance backed by governance guardrails.

---

## Who Is FORGE For?

FORGE is built for every engineering persona involved in delivering software at enterprise scale. Each persona gets a dedicated set of prompts, guides, and artifacts tailored to their workflow.

| Persona | What They Get | Start Here |
|---|---|---|
| **Product Manager / BA** | Requirements decomposition prompts, user story templates, acceptance criteria generators | `personas/product-manager/` |
| **Enterprise Architect** | Architecture decision record (ADR) templates, system design prompts, trade-off analysis | `personas/architect/` |
| **Software Developer** | Code generation prompts, refactoring guides, SDLC stage prompts | `personas/developer/` |
| **QA / Test Engineer** | Test strategy prompts, test case generators, coverage analysis, BDD scenario prompts | `personas/qa-engineer/` |
| **DevSecOps Engineer** | Security review prompts, threat modeling, IaC scanning, CI/CD pipeline prompts | `personas/devsecops/` |
| **Data Engineer** | Schema migration prompts, data mapping, ETL modernization, data contract generation | `personas/data-engineer/` |
| **Mainframe Engineer** | COBOL/PL1 analysis prompts, JCL decomposition, z/OS-specific guides | `personas/mainframe-engineer/` |
| **Cloud Engineer** | Cloud migration prompts, infrastructure-as-code templates, AWS/Azure/GCP patterns | `personas/cloud-engineer/` |
| **Tech Lead / Engineering Manager** | Review gate checklists, ADR governance, team workflow setup | `personas/tech-lead/` |
| **Platform / SRE Engineer** | Observability prompts, incident response codegen, runbook generation | `personas/platform-engineer/` |

---

## Supported AI Tools

FORGE provides dedicated configuration templates and setup guides for each supported tool. You do not need all of them — pick the one(s) your team uses.

| Tool | Supported | Configuration File / Path | Notes |
|---|---|---|---|
| **GitHub Copilot** | ✅ | `.github/copilot-instructions.md` | Copy from `tool-setup/github-copilot/copilot-instructions.md` |
| **GitHub Copilot Chat** | ✅ | Same as Copilot above | Chat mode reads the same instructions file |
| **Claude Code** | ✅ | `CLAUDE.md` (repo root) | Copy from `tool-setup/claude-code/CLAUDE.md` |
| **watsonx Code Assist for Z** | ✅ | `tool-setup/watsonx-code-assist/for-z-mainframe/` | IBM's purpose-built COBOL/PL1/z/OS AI tool |
| **IBM watsonx.ai / BAM** | ✅ | `tool-setup/watsonx-code-assist/for-distributed/` | Distributed workloads, batch analysis, fine-tuned models |
| **Cursor** | ✅ | `.cursorrules` (repo root) | Copy from `tool-setup/cursor/cursorrules.md` |
| **AWS Q Developer** | ✅ | `tool-setup/aws-q-developer/` | Best for AWS-native and cloud migration workloads |
| **JetBrains AI Assistant** | ✅ | `tool-setup/jetbrains-ai/` | IntelliJ/IDEA, GoLand, PyCharm integration |

---

## Project Type Support

FORGE provides project-context files that load domain-specific knowledge into your AI tool's session. Select the context that matches your project.

| Project Type | Context Directory | Use When |
|---|---|---|
| **Greenfield** | `project-contexts/greenfield/` | Building a new system from scratch with no legacy dependencies |
| **Brownfield (Distributed)** | `project-contexts/brownfield/` | Extending or modernizing an existing Java/Python/Node codebase |
| **Mainframe Modernization** | `project-contexts/mainframe-modernization/` | Transforming COBOL, PL/1, Assembler, JCL, or CICS/IMS applications |
| **Cloud Migration** | `project-contexts/cloud-migration/` | Lifting and shifting or re-architecting applications to AWS/Azure/GCP/IBM Cloud |
| **API Modernization** | `project-contexts/api-modernization/` | Exposing legacy functionality via REST/GraphQL/AsyncAPI; strangler fig patterns |
| **Event-Driven Transformation** | `project-contexts/event-driven/` | Introducing Kafka, MQ, or event streaming into batch or synchronous systems |
| **Data Platform Modernization** | `project-contexts/data-modernization/` | Schema migration, data lakehouse, CDC pipelines, data contract adoption |

---

## Framework Structure

```
forge/  (this repository)
│
├── README.md                          ← You are here. Framework hub.
├── QUICK-START.md                     ← 5-minute setup guide
│
├── constitution/                      ← Always-on AI governance rules
│   ├── 01-core-principles.md          ← The 10 non-negotiable FORGE rules
│   ├── 02-security-rules.md           ← Security and data protection guardrails
│   ├── 03-architecture-principles.md  ← Architectural guardrails
│   └── 04-modernization-intent.md     ← Modernization philosophy
│
├── tool-setup/                        ← AI tool configuration files
│   ├── README.md                      ← Tool selection and comparison guide
│   ├── github-copilot/                ← GitHub Copilot custom instructions
│   ├── claude-code/                   ← CLAUDE.md template
│   ├── cursor/                        ← .cursorrules template
│   ├── watsonx-code-assist/
│   │   ├── for-z-mainframe/           ← watsonx Code Assist for Z setup
│   │   └── for-distributed/           ← watsonx.ai / BAM setup
│   ├── aws-q-developer/               ← AWS Q Developer setup
│   └── jetbrains-ai/                  ← JetBrains AI Assistant setup
│
├── project-contexts/                  ← Project-type context files
│   ├── greenfield/
│   ├── brownfield/
│   ├── mainframe-modernization/
│   ├── cloud-migration/
│   ├── api-modernization/
│   ├── event-driven/
│   └── data-modernization/
│
├── sdlc/                              ← SDLC stage prompt packs
│   ├── README.md                      ← SDLC coverage overview
│   ├── 01-discovery/                  ← Discovery and problem framing
│   ├── 02-requirements/               ← Requirements engineering prompts
│   ├── 03-architecture/               ← System design and ADRs
│   ├── 04-design/                     ← Detailed design and API contracts
│   ├── 05-development/                ← Code generation and review
│   ├── 06-testing/                    ← Test generation and QA
│   ├── 07-security-review/            ← Security analysis and threat modeling
│   ├── 08-deployment/                 ← CI/CD, IaC, release automation
│   ├── 09-operations/                 ← Observability, runbooks, incident response
│   └── 10-retrospective/              ← Post-release review and learning capture
│
├── personas/                          ← Role-specific prompt packs
│   ├── README.md
│   ├── product-manager/
│   ├── architect/
│   ├── developer/
│   ├── qa-engineer/
│   ├── devsecops/
│   ├── data-engineer/
│   ├── mainframe-engineer/
│   ├── cloud-engineer/
│   ├── tech-lead/
│   └── platform-engineer/
│
├── guardrails/                        ← Quality and compliance gates
│   ├── review-gates.md                ← Human review checkpoints
│   ├── code-quality-policy.md         ← Code quality standards
│   ├── architecture-policy.md         ← Architecture compliance rules
│   └── prompt-boundary-policy.md      ← What must not go into AI prompts
│
├── templates/                         ← Reusable document templates
│   ├── adr-template.md                ← Architecture Decision Record
│   ├── assumption-register.md         ← Assumption tracking
│   ├── decision-log.md                ← Decision log
│   ├── knowledge-pack-template.md     ← Custom knowledge pack scaffold
│   └── prompt-template.md             ← Prompt authoring template
│
├── examples/                          ← Worked examples by use case
│   ├── cobol-to-java/                 ← End-to-end COBOL modernization example
│   ├── api-first-greenfield/          ← Greenfield API-first project example
│   ├── cloud-migration-lift-shift/    ← Cloud migration example
│   └── event-driven-transformation/   ← Event-driven refactor example
│
└── repo-template/                     ← Starting template for a new project repo
    ├── .github/
    │   └── copilot-instructions.md
    ├── CLAUDE.md
    ├── .cursorrules
    └── src/
```

---

## SDLC Coverage

FORGE provides structured AI prompts and governance artifacts for all 10 stages of the software development lifecycle. Prompts are organized by stage and further filterable by project type and persona.

| # | Stage | Description | Directory |
|---|---|---|---|
| 1 | **Discovery** | Problem framing, stakeholder alignment, legacy assessment, scope definition | `sdlc/01-discovery/` |
| 2 | **Requirements** | User story generation, acceptance criteria, non-functional requirements, backlog structuring | `sdlc/02-requirements/` |
| 3 | **Architecture** | System design, technology selection, ADR generation, trade-off analysis | `sdlc/03-architecture/` |
| 4 | **Design** | Detailed component design, API contracts (OpenAPI/AsyncAPI), data models, sequence diagrams | `sdlc/04-design/` |
| 5 | **Development** | Code generation, refactoring, code review, legacy transformation, unit test generation | `sdlc/05-development/` |
| 6 | **Testing** | Test strategy, test case generation, integration test scaffolding, BDD scenario authoring, coverage analysis | `sdlc/06-testing/` |
| 7 | **Security Review** | SAST guidance, threat modeling, dependency scanning, secrets detection, compliance checks | `sdlc/07-security-review/` |
| 8 | **Deployment** | CI/CD pipeline generation, IaC (Terraform/Helm/CDK), release notes, deployment runbooks | `sdlc/08-deployment/` |
| 9 | **Operations** | Observability instrumentation, alert rule generation, runbook authoring, incident response | `sdlc/09-operations/` |
| 10 | **Retrospective** | Lessons learned capture, knowledge pack updates, assumption register closure, improvement backlog | `sdlc/10-retrospective/` |

---

## Quick Navigation by Persona

Find your role and jump straight to your starting point.

| I am a... | My quick start |
|---|---|
| 📋 **Product Manager / BA** | `personas/product-manager/` → `sdlc/02-requirements/` |
| 🏛️ **Enterprise Architect** | `personas/architect/` → `sdlc/03-architecture/` → `constitution/03-architecture-principles.md` |
| 💻 **Developer (Distributed)** | `personas/developer/` → `tool-setup/` → `sdlc/05-development/` |
| 🖥️ **Mainframe Engineer** | `personas/mainframe-engineer/` → `tool-setup/watsonx-code-assist/for-z-mainframe/` → `project-contexts/mainframe-modernization/` |
| ☁️ **Cloud Engineer** | `personas/cloud-engineer/` → `tool-setup/aws-q-developer/` → `project-contexts/cloud-migration/` |
| 🔒 **DevSecOps / Security** | `personas/devsecops/` → `sdlc/07-security-review/` → `guardrails/` |
| 🧪 **QA / Test Engineer** | `personas/qa-engineer/` → `sdlc/06-testing/` |
| 🗄️ **Data Engineer** | `personas/data-engineer/` → `project-contexts/data-modernization/` |
| 🔧 **Tech Lead** | `personas/tech-lead/` → `guardrails/review-gates.md` → `templates/adr-template.md` |
| ⚙️ **Platform / SRE** | `personas/platform-engineer/` → `sdlc/09-operations/` |

---

## The 3-Step Quick Start

Getting started with FORGE takes about 5 minutes. See `QUICK-START.md` for the full walkthrough. The essential pattern is:

### Step 1 — Pick Your AI Tool and Copy Its Config

| If you use... | Do this |
|---|---|
| GitHub Copilot | Copy `tool-setup/github-copilot/copilot-instructions.md` → `.github/copilot-instructions.md` in your repo |
| Claude Code | Copy `tool-setup/claude-code/CLAUDE.md` → `CLAUDE.md` in your repo root |
| Cursor | Copy `tool-setup/cursor/cursorrules.md` content → `.cursorrules` in your repo root |
| watsonx Code Assist for Z | Follow `tool-setup/watsonx-code-assist/for-z-mainframe/setup-guide.md` |
| AWS Q Developer | Follow `tool-setup/aws-q-developer/setup-guide.md` |

Edit the `[USER FILLS]` sections in your copied config to reflect your specific project.

### Step 2 — Pick Your SDLC Stage and Open the Prompt

Navigate to `sdlc/` and select the stage you are working in. Each stage directory contains prompts organized by task and, where applicable, by project type (greenfield, brownfield, mainframe).

### Step 3 — Pick Your Persona and Customize

Navigate to `personas/` and find your role. Each persona directory provides role-specific prompt variants that layer on top of the stage prompts.

**Always load `constitution/01-core-principles.md` as your AI session's always-on rules.**

---

## Governance and Safety

FORGE is designed for enterprise environments where AI-generated outputs must meet quality, security, compliance, and auditability standards.

**The FORGE Constitution** (`constitution/01-core-principles.md`) contains 10 non-negotiable rules that govern AI behavior across all tools and all stages. Load this file in every AI session.

**Guardrails** (`guardrails/`) define what must never enter an AI prompt (secrets, PII, production credentials, PAN data, regulated customer data) and what review gates must be passed before AI-generated code is merged or deployed.

**Traceability** is built in. Every FORGE workflow produces assumption registers, decision logs, and traceability artifacts that link AI-generated outputs back to source requirements, source artifacts, and human review records.

**Data Protection**: FORGE strictly prohibits including production data, customer PII, payment card data (PAN/CVV), authentication credentials, or any regulated content in AI prompts. See `guardrails/prompt-boundary-policy.md` for the full data protection ruleset.

**Human Review Gates**: AI-generated code, architecture decisions, and security configurations are subject to mandatory human review before promotion. Review gate checklists are in `guardrails/review-gates.md`.

---

## Migrating from ECIF

If you are migrating from the ECIF (Enterprise Coding Intelligence Framework) starter kit:

- ECIF's constitution rules are preserved and expanded in `constitution/`
- ECIF's payments-specific knowledge packs are preserved in `project-contexts/mainframe-modernization/` and `personas/mainframe-engineer/`
- ECIF's watsonx prompt modes (legacy understanding → target mapping → code generation → review/hardening) are now formalized as FORGE SDLC stages 1–4 in `sdlc/`
- ECIF's COBOL/ICS/Spring Boot packs are now organized under the mainframe modernization project context
- The `repo-template/` structure is preserved and enhanced

---

## Version History

| Version | Description |
|---|---|
| ECIF v1 | Initial payments/banking modernization framework |
| ECIF v2 | Added payments-specific knowledge packs and watsonx prompt modes |
| ECIF v3 | Added repo template, ICS authorization bundle, traceability spreadsheets, Orchestrate pack |
| **FORGE v1.0** | **Full framework rewrite — multi-tool, multi-project-type, full SDLC coverage, all engineering personas** |

---

## License and Usage

FORGE is an enterprise engineering framework. The prompts, templates, and guides in this repository are designed to be adapted to your organization's standards. They do not contain proprietary business logic, customer data, or confidential system details. Teams are expected to fill in the `[USER FILLS]` sections with their own project-specific content before use.

---

*FORGE v1.0 — Built on the foundation of ECIF. Designed for every engineer, every tool, every stage.*
