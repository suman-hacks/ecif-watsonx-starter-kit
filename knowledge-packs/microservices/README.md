# Microservices Knowledge Pack

## Purpose

This pack teaches an AI coding assistant how to design, evaluate, and build microservices correctly — with correct bounded context boundaries, appropriate data patterns, proven resilience patterns, and clear communication strategy choices. Load this pack when designing a new service, reviewing an existing service's design, or modernizing a monolith into a set of services.

## When to Load

- Architecture (Stage 03): Designing target microservice decomposition
- Design (Stage 04): Producing service design documents, API contracts, event contracts
- Development (Stage 05): Generating code for a service — especially cross-cutting concerns
- Code Review: Evaluating whether a proposed design respects service boundaries and patterns
- Any task where the AI must choose between microservice architectural options

## Inputs This Pack Requires

- The business domain being decomposed (what the system does)
- Any existing system analysis (pre-engagement documents if available)
- Target platform and technology stack (Java/Spring Boot? Go? Python? Kubernetes or serverless?)
- Non-functional requirements: latency SLAs, throughput, availability targets

---

## Core Knowledge

### Service Design Principles

#### Principle 1: Single Business Capability
Each microservice is responsible for one and only one business capability. A business capability is something the business does, not a technical function.

**Good capability alignment:**
- `AuthorizationService` — decides whether a transaction should be approved or declined
- `AccountService` — manages account state, limits, and lifecycle
- `FraudService` — evaluates fraud risk for transactions
- `NotificationService` — sends customer notifications

**Bad: Technical layering instead of business capability:**
- `DatabaseService` — wraps database access (no business value, creates coupling)
- `ValidationService` — validates inputs for multiple domains (a utility, not a capability)
- `DataService` — too broad, no single responsibility

#### Principle 2: Owns Its Data
A service owns its data store. No other service may read or write the service's database directly. The only access to a service's data is through the service's public API.

```
AuthorizationService <-- REST --> AccountService
        |                              |
   auth_db (Postgres)          account_db (Postgres)
```

This is the most commonly violated principle. If two services share a database, they are effectively one service. Common violations:
- "We'll share the accounts table — both services need it"
- "The reporting service reads directly from the order database"
- "We use a single schema but with different table prefixes"

None of these are acceptable. If a service needs data owned by another service, it either calls the owning service's API or maintains its own read model (see CQRS below).

#### Principle 3: Independently Deployable
A service can be built, tested, and deployed without coordinating with other service teams. Violations:
- Deploying ServiceA always requires deploying ServiceB (tight coupling)
- An API change in ServiceA requires changes in ServiceB before either can deploy (synchronous coupling)
- A shared library version must be updated across all services simultaneously

**Solutions:**
- Consumers tolerate additive changes (new optional fields)
- Breaking API changes require versioning (`/v2/`) not coordinated deployment
- Shared libraries should be stable contracts — event schemas, not implementation utilities

#### Principle 4: Resilient by Design
Services fail. The question is how failure propagates. A well-designed service:
- Has explicit timeouts on all external calls
- Fails fast when dependencies are unavailable (circuit breaker)
- Degrades gracefully (fallback behavior) instead of cascading failure
- Does not hold resources (threads, connections) during a dependency failure

#### Principle 5: Aligned to a Team
Conway's Law: your architecture reflects your organizational structure. Each microservice should be owned by one team. If a service requires coordination between multiple teams to change, it is either the wrong service boundary or the org structure needs adjustment. Design services around team boundaries where possible.

---

### Bounded Context Design

A bounded context is a boundary within which a particular domain model applies. The same word may mean different things in different bounded contexts. Correct bounded context identification is the foundation of microservice decomposition.

#### How to Identify Bounded Contexts

**Step 1: Find the natural language boundaries.**
Different teams use different vocabulary for the same thing. "Account" means something different to the card issuance team (card account with limits) than to the customer service team (customer record with contact details). These different meanings in different groups are natural bounded context indicators.

**Step 2: Look for data ownership.**
Ask: "Who is allowed to change this data?" If only one team/process can authoritatively update a piece of data, that's a bounded context boundary.

**Step 3: Look for different SLAs.**
The authorization service must respond in <200ms. The statement generation service runs overnight in batch. Different latency/availability requirements indicate different bounded contexts.

**Step 4: Look for different rates of change.**
Fraud rules change frequently. Core account structure changes rarely. High change frequency difference often indicates a separate bounded context.

**Step 5: Event storming.**
Run an event storming session:
1. Identify domain events (past tense: `TransactionAuthorized`, `AccountBlocked`)
2. Find the commands that trigger events (`AuthorizeTransaction`, `BlockAccount`)
3. Identify aggregates that own the commands and events
4. Group aggregates with high cohesion → bounded contexts

#### Context Mapping Patterns

When two bounded contexts need to interact, define the relationship:

| Pattern | Description | When to Use |
|---|---|---|
| **Partnership** | Both contexts evolve together; teams coordinate | Two closely coupled contexts owned by teams that work together daily |
| **Shared Kernel** | Subset of domain model shared between contexts | Use sparingly — creates coupling; suitable for ubiquitous language concepts |
| **Customer-Supplier** | Upstream (supplier) context defines what downstream (customer) context gets | When downstream is dependent on upstream |
| **Conformist** | Downstream conforms to upstream's model without translation | When upstream is a legacy system or external system with fixed API |
| **Anti-Corruption Layer (ACL)** | Translation layer that insulates downstream from upstream's model | **Preferred when integrating with legacy systems** — prevents legacy model from polluting new design |
| **Open-Host Service** | Upstream defines a public API that downstream can use | Upstream publishes a stable interface for multiple consumers |
| **Published Language** | Shared language (schema, specification) all parties understand | Event schemas, AsyncAPI/OpenAPI specifications |
| **Separate Ways** | Contexts have no integration; solve problems independently | When integration cost exceeds the benefit |

#### When to Use Anti-Corruption Layer (ACL)

Use an ACL when:
- Integrating with a COBOL/mainframe system with a legacy data model
- Integrating with a third-party system or SaaS product with its own terminology
- The legacy model has concepts that do not cleanly map to your domain model
- You do not control the upstream system and it cannot change

What an ACL does:
```
Legacy ISO 8583 Message → [ACL] → AuthorizationRequest (domain model)
AuthorizationResponse (domain model) → [ACL] → ISO 8583 Response Message
```

The ACL lives at the integration boundary. It translates, enriches, and validates. It does not contain business logic — only translation logic. Business rules live in the domain service.

---

### Data Patterns

#### Database Per Service (Mandatory)

This is not optional. Each service has its own dedicated data store. The type of data store should be chosen for the service's access patterns:

| Service Characteristic | Suitable Data Store |
|---|---|
| Complex relational queries, transactions | PostgreSQL, MySQL |
| Simple key-value lookup, high throughput | Redis, DynamoDB |
| Document storage with flexible schema | MongoDB, Cosmos DB |
| Full-text search | Elasticsearch, OpenSearch |
| Time-series data | TimescaleDB, InfluxDB |
| Graph relationships | Neo4j |
| Event sourcing | EventStoreDB, Kafka (compacted topic) |

**Enforcement:** in code reviews, reject any code that connects to another service's database. In infrastructure, use network policies to prevent cross-service DB access.

#### Saga Pattern for Distributed Transactions

When a business operation spans multiple services, you cannot use a single database transaction. Use the Saga pattern.

**Choreography-based Saga:**
```
PaymentService             AccountService          FraudService
     |                          |                       |
     |--PaymentInitiated------->|                       |
     |                     [reserve funds]              |
     |                     FundsReserved--------------->|
     |                          |                  [check fraud]
     |                          |                  FraudApproved
     |<--FundsReserved + FraudApproved
     |--PaymentCompleted------->|
     |                     [confirm debit]
```
Each service listens for events and reacts. No central coordinator.

**Orchestration-based Saga:**
```
PaymentOrchestrator
     |
     |--[Step 1] POST /accounts/{id}/reserve
     |<-- 200 FundsReserved
     |--[Step 2] POST /fraud/check
     |<-- 200 FraudApproved
     |--[Step 3] POST /accounts/{id}/confirm
     |<-- 200 DebitConfirmed
     |--[emit] PaymentCompleted
```
A central orchestrator directs each step. Easier to understand and debug. Couples orchestrator to each participant.

**Compensating Transactions:**
Every saga step must have a compensating transaction that undoes it if the saga fails:
```
Reserve funds → compensate: Release funds reservation
Check fraud → compensate: (no state change, no compensation needed)
Send notification → compensate: Send cancellation notification
```
Compensating transactions are not rollbacks — they are new forward-moving actions that undo the effect. They must be designed upfront.

**When to use Choreography vs Orchestration:**
- Choreography: when steps are loosely coupled, teams are autonomous, and the flow is simple
- Orchestration: when the flow is complex with branching logic, when you need a clear audit trail, when a central team owns the business process

#### Event Sourcing

Event sourcing stores the history of all changes as events, rather than storing current state. Current state is derived by replaying events.

```
AccountEvents (append-only):
1. AccountOpened      {accountId: A123, creditLimit: 5000}
2. CreditLimitChanged {accountId: A123, newLimit: 7500}
3. TransactionPosted  {accountId: A123, amount: -150.00, txnId: T001}
4. PaymentReceived    {accountId: A123, amount: 200.00, refId: P001}

Current balance = derived by replaying all events for A123
```

**When to use event sourcing:**
- Complete audit trail is required (regulatory, financial)
- Temporal queries needed ("what was the state at time T?")
- Event replay needed for debugging or recovery
- Multiple read models needed from same data

**When NOT to use event sourcing:**
- Simple CRUD with no audit requirements
- Team not experienced with event sourcing (high learning curve)
- No tooling available for event store management

**Snapshots:** to avoid replaying millions of events, periodically snapshot the current state. On replay: load latest snapshot, then replay events after the snapshot timestamp.

#### CQRS — Command Query Responsibility Segregation

CQRS separates the write model (commands, strong consistency) from the read model (queries, eventual consistency, optimized for reads).

```
Command side:                          Query side:
POST /accounts/{id}/transactions  →   GET /accounts/{id}/balance
      |                                     |
   AccountService (write)             BalanceReadModel (denormalized)
      |                                     |
  account_write_db                   account_read_db (or cache)
      |                                     ^
      |--[event: TransactionPosted]---------| (async update)
```

**When to use CQRS:**
- High read/write ratio — reads and writes scale independently
- Read model requires a different schema than the write model
- Complex queries that don't fit the normalized write schema
- Combined with event sourcing (natural fit)

**When NOT to use CQRS:**
- Simple CRUD with roughly equal reads and writes
- Team unfamiliar with eventual consistency and its implications
- No need for separate scaling of reads and writes

---

### Communication Patterns

#### Synchronous REST

Use REST for:
- Queries where the caller needs an immediate answer
- Simple request-response interactions
- External-facing APIs (public API, partner integrations)
- When the caller must know the outcome before proceeding

**REST best practices:**
- Idempotent GET (never modify state on GET)
- Explicit timeouts on all outbound HTTP calls
- Retry with exponential backoff on 429 (rate limit) and 5xx errors
- Circuit breaker to prevent cascading failures
- Version APIs from the start (even v1 in the URL)

#### Synchronous gRPC

Use gRPC for:
- High-performance internal service-to-service communication
- When bandwidth efficiency matters (Protobuf vs JSON)
- Bi-directional streaming requirements
- Polyglot environments with strong type contracts

**gRPC considerations:**
- Requires Protobuf schema management
- Less human-readable than REST for debugging
- Excellent for: intra-cluster communication between high-throughput services
- Less suitable for: external APIs (browser compatibility issues)

#### Asynchronous Events (Kafka/MQ)

Use async events for:
- Cross-domain communication (different bounded contexts)
- Fire-and-forget commands where the caller does not need an immediate result
- Fan-out to multiple consumers
- Audit trail / event log requirements
- Decoupling producer from consumer (producer does not need to know who consumes)

```
PaymentService --[PaymentProcessed]--> Kafka topic
                                          |
                           +-----------+-----------+
                           |           |           |
                   NotificationSvc  LedgerSvc  AnalyticsSvc
```

**Async event rules:**
- Events are immutable once published
- Event schema is a public API contract — versioning rules apply
- Consumers must be idempotent (events may be delivered more than once)
- Always use a dead-letter queue for failed processing

#### Request-Reply Over Messaging

For async communication where you need a reply:
```
AuthorizationService --[AuthRequestEvent]--> request-topic
                                                   |
                                          FraudService
                                                   |
FraudService --[FraudResponseEvent]--> reply-topic with correlationId
```
Use `correlationId` to match reply to original request. Set a timeout — if no reply arrives within N seconds, treat as fraud service unavailable and apply fallback rule.

**When to use request-reply over sync REST:**
- The downstream service might be slow and you want to free the thread
- You need the resilience properties of messaging (buffering, replay)
- The response will come from a different node than received the request

---

### Resilience Patterns

#### Circuit Breaker (Resilience4j)

```java
CircuitBreakerConfig config = CircuitBreakerConfig.custom()
    .failureRateThreshold(50)           // Open at 50% failure rate
    .slowCallRateThreshold(80)          // Open if 80% of calls are slow
    .slowCallDurationThreshold(Duration.ofSeconds(2))  // "slow" = >2s
    .waitDurationInOpenState(Duration.ofSeconds(30))   // Wait 30s before half-open
    .permittedNumberOfCallsInHalfOpenState(5)
    .slidingWindowType(SlidingWindowType.COUNT_BASED)
    .slidingWindowSize(10)              // Evaluate last 10 calls
    .build();

CircuitBreaker circuitBreaker = CircuitBreaker.of("fraudService", config);

// Decorate your function:
Supplier<FraudResult> decoratedCall = CircuitBreaker.decorateSupplier(
    circuitBreaker, 
    () -> fraudServiceClient.checkFraud(request)
);

// Execute with fallback:
Try<FraudResult> result = Try.ofSupplier(decoratedCall)
    .recover(CallNotPermittedException.class, ex -> FraudResult.fallback());
```

Circuit breaker states:
- **Closed** — normal operation, calls pass through
- **Open** — fast-fail all calls, no calls attempted (return fallback immediately)
- **Half-Open** — allow a probe set of calls to test if dependency has recovered

#### Retry with Exponential Backoff and Jitter

```java
RetryConfig retryConfig = RetryConfig.custom()
    .maxAttempts(3)
    .waitDuration(Duration.ofMillis(500))
    .intervalFunction(IntervalFunction.ofExponentialRandomBackoff(
        Duration.ofMillis(500),    // initial interval
        2.0,                       // multiplier
        Duration.ofSeconds(10)     // max interval
    ))
    .retryOnException(e -> e instanceof ServiceUnavailableException)
    .retryOnResult(result -> result == null)
    .build();
```

**Jitter is not optional.** Without jitter, all clients retry at the same time after a failure — creating a thundering herd that overwhelms the recovering service. Always add random jitter to retry delays.

**What to retry vs not retry:**
- Retry: `503 Service Unavailable`, `429 Too Many Requests`, `IOException`, `SocketTimeoutException`
- Do NOT retry: `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `422 Unprocessable` — these will never succeed on retry

#### Timeout — Always Explicit, Never Default

```java
@Bean
public WebClient fraudServiceWebClient() {
    HttpClient httpClient = HttpClient.create()
        .responseTimeout(Duration.ofMillis(500));  // Explicit timeout
    return WebClient.builder()
        .baseUrl(fraudServiceBaseUrl)
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
}
```

Never rely on OS-level timeouts (they are too long — often minutes). Set explicit timeouts at the application level:
- **Connect timeout:** how long to wait to establish a connection (100–500ms typical)
- **Read timeout:** how long to wait for the first byte of a response (varies by SLA)
- **Request timeout / overall deadline:** total time budget for the whole operation

For payments: the card network SLA is typically 1–2 seconds total. Your internal service timeouts must fit within this budget with headroom.

#### Bulkhead — Thread Pool Isolation

Isolate calls to different downstream services in separate thread pools so that a slow downstream service does not exhaust the common thread pool.

```java
// Resilience4j Bulkhead
BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
    .maxConcurrentCalls(25)        // Max concurrent calls to this downstream
    .maxWaitDuration(Duration.ofMillis(50))  // How long to wait for a slot
    .build();

// ThreadPoolBulkhead for async
ThreadPoolBulkheadConfig tpBulkheadConfig = ThreadPoolBulkheadConfig.custom()
    .maxThreadPoolSize(10)
    .coreThreadPoolSize(5)
    .queueCapacity(100)
    .build();
```

Apply bulkheads per downstream dependency. This prevents a slow fraud service from blocking authorization threads when processing hundreds of concurrent transactions.

#### Fallback — Degraded Mode Operation

Every external call should have a defined fallback behavior:

```java
public AuthorizationDecision authorizeTransaction(AuthorizationRequest request) {
    return Try.of(() -> fraudServiceClient.checkFraud(request))
        .recover(Exception.class, e -> {
            log.warn("Fraud service unavailable, applying stand-in rules: {}", e.getMessage());
            metrics.counter("fraud.service.fallback").increment();
            return applyStandInFraudRules(request);  // Conservative fallback
        })
        .get();
}

private FraudResult applyStandInFraudRules(AuthorizationRequest request) {
    // Stand-in rules: approve low-value transactions, decline high-value
    if (request.getAmount().compareTo(STANDIN_THRESHOLD) <= 0) {
        return FraudResult.approveWithStandIn();
    }
    return FraudResult.declineWithStandIn();
}
```

**Fallback design rules:**
- Fallback should always be safer than the primary (more conservative, not more permissive)
- Fallback is not an error — it is designed degraded mode. Log a warning, not an error
- Alert on sustained fallback activation (fallback active for >2 minutes = dependency down)
- Document the fallback business rule explicitly (it may have regulatory implications)

---

### Service Mesh vs Library-Level Resilience

#### Use a Service Mesh (Istio, Linkerd) When:
- Platform-level resilience is required across all services regardless of language
- You want mTLS between all services without application code changes
- Centralized observability (traffic metrics, service-to-service latency) is needed
- Progressive delivery (canary, blue-green) is managed at the platform level
- The platform team wants to enforce policies without requiring each service team to implement them

**What a service mesh provides:**
- Automatic retry with configurable policy (no application code)
- Circuit breaking at the proxy level
- mTLS for all east-west traffic
- Distributed tracing propagation
- Traffic splitting (canary deployments)

**Trade-offs:**
- Adds infrastructure complexity (sidecar proxies)
- Requires Kubernetes (or compatible orchestrator)
- Debugging is harder (two layers: app + proxy)
- Retry amplification risk (app retries + mesh retries = N² total attempts)

#### Use Library-Level Patterns (Resilience4j, Hystrix) When:
- Full control over retry behavior is required (e.g., payments retry rules are business decisions)
- Not on Kubernetes or using a platform without mesh support
- Team wants to own and tune resilience behavior per service
- Testing of fallback behavior in unit tests (easier with library)
- The mesh is not available or not standardized across the fleet

**Recommendation for payments/banking services:** use library-level patterns even when a mesh is available. Payments retry and timeout behavior is a business decision (retry an authorization? No — that's a duplicate charge risk). Library-level control ensures these business rules are explicit in code and reviewable.

---

## Key Patterns

- **Database per service** — enforced strictly, no exceptions
- **ACL at legacy integration boundary** — translate legacy models, don't let them pollute new services
- **Saga with compensating transactions** — for distributed operations spanning services
- **Circuit breaker + retry + timeout + bulkhead** — the four resilience patterns applied to every external call
- **Conservative fallback** — degraded mode is safer than the primary, not more permissive
- **Event-based cross-domain communication** — different bounded contexts communicate via events, not direct calls
- **API versioning from day one** — even v1 in the URL, before you need v2

---

## Anti-Patterns (What Not To Do)

- **Shared database between services.** If two services share a DB, they are one service. Split the data or reconsider the service boundary.
- **Chatty services calling each other synchronously in a chain.** ServiceA → ServiceB → ServiceC → ServiceD synchronously creates a latency chain and tight coupling. Flatten the chain or use events.
- **"God" service.** A service that knows about everything and calls everyone. This is a monolith wearing a microservice costume.
- **Synchronous chain for distributed transaction.** Do not implement a distributed transaction by chaining synchronous calls. Use Saga instead.
- **No timeout on outbound calls.** An unconfigured HTTP client uses the OS TCP timeout — potentially minutes. This will hang threads and cause cascading failures.
- **Retry without backoff.** Immediate retry amplifies load on a struggling service. Always use exponential backoff with jitter.
- **No fallback for payment-critical dependencies.** If fraud service is down, you cannot just throw an error. Design and implement stand-in behavior.
- **Syncing data by calling another service's database directly.** Use the service's API or subscribe to its events. Never bypass the service's data ownership.
- **Breaking API changes without versioning.** Adding a required field or removing an existing field without a version bump breaks consumers. All breaking changes require a new version.
- **Deploying two services in lockstep.** If you always deploy ServiceA and ServiceB together, they are not independently deployable — revisit the boundary.

---

## Examples

### Bounded Context Decomposition for a Card Authorization System

Given: a COBOL monolith that does card authorization, fraud checking, account balance checking, and audit logging.

**Correct bounded context decomposition:**
```
AuthorizationService
  - Owns: authorization decision logic, authorization records
  - API: POST /authorizations → AuthorizationResponse
  - Calls: AccountService (sync, for balance), FraudService (sync, for fraud score)
  - Publishes: TransactionAuthorized, TransactionDeclined
  - Fallback: stand-in rules when dependencies unavailable

AccountService
  - Owns: account state, balance, limits, card status
  - API: GET /accounts/{id}/balance, POST /accounts/{id}/hold, DELETE /accounts/{id}/holds/{holdId}
  - Publishes: BalanceUpdated, AccountBlocked, LimitChanged
  - Subscribes: TransactionAuthorized (to apply hold), TransactionSettled (to post debit)

FraudService
  - Owns: fraud rules, fraud scores, device/behavioral data
  - API: POST /fraud/evaluate → FraudEvaluationResult
  - Publishes: FraudDetected, FraudCleared
  - Subscribes: TransactionAuthorized (to update velocity counters)

AuditService
  - Owns: immutable audit log
  - API: GET /audit/{transactionId} (read-only)
  - Subscribes: ALL events from AuthorizationService, AccountService, FraudService
  - Stores: event journal, never deletes
```

---

## Expected AI Behavior When This Pack Is Loaded

When this pack is loaded and the AI is asked to design a microservice or evaluate an existing one, it should:

1. **Identify the bounded context** — what business capability does this service own?
2. **Check data ownership** — does this service own its data, or is it sharing with another service?
3. **Identify the context mapping pattern** for each integration point (ACL, Open-Host, etc.)
4. **Select appropriate data patterns** — does the use case require saga, event sourcing, or CQRS?
5. **Select communication pattern** — sync REST, gRPC, or async events — and justify the choice
6. **Apply all four resilience patterns** to every external dependency: circuit breaker, retry, timeout, bulkhead
7. **Define explicit fallback behavior** for every external dependency
8. **Reject anti-patterns** — flag shared database, chatty chains, god services, missing timeouts
9. **Produce a service design document** using the `templates/architecture/service-design-template.md` format

---

## Handoff to Next Stage

Service design work using this pack produces:
- `templates/architecture/service-design-template.md` — completed service design per service
- `templates/architecture/adr-template.md` — ADRs for major pattern decisions (why saga vs 2PC, why REST vs events)
- API contracts (OpenAPI) — consumed by API design pack
- Event contracts (AsyncAPI) — consumed by event-driven pack
- Test strategy inputs — service boundaries define integration test boundaries

---

*FORGE Microservices Knowledge Pack | Domain: Distributed Systems Architecture | Version 1.0*
