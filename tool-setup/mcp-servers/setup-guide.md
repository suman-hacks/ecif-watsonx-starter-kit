# MCP Server Setup Guide — FORGE + Claude Code

> **What is MCP?** The Model Context Protocol (MCP) is an open standard that lets Claude Code connect directly to external systems — your Git repositories, Jira boards, Confluence wikis, databases, and internal service catalogs — and query them live during a coding session. Instead of copying and pasting context from tools into the chat, Claude reads it directly.

---

## Why MCP Matters for FORGE Workflows

Without MCP, engineers must manually copy tickets, schemas, and documentation into the AI session. With MCP:

| Without MCP | With MCP |
|---|---|
| Copy Jira ticket into chat | Claude reads the ticket live: `/analyze-legacy — see AUTHZ-1042` |
| Paste DB schema manually | Claude queries the schema directly when generating JPA entities |
| Manually describe open PRs | Claude lists open PRs and their context before generating code |
| Re-explain Confluence decisions | Claude reads the ADR page directly from Confluence |
| Copy service inventory by hand | Claude queries your internal API catalog for existing services |

The result: your FORGE prompts become shorter, more accurate, and grounded in live data rather than stale copy-pastes.

---

## Prerequisites

- Claude Code installed and authenticated (`claude --version`)
- Node.js 18+ or npx available (`node --version`)
- Access credentials for the systems you want to connect (GitHub token, Jira API token, etc.)

---

## How MCP Works with Claude Code

Claude Code reads MCP server configuration from `.mcp.json` in your project root, or from `~/.claude/mcp.json` globally.

```json
{
  "mcpServers": {
    "server-name": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-name"],
      "env": {
        "API_KEY": "${MY_API_KEY}"
      }
    }
  }
}
```

Start Claude Code after configuring — it will connect to all configured MCP servers automatically.

---

## Recommended MCP Servers for Enterprise Engineering Teams

---

### 1. GitHub MCP Server

**What it gives Claude:** Read/write access to repositories, issues, pull requests, workflows, and code search across your GitHub organisation.

**Install:**
```bash
# No installation needed — uses npx
```

**Configure in `.mcp.json`:**
```json
{
  "mcpServers": {
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "${GITHUB_TOKEN}"
      }
    }
  }
}
```

**Generate your token:**
GitHub → Settings → Developer Settings → Personal Access Tokens → Fine-grained tokens
Required scopes: `repo`, `read:org`, `read:user`

**FORGE usage examples:**
```
# Check all open PRs before starting work
List open pull requests in the [repo] repository

# Trace a ticket to its implementation
Find all commits and code related to issue #412 in [repo]

# Before /review-code, pull the PR diff directly
Review the changes in PR #87 against FORGE standards
```

---

### 2. Jira MCP Server

**What it gives Claude:** Read/write access to Jira tickets, sprints, epics, and comments — so Claude can read acceptance criteria and link code to stories without copy-paste.

**Install:**
```bash
npm install -g @aashari/mcp-server-atlassian-jira
```

**Configure in `.mcp.json`:**
```json
{
  "mcpServers": {
    "jira": {
      "command": "mcp-server-atlassian-jira",
      "env": {
        "ATLASSIAN_SITE_NAME": "yourorg.atlassian.net",
        "ATLASSIAN_USER_EMAIL": "your.email@yourorg.com",
        "ATLASSIAN_API_TOKEN": "${JIRA_API_TOKEN}"
      }
    }
  }
}
```

**Generate your token:**
Atlassian account → Security → API tokens → Create API token

**FORGE usage examples:**
```
# Pull acceptance criteria before generating code
Read AUTHZ-1042 and generate tests that verify each acceptance criterion

# Use /extract-rules with live ticket data
/extract-rules — read the business rules in epic AUTHZ-100 and all child stories

# Before /generate-service, validate the spec
What are the acceptance criteria for AUTHZ-1056? Generate a service that satisfies them.
```

---

### 3. Confluence MCP Server

**What it gives Claude:** Read access to your Confluence wiki — architecture decision pages, runbooks, domain glossaries, and design documents — so Claude can reference your existing decisions without re-explaining them.

**Install:**
```bash
npm install -g @aashari/mcp-server-atlassian-confluence
```

**Configure in `.mcp.json`:**
```json
{
  "mcpServers": {
    "confluence": {
      "command": "mcp-server-atlassian-confluence",
      "env": {
        "ATLASSIAN_SITE_NAME": "yourorg.atlassian.net",
        "ATLASSIAN_USER_EMAIL": "your.email@yourorg.com",
        "ATLASSIAN_API_TOKEN": "${JIRA_API_TOKEN}"
      }
    }
  }
}
```

**FORGE usage examples:**
```
# Pull the architecture page before /create-adr
Read the "Payment Authorization Architecture" page in Confluence and create an ADR for the proposed changes

# Reference existing runbooks
Read the existing runbook for the Authorization Service in Confluence and update it with the new failure scenarios

# Pull domain glossary for the session context
Load the "Payments Domain Glossary" page from Confluence and use it for this session
```

---

### 4. PostgreSQL MCP Server

**What it gives Claude:** Read-only query access to your PostgreSQL database — so Claude can inspect the actual schema, constraints, and indexes when generating JPA entities, Flyway migrations, or data mapping code.

**Install:**
```bash
npm install -g @modelcontextprotocol/server-postgres
```

**Configure in `.mcp.json`:**
```json
{
  "mcpServers": {
    "postgres": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-postgres", "${DATABASE_URL}"],
      "env": {
        "DATABASE_URL": "postgresql://user:password@host:5432/dbname"
      }
    }
  }
}
```

> **Security:** Use a read-only database user for the MCP connection. Never use the application user or a DBA account. The MCP server should only have SELECT privileges on tables relevant to the project.

**FORGE usage examples:**
```
# Generate JPA entities from the actual schema
Query the schema for the authz_transactions table and generate a JPA entity with all constraints

# Generate a Flyway migration from schema diff
Compare the current schema with the entity class in src/ and generate the Flyway migration

# Validate data mapping against real schema
The COBOL COMMAREA maps account_id to ACCT-ID. What is the column type in the accounts table?
```

---

### 5. Filesystem MCP Server

**What it gives Claude:** Explicit, controlled access to files outside the current working directory — useful for reading legacy artifacts stored on shared drives, or referencing other project repositories without switching directories.

**Configure in `.mcp.json`:**
```json
{
  "mcpServers": {
    "filesystem": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-filesystem",
        "/path/to/legacy-artifacts",
        "/path/to/shared-documents"
      ]
    }
  }
}
```

**FORGE usage examples:**
```
# Read legacy COBOL from a shared drive
/analyze-legacy — read AUTHZ0100.cbl from the legacy-artifacts volume

# Reference a shared architecture document
Read the enterprise reference architecture from the shared drive and generate an ADR aligned with it
```

---

### 6. Slack MCP Server (Optional — for Team Notifications)

**What it gives Claude:** Read access to Slack channels and the ability to post notifications — useful for posting delivery package summaries or review gate results to the team channel.

**Configure in `.mcp.json`:**
```json
{
  "mcpServers": {
    "slack": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-slack"],
      "env": {
        "SLACK_BOT_TOKEN": "${SLACK_BOT_TOKEN}",
        "SLACK_TEAM_ID": "${SLACK_TEAM_ID}"
      }
    }
  }
}
```

**FORGE usage examples:**
```
# After /package-delivery, notify the team
Post the Stage 2 delivery package summary to #auth-modernization-team in Slack

# Check for SME responses
Read the last 20 messages in #authz-sme-questions for answers to Q-001 and Q-002
```

---

## FORGE-Specific MCP Configuration

### Recommended `.mcp.json` for a Mainframe Modernization Project

```json
{
  "mcpServers": {
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "${GITHUB_TOKEN}"
      }
    },
    "jira": {
      "command": "mcp-server-atlassian-jira",
      "env": {
        "ATLASSIAN_SITE_NAME": "${ATLASSIAN_SITE}",
        "ATLASSIAN_USER_EMAIL": "${ATLASSIAN_EMAIL}",
        "ATLASSIAN_API_TOKEN": "${ATLASSIAN_TOKEN}"
      }
    },
    "confluence": {
      "command": "mcp-server-atlassian-confluence",
      "env": {
        "ATLASSIAN_SITE_NAME": "${ATLASSIAN_SITE}",
        "ATLASSIAN_USER_EMAIL": "${ATLASSIAN_EMAIL}",
        "ATLASSIAN_API_TOKEN": "${ATLASSIAN_TOKEN}"
      }
    },
    "postgres": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-postgres", "${POSTGRES_READONLY_URL}"]
    }
  }
}
```

### Environment Variables (`.env.local` — never commit)

```bash
# GitHub
GITHUB_TOKEN=ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

# Atlassian (Jira + Confluence share the same token)
ATLASSIAN_SITE=yourorg.atlassian.net
ATLASSIAN_EMAIL=your.email@yourorg.com
ATLASSIAN_TOKEN=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

# PostgreSQL (read-only user)
POSTGRES_READONLY_URL=postgresql://forge_readonly:password@localhost:5432/authz_db
```

> Add `.env.local` to `.gitignore`. Never commit API tokens.

---

## Security Guidelines for MCP in Enterprise Environments

| Rule | Detail |
|---|---|
| Use read-only credentials | Database MCP: SELECT-only user. GitHub MCP: fine-grained token, not classic PAT. |
| Scope tokens to the project | GitHub token: only the repositories in scope. Jira token: only the project boards in scope. |
| Never put tokens in `.mcp.json` | Use environment variable references (`${TOKEN_NAME}`) — never inline the value. |
| Review what Claude can access | Before starting a session, check which MCP servers are connected: Claude will list them. |
| Separate tokens per environment | Dev/staging tokens ≠ production tokens. The AI should never have production credentials. |
| Rotate tokens regularly | Treat MCP tokens like service account passwords — quarterly rotation minimum. |

---

## Verifying Your MCP Setup

After configuring, start Claude Code and run:
```
What MCP servers are you connected to? List each one and what it gives you access to.
```

Claude will enumerate the connected servers and their capabilities. If a server fails to connect, Claude will report the error with enough detail to diagnose the issue.

---

## FORGE Workflow with MCP — End to End Example

```bash
# Start Claude Code with MCP servers connected
claude

# Session opens — Claude reads CLAUDE.md and connects to MCP servers

# T1 — Pre-engagement: Pull legacy inventory from Jira epic
/pre-engagement
Read the AUTHZ epic (AUTHZ-100) from Jira. Use all child stories as the scope inventory for T1-T5 analysis.

# Stage 1 — Analysis grounded in live ticket data
/analyze-legacy
Analyze AUTHZ0100.cbl from the filesystem. Cross-reference business rules with acceptance criteria in AUTHZ-1042 through AUTHZ-1059.

# Stage 2 — Design cross-referenced with existing architecture
/create-adr
Read the "Payment Authorization Architecture" page from Confluence. Create an ADR for introducing a dedicated Limit Service as described in AUTHZ-1060.

# Stage 3 — Generate from live schema
/generate-service
Generate the LimitService. Use the actual authz_limits table schema from Postgres to generate the JPA entity. Reference AUTHZ-1060 and AUTHZ-1061 for the business rules.

# Stage 4 — Package and notify
/package-delivery
Compile the Stage 3 delivery package. Post a summary to #auth-modernization-team in Slack.
```

---

*FORGE MCP Server Setup Guide | Tool Integration | Version 2.0*
*MCP protocol: modelcontextprotocol.io | Claude Code docs: anthropic.com/claude-code*
