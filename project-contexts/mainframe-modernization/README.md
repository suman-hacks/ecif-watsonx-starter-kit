# FORGE Project Context: Mainframe Modernization

This context covers AI-assisted modernization of mainframe systems — COBOL, PL/1, Assembler, JCL, CICS, IMS, VSAM, DB2 z/OS — to modern distributed platforms.

## Sub-Contexts

| Sub-Context | File | Use When |
|---|---|---|
| COBOL to Java/Spring | `cobol-to-java.md` | Migrating COBOL batch or online programs to Spring Boot |
| CICS Modernization | `cics-modernization.md` | Replacing CICS transactions with REST/event-driven services |
| Batch to Streaming | `batch-to-streaming.md` | Converting JCL batch jobs to Kafka Streams / Spring Batch |

## The Cardinal Rule of Mainframe Modernization

> **Understand the behavior. Preserve the behavior. Then modernize the platform.**

The most common failure in mainframe modernization is generating modern-looking code that doesn't actually preserve the business behavior of the legacy system. FORGE prevents this by requiring complete legacy analysis before any target design or code generation.

## Recommended AI Tool Routing

| Task | Best Tool |
|---|---|
| COBOL source reading and analysis | watsonx Code Assist for Z (WCA4Z) |
| Business rule extraction | Claude Code / watsonx.ai |
| Java/Spring architecture design | Claude Code |
| Java/Spring code generation | Claude Code + GitHub Copilot |
| Code review | Claude Code |

## Modernization Stage Mapping

```
Stage 00 (Pre-Engagement)   → Codebase analysis with T1-T5 prompts
Stage 01 (Discovery)        → COBOL analysis (agents/02-legacy-analyzer-agent.md)
Stage 02 (Requirements)     → Business rule extraction (agents/03-rule-extractor-agent.md)
Stage 03 (Architecture)     → Target design (agents/04-architect-agent.md)
Stage 05 (Development)      → Java generation (agents/05-code-generator-agent.md)
Stage 05 (Review)           → Hardening (agents/06-reviewer-agent.md)
```

## Key Knowledge Packs to Load

- `knowledge-packs/legacy-cobol/` — COBOL reading guide
- `knowledge-packs/microservices/` — target architecture patterns
- `knowledge-packs/event-driven/` — event-driven patterns for mainframe replacement
- `knowledge-packs/domains/payments-banking/` — if modernizing payment systems
