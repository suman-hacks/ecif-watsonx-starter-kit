# P3 — Non-Functional Requirements Definition

**Stage:** 02 — Requirements  
**Persona:** Solution Architect, Lead Engineer, Product Owner  
**Output file:** `requirements/nfr-register.md`

---

## The Prompt

```text
You are a solution architect defining non-functional requirements for a software system.

CONTEXT
System: [NAME AND PURPOSE]
Business criticality: [Critical/High/Medium/Low — e.g., "Critical: real-time payment processing"]
User base: [volume, geography, user types]
Compliance requirements: [PCI-DSS/HIPAA/GDPR/SOX/OFAC — list applicable]
Existing system SLAs (if modernizing): [paste if known]

TASK
Produce a complete Non-Functional Requirements Register covering all categories below.
For EACH requirement, provide:

| ID | Category | Requirement | Acceptance Criteria | Priority | Source |
|---|---|---|---|---|---|
| NFR-[NNN] | [category] | [requirement statement] | [measurable acceptance criterion] | [Must/Should/Could] | [business driver] |

CATEGORIES TO COVER

**Performance**
- Response time: p50, p95, p99 latency targets per operation type
- Throughput: transactions per second at peak load
- Batch processing: throughput for batch operations
- Database query performance: max acceptable query time

**Reliability & Availability**
- Uptime SLA: expressed as % (e.g., 99.95% = ~4.4 hours downtime/year)
- Recovery Time Objective (RTO): max time to restore service after failure
- Recovery Point Objective (RPO): max acceptable data loss window
- Mean Time to Recovery (MTTR) target

**Scalability**
- Horizontal scaling: must support N→M instances without downtime
- Load growth: must handle X% year-over-year growth without re-architecture
- Peak load handling: must sustain Nx normal load for Y minutes

**Security**
- Authentication: mechanism required (OAuth 2.0, mTLS, SAML)
- Authorization: model required (RBAC, ABAC, scope-based)
- Data encryption: at rest and in transit requirements
- Secrets management: no hardcoded credentials, approved secrets store
- Audit logging: what events must be logged, retention period
- Penetration testing: frequency and scope

**Compliance**
[For each applicable standard, list specific requirements]
- PCI-DSS: card data handling, network segmentation, access controls, logging
- GDPR: data minimization, right to erasure, consent management, DPA requirements
- SOX: audit trails, access controls, change management
- HIPAA: PHI handling, BAA requirements, minimum necessary access

**Observability**
- Logging: format (structured JSON), required fields, retention period
- Metrics: RED metrics per service, business metrics required
- Tracing: distributed tracing coverage, sampling rate
- Alerting: SLO breach alerting, escalation path

**Maintainability**
- Test coverage: minimum % unit/integration/e2e
- Code quality: complexity thresholds, duplication limits
- Documentation: what must be documented (APIs, runbooks, ADRs)
- Deployment frequency: target (daily/weekly) and deployment time target

**Operational**
- Deployment: zero-downtime deployment required?
- Feature flags: canary release / progressive rollout required?
- Rollback: time to roll back a bad deployment
- On-call: paging criteria, escalation path

OUTPUT FORMAT
1. NFR Register table (all requirements)
2. Architecture implications: which NFRs have the highest design impact
3. Gaps: requirements that couldn't be determined from the input
4. Conflicts: any NFRs that conflict with each other (e.g., strict latency + full encryption)

Confidence: [High/Medium/Low] | Basis: [sources]
```
