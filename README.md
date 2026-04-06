# ECIF Watsonx Starter Kit v2
Enterprise Coding Intelligence Framework (ECIF) starter kit focused on bank and payments modernization.

## What changed in v2
This version adds **payments-specific knowledge packs** and **Watsonx-oriented working assets** for:
- COBOL and batch modernization
- ICS-style legacy servicing decomposition
- API abstraction and partner-facing patterns
- Event-driven modernization and replay/recovery design
- Spring Boot target-state patterns for regulated enterprise systems

## New payments-specific packs
- `ai-framework/knowledge-packs/payments-cobol-modernization/`
- `ai-framework/knowledge-packs/ics-modernization/`
- `ai-framework/knowledge-packs/payments-api-abstraction/`
- `ai-framework/knowledge-packs/event-driven-modernization-payments/`
- `ai-framework/knowledge-packs/spring-boot-target-patterns/`
- `ai-framework/knowledge-packs/payments-testing-observability/`

## Intended usage
1. Load the constitution files as always-on rules.
2. Select one or more payments-specific knowledge packs based on task.
3. Use the Watsonx prompt modes in sequence:
   - legacy understanding
   - target mapping
   - code generation
   - review/hardening
4. Save outputs into traceability and memory artifacts.

## Suggested first pilot
Run one narrow module through the workflow:
- one COBOL program or batch step
- one ICS servicing function
- one API abstraction use case
- one event publishing/recovery flow

## Important
This kit is **bank-grade but generic**. It does not contain confidential or proprietary bank logic. Teams should adapt the content to their own enterprise standards, controls, and system vocabulary.


## Version 3 additions
- actual Java/Spring modernization repo template under `repo-template/`
- sample ICS authorization prompt bundle under `sample-bundles/ics-authorization-use-case/`
- traceability spreadsheet templates under `traceability-spreadsheets/`
- watsonx Orchestrate-ready workflow pack under `repo-template/orchestrate-pack/`
- v3 usage guide under `docs/v3-usage-guide.md`
