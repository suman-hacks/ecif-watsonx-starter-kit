# Stage 01: Discovery

## Purpose

Discovery is the structured process of deeply understanding the problem space, the existing systems, and the stakeholder landscape before committing to any solution. It translates business needs, legacy system behavior, and operational constraints into a shared, documented understanding that all subsequent SDLC stages can build on.

Unlike pre-engagement (Stage 00), which is done quickly from code alone, Discovery involves human interviews, business process analysis, and iterative investigation. AI assists in preparation, synthesis, and documentation — but the insights come from people, not just code.

## When to Use

- After a POC scoping workshop selects a direction (following Stage 00)
- At the start of any greenfield or modernization project
- When bringing a new team onto an existing product
- Before any significant architectural change
- When a system's behavior is poorly understood or undocumented

## Who Runs It

| Role | Responsibility |
|---|---|
| Business Analyst / Product Owner | Leads stakeholder interviews; owns requirements discovery |
| Lead Engineer / Solution Architect | Leads technical discovery; owns system analysis |
| Workshop Facilitator | Facilitates discovery workshops; owns agenda and outputs |
| Domain Expert (from client) | Participates in interviews; validates findings |
| QA Lead (optional) | Contributes to testability and acceptance criteria discovery |

**Duration:** Typically 1–4 weeks depending on system complexity. A greenfield MVP may complete discovery in 3–5 days; a legacy modernization program may require 4+ weeks.

## Output Files Produced

All outputs are placed in a `discovery/` folder within the project workspace.

| File | Prompt | Contents |
|---|---|---|
| `DISCOVERY_INTERVIEW_GUIDE.md` | P1 | Stakeholder map, interview questions by role, constraints to validate |
| `DISCOVERY_LEGACY_ANALYSIS.md` | P2 | Comprehensive legacy system inventory, data flow, pain points |
| `DISCOVERY_CURRENT_STATE.md` | P3 | Current-state assessment ready for stakeholder review |

These documents feed directly into Stage 02 Requirements as the primary input for user story generation.

## Process

```
1. Pre-Discovery Preparation
   ├── Review T1–T5 documents from Stage 00 (if available)
   ├── Run P1 to generate stakeholder interview guide
   └── Schedule interviews with all stakeholder types

2. Stakeholder Interviews (human-led, AI-assisted note synthesis)
   ├── Conduct interviews using P1 guide
   ├── Record key quotes, decisions, and unknowns
   └── After each interview: use AI to synthesize notes

3. Legacy System Deep Dive
   ├── Run P2 against any additional codebases or documentation
   ├── Cross-reference with Stage 00 T1–T4 findings
   └── Interview system owners to fill gaps

4. Current State Assessment Synthesis
   ├── Run P3 to synthesize all discovery inputs
   ├── Share draft with stakeholders for validation
   └── Incorporate feedback and finalize

5. Handoff to Requirements (Stage 02)
   └── DISCOVERY_CURRENT_STATE.md becomes primary input for P1-user-story-generation
```

## Discovery Principles

**Separate observation from interpretation.** During discovery, record what the system does and what stakeholders say. Reserve interpretation (what should change and why) for the requirements and architecture stages.

**Name the unknowns explicitly.** Every discovery produces new unknowns. Document them as first-class items with owners and resolution plans. Undocumented unknowns become assumptions, and assumptions become bugs.

**Validate against the code.** Stakeholder descriptions of how the system works are frequently wrong or outdated. Always cross-reference interview findings with T1–T4 code analysis.

**Capture the "why," not just the "what."** The most valuable discovery output is understanding *why* the system works the way it does — what business constraint, historical decision, or technical limitation led to the current design. This context prevents re-introducing the same problems in the new system.

## Tool Guidance

| Tool | Guidance |
|---|---|
| **GitHub Copilot** | Most useful for P2 (legacy analysis). Use workspace context for code-grounded analysis |
| **Claude Code** | Excellent for P2 and P3 synthesis. Can cross-reference multiple documents |
| **watsonx Code Assist** | Use for code analysis in P2. Use IBM Watson NLP tools for interview note synthesis if available |
| **Cursor** | Use Composer with multi-file context for P3 synthesis |

## Workshop Connection

Discovery outputs feed into:
- **Stage 02 Requirements:** DISCOVERY_CURRENT_STATE.md → user stories, acceptance criteria, NFRs
- **Stage 03 Architecture:** DISCOVERY_LEGACY_ANALYSIS.md → service decomposition, ADRs
- **Stage 06 Testing:** DISCOVERY_CURRENT_STATE.md → test strategy, UAT scenarios

## Quality Bar

Discovery is complete when:
- [ ] All stakeholder types have been interviewed at least once
- [ ] DISCOVERY_LEGACY_ANALYSIS.md has been reviewed by a technical team member who knows the legacy system
- [ ] DISCOVERY_CURRENT_STATE.md has been reviewed and signed off by the primary business stakeholder
- [ ] All documented unknowns have owners and resolution plans
- [ ] No major contradictions remain between interview findings and code analysis
- [ ] The Requirements lead has confirmed the discovery outputs are sufficient to begin writing user stories

---

*FORGE Framework — Stage 01: Discovery | Previous Stage: 00-Pre-Engagement | Next Stage: 02-Requirements*
