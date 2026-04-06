# Version 3 Usage Guide

Version 3 adds:
- an actual Java/Spring modernization repository template
- a sample ICS authorization prompt bundle
- spreadsheet-based traceability templates
- a watsonx Orchestrate workflow pack

## Recommended pilot path
1. Copy `repo-template/` into a working repository.
2. Place one real legacy module in `legacy-artifacts/`.
3. Use `sample-bundles/ics-authorization-use-case/01-legacy-understanding.md`.
4. Save outputs under `analysis/` and `traceability/`.
5. After approval, use prompts 03 and 04 to generate target code.
6. Run prompt 05 and complete the review package.
7. Package the markdown, JSON, spreadsheet, and code artifacts for review.

## Suggested bank usage
- Keep ECIF under version control.
- Require review sign-off after business rule extraction and target mapping.
- Use the orchestrate pack for repeatable, auditable workflows.
