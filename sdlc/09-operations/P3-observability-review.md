# P3: Observability Review

## When to Use This Prompt
During Stage 09 (Operations) or as a pre-deployment gate check. Use to audit whether a service has sufficient observability to be operated safely in production. Required before Gate 8 (Production Deployment Approval).

---

## Prompt

```text
You are a FORGE DevSecOps / Platform Engineer performing an observability review.

INPUTS
Service name: [SERVICE-NAME]
Code or configuration to review: [Paste relevant code, or describe what logging/metrics/tracing is implemented]
NFRs for observability: [Paste from Stage 02 P3 NFR definition — availability, latency SLOs]
Production environment: [Kubernetes / VM / z/OS / cloud — describe]
Monitoring stack: [Prometheus + Grafana / Datadog / IBM Instana / Dynatrace / AWS CloudWatch]
Alerting system: [PagerDuty / OpsGenie / VictorOps]
Existing runbook: [Yes — link / No — create as part of Stage 09 P1]

TASK
Review observability completeness and produce an observability health report.

---

# Observability Review: [SERVICE-NAME]
Reviewed by: [AI tool]  Date: [date]
Monitoring stack: [stack]
Status: PRODUCTION-READY / GAPS TO FIX / INSUFFICIENT — BLOCK DEPLOYMENT

---

## Observability Pillars Assessment

### Pillar 1: Logging

**Requirement:** Every business operation produces structured, queryable logs. No PII. Correlation IDs throughout.

| Check | Status | Evidence | Gap / Recommendation |
|---|---|---|---|
| Structured logging (JSON) | PASS / FAIL | [e.g., logstash-logback-encoder configured] | |
| Correlation ID propagated | PASS / FAIL | [MDC.put("correlationId", ...) in filter] | |
| Business operation outcomes logged | PASS / FAIL | [INFO log at use case exit] | |
| No PII in any log statement | PASS / FAIL | [grep for name/email/ssn/cardNumber in logs] | |
| No credentials or tokens logged | PASS / FAIL | | |
| Error stack traces at ERROR level | PASS / FAIL | | |
| Log level configurable at runtime | PASS / FAIL | [Spring Boot Actuator /loggers endpoint] | |
| Logs ship to centralized system | PASS / FAIL | [Fluent Bit sidecar / log agent configured] | |
| Log retention configured | PASS / FAIL | [7 days hot / 12 months cold] | |

**Log Quality Sample Review:**

Expected log format:
```json
{
  "timestamp": "2025-01-15T10:23:45.123Z",
  "level": "INFO",
  "service": "authorization-service",
  "version": "1.2.3",
  "correlationId": "550e8400-e29b-41d4-a716-446655440000",
  "event": "authorization.decision",
  "cardId": "XXXX-XXXX-XXXX-1234",
  "decision": "APPROVED",
  "businessRule": "BR-001",
  "duration_ms": 47,
  "merchantId": "MERCHANT-001"
}
```

Issues to flag:
- String concatenation in log messages (prevents structured querying)
- Log levels inconsistent (debug in production, info missing from critical paths)
- Missing correlation ID in any service-to-service call
- PII appearing in any field

---

### Pillar 2: Metrics

**Requirement:** Every service exposes metrics for its business operations, dependencies, and resource usage.

| Metric | Type | Labels | Current Status | Gap |
|---|---|---|---|---|
| Request rate (RPS) | Counter | endpoint, method, status_code | PRESENT / MISSING | |
| Request latency | Histogram | endpoint, p50/p99/p999 | PRESENT / MISSING | |
| Business operation rate | Counter | operation, decision | PRESENT / MISSING | |
| External dependency health | Gauge | dependency_name, status | PRESENT / MISSING | |
| JVM / runtime metrics | Gauge | heap, GC, threads | PRESENT / MISSING (auto via Micrometer) | |
| Database connection pool | Gauge | pool_name, state | PRESENT / MISSING | |
| Kafka consumer lag | Gauge | topic, partition, consumer_group | PRESENT / MISSING | |
| Error rate by type | Counter | exception_class | PRESENT / MISSING | |

**Metrics implementation check (Spring Boot / Micrometer):**
```java
// Required: Business operation counter
@Autowired
private MeterRegistry meterRegistry;

private void recordDecision(String decision, String businessRule) {
    meterRegistry.counter("authorization.decisions.total",
            "decision", decision,
            "businessRule", businessRule)
            .increment();
}

// Required: Business operation timing
private AuthorizationResult timedAuthorize(AuthorizationRequest request) {
    return Timer.builder("authorization.duration")
            .tag("endpoint", "authorize")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry)
            .record(() -> authorize(request));
}
```

---

### Pillar 3: Tracing

**Requirement:** Distributed traces correlate a request across all services it touches.

| Check | Status | Evidence | Gap |
|---|---|---|---|
| OpenTelemetry / Zipkin / Jaeger agent configured | PASS / FAIL | | |
| Trace IDs propagated via HTTP headers (B3 / W3C TraceContext) | PASS / FAIL | | |
| Trace IDs propagated via Kafka message headers | PASS / FAIL | | |
| All external calls include trace context | PASS / FAIL | | |
| Trace sampling rate configured (not 100% in prod) | PASS / FAIL | [1-10% sampling for high-volume] | |
| Service name set correctly in spans | PASS / FAIL | | |

---

### Pillar 4: Alerting

**Requirement:** Production issues are detected and escalated before customers notice.

**SLO-based alerts (must have for production):**

| Alert | Condition | Severity | Routing | Runbook Link |
|---|---|---|---|---|
| High error rate | error_rate > 5% for 5 min | P1 | On-call → PagerDuty | runbook/authorization-high-error-rate.md |
| Critical error rate | error_rate > 20% for 2 min | P0 | On-call + Lead Engineer | same |
| High p99 latency | p99 > 500ms for 5 min | P2 | On-call (low urgency) | runbook/authorization-high-latency.md |
| Service down | health endpoint fails 3 consecutive checks | P0 | On-call + Lead Engineer | runbook/authorization-service-down.md |
| DB connection pool exhausted | pool_active / pool_max > 90% for 2 min | P1 | On-call | runbook/authorization-db-pool.md |
| Kafka consumer lag | consumer_lag > 10000 for 5 min | P2 | On-call (low urgency) | runbook/authorization-kafka-lag.md |

**Alert quality criteria:**
- [ ] Every alert has a runbook link
- [ ] Every P0/P1 alert pages a human
- [ ] Alerts have appropriate thresholds — not too sensitive (alert fatigue) or too loose
- [ ] Alert conditions tested in staging
- [ ] On-call rotation confirmed before production deployment

---

### Pillar 5: Dashboards

**Minimum required dashboards before production:**

| Dashboard | Panels | Audience | Status |
|---|---|---|---|
| Service health overview | RPS, error rate, p99 latency, pod count | On-call, Lead Eng | PRESENT / MISSING |
| Business metrics | Authorization rate, approval/decline ratio by reason | PO, Ops | PRESENT / MISSING |
| Dependency health | DB latency, DB pool, fraud service latency | On-call | PRESENT / MISSING |
| Infrastructure | CPU, memory, pod restarts, HPA scale events | DevSecOps | PRESENT / MISSING |

---

### Pillar 6: Health Endpoints

| Endpoint | Purpose | Status |
|---|---|---|
| `/actuator/health` | Overall health (used by load balancer) | PRESENT / MISSING |
| `/actuator/health/liveness` | Is the process alive? (Kubernetes liveness probe) | PRESENT / MISSING |
| `/actuator/health/readiness` | Is the service ready for traffic? (Kubernetes readiness probe) | PRESENT / MISSING |
| `/actuator/prometheus` | Metrics scrape endpoint | PRESENT / MISSING |
| `/actuator/info` | Version, build info | PRESENT / MISSING |

**Custom health indicator requirement:**
```java
@Component
public class DatabaseHealthIndicator extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(Health.Builder builder) {
        // Attempt a simple DB query
        // If it fails, return DOWN — this will fail the readiness probe
        // and remove the pod from load balancer rotation
    }
}
```

---

## Observability Gaps Summary

| Gap ID | Pillar | Description | Severity | Owner | Resolved Before Deploy? |
|---|---|---|---|---|---|
| OBS-001 | Logging | Correlation ID not propagated to Kafka messages | High | Dev | Yes |
| OBS-002 | Alerting | No alert for DB connection pool exhaustion | High | DevSecOps | Yes |
| OBS-003 | Dashboard | Business metrics dashboard not created | Medium | DevSecOps | Recommended |

**Overall status:** PRODUCTION-READY / GAPS TO FIX / INSUFFICIENT

Gate 8 prerequisite: All High severity observability gaps resolved.
```
