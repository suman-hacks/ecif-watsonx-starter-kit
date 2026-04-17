---
knowledge-pack: "Observability"
load-when: "Adding logging, metrics, tracing, or alerting to a service; designing monitoring for production"
tools: "Claude Code, GitHub Copilot, watsonx.ai"
---

# Knowledge Pack: Observability

## When to Load This Pack

Load when:
- Adding logging to a new service
- Setting up Prometheus metrics
- Configuring distributed tracing
- Creating alerting rules
- Writing runbooks
- Reviewing observability completeness (Stage 09 P3)

## The Three Pillars

```text
OBSERVABILITY CONTEXT — inject into AI session

Three pillars of observability:

1. LOGS — what happened, when, and why
   - Structured (JSON), not string-based
   - Correlation IDs from edge to DB to event
   - No PII, no credentials, no card numbers (mask to last 4)
   - Log levels: ERROR (needs human), WARN (degraded), INFO (normal operations), DEBUG (diagnosis)
   - Retention: 7-30 days hot; 12+ months cold (PCI requirement: 12 months)

2. METRICS — how the system is behaving (quantitative)
   - Four golden signals: latency, traffic (RPS), errors, saturation
   - Business metrics: decision rate, approval/decline ratio, fraud scores
   - Infrastructure metrics: CPU, memory, pod restarts, connection pool
   - Expose via /actuator/prometheus (Spring Boot) or /metrics (generic)

3. TRACES — how a request flows through the system
   - Distributed trace ties all service calls for one request together
   - OpenTelemetry is the standard (replaces Zipkin/Jaeger client libs)
   - Every service-to-service call propagates trace context
   - Sampling: 1-10% for high-volume; 100% for errors

The fourth pillar: EVENTS (business observability)
   - Business events represent significant outcomes: AuthorizationApproved, LimitThresholdReached
   - Distinct from technical events (logs) — these are for business monitoring
   - Feed into business intelligence dashboards, SLA monitoring
```

## Structured Logging Standards

### Log Field Standards

| Field | Type | Required | Example |
|---|---|---|---|
| `timestamp` | ISO8601 UTC | Yes | `"2025-01-15T10:23:45.123Z"` |
| `level` | string | Yes | `"INFO"` |
| `service` | string | Yes | `"authorization-service"` |
| `version` | string | Yes | `"1.2.3"` |
| `correlationId` | UUID | Yes | `"550e8400-..."` |
| `event` | string | Yes | `"authorization.decision"` |
| `duration_ms` | number | When applicable | `47` |
| `businessRule` | string | When applicable | `"BR-001"` |
| `userId` | string | When applicable | Masked/pseudonymized |
| `error` | string | On ERROR | Exception message only |

### Java (Logback + Logstash Encoder)

```xml
<!-- logback-spring.xml -->
<configuration>
  <springProfile name="!local">
    <appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
      <encoder class="net.logstash.logback.encoder.LogstashEncoder">
        <includeMdcKeyName>correlationId</includeMdcKeyName>
        <includeMdcKeyName>userId</includeMdcKeyName>
        <includeMdcKeyName>requestId</includeMdcKeyName>
      </encoder>
    </appender>
    <root level="INFO">
      <appender-ref ref="JSON"/>
    </root>
  </springProfile>
</configuration>
```

```java
// Correlation ID filter — add to Spring Security or servlet filter chain
@Component
@Order(1)
public class CorrelationIdFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String correlationId = Optional.ofNullable(request.getHeader("X-Correlation-ID"))
                .orElse(UUID.randomUUID().toString());
        
        MDC.put("correlationId", correlationId);
        response.setHeader("X-Correlation-ID", correlationId);
        
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}

// Business operation logging pattern
private static final Logger log = LoggerFactory.getLogger(AuthorizeCardUseCase.class);

public AuthorizationResult execute(AuthorizationRequest request) {
    long startTime = System.currentTimeMillis();
    
    try {
        AuthorizationResult result = doAuthorize(request);
        
        log.atInfo()
           .addKeyValue("event", "authorization.completed")
           .addKeyValue("decision", result.getDecision())
           .addKeyValue("cardId", maskCardId(request.getCardId()))  // Last 4 only
           .addKeyValue("duration_ms", System.currentTimeMillis() - startTime)
           .addKeyValue("merchantId", request.getMerchantId())
           .log("Authorization completed");
        
        return result;
        
    } catch (BusinessRuleException e) {
        log.atInfo()
           .addKeyValue("event", "authorization.declined")
           .addKeyValue("declineReason", e.getDeclineReason())
           .addKeyValue("businessRule", e.getBusinessRuleId())
           .addKeyValue("cardId", maskCardId(request.getCardId()))
           .log("Authorization declined by business rule");
        throw e;
        
    } catch (Exception e) {
        log.atError()
           .addKeyValue("event", "authorization.error")
           .addKeyValue("errorType", e.getClass().getSimpleName())
           .addKeyValue("duration_ms", System.currentTimeMillis() - startTime)
           .setCause(e)
           .log("Authorization processing error");  // Stack trace included automatically
        throw e;
    }
}

private String maskCardId(String cardId) {
    if (cardId == null || cardId.length() < 4) return "XXXX";
    return "XXXX-XXXX-XXXX-" + cardId.substring(cardId.length() - 4);
}
```

## Metrics Standards (Micrometer / Prometheus)

```java
@Component
public class AuthorizationMetrics {
    
    private final Counter approvedCounter;
    private final Counter declinedCounter;
    private final Timer authorizationTimer;
    private final DistributionSummary amountSummary;
    
    public AuthorizationMetrics(MeterRegistry registry) {
        // Business operation counters — always tag with outcome and rule
        approvedCounter = Counter.builder("authorization.decisions.total")
                .tag("decision", "APPROVED")
                .description("Total approved authorizations")
                .register(registry);
        
        declinedCounter = Counter.builder("authorization.decisions.total")
                .tag("decision", "DECLINED")
                .description("Total declined authorizations")
                .register(registry);
        
        // Latency histogram with SLO buckets matching NFRs
        authorizationTimer = Timer.builder("authorization.duration")
                .description("Authorization processing duration")
                .publishPercentiles(0.5, 0.95, 0.99)
                .sla(Duration.ofMillis(100), Duration.ofMillis(200), Duration.ofMillis(500))
                .register(registry);
        
        // Amount distribution for business analytics
        amountSummary = DistributionSummary.builder("authorization.amount")
                .description("Authorization amounts (in cents)")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }
    
    public void recordApproved(BigDecimal amount, long durationMs) {
        approvedCounter.increment();
        authorizationTimer.record(durationMs, TimeUnit.MILLISECONDS);
        amountSummary.record(amount.movePointRight(2).doubleValue());
    }
    
    public void recordDeclined(String reason, long durationMs) {
        Counter.builder("authorization.decisions.total")
                .tag("decision", "DECLINED")
                .tag("reason", reason)
                .register(meterRegistry)
                .increment();
        authorizationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }
}
```

## Prometheus Alert Rules

```yaml
# authorization-service.rules.yaml
groups:
  - name: authorization-service-slos
    rules:
      # SLO: p99 latency < 200ms
      - alert: AuthorizationHighLatency
        expr: histogram_quantile(0.99, rate(authorization_duration_bucket[5m])) > 0.2
        for: 5m
        labels:
          severity: warning
          team: payments
        annotations:
          summary: "Authorization p99 latency exceeds 200ms SLO"
          description: "Current p99: {{ $value | humanizeDuration }}"
          runbook: "https://runbooks.company.com/authorization-high-latency"

      # SLO: error rate < 1%
      - alert: AuthorizationHighErrorRate
        expr: |
          rate(http_server_requests_seconds_count{service="authorization-service",status=~"5.."}[5m])
          /
          rate(http_server_requests_seconds_count{service="authorization-service"}[5m])
          > 0.05
        for: 5m
        labels:
          severity: critical
          team: payments
        annotations:
          summary: "Authorization error rate exceeds 5%"
          description: "Error rate: {{ $value | humanizePercentage }}"
          runbook: "https://runbooks.company.com/authorization-errors"

      # Infrastructure: DB connection pool
      - alert: DBConnectionPoolExhausted
        expr: hikaricp_connections_active / hikaricp_connections_max > 0.9
        for: 2m
        labels:
          severity: warning
        annotations:
          summary: "Database connection pool > 90% utilized"
          runbook: "https://runbooks.company.com/db-pool-exhausted"
```

## Distributed Tracing (OpenTelemetry)

```java
// Spring Boot auto-configuration (add to pom.xml):
// io.micrometer:micrometer-tracing-bridge-otel
// io.opentelemetry:opentelemetry-exporter-otlp

// application.yaml
management:
  tracing:
    sampling:
      probability: 0.1  # 10% sampling in production
  otlp:
    tracing:
      endpoint: http://otel-collector:4318/v1/traces
    
spring:
  application:
    name: authorization-service  # Appears as service name in traces

# Automatic: Spring Web, WebClient, Kafka, JDBC are all auto-instrumented
# Manual span for critical business operation:
```

```java
@Autowired
private Tracer tracer;

public AuthorizationResult execute(AuthorizationRequest request) {
    Span span = tracer.nextSpan().name("authorization.execute").start();
    
    try (Tracer.SpanInScope scope = tracer.withSpan(span)) {
        span.tag("cardId", maskCardId(request.getCardId()));
        span.tag("merchantId", request.getMerchantId());
        
        AuthorizationResult result = doAuthorize(request);
        span.tag("decision", result.getDecision().name());
        return result;
        
    } catch (Exception e) {
        span.error(e);
        throw e;
    } finally {
        span.end();
    }
}
```

## AI Session Instructions

When adding observability, the AI must:
1. Add correlation ID propagation to ALL service-to-service calls (HTTP headers + Kafka message headers)
2. Never log PII — mask card numbers to last 4, mask names, never log passwords or tokens
3. Add structured log statements at INFO for every business operation outcome
4. Add ERROR logs with correlation ID for every caught exception (no swallowed exceptions)
5. Add Micrometer metrics for request rate, latency (with percentiles), and error rate
6. Add custom business metrics for domain-specific outcomes (approval rate, decline reasons)
7. Configure health indicators for all external dependencies (DB, Kafka, external APIs)
8. Add Prometheus alert rules for all SLOs defined in the NFR catalog
9. Include `runbook` annotation in every alert rule
10. Set sampling rate ≤ 10% for tracing in high-volume production services
