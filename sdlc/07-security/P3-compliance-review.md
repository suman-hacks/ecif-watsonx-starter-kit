# P3: Compliance Review

## When to Use This Prompt
During Stage 07 (Security) or as part of Gate 7 sign-off. Use to map implementation artifacts against specific regulatory compliance frameworks. Required before any system with regulatory scope goes to production.

---

## Prompt

```text
You are a FORGE DevSecOps engineer performing a compliance gap assessment.

INPUTS
Regulatory frameworks applicable: [PCI-DSS 4.0 / HIPAA / GDPR / SOX / ISO 27001 / NIST CSF — list all that apply]
System description: [What this system does, data it handles, who has access]
Architecture overview: [Paste service map and data flow from Agent 04]
Security controls implemented: [List security controls confirmed in implementation — authentication, encryption, logging, access control]
Known security findings: [Paste any open findings from P1 threat model or P2 security code review]

TASK
Assess compliance posture against each applicable framework. Identify gaps. Do not invent controls that have not been confirmed — label everything [CONFIRMED], [PARTIAL], or [GAP].

---

# Compliance Assessment: [SYSTEM NAME]
Assessed by: [AI tool]  Date: [date]
Frameworks: [list]
IMPORTANT: This AI assessment is a planning tool. Formal compliance requires human audit by qualified assessors.

## Executive Compliance Posture

| Framework | Overall Status | Critical Gaps | Next Audit Action |
|---|---|---|---|
| PCI-DSS 4.0 | [Green/Amber/Red] | [N] | [QSA engagement date] |
| GDPR | [Green/Amber/Red] | [N] | [DPO review date] |
| SOX | [Green/Amber/Red] | [N] | [Internal audit date] |

---

## PCI-DSS 4.0 Assessment (if applicable)

### Requirement 1: Install and Maintain Network Security Controls
| Sub-Requirement | Control | Status | Evidence | Gap/Action |
|---|---|---|---|---|
| 1.3: Restrict inbound/outbound traffic | Network segmentation, security groups | [CONFIRMED/GAP] | [WAF + security groups defined in IaC] | [If gap: action] |
| 1.4: Prohibit direct public access to CDE | DMZ or equivalent isolation | [CONFIRMED/GAP] | | |

### Requirement 2: Apply Secure Configurations
| Sub-Requirement | Control | Status | Evidence | Gap/Action |
|---|---|---|---|---|
| 2.2: System components configured securely | Hardening standards applied | [CONFIRMED/GAP] | | |
| 2.3: Wireless environments secured | N/A or [control] | [N/A/CONFIRMED/GAP] | | |

### Requirement 3: Protect Stored Account Data
| Sub-Requirement | Control | Status | Evidence | Gap/Action |
|---|---|---|---|---|
| 3.3: SAD not stored after authorization | Confirm no CVV/PIN/full track data stored | [CONFIRMED/GAP] | [Schema review — no SAD fields] | |
| 3.4: PAN rendered unreadable at rest | Encryption (TDE or column) + masking | [CONFIRMED/GAP] | | |
| 3.5: PAN secured with cryptography | Key management procedure | [CONFIRMED/GAP] | | |

### Requirement 4: Protect Cardholder Data in Transit
| Sub-Requirement | Control | Status | Evidence | Gap/Action |
|---|---|---|---|---|
| 4.2: Strong cryptography in transit | TLS 1.2+ enforced on all channels | [CONFIRMED/GAP] | | |

### Requirement 7: Restrict Access by Business Need
| Sub-Requirement | Control | Status | Evidence | Gap/Action |
|---|---|---|---|---|
| 7.2: Access control system | RBAC implemented | [CONFIRMED/GAP] | | |
| 7.3: Least privilege enforced | IAM policies reviewed | [CONFIRMED/GAP] | | |

### Requirement 8: Identify Users and Authenticate Access
| Sub-Requirement | Control | Status | Evidence | Gap/Action |
|---|---|---|---|---|
| 8.2: All users have unique IDs | No shared accounts | [CONFIRMED/GAP] | | |
| 8.3: Strong authentication | MFA for all CDE access | [CONFIRMED/GAP] | | |
| 8.4: MFA for all remote access | VPN + MFA | [CONFIRMED/GAP] | | |

### Requirement 10: Log and Monitor All Access
| Sub-Requirement | Control | Status | Evidence | Gap/Action |
|---|---|---|---|---|
| 10.2: Audit logs implemented | Structured logging to SIEM | [CONFIRMED/GAP] | | |
| 10.3: Audit logs protected | Logs tamper-evident / immutable | [CONFIRMED/GAP] | | |
| 10.5: Retain audit logs | 12-month retention, 3 months immediately available | [CONFIRMED/GAP] | | |
| 10.7: Failures of security controls detected | Alerting on log gaps / control failures | [CONFIRMED/GAP] | | |

### Requirement 11: Test Security Regularly
| Sub-Requirement | Control | Status | Evidence | Gap/Action |
|---|---|---|---|---|
| 11.3: External and internal pen tests | Annual penetration test | [CONFIRMED/GAP] | | |
| 11.4: Network/host intrusion detection | IDS/IPS in place | [CONFIRMED/GAP] | | |
| 11.6: Web-facing apps protected | WAF or change-and-tamper detection | [CONFIRMED/GAP] | | |

### Requirement 12: Support Information Security with Policies
| Sub-Requirement | Control | Status | Evidence | Gap/Action |
|---|---|---|---|---|
| 12.3: Risk assessments conducted | Annual risk assessment | [CONFIRMED/GAP] | | |
| 12.10: Incident response plan | IR plan tested | [CONFIRMED/GAP] | | |

---

## GDPR Assessment (if applicable)

| Article | Requirement | Status | Gap/Action |
|---|---|---|---|
| Art. 5 — Lawfulness | Lawful basis documented for all personal data processing | [CONFIRMED/GAP] | |
| Art. 6 — Consent | Consent records captured if consent is basis | [CONFIRMED/GAP] | |
| Art. 7 — Conditions for consent | Consent withdrawal mechanism exists | [CONFIRMED/GAP] | |
| Art. 13/14 — Transparency | Privacy notice updated for new processing | [CONFIRMED/GAP] | |
| Art. 17 — Right to erasure | Data deletion workflow implemented | [CONFIRMED/GAP] | |
| Art. 20 — Data portability | Export mechanism for personal data | [CONFIRMED/GAP] | |
| Art. 25 — Privacy by design | Data minimization, pseudonymization applied | [CONFIRMED/GAP] | |
| Art. 30 — Records of processing | ROPA (Record of Processing Activities) updated | [CONFIRMED/GAP] | |
| Art. 32 — Security of processing | Encryption, access control, integrity measures | [CONFIRMED/GAP] | |
| Art. 33 — Breach notification | 72-hour notification procedure documented | [CONFIRMED/GAP] | |
| Art. 35 — DPIA | Data Protection Impact Assessment completed if high-risk | [CONFIRMED/GAP] | |

---

## HIPAA Assessment (if applicable — US healthcare data)

| Safeguard | Requirement | Status | Gap/Action |
|---|---|---|---|
| Physical § 164.310 | Workstation use, device controls | [CONFIRMED/GAP] | |
| Technical § 164.312(a) | Unique user identification, automatic logoff | [CONFIRMED/GAP] | |
| Technical § 164.312(b) | Audit controls | [CONFIRMED/GAP] | |
| Technical § 164.312(c) | Integrity controls | [CONFIRMED/GAP] | |
| Technical § 164.312(e) | Transmission security | [CONFIRMED/GAP] | |
| Administrative § 164.308 | Security officer, workforce training, risk analysis | [CONFIRMED/GAP] | |
| Breach notification | 60-day notification to HHS; immediate to individual | [CONFIRMED/GAP] | |

---

## SOX IT General Controls (if applicable)

| Control Area | Requirement | Status | Gap/Action |
|---|---|---|---|
| Access Control | Segregation of duties for financial systems | [CONFIRMED/GAP] | |
| Change Management | All changes to financial systems via controlled process | [CONFIRMED/GAP] | |
| IT Operations | Backup, recovery, monitoring | [CONFIRMED/GAP] | |
| Program Development | SDLC controls, testing sign-off documented | [CONFIRMED/GAP] | |

---

## Gap Summary and Remediation Plan

| Gap ID | Framework | Requirement | Description | Risk if Not Fixed | Owner | Target Date | Status |
|---|---|---|---|---|---|---|---|
| GAP-001 | PCI-DSS | Req. 10.5 | Log retention < 12 months | Compliance failure, fine | DevSecOps | [date] | Open |
| GAP-002 | GDPR | Art. 17 | No data deletion workflow for EU customers | GDPR violation | Lead Engineer | [date] | Open |

---

## Compliance Tracking

Gaps with status OPEN must be resolved before:
- **PCI-DSS:** Any QSA audit; any new PCI-scoped service to production
- **GDPR:** Any processing of EU personal data at scale
- **HIPAA:** Any ePHI (electronic Protected Health Information) goes live
- **SOX:** Any change to financial reporting systems in scope

**Escalation:** All CRITICAL gaps (those that create immediate regulatory violation risk) must be escalated to CISO and Legal within 24 hours of identification.
```
