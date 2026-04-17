---
knowledge-pack: "Event-Driven Architecture"
load-when: "Designing or implementing event-driven systems, message queues, Kafka, async integrations"
tools: "Claude Code, GitHub Copilot, watsonx.ai"
---

# Knowledge Pack: Event-Driven Architecture

## When to Load This Pack

Load this pack into your AI session when:
- Designing services that communicate via events or messages
- Implementing Kafka, RabbitMQ, IBM MQ, AWS SQS/SNS, or Azure Service Bus
- Converting synchronous integrations to asynchronous
- Designing event sourcing or CQRS patterns
- Reviewing event contracts

## Core Concepts to Inject Into AI Sessions

```text
EVENT-DRIVEN ARCHITECTURE CONTEXT

Core principles:
- Events represent facts — something that happened in the past (immutable)
- Commands tell a service to do something (may be rejected)
- Events enable loose coupling — producers don't know consumers
- At-least-once delivery is the default — consumers must be idempotent

Event taxonomy:
- Domain Event: BusinessRuleViolated, CardStatusChanged, PaymentSettled
- Integration Event: cross-service event with versioned schema
- Command Message: CreateAuthorization, BlockCard (directed at one service)
- Query Message: RequestAccountBalance (request-reply pattern)

Kafka specifically:
- Topic = category of events (authorization.decisions, card.status.changes)
- Partition = unit of ordering — events on the same partition are ordered
- Consumer group = set of consumers sharing the processing load
- Offset = position in the partition — commit only after successful processing
- Log compaction = retain only the latest value per key (useful for state topics)

Ordering guarantees:
- Kafka guarantees order WITHIN a partition only
- To guarantee order for a business entity: partition by entity ID (cardId, accountId)
- Do NOT rely on cross-partition order

Idempotency requirement:
- Every consumer must handle duplicate events safely
- Technique 1: Idempotency key — store eventId; reject if already processed
- Technique 2: Upsert on unique constraint — database rejects duplicate insert
- Technique 3: Check-then-act — verify business state before processing
```

## Event Design Patterns

### Pattern 1: Event Notification (thin event)
```json
{
  "eventId": "uuid",
  "eventType": "CardStatusChanged",
  "timestamp": "ISO8601",
  "cardId": "CARD-123",
  "newStatus": "BLOCKED"
}
```
**Use when:** Consumers only need to know something happened and can fetch details  
**Trade-off:** Extra round-trip for consumers; load on source service

### Pattern 2: Event-Carried State Transfer (fat event)
```json
{
  "eventId": "uuid",
  "eventType": "AuthorizationDecided",
  "timestamp": "ISO8601",
  "authorizationId": "AUTH-456",
  "decision": "APPROVED",
  "cardId": "CARD-123",
  "amount": { "value": 100.00, "currency": "USD" },
  "merchantId": "MERCHANT-789",
  "fraudScore": 0.12
}
```
**Use when:** Consumers need the data to process without calling back  
**Trade-off:** Larger payload; more data coupling

### Pattern 3: Event Sourcing (full audit trail)
Store events as the source of truth; derive current state by replaying events.  
**Use when:** Full audit trail required; time-travel queries needed; complex domain state  
**Trade-off:** Complexity; snapshot management; eventual consistency

## Kafka Topic Design Standards

```
Topic naming: [domain].[entity].[event-type]
Examples:
  authorization.decisions          — outcomes of authorization requests
  card.status.changes              — card status change events
  limit.threshold.breaches         — daily limit exceeded notifications
  payment.settlements              — payment settlement events

Partition count:
  Rule of thumb: partitions = target throughput / (throughput per partition)
  Minimum for production: 3 (allows rebalancing during failure)
  Change partitions ONLY via Kafka Streams or new topic (order breaks on repartition)

Replication factor:
  Development: 1
  Staging: 2
  Production: 3 (always)

Retention:
  Transactional events: 7 days (or per compliance requirement)
  State/compacted topics: forever (log.cleanup.policy=compact)
  Audit events: 13 months (PCI-DSS)
```

## Consumer Implementation Standards

```java
// Idempotent consumer pattern
@KafkaListener(topics = "authorization.decisions", groupId = "audit-service")
public void handleAuthorizationDecided(AuthorizationDecidedEvent event) {
    // Check idempotency FIRST — before any state mutation
    if (processedEventRepository.existsById(event.getEventId())) {
        log.info("Duplicate event skipped: eventId={}", event.getEventId());
        return;  // Ack the message — it was already processed
    }
    
    try {
        // Process the event
        auditService.recordDecision(event);
        
        // Mark as processed — within the same transaction
        processedEventRepository.save(new ProcessedEvent(event.getEventId(), Instant.now()));
        
    } catch (Exception e) {
        log.error("Failed to process event: eventId={}, error={}", event.getEventId(), e.getMessage());
        throw e;  // Re-throw to trigger retry / DLQ
    }
}
```

## Dead Letter Queue (DLQ) Pattern

Every consumer must configure a DLQ for messages that fail after max retries:

```java
@Bean
public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> template) {
    DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
            (record, ex) -> new TopicPartition(record.topic() + ".DLQ", record.partition()));
    
    FixedBackOff backOff = new FixedBackOff(1000L, 3);  // 3 retries, 1s interval
    return new DefaultErrorHandler(recoverer, backOff);
}
```

DLQ monitoring requirements:
- Alert when DLQ depth > 0 for more than 5 minutes
- DLQ consumers must be built for every topic
- DLQ entries include original headers + failure reason + stack trace

## Schema Management

Use Schema Registry (Confluent / AWS Glue) for all production event schemas:

- **Avro** (preferred for Kafka): compact binary, strict schema evolution
- **JSON Schema**: human-readable, slightly larger, good for debugging
- **Protobuf**: highest performance, language-neutral

**Compatibility rules:**
- BACKWARD: new schema can read old messages (add fields with defaults)
- FORWARD: old schema can read new messages (add fields, don't remove)
- FULL: both (safest — start here)

**Breaking changes require:**
1. New topic version: `authorization.decisions.v2`
2. Consumer migration period (run both consumers)
3. Producer cutover
4. Old topic deprecation (after all consumers migrated)

## AI Session Instructions

When designing event-driven components, the AI assistant must:
1. Define partition key for every topic (determines ordering guarantee)
2. Specify idempotency strategy for every consumer
3. Define DLQ handling for every consumer
4. Use versioned event schemas — never `Object` or untyped JSON
5. Document what happens when the consumer is down (message backlog, catch-up strategy)
6. Confirm that the event carries enough data OR that the consumer can fetch the rest without race conditions
