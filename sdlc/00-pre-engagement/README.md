# Stage 00: Pre-Engagement Analysis

## Purpose

Pre-Engagement is the AI-assisted codebase analysis phase conducted **before** a discovery workshop or POC scoping session. Its goal is to give workshop facilitators and technical leads a factual, evidence-based picture of the current system so that workshop time is spent on decisions — not on discovering things the codebase could have told you.

This stage transforms raw source code into five structured analysis documents that anchor every conversation in reality rather than assumption.

## When to Use

- Before a POC scoping workshop
- Before a modernization or re-platforming assessment
- Before an AI/ML feasibility study (e.g., "can we add intelligent decisioning here?")
- Before a vendor or partner technical deep-dive
- When onboarding a new team onto an existing system

## Who Runs It

| Role | Responsibility |
|---|---|
| Lead Engineer / Architect | Runs T1–T4 against the codebase |
| Business Analyst (optional) | Reviews T5 POC options for business alignment |
| Workshop Facilitator | Receives all five documents before the workshop |

**Time commitment:** Each task takes 5–10 minutes to run (prompt + review output). Full pre-engagement analysis = 30–60 minutes.

## Output Files Produced

All outputs are placed in a `pre-engagement/` folder and shared with the workshop facilitator at least 24 hours before the session.

| File | Task | Contents |
|---|---|---|
| `TX_ARCH.md` | T1 | System architecture, tech stack, data flow, ASCII diagram |
| `TX_DECISIONS.md` | T2 | Decision logic inventory, rules, hardcoded vs configurable |
| `TX_INTEGRATIONS.md` | T3 | Integration map, latency budget, stand-in behavior |
| `TX_RISK_MAP.md` | T4 | Complexity hotspots, test coverage, regression risk |
| `TX_POC_OPTIONS.md` | T5 | POC options with effort estimates and recommendation |

Replace `TX` with your transaction or project code (e.g., `AUTH_ARCH.md`, `PMT_DECISIONS.md`).

## Process

```
1. Clone / access the relevant codebase
2. Open your AI coding assistant (GitHub Copilot, Claude Code, watsonx Code Assist, Cursor)
3. Open Task T1 → copy the prompt → paste into your assistant with codebase context
4. Review the output: correct anything obviously wrong
5. Save output as TX_ARCH.md
6. Repeat for T2, T3, T4
7. Run T5 last — it synthesizes T1–T4
8. Share all five documents with workshop facilitator
9. Bring to workshop as shared reference
```

## Workshop Connection

The five documents feed directly into the workshop agenda:

- **T1 ARCH** → Used in "Understand the Current State" segment
- **T2 DECISIONS** → Used in "Where Could AI Add Value?" segment
- **T3 INTEGRATIONS** → Used in "What Are the Integration Constraints?" segment
- **T4 RISK MAP** → Used in "What Could Go Wrong?" segment
- **T5 POC OPTIONS** → Used in "Which POC Should We Run?" decision segment

The workshop facilitator should read all five documents before the session and prepare probing questions based on the unknowns and gaps identified.

## Note About Mainframe and Non-Repo Systems

If COBOL, CICS, JCL, or other mainframe code exists but is **not** in the repository being analyzed:

1. Annotate each output file with a `[MAINFRAME GAP]` marker where the analysis is incomplete
2. Document what is known about the mainframe component from documentation, comments, or team knowledge
3. Add a dedicated "Mainframe Integration Points" section to T3 INTEGRATIONS even if based on partial information
4. Flag this in T4 RISK MAP as a high-risk unknown
5. T5 POC OPTIONS should include a mainframe access path as a data/system requirement

Similarly, if systems exist behind APIs with no source access (SaaS platforms, vendor black boxes), mark these as `[BLACK BOX]` and document what is observable from the integration layer.

## Tool-Specific Notes

| Tool | Guidance |
|---|---|
| **GitHub Copilot** | Use Copilot Chat with workspace context enabled. Type `@workspace` before pasting the prompt to ground it in your codebase. |
| **Claude Code** | Claude Code automatically indexes the codebase. Paste the prompt directly. Use `--dangerously-skip-permissions` only in isolated analysis environments. |
| **watsonx Code Assist** | Use the chat interface with the project files open. Reference specific file paths in the prompt for best results. |
| **Cursor** | Use Cursor's Composer with codebase indexing enabled. The `@codebase` reference will ground the analysis. |

## Quality Bar

A pre-engagement package is ready when:
- [ ] All five output documents exist
- [ ] Each document has fewer than 5 `[UNKNOWN]` markers (or all unknowns are explicitly explained)
- [ ] The ASCII architecture diagram in T1 matches what the team knows to be true
- [ ] At least 3 POC options are documented in T5 with effort estimates
- [ ] The workshop facilitator has confirmed receipt and has no blocking questions

---

*FORGE Framework — Stage 00: Pre-Engagement | Next Stage: 01-Discovery*
