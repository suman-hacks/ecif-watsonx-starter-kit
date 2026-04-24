# FORGE Personas — Role-Specific Prompt Packs

Each persona gets AI prompts calibrated to their responsibilities, combined with FORGE guardrails and ATOM patterns. Load your persona prompts alongside `.context/CORE_SKILLS.md` and `.context/ATOM_CHASSIS.md` for best results.

---

## Available Personas

| Persona | What They Do with FORGE + AI | Directory |
|---|---|---|
| **Developer** | ATOM service generation, COBOL analysis, code review, test generation | [developer/](developer/) |
| **Solution Architect** | Service decomposition, ADR generation, API design, migration strategy | [solution-architect/](solution-architect/) |
| **Business Analyst** | Business rule extraction, user story generation, process mapping | [business-analyst/](business-analyst/) |
| **QA Engineer** | Test strategy, test generation, parallel-run testing, UAT scenarios | [qa-engineer/](qa-engineer/) |
| **DevSecOps** | Threat modeling, security review, IaC generation, compliance checks | [devsecops/](devsecops/) |
| **Product Manager** | Requirements, roadmap communication, executive summaries | [product-manager/](product-manager/) |
| **Product Owner** | Acceptance criteria, story refinement, backlog management | [product-owner/](product-owner/) |
| **Lead Engineer** | Review gates, ADR governance, team workflow setup, technical mentoring | [lead-engineer/](lead-engineer/) |
| **Project Manager** | Status reporting, risk tracking, delivery planning | [project-manager/](project-manager/) |
| **UAT / Business Readiness** | UAT test scenarios, sign-off checklists, business acceptance | [uat-business-readiness/](uat-business-readiness/) |

---

## How to Use Your Persona Prompts

### In Claude Code
```bash
claude
# Then pick a prompt from your persona file and paste it, or use a FORGE skill:
/analyze-legacy    # Developer / Architect
/extract-rules     # Business Analyst
/create-tests      # QA Engineer
/review-code       # Lead Engineer / DevSecOps
```

### In GitHub Copilot Chat (VS Code or IntelliJ)
```
# Reference your context files and persona:
Using #file:.context/ATOM_CHASSIS.md, I am a [PERSONA] working on [TASK].
[Paste the prompt from your persona prompts.md file]
```

### In JetBrains AI Assistant
```
I am a [PERSONA] on a Java 17 / Spring Boot 3.x ATOM project.
Apply .context/CORE_SKILLS.md guardrails and .context/ATOM_CHASSIS.md patterns.
[Paste the prompt from your persona prompts.md file]
```

### In the Web Portal
Open `web-ui/index.html` and navigate to your persona section in the left sidebar.

---

## Combining Personas

Engineers often wear multiple hats. Combine persona prompts freely:

- **Lead Engineer + Developer:** Use lead engineer review gates to review your own code before PR
- **Developer + Business Analyst:** Use BA rule extraction prompts when analyzing legacy code you're also modernizing
- **Architect + Developer:** Use architecture prompts for ADRs, then developer prompts for implementation

---

## Adding Your Context to Every Persona Session

Start every AI session with the FORGE first-session template from [QUICK-START.md](../QUICK-START.md#first-session-template), then add your persona-specific prompts.

The non-negotiable preamble:
```
I am a [PERSONA] working on [PROJECT NAME].
Apply rules from: .context/CORE_SKILLS.md and .context/ATOM_CHASSIS.md.
Current SDLC stage: [Stage N].
Task: [describe your task]
```
