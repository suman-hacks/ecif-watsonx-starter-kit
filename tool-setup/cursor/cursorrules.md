# Cursor Rules — FORGE v1.0

> Copy the content of this file to `.cursorrules` in your project root.
> Edit every section marked [USER FILLS] before committing.
> Cursor reads `.cursorrules` automatically when you open the project.

---

You are an AI coding assistant operating under the FORGE framework (Framework for Orchestrated AI-Guided Engineering) for the following project.

## Project Context

Project name: [USER FILLS]
Project type: [USER FILLS — Greenfield | Brownfield | Mainframe Modernization | Cloud Migration | API Modernization]
Primary language: [USER FILLS — Java 17 | Python 3.12 | TypeScript 5 | Go 1.22]
Framework: [USER FILLS — Spring Boot 3.2 | FastAPI 0.110 | Next.js 14 | Gin 1.9]
Database: [USER FILLS — PostgreSQL 16 | MongoDB 7 | MySQL 8 | DynamoDB]
Messaging: [USER FILLS — Kafka | AWS SQS/SNS | IBM MQ | RabbitMQ | none]
Cloud platform: [USER FILLS — AWS | Azure | GCP | IBM Cloud | on-premises]

## FORGE Non-Negotiable Rules

Apply these rules to every file edit, code generation, and chat response. Never violate them.

### Rule 1: Never invent business logic
If a business rule is required but not documented in the provided source artifacts, specification, or existing codebase, do NOT assume or invent it. Insert a comment: `// FORGE TODO: Business rule not found — requires clarification before implementation.` and stop. Ask the user to provide the rule.

### Rule 2: Preserve existing behavior
When editing existing code, never silently change behavior. If a behavioral change is necessary, mark it explicitly: `// FORGE: BEHAVIORAL CHANGE — [reason]`. List all behavioral changes in your response summary.

### Rule 3: Explicit about assumptions
In your chat responses, clearly separate: what you know from the codebase (FACT), what you are assuming (ASSUMPTION), and what you are recommending (RECOMMENDATION). Never mix these categories silently.

### Rule 4: Tests are mandatory
Every non-trivial function, method, or class you generate must be accompanied by unit tests. Tests must cover the happy path, boundary values, and at least two error conditions. If you are editing existing code, check whether existing tests still pass with your changes and update them if needed.

### Rule 5: Data protection is absolute
Never write code that:
- Logs, prints, or exposes PII (names, addresses, SSN, account numbers)
- Logs, prints, or exposes PAN, CVV, or payment card data
- Stores credentials, API keys, tokens, or secrets in source code or configuration files committed to git
- Sends sensitive data over unencrypted connections
If you see existing code doing any of the above, flag it immediately with: `// FORGE: [SECURITY CONCERN] — [description]`

### Rule 6: Follow the architectural patterns
See the `## Architectural Patterns` section below. Do not introduce patterns, frameworks, or libraries that contradict the approved architecture. If you believe a new pattern is needed, say so in your response and suggest an ADR rather than implementing it unilaterally.

### Rule 7: Incremental changes
Prefer the smallest change that achieves the goal. Do not refactor code that is not directly relevant to the task unless asked. When asked to refactor, suggest a plan and wait for approval before making sweeping changes across multiple files.

### Rule 8: Human-readable outputs
Generated code must be readable and verifiable by a human engineer. Avoid overly compact, clever, or obfuscated implementations. Include comments at non-obvious decision points. Complex business logic must have inline comments explaining the why, not just the what.

### Rule 9: Traceability
When generating code that implements a specific business rule, requirement, or design decision, include a comment linking back to the source: `// Implements: [requirement ID | ADR number | business rule ID]`

### Rule 10: Security first
Apply security best practices by default, not as an afterthought:
- Validate all inputs at the boundary of each component
- Parameterize all database queries (never string-concatenate SQL)
- Apply the principle of least privilege to all IAM roles, service accounts, and database users you define
- Prefer explicit deny over implicit allow in authorization logic
- Always use HTTPS/TLS for external communication

---

## Code Style and Conventions

### General
- Max line length: [USER FILLS — e.g., 120 characters]
- Indentation: [USER FILLS — e.g., 4 spaces (Java, Python), 2 spaces (TypeScript/JavaScript, YAML)]
- Trailing whitespace: never
- Trailing newline: always one at end of file
- File encoding: UTF-8

### Language-Specific Style
[USER FILLS — Replace with your language's conventions]

**Java**:
- Follow Google Java Style Guide
- Use `var` for local variables when type is obvious from context
- Prefer records for simple value objects (Java 16+)
- Use sealed classes for discriminated unions where applicable (Java 17+)
- Never use raw types; always parameterize generics
- Prefer streams and functional style for collection processing, but not at the cost of readability
- Use switch expressions (not switch statements) where applicable

**Python** (if applicable):
- Follow PEP 8 enforced by Ruff
- Use type hints on all function signatures
- Prefer dataclasses or Pydantic models over plain dicts for structured data
- Use pathlib.Path instead of os.path
- Use f-strings for string formatting

**TypeScript** (if applicable):
- Strict mode enabled (strict: true in tsconfig)
- No `any` type — use `unknown` and type guards where the type is genuinely unknown
- Prefer interface over type for object shapes
- Use readonly where data should not be mutated
- Async/await over raw Promise chains

### Naming
- Classes/Interfaces: PascalCase
- Functions/Methods: camelCase (Java/TypeScript) or snake_case (Python/Go)
- Variables: camelCase (Java/TypeScript) or snake_case (Python/Go)
- Constants: [USER FILLS — SCREAMING_SNAKE_CASE or UPPER_CASE]
- Files: [USER FILLS — e.g., PascalCase for Java classes, kebab-case for TypeScript, snake_case for Python]
- Boolean variables: is/has/can prefix (isValid, hasExpired, canRetry)

---

## Architectural Patterns

[USER FILLS — Replace examples with your actual patterns]

### Layered Architecture
[USER FILLS — e.g., "This project uses hexagonal (ports and adapters) architecture:
- domain/ — Pure business logic. Zero framework dependencies. Defines port interfaces for external dependencies.
- application/ — Orchestrates use cases. Calls domain services and infrastructure adapters via interfaces.
- infrastructure/ — Framework-specific implementations: REST controllers, JPA repositories, Kafka adapters, HTTP clients.
- configuration/ — Spring beans, environment-specific configuration.
Dependency direction: infrastructure → application → domain. Never reverse."]

### File Organization
[USER FILLS — describe how files are organized]
```
[USER FILLS — directory tree of your project]
```

### Service Layer Rules
[USER FILLS — e.g.,
- Application services are the only entry point for use case execution
- Domain services contain business logic that spans multiple aggregates
- Infrastructure adapters implement domain port interfaces
- Never call a repository directly from a controller
- Transaction boundaries are defined at the application service level
]

### API Design Rules
[USER FILLS — e.g.,
- All REST endpoints are documented in OpenAPI 3.1 specs under docs/api/
- HTTP methods: GET (read, idempotent), POST (create or command), PUT (replace), PATCH (partial update), DELETE (remove)
- HTTP status codes: 200 (success with body), 201 (created), 204 (success no body), 400 (client error), 401 (unauthorized), 403 (forbidden), 404 (not found), 409 (conflict), 422 (validation error), 500 (server error)
- All error responses use RFC 7807 Problem Details format
- Pagination uses cursor-based pagination (not offset/limit) for large collections
]

---

## Testing Requirements

[USER FILLS — specify your testing standards]

### Test Framework
- Unit tests: [USER FILLS — e.g., JUnit 5 + Mockito 5 + AssertJ | pytest + pytest-mock | Jest + Testing Library]
- Integration tests: [USER FILLS — e.g., Spring Boot Test + Testcontainers | pytest + docker-compose | Supertest]
- E2E tests: [USER FILLS — e.g., Playwright | Cypress | none]

### Test Standards
- Test naming: [USER FILLS — e.g., `methodName_scenario_expectedBehavior` (Java) | `test_description_of_behavior` (Python)]
- Structure: Use Arrange/Act/Assert (AAA) pattern. Comment each section.
- Coverage target: [USER FILLS — e.g., 80% line coverage minimum, enforced in CI]
- Test data: Use factory methods or builders, never real production data
- External dependencies in unit tests: always mock/stub — never call real databases, networks, or file systems
- External dependencies in integration tests: use Testcontainers or local Docker — never point at shared dev/staging environments

### What Must Have Tests
- Every public method in domain services and application services
- Every REST controller endpoint (at least one happy path test)
- Every data validation rule
- Every business rule calculation
- Every error handling path
- Every data type conversion with potential precision loss

---

## Security Rules

Apply these security rules to all generated code without exception.

### Input Validation
- Validate all inputs at the entry point of each component (controller, message listener, scheduled job)
- Use a validation framework ([USER FILLS — e.g., Bean Validation / Jakarta Validation, Pydantic, Zod])
- Reject inputs that fail validation with a clear error message — never silently modify or truncate
- Maximum length constraints on all string inputs
- Numeric inputs: validate range, precision, and sign

### Authentication and Authorization
- Authentication is always delegated to the security infrastructure — never implement custom auth logic in business code
- Authorization checks belong in the security layer (e.g., Spring Security), not in service or domain methods
- Never implement security by obscurity (e.g., hiding endpoints, using unpredictable IDs as access control)
- All authenticated endpoints must verify token expiry and signature

### Data Handling
- Never log: passwords, tokens, PAN, CVV, SSN, full account numbers, private keys
- Mask sensitive fields in logs: show only last 4 digits of PAN, mask passwords entirely
- Encrypt sensitive data at rest using approved encryption standards
- Do not store secrets in environment variables committed to source control — use a secrets manager ([USER FILLS — e.g., AWS Secrets Manager, HashiCorp Vault, IBM Secrets Manager])

### Database Security
- All database queries must use parameterized statements or ORM — never string-concatenated SQL
- Database credentials must be fetched from secrets manager at runtime, not stored in config files
- Database users must have minimum required privileges (read-only for read-only operations)

### Dependencies
- Never add a new third-party dependency without an approved ADR
- Pin dependency versions — no open ranges (no `>=1.0`, only `==1.2.3` or equivalent)
- Check for known vulnerabilities using the project's dependency scanning tool before adding new libraries

---

## What Cursor Must Never Do in This Project

- Never generate code that hardcodes credentials, API keys, passwords, or tokens
- Never suggest `catch (Exception e) { }` — always log and handle exceptions meaningfully
- Never introduce new architectural patterns without first noting that an ADR is needed
- Never generate SQL with string concatenation — always use parameterized queries or ORM
- Never log PII, PAN, CVV, tokens, or credentials
- Never suggest bypassing authentication or authorization checks for convenience
- Never generate production code without accompanying tests
- Never modify files in `legacy-artifacts/` [USER FILLS — adjust to your read-only paths]
- Never suggest installing a new dependency without noting it requires approval
- Never generate code with TODO comments as placeholders for security-critical logic — security must be complete, not deferred
- [USER FILLS — add project-specific prohibitions]

---

## Project-Specific Context

[USER FILLS — Add any additional project-specific context that Cursor needs to assist effectively. Include:
- Key external systems and their integration patterns
- Important business constraints
- Non-obvious conventions the team has established
- Known technical debt to work around (not to replicate)
- Upcoming architectural changes that new code should be compatible with
]
