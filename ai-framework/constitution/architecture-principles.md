# Architecture Principles

- Prefer domain-aligned service boundaries.
- Separate interfaces, application logic, and infrastructure concerns.
- Favor event-driven integration where loose coupling and recovery matter.
- Keep synchronous calls for truly real-time or transactional requirements only.
- Make target services observable by default.
- Externalize configuration.
- Treat the system of record and derived read models distinctly.
- Design for replay, auditability, and resilience.
- Avoid framework-heavy coupling when simpler abstractions are sufficient.
