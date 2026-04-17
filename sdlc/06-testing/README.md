# Stage 06 — Testing

**Purpose:** Verify the system meets all functional and non-functional requirements.

**Who uses this:** QA Engineers, Developers (unit/integration), UAT team (business scenarios)

**Inputs needed:**
- User stories with acceptance criteria (Stage 02)
- Implemented code (Stage 05)
- NFR register (for performance and security acceptance criteria)
- Business rules register (verify each rule is tested)

**Outputs produced:**
- Test strategy document
- Automated test suites (unit, integration, API, performance)
- UAT test scripts (business-language)
- Traceability: business rule → test case mapping

## Prompts in This Stage

| File | Purpose |
|---|---|
| `P1-test-strategy.md` | Create a comprehensive test strategy |
| `P2-unit-test-generation.md` | Generate unit tests for a component |
| `P3-integration-test-generation.md` | Generate integration/API tests |
| `P4-uat-test-scenario-generation.md` | Generate business-language UAT scripts |
