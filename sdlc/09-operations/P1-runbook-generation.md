# P1 — Runbook Generation

**Stage:** 09 — Operations  
**Persona:** SRE, DevSecOps, Lead Engineer  
**Output file:** `operations/runbooks/[service]-runbook.md`

---

## The Prompt

```text
You are an SRE generating operational runbooks for a production service.

CONTEXT
Service: [NAME]
Architecture: [brief — e.g., "Spring Boot service on Kubernetes, depends on PostgreSQL and Kafka"]
SLA: [availability target — e.g., 99.95%]
On-call rotation: [team responsible]
Monitoring: [tools — e.g., Grafana, PagerDuty, Datadog]
Key dashboards: [dashboard URLs or names]

ALERTS THAT EXIST (paste alert definitions or describe them):
[List alerts with their names and thresholds]

TASK
Generate a production runbook covering:

## 1. Service Overview
[1 paragraph: what this service does, why it matters, downstream impact if it fails]

## 2. Architecture Quick Reference
[ASCII diagram of service and its direct dependencies]

## 3. Access and Tools
| What | How | Notes |
|---|---|---|
| View logs | `kubectl logs -n [ns] -l app=[service]` or [logging tool URL] | |
| View metrics | [Dashboard URL] | |
| SSH / exec into pod | `kubectl exec -n [ns] -it [pod] -- /bin/sh` | Last resort only |
| Scale up/down | `kubectl scale deploy/[service] --replicas=N` | |

## 4. Alert Runbooks

For EACH alert:

### Alert: [ALERT_NAME]

**What it means (plain English):** [2-3 sentences]
**Urgency:** [P1 / P2 / P3]
**Business impact:** [what customers/systems experience]

**Investigation steps:**
1. [First thing to check — be specific about commands and where to look]
2. [Second thing]
3. [If X is true, go to section Y]

**Common causes and fixes:**
| Cause | How to Identify | Fix |
|---|---|---|
| [e.g., Database connection pool exhausted] | [pg_stat_activity shows max_connections reached] | [Restart service; scale up replicas; increase pool size] |

**Escalation:**
- After [X minutes] without resolution: page [role/person]
- If data loss suspected: immediately page [security/data owner] and DO NOT restart

## 5. Restart and Recovery Procedures

### Graceful Restart
```bash
# Rolling restart (zero downtime)
kubectl rollout restart deploy/[service] -n [namespace]
kubectl rollout status deploy/[service] -n [namespace]
```

### Emergency Procedures
[Step-by-step for each emergency scenario — database unavailable, memory leak, traffic spike]

## 6. Capacity Scaling
[When and how to scale — triggers, commands, limits]

## 7. Dependency Failure Handling
| Dependency | If Unavailable | Fallback Behavior | Recovery Steps |
|---|---|---|---|
| [PostgreSQL] | [service rejects writes, reads from cache] | [Cache TTL = 5min] | [Check DB health, restore from backup if needed] |

## 8. Rollback Procedure
See `deployment/rollback-strategy.md` for full procedure.
Quick reference: `kubectl rollout undo deploy/[service] -n [namespace]`

## 9. Common Commands Reference
```bash
# View recent logs (last 30 min)
kubectl logs -n [ns] -l app=[service] --since=30m

# Check pod health
kubectl get pods -n [ns] -l app=[service]

# Describe a failing pod
kubectl describe pod [pod-name] -n [ns]

# View recent events
kubectl get events -n [ns] --sort-by='.lastTimestamp' | tail -20
```

## 10. Contacts
| Role | Contact | When to Escalate |
|---|---|---|
| On-call Lead | [contact] | Immediately for P1 |
| Service Owner | [contact] | P1 lasting > 15 min |
| Database Team | [contact] | Any DB issue |
| Security Team | [contact] | Any suspected breach |
```
