# FORGE Guardrails

Guardrails are the enforcement mechanisms for the FORGE Constitution. Some are automated (CI/CD gates, linters, scanners), some are manual (checklists).

## Guardrail Files

| File | Purpose |
|---|---|
| `prompt-safety.md` | What must never go into an AI prompt |
| `code-quality.md` | Code quality gates before merge |
| `architecture-compliance.md` | Architecture conformance checks |

Governance review gates are in `governance/review-gates.md`.

---

## Prompt Safety Guardrail

**Pre-prompt checklist — run before every AI session involving source code:**

- [ ] No production passwords, API keys, or tokens in the prompt or pasted code
- [ ] No real customer PII (names, SSNs, full card numbers, account numbers)
- [ ] No real dates of birth, addresses, or contact information
- [ ] No production database connection strings
- [ ] Card numbers: masked to last 4 digits (XXXX-XXXX-XXXX-1234)
- [ ] SSNs: masked (***-**-XXXX)
- [ ] Account numbers: masked or use TEST-ACC-NNN format
- [ ] No internal network IP ranges or hostnames that are not already public
- [ ] Source code from regulated repositories has been reviewed for sensitive content

**If any item is checked as present:** Stop. Redact the sensitive content. Then proceed.

**Automated scanning patterns** (add to pre-commit hooks or CI):
```bash
# Common PAN pattern (Luhn-valid card numbers)
# grep -E "\b([4-9][0-9]{3}[\s-]?[0-9]{4}[\s-]?[0-9]{4}[\s-]?[0-9]{4})\b"

# AWS key pattern
# grep -E "AKIA[0-9A-Z]{16}"

# Generic secret patterns
# truffleHog / gitleaks / detect-secrets
```

---

## Code Quality Guardrail

**Pre-merge checklist:**
- [ ] Code compiles without warnings
- [ ] All unit tests pass
- [ ] Branch coverage ≥ 90% on business logic classes
- [ ] No BLOCKER findings from static analysis (SonarQube / Checkstyle)
- [ ] No hardcoded credentials, URLs, or magic numbers
- [ ] Structured logging added to all new business operations
- [ ] API changes are backwards-compatible or versioned
- [ ] Performance: no N+1 queries introduced

**Automated tools:**
- Java: SonarQube + SpotBugs + Checkstyle + OWASP Dependency-Check
- Python: bandit + pylint + safety
- TypeScript: ESLint + Snyk
- All: Semgrep (custom rules), secret scanning

---

## Architecture Compliance Guardrail

**Per-PR architecture check:**
- [ ] No business logic in API controllers (@RestController)
- [ ] No infrastructure imports in domain classes (no @Repository, @JpaRepository in domain package)
- [ ] No cross-service database access (verify with ArchUnit tests)
- [ ] All external calls go through adapter interfaces (no direct HTTP calls in domain)
- [ ] No shared mutable state without explicit documentation

**ArchUnit example (Java):**
```java
@ArchTest
static final ArchRule domain_must_not_depend_on_infrastructure =
    noClasses().that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAPackage("..adapters..");
        
@ArchTest        
static final ArchRule controllers_must_not_contain_business_logic =
    noClasses().that().areAnnotatedWith(RestController.class)
        .should().haveSimpleName("..Service..")
        .orShould().dependOnClassesThat().resideInAPackage("..domain.policies..");
```
