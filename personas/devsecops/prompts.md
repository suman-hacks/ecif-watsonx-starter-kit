# DevSecOps — FORGE Prompt Collection

---

## Prompt 1: Threat Model Generation

See full prompt in `sdlc/07-security/P1-threat-modeling.md`

Quick-start version:
```text
You are a senior security architect. Conduct a STRIDE threat model for:

System: [NAME]
Architecture summary: [PASTE]
Sensitive data types: [list — PII, PAN, PHI, credentials]
Trust boundaries: [external internet → DMZ → internal → database]
Compliance: [PCI-DSS / HIPAA / GDPR / SOX]

For each STRIDE category (Spoofing, Tampering, Repudiation, Information Disclosure, DoS, Elevation of Privilege):
- List threats specific to this system
- Rate each: Likelihood (H/M/L) × Impact (H/M/L) = Risk (Critical/High/Medium/Low)
- Provide specific mitigation for each High/Critical risk
- Generate security user stories for mitigations that require new features

Output:
1. Threat table by STRIDE category
2. Top 5 critical risks (blockers)
3. Security user stories (additional requirements)
4. Compliance mapping (each threat to applicable standard requirement)
```

---

## Prompt 2: CI/CD Pipeline Security Design

```text
You are a DevSecOps engineer designing a secure CI/CD pipeline.

CONTEXT
Language/framework: [e.g., Java / Spring Boot]
Build tool: [Maven / Gradle / npm]
Container: [Docker / Podman]
Deployment target: [Kubernetes / ECS / App Service]
Source control: [GitHub / GitLab / Azure DevOps / Bitbucket]
Compliance: [PCI-DSS / SOC2 / HIPAA]

Design a pipeline with these security gates (specify tool for each):

## Stage 1: Commit
- [ ] Secret scanning (prevents credentials entering repo)
  Tool: [GitGuardian / truffleHog / GitHub secret scanning]
  Config: [fail on any secret pattern match — no warnings, only fail]

## Stage 2: Build
- [ ] Dependency vulnerability scan (known CVEs)
  Tool: [OWASP Dependency Check / Snyk / Dependabot]
  Policy: [fail on CVSS >= 7.0 (High); warn on CVSS 4-7]
- [ ] SAST — Static Application Security Testing
  Tool: [SonarQube / Semgrep / Checkmarx]
  Policy: [fail on Critical; fail on > N High findings]
- [ ] License compliance scan
  Tool: [FOSSA / WhiteSource]
  Policy: [fail on GPL in proprietary code]

## Stage 3: Test
- [ ] DAST — Dynamic Application Security Testing (against deployed test env)
  Tool: [OWASP ZAP / Burp Suite Enterprise]
  Policy: [fail on Critical, High findings]

## Stage 4: Container
- [ ] Container image vulnerability scan
  Tool: [Trivy / Snyk Container / AWS ECR scanning]
  Policy: [fail on Critical CVEs in base image]
- [ ] Container security policy (Dockerfile best practices)
  Check: [non-root user, no SUID binaries, read-only filesystem]

## Stage 5: IaC
- [ ] Infrastructure as Code security scan
  Tool: [Checkov / tfsec / Terrascan]
  Policy: [fail on Critical misconfigurations]

## Stage 6: Pre-production gate
- [ ] Manual security approval required for:
  - First deployment to production
  - Changes to authentication/authorization code
  - Changes to PCI-DSS or HIPAA scope
  Approver: [Security team role]

Generate:
1. Pipeline YAML (GitHub Actions / GitLab CI / Azure Pipelines — specify which)
2. Security gate configuration for each tool
3. Approval workflow configuration
4. Exception process (how to override a security gate with documented approval)
```

---

## Prompt 3: Security Code Review

```text
You are a security engineer conducting an application security code review focused on OWASP Top 10.

CONTEXT
Application type: [REST API / Web application / Batch / Mobile backend]
Language/framework: [e.g., Java 21 / Spring Boot 3.3]
Handles: [PII / PAN / PHI / financial data — list what applies]
Authentication: [OAuth2 / JWT / mTLS / session]

CODE UNDER REVIEW
[PASTE CODE]

REVIEW FOR OWASP TOP 10 (2021)

For each finding use:
[SEVERITY] [CWE-NNN] `file:line` — [Vulnerability Name]
> Description: [what the vulnerability is]
> Attack scenario: [how an attacker could exploit this]  
> Fix: [specific corrected code]

A01 — Broken Access Control
- Missing authorization checks?
- IDOR (insecure direct object references)?
- Forced browsing possibilities?

A02 — Cryptographic Failures
- Sensitive data transmitted without encryption?
- Weak cipher suites?
- Keys hardcoded or stored insecurely?

A03 — Injection
- SQL injection via string concatenation?
- NoSQL injection?
- OS command injection?
- LDAP injection?

A04 — Insecure Design
- Missing rate limiting?
- Missing input validation?
- Business logic flaws?

A05 — Security Misconfiguration
- Default credentials or accounts?
- Unnecessary features enabled?
- Missing security headers?
- Verbose error messages exposing internals?

A06 — Vulnerable and Outdated Components
- Known CVEs in dependencies?
- Outdated framework versions?

A07 — Authentication Failures
- Weak password policy?
- Missing MFA for sensitive operations?
- Insecure token storage?
- Missing account lockout?

A08 — Integrity Failures
- Missing input validation?
- Insecure deserialization?
- Unverified software update mechanisms?

A09 — Security Logging Failures
- Missing security event logging?
- Logging sensitive data (PAN, passwords)?
- Insufficient audit trail for privileged operations?

A10 — SSRF
- Server-side request forgery vectors?
- Unvalidated URL inputs?

Output:
1. Critical findings (must fix before production)
2. High findings (fix before production, exceptions require CISO approval)
3. Medium findings (fix within 30 days)
4. Corrected code snippets for all Critical and High findings
```

---

## Prompt 4: Compliance Gap Assessment

```text
You are a compliance engineer assessing a system against a security standard.

STANDARD: [PCI-DSS v4.0 / HIPAA / GDPR / SOC 2 Type II / SOX]
SYSTEM: [NAME AND DESCRIPTION]
ARCHITECTURE: [PASTE SUMMARY]
CURRENT CONTROLS: [PASTE WHAT IS IMPLEMENTED — or "to be assessed from architecture"]

For EACH applicable requirement in the standard:

| Req # | Requirement | Current State | Gap | Remediation | Priority | Effort |
|---|---|---|---|---|---|---|
| [e.g., PCI 3.4] | [Render PAN unreadable] | [Masking implemented for logs; not for DB] | [DB stores full PAN] | [Implement tokenization] | [Critical] | [Large] |

Prioritize gaps as:
- Critical: violation creates legal liability or enables breach
- High: significant compliance risk, must remediate within 90 days
- Medium: remediate within 6 months
- Low: best practice, remediate in next cycle

Also produce:
1. Executive summary (2 paragraphs: what is in place, what is missing)
2. Remediation roadmap (phased, by priority)
3. What compensating controls could cover critical gaps temporarily
4. Evidence required for audit (what artifacts must be produced)
```
