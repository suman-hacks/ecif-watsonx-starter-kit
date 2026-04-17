# Knowledge Packs — FORGE Framework

## What Are Knowledge Packs?

Knowledge packs are self-contained, task-specific context documents that you load into your AI coding tool's session to give it domain expertise it would not otherwise have.

An AI coding assistant's base model knows general programming patterns, common frameworks, and broad software engineering principles. It does not know:
- Your organization's specific domain (payments, banking, insurance, logistics)
- Legacy system conventions (COBOL program structure, CICS idioms, JCL patterns)
- Your chosen architecture patterns (which resilience library, which event schema standard)
- Industry-specific compliance requirements (PCI-DSS card data handling, AML screening rules)

Knowledge packs fill these gaps. Each pack is a structured markdown document that teaches the AI what it needs to know to perform a specific type of task accurately, without hallucinating domain rules or generating architecturally inconsistent code.

**Knowledge packs are not prompts.** They are context — the background knowledge the AI reads before you give it a task prompt. Think of them as the briefing document you hand a new engineer before they start on a task.

---

## When to Load Knowledge Packs

Each SDLC stage README identifies which knowledge packs are relevant for the tasks in that stage. The general rule is:

| Situation | Knowledge Pack to Load |
|---|---|
| Analyzing a COBOL program | `legacy-cobol/` |
| Designing a new microservice | `microservices/` |
| Building event-driven integration | `event-driven/` |
| Designing REST or async APIs | `api-design/` |
| Writing or reviewing tests | `testing/` |
| Adding observability instrumentation | `observability/` |
| Working on payments or banking features | `domains/payments-banking/` |
| Combining modernization approaches | Combine relevant packs (see below) |

You may also load a knowledge pack when:
- A developer is unfamiliar with a domain and needs AI-assisted context building
- You are generating code in an area the AI historically gets wrong (e.g., financial arithmetic precision, COBOL level numbers)
- You need consistent AI behavior across a team (everyone loads the same pack for the same task)
- You are onboarding a new team member and want the AI to produce idiomatic, pattern-consistent code

---

## How to Load a Knowledge Pack

Loading a knowledge pack is a manual step. There is no automatic injection — you copy the pack content into your AI session before issuing your task prompt.

### Step-by-Step

1. Identify which knowledge pack(s) you need (from this README or from the SDLC stage README)
2. Open the knowledge pack file (e.g., `knowledge-packs/legacy-cobol/README.md`)
3. Copy the entire file content
4. Open your AI tool's chat or prompt interface
5. Paste the knowledge pack content as the first message or as part of the system prompt
6. Then provide your task prompt

### Tool-Specific Loading

| AI Tool | How to Load a Knowledge Pack |
|---|---|
| **GitHub Copilot Chat** | Paste pack content as first message in the chat, then ask your question |
| **Claude Code** | Add pack content to `CLAUDE.md` under a "Loaded Knowledge Packs" section, or paste into the session before the task |
| **Cursor** | Add to `.cursorrules` for always-on loading, or paste into Composer/Chat for session-specific loading |
| **watsonx Code Assist** | Include pack content in the prompt context before the task instruction |
| **AWS Q Developer** | Paste pack content into the chat before issuing the task instruction |
| **JetBrains AI Assistant** | Include in the prompt context block before your question |

### Keeping Pack Scope Focused

Load only the packs relevant to your current task. Loading too many packs at once:
- Consumes token context that should be used for code
- May introduce conflicting guidance if packs cover overlapping topics at different levels of specificity
- Makes it harder for the AI to identify which guidance applies to the current task

A typical session uses 1–2 knowledge packs plus the FORGE constitution.

---

## Available Knowledge Packs

| Pack | File | Purpose | Typical Users |
|---|---|---|---|
| **Legacy COBOL** | `legacy-cobol/README.md` | Teaches AI to read, analyze, and document COBOL programs: divisions, data definitions, EXEC SQL, EXEC CICS, file operations, modernization mapping | Mainframe engineers, architects analyzing legacy systems, developers receiving modernization handoff |
| **Microservices** | `microservices/README.md` | Service design principles, bounded context identification, data patterns (saga, CQRS, event sourcing), communication patterns, resilience patterns (circuit breaker, retry, bulkhead) | Architects, developers building distributed services |
| **Event-Driven Architecture** | `event-driven/README.md` | Event design (naming, envelope, versioning), Kafka patterns (topic design, consumer groups, DLQ), outbox pattern, saga (choreography vs orchestration), replay and recovery | Architects, developers building event-driven systems |
| **API Design** | `api-design/README.md` | REST resource naming, HTTP methods, status codes, idempotency, pagination, OpenAPI structure, API security (OAuth 2.0, scopes), payments API specifics | Architects, developers building APIs, product managers reviewing API contracts |
| **Testing** | `testing/README.md` | Test pyramid (unit/integration/E2E ratios), AAA pattern, test data builders, Testcontainers, WireMock, contract testing with Pact, performance testing tools, test data management | QA engineers, developers writing tests, tech leads reviewing test coverage |
| **Observability** | `observability/README.md` | Structured logging standards, RED metrics, distributed tracing with OpenTelemetry, alerting strategy, SLO-based burn rate alerts, payments-specific observability | Platform engineers, SREs, developers adding instrumentation |
| **Payments & Banking Domain** | `domains/payments-banking/README.md` | Domain glossary (authorization, settlement, clearing, ISO 8583, PAN), authorization flow, issuer business rules, compliance (PCI-DSS, AML, OFAC), ICS domain, technical patterns | All engineers working on payments or banking modernization projects |

---

## How to Combine Packs

For complex tasks, you will often need more than one knowledge pack. The recommended composition pattern is:

```
[FORGE Constitution]
+
[Project Context File]
+
[Knowledge Pack 1]
+
[Knowledge Pack 2]
+
[Task Prompt]
```

### Composition Examples

**COBOL Authorization Program Analysis:**
```
constitution/01-core-principles.md
+ project-contexts/mainframe-modernization/README.md
+ knowledge-packs/legacy-cobol/README.md
+ knowledge-packs/domains/payments-banking/README.md
+ [Task prompt: "Analyze the AUTHZ001.cbl program and produce a T2 DECISIONS document"]
```

**New Payments Microservice Design:**
```
constitution/01-core-principles.md
+ project-contexts/greenfield/README.md
+ knowledge-packs/microservices/README.md
+ knowledge-packs/api-design/README.md
+ knowledge-packs/domains/payments-banking/README.md
+ [Task prompt: "Design the Authorization Service bounded context and produce a service design document"]
```

**Event-Driven Integration Testing:**
```
constitution/01-core-principles.md
+ knowledge-packs/event-driven/README.md
+ knowledge-packs/testing/README.md
+ [Task prompt: "Generate integration tests for the PaymentProcessed event consumer"]
```

**Adding Observability to a Payments Service:**
```
constitution/01-core-principles.md
+ knowledge-packs/observability/README.md
+ knowledge-packs/domains/payments-banking/README.md
+ [Task prompt: "Add structured logging and RED metrics to the AuthorizationService"]
```

### Pack Layering Rule

When two packs cover the same topic at different levels of specificity, the more specific pack takes precedence. For example:
- `api-design/` covers general API standards
- `domains/payments-banking/` covers payments-specific API requirements (idempotency for payment initiation, amount handling as strings, masked PAN)
- The payments-specific requirements supplement and override the general rules where they conflict

---

## How to Create Custom Knowledge Packs

Organizations often have internal standards, domain conventions, or technology choices that are not covered by the standard FORGE packs. You can create custom knowledge packs for your team.

### Custom Pack Template

Use `templates/knowledge-pack-template.md` as your starting point. A well-formed knowledge pack includes:

```markdown
# [Pack Name] Knowledge Pack
## Purpose
[What does this pack teach the AI?]

## When to Load
[Which tasks or SDLC stages need this pack?]

## Inputs This Pack Requires
[What context must the AI already have to use this pack?]

## Core Knowledge
[The actual knowledge — detailed, specific, actionable.
Use subsections. Include examples of correct patterns.
Be specific enough that the AI cannot misinterpret.]

## Key Patterns
[Concrete patterns the AI should generate or recognize]

## Anti-Patterns (What Not To Do)
[Explicit list of things the AI must NOT do in this domain,
with reasons. This is important — anti-patterns prevent
hallucination of incorrect but plausible-looking patterns.]

## Examples
[Worked examples showing correct AI behavior]

## Expected AI Behavior When This Pack Is Loaded
[What should the AI do differently? What outputs should change?]

## Handoff to Next Stage
[What artifacts does this pack's tasks produce? Where do they go next?]
```

### Guidelines for Custom Packs

1. **Be specific, not general.** The AI already knows general programming. Pack content should cover things the AI could plausibly get wrong because the domain is specialized.
2. **Include anti-patterns.** Telling the AI what NOT to do is as important as telling it what to do.
3. **Use examples.** Worked examples anchor the AI's output to your expected format and quality level.
4. **Keep packs focused.** A pack covering one topic well is more useful than a pack that tries to cover everything.
5. **Version your packs.** Add a version number and last-updated date to the pack header so teams know when to refresh.
6. **Store in the repo.** Custom packs should live in `knowledge-packs/[your-org]/` within your project repository so they are version-controlled alongside your code.

### Organization-Specific Pack Ideas

- `knowledge-packs/[org]/internal-architecture-standards.md` — your internal platform choices, naming conventions, package structure
- `knowledge-packs/[org]/data-classification.md` — how your organization classifies data and what controls apply at each level
- `knowledge-packs/[org]/regulatory-environment.md` — the specific regulations you operate under (e.g., GDPR, CCPA, PSD2, Basel III)
- `knowledge-packs/[org]/test-framework.md` — your test framework choices, test naming conventions, CI/CD test gates

---

## Quick Reference: Pack × Task Matrix

| Task | Recommended Packs |
|---|---|
| Analyze a COBOL batch program | `legacy-cobol` + `domains/payments-banking` (if payments) |
| Analyze a CICS online transaction | `legacy-cobol` + `domains/payments-banking` |
| Design a new microservice bounded context | `microservices` + domain pack |
| Write OpenAPI spec for a payments endpoint | `api-design` + `domains/payments-banking` |
| Design Kafka event schema | `event-driven` + `api-design` |
| Write unit tests for domain logic | `testing` |
| Write integration tests for Kafka consumer | `testing` + `event-driven` |
| Add logging/metrics/tracing to a service | `observability` |
| Design an authorization flow modernization | `legacy-cobol` + `microservices` + `domains/payments-banking` |
| Performance test a payments API | `testing` + `observability` + `domains/payments-banking` |

---

*FORGE Knowledge Packs — Domain expertise, on demand. Load what you need. Leave what you don't.*
