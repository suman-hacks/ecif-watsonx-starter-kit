# Test Strategy

## Test pyramid for modernization
1. Unit tests for extracted business rules
2. Slice tests for controllers/adapters
3. Contract tests for APIs and events
4. Integration tests for key dependencies
5. Replay/recovery tests for asynchronous flows

## Mandatory scenarios
- happy path
- reject path
- duplicate/idempotent request
- downstream timeout/failure
- invalid state transition
- audit evidence creation
