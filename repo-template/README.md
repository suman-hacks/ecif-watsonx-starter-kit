# Java/Spring Modernization Repo Template

This template shows how to run ECIF across a real modernization repository for a legacy payments use case.

## Layout

- `legacy-artifacts/` - source COBOL, copybooks, JCL notes, DB2 schemas, and screenshots.
- `analysis/` - extracted behavior, business rules, dependencies, and risks.
- `traceability/` - spreadsheets and markdown logs linking source to target.
- `modernization-workflow/` - staged prompts, outputs, approvals, and review notes.
- `services/authorization-service/` - target Spring Boot service.
- `shared/` - common Java libraries, API schemas, event contracts.
- `orchestrate-pack/` - watsonx Orchestrate-ready workflow assets.

## Typical sequence

1. Drop legacy artifacts into `legacy-artifacts/`.
2. Run prompt bundle 01 for source understanding.
3. Save extracted outputs in `analysis/`.
4. Approve design and traceability.
5. Run prompt bundle 02 and 03 to generate target code.
6. Review using prompt bundle 04 and quality gates.
7. Package approved outputs into `services/`.

