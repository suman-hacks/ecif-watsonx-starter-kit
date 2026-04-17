# T3: Integration and Latency Map

**Task:** 3 of 5  
**Output File:** `TX_INTEGRATIONS.md`  
**Who Runs It:** Lead Engineer or Integration Architect  
**Time Required:** 5–10 minutes  
**Prerequisite:** T1 (TX_ARCH.md) completed. Codebase loaded into AI assistant context.

---

## Stage Overview

This task maps every integration point the system has — inbound, outbound, and data lookups during processing — and estimates their contribution to end-to-end latency. It answers: *what external dependencies does this system have, how does data flow through them, and what happens when they are slow or unavailable?*

For modernization projects, this document reveals integration complexity and migration risk. For AI augmentation, it shows where real-time data enrichment is possible and what the latency budget allows. For reliability projects, it identifies the failure points that require stand-in behavior.

## Workshop Connection

`TX_INTEGRATIONS.md` is used in the "Integration Constraints" and "Availability" segments of the discovery workshop. It directly informs:
- Which integrations are showstoppers for a POC
- Whether real-time AI inference is feasible within the latency budget
- Which stand-in scenarios must be handled in any modernized architecture

## Tool Guidance

| Tool | Instructions |
|---|---|
| **GitHub Copilot** | Use `@workspace`. Also examine connection pool configs, timeout settings, and retry logic explicitly |
| **Claude Code** | Ask Claude to search for timeout configurations, circuit breaker patterns, and retry decorators specifically |
| **watsonx Code Assist** | Open configuration files (application.yml, application.properties, env files) alongside source files |
| **Cursor** | Use `@codebase`. Supplement with `@file` references to integration-specific configuration files |

**Tip:** Latency information is rarely in source code — look for it in timeout configurations, SLA documentation, comments, and integration test stubs. Note the source of every latency estimate.

---

## The Prompt

Copy everything between the `---PROMPT START---` and `---PROMPT END---` markers.

---PROMPT START---

You are a senior integration architect performing a pre-engagement integration and latency analysis. Analyze this codebase and produce a complete map of all integration points and their latency implications. Ground every claim in code evidence: cite configuration files, client class names, timeout settings, and connection configurations.

**Project context:** [INSERT: same description used in T1]

**Known integration points from T1 analysis:** [INSERT: paste relevant section from TX_ARCH.md, or write "Unknown — discover from scratch"]

Produce a document titled "Integration and Latency Map" with the following sections:

---

## 1. Inbound Interfaces

List every interface through which external systems send data INTO this system. Produce a table with columns: **Interface Name | Protocol / Format | Source System | Trigger Model | Volume Indicator | Entry Point (file/class)**

**Protocol/Format options:** REST/JSON, REST/XML, SOAP/XML, ISO 8583/TCP, ISO 20022/XML, SWIFT, FIX, MQ/JMS, Kafka, gRPC/Protobuf, SFTP/Fixed-width, SFTP/CSV, Batch File, WebSocket, GraphQL, or other

**Trigger Model options:** Synchronous Request-Response, Asynchronous Message (fire-and-forget), Event-Driven (consume from queue/topic), Scheduled Batch, Webhook/Callback

**Volume Indicator:** Extract from configuration (thread pool sizes, consumer group counts, batch sizes, queue depth settings, rate limiter configs). If not found in config, mark as `[UNKNOWN — check ops team]`.

For each inbound interface, also document:
- Authentication mechanism used (mTLS, API Key, JWT, basic auth, IP allowlist, none)
- Message validation/schema enforcement (yes/partial/none — with evidence)
- How malformed or unexpected messages are handled
- Whether the interface supports idempotency (can the same message be safely replayed?)

---

## 2. Data Lookups During Transaction Processing

List every database query, cache lookup, or in-process data fetch that occurs during real-time transaction processing. Produce a table with columns: **Lookup Name | Data Source Type | Data Retrieved | Lookup Pattern | Cache Layer? | Estimated Latency Impact | Critical Path?**

**Data Source Type options:** Relational DB (note name/type), NoSQL DB, In-Memory Cache (Redis, Memcached, Hazelcast), Local Cache (JVM heap), External API, Mainframe (via API or direct), File System, Message Queue read

**Lookup Pattern options:** Point lookup by PK, Range query, Full table scan (flag as risk), Stored procedure call, Batch lookup, Pub/Sub read

**Estimated Latency Impact:** Extract from timeout configurations (e.g., `connectionTimeout: 200ms` implies expected latency < 200ms). If no timeout config found, mark as `[UNKNOWN — measure in test environment]`.

**Critical Path?** YES if this lookup must complete before a response can be returned; NO if it is async or done post-response.

For each lookup, also note:
- What happens if this lookup fails or times out (fail-open, fail-closed, cached fallback, stand-in value)
- Whether N+1 query patterns are present (querying in a loop)
- Connection pool configuration (max connections, wait timeout)

---

## 3. Outbound Calls and Service Dependencies

List every call this system makes to external systems during or after processing. Produce a table with columns: **Target System | Protocol | Purpose | Sync/Async | Critical Path? | Timeout Config | Retry Config | Circuit Breaker?**

Cover:
- Card network authorizations (Visa, Mastercard, Amex, etc.)
- Fraud/risk scoring services (internal or vendor)
- Core banking / account management systems
- Customer data / identity services
- Notification services (SMS, email, push)
- Audit logging services
- Settlement and clearing systems
- Third-party data enrichment services
- Reporting / analytics pipelines
- Any other outbound integration

For each outbound call, document:
- The client class or library used (e.g., `FeignClient`, `RestTemplate`, `WebClient`, `axios`, `requests`)
- Timeout settings (connection timeout, read timeout, write timeout — extract exact values from config)
- Retry configuration (max retries, backoff strategy, retryable error codes)
- Circuit breaker configuration (if present: threshold, half-open window, fallback behavior)
- What error codes or exceptions are expected and handled
- What happens to the transaction if this call fails (the fallback or stand-in behavior)

---

## 4. Message Formats and Protocols

For each unique message format used in the system, provide a detailed description:

### [Format Name] (e.g., ISO 8583, ISO 20022, Kafka JSON Event, Fixed-Width File)

**Direction:** Inbound | Outbound | Both  
**Standard/Specification:** [standard name and version, or "proprietary"]  
**Parser/Library Used:** [class or library name, with version if in dependencies]  
**Key Fields Mapped:** List the most important fields used by the business logic (e.g., "Field 2: PAN, Field 4: Amount, Field 39: Response Code")  
**Validation:** How is message validity enforced? (schema validation, custom parser, none)  
**Character Encoding / Endianness:** (relevant for binary formats)  
**Version Handling:** How are different versions of the same format handled?  
**Extension Fields:** Any proprietary extensions to standard formats?  
**Test Coverage:** Are there unit tests for message parsing? Any test fixtures with sample messages?

---

## 5. Latency Budget Analysis

### End-to-End Latency Target
**SLA Target:** [Extract from config, comments, or README. If not found: `[UNKNOWN — check SLA documentation]`]  
**Source of SLA Config:** [file name and key, or "not found in codebase"]

### Latency Contributors

Produce a table estimating the latency contribution of each processing step, ordered from largest to smallest. Columns: **Step | Component | Latency Estimate | Source of Estimate | % of Total Budget**

Sources for estimates (in priority order):
1. Explicit timeout configurations in the codebase
2. Connection pool / keep-alive configurations
3. Comments or documentation in the code
4. Industry benchmarks for well-known operations (note when using this)

### Latency Risk Assessment

**Biggest Latency Risks:**
List the 3–5 steps with the highest latency or the highest uncertainty, and explain why they are risks.

**Synchronous Chain Depth:**
How many sequential synchronous calls are in the critical path? (Deeper chains = higher tail latency risk)

**Timeout Configuration Quality:**
Are all external calls timeout-configured? List any calls with no timeout configured (these are latency bombs).

**Caching Opportunities Observed:**
List any repeated lookups of the same data that could be cached but currently are not.

---

## 6. Stand-In and Fallback Behavior

This is critical for resilience and for understanding what the system does when dependencies are unavailable.

Produce a table with columns: **Dependency | Failure Scenario | Current Fallback Behavior | Fallback Evidence (file/class) | Stand-In Data Source | Recovery Trigger**

For each dependency identified in sections 1–3, document:
- What happens when the dependency is completely unavailable
- What happens when the dependency is slow (latency spike, partial degradation)
- What happens when the dependency returns an error
- Whether the system has explicit stand-in/offline mode logic
- Whether stand-in decisions are audited/reconciled later

### Stand-In Risk Assessment

**Highest-Risk Failure Modes:** Which dependency failure would cause the most harm (customer impact, financial loss, compliance breach)?

**Unhandled Failure Modes:** Which dependencies have no fallback behavior? (These are single points of failure.)

**Stand-In Data Staleness:** If cached or stand-in data is used, how old can it be? Is there a maximum staleness configuration?

**Recovery Behavior:** When a dependency recovers, does the system automatically resume normal behavior, or does it require manual intervention?

---

**Output format requirements:**
- Use Markdown with clear heading hierarchy
- All tables must be properly formatted Markdown tables
- Timeout and retry configurations must be extracted verbatim from config files with the source file cited
- Mark latency estimates based on industry benchmarks (not config) with `[ESTIMATED — no config found]`
- Mark any integration point not found in the codebase with `[EXTERNAL — not in repo]`
- Analysis only — no recommendations at this stage

---PROMPT END---

---

## Output Template

Save the AI's output as `TX_INTEGRATIONS.md` in the `pre-engagement/` folder. The file should begin with:

```markdown
# Integration and Latency Map
**Project:** [Project Name]
**Date:** [YYYY-MM-DD]
**Analyzed By:** [Engineer Name]
**AI Tool Used:** [Tool Name + version]
**Codebase Commit/Branch:** [Git SHA or branch name]
**Related Documents:** TX_ARCH.md, TX_DECISIONS.md
**Review Status:** [ ] Draft | [ ] Reviewed | [ ] Approved

---
```

## Completion Checklist

- [ ] All 6 sections are populated
- [ ] Every inbound interface has authentication mechanism documented
- [ ] Every outbound call has timeout and retry configuration (or `[UNKNOWN]` if not found)
- [ ] Latency budget table covers all major processing steps
- [ ] Stand-in behavior is documented for all critical dependencies
- [ ] ISO 8583 or other financial message format fields are mapped (if applicable)
- [ ] Unhandled failure modes (single points of failure) are explicitly flagged
- [ ] Document reviewed by integration or platform engineer
- [ ] Saved and shared with workshop facilitator

## Common Pitfalls

**Timeout configurations hidden in external config.** Many systems externalize timeout configs to environment variables or secrets managers. The code may show `${db.timeout}` but the actual value lives in Vault, Kubernetes ConfigMap, or a `.env` file not committed to the repo. Note these as `[EXTERNALIZED — check ops configuration]`.

**Missing async/event-driven integrations.** Kafka consumers, JMS listeners, and SQS pollers are not always obvious from reading entry-point code. Search specifically for message listener annotations, `@KafkaListener`, `@JmsListener`, `MessageDrivenBean`, or equivalent.

**Stand-in logic disguised as error handling.** Fallback behavior is often written as catch blocks or circuit breaker fallback methods rather than as explicit stand-in logic. Search for fallback, default, offline, and degraded mode patterns specifically.

**Latency budget missing the network.** AI analysis will often miss network transit time (especially for cross-region calls) and TLS handshake costs. Add these manually based on architecture topology if known.

**ISO 8583 field mapping incomplete.** ISO 8583 is a binary protocol. If the system uses it, there may be a field map configuration file (CSV, XML, or properties format) that defines which fields are used. Ensure the AI finds and analyzes this configuration file, not just the parser code.

---

*Previous Task: T2 — Decision Logic Inventory | Next Task: T4 — Complexity and Risk Map*
