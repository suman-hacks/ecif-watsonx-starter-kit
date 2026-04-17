# Lead Engineer — FORGE Prompt Collection

---

## Prompt 1: Technical Task Breakdown

```text
You are a lead engineer breaking down user stories into technical tasks for a development team.

USER STORY
[PASTE FULL STORY WITH ACCEPTANCE CRITERIA]

ARCHITECTURE
[PASTE RELEVANT PARTS OF SERVICE DESIGN]

TECH STACK: [Java 21 / Spring Boot / Kafka / PostgreSQL / Kubernetes]

Break this story into technical tasks. For EACH task:

**Task ID:** T-[NNN]
**Title:** [imperative — "Implement AuthorizationApplicationService.authorize() method"]
**Layer:** [API / Application / Domain / Adapter / Test / Infrastructure]
**Description:** [3-5 sentences: what to build, which patterns to follow, which business rules to implement]
**Business Rules:** [BR-NNN list]
**Acceptance Criteria (technical):**
- [ ] [specific technical criterion]
**Dependencies:** [other tasks that must complete first]
**Skills required:** [Java / Spring / Kafka / DB / Security]
**Estimate:** [hours — be realistic]
**Test requirement:** [what tests must accompany this task]

Sequencing:
Phase 1 (parallel): [tasks with no dependencies]
Phase 2 (after Phase 1): [tasks dependent on Phase 1]
...

Also flag: any task where the design is ambiguous and needs clarification before coding starts.
```

---

## Prompt 2: Technical Risk Register

```text
You are a lead engineer identifying technical risks for a project.

PROJECT CONTEXT: [PASTE SUMMARY — what's being built, tech stack, timeline, team size]
ARCHITECTURE: [PASTE SERVICE MAP AND KEY DECISIONS]

Identify technical risks across these categories:

1. **Technology risks** (new/unfamiliar tech, library limitations, version conflicts)
2. **Integration risks** (external system dependencies, API contract uncertainties)
3. **Performance risks** (latency under load, database bottlenecks, event lag)
4. **Security risks** (attack surface, auth complexity, compliance gaps)
5. **Operational risks** (deployment complexity, observability gaps, rollback difficulty)
6. **Team risks** (skill gaps, knowledge silos, onboarding complexity)
7. **Legacy risks** (behavior preservation uncertainty, missing documentation)

For EACH risk:
| ID | Category | Risk | Likelihood (H/M/L) | Impact (H/M/L) | Score | Mitigation | Owner | Trigger |
|---|---|---|---|---|---|---|---|---|

Prioritize: Critical (HH), High (HM or MH), Medium (MM or HL), Low (rest).

Also produce:
- Top 3 risks that could derail the project (executive summary)
- Spike recommendations (what unknowns need investigation before committing to design)
- Escalation triggers (what events should cause the risk to be escalated to leadership)
```

---

## Prompt 3: Coding Standards Document

```text
You are a lead engineer creating a coding standards document for your team.

PROJECT CONTEXT
Language: [Java 21]
Framework: [Spring Boot 3.3]
Architecture: [Hexagonal]
Test framework: [JUnit 5, Mockito, AssertJ, Testcontainers]
Build: [Gradle 8.x]
Team size: [N engineers]
Domain: [e.g., payment processing]

Generate a coding standards document covering:

## 1. Package Structure
[Show exact package layout with description of what goes where]

## 2. Naming Conventions
[Classes, methods, variables, constants, packages, test classes — be specific]

## 3. Dependency Injection
[Constructor injection only — show example of correct and incorrect]

## 4. Error Handling
[Domain exception hierarchy, when to catch vs propagate, error codes]

## 5. Logging Standards
[Framework, required MDC fields, what to log at each level, what NEVER to log]

## 6. Testing Standards
[Test class naming, method naming (given_when_then), AAA structure, mocking rules, what to assert]

## 7. Git Workflow
[Branch naming, commit message format, PR requirements, merge strategy]

## 8. Code Review Standards
[What reviewers check, turnaround SLA, what blocks merge]

## 9. Domain Language Rules
[Use business terms, avoid COBOL variable names, approved glossary]

## 10. What We Never Do
[Explicit anti-patterns banned on this project — with examples of wrong vs right]

Format as a practical reference document the team can refer to daily.
```
