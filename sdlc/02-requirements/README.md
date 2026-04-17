# Stage 02 — Requirements

**Purpose:** Translate discovered needs and legacy behavior into structured, testable requirements.

**Who uses this:** Product Owner, Business Analyst, Product Manager (with engineer input for NFRs)

**Inputs needed:**
- Stage 01 (Discovery) outputs: current-state assessment, system inventory, pain points
- Stakeholder interview notes
- Business objectives and OKRs

**Outputs produced:**
- User stories with acceptance criteria (Given/When/Then)
- Non-functional requirements register
- Requirements traceability matrix
- Open questions and assumptions log

**Gates before proceeding to Stage 03:**
- [ ] PO has reviewed and prioritized all user stories
- [ ] All acceptance criteria are testable (not aspirational)
- [ ] NFRs have measurable thresholds (not "fast" — "`< 200ms p99`")
- [ ] Ambiguities flagged in Stage 01 have been resolved or captured as assumptions

## Prompts in This Stage

| File | Purpose |
|---|---|
| `P1-user-story-generation.md` | Generate user stories from discovery findings |
| `P2-acceptance-criteria.md` | Write complete BDD acceptance criteria |
| `P3-nfr-definition.md` | Define non-functional requirements register |
| `P4-requirements-traceability.md` | Build requirements traceability matrix |
