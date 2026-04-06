# ECIF v2 Usage Guide for Payments Modernization

## Recommended workflow
### Step 1 — Load always-on context
Use the files under `ai-framework/constitution/` as the permanent operating baseline:
- constitution
- architecture principles
- security rules
- modernization intent

### Step 2 — Select the right pack
Choose only the packs relevant to the task:
- COBOL analysis -> `payments-cobol-modernization`
- ICS decomposition -> `ics-modernization`
- API façade / domain services -> `payments-api-abstraction`
- events, Kafka, replay, consistency -> `event-driven-modernization-payments`
- Spring Boot implementation -> `spring-boot-target-patterns`
- test strategy / observability -> `payments-testing-observability`

### Step 3 — Run the Watsonx prompt modes
1. Legacy understanding
2. Target mapping
3. Code generation
4. Review and hardening

### Step 4 — Persist outputs
Write results into:
- source-to-target map
- assumption register
- decision log
- session/project/domain memory

## Output rule
Never allow the assistant to jump directly from legacy source to final code for critical payment flows. Require:
1. behavior summary
2. business rules
3. ambiguity list
4. mapping decision
5. generated code
6. test evidence
