---
stage: "04 — Detailed Design"
purpose: "Translate architecture decisions into implementable specifications"
gate: "Gate 3 (Architecture) must be complete before this stage"
output: "Detailed design specs, data models, sequence diagrams — ready for developers"
---

# Stage 04: Detailed Design

## What This Stage Produces

| Artifact | File | Audience |
|---|---|---|
| Component detailed design | `P1-detailed-design.md` | Lead Engineer, Developers |
| Data model design | `P2-data-model-design.md` | Architects, DBAs, Developers |
| Sequence diagrams | `P3-sequence-diagrams.md` | All technical roles |

## Stage Gate

**Input gate:** Gate 3 (Architecture Complete) must be signed off — ADRs accepted, service map approved, API contracts reviewed.

**Output gate:** Gate 3 output + detailed design reviewed by Lead Engineer before development begins.

## Rules for This Stage

1. **Design to the rules** — every component must trace back to BR-NNN business rules
2. **No gold-plating** — design exactly what is needed for the approved scope
3. **Data ownership is explicit** — every entity has one and only one owning service
4. **Interfaces before implementations** — define ports/contracts before writing code
5. **Error paths are designed, not afterthoughts** — every operation has explicit failure modes

## When to Use Each Prompt

| Situation | Use |
|---|---|
| Breaking a service into components and methods | P1 — Detailed Design |
| Designing database schema or entity relationships | P2 — Data Model Design |
| Designing how components interact across a flow | P3 — Sequence Diagrams |
| All three needed | Run P1 first, then P2 (data layer), then P3 (interactions) |

## Typical Design Session Flow

```
1. Load constitution/01-core-principles.md
2. Load constitution/03-architecture-standards.md
3. Load project-contexts/[your context]/context.md
4. Reference approved service-map from Stage 03
5. Reference business-rules-register.md from Stage 02
6. Run P1 → review → run P2 → review → run P3
```

## Handoff to Stage 05 (Development)

The design artifacts from this stage are the authoritative specification for code generation. Developers must not deviate from the interfaces, error codes, or data contracts defined here without a documented design change.

Pass to Stage 05:
- Component design documents (P1 output per service)
- Entity-relationship design (P2 output)
- Sequence diagrams for all primary and error flows (P3 output)
- Open design questions resolved or escalated
