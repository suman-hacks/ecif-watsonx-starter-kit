# FORGE Project Context: Brownfield (Existing Distributed System)

Load this file alongside `constitution/01-core-principles.md` for projects modifying existing distributed/cloud systems.

---

## Project Type: Brownfield

**Definition:** Modifying, extending, or refactoring an existing distributed system (not mainframe — see `mainframe-modernization/` for that).

**Key Characteristics:**
- Existing behavior must be preserved unless change is explicit and approved
- Existing integrations are live and have consumers who depend on them
- Risk of regression is the primary concern
- Technical debt is real and must be managed, not just worked around

---

## AI Behavior for Brownfield Projects

**Understand before changing:**
- Read and understand existing code before suggesting modifications
- Map existing behavior explicitly before designing new behavior
- Identify all callers/consumers of anything being changed
- Check git history for context on why code is the way it is

**Minimize blast radius:**
- Prefer additive changes (new endpoint, new field) over modifications
- Use feature flags for all significant behavioral changes
- Never remove or rename public APIs/events without a deprecation period
- Keep the surface area of each change as small as possible

**Test before and after:**
- Capture existing behavior as tests BEFORE modifying code
- Confirm tests fail appropriately (characterization tests)
- Confirm tests pass after changes
- Run regression suite on every PR

**Strangler fig for large changes:**
- Do not rewrite large components in one pass
- Extract one capability at a time behind an anti-corruption layer
- Run old and new in parallel until new is proven

---

## Risk Management

Before making any change, answer:
1. What existing functionality could this break?
2. Who are the consumers of what I'm changing?
3. What is the rollback plan if this breaks production?
4. What tests verify existing behavior won't regress?

If any of these can't be answered, stop and find the answers before proceeding.

---

## Brownfield SDLC Emphasis

Invest heavily in:
- Stage 01 (Discovery) — understand what exists before proposing changes
- Stage 04 (Design) — detailed impact analysis before any code changes
- Stage 06 (Testing) — regression testing is critical

Special attention to:
- Backwards compatibility for all API and event contract changes
- Database migration safety (forward-only, backward-compatible until cutover)
- Feature flag strategy for each significant change
- Monitoring baseline before and after deployment

---

## Common Brownfield Anti-Patterns (AI Should Flag)

- Changing existing API response structure without versioning
- Removing a field that might be consumed downstream (even if not obvious)
- Assuming code you don't recognize is dead — check usage first
- "While we're in here" refactoring — separate refactor from feature work
- Fixing one thing and accidentally changing adjacent behavior
