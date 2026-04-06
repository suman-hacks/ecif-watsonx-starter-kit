# Code Generation Rules

## Required patterns
- hexagonal or clean architecture bias
- package-private where possible
- bean validation on inbound DTOs
- service methods with explicit command/query separation
- retries and circuit breakers only in integration layer
- metrics and tracing for critical flows
- tests at unit + slice/integration levels as appropriate

## Avoid
- god services
- leaking entity objects through API contracts
- hidden static utilities for business logic
- repository calls from controllers
- framework annotations inside pure domain objects unless justified
