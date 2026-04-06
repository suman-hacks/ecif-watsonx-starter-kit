# Payments Modernization Reference Architecture

## Architectural intent
Use an incremental strangler approach:
- preserve system-of-record authority where required
- expose stable APIs through an abstraction layer
- shift read-heavy and domain-specific processing to modern services
- publish domain events for consistency, analytics, and recovery
- isolate legacy dependencies behind anti-corruption adapters

## Typical target layers
1. **API Layer**
   - REST/gRPC depending on enterprise standard
   - authentication, authorization, throttling, versioning
2. **Domain Services**
   - business orchestration
   - validation
   - policy enforcement
3. **Adapters**
   - mainframe connectors
   - database repositories
   - event publishers/consumers
4. **Data Layer**
   - operational stores
   - read models
   - audit/replay stores
5. **Observability + Governance**
   - structured logs
   - traces
   - metrics
   - audit evidence

## Decision principles
- prefer asynchronous propagation where end-user SLA permits
- keep transactional boundaries explicit
- never split a critical authorization decision across unreliable dependencies without fallback strategy
- every modernization decision must declare: consistency model, failure model, and replay model
