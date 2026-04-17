# FORGE Project Contexts

Project contexts configure FORGE for your specific type of project. Load the relevant context alongside the constitution at the start of every AI session.

## Available Contexts

| Context | Directory | Use When |
|---|---|---|
| Greenfield | `greenfield/` | Building a new system from scratch |
| Brownfield (Distributed) | `brownfield/` | Modifying existing distributed/cloud systems |
| Mainframe Modernization | `mainframe-modernization/` | Migrating COBOL/PL1/CICS/IMS to modern |
| Cloud Migration | `cloud-migration/` | Moving on-prem workloads to cloud |
| API Modernization | `api-modernization/` | Replacing or wrapping legacy APIs |

## How to Load a Context

**Claude Code / CLAUDE.md:** Add a `## Project Context` section citing which context applies.

**GitHub Copilot:** Add to `.github/copilot-instructions.md` under `## Project Type`.

**Any AI tool (chat):** Paste the context file content before your first task prompt.

## Combining Contexts

It's common to combine contexts. Examples:
- Mainframe modernization + Cloud migration → load both, cloud migration is the "to" side
- Brownfield + API modernization → load both if you're wrapping a legacy system's APIs

## Creating Custom Contexts

Copy `greenfield/context.md` as a template and customize for your organization's specific standards.
