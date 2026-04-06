# Payments Modernization Pilot Example

## Scenario
Modernize one legacy card-servicing capability into:
- a stable API
- a Spring Boot service
- an event publication path
- a replayable audit trail

## Suggested flow
1. Analyze COBOL or legacy source with `payments-mode-1-cobol-analysis.md`
2. Map to domains with `payments-mode-2-domain-mapping.md`
3. Generate code with `payments-mode-3-spring-generation.md`
4. Harden with `payments-mode-4-review-hardening.md`

## Artifacts to produce
- business-rules.md
- source-to-target-map.md
- assumptions.md
- api-contract.md
- event-catalog.md
- generated-code/
- test-evidence.md
