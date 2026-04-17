# FORGE Architecture Standards

These standards define mandatory architectural patterns for all systems designed and built with AI assistance under the FORGE framework. Deviations require an Architecture Decision Record (ADR) with documented rationale and human sign-off.

---

## Section 1: Service Design Principles

### 1.1 Single Responsibility

Each service has exactly one clear business capability. A business capability is a noun phrase that describes what the service is responsible for managing, not what it does technically.

**Correct service decomposition:**
- `PaymentProcessingService` — owns the lifecycle of payment transactions
- `CustomerProfileService` — owns customer identity and profile data
- `FraudRiskService` — owns fraud assessment and risk scoring
- `NotificationService` — owns delivery of notifications to customers

**Incorrect decomposition:**
- `DataService` — too broad; owns nothing specifically
- `PaymentAndCustomerService` — owns two capabilities; violates single responsibility
- `UtilityService` — catch-all; not a business capability
- `CommonService` — not a business capability

**Test for single responsibility:** If you cannot describe the service's purpose in one sentence using business domain language without the word "and," the service is likely violating single responsibility.

### 1.2 Explicit API Contracts

All service interfaces must be defined and reviewed before implementation begins. API contracts are the binding agreement between producer and consumer.

**Required contract elements for REST APIs:**
- OpenAPI 3.x specification (YAML or JSON)
- All endpoints with HTTP method, path, and summary
- All request body schemas with field names, types, constraints, and descriptions
- All response schemas for each HTTP status code
- Authentication and authorization requirements
- Rate limiting and quota documentation
- Versioning strategy (URL path versioning: `/v1/payments`)

**Required contract elements for events/messages:**
- AsyncAPI 2.x or higher specification
- Topic/queue names and access patterns
- Event schema (with Avro, JSON Schema, or Protobuf)
- Event ordering guarantees (ordered per key / unordered)
- At-least-once vs. exactly-once delivery semantics
- Retention policy
- Consumer group conventions

**Contract-first rule:** The API contract document is the source of truth. Implementation must match the contract. The contract is never generated from the implementation.

### 1.3 Data Sovereignty

Each service owns its data exclusively. No service may directly access another service's persistence layer.

**Data sovereignty rules:**
1. Each service has its own database schema or database instance
2. No service may execute queries against another service's tables
3. No service may share a database schema with another service
4. Data from another service is accessed via that service's published API only
5. When a service needs a read-optimized projection of another service's data, it maintains its own local read model, populated via events

**Detecting violations:**
- A foreign key in Service A's schema pointing to a table owned by Service B — VIOLATION
- A query that JOINs tables from two different service schemas — VIOLATION
- A service that writes directly to another service's table (even "just for this one case") — VIOLATION

**Handling cross-service data needs:**
```
Need: Order service needs customer name for order confirmation email

Wrong approach: SELECT c.name FROM customer_service.customers c WHERE c.id = :id
Right approach: 
  1. Order service calls CustomerProfileService.getCustomer(customerId) via REST
  2. OR Order service subscribes to CustomerProfileUpdated events and maintains 
     a local read model of customer names needed for order processing
```

### 1.4 Stateless by Default

Services must not hold client or session state in process memory between requests. State belongs in purpose-built persistence layers.

**Stateless rules:**
- No in-memory session state (no `HttpSession` with business data)
- No in-process caches that hold authoritative state (read-through caches for performance are acceptable)
- No instance variables that accumulate state across requests
- Horizontal scaling must work correctly — any instance can handle any request

**Where state lives:**
| State Type | Correct Location |
|------------|-----------------|
| Session / user context | Redis, distributed cache |
| Business entity state | Primary database |
| Workflow/saga state | Workflow engine or state machine DB |
| Configuration | Configuration service / environment |
| Short-lived computation cache | Redis with TTL |

---

## Section 2: Communication Patterns

### 2.1 When to Use Synchronous Communication

Use synchronous (REST/gRPC) communication for:
- Query operations where the caller needs a response to continue (reads)
- User-facing operations where the user is waiting for a result
- Validation operations that must succeed before a write proceeds
- Operations where the caller needs the result to make a decision in the same flow

**Synchronous call requirements (every call, no exceptions):**

| Requirement | Minimum Standard |
|------------|-----------------|
| Timeout | Configured at call site; never use default/infinite timeout |
| Retry | Exponential backoff with jitter; maximum 3 retries for idempotent operations; 0 retries for non-idempotent |
| Circuit breaker | Resilience4j or equivalent; trip at 50% failure rate over 10-second window |
| Fallback | Defined behavior when circuit is open (cached response, degraded mode, graceful error) |

**Generated synchronous call pattern:**
```java
@CircuitBreaker(name = "fraud-service", fallbackMethod = "fraudCheckFallback")
@Retry(name = "fraud-service")
@TimeLimiter(name = "fraud-service")
public FraudAssessment checkFraud(PaymentRequest request, String correlationId) {
    return fraudServiceClient.assess(request);
}

public FraudAssessment fraudCheckFallback(PaymentRequest request, String correlationId, Exception ex) {
    log.warn("Fraud service unavailable, using fallback behavior",
        "correlationId", correlationId,
        "fallback", "ALLOW_WITH_ENHANCED_MONITORING");
    // Documented fallback: allow with enhanced transaction monitoring
    return FraudAssessment.allowWithEnhancedMonitoring(request.getTransactionId());
}
```

### 2.2 When to Use Asynchronous Communication

Use asynchronous (events/queues) communication for:
- Commands that do not require an immediate response from the consumer
- Cross-domain communication (crossing a bounded context boundary)
- Audit trail events (all business events should be published, regardless of consumer need)
- Fan-out patterns (one event, multiple independent consumers)
- Long-running operations (status updated via event when complete)
- Write operations crossing bounded context boundaries

**Rule:** Never call synchronously across bounded contexts for write operations. If Service A wants Service B to do something (a command), it must publish an event or message. Service B consumes and processes asynchronously.

**Asynchronous communication requirements:**
- Idempotency: Every event consumer must be idempotent (processing the same event twice must produce the same result as processing it once)
- Ordering: Document whether ordering guarantees are required; use message keys for ordered processing per entity
- Dead letter queue: Every consumer must define a DLQ and a process for handling DLQ messages
- Retry policy: Maximum retries before routing to DLQ; exponential backoff between retries
- Schema evolution: Events must support backward-compatible schema changes; consumers must tolerate unknown fields

### 2.3 The Anti-Pattern: Distributed Monolith

A distributed monolith is a system where multiple services are deployed independently but are tightly coupled through synchronous calls, shared databases, or shared deployment schedules. It combines the worst of both architectures.

**Warning signs of a distributed monolith (flag immediately):**
- Service A cannot start without Service B being available
- A single business operation requires synchronous calls across 5+ services
- Services share a database schema
- Deployment of Service A must be coordinated with deployment of Service B
- A team cannot deploy their service without coordinating with 3 other teams

---

## Section 3: Resilience Patterns

### 3.1 Required Resilience for Every Synchronous Integration

No synchronous call to an external service is complete without all four resilience mechanisms:

```
Every External Call = Timeout + Retry + Circuit Breaker + Fallback
```

**Timeout guidelines:**

| Integration Type | Recommended Timeout |
|-----------------|---------------------|
| Inter-service REST (intra-datacenter) | 2 seconds |
| Inter-service REST (cross-region) | 5 seconds |
| Third-party payment gateway | 30 seconds |
| Document generation service | 60 seconds |
| Batch data export | Use async pattern, not synchronous |

**Retry guidelines:**
- Idempotent GET operations: up to 3 retries with exponential backoff
- Idempotent POST/PUT with idempotency key: up to 3 retries
- Non-idempotent POST/DELETE: no automatic retry; require human/business decision
- Exponential backoff formula: `min(base * 2^attempt + jitter, max_wait)` where base=100ms, max_wait=10s

**Circuit breaker states:**
- CLOSED (normal): All calls pass through; monitor for failures
- OPEN (tripped): All calls fail fast to fallback; do not call the dependency
- HALF-OPEN (testing): Allow limited calls through to test if dependency has recovered

**Fallback behavior must be explicit and documented:**
```
Service: FraudRiskService
Fallback behavior when circuit open: 
  - Low-value transactions (<$100): Allow with enhanced audit logging
  - High-value transactions (>=$100): Queue for manual review; return PENDING status to caller
  - Rationale: Business decision by Risk team on [date]; review quarterly
```

### 3.2 Failure Mode Documentation

Every service integration must have a documented failure mode table:

| Scenario | Expected Behavior | Recovery | SLA Impact |
|----------|------------------|----------|------------|
| FraudService timeout | Use fallback: allow low-value, queue high-value | Automatic via circuit breaker | Minimal |
| Database connection pool exhausted | Reject new requests with 503; existing in-flight complete | Auto-recovery when pool frees | Degraded |
| Message broker unreachable | Buffer locally for 5 minutes; retry; alert after 2 minutes | Manual intervention if >5 min | Delayed processing |

### 3.3 Chaos Engineering Readiness

For each resilience mechanism implemented, there must be at least one test that verifies the mechanism works:
- Timeout test: Dependency returns response after timeout threshold — verify timeout fires
- Retry test: Dependency fails N-1 times then succeeds — verify retry succeeds
- Circuit breaker test: Dependency fails above threshold — verify circuit opens and fallback activates
- Fallback test: Circuit open — verify fallback behavior is correct

---

## Section 4: Observability Standards

### 4.1 Structured Logging

**Required format:** All service logs must be emitted as structured JSON. No free-form text log formats.

**Required fields in every log record:**

| Field | Type | Description |
|-------|------|-------------|
| `timestamp` | ISO-8601 UTC | When the event occurred |
| `level` | string | TRACE/DEBUG/INFO/WARN/ERROR |
| `service` | string | Service name (matches deployment label) |
| `version` | string | Service version |
| `correlationId` | string | Request correlation ID (UUID) |
| `traceId` | string | OpenTelemetry trace ID |
| `spanId` | string | OpenTelemetry span ID |
| `message` | string | Human-readable description |

**Additional fields for business events:**

| Field | When to Include |
|-------|----------------|
| `transactionId` | Any transaction-related operation |
| `accountId` | Any account-related operation |
| `customerId` | Any customer-related operation |
| `operation` | The business operation being performed |
| `durationMs` | For timed operations (start and completion) |
| `errorCode` | For error conditions |
| `errorClass` | Java class name of exception |

**Log level guidance:**
- `ERROR`: System error requiring human attention; operation failed and cannot self-recover
- `WARN`: Degraded behavior, fallback activated, retry occurring; worth monitoring
- `INFO`: Normal business events (payment processed, customer created, etc.); expected operational traffic
- `DEBUG`: Detailed technical execution flow; enabled on demand; never in production by default
- `TRACE`: Line-by-line execution; development only; never in any shared environment

### 4.2 Correlation ID Propagation

Every request entering a service must carry a correlation ID. If no correlation ID is present, the service generates one.

**Inbound propagation:**
```java
// HTTP header: X-Correlation-ID
// Generated if absent: UUID.randomUUID()
// Added to MDC for all log statements in the request scope
// Propagated to all outbound calls
```

**Outbound propagation:**
- REST: Set `X-Correlation-ID` header on every outbound HTTP call
- Messaging: Include `correlationId` in message headers
- Database: Include in application-level audit tables, not in the query itself

### 4.3 RED Metrics

Every service entry point (each HTTP endpoint, each message consumer) must emit three metrics:

| Metric | Description | Unit |
|--------|-------------|------|
| **R**ate | Number of requests processed | requests/second |
| **E**rrors | Number of requests resulting in errors | errors/second or error rate % |
| **D**uration | How long requests take to process | milliseconds (p50, p95, p99) |

**Metric naming convention:**
```
{service_name}_{operation}_{metric_type}
payment_processing_process_payment_requests_total
payment_processing_process_payment_errors_total
payment_processing_process_payment_duration_seconds
```

**Required labels/tags:**
- `service`: service name
- `operation`: specific operation/endpoint
- `status`: success/error/fallback
- `environment`: production/staging/development

### 4.4 Distributed Tracing

All services must integrate with an OpenTelemetry-compatible distributed tracing system (Jaeger, Zipkin, AWS X-Ray, Datadog APM, etc.).

**Tracing requirements:**
- Every inbound request creates or continues a trace
- Every outbound call creates a child span
- Every significant internal operation (database query, cache access, external API call) creates a span
- Business keys are added as span attributes (transaction ID, account ID, etc.)
- Error conditions add exception details to the span

---

## Section 5: Legacy Integration Standards

### 5.1 Anti-Corruption Layer (ACL)

Every integration between modern services and legacy systems must include an Anti-Corruption Layer. The ACL translates between the legacy system's domain model and language and the modern domain model and language.

**ACL responsibilities:**
- Protocol translation (MQ messages → REST calls, file-based → event-driven)
- Data model translation (COBOL copybook record → Java DTO → domain object)
- Error code translation (legacy 4-character error codes → typed exceptions or error responses)
- Semantic translation (legacy status codes → modern domain status values)

**ACL is NOT:**
- A place to add business logic
- A permanent layer (plan for removal as modernization proceeds)
- A workaround for poor API design in the modern services

**ACL structure:**
```
LegacySystem ←→ [ACL: Translator + Adapter + Protocol Bridge] ←→ ModernService
                     ↑ owned by the modernization team
                     ↑ tested with contract tests
                     ↑ has its own observability
```

### 5.2 Legacy Systems Are Black Boxes

When integrating with legacy systems, treat them as black boxes with observable external behavior only. Do not assume internal behavior from external observation.

**Rules:**
- Document observed behavior, not assumed internal logic
- Test legacy behavior before assuming it for architecture design
- When legacy behavior changes unexpectedly, treat as a production incident regardless of whether it "should" have changed
- Maintain legacy system documentation separate from assumptions about how it works

### 5.3 Strangler Fig Pattern

For gradual replacement of legacy systems, use the Strangler Fig pattern: route traffic progressively from legacy to modern, component by component, capability by capability.

**Strangler Fig phases:**

1. **Identify** — Select the capability to migrate; ensure it has clear boundaries
2. **Build** — Build the modern capability behind the strangler proxy; legacy still handles all traffic
3. **Verify** — Run dual-mode: modern handles traffic, results compared to legacy; differences are incidents
4. **Migrate** — Route percentage of traffic to modern; monitor for divergence
5. **Retire** — Route 100% to modern; keep legacy in standby; confirm retirement when stable

**Required dual-run period:**
- Duration: minimum 2 weeks for low-risk capabilities; minimum 3 months for critical financial capabilities
- Comparison: every transaction compared between legacy and modern; all differences logged
- Divergence threshold: zero tolerance for financial calculation differences; <0.1% threshold for non-financial operations

### 5.4 Integration Testing with Legacy Systems

Legacy system integrations must be tested at the boundary, not assumed. Required tests:

- **Positive path**: Modern system sends valid request → Legacy returns expected response → Modern correctly processes response
- **Error path**: Legacy returns each known error code → Modern handles each one correctly
- **Timeout/unavailable**: Legacy does not respond → Modern's resilience mechanisms activate correctly
- **Data edge cases**: Maximum field lengths, special characters, zero values, negative values

---

*FORGE Constitution — Architecture Standards v1.0*
