# FORGE Web Portal

A zero-dependency, single-file browser portal that gives every engineering persona access to FORGE prompts — no IDE or AI tool installation required.

## Who It's For

| Persona | Why They Use the Portal |
|---|---|
| **Product Manager / Business Analyst** | Access requirements and rules prompts without an IDE |
| **Workshop Facilitators** | Display prompts on a screen during discovery workshops |
| **New Team Members** | Explore available prompts before setting up an IDE |
| **Non-Technical Stakeholders** | Understand what AI analysis is being run and why |
| **Any Engineer** | Quick access to prompts without opening a project in an IDE |

---

## How to Use It

### Option 1: Open Locally (Immediate, No Setup)

```bash
# From the forge/ directory
open web-ui/index.html      # macOS
start web-ui/index.html     # Windows
xdg-open web-ui/index.html  # Linux
```

Or just double-click `index.html` in Finder/Explorer.

### Option 2: Serve Locally (for teams)

```bash
# Python (works on any machine with Python 3)
cd web-ui
python3 -m http.server 8080
# Open: http://localhost:8080

# Node.js (if installed)
npx serve web-ui
# Open: http://localhost:3000
```

### Option 3: Deploy to GitHub Pages (team-wide access)

1. Push the `forge/` repository to GitHub.
2. Go to repository **Settings → Pages**.
3. Set **Source** to `main` branch, `/web-ui` folder.
4. Click **Save**. GitHub Pages deploys at: `https://[org].github.io/forge`

All engineers access the portal at that URL — no installation required.

### Option 4: Host on Internal Web Server

Copy `web-ui/index.html` to any internal web server, S3 bucket, or Confluence page (as an HTML attachment). The file is fully self-contained — one file, no dependencies.

---

## What the Portal Provides

- **Pre-Engagement Analysis** — 5-task AI-accelerated discovery prompts (T1–T5)
- **Developer Prompts** — COBOL analysis, ATOM service generation, code review, test generation
- **Architect Prompts** — Service decomposition, ADR generation
- **QA Engineer Prompts** — Test strategy, parallel-run testing
- **DevSecOps Prompts** — Threat modeling, security review
- **Business Analyst Prompts** — Business rule extraction, user story generation
- **Product Manager Prompts** — Modernization roadmap, executive summaries
- **Lead Engineer Prompts** — Review gate checklists, governance

---

## Using a Prompt from the Portal

1. Select your role in the left sidebar or on the home screen
2. Click a prompt card to expand it
3. Click **Copy Prompt**
4. Paste into your AI tool:
   - **Claude.ai** (web) — paste directly in the chat
   - **GitHub Copilot Chat** — paste in the VS Code / JetBrains chat panel
   - **IBM watsonx** — paste in the Prompt Lab or chat interface
   - **ChatGPT / GPT-4** — paste directly in the chat
   - **Claude Code (CLI)** — paste in the terminal session
5. Fill in the `[BRACKETED]` placeholders with your project-specific details
6. Send and review the output

---

## Customizing the Portal

The portal is a single `index.html` file. To add or modify prompts:

1. Open `web-ui/index.html` in any text editor
2. Find the `const PROMPTS = { ... }` section in the `<script>` block
3. Add your prompt to the appropriate section:

```javascript
{
  id: 'DEV-05',                   // Unique ID
  stage: 'Development',           // Display stage
  badge: 'development',           // CSS class for color coding
  badgeLabel: 'Development',      // Label shown in the badge
  title: 'My New Prompt',         // Card title
  desc: 'What this prompt does.', // Short description shown when collapsed
  prompt: `Your prompt text here...
  
  Use [BRACKETS] for sections engineers fill in.`
}
```

4. Save the file. Refresh the browser. The prompt appears immediately.

---

## Keeping Prompts in Sync

The portal prompts should match the detailed prompts in the `sdlc/` and `personas/` directories. When you update a prompt in `sdlc/`, update the corresponding entry in `web-ui/index.html`.

Consider this mapping:

| Portal Section | FORGE Source Files |
|---|---|
| Pre-Engagement | `sdlc/00-pre-engagement/T1–T5` |
| Developer | `personas/developer/prompts.md` |
| Architect | `personas/solution-architect/prompts.md` |
| QA | `personas/qa-engineer/prompts.md` |
| DevSecOps | `personas/devsecops/prompts.md` |
| Business Analyst | `personas/business-analyst/prompts.md` |
| Product Manager | `personas/product-manager/prompts.md` |
| Lead Engineer | `personas/lead-engineer/prompts.md` |

---

## Workshop Setup

For modernization discovery workshops:

1. Open the portal on a laptop connected to a projector
2. Navigate to **Pre-Engagement** section
3. Walk through T1–T5 prompts with the team
4. Engineers run each prompt in their AI tool (Claude Code, Copilot, etc.)
5. Results are reviewed and annotated in the session

The portal gives the room a shared view of what the AI is being asked and why.
