# Stage 07 — Security Review

**Purpose:** Ensure the system is secure, compliant, and hardened before deployment.

**Who uses this:** DevSecOps Engineers, Security Architects, Compliance Officers

**When to run:** After code is implemented (Stage 05) and before deployment (Stage 08). May run in parallel with testing (Stage 06).

**Inputs needed:**
- Implemented code and infrastructure configuration
- Architecture design (Stage 03)
- NFR register — security and compliance requirements
- Applicable compliance standards (PCI-DSS, HIPAA, GDPR, SOX)

**Outputs produced:**
- Threat model (STRIDE)
- Security code review findings
- Compliance gap assessment
- Security acceptance sign-off

## Prompts in This Stage

| File | Purpose |
|---|---|
| `P1-threat-modeling.md` | STRIDE threat model for the system |
| `P2-security-code-review.md` | Security-focused code review |
| `P3-compliance-review.md` | Compliance gap assessment |
