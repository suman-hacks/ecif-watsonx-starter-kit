# Event-Driven Patterns for Payments

## Core patterns
1. **Audit-first intake**
   - persist inbound request or event to audit table/log before transformation
2. **Reliable publish**
   - outbox or equivalent consistency mechanism
3. **Replay and recovery**
   - failed or incomplete events must be replayable independently of original producer
4. **Read-model propagation**
   - publish events to build modern read stores
5. **Reconciliation**
   - scheduled comparison between source-of-record and target projections

## Design questions
- what is the event of record
- who owns replay
- where is idempotency enforced
- how is ordering handled
- what happens when downstream APIs are unavailable
- what evidence exists for audit and ops teams
