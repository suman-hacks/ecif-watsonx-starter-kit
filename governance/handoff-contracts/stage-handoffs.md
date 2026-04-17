# FORGE Stage Handoff Contracts

Each handoff contract defines the exact artifacts that must be present when passing work from one stage (or agent) to the next. An agent or stage that receives an incomplete handoff must STOP and list what is missing before proceeding.

---

## Handoff 00→01: Pre-Engagement → Discovery

**From:** Stage 00 (Pre-Engagement)  
**To:** Stage 01 (Discovery)  
**Gate:** Gate 0 (Pre-Engagement Complete)

### Required Artifacts

| Artifact | File | Mandatory? | Notes |
|---|---|---|---|
| System architecture analysis | `analysis/T1-architecture.md` | Yes | Tech stack, component inventory, ASCII diagram |
| Decision logic inventory | `analysis/T2-decision-logic.md` | Yes | Rule categories, top complex paths |
| Integration and latency map | `analysis/T3-integrations.md` | Yes | All inbound/outbound, stand-in behavior |
| Complexity and risk map | `analysis/T4-complexity.md` | Yes | Risk hotspots, test coverage gaps |
| POC option analysis | `analysis/T5-poc-options.md` | If POC planned | Option comparison matrix |

### Handoff Summary Fields (required in T1 for context-window efficiency)
```markdown
## Handoff Summary for Stage 01
- System type: [CICS Online / Batch / REST API / Hybrid]
- Primary technology: [COBOL/Java/Python/etc.]
- Business domain: [Payments / Account Management / etc.]
- Highest-risk component for deep analysis: [PROGRAM-NAME — reason]
- Outstanding decisions needed before Discovery: [list or "None"]
- Human gate status: [PASSED — signed by Lead Engineer + Architect on DATE]
```

### Consuming Agent Check (Agent 01 or Lead Engineer)
Before beginning Stage 01:
- [ ] All 4 mandatory artifacts present
- [ ] Gate 0 signed off (Lead Engineer + Architect)
- [ ] Handoff summary includes risk prioritization for discovery
- [ ] No blocking unknowns that prevent starting discovery

---

## Handoff 01→02: Discovery → Legacy Analysis

**From:** Stage 01 (Discovery) / Agent 01  
**To:** Stage 02 (Legacy Analysis) / Agent 02  
**Gate:** Gate 1 (Discovery Complete)

### Required Artifacts

| Artifact | File | Mandatory? |
|---|---|---|
| Discovery report | `analysis/discovery-report.md` | Yes |
| System inventory (complete) | In discovery report | Yes |
| Priority list for deep analysis | In discovery report | Yes |
| Open questions log | In discovery report | Yes |

### Handoff Summary Fields
```markdown
## Handoff Summary for Agent 02
- Programs to analyze (in priority order): [PROG1, PROG2, PROG3]
- Copybooks and includes available: [list or "None provided"]
- DB2/database layouts available: [list or "None — flag as gap"]
- Known missing artifacts: [list]
- Key risk areas: [e.g., AUTHPROG daily limit logic — table source unknown]
- Gate 1 status: [PASSED — signed by PO + Lead Engineer on DATE]
```

### Agent 02 Pre-Processing Check
- [ ] Programs list provided and prioritized
- [ ] Source code files attached or accessible
- [ ] Missing artifacts listed (do not proceed without listing)
- [ ] Gate 1 signed off

---

## Handoff 02→03: Legacy Analysis → Rule Extraction

**From:** Stage 02 (Legacy Analysis) / Agent 02  
**To:** Stage 03 (Rule Extraction) / Agent 03  
**Gate:** Part of Gate 1 (within Discovery stage)

### Required Artifacts

| Artifact | File | Mandatory? |
|---|---|---|
| Legacy analysis (per program) | `analysis/legacy-behavior-summary-[PROG].md` | Yes — one per program |
| Decision point inventory | In each analysis file | Yes |
| Open questions for SME | In each analysis file | Yes |
| Confidence summary | In each analysis file | Yes |
| Missing artifacts log | In each analysis file | Yes |

### Handoff Summary Fields
```markdown
## Handoff Summary for Agent 03
- Programs analyzed: [PROG1, PROG2]
- Total decision points identified: [N]
- High-confidence rules (ready to extract): [N]
- Low-confidence rules (need SME): [N]
- Unresolved missing artifacts: [list — these limit confidence]
- SME sessions required before full extraction: [Yes/No — topic list]
```

### Agent 03 Pre-Processing Check
- [ ] At least one legacy analysis file present
- [ ] Decision points listed with source references
- [ ] Confidence levels assigned per section
- [ ] Missing artifacts documented

---

## Handoff 03→04: Rule Extraction → Architecture

**From:** Stage 02 (Requirements) / Agent 03  
**To:** Stage 03 (Architecture) / Agent 04  
**Gate:** Gate 4 (Business Rules Approved) — SME sign-off required

### Required Artifacts

| Artifact | File | Mandatory? |
|---|---|---|
| Business rules register | `analysis/business-rules-register.md` | Yes |
| NFR catalog | `requirements/nfr-catalog.md` | Yes |
| SME-validated rules | In rules register | Yes — all High/Critical |
| Open questions resolved | In rules register | Yes — or explicitly accepted as assumptions |

### Handoff Summary Fields
```markdown
## Handoff Summary for Agent 04
- Total rules: [N] (High: [N], Medium: [N], Low: [N])
- Rules approved by SME: [N of N]
- Rules accepted as assumptions (not yet SME-validated): [N — list BR-NNN]
- NFR targets: latency=[Xms], availability=[X%], throughput=[X TPS]
- Key constraints for architecture: [list — e.g., "Must integrate with existing IBM MQ"]
- Gate 4 status: [PASSED — signed by SME + Lead Engineer on DATE]
```

### Agent 04 Pre-Processing Check
- [ ] Business rules register complete with BR-NNN numbering
- [ ] At least 80% of rules SME-validated (or explicitly accepted)
- [ ] NFR catalog present with numeric targets
- [ ] Gate 4 signed off — do not begin architecture without this

---

## Handoff 04→05: Architecture → Code Generation

**From:** Stage 03 (Architecture) / Agent 04  
**To:** Stage 05 (Development) / Agent 05  
**Gate:** Gate 3 (Architecture Complete)

### Required Artifacts

| Artifact | File | Mandatory? |
|---|---|---|
| Service map | `architecture/service-map.md` | Yes |
| API contracts (per service) | `architecture/api-contracts/` | Yes |
| Event contracts | `architecture/event-contracts/` | If async used |
| ADR set | `architecture/adrs/` | Yes — one per major decision |
| Data ownership map | In service map | Yes |
| Detailed component design (Stage 04) | `architecture/detailed-design/` | Yes |
| Data model design | `architecture/data-models/` | Yes |
| Sequence diagrams | `architecture/sequence-diagrams/` | Yes |

### Handoff Summary Fields (per service being implemented)
```markdown
## Handoff Summary for Agent 05 — [SERVICE-NAME]
- Business rules this service implements: [BR-001, BR-003, BR-007]
- API contract: [filename — version]
- Data model: [filename — entity list]
- External dependencies: [list — with adapters defined]
- Technology stack confirmed: [Java 21, Spring Boot 3.2, PostgreSQL 15, JUnit 5]
- Package base: [com.acme.authorization]
- Key ADRs affecting this service: [ADR-001 (hexagonal), ADR-003 (PostgreSQL)]
- Gate 3 status: [PASSED — signed by Architect + Security on DATE]
```

### Agent 05 Pre-Processing Check
- [ ] Service design present for this service
- [ ] API contract present (method, request, response, errors)
- [ ] Data model present (entity fields, constraints, indexes)
- [ ] Business rules list with BR-NNN
- [ ] Technology stack confirmed
- [ ] Gate 3 signed — do not generate code without architecture approval

---

## Handoff 05→06: Code Generation → Code Review

**From:** Stage 05 (Development) / Agent 05  
**To:** Stage 06 Review / Agent 06  
**Gate:** Gate 5 (Code Review Complete)

### Required Artifacts

| Artifact | File | Mandatory? |
|---|---|---|
| Generated/written code | `src/` | Yes |
| Unit tests | `src/test/` | Yes |
| Generation summary | In code output | Yes |
| Assumptions made | In code output | Yes |
| Business rules implemented | In code output | Yes — BR-NNN list |

### Handoff Summary Fields
```markdown
## Handoff Summary for Agent 06
- Service: [SERVICE-NAME]
- Files generated: [N]
- Test files generated: [N]
- Business rules implemented: [BR-001, BR-003, BR-007]
- Business rules with tests: [BR-001 ✓, BR-003 ✓, BR-007 ✓]
- Assumptions made (require human validation): [list]
- Architecture spec referenced: [filename]
- Known gaps: [list or "None"]
```

### Agent 06 Pre-Processing Check
- [ ] Code present and compilable (or stated otherwise)
- [ ] Test files present
- [ ] Business rules list present (to check against implementation)
- [ ] Architecture spec available (to check conformance)
- [ ] Assumptions list reviewed before beginning review

---

## Handoff 06→07: Review → Coordinator / Delivery

**From:** Agent 06 (Reviewer)  
**To:** Agent 07 (Coordinator)  
**Gate:** Gate 5 completion

### Required Artifacts

| Artifact | File | Mandatory? |
|---|---|---|
| Code review report | `reviews/[service]-review.md` | Yes |
| Open findings list | In review report | Yes |
| Architecture conformance table | In review report | Yes |
| Business rule coverage table | In review report | Yes |
| Security findings | In review report | Yes |
| Review decision | In review report | Yes |

### Handoff Summary Fields
```markdown
## Handoff Summary for Agent 07
- Review decision: [APPROVED / CHANGES REQUIRED]
- BLOCKERs: [N — list if > 0]
- MAJORs: [N]
- Business rules fully covered: [N of N]
- Business rules missing tests: [list BR-NNN or "None"]
- Security clearance: [CLEARED / HOLD — reason]
- Remaining actions before merge: [list or "None"]
```

---

## Handoff Contract: Confidence Inheritance Rule

When an agent or stage receives output from a prior stage that contains `[ASSUMPTION]` or `[LOW CONFIDENCE]` items that affect its work:

1. **Do not silently drop the uncertainty** — surface it in your own output
2. **Label inherited assumptions explicitly**: `[INHERITED ASSUMPTION from Agent 02: ...]`
3. **Escalate if the assumption is load-bearing**: if your work is built on an unvalidated assumption, flag it at the top of your output before proceeding
4. **Do not increase the confidence of inherited assumptions** without new evidence

A chain of assumptions is not confidence. An assumption validated by an SME becomes a [FACT].
