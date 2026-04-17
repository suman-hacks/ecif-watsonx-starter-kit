# P2: Security Code Review

## When to Use This Prompt
During Stage 07 (Security), before any deployment to staging or production. Use this alongside the general code review (Stage 05 P2) for security-focused deep review. Mandatory for any service that handles PII, financial data, authentication, or external-facing APIs.

---

## Prompt

```text
You are a FORGE DevSecOps engineer performing a security-focused code review. Apply OWASP Top 10 (2021) and the security rules from the FORGE constitution.

INPUTS
Code to review: [Paste code or PR diff]
Service description: [What this service does, its security perimeter]
Data classification: [PCI / PII / public / internal — what data this service handles]
Authentication mechanism: [e.g., OAuth 2.0 JWT / API key / mutual TLS]
Known threat model findings: [Paste relevant STRIDE threats from P1-threat-modeling.md, or "Not yet produced"]

TASK
Perform a complete security code review covering:
1. OWASP Top 10 (2021) — every category
2. Credential and secret management
3. Input validation and sanitization
4. Authentication and authorization enforcement
5. Cryptographic practices
6. Sensitive data handling and logging
7. Dependency and supply chain risks
8. Error handling (no information leakage)
9. Business logic security (authorization bypass, privilege escalation)
10. Infrastructure and configuration security

---

# Security Code Review: [SERVICE-NAME]
Reviewed by: [AI tool]  Date: [date]
Data classification: [PCI / PII / Internal / Public]
Reviewer note: AI security review is a supplement to, not a replacement for, human security engineer review.

## Review Decision
**APPROVED** / **APPROVED WITH MINOR FINDINGS** / **SECURITY HOLD — CHANGES REQUIRED**

> [Summary]

---

## OWASP Top 10 (2021) Assessment

### A01:2021 — Broken Access Control
**Status:** PASS / FAIL / REVIEW NEEDED

Checks performed:
- [ ] All API endpoints require authentication (except explicitly public ones)
- [ ] Authorization checked at service layer, not just API layer
- [ ] User can only access their own data (no IDOR — insecure direct object reference)
- [ ] Admin functions inaccessible to regular users
- [ ] Directory traversal not possible in file operations

**Finding [if any]:**
```
File: [path:line]
Severity: BLOCKER / MAJOR
Issue: [description]
Fix: [specific instruction]
```

---

### A02:2021 — Cryptographic Failures
**Status:** PASS / FAIL / REVIEW NEEDED

Checks performed:
- [ ] Sensitive data encrypted at rest (TDE, column encryption)
- [ ] Sensitive data encrypted in transit (TLS 1.2+ enforced)
- [ ] No sensitive data transmitted in URLs (query params visible in logs)
- [ ] Passwords hashed with bcrypt/scrypt/Argon2 — not MD5/SHA1
- [ ] Cryptographic keys not hardcoded or in source code
- [ ] No use of weak algorithms (DES, RC4, MD5)

**Finding [if any]:** [per format above]

---

### A03:2021 — Injection
**Status:** PASS / FAIL / REVIEW NEEDED

Checks performed:
- [ ] All SQL queries use parameterized queries or ORM — no string concatenation
- [ ] All shell commands use arrays, not string interpolation — no command injection
- [ ] All XML parsing uses secure parsers (XXE disabled)
- [ ] All LDAP queries parameterized
- [ ] All NoSQL queries use driver-native query builders

**Finding [if any]:** [per format above]

**Code patterns searched for:**

SQL injection risks:
```java
// DANGEROUS — flag as BLOCKER
String query = "SELECT * FROM card WHERE id = '" + cardId + "'";
jdbcTemplate.query(query, ...);

// SAFE
jdbcTemplate.query("SELECT * FROM card WHERE id = ?", cardId);
// OR: JPA Repository — parameterized by default
cardRepository.findById(cardId);
```

---

### A04:2021 — Insecure Design
**Status:** PASS / FAIL / REVIEW NEEDED

Checks performed:
- [ ] Threat model exists and architecture matches it
- [ ] No security control bypass in business logic (rate limiting, fraud check)
- [ ] Multi-step flows protected against step skipping
- [ ] Resource limits exist (max request size, pagination limits)

---

### A05:2021 — Security Misconfiguration
**Status:** PASS / FAIL / REVIEW NEEDED

Checks performed:
- [ ] No default credentials anywhere in configuration
- [ ] Error responses do not expose stack traces or internal structure
- [ ] CORS policy is restrictive (not `*`)
- [ ] HTTP security headers present (HSTS, X-Frame-Options, CSP)
- [ ] Debug mode / actuator endpoints not exposed in production
- [ ] Spring Boot actuator secured (not open to unauthenticated access)
- [ ] No unnecessary features, ports, or services enabled

**Spring Boot actuator check:**
```yaml
# DANGEROUS — exposes all endpoints publicly
management.endpoints.web.exposure.include=*

# SAFE — restrict to health and info only
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=when-authorized
```

---

### A06:2021 — Vulnerable and Outdated Components
**Status:** PASS / FAIL / NOT CHECKED

Checks:
- [ ] Dependency vulnerability scan run (Snyk / OWASP Dependency-Check / Trivy)
- [ ] No CRITICAL CVEs in production dependencies
- [ ] No HIGH CVEs without documented exception
- [ ] Base Docker image is not end-of-life

**Findings from dependency scan:** [Paste scan output or "Run: snyk test / mvn dependency-check:check"]

---

### A07:2021 — Identification and Authentication Failures
**Status:** PASS / FAIL / REVIEW NEEDED

Checks:
- [ ] JWT validation: signature verified, expiry checked, issuer checked
- [ ] No JWT algorithm confusion (reject `alg: none`)
- [ ] Session tokens not in URLs
- [ ] API keys not logged
- [ ] No hardcoded credentials in code or config files

**JWT validation check:**
```java
// DANGEROUS — only decodes, does not verify signature
Jwts.parserBuilder().build().parseClaimsJwt(token);

// SAFE — verifies signature with public key
Jwts.parserBuilder()
    .setSigningKey(publicKey)
    .requireIssuer("https://auth.company.com")
    .build()
    .parseClaimsJws(token);
```

---

### A08:2021 — Software and Data Integrity Failures
**Status:** PASS / FAIL / REVIEW NEEDED

Checks:
- [ ] No unsafe deserialization of untrusted data
- [ ] Dependencies pulled from trusted repositories with checksums
- [ ] CI/CD pipeline integrity (signed commits, protected branches)
- [ ] No auto-update from untrusted sources

---

### A09:2021 — Security Logging and Monitoring Failures
**Status:** PASS / FAIL / REVIEW NEEDED

Checks:
- [ ] Authentication events logged (success and failure)
- [ ] Authorization failures logged
- [ ] Input validation failures logged
- [ ] No PII in any log statement
- [ ] No credentials or tokens in log statements
- [ ] Logs ship to centralized SIEM (configuration check)
- [ ] Alerting exists for high-frequency failures

**PII in logs check — patterns to search for:**
```java
// DANGEROUS
log.info("Processing card for customer: name={}, ssn={}, cardNumber={}", name, ssn, cardNumber);

// SAFE
log.info("Processing authorization: cardId={}, masked={}", cardId, maskCard(cardNumber));
// where maskCard returns "XXXX-XXXX-XXXX-1234"
```

---

### A10:2021 — Server-Side Request Forgery (SSRF)
**Status:** PASS / FAIL / N/A

Checks:
- [ ] No user-controlled values used in HTTP client URLs
- [ ] HTTP clients have allowlist of permitted target hosts
- [ ] Internal metadata endpoints not reachable (AWS: 169.254.169.254)

---

## Additional Security Findings

### Credential and Secret Management
- [ ] No credentials in `application.properties` / `application.yml`
- [ ] No credentials in environment variables checked into source
- [ ] Secrets sourced from vault (HashiCorp Vault / AWS Secrets Manager / Azure Key Vault)
- [ ] Secret rotation supported (service picks up new secret without restart)

### PCI-DSS Specific (if applicable)
- [ ] PAN (card number) never stored unencrypted
- [ ] PAN masked in all logs and API responses (last 4 digits only)
- [ ] PAN not present in URLs
- [ ] Cardholder data not transmitted over unencrypted channels
- [ ] Access to cardholder data logged and monitored

### Dependency Security
```bash
# Run these before review is complete:
mvn org.owasp:dependency-check-maven:check    # Java
pip install safety && safety check             # Python
npm audit --audit-level=high                   # Node.js
snyk test                                       # All (requires Snyk account)
```

---

## Security Findings Summary

| ID | OWASP Category | Severity | File | Description | Required Fix |
|---|---|---|---|---|---|
| SEC-001 | A03 Injection | BLOCKER | [file:line] | SQL concatenation | Use parameterized query |
| SEC-002 | A09 Logging | MAJOR | [file:line] | PAN in log statement | Mask to last 4 digits |

**Total BLOCKERs:** [N]  **Total MAJORs:** [N]  **Total MINORs:** [N]
**Security clearance:** HOLD / CONDITIONAL / CLEARED

---

## Security Review Notes

This AI security review checks for common patterns. It does NOT replace:
- Manual penetration testing for production systems
- Human security engineer review for PCI/HIPAA/SOX-regulated services
- Dynamic analysis (DAST) with tools like OWASP ZAP
- Static analysis with purpose-built SAST tools (Semgrep, Checkmarx, Veracode)
```
