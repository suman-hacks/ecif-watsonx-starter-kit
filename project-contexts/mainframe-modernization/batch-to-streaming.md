---
context: "Batch-to-Streaming Modernization"
parent: "project-contexts/mainframe-modernization/README.md"
use-when: "Replacing nightly batch jobs (JCL/COBOL) with real-time or near-real-time stream processing"
---

# Project Context: Batch-to-Streaming Modernization

## The Core Challenge

Mainframe batch jobs process large volumes of data on a schedule (nightly, weekly). Streaming processes data as it arrives (milliseconds to seconds latency). These are fundamentally different paradigms:

| Dimension | Batch | Streaming |
|---|---|---|
| Latency | Hours (next morning) | Milliseconds to seconds |
| Data arrival | All at once (file, DB snapshot) | Continuous, event-by-event |
| State | Entire dataset in memory / sequential | Per-key state, windowed aggregations |
| Error handling | Re-run the whole job | Individual record retry / DLQ |
| Ordering | Deterministic (sequential file) | May be out-of-order (network) |
| Transactions | Typically no DB transactions | At-least-once; idempotency required |
| Exactly-once | N/A (batch is inherently exactly-once) | Requires careful design |

**Rule:** Not all batch jobs should become streaming. Analyze the business need first.

---

## Decision Framework: When to Stream vs Keep as Batch

Ask these questions before choosing streaming:

| Question | If Yes → Streaming | If No → Consider Keeping Batch |
|---|---|---|
| Does the business need results in < 1 hour? | Yes | No — batch may be fine |
| Does data arrive continuously throughout the day? | Yes | No |
| Do downstream systems need to react to individual events? | Yes | No |
| Are there 10+ million records processed in each batch run? | Consider both | |
| Is there complex interdependency between records? | Streaming is harder — assess | |
| Does the process require global sort across all records? | Streaming is hard — keep batch | |
| Is the business SLA time-of-day (e.g., "by 2am")? | May keep as batch | |

**Hybrid approach is valid:** Some sub-processes stream; the final reconciliation remains batch.

---

## JCL-to-Streaming Mapping

| JCL/Batch Concept | Streaming Equivalent | Notes |
|---|---|---|
| JCL JOB | Streaming application (Kafka Streams / Flink / Spark Streaming) | One job → one streaming topology |
| JCL EXEC (program step) | Stream processing operator / processor | Each step → stream processor |
| JCL DD INPUT file | Kafka topic (source) | File → continuous event stream |
| JCL DD OUTPUT file | Kafka topic (sink) / database write | Output → downstream consumers |
| SORT CARD | KTable join / stream sort (windowed) | Global sort impossible in streaming |
| ICETOOL MERGE | Stream merge (co-partition topics) | Requires same partition key |
| DB2 SELECT (batch) | KTable / reference data store | Materialized view for lookups |
| CHECKPOINT restart | Kafka offset commit | Committed offset = processed position |
| ABEND + restart | DLQ + replay | Failed record → DLQ → investigate → replay |
| Job schedule (cron) | Triggered by first event of day | Or keep as scheduled trigger for hybrid |

---

## Common Batch Pattern Translations

### Pattern 1: Sequential Record Processing (most common)
**Batch:** Read all records → process each → write output file  
**Stream:** Kafka topic → processor (stateless) → output topic

```java
// Kafka Streams — stateless record processor
KStream<String, LimitRecord> limitStream = builder.stream("batch.limit-updates");

limitStream
    .mapValues(record -> processLimitUpdate(record))   // Stateless transformation
    .filter((key, result) -> result.isValid())
    .to("processed.limit-updates");
```

### Pattern 2: Balance Update (stateful — most complex)
**Batch:** Sort by account → accumulate running total → write new balance  
**Stream:** Kafka Streams KTable for per-account state aggregation

```java
// Stateful aggregation — running balance per account
KTable<String, Balance> accountBalances = transactions
    .groupByKey()  // Group by accountId (= partition key)
    .aggregate(
        Balance::zero,                          // Initial value
        (accountId, txn, balance) ->            // Aggregator
            balance.apply(txn),
        Materialized.as("account-balances")     // State store name
    );

// Output: balance updated for each transaction
accountBalances.toStream().to("account.balances.current");
```

### Pattern 3: Reconciliation (compare two data sources)
**Batch:** Full outer join of two files → find discrepancies  
**Stream:** Co-partitioned topics + windowed join

```java
// Windowed join — match authorizations with settlements within 24h window
KStream<String, Authorization> authorizations = builder.stream("authorization.approved");
KStream<String, Settlement> settlements = builder.stream("settlement.received");

authorizations.join(
    settlements,
    (auth, settlement) -> reconcile(auth, settlement),
    JoinWindows.ofTimeDifferenceAndGrace(Duration.ofHours(24), Duration.ofHours(1)),
    StreamJoined.with(Serdes.String(), authSerde, settlementSerde)
).to("reconciliation.results");
```

### Pattern 4: Nightly Report Generation (keep as triggered batch)
**Analysis:** Report generation benefits from complete daily data; streaming adds complexity with no business benefit  
**Decision:** Keep as scheduled job, but trigger via event rather than cron  
**Implementation:** Kafka consumer that triggers when end-of-day marker event received

```java
@KafkaListener(topics = "day.processing.complete")
public void onDayClose(DayCloseEvent event) {
    // Trigger existing batch report job via REST or message
    reportService.generateDailyReport(event.getBusinessDate());
}
```

---

## Key Design Decisions for Batch-to-Streaming

### 1. Partition Key Selection (Critical)
The partition key determines ordering and state co-location.

- **Use the business entity key** (accountId, cardId) — all events for the same entity go to the same partition
- **Avoid sequential keys** (timestamp, sequence number) — creates hotspots
- **Verify with SME:** What was the sort key in the batch job? That's usually the right partition key.

### 2. State Store Management
Streaming applications accumulate state. Define:

| State | Store Type | Size | Retention | Backup |
|---|---|---|---|---|
| Running balance | KTable (RocksDB) | Per key | Forever | Kafka changelog topic |
| Daily counters | KTable with TTL | Per key | 24 hours | Kafka changelog topic |
| Reference data (card status) | GlobalKTable | Full copy per instance | Forever | Kafka compacted topic |

### 3. Out-of-Order Event Handling
Streaming events may arrive out of order (network delays). Define:

- **Event time vs processing time** — use event timestamp, not arrival timestamp
- **Grace period** — how long to wait for late events before considering a window closed
- **Late event handling** — update result? discard? DLQ?

### 4. Exactly-Once vs At-Least-Once
- **At-least-once** (default Kafka): easier; consumers must be idempotent
- **Exactly-once** (Kafka Streams with transactional producers): higher overhead; use only when duplicate events cause business problems (e.g., duplicate financial postings)

---

## Analysis Prompts for JCL/COBOL Batch Jobs

Use with Agent 02 (Legacy Analyzer):

```text
Analyze this batch job for streaming migration feasibility:

1. SORT/MERGE analysis:
   - What is the sort key? Is this a natural business entity key?
   - Is the sort used for sequential processing or just ordering?
   - Can ordering be replaced with per-key stateful processing?

2. State analysis:
   - Does any step maintain running totals, counts, or accumulated state?
   - Is there carry-forward state between business days?
   - What is the maximum state size per key?

3. Dependency analysis:
   - Does this job depend on another job's output?
   - Are there circular dependencies between jobs?
   - What is the job's position in the overall batch schedule?

4. Exactly-once requirement:
   - What happens if a record is processed twice?
   - Are there financial postings, balance updates, or irreversible operations?

5. Streaming recommendation:
   - Which steps are good streaming candidates?
   - Which steps should remain as batch?
   - Hybrid architecture recommendation if applicable
```

---

## Technology Selection Guide

| Use Case | Recommended | Alternative | Avoid |
|---|---|---|---|
| Simple record transformation | Kafka Streams | Spring Kafka | Flink (overkill) |
| Complex stateful aggregations | Kafka Streams or Flink | | |
| Global sort required | Keep batch | | Streaming (redesign needed first) |
| ML/scoring on stream | Flink + model serving | Spark Streaming | |
| IBM MQ source (mainframe) | IBM MQ → Kafka bridge | IBM Event Streams | |
| On-premise only | Kafka + Flink | IBM Event Streams | Cloud-only solutions |
| AWS cloud | Amazon Kinesis + Lambda | MSK + Kafka Streams | |
| Real-time dashboard | Kafka Streams → ksqlDB | Flink → Druid | |
