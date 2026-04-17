# FORGE Agents

Agents are specialized AI personas with bounded responsibilities. Use them sequentially — like a relay race — passing structured handoff artifacts between stages.

## Agent Roster

| Agent | File | Stage | Role |
|---|---|---|---|
| 01 — Discovery | `01-discovery-agent.md` | Stage 01 | Understand the current system |
| 02 — Legacy Analyzer | `02-legacy-analyzer-agent.md` | Stage 01 | Deep-read COBOL and legacy code |
| 03 — Rule Extractor | `03-rule-extractor-agent.md` | Stage 02 | Extract business rules from analysis |
| 04 — Architect | `04-architect-agent.md` | Stage 03 | Design the target architecture |
| 05 — Code Generator | `05-code-generator-agent.md` | Stage 05 | Generate production code + tests |
| 06 — Reviewer | `06-reviewer-agent.md` | Stage 05 | Review generated artifacts |
| 07 — Coordinator | `07-coordinator-agent.md` | All | Package and validate delivery |

## How to Activate an Agent

**Claude Code / CLAUDE.md:** Add `"Active agent: [Agent Name]"` to your session context, then paste the agent definition.

**Any AI tool:** Paste the agent's full definition as your first message, then proceed with your task.

## Handoff Protocol

Each agent receives specific inputs and produces specific outputs. An agent must NOT proceed if required inputs are missing — it must stop and list what is needed.

The sequence:
```
[01 Discovery] → discovery-report.md
                              ↓
[02 Legacy Analyzer] → legacy-behavior-summary.md (per program)
                              ↓
[03 Rule Extractor] → business-rules-register.md  ← HUMAN GATE
                              ↓
[04 Architect] → service-map.md + api-contracts + event-contracts  ← HUMAN GATE
                              ↓
[05 Code Generator] → implementation + tests  ← HUMAN GATE
                              ↓
[06 Reviewer] → review-report.md
                              ↓
[07 Coordinator] → delivery-package.md  ← FINAL SIGN-OFF
```

## Multi-Tool Agent Routing

| Agent | Recommended Tool | Why |
|---|---|---|
| 01 — Discovery | Claude Code | Best for reading large codebases |
| 02 — Legacy Analyzer | WCA4Z (COBOL) or Claude Code | WCA4Z for mainframe; Claude for general |
| 03 — Rule Extractor | Claude Code / watsonx.ai | Best reasoning for rule extraction |
| 04 — Architect | Claude Code | Best multi-step architectural reasoning |
| 05 — Code Generator | Claude Code + GitHub Copilot | Claude for complex; Copilot for velocity |
| 06 — Reviewer | Claude Code | Best for comprehensive review reasoning |
| 07 — Coordinator | Claude Code | Document synthesis |
