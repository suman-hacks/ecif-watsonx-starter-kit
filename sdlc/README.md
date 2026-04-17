# FORGE SDLC Prompt Library

This directory contains AI prompt packs for every stage of the Software Development Lifecycle (SDLC) and Product Development Lifecycle (PDLC).

## How to Use

1. Identify which stage you're in (see table below)
2. Load the **FORGE Constitution** into your AI tool first (`constitution/01-core-principles.md`)
3. Load your **Project Context** (`project-contexts/[your-type]/context.md`)
4. Open the relevant stage directory and copy the prompt for your task

## Stage Map

| Stage | Directory | Who Uses It | Key Outputs |
|---|---|---|---|
| 00 — Pre-Engagement | `00-pre-engagement/` | Architects, Lead Engineers | Codebase analysis, POC options |
| 01 — Discovery | `01-discovery/` | BA, Architects, Engineers | Current-state assessment, system inventory |
| 02 — Requirements | `02-requirements/` | PM, PO, BA | User stories, acceptance criteria, NFRs |
| 03 — Architecture | `03-architecture/` | Solution Architect, Lead Engineer | ADRs, service design, API contracts |
| 04 — Design | `04-design/` | Lead Engineer, Developers | Detailed design, data models, sequence diagrams |
| 05 — Development | `05-development/` | Developers, Lead Engineer | Code, unit tests, PR reviews |
| 06 — Testing | `06-testing/` | QA, Developers | Test plans, test suites, UAT scripts |
| 07 — Security | `07-security/` | DevSecOps, Security Architects | Threat model, security review, compliance |
| 08 — Deployment | `08-deployment/` | DevSecOps, Platform Engineers | Deployment plan, IaC, runbooks |
| 09 — Operations | `09-operations/` | SRE, DevSecOps, On-call | Runbooks, incident analysis, observability |

## Cross-Stage Rules

- **Never skip a stage** — each stage's outputs are required inputs for the next
- **Human approval gates** — see `governance/review-gates.md` for what requires sign-off before proceeding
- **Traceability** — record every AI-assisted decision in `governance/audit-trail-template.md`
- **Constitution is always loaded** — paste `constitution/01-core-principles.md` into every session

## For Legacy Modernization Projects

The modernization pipeline maps to SDLC stages as follows:

```
SDLC Stage 01 (Discovery)     → Stage 00 Pre-Engagement Tasks T1-T4
SDLC Stage 02 (Requirements)  → Business rule extraction (agents/03-rule-extractor-agent.md)
SDLC Stage 03 (Architecture)  → Target mapping (agents/04-architect-agent.md)
SDLC Stage 05 (Development)   → Code generation (agents/05-code-generator-agent.md)
SDLC Stage 05 (Review)        → Code hardening (agents/06-reviewer-agent.md)
```

See `project-contexts/mainframe-modernization/` for full guidance.
