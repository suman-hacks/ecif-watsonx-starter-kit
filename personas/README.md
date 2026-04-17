# FORGE Personas — Navigation Guide

**FORGE** (Framework for Orchestrated AI-Guided Engineering) provides role-specific AI prompt packs so every team member gets assistance calibrated to their actual responsibilities. This directory contains ten persona packs covering the full software delivery lifecycle.

---

## Overview of All 10 Personas

| # | Persona | Directory | Primary SDLC Stages |
|---|---------|-----------|---------------------|
| 1 | Product Manager | `product-manager/` | Discovery, Strategy, Release |
| 2 | Product Owner | `product-owner/` | Requirements, Sprint Planning, UAT |
| 3 | Business Analyst | `business-analyst/` | Discovery, Requirements, Design |
| 4 | Solution Architect | `solution-architect/` | Architecture, Design, Review |
| 5 | Lead Engineer | `lead-engineer/` | Design, Build, Review, Release |
| 6 | Developer | `developer/` | Build, Test, Refactor |
| 7 | QA Engineer | `qa-engineer/` | Test, Validation, Release |
| 8 | UAT / Business Readiness | `uat-business-readiness/` | UAT, Go-Live, Training |
| 9 | DevSecOps | `devsecops/` | Build, Security, Infrastructure |
| 10 | Project Manager | `project-manager/` | All stages (coordination) |

---

## Decision Guide: Which Persona Am I?

Read the description that best fits your day-to-day work. On small teams you may identify with more than one — see [Combining Personas](#combining-personas) below.

**Product Manager**
You own the product vision. You write PRDs, define OKRs, run competitive analysis, talk to customers, and decide what gets built and why. You spend more time in slide decks and strategy documents than in Jira tickets.

**Product Owner**
You are the Scrum team's primary interface to the business. You own the backlog, write user stories, run refinement, define acceptance criteria, and accept completed work each sprint. You live in Jira/Azure DevOps.

**Business Analyst**
You bridge business and technology. You extract business rules from legacy systems, document processes, write BRDs, create data dictionaries, and validate that technical designs actually meet what the business asked for.

**Solution Architect**
You design the target state. You make architectural decisions, write ADRs, define API contracts, design event schemas, produce system diagrams, and ensure non-functional requirements are addressed. You own the Architecture Decision Log.

**Lead Engineer**
You lead a delivery squad. You translate stories into technical tasks, define coding standards, conduct or coordinate code reviews, manage technical risk, and mentor junior engineers. You are accountable for technical quality.

**Developer**
You build the software. You implement features from specifications, write unit and integration tests, refactor legacy code, and debug issues. You use AI as a coding co-pilot for every phase of implementation.

**QA Engineer**
You ensure quality. You write test plans, generate test cases from acceptance criteria, build automated test suites, design performance tests, and manage regression coverage. You find defects before users do.

**UAT / Business Readiness**
You validate from the business perspective. You write business-language test scripts, coordinate sign-off, create training materials, produce cutover checklists, and communicate go-live status to stakeholders.

**DevSecOps**
You build and secure the delivery platform. You design CI/CD pipelines with security gates, conduct threat modeling, review IaC for security issues, manage secrets, and respond to security incidents.

**Project Manager**
You run the project. You create project plans, manage risks and issues, produce status reports for steering committees, manage the RACI, handle vendor relationships, and keep the team on track to deliver.

---

## Cross-Persona Workflows

FORGE prompts are designed to chain across personas. The output of one prompt is often the direct input for another.

### Workflow 1: Discovery to Architecture

```
Product Manager
  └─ [PRD Generation] → PRD document
      └─ Business Analyst
            └─ [Business Rule Extraction] → Business Rules Catalog
            └─ [As-Is to To-Be Mapping] → Process Change Register
                └─ Solution Architect
                      └─ [Service Decomposition Analysis] → Bounded Contexts
                      └─ [ADR] → Architecture Decision Log
                      └─ [Non-Functional Requirements] → NFR Specification
```

### Workflow 2: Requirements to Working Software

```
Product Owner
  └─ [Epic Decomposition] → User Stories with Acceptance Criteria
      └─ Lead Engineer
            └─ [Technical Task Breakdown] → Sprint Tasks
                └─ Developer
                      └─ [Feature Implementation] → Code + Tests
                          └─ Lead Engineer
                                └─ [PR Review] → Reviewed, Merged Code
                                    └─ QA Engineer
                                          └─ [Test Case Generation] → Test Results
```

### Workflow 3: Release to Go-Live

```
QA Engineer
  └─ [Regression Suite Design] → Regression Results
      └─ UAT / Business Readiness
            └─ [UAT Test Script Generation] → UAT Results
            └─ [Sign-Off Template] → Signed UAT Document
                └─ Project Manager
                      └─ [Status Report Generation] → Go/No-Go Status
                          └─ UAT / Business Readiness
                                └─ [Go-Live Communication] → Stakeholder Comms
```

### Workflow 4: Security Integration (Shift Left)

```
Solution Architect
  └─ [Reference Architecture Review] → Architecture + NFRs
      └─ DevSecOps
            └─ [Threat Model Generation] → STRIDE Threat Model
            └─ [CI/CD Pipeline Security Design] → Secure Pipeline Definition
                └─ Developer
                      └─ [Feature Implementation] → Code (security patterns applied)
                          └─ DevSecOps
                                └─ [Security Code Review] → Security Findings
```

### Workflow 5: Risk and Governance

```
Business Analyst
  └─ [Gap Analysis] → Gap Register
      └─ Solution Architect
            └─ [Migration Strategy Design] → Phased Migration Plan
                └─ Project Manager
                      └─ [Risk Register Creation] → Risk Register
                      └─ [Steering Committee Deck] → Presentation
                          └─ Product Manager
                                └─ [Stakeholder Communication] → Executive Summary
```

---

## Combining Personas

On small teams or in consulting engagements, individuals often cover multiple roles. Here is guidance on common combinations.

**Developer + Lead Engineer (Tech Lead on a small team)**
Use all prompts from both packs. Start your day in `lead-engineer/` for planning and design work, then switch to `developer/` during implementation sessions.

**Product Manager + Product Owner (startup or solo PM)**
Use the PM prompts for strategy, OKRs, and roadmap. Use the PO prompts for day-to-day backlog management and story writing. The PRD from the PM prompts feeds directly into the Epic Decomposition prompt in the PO pack.

**Business Analyst + QA Engineer**
This combination is common in agile teams without separate QA. The BA's requirements work feeds directly into the QA test case generation prompts. The Data Dictionary from BA work feeds the Test Data Generation prompt in QA.

**DevSecOps + Lead Engineer (Platform/infrastructure team)**
Use the DevSecOps prompts for security and pipeline work, and the Lead Engineer prompts for team standards, onboarding, and technical risk tracking.

**Project Manager + Product Owner (delivery lead on a smaller project)**
Use PM prompts for governance, status reporting, and vendor management. Use PO prompts for backlog ownership and sprint coordination.

---

## Quick Reference Table

| Persona | Key SDLC Stages | Primary Outputs | Key FORGE Prompts |
|---------|----------------|-----------------|-------------------|
| Product Manager | Discovery, Strategy, Release | PRD, OKRs, Roadmap, Release Notes | PRD Generation, OKR Creation, ROI Analysis |
| Product Owner | Requirements, Sprint, UAT | User Stories, DoD, Sprint Goals | Epic Decomposition, User Story Writing, Backlog Refinement |
| Business Analyst | Discovery, Requirements, Design | BRD, Process Maps, Data Dictionary | Business Rule Extraction, As-Is/To-Be Mapping, Gap Analysis |
| Solution Architect | Architecture, Design, Review | ADRs, API Contracts, NFRs, Migration Plan | ADR, Service Decomposition, Migration Strategy |
| Lead Engineer | Design, Build, Review | Technical Tasks, Standards, Risk Register | Technical Task Breakdown, PR Review, Technical Risk Register |
| Developer | Build, Test, Refactor | Code, Tests, Documentation | Feature Implementation, TDD, Legacy Code Analysis |
| QA Engineer | Test, Validation, Release | Test Plan, Test Cases, Defect Reports | Test Plan Generation, API Test Scripts, Performance Test Design |
| UAT / Business Readiness | UAT, Go-Live, Training | UAT Scripts, Sign-Off, Training Docs | UAT Test Script, Cutover Checklist, Go-Live Communication |
| DevSecOps | Build, Security, Infrastructure | Threat Model, Pipeline Config, Runbooks | Threat Model, Security Code Review, CI/CD Pipeline Design |
| Project Manager | All stages | Charter, Risk Register, Status Reports | Project Charter, RACI Matrix, Steering Committee Deck |

---

## How to Use FORGE Prompts

### Step 1 — Select Your Persona
Navigate to your persona directory and open `prompts.md`.

### Step 2 — Identify the Task
Find the prompt that matches what you need to produce. Each prompt includes a **When to use** description to help you pick the right one.

### Step 3 — Gather Inputs
Check the **Inputs needed** section. Collect relevant documents, code snippets, or context before opening your AI tool. The more specific your input, the more useful the output.

### Step 4 — Run the Prompt
Copy the prompt text block. Paste it into your AI tool (GitHub Copilot Chat, Claude Code, watsonx Code Assist, or Cursor). Replace placeholder text in `[square brackets]` with your actual content.

### Step 5 — Validate the Output
Work through the **Review checklist** items before using the output. AI output is a draft — your domain expertise is what makes it production-ready.

### Step 6 — Pass Forward
Where applicable, save your output as the input for the next persona in the workflow. Use the cross-persona workflow maps above to identify who receives your output.

---

## Tool-Specific Notes

| Tool | Best For | Notes |
|------|----------|-------|
| GitHub Copilot Chat | Code-centric prompts | Best with open repo context; use `@workspace` for codebase awareness |
| Claude Code | Long-document generation, analysis | Handles large context windows; great for multi-file analysis |
| watsonx Code Assist | Enterprise IBM environments | Aligned with IBM Cloud and mainframe patterns |
| Cursor | Inline code generation, refactoring | Best for developer prompts with file context active |

---

*FORGE — Framework for Orchestrated AI-Guided Engineering*
*Persona Pack Version 1.0*
