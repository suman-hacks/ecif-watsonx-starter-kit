# Payments API Patterns

## Contract rules
- use explicit resource nouns and command endpoints only when domain action is required
- separate inquiry APIs from update/command APIs when behavior differs materially
- include business correlation identifiers
- define error contracts with machine-readable reason codes
- version carefully; prefer backward-compatible additions

## Abstraction patterns
1. façade API over legacy transaction
2. orchestration API calling multiple backends
3. command API + async event/result callback
4. read API backed by modern read store
5. compatibility API preserving legacy response semantics for transition period

## Output checklist
- endpoint and method
- request/response model
- status/reason handling
- idempotency rules
- latency/SLA expectations
- backend ownership
- observability fields
