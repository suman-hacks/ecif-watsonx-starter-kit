---
tool: "JetBrains AI Assistant"
compatible-ides: "IntelliJ IDEA, PyCharm, WebStorm, GoLand, Rider, CLion"
forge-integration: "Full — uses custom prompts and constitution injection"
---

# JetBrains AI Assistant: FORGE Setup Guide

## Overview

JetBrains AI Assistant integrates directly into IntelliJ IDEA and other JetBrains IDEs. It provides inline code completion, chat, and refactoring. FORGE uses it primarily for Stage 05 (Development) and Stage 06 (Code Review).

**Best for:** Java (Spring Boot), Kotlin, Python, JavaScript/TypeScript, Go — any language with a JetBrains IDE

---

## Installation

### Step 1: Install the Plugin

1. Open IntelliJ IDEA → **Settings** (⌘, / Ctrl+Alt+S)
2. Go to **Plugins** → **Marketplace**
3. Search for **"AI Assistant"** (by JetBrains)
4. Install and restart IDE

### Step 2: Activate

- Requires JetBrains AI subscription (bundled with All Products Pack or available separately)
- Sign in via: **Tools → AI Assistant → Log In**
- Alternatively, configure a custom AI provider (OpenAI / Anthropic) via AI Assistant settings

---

## FORGE Configuration

### Custom System Prompt (Persona Configuration)

JetBrains AI Assistant supports custom system prompts. Set your FORGE persona here:

**Settings → Tools → AI Assistant → Customize System Prompt**

Paste the following (fill in your project details):

```
You are a FORGE-configured AI assistant for [PROJECT NAME].

ALWAYS ACTIVE RULES (from FORGE Constitution):
1. Source-Grounded: Every finding cites a specific source location — file name, line number
2. Facts vs Assumptions: Label [FACT] for directly observed, [ASSUMPTION] for inferred
3. Staged Progression: Do not jump stages — analysis before design, design before code
4. Security by Default: Input validation, parameterized queries, no PII in logs
5. Tests Alongside Code: Every new public method has a unit test; named with business rule (BR-NNN)
6. No Hardcoded Values: No magic numbers, hardcoded URLs, or credentials
7. Observability Built In: Structured logging (JSON), correlation IDs, Micrometer metrics
8. Hexagonal Architecture: api/ application/ domain/ adapters/ config/ observability/

PROJECT CONTEXT:
- Language: [Java 21 / Python 3.12 / TypeScript 5.x]
- Framework: [Spring Boot 3.2 / FastAPI / NestJS]
- Architecture: Hexagonal (clean architecture)
- Test framework: [JUnit 5 + Testcontainers / pytest / Jest]
- Package base: [com.acme.authorization]
- Business domain: [Payments / Banking / Healthcare / etc.]

BEHAVIOR:
- When generating code: follow hexagonal structure; add structured logging; include unit test
- When reviewing code: check OWASP Top 10; verify business rule test coverage; check for PII in logs
- When analyzing legacy: do not propose modernization — analyze only; label all confidence levels
- Use BigDecimal for all monetary amounts — never double or float
- Always import specific classes — never wildcard imports
```

---

## FORGE Prompt Library for JetBrains AI Chat

Open the AI Assistant chat panel: **View → Tool Windows → AI Assistant**

### Code Generation

```
Generate the Spring Boot implementation for [COMPONENT NAME].

Architecture spec: [paste service design section]
Business rules to implement: BR-001, BR-003 (paste rule descriptions)
Technology: Java 21, Spring Boot 3.2, Spring Data JPA, PostgreSQL

Follow hexagonal architecture:
- Domain layer: CardStatusValidator class with validate(Card) method
- Port interface in domain/port/in/
- Use case in application/ layer
- JPA adapter in adapters/persistence/

Requirements:
1. Every business rule has a named unit test: @DisplayName("BR-001: ...")
2. Structured logging at INFO for every business operation outcome
3. No PII (card numbers) in log statements — mask to last 4
4. BigDecimal for all monetary amounts
5. Return complete code — no abbreviations or "// rest here" comments
```

### Code Review

```
Review this code against FORGE standards:

[paste code]

Check specifically:
1. OWASP Top 10 — especially A03 (injection) and A09 (logging)
2. Business rule BR-NNN is implemented correctly per: [paste rule description]
3. Test coverage — is every public method tested? Are tests named with BR-NNN?
4. Hexagonal architecture — is business logic out of the controller?
5. No PII in log statements

Format findings as: BLOCKER / MAJOR / MINOR with file:line reference.
```

### Legacy COBOL Analysis

```
Analyze this COBOL paragraph for business rules. Do NOT propose modernization.

[paste COBOL code]

Produce:
1. What this code does in plain business language
2. Every IF/EVALUATE decision point with: condition, true path, false path
3. All data fields accessed and their apparent business meaning
4. Confidence level for each finding [FACT] or [ASSUMPTION]
5. Any ambiguous conditions that require SME clarification
```

### Unit Test Generation

```
Generate JUnit 5 unit tests for this class:

[paste class code]

Business rules this class implements: BR-001, BR-002 (paste descriptions)

Requirements:
1. Name every test after its business rule: @DisplayName("BR-001: ...")
2. Use AAA pattern (Arrange / Act / Assert) with blank line separation
3. Use @ParameterizedTest for multiple similar scenarios
4. Test all boundary conditions (at-limit, just-over, just-under)
5. Test all null/empty inputs at public API boundaries
6. Use CardTestBuilder (see below) for test data:
   [paste test builder if available]
```

---

## JetBrains AI Assistant Features to Use

| Feature | How to Access | FORGE Use Case |
|---|---|---|
| **Inline completion** | Tab to accept | Completes method bodies aligned with patterns |
| **Generate** | Alt+Insert → AI Generate | Generate tests, docs, implementations |
| **Explain code** | Right-click → AI Actions → Explain | Understand legacy code before touching it |
| **Review code** | Right-click → AI Actions → Review | Pre-PR quality check |
| **Fix code** | Right-click → AI Actions → Fix | Fix compilation errors or obvious bugs |
| **Rename** | Shift+F6 → AI Suggestions | Rename with semantic context |
| **Chat** | View → Tool Windows → AI Assistant | Complex multi-step tasks |

---

## Multi-Tool Strategy (JetBrains + Claude Code)

Use both together based on task:

| Task | Use JetBrains AI | Use Claude Code |
|---|---|---|
| Inline code completion | ✓ | |
| Quick method generation | ✓ | |
| Complex architecture design | | ✓ |
| Legacy COBOL analysis | | ✓ (or WCA4Z) |
| Multi-file refactoring | | ✓ |
| Business rule extraction | | ✓ |
| Security review (multi-file) | | ✓ |
| Unit test generation (in-file) | ✓ | |
| Full test suite generation | | ✓ |
| Documentation generation | ✓ | |

---

## .idea/forge-context.md (Optional Project Context File)

Create this file in your project root to give JetBrains AI context that persists across sessions. JetBrains AI can be pointed to reference files in the IDE.

```markdown
# FORGE Project Context — [PROJECT NAME]

## Current Stage
Stage [N] — [Stage Name]

## Active Business Rules
BR-001: Decline authorization for blocked cards
BR-002: Decline authorization for expired cards
BR-003: Decline authorization when daily limit exceeded

## Technology Stack
- Language: Java 21
- Framework: Spring Boot 3.2
- Database: PostgreSQL 15 (via Spring Data JPA)
- Messaging: Apache Kafka 3.4
- Test: JUnit 5 + Testcontainers + WireMock

## Package Structure
Base: com.acme.authorization
Layers: api / application / domain / adapters / config / observability

## Coding Rules
- BigDecimal for all money — never double
- Mask card numbers in all logs (last 4 only)
- Structured logging — use key-value pairs, not string concatenation
- Return Optional<T> instead of null from repository methods
- All external calls via port interfaces in domain/port/out/
```
