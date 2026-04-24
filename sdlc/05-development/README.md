# Stage 05 — Development

**Purpose:** Generate production-quality code and tests from approved architecture and design artifacts.

**Who uses this:** Developers, Lead Engineers

**Critical rule:** No code generation without completed Stage 03 (Architecture) and Stage 04 (Design) outputs. The AI must have approved artifacts to generate from — not a verbal description.

**Inputs needed:**
- Approved service design (from Stage 03/04)
- Business rules register
- API contracts
- Coding standards (from `.context/CORE_SKILLS.md` and `.context/ATOM_CHASSIS.md`)

**Outputs produced:**
- Implementation code with tests
- PR descriptions
- Assumption lists (anything not specified in design)

## Prompts in This Stage

| File | Purpose |
|---|---|
| `P1-code-generation.md` | Generate service implementation + tests |
| `P2-code-review.md` | AI-assisted code review (security, quality, architecture) |
| `P3-refactoring.md` | Refactor existing code to improve quality |
| `P4-debugging.md` | Root cause analysis and bug fixing |
