# P1 — Threat Modeling (STRIDE)

**Stage:** 07 — Security  
**Persona:** DevSecOps Engineer, Security Architect  
**Output file:** `security/threat-model.md`

---

## The Prompt

```text
You are a senior security architect conducting a STRIDE threat model analysis.

CONTEXT
System: [NAME]
Architecture: [PASTE ARCHITECTURE SUMMARY OR DIAGRAM]
Deployment model: [on-prem / cloud / hybrid / mainframe + distributed]
Data handled: [types of sensitive data — PII, financial, health, etc.]
Compliance requirements: [PCI-DSS / HIPAA / GDPR / SOX / OFAC]
External integrations: [PASTE INTEGRATION LIST FROM STAGE 03]

TASK
Produce a comprehensive STRIDE threat model.

## 1. Asset Inventory
| Asset | Type | Sensitivity | Owner |
|---|---|---|---|
| [e.g., Customer PAN data] | Data | Critical | [service] |
| [e.g., Authorization decision engine] | Service | High | [team] |

## 2. Trust Boundaries
[Map where trust boundaries exist — e.g., external network → DMZ → internal network → database]

## 3. Data Flow Diagram (ASCII)
[Show data flows across trust boundaries — these are where threats concentrate]

## 4. STRIDE Threat Analysis

For each trust boundary crossing or sensitive asset, enumerate threats:

### S — Spoofing (claiming to be something you're not)
| Threat | Component | Likelihood [H/M/L] | Impact [H/M/L] | Risk Score | Mitigation | Status |
|---|---|---|---|---|---|---|
| [e.g., Attacker replays stolen auth token] | API Gateway | M | H | High | Short token TTL + refresh token rotation | Required |

### T — Tampering (modifying data in transit or at rest)
[Same table format]

### R — Repudiation (denying an action was taken)
[Same table format — focus on audit log completeness]

### I — Information Disclosure (exposing data to unauthorized parties)
[Same table format — focus on PII/PAN exposure, error messages, logs]

### D — Denial of Service (making system unavailable)
[Same table format — focus on rate limiting, resource exhaustion]

### E — Elevation of Privilege (gaining more access than authorized)
[Same table format — focus on broken access control, IDOR, IAM misconfig]

## 5. High-Priority Mitigations (BLOCKER — must implement before go-live)
| # | Threat | Required Mitigation | Owner | Due |
|---|---|---|---|---|

## 6. Security User Stories (additional requirements surfaced by threat model)
| ID | Story | Priority | Acceptance Criteria |
|---|---|---|---|
| SEC-001 | As a system, I must rate-limit auth attempts to prevent credential stuffing | Critical | Max 5 failed attempts per 15 min per IP; exponential backoff |

## 7. Compliance Mapping
| Requirement | Standard | Section | Threat Mitigated | Status |
|---|---|---|---|---|
| Encrypt cardholder data in transit | PCI-DSS | 4.2 | Information Disclosure | [Implemented/Partial/Gap] |

## 8. Residual Risk Register
| Risk | Accepted By | Rationale | Review Date |
|---|---|---|---|

Confidence: [High/Medium/Low] | Basis: [what artifacts reviewed]
```
