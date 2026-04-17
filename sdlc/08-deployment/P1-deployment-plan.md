# P1 — Deployment Plan Generation

**Stage:** 08 — Deployment  
**Persona:** DevSecOps, Platform Engineer  
**Output file:** `deployment/deployment-plan-[version].md`

---

## The Prompt

```text
You are a senior DevSecOps engineer creating a production deployment plan.

CONTEXT
System/service being deployed: [NAME AND VERSION]
Deployment type: [New service / Feature release / Breaking change / Database migration]
Rollout strategy: [Blue-Green / Canary / Rolling / Feature flag]
Current state: [what's in production now — version, users, load]
Target state: [what will be in production after]
Deployment window: [maintenance window if required, or "continuous deployment"]
Approval required from: [list approvers]

DEPENDENCIES
- Database migrations: [Yes/No — describe if yes]
- External service changes: [Yes/No — describe if yes]
- Configuration changes: [Yes/No — describe if yes]
- Feature flags: [Yes/No — which flags, initial state]

TASK
Produce a complete deployment plan:

## Pre-Deployment Checklist
- [ ] All stage gates complete (testing, security sign-off)
- [ ] Deployment artifacts built and signed: [image digest/checksum]
- [ ] Feature flags configured: [list with initial states]
- [ ] Monitoring dashboards ready and baseline captured
- [ ] On-call engineer confirmed for deployment window
- [ ] Rollback procedure reviewed and tested in staging
- [ ] Stakeholder communication sent
- [ ] [Database migration dry-run completed on staging — if applicable]

## Deployment Sequence

### Phase 1: Pre-Deployment Validation
[Steps with expected time and validation criteria]

### Phase 2: Database Migration (if applicable)
[Execution steps — migration should be backward-compatible with current code]
[Validation: row counts, sample data checks]
[Duration estimate]

### Phase 3: Application Deployment
[Rolling/blue-green/canary steps]
[Health check endpoints to verify]
[Traffic shifting percentage if canary]

### Phase 4: Smoke Testing
[Automated smoke tests to run]
[Manual validation steps]
[Metrics to verify (error rate, latency)]

### Phase 5: Go/No-Go Decision
- Error rate: [acceptable threshold — e.g., < 0.1%]
- P99 latency: [acceptable — e.g., < 200ms]
- Business metric: [e.g., authorization approval rate within 2% of baseline]

**If Go:** [proceed to Phase 6]
**If No-Go:** [execute rollback procedure — see rollback-strategy.md]

### Phase 6: Monitoring Period
[How long to watch before declaring success]
[What metrics to monitor]
[Escalation path if issues appear]

### Phase 7: Cleanup (post-success)
[Remove old version, clean up feature flags, update documentation]

## Communication Plan
| When | To | Message |
|---|---|---|
| -24h | Engineering team | Deployment reminder |
| -1h | On-call + stakeholders | Deployment starting soon |
| Start | Status page | Deployment in progress |
| Complete | All stakeholders | Deployment complete, monitoring |
| +2h | Management | Deployment confirmed stable |

## Go-Live Authorization
| Sign-off | Role | Name | Time |
|---|---|---|---|
| Technical approval | Lead Engineer | | |
| Security approval | DevSecOps | | |
| Business approval | Product Owner | | |
```
