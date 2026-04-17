# FORGE Security and Data Protection Rules

These rules govern how AI tools handle sensitive data, generate secure code, and interact with regulated systems. They are absolute. No exception is permitted based on convenience, time pressure, or task scope.

---

## Section 1: Prompt Safety Rules — What NEVER Goes Into a Prompt

The following categories of information must **never** be included in any prompt sent to an AI tool, regardless of the tool, the context, or the stated security posture of the AI service. Assume every prompt may be logged, retained, and potentially accessible to third parties.

### 1.1 Credentials and Secrets

**NEVER include in a prompt:**

| Category | Examples |
|----------|----------|
| Database passwords | `DB_PASSWORD=S3cur3P@ss!`, connection strings with embedded auth |
| API keys and tokens | AWS secret access keys, Stripe API keys, OAuth client secrets |
| Private keys and certificates | RSA private keys, TLS certificate private keys, SSH private keys |
| Service account credentials | Kubernetes service account tokens, Cloud IAM key files |
| Message broker credentials | Kafka SASL passwords, RabbitMQ credentials |
| Application passwords | Admin console passwords, monitoring tool credentials |
| Encryption keys | AES keys, KMS key material, JWT signing secrets |
| VPN credentials | Pre-shared keys, user credentials for VPN access |

**What to do instead:**
- Reference the secret by its placeholder name: `DB_PASSWORD` (not the value)
- Describe the type of credential needed without providing the actual value
- Use a clearly fake placeholder: `[REPLACE_WITH_ACTUAL_API_KEY]`

### 1.2 Personally Identifiable Information (PII)

**NEVER include in a prompt:**

| Category | Examples |
|----------|----------|
| Government IDs | Social Security Numbers, National Insurance Numbers, Passport numbers, Driver's license numbers |
| Financial account identifiers | Full credit/debit card numbers, bank account numbers, routing numbers |
| Contact information | Real customer names, real email addresses, real phone numbers (use synthetic) |
| Location data | Real home addresses, GPS coordinates tied to individuals |
| Biometric identifiers | Fingerprint data, facial recognition templates, retinal scan data |
| Authentication credentials | Usernames and passwords, PINs, security question answers |
| Unique device identifiers | IMEI numbers, device serial numbers tied to known individuals |

**What to do instead:**
- Generate synthetic test data: `John Doe`, `555-0100`, `test@example.com`
- Use mask notation: `XXX-XX-1234` for SSN, `XXXX-XXXX-XXXX-4242` for card numbers
- See Section 3 for approved masking patterns

### 1.3 Protected Health Information (PHI) — HIPAA

**NEVER include in a prompt:**

- Patient names combined with any health information
- Medical record numbers
- Dates of service, admission, or discharge
- Diagnosis codes (ICD-10) or procedure codes (CPT) tied to real patients
- Lab results, imaging reports, clinical notes
- Prescription information
- Health insurance member IDs
- Any data from which a patient could be identified

**What to do instead:**
- Use fictional patient names with fictional diagnoses
- Use synthetic encounter records with no real identifiers
- Work with your compliance officer to establish approved synthetic datasets for AI-assisted development

### 1.4 Internal Infrastructure Information

**NEVER include in a prompt:**

- Production database hostnames and connection strings
- Internal IP address ranges and network topology maps (unless already publicly documented)
- Firewall rule details and security group configurations
- VPC peering configurations and private endpoint details
- Internal DNS naming conventions that reveal infrastructure layout
- Vulnerability scan results or penetration test reports
- Details of unpatched systems or known exploitable configurations

**What to do instead:**
- Use generic service names: `payment-service`, `customer-db`
- Describe topology in abstract terms: "Service A calls Service B via REST"
- Use fictional hostnames: `payment-service.internal`, `customer-db.internal`

### 1.5 Competitive and Regulatory Sensitive Information

**NEVER include in a prompt:**

- Unreleased product roadmaps with specific timing and feature details
- M&A due diligence materials and target company details
- Regulatory examination findings and enforcement correspondence
- Internal risk ratings and audit findings before public disclosure
- Trade secrets and proprietary algorithms
- Customer lists and pricing strategies
- Pending patent applications before publication

---

## Section 2: Data Handling Rules

### 2.1 Use Synthetic Data in All Examples

When providing examples to illustrate a question or demonstrate a pattern, always use synthetic (made-up) data that has no resemblance to real individuals or real business data.

**Acceptable synthetic data patterns:**
```
Customer Name: Alice Johnson (fictional)
Account Number: ACC-000001234 (clearly sequential/test)
SSN: 000-00-1234 (invalid SSN range — safe for testing)
Card Number: 4111-1111-1111-1111 (well-known test card number)
Phone: (555) 010-0001 (555 numbers are fictional by convention)
Email: alice.johnson@example.com (example.com is reserved for documentation)
Date of Birth: 1970-01-01 (commonly used test date)
Address: 123 Test Street, Testville, TS 00001
```

**Tools for generating safe synthetic data:**
- Faker libraries (Java: JavaFaker, Python: Faker, JS: @faker-js/faker)
- Mockaroo (web-based synthetic data generator)
- IBM InfoSphere Optim (enterprise data masking)
- AWS Glue DataBrew (cloud data masking)

### 2.2 Masking Before Including in Prompts

If you must include data structures that contain sensitive fields (e.g., when showing the AI a real data structure for analysis), mask all sensitive values before including them.

**Approved masking patterns:**

| Data Type | Masking Pattern | Example |
|-----------|----------------|---------|
| Credit/debit card number | Preserve last 4 | `XXXX-XXXX-XXXX-4242` |
| Bank account number | Preserve last 4 | `XXXXXXXX1234` |
| SSN / Tax ID | Preserve last 4 | `XXX-XX-5678` |
| Phone number | Preserve last 4 | `(XXX) XXX-9012` |
| Email address | Partial mask | `j***@e***.com` |
| Full name | Use initials or fictional name | `J.D.` or `Jane Doe` |
| IP address | Mask last octet | `192.168.1.XXX` |
| Password | Full mask | `[REDACTED]` |
| API key | Preserve last 6 characters | `sk-...abc123` |
| Database connection string | Mask credentials component | `jdbc:postgresql://host:5432/db?user=REDACTED&password=REDACTED` |

### 2.3 Output Monitoring

After receiving any AI output that deals with data structures, records, or transformations, review the output for accidental inclusion of:

- Data that was not in the input (hallucinated data that looks real)
- Patterns that match sensitive field formats (16-digit numbers, XXX-XX-XXXX patterns)
- Credential-like strings (high-entropy alphanumeric strings that could be keys)

**Automated scanning:** Where possible, run AI outputs through a secrets/PII scanner before storing or sharing them. Recommended tools:
- `detect-secrets` (Yelp): scans for credential patterns
- `gitleaks`: git-integrated credential scanning
- AWS Macie: S3-based PII detection
- Microsoft Purview: Enterprise data classification

---

## Section 3: Code Security Rules

### 3.1 No Hardcoded Credentials — Ever

**Rule:** No generated code may contain hardcoded credentials, secrets, or sensitive configuration values. This applies to:
- Application configuration files (`.properties`, `.yaml`, `.json`, `.xml`)
- Source code files (Java, Python, JavaScript, COBOL, etc.)
- Infrastructure as code (Terraform, CloudFormation, Kubernetes YAML, Helm)
- Scripts (shell, PowerShell, Python, Makefile)
- Docker files and container specifications
- CI/CD pipeline definitions

**Correct pattern — environment variables:**
```java
// Correct
String dbPassword = System.getenv("DB_PASSWORD");
if (dbPassword == null || dbPassword.isEmpty()) {
    throw new ConfigurationException("DB_PASSWORD environment variable is required");
}

// Wrong — never generate this
String dbPassword = "S3cur3P@ss!";
```

**Correct pattern — secrets manager:**
```java
// Correct — AWS Secrets Manager
SecretsManagerClient client = SecretsManagerClient.create();
GetSecretValueResponse response = client.getSecretValue(
    GetSecretValueRequest.builder().secretId("/myapp/db/password").build()
);
String dbPassword = response.secretString();
```

**Correct pattern — Spring Boot:**
```yaml
# application.yaml — correct
spring:
  datasource:
    password: ${DB_PASSWORD}   # resolved from environment variable
    
# application.yaml — never generate this
spring:
  datasource:
    password: mypassword123    # VIOLATION
```

### 3.2 Input Validation at System Boundaries

Every system boundary (HTTP endpoint, message queue consumer, file reader, command-line argument) must validate inputs before processing them.

**Generated REST endpoint pattern:**
```java
@PostMapping("/payments")
public ResponseEntity<PaymentResponse> processPayment(
        @Valid @RequestBody PaymentRequest request,   // Bean validation
        @RequestHeader("X-Correlation-ID") String correlationId) {
    
    // Additional domain validation beyond bean validation
    paymentValidator.validate(request);
    
    return ResponseEntity.ok(paymentService.process(request, correlationId));
}
```

**Validation requirements:**
- All numeric inputs: range validation (min, max, non-negative where applicable)
- All string inputs: max length, character whitelist where applicable
- All enum/code inputs: validate against allowed values, not just type-correct
- All date inputs: range validation, format validation, business logic validation (not in future, not before epoch)
- All foreign key / reference inputs: validate existence before processing

### 3.3 Principle of Least Privilege

Generated code must request only the permissions required for its function.

**Database access:**
- Application reads: SELECT only on required tables
- Application writes: INSERT, UPDATE on required tables only
- No application account should have DDL rights (CREATE, DROP, ALTER)
- No application account should have unrestricted wildcard grants

**Cloud IAM:**
```json
// Correct — least privilege policy
{
  "Statement": [{
    "Effect": "Allow",
    "Action": ["s3:GetObject", "s3:PutObject"],
    "Resource": "arn:aws:s3:::payment-docs-bucket/uploads/*"
  }]
}

// Wrong — never generate this
{
  "Statement": [{
    "Effect": "Allow",
    "Action": "*",
    "Resource": "*"
  }]
}
```

**Service-to-service:** Use service accounts with permissions scoped to the specific APIs called. Never reuse admin credentials for service accounts.

### 3.4 Secure Communication Defaults

Generated code for any network communication must use secure protocols by default:

- HTTP client: Always configure TLS, never `http://` for external endpoints
- Certificate validation: Never disable certificate validation (no `trustAllCerts`, no `verify=False`)
- Cipher suites: Use platform defaults (TLS 1.2 minimum, TLS 1.3 preferred); never configure weak ciphers
- Message queues: Always configure TLS for broker connections

### 3.5 Secure Logging Defaults

**Logging must NEVER include:**
- Passwords, tokens, or secrets (even in error messages)
- Full card numbers, SSNs, or other PII in log statements
- Request bodies for endpoints that accept sensitive data (log presence, not content)

**Logging must ALWAYS include:**
- Correlation ID for every log statement
- Masked versions of sensitive fields where necessary for debugging (last 4 digits, etc.)
- Error details without including sensitive data in the exception message chain

**Generated logging pattern:**
```java
// Correct
log.info("Payment authorized",
    "correlationId", correlationId,
    "transactionId", transactionId,
    "maskedCard", maskCard(cardNumber),  // "XXXX-XXXX-XXXX-4242"
    "amount", amount);

// Wrong — never generate this
log.info("Payment for card " + cardNumber + " by SSN " + ssn + " authorized");
```

---

## Section 4: Compliance Rules

### 4.1 Regulated Data Handling Flags

When generated code processes, stores, or transmits regulated data, the AI must explicitly flag the applicable regulation and the minimum required controls.

**Trigger conditions and required flags:**

| Condition | Regulation | Minimum Required Controls |
|-----------|------------|--------------------------|
| Code handles credit/debit card data | PCI-DSS | Tokenization, encryption at rest, audit logging, access control |
| Code handles patient health data | HIPAA | Encryption at rest and in transit, access logging, minimum necessary access |
| Code handles EU resident personal data | GDPR | Consent tracking, right to erasure capability, data minimization, breach notification |
| Code handles financial records for public companies | SOX | Audit trail, change management controls, access reviews |
| Code handles US financial account data | GLBA | Safeguards rule compliance, data disposal requirements |
| Code handles children's data (under 13, US) | COPPA | Parental consent verification, data collection minimization |

**Flag format:**
```
COMPLIANCE FLAG — PCI-DSS Scope
This code processes payment card data and is in scope for PCI-DSS.
Required controls that must be implemented before production deployment:
1. Card numbers must be tokenized using an approved tokenization service; do not store PANs
2. Any stored card data (tokens only) must be encrypted at rest (AES-256 minimum)
3. All access to card data must be logged with user ID, timestamp, and action
4. Access must be restricted to minimum necessary personnel
5. This module must be included in your annual PCI-DSS assessment scope

These controls are NOT included in the generated code and MUST be added before production use.
```

### 4.2 Masking and Encryption for Regulated Fields

When generating persistence layer code (JPA entities, database schemas, message schemas) that includes regulated fields:

- Card numbers: Tokenize (store token, not PAN) or encrypt with field-level encryption
- SSN/Tax IDs: Encrypt at field level; hash for lookups (not reversible)
- PHI fields: Field-level encryption; audit all reads
- Passwords: Hash only (bcrypt, Argon2, PBKDF2); never encrypt (encryption is reversible)

**Generated entity example with regulated field handling:**
```java
@Entity
public class CustomerPaymentMethod {
    @Id
    private UUID id;
    
    // Tokenized — never store raw PAN
    @Column(name = "card_token", nullable = false)
    private String cardToken;        // e.g., "tok_visa_4242" from payment processor
    
    // Safe to store — not the full number
    @Column(name = "card_last_four", length = 4)
    private String cardLastFour;
    
    // COMPLIANCE NOTE: cardToken must be managed through your PCI-DSS compliant
    // payment tokenization service (e.g., Stripe, Braintree, CyberSource).
    // This entity MUST NOT store the raw card number at any point.
}
```

---

*FORGE Constitution — Security and Data Protection v1.0*
