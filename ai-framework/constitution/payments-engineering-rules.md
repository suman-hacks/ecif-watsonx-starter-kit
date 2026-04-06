# Payments Engineering Rules

## Core rules
- Preserve approved business behavior before optimizing structure.
- Do not convert legacy code directly into framework-heavy code without first extracting rules and boundaries.
- Every critical payment flow must declare:
  - system of record
  - transaction boundary
  - consistency model
  - retry model
  - replay model
- Use explicit business reason codes and status transitions.
- Record assumptions separately from facts.

## Service design rules
- keep APIs consumer-stable even when backend dependencies change
- isolate legacy access behind adapters
- prefer domain-oriented contracts over screen/file-oriented payloads
- define idempotency for mutation endpoints
- include audit and supportability considerations in design
