# FORGE Review Gates

Review gates are checkpoints where human approval is required before the next stage begins.

## Gate 0: Pre-Engagement Complete
**Trigger:** Pre-engagement analysis (T1-T5) produced  
**Reviewers:** Lead Engineer, Architect  
**Check:**
- [ ] Architecture diagram reviewed for accuracy and gaps annotated
- [ ] Decision logic inventory reviewed — major decision locations confirmed
- [ ] Integration map reviewed — no critical dependencies missed
- [ ] POC options reviewed — grounded in analysis findings
- [ ] Sensitive content reviewed — redacted before sharing externally  
**Output artifact:** Reviewed and annotated analysis files  
**Pass:** All outputs reviewed, annotations added where AI was wrong/incomplete

---

## Gate 1: Discovery Complete
**Trigger:** Stage 01 (Discovery) outputs produced  
**Reviewers:** Lead Engineer, Business Analyst, Product Owner  
**Check:**
- [ ] System inventory is complete (no major components missing)
- [ ] Data flow is accurate
- [ ] Integration catalog is complete
- [ ] Pain points reflect actual business problems (not just technical preferences)
- [ ] All unknowns are captured and assigned  
**Output artifact:** Signed-off current-state assessment  
**Pass:** PO and Lead Engineer both sign off

---

## Gate 2: Requirements Complete
**Trigger:** Stage 02 (Requirements) outputs produced  
**Reviewers:** Product Owner (final sign-off), Business Analyst, QA Engineer (testability check)  
**Check:**
- [ ] All business capabilities have user stories
- [ ] All acceptance criteria are testable
- [ ] NFRs have measurable thresholds
- [ ] Compliance requirements explicitly called out
- [ ] Requirements traceability matrix completed  
**Output artifact:** Approved user story set with acceptance criteria  
**Pass:** PO signs off; QA confirms testability

---

## Gate 3: Architecture Complete
**Trigger:** Stage 03 (Architecture) outputs produced  
**Reviewers:** Senior Architect (if available), Lead Engineer, Security Engineer, PO (scope check)  
**Check:**
- [ ] All ADRs reviewed and status = Accepted
- [ ] Service boundaries validated — no shared databases
- [ ] API contracts reviewed by at least one consumer team
- [ ] Threat model produced and reviewed
- [ ] NFRs achievable with proposed architecture  
**Output artifact:** Approved architecture package (ADRs + service map + API contracts)  
**Pass:** Architect and Security both sign off

---

## Gate 4: Business Rules Approved (Modernization Projects)
**Trigger:** Business rule extraction complete  
**Reviewers:** Subject Matter Expert (SME), Business Analyst, Lead Engineer  
**Check:**
- [ ] All rules have source citations (no invented rules)
- [ ] All High/Critical confidence rules reviewed by SME
- [ ] All Low confidence rules either validated or escalated
- [ ] Open questions resolved or explicitly accepted as assumptions  
**Output artifact:** Approved business rules register  
**Pass:** SME signs off; Lead Engineer confirms completeness

---

## Gate 5: Code Review Complete
**Trigger:** Implementation submitted (PR)  
**Reviewers:** Lead Engineer (mandatory), Peer Developer (mandatory), Security Engineer (when applicable)  
**Check:**
- [ ] All BLOCKER findings resolved
- [ ] Test coverage meets targets (90% branch on business logic)
- [ ] All acceptance criteria have corresponding tests
- [ ] Security review passed (no OWASP violations)
- [ ] Observability implemented  
**Output artifact:** Approved PR merge  
**Pass:** Lead Engineer approval + no open BLOCKER/MAJOR findings

---

## Gate 6: Testing Complete
**Trigger:** Stage 06 (Testing) complete  
**Reviewers:** QA Lead, Product Owner (UAT sign-off)  
**Check:**
- [ ] All Critical and High test cases passed
- [ ] Performance NFRs validated
- [ ] UAT sign-off from business stakeholders
- [ ] Regression suite passing  
**Output artifact:** Test completion report + UAT sign-off  
**Pass:** QA Lead + PO both sign off

---

## Gate 7: Security Sign-Off
**Trigger:** Stage 07 (Security) complete  
**Reviewers:** Security Engineer / DevSecOps Lead  
**Check:**
- [ ] Threat model reviewed and accepted
- [ ] All CRITICAL security findings resolved
- [ ] Compliance gaps either resolved or accepted with documented exception  
**Output artifact:** Security acceptance  
**Pass:** Security Lead signs off

---

## Gate 8: Production Deployment Approval
**Trigger:** Ready to deploy to production  
**Reviewers:** Lead Engineer, DevSecOps, Product Owner, Change Manager (if applicable)  
**Check:**
- [ ] All earlier gates complete
- [ ] Deployment plan reviewed
- [ ] Rollback plan tested in staging
- [ ] On-call rota confirmed
- [ ] Monitoring dashboards ready  
**Output artifact:** Go/No-Go sign-off  
**Pass:** All three sign off
