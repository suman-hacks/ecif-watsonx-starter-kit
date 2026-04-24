# FORGE Templates

Ready-to-use templates for all structured outputs produced during AI-assisted development. Each template is designed to be populated by Claude skills or manually by engineers.

---

## Architecture Templates

| Template | Used In | Claude Skill |
|---|---|---|
| [architecture/adr-template.md](architecture/adr-template.md) | Stage 03 — Architecture | `/create-adr` |
| [architecture/openapi-spec-template.yaml](architecture/openapi-spec-template.yaml) | Stage 03 — API contract design | `/generate-openapi-spec` |
| [architecture/service-design-template.md](architecture/service-design-template.md) | Stage 03 — Service decomposition | `/generate-service` |

## Requirements Templates

| Template | Used In | Claude Skill |
|---|---|---|
| [requirements/user-story-template.md](requirements/user-story-template.md) | Stage 02 — Requirements | Manual / BA persona prompts |

## Development Templates

| Template | Used In | Claude Skill |
|---|---|---|
| [development/code-review-checklist.md](development/code-review-checklist.md) | Stage 05 — Code review | `/review-code` |

## Testing Templates

| Template | Used In | Claude Skill |
|---|---|---|
| [testing/test-plan-template.md](testing/test-plan-template.md) | Stage 06 — Testing | `/create-tests` |

## Governance Templates

| Template | Used In | Claude Skill |
|---|---|---|
| [governance/business-rules-register-template.md](governance/business-rules-register-template.md) | Stage 01 — Discovery | `/extract-rules` |
| [assumption-register-template.md](assumption-register-template.md) | All stages | Manual — updated throughout project |
| [decision-log-template.md](decision-log-template.md) | All stages | Manual — updated throughout project |

---

## How Templates Relate to Governance

These templates feed the governance process in [governance/](../governance/):

- ADRs (`adr-template.md`) → reviewed at Stage 3 gate
- Business rules register (`business-rules-register-template.md`) → validated by SME at Stage 1 gate
- Assumption register (`assumption-register-template.md`) → reviewed at every stage gate
- Code review checklist (`code-review-checklist.md`) → required before any merge
- Delivery package (`../governance/delivery-package-template.md`) → compiled from all of the above at each gate

---

*FORGE Templates | Version 2.0*
