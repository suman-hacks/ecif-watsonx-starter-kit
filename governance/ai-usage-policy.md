# AI Usage Policy — FORGE Framework

> This policy governs how engineers in this organisation use AI coding assistants when working on software projects under the FORGE framework. It covers: what data you may and may not share with AI tools, how to disclose AI-assisted work, IP and licensing considerations, audit requirements, and tool-specific data handling.
>
> **Who this applies to:** All engineers, architects, BAs, QA, and DevSecOps staff using any AI coding assistant (Claude Code, GitHub Copilot, JetBrains AI, IBM watsonx, Cursor, or any other AI tool) on company projects.

---

## 1. Acceptable Use

AI coding assistants are approved for the following activities:

| Activity | Approved? | Notes |
|---|---|---|
| Code generation from specifications | Yes | Must be reviewed before merge — see Section 4 |
| Legacy code analysis (COBOL, Java, SQL) | Yes | Do not include data with real customer records |
| Writing and running tests | Yes | Test data must be synthetic — see Section 3 |
| Generating architecture documentation and ADRs | Yes | Must be reviewed by Tech Lead |
| Generating runbooks and operational documentation | Yes | Must be reviewed by Lead Engineer |
| Explaining legacy code in plain English | Yes | Do not include live production data |
| Writing Jira stories and acceptance criteria | Yes | Do not include real customer names or account numbers |
| Generating Terraform / Helm / pipeline configuration | Yes | Must be reviewed by DevSecOps before merge |
| Generating SQL migrations | Yes | Must be reviewed by DBA or Lead Engineer |
| Submitting code directly to main/master without review | **No** | AI-generated code requires human review gate |
| Using AI to bypass security controls or code review | **No** | Violation of FORGE Constitution Rule 9 |

---

## 2. Data Classification — What You May and May Not Share

### Never share with any AI tool (external or internal):

| Data Type | Examples | Reason |
|---|---|---|
| Real customer PAN (card numbers) | Any 16-digit card number that is not a published test PAN | PCI-DSS Req. 3.3 — PAN is restricted data |
| Real CVV/CVV2 codes | The 3-digit security code from any real card | PCI-DSS — CVV must never be stored or transmitted outside PCI scope |
| Real customer PII | Names, addresses, date of birth, national ID, email linked to a real person | GDPR / POPIA / CCPA — personal data may not be processed outside approved systems |
| Production database connection strings | `jdbc:postgresql://prod.internal:5432/...` with credentials | Credential exposure risk |
| Production API keys, tokens, passwords | Any live credential for any environment | Credential exposure risk |
| Internal system hostnames and IP addresses for production | Internal DNS names or IPs of production infrastructure | Reconnaissance risk |
| Confidential business rules marked "Restricted" | Pricing algorithms, fee structures, regulatory decisions classified as confidential | IP and regulatory exposure |
| Unpublished financial results or M&A information | Any material non-public information | Legal and regulatory risk |

### Safe to share with AI tools:

| Data Type | Examples |
|---|---|
| Synthetic or anonymized test data | Generated accounts, masked card numbers (XXXX-XXXX-XXXX-1234) |
| Legacy source code (COBOL, Java, PL/1) | Internal programs being modernized — check with your security team if the code contains embedded credentials |
| Architecture diagrams and schemas | Table schemas, service diagrams, data models |
| Approved test PANs | 4111 1111 1111 1111 (Visa), 5500 0000 0000 0004 (Mastercard) |
| Publicly available domain concepts | ISO 8583 field definitions, Spring Boot patterns |
| Jira stories and Confluence pages | Business requirements and design decisions — excluding Restricted-classified content |
| Anonymized log examples | Log lines with PAN masked, no real correlation IDs from production |

### When in doubt:
Apply the **"newspaper test"** — if this data appeared in a news article alongside the name of your AI vendor, would it cause a problem? If yes, do not share it.

---

## 3. Test Data Requirements

All test data generated with or for AI tools must:

1. **Use synthetic data generators** — not real customer records from any environment (including UAT if UAT contains real data)
2. **Use approved test PANs** — only published test card numbers (see `.context/PAYMENTS_DOMAIN.md`)
3. **Never contain real names linked to real accounts** — use `John Doe`, `Test User`, or generated names
4. **Use placeholder amounts** — $100.00, $10.50, etc. — not real transaction amounts from customer records
5. **Mask or remove correlation IDs** from production log examples — production correlation IDs can reveal system topology

---

## 4. AI Code Disclosure

### Commit Message Disclosure

All commits that contain AI-generated code must include a disclosure tag in the commit message:

```
feat(authorization): add card status validation service

Implemented CardStatusValidator with BR-001 through BR-003 logic.
All unit tests passing (12/12).

AI-assisted: Claude Code — /generate-service
Reviewed by: [engineer name]
```

The `AI-assisted:` line must include: the tool name and the specific skill or mode used. This is mandatory for audit trail compliance.

### Pull Request Disclosure

All pull requests containing AI-generated code must:

1. Include `[AI-assisted]` in the PR title, or add the `ai-generated` label if your GitHub org has it configured
2. Note in the PR description which files or components were AI-generated
3. Confirm that the AI-assisted code was reviewed by a human before requesting review

**PR description template addition:**
```markdown
## AI Assistance
- Files generated with AI: [list files]
- Tool used: [Claude Code / GitHub Copilot / etc.]
- Human review completed: Yes
- FORGE review gate satisfied: Yes / No (in progress)
```

### Code Comment Disclosure (Optional but Recommended)

For complex logic generated by AI, add a brief comment:
```java
// Generated with Claude Code /generate-service — reviewed by [name] on [date]
// Source: BR-001, BR-003 from analysis/business-rules-register.md
public AuthorizationDecision evaluate(AuthorizationRequest request) {
    // ...
}
```

---

## 5. IP and Licensing Considerations

### Code Ownership

Code generated by AI tools on company projects is considered company intellectual property, subject to the same policies as any other code written by employees. Engineers must:

- Ensure generated code does not reproduce substantial portions of known open-source code without maintaining the original license
- Not claim AI-generated code as entirely original for any external publication, patent, or licence filing without legal review

### Licence Compliance

GitHub Copilot, Claude Code, and similar tools may occasionally suggest code that resembles open-source code under specific licences (GPL, AGPL, etc.). Before merging AI-generated code:

- If the code is non-trivial and closely resembles a known library or project, verify it is not reproducing licence-restricted code
- Your organisation's legal team should be consulted for any AI-generated code intended for external distribution or open-sourcing
- Where in doubt, rewrite the logic rather than risk inadvertent licence violation

### Data Sent to AI Vendors

| Tool | Data Sent to Vendor | Telemetry Opt-Out |
|---|---|---|
| Claude Code (Anthropic) | Prompts and context you send; not stored for training by default on paid plans | Confirm with your Anthropic enterprise agreement |
| GitHub Copilot (Microsoft) | Code snippets for suggestion; enterprise plans have telemetry controls | GitHub Copilot settings → Telemetry |
| JetBrains AI (JetBrains) | Prompts and code snippets; on-premises options available | JetBrains AI settings |
| IBM watsonx (IBM) | Prompts and context; enterprise agreements include data protection clauses | Confirm with IBM account team |
| Cursor | Prompts and code snippets | Privacy settings in Cursor preferences |

> Your organisation should have a data processing agreement (DPA) or enterprise licence with each AI vendor before engineers use these tools with internal code. Verify with your security and legal team.

---

## 6. Audit and Compliance Requirements

The following records must be maintained for all AI-assisted work on regulated or production systems:

| Record | Where Stored | Retention |
|---|---|---|
| AI tool used for each significant output | Commit message `AI-assisted:` tag | Git history (indefinite) |
| Human reviewer for each AI-assisted commit | PR approval record in GitHub | GitHub audit log (per org policy) |
| FORGE delivery package (traceability matrix) | `deliveries/stage-N-delivery-package.md` | Project repository (indefinite) |
| Business rules approved by SME/BA | `analysis/business-rules-register.md` | Project repository (indefinite) |
| ADRs for significant AI-influenced design decisions | `docs/architecture/adr-NNN-*.md` | Project repository (indefinite) |
| AI assumption register entries | `traceability/assumption-register.md` | Project repository (indefinite) |

For projects in PCI-DSS, GDPR, or other regulatory scope, the AI-assisted decision log in `traceability/decision-log.md` must be maintained and available for audit.

---

## 7. Prohibited Uses

The following uses of AI coding tools are prohibited under this policy:

1. **Bypassing code review** — AI-generated code may not be merged to main/master without a human review gate, regardless of the engineer's confidence in the output
2. **Using AI to reverse-engineer competitor products** — Do not feed competitor code, decompiled binaries, or scraped proprietary content into AI tools
3. **Using AI to generate misleading outputs** — Do not use AI to generate fake test results, false audit trails, or misleading documentation
4. **Using production credentials in AI sessions** — Covered in Section 2, but repeated here for emphasis: production credentials must never be pasted into any AI chat session
5. **Using public AI tools for code classified above INTERNAL** — Code or data classified as Confidential or Restricted must only be processed in approved, enterprise-licensed AI tools with confirmed data protection agreements
6. **Autonomous deployment of AI-generated code** — AI tools must not be granted write access to production infrastructure or the ability to deploy without a human approval step in the pipeline

---

## 8. Reporting a Concern

If you believe AI-generated code has been merged without proper review, or that sensitive data has been inadvertently shared with an AI tool:

1. **Immediately notify:** your Lead Engineer and Security team
2. **Do not delete evidence** — preserve the session logs, commit history, and any relevant context
3. **Log the incident** in your organisation's security incident management system
4. **For PAN exposure:** Follow your PCI Incident Response procedure immediately

---

## Policy Governance

| Role | Responsibility |
|---|---|
| Engineering Lead | Ensure team members are trained on this policy before using AI tools |
| DevSecOps | Enforce commit disclosure requirements in CI pipeline; audit quarterly |
| Security Team | Review and update this policy annually; respond to incidents |
| Individual Engineers | Comply with data classification rules; disclose AI assistance in all commits and PRs |

**Last reviewed:** 2026-04-24
**Next review due:** 2027-04-24
**Policy owner:** [USER FILLS — e.g., Head of Engineering / CISO]

---

*FORGE AI Usage Policy | Governance | Version 1.0*
*Aligned with: PCI-DSS v4.0, GDPR Article 22, POPIA Section 11*
