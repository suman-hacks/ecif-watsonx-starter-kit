# Payments Target Reference Architecture

## Goals
- Preserve legacy business behavior while decoupling from monolithic mainframe orchestration.
- Introduce API abstraction and event-driven side effects.
- Keep system-of-record writes aligned with approved integration patterns.
- Ensure replay, recovery, and auditability for failed asynchronous steps.

## Core target pattern
- Inbound API / batch / event adapter
- Application service with domain orchestration
- Legacy gateway adapter for mainframe interactions
- Read model adapter for PostgreSQL or operational cache
- Outbox publisher for downstream events
- Audit log and replay support for failed calls
