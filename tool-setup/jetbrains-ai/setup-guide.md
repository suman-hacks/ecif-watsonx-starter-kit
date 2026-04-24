# JetBrains AI + ATOM Setup Guide
# FORGE v2.0 | IntelliJ IDEA, PyCharm, WebStorm, Rider

> Step-by-step guide for configuring AI assistance in JetBrains IDEs with FORGE guardrails and ATOM patterns.

---

## AI Options for JetBrains IDEs

You have three options. You can use one or combine them.

| Option | Best For | Cost |
|---|---|---|
| **JetBrains AI Assistant** | Native IntelliJ integration, code completion, chat | JetBrains AI subscription |
| **GitHub Copilot plugin** | If your team already has Copilot licenses | GitHub Copilot license |
| **Claude Code (terminal + IDE)** | Deep codebase analysis, complex generation, FORGE Skills | Anthropic/Claude subscription |

**Recommended setup for ATOM engineers:**
- **JetBrains AI Assistant** for in-IDE completion and chat
- **Claude Code in the terminal** for multi-file analysis, legacy discovery, and FORGE slash commands
- Both read from your `.context/` files and `CLAUDE.md`

---

## Option A: JetBrains AI Assistant + FORGE

### Step 1: Install JetBrains AI Assistant

1. Open IntelliJ IDEA (or any JetBrains IDE)
2. Go to **File → Settings → Plugins** (or **IntelliJ IDEA → Preferences → Plugins** on macOS)
3. Search for **"AI Assistant"**
4. Click **Install** and restart the IDE
5. Log in with your JetBrains account when prompted

### Step 2: Open the AI Assistant Panel

- Press **Ctrl+Shift+A** (Windows/Linux) or **Cmd+Shift+A** (macOS) → search "AI Assistant"
- Or: click the AI icon in the right toolbar

### Step 3: Configure FORGE Context as System Instructions

1. In the AI Assistant panel, click the **gear icon (⚙)** → **Modify AI Instructions**
2. Paste the following as your AI instructions:

```
You are an AI engineering assistant operating under the FORGE framework with the ATOM microservices chassis.

ALWAYS apply these rules:
1. Never invent business logic not present in provided source artifacts.
2. Preserve existing behavior when modifying or modernizing code.
3. Separate FACT (from code/docs) from ASSUMPTION (inferred) in all analysis.
4. Use @AtomService (not @Service), @AtomRepository (not @Repository), @AtomValidated.
5. All controllers return ResponseEntity<ApiResponse<T>>.
6. All downstream HTTP calls use @CircuitBreaker with a fallback method.
7. Logging: always structured key-value pairs. Never log PAN, CVV, passwords, or tokens.
8. Constructor injection only — never @Autowired field injection.
9. Domain layer must have zero Spring/JPA/Kafka imports.
10. Generate unit tests alongside every generated class.
11. Never hardcode URLs, credentials, or configuration values.
12. Flag security issues immediately with [SECURITY: SG-N] format.

ATOM Package Structure:
- api/controller/ — REST controllers
- api/dto/ — immutable request/response DTOs
- application/service/ — @AtomService business logic
- domain/model/ — entities, value objects (no framework deps)
- domain/port/ — interface definitions
- infrastructure/persistence/ — @AtomRepository implementations
- infrastructure/client/ — external HTTP clients with @CircuitBreaker

Apply these rules to every suggestion, completion, and chat response.
```

3. Click **OK**. These instructions are now applied to every session.

### Step 4: Add Project-Specific Context to Each Session

For ATOM-specific work, start your AI Assistant chat with:

```
This is a Java 17 / Spring Boot 3.x project using the ATOM microservices chassis.
The project context is in .context/ATOM_CHASSIS.md — apply all ATOM patterns.
The guardrails are in .context/CORE_SKILLS.md — apply all security and quality rules.
[For modernization: The migration rules are in .context/MODERNIZATION.md]

Current task: [describe your task]
```

### Step 5: Using AI Assistant for ATOM Service Generation

**Inline completion:**
Start typing the class signature — AI Assistant completes the skeleton:
```java
@AtomService  // AI continues with correct ATOM structure
@Slf4j
@RequiredArgsConstructor
public class PaymentAuthorizationService {
```

**In the AI Chat panel:**
```
Generate a complete ATOM service for transaction spending limit checks.
- Use @AtomService with @Slf4j and @RequiredArgsConstructor
- Inject: AccountLimitRepository (outbound port), CoreBankingClient
- Method: LimitCheckResult checkLimit(TransactionRequest request)
- All downstream calls with @CircuitBreaker
- Structured logging for every decision
- Unit tests with JUnit 5 + Mockito + AssertJ
Apply ATOM chassis patterns: @AtomService, constructor injection, no hardcoded URLs.
```

---

## Option B: GitHub Copilot Plugin for IntelliJ

### Step 1: Install GitHub Copilot Plugin

1. **File → Settings → Plugins** → search "GitHub Copilot"
2. Click **Install** and restart
3. Sign in with your GitHub account (requires Copilot license)

### Step 2: Verify copilot-instructions.md is Loaded

GitHub Copilot in IntelliJ reads `.github/copilot-instructions.md` from your project root automatically.

1. Ensure you have copied the FORGE Copilot instructions:
   ```bash
   cp /path/to/forge/tool-setup/github-copilot/copilot-instructions.md \
      .github/copilot-instructions.md
   ```
2. Fill in all `[USER FILLS]` sections in the file
3. Open IntelliJ Copilot Chat and verify:
   ```
   What FORGE and ATOM rules are you applying to this project?
   ```

### Step 3: Reference Context Files in Copilot Chat

In IntelliJ Copilot Chat, paste the contents of `.context/ATOM_CHASSIS.md` into the chat for context-heavy generation tasks:
```
[paste ATOM_CHASSIS.md content]

Using these ATOM patterns, generate a circuit-breaker-wrapped HTTP client
for the Core Banking Service. Include the fallback method.
```

---

## Option C: Claude Code (Terminal) + IntelliJ

This is the most powerful setup for complex tasks like legacy analysis and full service generation.

### Step 1: Install Claude Code CLI

```bash
npm install -g @anthropic-ai/claude-code
```

### Step 2: Set Up Your Project CLAUDE.md

```bash
cp /path/to/forge/tool-setup/claude-code/CLAUDE.md ./CLAUDE.md
# Edit CLAUDE.md — fill in all [USER FILLS] sections
```

### Step 3: Run Claude Code Alongside IntelliJ

Open two windows side-by-side:
- **Left:** IntelliJ IDEA (browse, navigate, run tests, debug)
- **Right:** Terminal with Claude Code running

```bash
cd /path/to/your-project
claude
```

Claude Code automatically reads `CLAUDE.md` and the `.context/` files.

### Step 4: Typical Workflow

```
Terminal (Claude Code)                     IntelliJ IDEA
┌──────────────────────────┐               ┌──────────────────────────┐
│ > /analyze-legacy        │               │ Browse code              │
│ [paste COBOL source]     │               │ Run tests (Ctrl+Shift+F10│
│                          │  generates →  │ Debug sessions           │
│ > /generate-service      │               │ Review generated files   │
│ Service: PaymentAuthz    │               │ Navigate to usages       │
│                          │               │ Git integration          │
│ > /review-code           │               │                          │
│ Files: AuthzService.java │               │                          │
└──────────────────────────┘               └──────────────────────────┘
```

### Step 5: Available FORGE Skills in Claude Code

```
/analyze-legacy [file]       — Analyze legacy COBOL/legacy code
/extract-rules [source]      — Number and categorize business rules (BR-NNN)
/generate-service [spec]     — Generate complete ATOM service + tests
/generate-atom-service       — Scaffold new ATOM service from scratch
/review-code [files]         — FORGE + ATOM code review
/create-tests [code]         — Generate full JUnit 5 + Testcontainers suite
/map-data [copybook]         — COBOL → Java domain model + MapStruct mapper
/create-adr [decision]       — Generate Architecture Decision Record
/pre-engagement              — Run 5-task pre-engagement analysis
/explain-legacy [code]       — Plain English explanation of legacy code
/runbook [service]           — Generate operational runbook
```

Full skill catalog: [tool-setup/claude-code/SKILLS.md](../claude-code/SKILLS.md)

---

## Recommended Combined Setup for ATOM Engineers

```
IntelliJ IDEA
├── JetBrains AI Assistant — system prompt loaded (Step A3)
│   → Use for: inline completion, quick questions, short generation
├── GitHub Copilot plugin (if licensed)
│   → .github/copilot-instructions.md auto-loaded
│   → Use for: completion suggestions while typing
└── Integrated Terminal (Alt+F12)
    └── claude (Claude Code CLI)
        → CLAUDE.md + .context/ auto-loaded
        → Use for: /analyze-legacy, /generate-service, /review-code
```

**Which tool for which task:**

| Task | Use |
|---|---|
| Inline completion while coding | JetBrains AI Assistant or Copilot |
| Quick "explain this method" | AI Assistant chat |
| Analyzing a COBOL file | Claude Code: `/analyze-legacy` |
| Generating a complete ATOM service | Claude Code: `/generate-service` |
| Reviewing a PR for FORGE compliance | Claude Code: `/review-code` |
| Generating full test suite | Claude Code: `/create-tests` |
| COBOL → Java data mapping | Claude Code: `/map-data` |

---

## Prompt Templates for JetBrains AI Chat

### Analyze Legacy Code
```
I am analyzing legacy COBOL code for modernization. Apply:
- Behavior preservation: never silently change business logic
- COBOL→Java type mapping: PIC X → String, PIC 9(n)V9(m) → BigDecimal, COMP-3 → BigDecimal
- Label every claim as FACT or ASSUMPTION with confidence level (HIGH/MEDIUM/LOW)
- Number every business rule found as BR-NNN

COBOL source:
[paste COBOL program]
```

### Generate ATOM Service
```
Generate a production-ready ATOM microservice. Apply these patterns strictly:
- @AtomService on service class (not @Service)
- @Slf4j + @RequiredArgsConstructor (constructor injection)
- ResponseEntity<ApiResponse<T>> from all controller methods
- @CircuitBreaker(name="...", fallbackMethod="...") on all downstream calls
- Structured logging: log.info("msg", "key", value, "key2", value2)
- JUnit 5 + Mockito unit tests alongside the service class

Service to generate:
- Name: [service name]
- Purpose: [business function]
- Integrations: [downstream services]
- Business rules: [list key rules]
```

### Code Review
```
Review this code against FORGE and ATOM standards. Report as CRITICAL / MAJOR / MINOR.

Check for:
- @AtomService (not @Service), @AtomRepository (not @Repository)
- Controller returns ResponseEntity<ApiResponse<T>>
- @CircuitBreaker on all downstream calls with fallback method
- No hardcoded URLs, hostnames, or credentials
- No PAN, CVV, passwords in any log statement
- Structured logging with key-value pairs (not string concat)
- Domain layer has zero Spring/JPA/Kafka imports
- Constructor injection only (no @Autowired)
- Unit tests cover all business rules and error paths

Code to review:
[paste code]
```

---

## Troubleshooting

| Issue | Solution |
|---|---|
| AI ignores ATOM patterns | Paste `.context/ATOM_CHASSIS.md` directly into the chat |
| AI generates `@Service` instead of `@AtomService` | Explicitly state: "Always use @AtomService, never plain @Service" |
| Copilot suggestions don't reflect `copilot-instructions.md` | Verify file is in `.github/` directory; restart IDE |
| Claude Code doesn't load `CLAUDE.md` | Ensure you run `claude` from the project root |
| AI generates code without tests | Add: "Generate JUnit 5 + Mockito unit tests in the same response" |
| AI produces hardcoded URLs | Add: "Never hardcode service URLs — always use @Value('${...}')" |
| Domain layer gets Spring imports | Add: "The domain package must have zero dependencies on Spring, JPA, or Kafka" |

---

*FORGE v2.0 — JetBrains AI Setup Guide*
*See also: [CLAUDE.md template](../claude-code/CLAUDE.md) | [SKILLS.md](../claude-code/SKILLS.md) | [Copilot instructions](../github-copilot/copilot-instructions.md)*
