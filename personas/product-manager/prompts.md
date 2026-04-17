# FORGE Prompts — Product Manager

These prompts are designed for use with GitHub Copilot Chat, Claude Code, watsonx Code Assist, or Cursor. Copy the prompt text block, replace all `[PLACEHOLDER]` values with your actual content, and paste into your AI tool.

---

## Prompt 1: PRD Generation

### PRD Generation
**When to use:** Starting a new feature, product initiative, or modernization workstream. Use after discovery sessions when you have business objectives and user research but need to produce a structured requirements document.

**Inputs needed:**
- Business objectives (1–5 sentences)
- Discovery findings or customer research notes
- Known constraints (budget, timeline, technology, regulatory)
- Target user personas (names/roles are sufficient)
- Any existing system context (what's being replaced or enhanced)

**Output:** A complete Product Requirements Document with all standard sections, ready for review by engineering, architecture, and business stakeholders.

**Tool notes:** In Claude Code or Copilot Chat, paste discovery notes directly after the prompt for richer output. In watsonx Code Assist, use document mode if available.

**Prompt:**
```text
You are a senior Product Manager writing a Product Requirements Document (PRD) for an enterprise software delivery initiative.

CONTEXT:
- Product/Initiative Name: [PRODUCT OR INITIATIVE NAME]
- Business Objectives: [LIST 3-5 BUSINESS OBJECTIVES]
- Discovery Findings: [PASTE DISCOVERY NOTES, CUSTOMER QUOTES, OR RESEARCH SUMMARIES]
- Target Users / Personas: [LIST USER ROLES OR PERSONAS, e.g., "Loan Officer, Branch Manager, Compliance Officer"]
- Known Constraints: [BUDGET CEILING, TECHNOLOGY CONSTRAINTS, REGULATORY REQUIREMENTS, DEADLINE]
- What is being replaced or enhanced: [DESCRIBE CURRENT STATE SYSTEM OR PROCESS]

INSTRUCTIONS:
Write a complete, professional Product Requirements Document with the following sections. Use clear headings. Be specific — avoid vague language like "improved performance" and instead write measurable statements.

1. EXECUTIVE SUMMARY (3-4 sentences: what are we building, why, and what outcome do we expect)

2. PROBLEM STATEMENT
   - Current state description (what problem exists today)
   - Impact of the problem (quantify where possible: cost, time, error rate, customer impact)
   - Who is most affected

3. USER PERSONAS
   For each persona: name/role, primary goals, current pain points, success criteria (what does "done" look like for them)

4. GOALS AND NON-GOALS
   - In Scope: what this initiative WILL deliver (use "Will" statements)
   - Out of Scope: what this initiative WILL NOT deliver (use "Will not" statements — be explicit to prevent scope creep)

5. FEATURE LIST (PRIORITIZED)
   Present as a table: Feature Name | Priority (P1/P2/P3) | Description | User Persona(s) Served | Acceptance Signal
   P1 = Must have for launch. P2 = Important, can follow. P3 = Nice to have.

6. SUCCESS METRICS
   For each goal, define 2-3 measurable KPIs. Format: Metric | Baseline | Target | Measurement Method | Review Cadence

7. ASSUMPTIONS AND DEPENDENCIES
   - Assumptions (things we believe to be true that we haven't verified)
   - Dependencies (things that must happen before or alongside delivery)

8. TIMELINE AND MILESTONES
   High-level milestones only (not sprint tasks). Format: Milestone | Target Date | Owner | Success Criteria

9. RISKS AND OPEN QUESTIONS
   - Top 5 risks with: Risk | Likelihood (H/M/L) | Impact (H/M/L) | Mitigation Approach
   - Open questions that need resolution before build begins (numbered list)

10. APPENDIX
    - Glossary of domain-specific terms used in this document
    - References to related documents or prior research

FORMAT REQUIREMENTS:
- Use Markdown formatting with clear section headers
- Write in third person professional tone
- Every feature and metric must be specific and testable
- Flag any section where you have made assumptions due to insufficient input — mark these with [ASSUMPTION: reason]
- Total length: 800–1500 words depending on initiative complexity
```

**Review checklist:**
- [ ] Every feature in the feature list has a clear acceptance signal
- [ ] All metrics have a baseline, target, and measurement method (not just "improve X")
- [ ] Non-goals explicitly call out the most likely scope creep risks
- [ ] Risks have owner assignments added by you (AI will not know your team)
- [ ] Personas reflect real users you've spoken to, not generic descriptions
- [ ] Open questions have been assigned to named individuals for resolution
- [ ] Remove all `[ASSUMPTION:]` flags before distributing — either confirm or research each

---

## Prompt 2: OKR Creation

### OKR Creation
**When to use:** Quarterly planning cycles, initiative kickoffs, or when business objectives need to be translated into measurable team outcomes. Use after executive strategy sessions or after a PRD is approved.

**Inputs needed:**
- Business-level objectives (from leadership or strategy documents)
- Product area or initiative scope
- Current performance baselines (optional but improves output quality)
- Time horizon (quarter, half-year, annual)

**Output:** A structured OKR framework with 3–5 Objectives and 3–5 Key Results per Objective, plus scoring guidance.

**Tool notes:** Works well in any AI tool. For Claude Code, you can paste a strategy document and ask the AI to extract objectives first before running this prompt.

**Prompt:**
```text
You are a product strategy expert helping a Product Manager create OKRs (Objectives and Key Results) for a software product initiative.

CONTEXT:
- Company/Division: [COMPANY OR DIVISION NAME]
- Product Area: [PRODUCT OR FEATURE AREA]
- Business Objectives from Leadership: [PASTE RAW BUSINESS OBJECTIVES OR STRATEGY STATEMENT]
- Time Horizon: [Q1 2025 / H1 2025 / FY2025]
- Current Performance Baselines (if known): [E.g., "Current customer satisfaction score: 3.2/5", "Current processing time: 4 days", "Current error rate: 8%"]
- Team Size and Constraints: [NUMBER OF ENGINEERS, ANY KNOWN DELIVERY CONSTRAINTS]

INSTRUCTIONS:
Create a complete OKR framework following Google's OKR methodology. Apply the following rules strictly:

OBJECTIVE rules:
- Qualitative, inspirational, directional — NOT a metric
- Answers "What do we want to achieve?"
- Should stretch the team but be achievable
- Maximum 5 objectives per cycle (recommend 3–4)

KEY RESULT rules:
- Quantitative and measurable — ALWAYS includes a number
- Answers "How will we know we achieved the objective?"
- 3–5 key results per objective
- Must be outcomes, NOT activities or tasks (e.g., NOT "launch feature X" — instead "reduce customer task completion time from 10 minutes to 3 minutes by launching feature X")
- Scored 0.0–1.0 at cycle end; target is 0.7 (hitting 1.0 means the target was too easy)

OUTPUT FORMAT:
For each Objective:

**Objective [N]: [Objective statement]**
Why this matters: [1-2 sentence rationale connecting to business strategy]

| KR | Key Result Statement | Baseline | Target | Data Source | Owner |
|----|---------------------|----------|--------|-------------|-------|
| KR1 | [Measurable outcome] | [Current value] | [Target value] | [Where to get the data] | [Role — PM/Eng/Design] |
| KR2 | ... | | | | |
| KR3 | ... | | | | |

After all OKRs, provide:

DEPENDENCIES AND RISKS:
- List any cross-team dependencies these OKRs create
- Flag any KRs where baseline data is not currently available and recommend how to establish it

ANTI-PATTERNS TO AVOID IN THIS OKR SET:
- Identify any of the drafted KRs that are outputs/tasks rather than outcomes and suggest rewrites

SCORING CALENDAR:
- Recommend check-in dates and what to review at each

Write [DATA NEEDED] in the Baseline column for any KR where I haven't provided the baseline — do not fabricate numbers.
```

**Review checklist:**
- [ ] No Key Result is an activity or task (no "complete X", "launch Y", "deliver Z" without an outcome measure)
- [ ] Every KR has a number — no qualitative KRs
- [ ] Baselines are real data, not AI-generated estimates — fill in `[DATA NEEDED]` fields before publishing
- [ ] 0.7 scoring targets are genuinely stretching (if the team could easily hit 1.0, recalibrate)
- [ ] Owner column has been filled in with actual people or teams by you
- [ ] OKRs have been reviewed with the engineering lead for feasibility

---

## Prompt 3: Competitive Analysis

### Competitive Analysis
**When to use:** Evaluating market position, preparing for a product review, deciding on feature investments, or assessing a modernization approach against market alternatives.

**Inputs needed:**
- Your product or solution description
- Names of known competitors or alternatives (at minimum 2–3)
- Evaluation dimensions you care about (or let AI suggest them)
- Target customer segment

**Output:** A structured competitive analysis framework with comparison matrix, strategic implications, and positioning recommendations.

**Tool notes:** AI tools do not have real-time market data. Use this prompt to generate the framework and structure, then fill in competitor data from your own research, analyst reports, or product trials. Claude Code handles longer analysis documents well.

**Prompt:**
```text
You are a product strategy consultant conducting a competitive analysis for an enterprise software product.

CONTEXT:
- Our Product/Solution: [DESCRIBE YOUR PRODUCT — what it does, who it serves, key differentiators you believe you have]
- Market Segment: [E.g., "enterprise banking core modernization", "supply chain visibility for mid-market manufacturers"]
- Known Competitors or Alternatives: [LIST COMPETITOR NAMES — if unknown, list "unknown" and AI will suggest a framework]
- Evaluation Dimensions I care about: [E.g., "total cost of ownership, integration complexity, time to deploy, vendor support quality, AI capabilities" — or write "suggest based on market segment"]
- Our Strategic Goal: [E.g., "win deals against Competitor X", "justify build vs buy decision", "identify gaps before a pricing review"]

INSTRUCTIONS:
Produce a complete competitive analysis document with the following sections:

1. COMPETITIVE LANDSCAPE OVERVIEW
   - Market segment summary (2-3 sentences)
   - Category of competition (direct, indirect, substitutes, status quo)
   - Classify each competitor by type

2. EVALUATION DIMENSIONS
   List 8–12 dimensions relevant to this market segment. For each:
   - Dimension name
   - Why it matters to the buyer
   - How to score it (qualitative or quantitative scale)

3. COMPARISON MATRIX
   Present as a table: Dimension | Our Product | [Competitor 1] | [Competitor 2] | [Competitor 3]
   Use: ++ (strong advantage) | + (advantage) | = (parity) | - (disadvantage) | -- (significant gap) | ? (unknown — requires research)
   Mark all cells as ? where competitor data has not been provided — do NOT fabricate competitor capabilities.

4. STRENGTHS AND WEAKNESSES SUMMARY
   For each competitor: 3 strengths, 3 weaknesses, 1 "watch out" (something they're likely investing in)

5. STRATEGIC GAPS AND OPPORTUNITIES
   - Where does our product have clear white space?
   - Where are we most vulnerable?
   - Which competitor is most dangerous to us and why?

6. POSITIONING RECOMMENDATIONS
   - 3 positioning statements we can use (based on genuine differentiators)
   - Features or capabilities to invest in to close the most critical gaps
   - Features NOT worth investing in because competitors have unassailable leads

7. RESEARCH TODO LIST
   List every cell marked ? and suggest where to find the data (vendor website, G2/Gartner, trial, customer interview, analyst report)

FORMAT: Professional Markdown with tables. Be direct and opinionated in the strategic sections — avoid hedged language like "it could potentially be considered."
```

**Review checklist:**
- [ ] Every `?` cell has been researched and filled in before presenting
- [ ] No AI-fabricated competitor capabilities have been left in the document
- [ ] Positioning statements have been validated against actual customer language
- [ ] Strategic recommendations have been reviewed with the sales or business development team
- [ ] Gap analysis is prioritized (not all gaps are equally important)
- [ ] Document has a version date — competitive analysis goes stale quickly

---

## Prompt 4: Release Notes

### Release Notes
**When to use:** Preparing for a product release, sprint demo, or milestone delivery. Use when you need to communicate changes to multiple audiences with different technical literacy levels.

**Inputs needed:**
- List of changes, features, bug fixes, and improvements in this release
- Version number and release date
- Known issues or limitations
- Target audiences (technical team, business stakeholders, end users)

**Output:** Three versions of release notes (technical, business, end-user) plus a headline announcement draft.

**Tool notes:** Works in all tools. For GitHub Copilot, pull in your PR/commit descriptions as input for more accurate technical notes.

**Prompt:**
```text
You are a product communications specialist writing release notes for a software release.

RELEASE INFORMATION:
- Product Name: [PRODUCT NAME]
- Version / Release Name: [e.g., "v2.4.0" or "April 2025 Release"]
- Release Date: [DATE]
- Release Type: [Major / Minor / Patch / Hotfix]

CHANGES IN THIS RELEASE:
New Features:
[LIST EACH NEW FEATURE — what it is, what it replaces or enables]

Enhancements to Existing Features:
[LIST IMPROVEMENTS — what was changed and the user-visible impact]

Bug Fixes:
[LIST BUGS FIXED — describe the original problem in user terms, not internal ticket IDs]

Deprecated or Removed:
[LIST ANYTHING REMOVED OR SCHEDULED FOR REMOVAL]

Known Issues / Limitations:
[LIST KNOWN ISSUES AND ANY WORKAROUNDS]

AUDIENCE CONTEXT:
- Technical Team: [Engineers, DevOps, architects — want detail on what changed and why]
- Business Stakeholders: [Managers, PMs, executives — want to understand business value and impact]
- End Users: [The people who use the system — want to know what's new and how to use it]

INSTRUCTIONS:
Write three distinct versions of release notes. Each version must be written in language appropriate for that audience — do not simply copy-paste between versions.

VERSION 1 — TECHNICAL RELEASE NOTES
Format: Standard technical changelog
- Version header with date and release type
- Breaking changes section (if any) at the top, highlighted clearly
- New features with: feature name, technical description, configuration or deployment notes, API changes if relevant
- Bug fixes with: original behavior, fix applied, affected components
- Dependencies updated
- Upgrade instructions or migration notes

VERSION 2 — BUSINESS STAKEHOLDER SUMMARY
Format: Executive-style summary (max 1 page)
- What was delivered in this release (in business outcomes, not technical features)
- What business problems this solves
- Metrics or KPIs this release moves
- What's coming next (optional teaser)
- Any action required from business stakeholders

VERSION 3 — END USER RELEASE NOTES
Format: Friendly, plain-language announcement
- Lead with the most impactful improvement for the user
- Step-by-step guidance for any changes to how they do their work
- "What's new" section with short, benefit-led descriptions
- "What's changed" section for anything that affects existing workflows
- Where to get help

BONUS — RELEASE ANNOUNCEMENT (for Slack/Teams/Email)
Write a 3-sentence announcement suitable for posting in a general channel:
- What's new
- When it's available
- Where to learn more

Use positive, active-voice language throughout. Do not use internal ticket IDs in user-facing content.
```

**Review checklist:**
- [ ] Technical notes include all breaking changes clearly flagged
- [ ] Business version contains no jargon (have a non-technical colleague read it)
- [ ] End-user version includes steps for any workflow that changed
- [ ] Known issues are not hidden or downplayed
- [ ] Upgrade/migration instructions are complete and tested
- [ ] Release announcement has been reviewed by comms or leadership before posting

---

## Prompt 5: Stakeholder Communication

### Stakeholder Communication
**When to use:** After receiving technical findings (risk assessments, architecture reviews, pre-engagement analysis, security reports) that need to be communicated to non-technical executive stakeholders. Use when you need to translate complex technical content into business language.

**Inputs needed:**
- The technical document or findings to translate (paste full text or key excerpts)
- Audience description (who will read this, what decisions they need to make)
- Recommended actions you want to propose
- Any political or organizational context that affects tone

**Output:** An executive summary suitable for a board update, steering committee, or senior leadership email.

**Tool notes:** Claude Code handles large documents best for this prompt. Paste the full technical report for best results. In Copilot Chat, use `@workspace` if the technical report is in the repo.

**Prompt:**
```text
You are a product manager translating a technical assessment into an executive communication for senior non-technical stakeholders.

AUDIENCE:
- Who will read this: [e.g., "CIO, CFO, and Head of Operations — none have software engineering backgrounds"]
- Decision(s) they need to make: [e.g., "Approve Phase 2 budget", "Decide on vendor selection", "Authorize go/no-go for migration"]
- Their primary concern: [e.g., "Cost and risk", "Timeline to delivery", "Compliance and audit readiness"]

TECHNICAL FINDINGS (paste below):
[PASTE THE FULL TECHNICAL REPORT, RISK ASSESSMENT, ARCHITECTURE REVIEW, OR KEY FINDINGS]

RECOMMENDED ACTIONS I WANT TO PROPOSE:
[LIST YOUR 3-5 RECOMMENDED NEXT STEPS OR DECISIONS NEEDED]

INSTRUCTIONS:
Write a professional executive summary document with the following structure. Apply these translation rules strictly:
- Replace all technical terms with business equivalents (e.g., "legacy monolith" → "current system", "microservices" → "modular architecture", "technical debt" → "accumulated maintenance cost")
- Lead with business impact, not technical detail
- Quantify risk in business terms (cost, time, compliance exposure) — not in technical severity ratings
- Every recommended action must state: what, why, by when, and who decides

DOCUMENT STRUCTURE:

EXECUTIVE SUMMARY (4-6 sentences)
What was assessed, what was found, what is the business significance, what action is recommended.

SITUATION OVERVIEW
What triggered this assessment and what was the scope? (2-3 sentences, no technical detail)

KEY FINDINGS (present as 3-5 bullet points)
Each finding must follow this format:
- [Business-language finding]: [Business impact in dollars, days, risk level, or compliance consequence]
Do not include technical implementation details in this section.

RISK SUMMARY TABLE
| Risk | Business Impact | Likelihood | Timeline if Unaddressed | Recommended Action |
|------|----------------|------------|------------------------|-------------------|
[3–5 most important risks only — translate from technical risk list]

RECOMMENDED ACTIONS
For each action:
- Action: [What to do]
- Business Rationale: [Why this protects or advances the business]
- Decision Required By: [Date]
- Decision Owner: [Role]
- Estimated Investment: [Leave blank — you will fill in]
- Expected Outcome: [What success looks like]

NEXT STEPS
A numbered list of immediate next steps, each with owner and date.

APPENDIX NOTE
State that the full technical assessment is available upon request and name the technical lead who can present it.

Tone: Professional, direct, confident. Avoid hedging language. Avoid alarming language — present risks with mitigations. Length: 400–600 words excluding tables.
```

**Review checklist:**
- [ ] Zero technical acronyms or jargon remain in the document
- [ ] Every risk has a stated business impact (not just a severity level)
- [ ] Recommended actions are specific enough to act on (not "investigate further")
- [ ] Decision owners are named individuals, not teams or roles
- [ ] Document has been reviewed by the technical lead to confirm accuracy
- [ ] Tone is appropriate for your specific organizational culture (adjust if needed)

---

## Prompt 6: Roadmap Prioritization

### Roadmap Prioritization
**When to use:** Quarterly roadmap planning, backlog grooming sessions, or when stakeholders are debating feature priorities. Use when you have a backlog of features or initiatives that need to be ranked and scheduled.

**Inputs needed:**
- List of features/initiatives to prioritize (minimum 5, can handle 30+)
- Any available data: user demand, revenue impact, effort estimates
- Strategic themes or focus areas for the period

**Output:** Features ranked using RICE and MoSCoW frameworks with rationale, plus a suggested roadmap structure.

**Tool notes:** Works well in all tools. For large backlogs (20+ items), Claude Code handles the analysis better. GitHub Copilot is effective if the backlog is in a markdown or YAML file in the repo.

**Prompt:**
```text
You are a product strategy expert helping a Product Manager prioritize a feature backlog using structured frameworks.

PRODUCT CONTEXT:
- Product: [PRODUCT NAME]
- Planning Period: [Q1 2025 / H1 2025]
- Strategic Themes for This Period: [LIST 2-3 STRATEGIC THEMES, e.g., "Reduce operational cost", "Improve onboarding experience", "Achieve PCI compliance"]
- Team Capacity: [APPROXIMATE SPRINT COUNT OR STORY POINTS AVAILABLE]

BACKLOG ITEMS (copy/paste your list):
[LIST EACH FEATURE/INITIATIVE — one per line. Include any data you have: user requests count, revenue impact, rough effort estimate, strategic theme]

Example format (adapt to what you have):
1. Feature: Automated loan statement generation | User requests: 47 | Revenue impact: unknown | Effort: ~2 sprints | Theme: Reduce operational cost
2. Feature: Dark mode UI | User requests: 312 | Revenue impact: none | Effort: ~1 sprint | Theme: none
...

INSTRUCTIONS:
Apply two prioritization frameworks to this backlog.

FRAMEWORK 1 — RICE SCORING
For each item, calculate a RICE score:
- Reach: How many users affected per quarter? (scale: 1-10 if no data, note assumption)
- Impact: How much does it move a key metric? (1=minimal, 2=low, 3=medium, 5=high, 10=massive)
- Confidence: How confident are we in the reach and impact estimates? (20%/50%/80%/100%)
- Effort: How many person-months to deliver? (lower effort = higher score)
- RICE Score = (Reach × Impact × Confidence) / Effort

Present as a table sorted by RICE score descending:
| Rank | Feature | Reach | Impact | Confidence | Effort | RICE Score | Notes |

FRAMEWORK 2 — MoSCoW CATEGORIZATION
Categorize each item:
- Must Have: Critical for this period — without it, the release/quarter fails
- Should Have: High value, include if capacity allows
- Could Have: Nice to have, include only if ahead of schedule
- Won't Have (This Period): Deliberately deferred

RECONCILED RECOMMENDATION
After both frameworks:
1. Identify any conflicts between RICE and MoSCoW rankings — explain the discrepancy
2. Propose a final prioritized list for the planning period
3. Map Must Haves to sprints/milestones at a high level
4. Call out items that should be split or further refined before committing

STAKEHOLDER TRADEOFF TABLE
Identify the 3 most contentious tradeoffs (high-request items that score low on RICE, or strategic items with very low confidence). For each:
- Feature A vs Feature B (or C): what is the tradeoff?
- Data needed to resolve the decision
- Recommendation

Where data is missing, use [ESTIMATE — VALIDATE] and note what to research.
```

**Review checklist:**
- [ ] All `[ESTIMATE — VALIDATE]` flags have been researched before presenting
- [ ] RICE scores have been reviewed with the engineering lead for effort accuracy
- [ ] Stakeholder tradeoff decisions have explicit owners
- [ ] Must Haves are genuinely must-haves — challenge any item that could survive a quarter without it
- [ ] Won't Have items have been communicated to stakeholders who requested them
- [ ] Final prioritized list has been reviewed against team capacity

---

## Prompt 7: ROI Analysis

### ROI Analysis
**When to use:** Building a business case for a modernization initiative, new product investment, or technology change. Use when you need to justify spend to finance or executive leadership.

**Inputs needed:**
- Description of the proposed initiative
- Current state costs (if known): operational costs, manual labor hours, error/rework costs, compliance risks
- Proposed investment estimate (if known)
- Expected timeline to value

**Output:** A structured ROI model template with calculation methodology, benefit categories, and investment justification narrative.

**Tool notes:** AI will generate the model structure and formulas but cannot populate real financial data. Use this as a template to fill in with your finance team. Claude Code produces the most complete output for this prompt.

**Prompt:**
```text
You are a product manager and business strategist building an ROI model for a technology investment proposal.

INITIATIVE:
- Name: [INITIATIVE NAME]
- Description: [2-3 sentences: what are we building/changing, and what is the primary mechanism of value?]
- Scope: [Systems affected, teams involved, geographic scope]
- Proposed Investment: [DOLLAR AMOUNT or "unknown — to be estimated"]
- Implementation Timeline: [e.g., "12 months to full production, phased in 3 stages"]
- Time Horizon for ROI Calculation: [e.g., "3 years post-launch"]

CURRENT STATE COSTS (fill in what you know, leave blank otherwise):
- Manual labor cost: [hours/week × headcount × average hourly fully-loaded cost]
- Error/rework rate: [% of transactions, cost per error]
- Compliance/audit cost: [annual cost of current compliance activities]
- System maintenance cost: [annual vendor support, internal maintenance hours]
- Downtime/incident cost: [average annual downtime hours × cost per hour]
- Opportunity cost: [revenue or growth blocked by current system limitations]

INSTRUCTIONS:
Build a comprehensive ROI model template with the following components.

SECTION 1 — INVESTMENT COSTS
Create a cost table covering:
- One-time costs: discovery, design, development, testing, training, data migration, infrastructure setup
- Recurring costs: licensing, hosting/cloud, support and maintenance, ongoing training
- Risk-adjusted costs: contingency (typically 15-20% of project costs)
- Total Cost of Ownership (TCO) over the stated time horizon

For each cost line: provide a calculation formula and a [FILL IN] placeholder.

SECTION 2 — BENEFIT CATEGORIES
Quantify benefits across these categories (use [FILL IN] where data is absent):

| Benefit Category | Description | Calculation Method | Year 1 | Year 2 | Year 3 | Notes |
|----------------|-------------|-------------------|--------|--------|--------|-------|
| Labor savings | [describe] | headcount × hours saved × loaded cost | | | | |
| Error reduction | [describe] | error rate reduction × cost per error × volume | | | | |
| Compliance cost reduction | [describe] | | | | | |
| Revenue enablement | [describe] | new capability × addressable transactions × margin | | | | |
| Maintenance cost avoidance | [describe] | | | | | |
| Risk reduction | [describe] — express as expected value: probability × cost of event | | | | | |

SECTION 3 — ROI CALCULATIONS
Provide Excel-compatible formulas for:
- Net Benefit = Total Benefits - Total Costs
- ROI % = (Net Benefit / Total Investment) × 100
- Payback Period = Total Investment / Annual Net Benefit
- NPV at [8%] discount rate (provide formula)
- Break-even point (year/month)

SECTION 4 — SCENARIO ANALYSIS
Build three scenarios:
- Conservative (50% of projected benefits, 120% of projected costs)
- Base Case (100% of projected benefits, 100% of projected costs)
- Optimistic (130% of projected benefits, 90% of projected costs)

Show ROI and payback period for each scenario.

SECTION 5 — INVESTMENT NARRATIVE
Write a 3-paragraph executive narrative:
1. The cost of inaction (what happens if we don't invest)
2. The investment and how it delivers value
3. Why this initiative compares favorably to alternatives

SECTION 6 — ASSUMPTIONS AND SENSITIVITIES
List 5-8 key assumptions that drive the model. For the top 3, describe what happens to ROI if the assumption is wrong by 25%.

Include a data collection checklist: what financial data must be gathered from finance, operations, and technology teams to finalize this model.
```

**Review checklist:**
- [ ] All `[FILL IN]` placeholders have been completed with real data before presenting
- [ ] Finance team has reviewed and validated cost assumptions
- [ ] Benefit calculations have been validated by the operational owners
- [ ] Conservative scenario still shows a positive return (if not, reconsider scope)
- [ ] Assumptions section is honest about uncertainties
- [ ] Investment narrative has been reviewed by communications or a senior leader for tone
- [ ] Model has been sense-checked against similar past projects in your organization

---

*FORGE — Framework for Orchestrated AI-Guided Engineering | Product Manager Prompt Pack v1.0*
