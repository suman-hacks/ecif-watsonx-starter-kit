# Persona: Solution Architect

## Role Overview

Solution Architects use FORGE to design target-state architectures grounded in discovery findings, business requirements, and non-functional requirements. They work at the intersection of business requirements and technical implementation — ensuring the system as designed can actually be built, operated, and evolved by the team.

## What AI Can Help You With

- Drafting Architecture Decision Records (ADRs) from requirements
- Proposing service decompositions from business domain analysis
- Designing REST and event-driven API contracts
- Reviewing proposed designs for pattern violations and risks
- Mapping legacy program structures to modern service boundaries
- Generating service design documents for team consumption

## How to Use FORGE

1. Load `constitution/01-core-principles.md` + `constitution/03-architecture-standards.md` into your AI session
2. Load the relevant knowledge packs: `knowledge-packs/microservices/`, `knowledge-packs/event-driven/`, `knowledge-packs/api-design/`
3. For mainframe projects, also load `project-contexts/mainframe-modernization/cobol-to-java.md`
4. Use prompts from `sdlc/03-architecture/` for stage-specific work
5. Use prompts in `prompts.md` here for cross-cutting architect tasks

## Key Outputs You Produce

| Output | Template | Where Stored |
|---|---|---|
| ADRs | `templates/architecture/adr-template.md` | `docs/architecture/decisions/` |
| Service design | `templates/architecture/service-design-template.md` | `docs/architecture/services/` |
| API contracts | `sdlc/03-architecture/P3-api-contract-design.md` | `docs/architecture/api-contracts/` |
| Event contracts | `sdlc/03-architecture/P3-api-contract-design.md` | `docs/architecture/event-contracts/` |
| Source-to-target map | `templates/governance/` | `analysis/source-to-target-map.md` |

## SDLC Stages You Lead

- Stage 03 (Architecture) — primary driver
- Stage 02 (Requirements) — NFR definition (co-author with Lead Engineer)
- Stage 04 (Design) — reviewer and approver
- Stage 07 (Security) — architecture review input
