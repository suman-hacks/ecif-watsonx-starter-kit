# FORGE Skills — Claude Code Slash Commands
# Load this file to register FORGE workflows as Claude Code skills

> **What are FORGE Skills?** Skills are custom slash commands that trigger structured FORGE workflows. When you type `/skill-name`, Claude executes a multi-step workflow with FORGE guardrails applied automatically — no need to write a long prompt each time.
>
> **Setup:** Add this file's path to your `CLAUDE.md` under the `## Custom Slash Commands` section, or reference it at the start of your session.

---

## How to Use Skills

```bash
# In your terminal, from the project root:
claude

# Then type any skill command:
/analyze-legacy [paste COBOL source or provide file path]
/generate-service PaymentAuthorization --context=.context/ATOM_CHASSIS.md
/extract-rules AUTHZ0100.cbl
/review-code src/main/java/com/example/service/AuthorizationService.java
```

---

## Available Skills

---

### `/analyze-legacy`

**Purpose:** Analyze a legacy source file (COBOL, PL/1, TIBCO process, legacy Java) and produce a structured understanding document.

**Triggers:** FORGE Stage 1 — Legacy Understanding workflow.

**Claude will:**
1. Read and parse the provided legacy source file(s)
2. Identify: program purpose, inputs/outputs, data structures, business rules, external calls (CICS, DB2 SQL, MQ, file I/O, called programs), and error handling paths
3. Output a structured analysis with FACT/ASSUMPTION labels and confidence levels
4. Extract all business rules numbered as `BR-NNN`
5. List open questions that must be resolved before modernization begins
6. Produce initial traceability mapping: legacy program → candidate ATOM service

**Usage:**
```
/analyze-legacy

[Paste COBOL source code here]
-- or --
File path: legacy-artifacts/cobol/AUTHZ0100.cbl
```

**Output:** `analysis/[program-name]-understanding.md` with sections: Purpose, Tech Stack, Data Structures, Business Rules, External Dependencies, Error Handling, Open Questions, Traceability Map.

---

### `/extract-rules`

**Purpose:** Extract and inventory all business rules from a source artifact (code, specification, or document).

**Triggers:** FORGE Stage 1 — Business Rule Extraction workflow.

**Claude will:**
1. Analyze the provided source code or documentation
2. Extract and classify business rules into categories: validation, calculation, routing, limit/threshold, status check, regulatory, fallback/default
3. Format each rule as: `BR-NNN | Category | Rule Statement | Source Location | Confidence Level | Open Questions`
4. Flag rules that are ambiguous, implicit, duplicated, or potentially outdated
5. Identify rules that live in: code, config files, database tables, rules engines, or external services
6. Produce an assumption register entry for any inferred rules

**Usage:**
```
/extract-rules

[Paste the legacy code, document, or specification]
-- or --
File path: legacy-artifacts/cobol/AUTHZ0100.cbl
```

**Output:** `analysis/business-rules-register.md` with full rules inventory.

---

### `/generate-service`

**Purpose:** Generate a complete ATOM microservice from a specification or design document.

**Triggers:** FORGE Stage 3 — Code Generation workflow.

**Pre-conditions (Claude will verify before generating):**
- Stage 1 (Legacy Understanding) complete
- Stage 2 (Target Mapping/Design) complete and approved

**Claude will:**
1. Confirm pre-conditions are met before generating any code
2. Generate the full ATOM service structure: Controller, Application Service, Domain Model, Repository, Infrastructure adapters
3. Apply all ATOM annotations (`@AtomService`, `@AtomRepository`, `ApiResponse<T>`, `@CircuitBreaker`)
4. Apply all CORE_SKILLS guardrails (no secrets, structured logging, input validation)
5. Label any behavioral differences from legacy code as `[BEHAVIORAL CHANGE: CBD-NNN]`
6. Generate unit tests alongside every class (JUnit 5 + Mockito + AssertJ)
7. Generate integration test scaffolding using Testcontainers
8. Produce traceability table: generated class → source legacy program → source business rule

**Usage:**
```
/generate-service

Service name: PaymentAuthorizationService
Context files: .context/ATOM_CHASSIS.md, .context/CORE_SKILLS.md
Design spec: [paste the Stage 2 design document or file path]
Business rules: [paste the rules register or file path]
```

---

### `/generate-atom-service`

**Purpose:** Scaffold a brand-new ATOM microservice from scratch (greenfield or net-new service).

**Claude will:**
1. Generate the full ATOM project structure with correct package layout
2. Create: Controller, Application Service, Domain model, Repository interface + JPA implementation, Kafka listener/producer (if needed)
3. Generate `application.yml` with all ATOM configuration sections filled
4. Generate `Dockerfile` and basic k8s deployment YAML
5. Generate unit and integration tests
6. Apply all FORGE guardrails

**Usage:**
```
/generate-atom-service

Service name: [e.g., fraud-scoring-service]
Domain: [e.g., fraud detection]
API: [describe the API — e.g., "POST /v1/scores accepts a transaction, returns a fraud score 0-100"]
Integrations: [e.g., "calls core-banking-service to get account history, publishes score to Kafka topic fraud.scores"]
```

---

### `/review-code`

**Purpose:** Review code against FORGE constitution, ATOM patterns, and CORE_SKILLS guardrails.

**Triggers:** FORGE Stage 4 — Review and Hardening workflow.

**Claude will review for:**
1. **Correctness** — Does it implement the specified behavior? Cross-check against business rules register.
2. **ATOM conformance** — Does it follow ATOM_CHASSIS.md patterns (annotations, layers, response format)?
3. **Security** — SG-1 through SG-6 from CORE_SKILLS.md. Flag any violation immediately with `[SECURITY: SG-N]`.
4. **Testability** — Is code structured for testability? Are tests present, meaningful, and covering edge cases?
5. **Observability** — Structured logging, correlation ID propagation, metrics at entry points?
6. **FORGE constitution** — All 12 principles from `constitution/01-core-principles.md`?
7. **Behavioral changes** — Any deviations from legacy behavior that are not documented as `[BEHAVIORAL CHANGE: CBD-NNN]`?

**Output format:** Findings organized as:
- `CRITICAL` — Must fix before merge (security, behavioral deviation, test failure)
- `MAJOR` — Should fix before merge (missing tests, ATOM non-conformance, missing observability)
- `MINOR` — Fix when convenient (naming, formatting, documentation)

**Usage:**
```
/review-code

[Paste code or provide file paths]
-- or --
Files: src/main/java/com/example/service/AuthorizationService.java
       src/test/java/com/example/service/AuthorizationServiceTest.java
Business rules register: analysis/business-rules-register.md
```

---

### `/create-tests`

**Purpose:** Generate a comprehensive test suite for the provided code or specification.

**Claude will:**
1. Analyze the provided code or specification and identify all testable behaviors
2. Generate a test strategy: what to unit test vs. integration test, edge cases, concurrency scenarios
3. Generate JUnit 5 unit tests with Mockito + AssertJ covering:
   - All happy paths
   - All documented business rules (one test per rule)
   - Boundary conditions (null, empty, max value, min value)
   - All error/exception paths
4. Generate Testcontainers integration test scaffolding for database and messaging dependencies
5. Include performance test outline if latency SLAs are defined

**Usage:**
```
/create-tests

[Paste the class to test, or provide file path]
Business rules: [paste rules that should be covered]
```

---

### `/map-data`

**Purpose:** Map source data structures (COBOL copybooks, VSAM layouts, DB2 tables) to target ATOM domain models.

**Claude will:**
1. Map every source field to its target equivalent using MODERNIZATION.md type mapping rules
2. Flag: type mismatches, precision differences, encoding differences (EBCDIC vs. UTF-8), packed decimal issues
3. Generate Java domain model classes (immutable, Lombok `@Value`)
4. Generate MapStruct mappers for DTO ↔ Domain mapping
5. Generate COBOL-to-Java conversion utilities for data types (e.g., packed decimal parser)
6. Identify data migration risks and required transformation logic
7. Flag any fields that appear in source but have no clear target mapping

**Usage:**
```
/map-data

Source: [paste COBOL copybook or DB2 DDL]
Target: [describe the target domain model, or leave blank to generate from source]
```

---

### `/create-adr`

**Purpose:** Generate an Architecture Decision Record (ADR) for a significant design decision.

**Claude will:**
1. Produce an ADR in the project's standard format
2. Sections: Title, Status, Date, Context (why this decision was needed), Decision (what was decided), Rationale (why this over alternatives), Alternatives Considered (what else was evaluated and why rejected), Consequences (positive and negative outcomes), Follow-up Required
3. All facts cited to source artifacts; all assumptions labeled
4. Flag any architectural risks associated with the decision

**Usage:**
```
/create-adr

Decision: [describe the architectural decision — e.g., "Use Kafka for async communication between authorization and fraud services instead of synchronous REST"]
Context: [why is this decision needed now?]
Alternatives: [what other options were considered?]
```

---

### `/pre-engagement`

**Purpose:** Run the full 5-task pre-engagement analysis for a legacy system modernization workshop.

**Triggers:** FORGE Stage 0 — Pre-Engagement Analysis workflow (all 5 tasks in sequence).

**Claude will guide you through:**
1. **T1** — System Architecture Analysis → produces `TX_ARCH.md`
2. **T2** — Decision Logic Inventory → produces `TX_DECISION_INVENTORY.md`
3. **T3** — Integration & Latency Map → produces `TX_INTEGRATIONS.md`
4. **T4** — Complexity & Risk Map → produces `TX_RISK_MAP.md`
5. **T5** — POC Option Analysis → produces `TX_POC_OPTIONS.md`

Run each task separately using:
```
/analyze-arch     # T1
/analyze-rules    # T2
/analyze-integrations  # T3
/analyze-risk     # T4
/generate-poc-options  # T5 (requires T1-T4 outputs)
```

Or run the full sequence:
```
/pre-engagement

Project: [project name]
Codebase: [describe the system or provide entry points]
```

---

### `/explain-legacy`

**Purpose:** Explain what a piece of legacy code does in plain English for non-technical stakeholders or new team members.

**Claude will:**
1. Explain the business purpose in plain language (no COBOL/technical jargon)
2. Describe the input, output, and key decisions
3. Highlight any business rules that are encoded in the logic
4. Note any areas that are confusing, poorly documented, or high-risk

**Usage:**
```
/explain-legacy

Audience: [Business Analyst | Product Manager | New Developer | Workshop Participants]
[Paste COBOL/legacy code]
```

---

### `/runbook`

**Purpose:** Generate an operational runbook for a service.

**Claude will generate:**
1. Service overview and business purpose
2. Prerequisites (access, tools, environment)
3. Health check procedures (how to verify the service is healthy)
4. Common failure scenarios and resolution steps
5. Escalation paths
6. Rollback procedure

**Usage:**
```
/runbook

Service: [service name]
Context: [paste service code, API spec, or architecture description]
```

---

### `/generate-openapi-spec`

**Purpose:** Generate an OpenAPI 3.1 specification from a Stage 2 service design document or an existing ATOM service. Produces a machine-readable `.yaml` contract that is the source of truth for consumer teams, API gateway configuration, Java stub generation, and contract testing.

**Why spec-first matters:** Writing the OpenAPI spec before implementation locks the API contract so consumer teams can start integration work in parallel. It also forces design decisions (response shapes, error codes, field types) to be made explicitly before code is written, rather than discovered after.

**Pre-conditions (Claude will verify):**
- Stage 2 (Target Mapping) complete — service name, endpoints, request/response shapes, business rules known
- Domain context loaded (`.context/PAYMENTS_DOMAIN.md` for payments, or domain glossary)

**Claude will:**
1. Generate a complete OpenAPI 3.1 `.yaml` spec following the `templates/architecture/openapi-spec-template.yaml` structure
2. Define all endpoints with correct HTTP methods, path parameters, and query parameters
3. Define all request and response schemas with: correct field types, format annotations (date-time, int64), pattern constraints for sensitive fields (PAN masking enforced by pattern), and required/optional designations
4. Map ISO 8583 response codes to the response schema enum if this is a payments service
5. Include worked examples for happy path and key error cases
6. Add security scheme definitions (bearer JWT or API key per project standards)
7. Annotate the spec with FORGE traceability comments linking schemas to business rules (BR-NNN)
8. Include `x-forge-notes` extensions for breaking change policy and consumer review requirements

**Usage:**
```
/generate-openapi-spec

Service: PaymentAuthorizationService
Context: #file:.context/PAYMENTS_DOMAIN.md
Design doc: [paste Stage 2 service design or file path]
Endpoints needed:
  - POST /v1/authorizations — submit an authorization request
  - GET /v1/authorizations/{id} — retrieve an authorization decision
  - GET /v1/limits/{maskedPan} — query card spending limits
Business rules to encode: BR-001 (card status), BR-003 (daily limit), BR-INT-001 (amounts in minor units)
```

**Output:** `docs/architecture/[service-name]-api-spec.yaml` — OpenAPI 3.1 compliant, validated against the spec template.

**After generation:**
```bash
# Preview in Swagger UI (no install needed)
npx @redocly/cli preview-docs docs/architecture/[service-name]-api-spec.yaml

# Validate the spec
npx @redocly/cli lint docs/architecture/[service-name]-api-spec.yaml

# Generate Java Spring stubs from the spec
openapi-generator-cli generate -i docs/architecture/[service-name]-api-spec.yaml \
  -g spring -o src/generated --additional-properties=useSpringBoot3=true
```

---

### `/package-delivery`

**Purpose:** Package and validate all artifacts from a completed FORGE stage into a delivery package for stakeholder review, audit, and handoff.

**Triggers:** End of any FORGE stage, or at any delivery milestone where human review and sign-off is required.

**Pre-conditions (Claude will verify before packaging):**
- All required artifacts for this stage are available
- Review gate checklist from `governance/review-gates.md` has been evaluated

**Claude will:**
1. Verify completeness — identify any missing required artifacts for this stage
2. Cross-check artifact consistency — business rules in the register must match rules in code; API contracts must match implementations
3. Build the full traceability matrix: `BR-NNN → Requirement/Story → Architecture Component → Implementation File → Test File → Test Status`
4. Evaluate the applicable review gate checklist from `governance/review-gates.md`
5. List all unresolved open questions and gaps from prior stage outputs
6. Compile the decisions log (decisions made, who made them, rationale)
7. Determine sign-off requirements and current status
8. Identify what must be resolved before proceeding to the next stage
9. Produce a delivery package that an auditor can use to trace any requirement to its implementation

**Output format:** Uses the standard FORGE Delivery Package template from `governance/delivery-package-template.md`.

**Usage:**
```
/package-delivery

Stage: [e.g., Stage 1 — Legacy Understanding]
Project: [project name]

Artifacts available:
- Discovery report: [paste or file path]
- Business rules register: [paste or file path]
- Open questions from analysis: [paste all open Q# items]
- Code review report (if Stage 3+): [paste or file path]

Gate to evaluate: [Gate N from governance/review-gates.md]
```

**Output:** A complete delivery package document following the template in `governance/delivery-package-template.md`.

**Note:** This skill documents gaps rather than hiding them. A gap is a finding, not a blocker — the business decides whether to accept or resolve it. Claude will not mark a gate as PASSED unless every mandatory check is complete.

---

## Skill Configuration (for `CLAUDE.md`)

Add the following to your project's `CLAUDE.md` to make all skills available:

```markdown
## FORGE Skills

The following slash commands are available in this session.
See `tool-setup/claude-code/SKILLS.md` for full documentation.

- `/analyze-legacy` — Analyze a legacy source file (FORGE Stage 1)
- `/extract-rules` — Extract business rules from source artifacts
- `/generate-service` — Generate a complete ATOM service from a spec
- `/generate-atom-service` — Scaffold a new ATOM service from scratch
- `/review-code` — Review code against FORGE + ATOM standards
- `/create-tests` — Generate comprehensive test suite
- `/map-data` — Map legacy data structures to Java domain models
- `/create-adr` — Generate Architecture Decision Record
- `/pre-engagement` — Run full 5-task pre-engagement analysis
- `/explain-legacy` — Explain legacy code in plain English
- `/runbook` — Generate operational runbook
- `/generate-openapi-spec` — Generate an OpenAPI 3.1 spec from a Stage 2 service design
- `/package-delivery` — Package stage artifacts into a traceable delivery package for sign-off

Always apply `.context/CORE_SKILLS.md` and `.context/ATOM_CHASSIS.md` guardrails
when executing any skill.
```

---

*FORGE Skills v2.0 — Claude Code slash command catalog*
*See `constitution/01-core-principles.md` for the behavioral rules that govern all skill outputs.*
