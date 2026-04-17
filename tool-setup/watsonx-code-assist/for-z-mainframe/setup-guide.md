# IBM watsonx Code Assist for Z — Setup and FORGE Integration Guide

> This guide covers installation, configuration, and FORGE-aligned workflow for IBM watsonx Code Assist for Z — IBM's purpose-built AI assistant for COBOL, PL/1, JCL, REXX, and Assembler on z/OS.

---

## What is watsonx Code Assist for Z?

IBM watsonx Code Assist for Z is an AI coding assistant specifically trained on mainframe artifacts: COBOL, PL/1, Assembler, JCL, REXX, copybooks, CICS transaction definitions, IMS DB/DC constructs, and z/OS system programming patterns. Unlike general-purpose AI coding tools, it understands:

- COBOL II and Enterprise COBOL syntax, WORKING-STORAGE layouts, PROCEDURE DIVISION logic
- PIC clause data types including packed decimal (COMP-3), binary (COMP), and EBCDIC text
- COPYBOOK structures and their role in shared data definitions across programs
- CICS API calls (EXEC CICS READ, WRITE, RETURN, LINK, XCTL, etc.)
- IMS DL/I calls (GU, GN, ISRT, DLET, REPL) and PCB/PSB structures
- JCL step sequencing, STEPLIB concatenation, DD statements, and GDG (Generation Data Groups)
- DB2 for z/OS embedded SQL, cursor processing, and SQLCODE handling
- REXX and CLIST scripting for z/OS system automation
- z/OS file I/O: VSAM (KSDS, ESDS, RRDS), sequential, and partitioned data sets (PDS/PDSE)

This tool is used during **FORGE Stage 1 (Legacy Understanding)** and **Stage 2 (Target Mapping)** of the mainframe modernization workflow. It is not used for generating target-state Java or distributed code — pair it with Claude Code or GitHub Copilot for that purpose.

---

## Prerequisites

Before beginning installation, confirm you have the following:

### IBM Account and Licensing
- [ ] An IBM ID (create at https://myibm.ibm.com if you do not have one)
- [ ] An active IBM watsonx Code Assist for Z license (contact your IBM account team for a trial or purchase)
- [ ] Access to the IBM Entitlement Registry or IBM Software Hub for downloading extensions

### Development Environment
- [ ] Visual Studio Code version 1.85 or later (recommended), OR Eclipse IDE for Z with IBM Z Open Editor
- [ ] **IBM Z Open Editor** extension for VS Code (available from the VS Code Marketplace)
- [ ] **IBM COBOL Language Support** extension for VS Code (available from the VS Code Marketplace)
- [ ] IBM Zowe CLI installed and configured (https://docs.zowe.org/stable/user-guide/cli-installcli.html)

### z/OS System Access
- [ ] Network access to your z/OS LPAR (typically via TN3270 port 992 or Zowe API Mediation Layer)
- [ ] A Zowe profile configured with your TSO/ISPF credentials
- [ ] READ access to the source datasets (COBOL libraries, JCL libraries, copybook libraries)
- [ ] Optionally: a USS (Unix System Services) sandbox environment for testing

### FORGE Prerequisites
- [ ] This FORGE repository cloned locally
- [ ] The mainframe modernization project context loaded: `project-contexts/mainframe-modernization/`
- [ ] Your project's legacy source inventory (list of programs to be modernized)

---

## Installation

### Step 1: Install VS Code Extensions

Open VS Code. Navigate to the Extensions panel (Ctrl+Shift+X / Cmd+Shift+X).

Install the following extensions in order:

1. **IBM Z Open Editor** (publisher: IBM)
   - Search: `IBM Z Open Editor`
   - This extension provides syntax highlighting, outline view, and basic editing for COBOL, PL/1, JCL, and REXX

2. **COBOL Language Support** (publisher: Broadcom — note: two versions exist; the IBM Z Open Editor includes its own; Broadcom's is an alternative)
   - If your organization standardizes on Broadcom Zowe Explorer, use that stack instead

3. **Zowe Explorer** (publisher: Zowe)
   - Search: `Zowe Explorer`
   - This extension provides DS browser, JES queue view, and USS file system access

4. **IBM watsonx Code Assist for Z** extension
   - This extension is not on the public marketplace — it is distributed via IBM Entitlement Registry or IBM Software Hub
   - Follow your organization's IBM software distribution process to obtain the `.vsix` file
   - Install via: `Extensions → ... → Install from VSIX`

### Step 2: Configure Zowe CLI Profile

Open a terminal and run:

```bash
# Initialize Zowe CLI
zowe config init

# Create a base profile for your z/OS system
zowe config set profiles.base.properties.host YOUR_ZOSMF_HOST
zowe config set profiles.base.properties.port 443
zowe config set profiles.base.properties.user YOUR_TSO_USERID
zowe config set profiles.base.properties.password YOUR_TSO_PASSWORD
zowe config set profiles.base.properties.rejectUnauthorized false

# Test the connection
zowe zosmf check status
```

If your organization uses the Zowe API Mediation Layer, configure the base URL accordingly. Consult your z/OS system programmer for the correct ZOSMF host and port.

### Step 3: Configure Zowe Explorer in VS Code

1. Open VS Code
2. Click the Zowe Explorer icon in the Activity Bar (zebra icon)
3. Click the `+` button next to "Data Sets" to add a data set profile
4. Enter your Zowe profile name (from Step 2)
5. Enter a dataset filter pattern matching your COBOL libraries (e.g., `YOURORG.PROD.COBOL`, `YOURORG.PROD.COPY`)
6. Navigate to your COBOL source — you should see your programs listed

### Step 4: Activate IBM watsonx Code Assist for Z

1. Open VS Code Command Palette (Ctrl+Shift+P / Cmd+Shift+P)
2. Run: `watsonx Code Assist: Sign In`
3. Follow the OAuth flow to authenticate with your IBM ID
4. Accept the license agreement
5. Run: `watsonx Code Assist: Check Status` — you should see a green indicator confirming the extension is connected

---

## Loading FORGE Context for Mainframe Sessions

watsonx Code Assist for Z does not have a persistent project config file like CLAUDE.md or `.cursorrules`. You must load your FORGE context at the beginning of each analysis session. Use the following procedure:

### Creating Your Session Context Block

Create a text file at `tool-setup/watsonx-code-assist/for-z-mainframe/session-context.txt` in your local FORGE repository (or your project repo). Populate it with the following, customized for your project:

```
You are assisting with the mainframe modernization of [PROJECT NAME].
Operating under FORGE v1.0 — Framework for Orchestrated AI-Guided Engineering.

ALWAYS-ON RULES:
1. Do not invent business logic not present in the source code or provided documentation.
2. When identifying a business rule, cite the exact COBOL paragraph name, line number, and program name.
3. Label every finding as: FACT (from source code), ASSUMPTION (inferred), or OPEN QUESTION (requires clarification).
4. Never include real customer data, production PAN/CVV, credentials, or regulated data in this session.
5. Flag any ambiguous COBOL logic before interpreting it — ask a clarifying question.
6. Preserve EBCDIC/packed-decimal precision semantics when mapping to Java/modern equivalents.

PROJECT CONTEXT:
- Legacy platform: z/OS [VERSION], CICS [VERSION], DB2 [VERSION]
- Target platform: [Java 17 | Python | etc.] on [AWS | IBM Cloud | on-premises]
- Domain: [payments | insurance | banking | HR | etc.]
- Current modernization scope: [list of programs in scope for this session]
- Copybook library: [DSN.COPY.LIB or local path]
- Known constraints: [e.g., "Packed decimal precision must be preserved to 2dp", "EBCDIC-to-UTF8 conversion is handled by the middleware layer"]
```

### Loading the Context in a Chat Session

1. Open watsonx Code Assist Chat panel in VS Code
2. As your FIRST message, paste the entire session context block above
3. After the tool acknowledges, begin your analysis tasks

---

## COBOL Analysis Workflow (FORGE Stage 1)

Use this workflow to systematically analyze a COBOL program and produce the structured output needed for Stage 2 (Target Mapping).

### Sub-step 1.1: Program Identification

Open the COBOL source file in VS Code (either from Zowe Explorer or local copy). Send this prompt to watsonx Code Assist:

```
Analyze the following COBOL program and provide:
1. Program purpose (one paragraph summary)
2. IDENTIFICATION DIVISION details (program ID, author, date if present)
3. All called programs (CALL statements) with parameter lists
4. All external file interactions (FD entries, OPEN/READ/WRITE/CLOSE statements) with file names and access modes
5. All DB2 SQL statements with table names, operation types, and accessed columns
6. All CICS API calls with their transaction codes and maps if applicable
7. All MQ or external messaging interactions

Format as structured JSON.

[PASTE PROGRAM SOURCE HERE]
```

### Sub-step 1.2: Business Rule Extraction

```
From the COBOL program above, extract all business rules encoded in the PROCEDURE DIVISION.
For each rule, provide:
- Rule ID (auto-assign BR-001, BR-002, ...)
- Rule type: VALIDATION | CALCULATION | ROUTING | ERROR_HANDLING | LIMIT_CHECK | AUDIT
- Rule description in plain English (no COBOL jargon)
- Source location: paragraph name and approximate line range
- Confidence: HIGH (unambiguous code) | MEDIUM (requires domain knowledge to confirm) | LOW (ambiguous or unclear)
- Open questions: any aspect of this rule that requires business analyst confirmation

Output as a markdown table.
```

### Sub-step 1.3: Data Structure Mapping

```
Analyze the WORKING-STORAGE SECTION and any referenced copybooks.
For each significant data structure, provide:
- Structure name (group-level 01 or 05 entry name)
- Field inventory: field name, PIC clause, usage (COMP-3, BINARY, etc.), length in bytes, description
- Proposed Java equivalent: Java type, precision notes (especially for COMP-3 packed decimal — always use BigDecimal for monetary amounts)
- EBCDIC encoding notes where applicable
- Any fields containing sensitive data (PAN, account numbers, SSN) — mark these with [SENSITIVE]

Output as a markdown table with columns: COBOL Field | PIC Clause | Usage | Bytes | Proposed Java Type | Notes
```

### Sub-step 1.4: Error Handling and Edge Case Capture

```
Analyze the error handling logic in this COBOL program.
Identify:
1. All FILE STATUS checks and the response for each status code
2. All SQLCODE checks and the response for each code
3. All CICS RESP/RESP2 checks and responses
4. All ABEND calls and their conditions
5. Any PERFORM or paragraph that handles exception conditions
6. Any default/fallback behavior when unexpected inputs are received

For each error condition, describe:
- Trigger condition
- System response (log, abend, return code, retry, etc.)
- Whether the behavior needs to be replicated in the modern system or is platform-specific
```

### Sub-step 1.5: Produce Stage 1 Summary

After completing the above analysis, produce a Stage 1 output document:

```
Produce a Stage 1 Analysis Summary for program [PROGRAM NAME].
Include:
1. Executive summary (3-5 sentences)
2. Business rules inventory (from extraction above)
3. Data structure inventory (from mapping above)
4. External dependencies list (calls, files, DB2, CICS, MQ)
5. Open questions list (numbered, with priority: BLOCKER | HIGH | MEDIUM | LOW)
6. Modernization risk assessment: LOW | MEDIUM | HIGH — with justification
7. Recommended modernization approach: LIFT-AND-SHIFT | REFACTOR | REPLACE | HYBRID — with justification

This document will be reviewed by the tech lead and business analyst before Stage 2 begins.
```

---

## Integration with Distributed Code Generation

watsonx Code Assist for Z produces the analysis artifacts. The actual Java/Spring Boot code generation is done by Claude Code or GitHub Copilot. Here is the handoff process:

### Handoff from Stage 1 to Stage 2 (Claude Code)

1. Save your Stage 1 Analysis Summary as a Markdown file in your project's `docs/business-rules/` directory. Name it `[PROGRAM_NAME]-stage1-analysis.md`.

2. Open a Claude Code session (run `claude` in your project root).

3. Reference the Stage 1 document:
   ```
   Load the Stage 1 analysis in docs/business-rules/[PROGRAM_NAME]-stage1-analysis.md
   and proceed with FORGE Stage 2 (Target Mapping). Design the target Java components 
   for this program following the architectural patterns in CLAUDE.md.
   ```

4. Claude Code will produce: Java package design, interface definitions, data mapping specification, and ADRs for non-trivial decisions.

### Handoff from Stage 2 to Stage 3 (GitHub Copilot / Claude Code)

1. Save the Stage 2 target design as `docs/architecture/[PROGRAM_NAME]-target-design.md`.
2. Use Claude Code or GitHub Copilot to implement the Java code from the Stage 2 specification.
3. Reference the design document explicitly: `Implement the design in docs/architecture/[PROGRAM_NAME]-target-design.md`

---

## Key Prompting Patterns for Mainframe Work

### Pattern: Packed Decimal Precision

Always include this note when analyzing financial calculations:

> "When identifying calculations involving COMP-3 (packed decimal) fields, flag the precision requirements and recommend `java.math.BigDecimal` with explicit `RoundingMode` and scale for all monetary amounts. Never use `double` or `float` for financial calculations."

### Pattern: EBCDIC Character Handling

> "Identify any string operations that depend on EBCDIC collation order or byte-level character comparisons. These may behave differently after conversion to UTF-8. Flag each occurrence with [EBCDIC DEPENDENCY]."

### Pattern: Batch vs. Online Distinction

> "Identify whether this program is designed to run as a CICS online transaction, a batch job step, or both. Note any logic that is conditional on the runtime environment (CICS vs. batch). This distinction affects threading model, transaction boundary design, and error handling in the modern equivalent."

### Pattern: File-to-Database Mapping

> "For each VSAM file interaction, identify the logical entity being stored (not just the file name) and suggest an appropriate relational table or document structure for the target system. Note any GDG (Generation Data Group) usage that implies versioning or historical data requirements."

### Pattern: Reentrant and Multi-Threading Safety

> "Review this COBOL program for non-reentrant patterns: WORKING-STORAGE data that holds inter-call state, use of ALTER, PERFORM THRU with no return guarantee, or external data areas shared across invocations. Flag any patterns that will require careful thread-safety design in the Java equivalent."

---

## Frequently Asked Questions

**Q: Can I paste an entire COBOL program into the chat?**
Yes. watsonx Code Assist for Z has a large context window designed to handle full COBOL programs. For very large programs (10,000+ lines), break them into logical sections (DATA DIVISION, MAIN-LOGIC, specific SECTIONS) and analyze each section in sequence, maintaining a running summary.

**Q: What about copybooks not in my local workspace?**
Either download the copybooks from z/OS using Zowe CLI (`zowe files download ds "DSN.COPY.LIB(COPYNAME)"`) or paste their content directly into the chat context before asking for analysis. The tool needs the copybook content to correctly interpret group-level data structures.

**Q: Can the tool connect directly to z/OS to read programs?**
Via Zowe Explorer (the VS Code extension), you can browse and open members directly from z/OS datasets. The tool will then analyze the open file. It does not issue direct ZOSMF API calls on its own.

**Q: How do I handle programs that CALL many sub-programs?**
Analyze the called sub-programs first (starting with the lowest-level utilities), document their Stage 1 summaries, then reference those summaries when analyzing the calling program. Build a call-tree diagram and work bottom-up.

**Q: The tool misidentified a business rule — what do I do?**
This is expected for complex or ambiguous COBOL logic. Mark the finding with confidence LOW, add it to the open questions list, and escalate to a business analyst or SME who knows the original program's intent. Never generate target-state code for LOW-confidence business rules without SME confirmation.
