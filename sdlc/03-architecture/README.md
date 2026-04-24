# Stage 03 — Architecture

**Purpose:** Define the target system architecture: service boundaries, APIs, events, data ownership, and key decisions.

**Who uses this:** Solution Architect (leads), Lead Engineer (co-authors), reviewed by Senior Engineers and Security

**Inputs needed:**
- Stage 02 outputs: user stories, acceptance criteria, NFR register
- For modernization: business rules register from Stage 01/02
- Organizational standards: approved cloud platforms, languages, data stores
- Security and compliance requirements

**Outputs produced:**
- Architecture Decision Records (ADRs)
- Service/bounded context map
- API contracts (OpenAPI)
- Event contracts (schemas)
- Source-to-target traceability map (for modernization)
- Architecture risk register

**Gates before proceeding to Stage 04:**
- [ ] All ADRs reviewed and status = Accepted
- [ ] Service boundaries validated (no data shared across services)
- [ ] API contracts reviewed by consumers
- [ ] Security architect has reviewed the design
- [ ] NFRs are achievable with proposed architecture (performance modeled)

## Prompts in This Stage

| File | Purpose |
|---|---|
| `P1-architecture-decision-records.md` | Generate ADRs for key decisions |
| `P2-service-decomposition.md` | Design service/bounded context map |
| `P3-api-contract-design.md` | Design REST and event contracts |
| `P4-architecture-review.md` | Review a proposed architecture |

## Templates and Skills

| Resource | Purpose |
|---|---|
| [templates/architecture/adr-template.md](../../templates/architecture/adr-template.md) | ADR template for all architectural decisions |
| [templates/architecture/openapi-spec-template.yaml](../../templates/architecture/openapi-spec-template.yaml) | OpenAPI 3.1 spec template — the source of truth for API contracts |
| [templates/architecture/service-design-template.md](../../templates/architecture/service-design-template.md) | Service design document template |
| `/generate-openapi-spec` (Claude skill) | Auto-generate an OpenAPI 3.1 spec from a Stage 2 design document |
| `/create-adr` (Claude skill) | Auto-generate an ADR from a decision description |
