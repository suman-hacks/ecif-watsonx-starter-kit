# Stage 0: Pre-Engagement Analysis

> **AI-Accelerated Discovery — Run this before any modernization workshop.**

---

## What This Is

Pre-engagement analysis uses AI to analyze a legacy codebase and produce a set of structured discovery documents **before** a workshop or modernization kickoff. Instead of spending the first workshop session drawing architecture diagrams on a whiteboard, you walk in with evidence-based documents the team can react to, correct, and build on.

The result: workshops that are faster, sharper, and grounded in what the code actually does — not what people remember it doing.

---

## The 5-Task Workflow

| Task | Output File | Who Runs It | Time |
|---|---|---|---|
| **T1** — System Architecture Analysis | `TX_ARCH.md` | Lead Engineer / Architect | 5–10 min |
| **T2** — Decision Logic Inventory | `TX_DECISION_INVENTORY.md` | Same engineer, same repo | 10–15 min |
| **T3** — Integration & Latency Map | `TX_INTEGRATIONS.md` | Same engineer | 5–10 min |
| **T4** — Complexity & Risk Map | `TX_RISK_MAP.md` | Same engineer | 5–10 min |
| **T5** — POC Option Analysis | `TX_POC_OPTIONS.md` | Same or tech lead | 10–15 min |

**Run T5 last.** It requires the outputs from T1–T4 as input.

Total time: approximately 35–60 minutes for one engineer, producing a complete pre-workshop picture of the system.

---

## How to Run Each Task

### Step 1: Open Your AI Tool in the Repository Root

| Tool | How to Open |
|---|---|
| **Claude Code** | `cd your-repo && claude` — full codebase access automatically |
| **GitHub Copilot (VS Code)** | Open the repo in VS Code, use `@workspace` prefix in Copilot Chat |
| **Cursor** | Open repo in Cursor, use `@codebase` in Composer |
| **watsonx Code Assist** | Open key entry-point files first, then paste prompt |
| **Claude.ai / other web tools** | Paste the prompt into chat; reference files by pasting their content |

### Step 2: Copy the Prompt for Each Task

Open the prompt file for each task:
- [T1-system-architecture-analysis.md](T1-system-architecture-analysis.md)
- [T2-decision-logic-inventory.md](T2-decision-logic-inventory.md)
- [T3-integration-and-latency-map.md](T3-integration-and-latency-map.md)
- [T4-complexity-and-risk-map.md](T4-complexity-and-risk-map.md)
- [T5-poc-option-analysis.md](T5-poc-option-analysis.md)

Copy the prompt text, paste into your AI tool, and run.

Or use the FORGE web portal: open `web-ui/index.html` → **Pre-Engagement Analysis** → click **Copy Prompt**.

Or use the Claude Code skill: `/pre-engagement`

### Step 3: Review and Annotate the Output

After receiving each AI output:
1. Read it carefully against what you know about the system
2. Mark any section where the AI was wrong or incomplete: `[ENGINEER NOTE: actual behavior is X]`
3. Add any mainframe context the AI couldn't see in the distributed code
4. Answer any `[UNKNOWN — needs investigation]` items you can resolve from memory

**The AI analysis is a starting point, not the final answer.** Your engineering judgment is the final word.

### Step 4: Save Outputs to `pre-engagement/` Folder

```
your-project-repo/
└── pre-engagement/
    ├── TX_ARCH.md
    ├── TX_DECISION_INVENTORY.md
    ├── TX_INTEGRATIONS.md
    ├── TX_RISK_MAP.md
    └── TX_POC_OPTIONS.md
```

### Step 5: Share with Workshop Facilitators

Share the completed files before the workshop. Facilitators use them to:
- Anchor the "Current State" discussion to real code evidence
- Identify which areas need deeper investigation in the session
- Propose concrete POC options (from T5) for the group to react to

---

## If the System Spans Multiple Repositories

Run T1–T3 in each repository separately. Then consolidate:

```
You have run pre-engagement analysis on multiple repositories that make up our system.
The output files are: [list TX_ARCH.md files from each repo]

Produce CONSOLIDATED_TX_AUTH_VIEW.md that:
1. Merges into one unified end-to-end flow diagram
2. Shows which repo/service owns which step in the flow
3. Consolidates the integration inventory (deduplicating cross-repo calls)
4. Identifies flows where a single transaction decision spans multiple repos
5. Highlights where you would draw a POC boundary

Resolve contradictions between analyses and flag them explicitly.
```

---

## If Significant Logic Lives on the Mainframe

When reviewing output files, annotate anywhere the analysis is missing mainframe-hosted logic:

```
[MAINFRAME GAP]: The authorization engine calls CICS transaction AUTHZ001 at this step.
This transaction contains [known behavior from team knowledge]. The COBOL source is in
the mainframe repo at [path] and has not been analyzed in this pass.
```

Even a few bullet points describing what lives on the mainframe will significantly improve workshop outcomes.

---

## Workshop Connection

Each task maps directly to a workshop session:

| Task | Workshop Segment |
|---|---|
| T1 — Architecture | "Current State Overview" — project the diagram, skip the whiteboard |
| T2 — Decision Logic | "Key Problems to Solve" — point to specific decision locations as pain points |
| T3 — Integrations | "Application Complexity & Boundaries" — scope POC boundaries realistically |
| T4 — Risk Map | "Define POC Scope" — avoid touching high-risk areas in the initial POC |
| T5 — POC Options | "Candidate POC Scenarios" — give the room concrete proposals to react to |

---

## Completion Checklist

Before the workshop, confirm:

**Output Files**
- [ ] `TX_ARCH.md` — generated and reviewed by engineer for accuracy
- [ ] `TX_DECISION_INVENTORY.md` — generated and reviewed for accuracy
- [ ] `TX_INTEGRATIONS.md` — generated and reviewed for accuracy
- [ ] `TX_RISK_MAP.md` — generated and reviewed for accuracy
- [ ] `TX_POC_OPTIONS.md` — generated using T1–T4 outputs and reviewed
- [ ] `CONSOLIDATED_TX_AUTH_VIEW.md` — generated if multi-repo

**Engineer Review**
- [ ] Architecture diagram accurately reflects the real flow (no hallucinated services)
- [ ] Decision logic inventory captures the primary decision locations (no major blind spots)
- [ ] Integration list is complete — no critical dependencies missing
- [ ] Mainframe components annotated where applicable
- [ ] T5 POC options are realistic — flagged any that are off-base

**Redaction**
- [ ] All files reviewed — no production credentials, real customer data, or confidential IP
- [ ] Files are safe to share with workshop facilitators and external participants

---

*FORGE Stage 0 — Pre-Engagement Analysis*
*Full prompt details: see individual T1–T5 files in this directory.*
*Web portal access: open `web-ui/index.html` → Pre-Engagement Analysis*
