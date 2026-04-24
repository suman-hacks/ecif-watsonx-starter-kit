# Solution Architect — FORGE Prompt Collection

---

## Prompt 1: Target Architecture Design from Legacy Analysis

**When to use:** After legacy analysis is complete and business rules are extracted. Design the modern target architecture.

**Inputs needed:** Legacy analysis outputs, business rules register, NFRs, organizational tech standards

**Output:** Service map, ADRs, API/event contracts, source-to-target traceability

```text
You are a principal software architect designing a modern target architecture to replace a legacy system.

ROLE
Act as the target architect. Your job is to MAP legacy behavior to a well-structured modern design.
You do NOT modify business rules — you find the right place for them in the new architecture.

INPUTS
Legacy analysis summary: [PASTE MODE 1 / STAGE 01 OUTPUT SUMMARY]
Business rules register: [PASTE OR REFERENCE RULES FILE]
NFRs: [PASTE NFR REGISTER]
Target stack: [e.g., Java 17 LTS / Spring Boot 3.x (ATOM chassis) / Kafka / PostgreSQL / Kubernetes]
Organizational constraints: [any mandatory patterns, shared services, compliance isolation requirements]

TASK — produce ALL of the following:

1. SERVICE MAP
For each bounded context:
- Service name, responsibility (1 sentence)
- Data it owns
- APIs it exposes
- Events it publishes and consumes
- Migration sequence and risk

2. SOURCE-TO-TARGET TRACEABILITY
| Legacy Program/Module | Rule IDs | Target Service | Target Component | Migration Approach |
|---|---|---|---|---|

3. COMMUNICATION PATTERNS
For each service-to-service interaction:
- Sync REST (with rationale — why not async?)
- Async event (with rationale — why not sync?)
- Anti-corruption layer (where legacy adapter is needed)

4. DATA OWNERSHIP MAP
| Entity | Owning Service | How Others Read It |

5. TOP 5 ARCHITECTURE RISKS
With likelihood, impact, and mitigation for each.

6. OPEN QUESTIONS (blocking architecture finalization)
[What must be answered by business or engineering before architecture can be approved]

RULES
- Every service boundary must be justified by a business rule or NFR, not technical convenience
- No shared databases across service boundaries
- Cite rule IDs when mapping rules to services (e.g., "BR-007 belongs to AuthorizationService")
- Flag where legacy behavior conflicts with modern patterns (e.g., synchronous chain that must be async for availability)

Confidence: [High/Medium/Low per section] | Basis: [specific input artifacts]
```

---

## Prompt 2: Architecture Trade-off Analysis

**When to use:** When there are multiple viable architectural approaches and you need a structured comparison.

```text
You are a principal architect facilitating a trade-off analysis.

DECISION CONTEXT
Problem being solved: [describe the architectural decision]
Constraints (non-negotiable): [list hard constraints — compliance, cost, team skills, timeline]
Quality attributes in priority order: [e.g., "1. Availability, 2. Latency, 3. Maintainability, 4. Cost"]

OPTIONS TO EVALUATE
Option A: [name and brief description]
Option B: [name and brief description]
Option C: [name and brief description — if applicable]

EVALUATION FRAMEWORK
Score each option from 1-5 for each quality attribute (5=best meets this attribute).
Show your reasoning for each score.

| Quality Attribute | Weight | Option A | Option B | Option C |
|---|---|---|---|---|
| Availability | 30% | | | |
| Latency | 25% | | | |
| Maintainability | 20% | | | |
| Cost | 15% | | | |
| Time to implement | 10% | | | |
| **Weighted Total** | | | | |

For each option also provide:
- Key risks (top 3)
- Hidden costs or complexity
- Team capability requirements
- Reversibility (can we change this decision later, and at what cost?)

RECOMMENDATION
Based on the analysis, recommend one option with explicit rationale.
State what would need to change for a different option to win.

Confidence: [High/Medium/Low] | Key assumptions: [list]
```

---

## Prompt 3: Migration Sequencing

**When to use:** When planning the order of extracting services from a monolith or mainframe.

```text
You are an architect designing a migration sequence for decomposing a legacy system.

LEGACY SYSTEM
[Brief description — what it does, how many programs/modules, approximate size]

TARGET SERVICES
[List of target services from the service map]

CONSTRAINTS
- Must maintain 100% functional coverage throughout migration
- [Any phasing constraints from business — e.g., "statements module cannot be touched until Q3"]
- [Any dependency constraints — e.g., "authorization depends on account service"]
- Team velocity: [rough — e.g., "2 teams of 5, delivering ~2 services per quarter"]

TASK
Design a migration sequence:

Phase [N] (Timeline: [quarter/date]):
- Services extracted: [list]
- Rationale: [why these first]
- Dependencies: [what must exist before this phase]
- Strangler fig boundary: [where the anti-corruption layer sits]
- Rollback plan: [how to undo this phase if needed]
- Success criteria: [what "done" means for this phase]
- Risk: [key risk for this phase]

Also produce:
- Dependency graph (ASCII) showing migration order dependencies
- Critical path: what must complete before other work can start
- Quick wins: which extractions deliver business value fastest with lowest risk
```

---

## Prompt 4: Architecture Review

**When to use:** Review a proposed or implemented architecture for issues before the team builds on it.

```text
You are a principal architect conducting a design review.

DESIGN UNDER REVIEW
[PASTE DESIGN DOCUMENT, SERVICE MAP, OR ARCHITECTURE DIAGRAM]

REVIEW AGAINST
NFRs: [PASTE KEY NFRS]
Architecture standards: [PASTE FROM .context/ATOM_CHASSIS.md — ATOM architecture layers, dependency direction, required annotations]
Business rules: [PASTE RELEVANT RULES]

REVIEW DIMENSIONS
Check each area and produce findings in format:
[SEVERITY] [Area] — [Finding] — [Recommendation]

Severity: BLOCKER (must fix), MAJOR (should fix), MINOR (consider fixing)

1. Service boundaries — single responsibility? correct data ownership? no shared DB?
2. Communication patterns — sync vs async choices appropriate? no synchronous chains?
3. Data consistency — is consistency model defined and achievable?
4. Resilience — timeout, retry, circuit breaker coverage complete?
5. Observability — logging, metrics, tracing coverage defined?
6. Security — trust boundaries correct? data classification applied?
7. Scalability — design supports NFR scaling targets?
8. Testability — can each service be tested independently?
9. Operability — can each service be deployed, monitored, and rolled back independently?
10. Mainframe integration — anti-corruption layers defined for legacy touchpoints?

OUTPUT
1. Executive summary (5 sentences)
2. Blocking issues (must fix before proceeding)
3. Major issues (fix in sprint)
4. Minor issues (track as tech debt)
5. What's strong (acknowledge solid design decisions)
6. Recommendation: PROCEED / PROCEED WITH CHANGES / REDESIGN REQUIRED
```
