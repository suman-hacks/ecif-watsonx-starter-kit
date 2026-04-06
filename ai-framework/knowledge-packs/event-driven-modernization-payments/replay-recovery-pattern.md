# Replay and Recovery Pattern

## Recommended structure
- inbound event/request captured in audit log
- processing status tracked explicitly
- payload and headers stored safely
- retries separated from original ingress
- operator or support UI can reprocess selected failures
- every replay creates linked audit evidence

## Required metadata
- event id
- correlation id
- source system
- business entity key
- processing state
- failure reason
- retry count
- timestamps
- actor/channel context
