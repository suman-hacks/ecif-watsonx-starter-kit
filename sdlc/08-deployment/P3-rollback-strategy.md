# P3: Rollback Strategy

## When to Use This Prompt
During Stage 08 (Deployment), before any production deployment. The rollback strategy must be reviewed and the rollback procedure must be tested in staging before Gate 8 sign-off.

---

## Prompt

```text
You are a FORGE DevSecOps engineer producing a rollback strategy for a production deployment.

INPUTS
Services being deployed: [List from Agent 04 service map]
Deployment plan: [Paste P1 deployment plan — especially migration phases]
Database changes: [Describe schema changes — additive only? destructive? data migration?]
Event changes: [New event types? Changed event schemas? Consumer changes?]
Infrastructure changes: [New resources? Modified config?]
Rollback time objective: [e.g., < 15 minutes for full rollback]
Current production version: [Version tag or description]
Target deployment version: [Version tag or description]

TASK
Produce the complete rollback strategy and playbook.

---

# Rollback Strategy: [SERVICE-NAME] v[VERSION]
Produced by: [AI tool]  Date: [date]
Rollback Time Objective (RTO): [target]
MUST BE TESTED IN STAGING before Gate 8 sign-off.

---

## Rollback Decision Tree

```
Production issue detected
│
├── Severity: Minor (degraded performance, non-critical feature)
│   └── Action: Monitor → alert escalation threshold → decide within 30 min
│
├── Severity: Major (feature broken, elevated error rate)
│   ├── Duration < 15 min: Attempt fix-forward (hotfix)
│   └── Duration > 15 min: INITIATE ROLLBACK → follow this playbook
│
└── Severity: Critical (data corruption, security breach, complete outage)
    └── IMMEDIATE ROLLBACK — no wait, no debate
```

## Rollback Triggers (automatic escalation criteria)

| Metric | Threshold | Window | Trigger Action |
|---|---|---|---|
| Error rate | > 5% | 5 minutes | PagerDuty alert → on-call decision |
| Error rate | > 20% | 2 minutes | Automatic rollback initiation |
| p99 latency | > 2x baseline | 5 minutes | PagerDuty alert → on-call decision |
| 5xx responses | Any from critical endpoints | 1 minute | Immediate escalation |
| Data integrity check | Any discrepancy > 0.01% | Continuous | IMMEDIATE ROLLBACK |

---

## Rollback Components

### Component 1: Application Rollback

**Mechanism:** Kubernetes rolling update to previous image tag

**Time to rollback:** ~3-5 minutes

**Steps:**
```bash
# Step 1: Identify the previous stable image tag
kubectl get deployment authorization-service -n production -o jsonpath='{.spec.template.spec.containers[0].image}'
# Note the current tag, then identify the previous:
helm history authorization-service -n production

# Step 2: Roll back Helm release to previous revision
helm rollback authorization-service [REVISION-NUMBER] -n production --wait --timeout 10m

# Step 3: Verify rollback
kubectl get pods -n production -l app=authorization-service
kubectl rollout status deployment/authorization-service -n production

# Step 4: Confirm health
curl -f https://[service-url]/actuator/health
```

**Verify rollback success:**
- All pods Running (not Pending/CrashLoopBackOff)
- Health endpoint returns UP
- Error rate returns to baseline (< 1%)
- Confirm image tag matches previous stable version

---

### Component 2: Database Rollback

**This is the most complex and highest-risk component.**

#### Scenario A: Additive changes only (new columns, new tables)
**Risk:** Low — additive changes are backwards compatible  
**Rollback:** Application rollback only — old code ignores new columns  
**Database action:** Optional cleanup of new columns after application is stable

#### Scenario B: Non-destructive changes (renamed or modified columns)
**Risk:** Medium  
**Rollback mechanism:** Expand-contract pattern

During this deployment, the expand phase ran:
- Old column: `[old_column_name]` — still present, still populated
- New column: `[new_column_name]` — added, being populated

**Rollback steps:**
```sql
-- Step 1: Verify old column still exists and has data
SELECT COUNT(*) FROM [table] WHERE [old_column_name] IS NOT NULL;

-- Step 2: Roll back application (Component 1)
-- Old code will use old column — data is intact

-- Step 3 (later, after investigation): Drop new column if rollback is permanent
-- ALTER TABLE [table] DROP COLUMN [new_column_name];  -- ONLY after stable
```

#### Scenario C: Data migration ran
**Risk:** High — migrated data may need reversal  
**Rollback mechanism:** Reconciliation rollback

Pre-deployment requirement: reconciliation snapshot was taken
```sql
-- The pre-migration snapshot table was created:
-- CREATE TABLE [table]_pre_migration_snapshot AS SELECT * FROM [table];

-- Rollback data migration:
BEGIN;
-- Restore from snapshot (only rows modified by migration)
UPDATE [table] t
SET [fields] = (SELECT [fields] FROM [table]_pre_migration_snapshot s WHERE s.id = t.id)
WHERE t.migration_batch_id = '[THIS-BATCH-ID]';

-- Verify row counts match
SELECT COUNT(*) FROM [table];
SELECT COUNT(*) FROM [table]_pre_migration_snapshot;
COMMIT;

-- Drop the new schema added for this migration
DROP TABLE IF EXISTS [new_table_added];
ALTER TABLE [existing_table] DROP COLUMN IF EXISTS [new_column];
```

**STOP:** If data rollback affects more than [N] rows or takes more than [N] minutes, escalate to Lead Engineer + DBA before proceeding.

---

### Component 3: Event Schema Rollback

**If new event types were published:**
- Downstream consumers may have processed new events — they cannot be un-processed
- Action: Publish compensation events or coordinate with consumer teams

**If event schema changed:**
- Check whether change was additive (consumers ignore new fields — safe)
- If breaking change was deployed: notify all consumer teams immediately

**Consumer notification template:**
```
Subject: ROLLBACK IN PROGRESS — [Event Topic] schema change reverted

We are rolling back [version] which included changes to [event topic].
Event schema is reverting to v[X.Y].

Action required: If your consumer processed any events between [start time] and [now]:
- Review events consumed against the v[X.Y] schema
- Contact [team contact] if any reconciliation is needed

Timeline: Rollback complete by [time].
```

---

### Component 4: Infrastructure Rollback (if IaC was applied)

```bash
# Terraform: Roll back to previous state
terraform plan -target=[resource] -destroy  # Review before destroy
terraform apply -target=[resource]          # Apply targeted rollback

# For Kubernetes infrastructure changes:
git revert [commit-hash]  # Revert IaC commit
kubectl apply -f [previous-config]  # Apply previous config
```

**Note:** Infrastructure rollback is slower (15-30 minutes). Application rollback should complete first while infrastructure investigation proceeds.

---

## Rollback Playbook: Step-by-Step

### T+0: Rollback Decision Made

**Who:** On-call engineer (confirmed with Lead Engineer)  
**Action:**
1. Open war room: [Slack channel / Teams / Bridge line]
2. Announce: "Rollback of [service] v[version] initiated — ETA [time]"
3. Start rollback timer

### T+0 to T+5: Application Rollback

```bash
# Run from deployment workstation (credentials pre-loaded)
helm rollback [service-name] [previous-revision] -n production --wait
```

Announce in war room: "Application rollback complete — monitoring"

### T+5 to T+10: Verify and Monitor

Check:
- [ ] All pods healthy
- [ ] Error rate back to baseline
- [ ] Key business flows operational (run smoke test suite)
- [ ] Downstream consumers healthy

```bash
# Smoke test suite
./scripts/smoke-test.sh production
```

### T+10 to T+15: Database Assessment

- [ ] Data integrity check: run reconciliation query
- [ ] If data migration ran: assess rollback need (Scenario C above)
- [ ] DBA involved if database rollback required

### T+15+: Communication

**Internal:**
- Update incident channel with status
- Notify product owner and stakeholders

**External (if customer-facing impact):**
- Status page update
- Customer communication per runbook

---

## Post-Rollback Actions

- [ ] Keep production at rolled-back version until root cause is confirmed
- [ ] Do NOT attempt re-deployment until blocker is fixed and tested in staging
- [ ] Initiate blameless PIR (use `sdlc/09-operations/P2-incident-analysis.md`)
- [ ] Update runbook if rollback revealed any gap
- [ ] Document timeline and decision points for Gate 8 review

---

## Rollback Test Results (to be completed in staging)

| Step | Expected Outcome | Actual Outcome | Pass/Fail | Time Taken |
|---|---|---|---|---|
| Application rollback | All pods healthy, health=UP | | | |
| Error rate recovery | < 1% within 5 min | | | |
| Database rollback (if applicable) | Row counts match, data integrity check passes | | | |
| Smoke tests | All critical flows pass | | | |
| Total rollback time | < [RTO target] | | | |

**Staging test date:** [date]  
**Tested by:** [name]  
**Result:** PASS / FAIL — [notes]

Gate 8 prerequisite: Rollback test result = PASS
```
