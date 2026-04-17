# P3 — API Contract Design

**Stage:** 03 — Architecture  
**Persona:** Solution Architect, Lead Engineer  
**Output files:** `docs/architecture/api-contracts/[service]-api.yaml`, `docs/architecture/event-contracts/[event]-schema.json`

---

## REST API Contract Prompt

```text
You are a senior API architect designing REST API contracts for an enterprise service.

CONTEXT
Service: [SERVICE NAME]
Responsibility: [1-sentence description]
Consumers: [who calls this API — internal services, external partners, mobile apps]
Authentication: [OAuth2 scopes / mTLS / API key]
SLA: [latency target, availability]
Compliance: [PCI-DSS / HIPAA / GDPR — any that affect the API response shape]

BUSINESS OPERATIONS TO EXPOSE
[List operations this service must support — derived from user stories and business rules]
Example:
- Authorize a card transaction (real-time, must respond < 150ms)
- Get account balance (read)
- Update account status (write)
- List recent transactions with pagination (read)

TASK
Design complete REST API contracts. For EACH endpoint produce:

### [HTTP METHOD] [/resource/path]

**Purpose:** [1 sentence]
**Latency target:** [e.g., p99 < 200ms]
**Idempotent:** [Yes/No — if POST, explain idempotency-key mechanism]

**Authentication:** [scope required]
**Authorization:** [who can call this — role/attribute requirements]

**Request:**
```yaml
headers:
  Authorization: "Bearer {token}"
  Idempotency-Key: "{uuid}"  # required for state-changing operations
  X-Correlation-ID: "{uuid}"  # required on all requests
  
path_params:
  accountId:
    type: string
    format: uuid
    description: "Unique account identifier"

request_body:
  content:
    application/json:
      schema:
        type: object
        required: [field1, field2]
        additionalProperties: false
        properties:
          field1:
            type: string
            description: "[business meaning]"
            example: "[realistic example — no real PII]"
          amount:
            type: string
            description: "Transaction amount as string to avoid floating point — e.g., '10.50'"
            pattern: "^\\d+\\.\\d{2}$"
```

**Response — Success:**
```yaml
status: [201/200]
body:
  schema:
    [complete schema]
```

**Response — Error Cases:**
| Status | Error Code | When | Message Pattern |
|---|---|---|---|
| 400 | INVALID_REQUEST | Missing required field | "Field [name] is required" |
| 401 | UNAUTHORIZED | Missing/invalid token | "Authentication required" |
| 403 | FORBIDDEN | Insufficient scope | "Insufficient permissions for operation X" |
| 404 | NOT_FOUND | Resource doesn't exist | "[Resource] [id] not found" |
| 409 | DUPLICATE_REQUEST | Idempotency-Key already used | "Request [key] already processed" |
| 422 | BUSINESS_RULE_VIOLATION | Business rule failed | "[specific rule message]" |
| 429 | RATE_LIMITED | Too many requests | "Rate limit exceeded. Retry after [seconds]s" |
| 503 | SERVICE_UNAVAILABLE | Dependency unavailable | "Service temporarily unavailable" |

**Rate limits:** [requests per second/minute]
**Versioning:** [how this endpoint will be versioned when breaking changes needed]

---

After all endpoints, produce:

### Standard Error Schema (apply to all endpoints)
```json
{
  "error": "[ERROR_CODE — machine-readable]",
  "message": "[Human-readable message]",
  "correlationId": "[echo back X-Correlation-ID from request]",
  "timestamp": "[ISO 8601]",
  "details": []  // optional array for field-level validation errors
}
```

### API Changelog Strategy
[How will breaking vs non-breaking changes be managed]

Confidence: [High/Medium/Low] | Basis: [sources]
```

---

## Event Contract Prompt

```text
Design event contracts for the following domain events:

SERVICE/DOMAIN: [NAME]
EVENTS TO DEFINE: [list events]
MESSAGE BROKER: [Kafka / RabbitMQ / SQS / Azure Service Bus]
SCHEMA FORMAT: [Avro / JSON Schema / Protobuf]

For EACH event produce:

### Event: [EventName] (past tense — e.g., PaymentAuthorized)

**Producer:** [service name]
**Trigger:** [what business action causes this event]
**Consumers:** [known consumers and what they do with it]
**Kafka topic:** `[domain].[aggregate].[event]` (e.g., `payments.authorization.approved`)
**Partition key:** [field used for ordering — e.g., accountId]
**Retention:** [how long this event must be replayable — e.g., 7 days]
**Idempotent processing:** [how consumers should deduplicate — e.g., using eventId]

**Schema:**
```json
{
  "eventId": "uuid — unique per event instance, stable for deduplication",
  "eventType": "PaymentAuthorized",
  "eventVersion": "1.0",
  "timestamp": "ISO 8601 with timezone",
  "correlationId": "propagated from originating request",
  "aggregateId": "the business entity this event is about",
  "aggregateType": "Payment",
  "payload": {
    "[business fields]": "[with descriptions and example values]"
  },
  "metadata": {
    "producerService": "payment-service",
    "producerVersion": "2.1.0"
  }
}
```

**Schema Evolution Rules:**
- What fields are nullable vs required
- How consumers should handle unknown fields (ignore)
- Breaking change policy (new topic version)

**Replay Considerations:**
- Is this event safe to replay? [Yes/No — why]
- Replay ordering requirement: [strict/none]
```
