# P1: Stakeholder Interview Preparation

**Prompt:** 1 of 3  
**Output File:** `DISCOVERY_INTERVIEW_GUIDE.md`  
**Who Runs It:** Business Analyst or Workshop Facilitator  
**Time Required:** 10–15 minutes  
**Prerequisite:** Stage 00 Pre-Engagement documents (if available); project brief or engagement charter

---

## Stage Overview

This prompt generates a structured stakeholder interview guide tailored to the specific project context. Good stakeholder interviews are not improvised conversations — they are structured explorations designed to surface requirements, constraints, risks, and priorities that are not visible from code or documentation alone.

The AI generates the guide based on: (1) what is known from the Stage 00 analysis, (2) the project's business objectives, and (3) standard discovery patterns for the domain.

## Workshop Connection

The interview guide feeds directly into the legacy system analysis (P2) and current-state assessment (P3). The most valuable outputs are: confirmed constraints, identified pain points, priority rankings, and explicit unknowns that the technical team must investigate.

## Tool Guidance

| Tool | Instructions |
|---|---|
| **GitHub Copilot / Claude Code / Cursor** | Paste the prompt with project context. The more specific the context, the more targeted the questions |
| **watsonx Code Assist** | Same approach; consider using IBM Watson Discovery for processing interview notes afterward |

---

## The Prompt

Copy everything between the `---PROMPT START---` and `---PROMPT END---` markers.

---PROMPT START---

You are a senior business analyst and discovery facilitator with deep experience in enterprise software delivery. Generate a comprehensive stakeholder interview guide for a discovery workshop.

**Project context:** [INSERT: 3–5 sentences describing the project. Include: what system is being assessed or built, what the business objective is, what the organization does, and any known constraints.]

**Known findings from pre-engagement analysis:** [INSERT: paste key points from T1–T5 Stage 00 documents, or write "No pre-engagement analysis available"]

**Stakeholders who will be interviewed:** [INSERT: list each stakeholder's role, e.g., "Head of Payments Operations, Senior Developer (legacy system owner), Business Analyst, Compliance Officer, Customer Experience Lead, IT Infrastructure Manager"]

**Domain:** [INSERT: e.g., "Retail banking / payment authorization", "Healthcare claims processing", "Insurance underwriting", "Telecommunications billing"]

Produce a document titled "Stakeholder Interview Guide" with the following sections:

---

## 1. Stakeholder Map

For each stakeholder identified, produce a card with:

### [Stakeholder Role/Name]
**Organizational Context:** [Where do they sit in the organization? Who do they report to? Who reports to them?]  
**Primary Concern:** [What is their #1 concern about this project? What success looks like to them?]  
**Likely Resistance Points:** [What might they push back on? What fears or concerns might they not voice directly?]  
**Key Information They Hold:** [What unique knowledge or data do they have that others don't?]  
**Influence Level:** DECISION MAKER | KEY INFLUENCER | SUBJECT MATTER EXPERT | INFORMATION SOURCE  
**Interview Priority:** HIGH (must interview) | MEDIUM (should interview) | LOW (optional if time limited)  
**Recommended Interview Format:** 1:1 (sensitive topics), Small Group (3–4 people), Working Session (whiteboard required)  
**Interview Duration:** [recommended time: 30/45/60/90 minutes]

---

## 2. Interview Questions by Stakeholder Type

For each stakeholder type, generate a bank of interview questions organized into the following categories. Questions must be open-ended, non-leading, and designed to surface both explicit information and implicit assumptions.

Generate questions for each stakeholder identified in section 1.

### [Stakeholder Role] — Interview Questions

**Opening / Context Setting (5 minutes)**
Generate 2–3 questions that establish rapport, confirm the stakeholder's role, and frame the conversation. These should feel conversational, not interrogative.

**Current State — What They See (15 minutes)**
Generate 5–7 questions that explore:
- How does the current system/process work from their perspective?
- What works well that must be preserved?
- What are the biggest pain points they experience day-to-day?
- What workarounds or manual interventions are required because the system cannot do something automatically?

**Future State — What They Need (15 minutes)**
Generate 4–6 questions that explore:
- What would success look like in 6 months? 2 years?
- Which pain points would they most want eliminated?
- What would they be willing to trade off to get the most important thing?
- Who else would benefit from improvements, and what would they need?

**Constraints and Non-Negotiables (10 minutes)**
Generate 3–5 questions that surface:
- Regulatory or compliance requirements they cannot compromise on
- Performance or availability requirements that are firm
- Political or organizational constraints on what can change
- Budget, timeline, or resource constraints they are aware of

**Technical Context (if applicable — for technical stakeholders) (10 minutes)**
Generate 3–5 questions specific to technical stakeholders:
- What parts of the system are most fragile?
- What would you rewrite if you could start over?
- What do you wish the new team understood about this system before touching it?
- What integrations cause the most operational pain?
- Where do bugs tend to cluster?

**Priorities and Trade-offs (10 minutes)**
Generate 3–4 questions that reveal relative priorities:
- "If you could only fix one thing, what would it be?"
- Stack ranking exercises
- MoSCoW framing (Must/Should/Could/Won't)
- Questions that reveal when stated priorities conflict

**Closing / Next Steps (5 minutes)**
Generate 2–3 closing questions:
- Who else should we speak with that we haven't?
- What question did we not ask that we should have?
- Is there anything you are worried about that you haven't raised?

---

## 3. Known Constraints and Assumptions to Validate

Based on the pre-engagement analysis and project context, produce a table of constraints and assumptions that must be validated in stakeholder interviews. Columns: **Item | Type | Source | Validation Question | Risk if Wrong**

**Type options:** TECHNICAL CONSTRAINT, BUSINESS CONSTRAINT, REGULATORY CONSTRAINT, ORGANIZATIONAL CONSTRAINT, ASSUMPTION

Include:
- Technical constraints identified in T1–T3 (latency targets, integration constraints, infrastructure limits)
- Business rules that appear hardcoded in T2 but may be negotiable
- Assumptions the pre-engagement team made that stakeholders may correct
- Regulatory or compliance constraints mentioned in any pre-engagement document
- Any constraint that, if wrong, would significantly change the POC option selected

---

## 4. Business Context Deep Dive Questions

Generate 8–12 questions designed to deeply understand the business context regardless of stakeholder type. These are questions that should weave into every interview:

**Business Process Questions:**
- End-to-end process description questions
- Volume and frequency questions (how many transactions? how often? peak periods?)
- Exception handling and escalation path questions
- Human touchpoint identification (where do people intervene in an automated process?)

**Customer Impact Questions:**
- Who is ultimately affected if this system fails or performs poorly?
- What does a customer experience when the system makes a wrong decision?
- How do customers currently appeal or contest decisions?

**Business Metrics Questions:**
- How is success currently measured?
- What metrics are reported to leadership?
- What metric would most directly show improvement if the project succeeds?

**Competitive and Strategic Questions:**
- What are competitors doing that this system currently cannot?
- What business capability would unlock the most strategic value?

---

## 5. Technical Context Questions

Generate 6–10 questions for technical stakeholders that go beyond what pre-engagement code analysis can reveal:

**Undocumented Behavior:**
- "Are there behaviors in the system that work but nobody fully understands why?"
- "Is there code that everyone is afraid to touch? Why?"

**Operational Reality:**
- "What alerts fire most often, and what do you do when they fire?"
- "What is your deployment process, and what is the riskiest step?"
- "How long does it take to diagnose and fix a production incident?"

**Data Reality:**
- "Is the data in production what you would expect? Any known quality issues?"
- "Are there data sources you wish you had access to but don't?"

**Team and Process:**
- "How does a change get from an engineer's laptop to production?"
- "What slows development down the most?"
- "What would you automate first if you had unlimited time?"

---

## 6. Interview Logistics and Facilitation Notes

**Scheduling Recommendations:**
- Recommended sequence: [order the stakeholder interviews to build on each other]
- Which stakeholders should NOT be in the same interview: [identify potential conflicts]
- Which stakeholders BENEFIT from being together: [identify where group dynamics help]

**Facilitation Tips:**
- How to handle a dominant stakeholder who speaks for others
- How to surface dissenting views in group settings
- How to time-box without cutting off valuable threads
- When to use silence effectively

**Note-Taking Template:**
For each interview, capture:
```
Interview: [Stakeholder Name/Role]
Date: [Date]
Duration: [Minutes]
Participants: [Names]
Facilitator: [Name]
Note-taker: [Name]

KEY QUOTES (verbatim — use quotation marks):
-
-

CONFIRMED FACTS (things we know are true):
-
-

CONSTRAINTS SURFACED:
-
-

UNKNOWNS OPENED (new questions this interview raised):
-
-

FOLLOW-UP REQUIRED:
-
-

SENTIMENT/TONE OBSERVATIONS (for facilitator notes, not shared):
-
```

**Post-Interview AI Synthesis Prompt:**
After each interview, paste your notes into an AI assistant with this prompt:
```
I just completed a stakeholder interview. Here are my notes: [PASTE NOTES]

Please:
1. Extract all explicit requirements or user needs mentioned
2. Extract all constraints mentioned (technical, business, regulatory)
3. Identify any implicit requirements (things the stakeholder needs but didn't explicitly state)
4. List new unknowns this interview opened
5. Flag any contradictions with these previously known facts: [PASTE RELEVANT T1–T4 FINDINGS]
6. Suggest 3 follow-up questions based on what was not fully answered
```

---

**Output format requirements:**
- Use Markdown with clear heading hierarchy
- Interview questions must be genuinely open-ended (not yes/no)
- Every constraint validation question must directly reference the constraint it is validating
- The note-taking template must be in a code block for easy copying

---PROMPT END---

---

## Output Template

Save the AI's output as `DISCOVERY_INTERVIEW_GUIDE.md` in the `discovery/` folder. The file should begin with:

```markdown
# Stakeholder Interview Guide
**Project:** [Project Name]
**Prepared By:** [BA Name]
**Date Prepared:** [YYYY-MM-DD]
**Interview Schedule:** [Link to calendar or paste schedule]
**AI Tool Used:** [Tool Name + version]
**Review Status:** [ ] Draft | [ ] Reviewed | [ ] Approved

---
```

## Completion Checklist

- [ ] Stakeholder map covers all known stakeholder types
- [ ] Each stakeholder has influence level and interview priority assigned
- [ ] Interview questions are open-ended and non-leading
- [ ] Constraints/assumptions table has at least 10 items
- [ ] Business context questions are domain-specific (not generic)
- [ ] Facilitation notes include conflict management guidance
- [ ] Post-interview AI synthesis prompt is included
- [ ] Note-taking template is included
- [ ] Guide reviewed by workshop facilitator
- [ ] Interviews scheduled with all HIGH-priority stakeholders

## Common Pitfalls

**Questions that telegraph the answer.** "Would you say the current system is too slow?" is a leading question. "How does the current system's response time affect your day-to-day operations?" is open-ended. Review every question for leading language.

**Interviewing only technical stakeholders.** The most critical insights often come from operations staff, customer service agents, and compliance officers who interact with the system's outputs. Do not skip non-technical stakeholders.

**Not recording verbatim quotes.** Paraphrased notes lose precision and introduce interpretation. Train note-takers to capture direct quotes for the most important statements — these become evidence in requirements documents.

**Scheduling interviews in the wrong order.** Interview business stakeholders before technical stakeholders — their framing should inform the technical questions. Interview individual contributors before managers — they often reveal realities that managers smooth over.

---

*Next Prompt: P2 — Legacy System Discovery*
