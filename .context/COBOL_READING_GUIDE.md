---
context: "Legacy COBOL Reading Guide"
load-when: "Any work involving reading, analyzing, or transforming COBOL source code"
tools: "Claude Code, GitHub Copilot, JetBrains AI, watsonx"
---

# Context: Legacy COBOL Reading Guide

## When to Load This File

Load for:
- Pre-engagement analysis (Stage 00): Analyzing COBOL programs before a discovery workshop
- Discovery (Stage 01): Understanding what a legacy COBOL system does
- Architecture (Stage 03): Mapping legacy COBOL capabilities to target microservice design
- Development (Stage 05): Transforming COBOL logic into Java or other target languages
- Testing (Stage 06): Generating test cases that prove parity between COBOL and modern implementation
- Any task where the AI must read a `.cbl`, `.cob`, `.copy`, or `.jcl` file

Add to your AI tool's context using:
- **Claude Code / CLAUDE.md:** `#file:.context/COBOL_READING_GUIDE.md`
- **GitHub Copilot:** `#file:.context/COBOL_READING_GUIDE.md` in Copilot Chat
- **JetBrains AI:** Paste this file into the session system prompt

---

## COBOL Program Structure

A standard COBOL program has four Divisions, always in this order:

### IDENTIFICATION DIVISION
Program metadata. Always present.
```cobol
IDENTIFICATION DIVISION.
PROGRAM-ID.   AUTHZ001.
AUTHOR.        JOHN DOE.
DATE-WRITTEN.  2003-07-15.
REMARKS.       AUTHORIZATION DECISION ENGINE FOR CREDIT CARDS.
```
Key field: `PROGRAM-ID` — this is the program's name, used in CALL statements from other programs.

### ENVIRONMENT DIVISION
System interface definitions. Declares file assignments connecting logical file names to JCL DD names.
```cobol
ENVIRONMENT DIVISION.
INPUT-OUTPUT SECTION.
FILE-CONTROL.
    SELECT TRANSACTION-FILE ASSIGN TO TXNFILE
        ORGANIZATION IS SEQUENTIAL
        ACCESS MODE IS SEQUENTIAL.
    SELECT ACCOUNT-VSAM   ASSIGN TO ACCTVSAM
        ORGANIZATION IS INDEXED
        ACCESS MODE IS DYNAMIC
        RECORD KEY IS ACC-KEY
        ALTERNATE RECORD KEY IS ACC-CARD-NUM WITH DUPLICATES.
```
Document: file names, organization (SEQUENTIAL, INDEXED, RELATIVE), access mode, record key fields.

### DATA DIVISION
All data definitions. The most information-dense section. Contains:
- **FILE SECTION** — record layouts of files declared in ENVIRONMENT DIVISION
- **WORKING-STORAGE SECTION** — program working variables, constants, flags, work areas
- **LINKAGE SECTION** — parameters passed to/from this program via CALL or CICS COMMAREA
- **LOCAL-STORAGE SECTION** — like WORKING-STORAGE but re-initialized per invocation (important in CICS)

### PROCEDURE DIVISION
All executable business logic. May begin with:
```cobol
PROCEDURE DIVISION.
```
or with parameters:
```cobol
PROCEDURE DIVISION USING WS-PARM1 WS-PARM2.
```
or in CICS with COMMAREA:
```cobol
PROCEDURE DIVISION.
    EXEC CICS HANDLE ABEND LABEL(9999-ABEND) END-EXEC.
    MOVE DFHCOMMAREA TO WS-COMMAREA.
```

---

## Data Definition Reading Guide

### Level Numbers

Level numbers define the hierarchy of data items. Higher numbers = children of lower numbers.

| Level | Meaning | Example |
|---|---|---|
| `01` | Top-level record or standalone group | `01 WS-ACCOUNT-RECORD.` |
| `02`–`49` | Fields or sub-groups within a record | `05 WS-ACCOUNT-ID PIC 9(10).` |
| `66` | RENAMES — alias for a range of fields | Rarely used; flag when seen |
| `77` | Standalone elementary item (not in a group) | `77 WS-EOF-FLAG PIC X VALUE 'N'.` |
| `88` | Condition name — named value for a field | `88 EOF-REACHED VALUE 'Y'.` |

**Reading a data structure:**
```cobol
01 WS-ACCOUNT-RECORD.
   05 WS-ACCOUNT-ID      PIC 9(10).
   05 WS-ACCOUNT-STATUS  PIC X(2).
      88 ACCT-ACTIVE     VALUE 'AC'.
      88 ACCT-BLOCKED    VALUE 'BL'.
      88 ACCT-EXPIRED    VALUE 'EX'.
   05 WS-BALANCE         PIC S9(11)V99 COMP-3.
   05 WS-LIMIT-GROUP.
      10 WS-CREDIT-LIMIT PIC S9(9)V99  COMP-3.
      10 WS-DAILY-LIMIT  PIC S9(7)V99  COMP-3.
```
Reading: `WS-ACCOUNT-RECORD` has `WS-ACCOUNT-ID` (10-digit integer), `WS-ACCOUNT-STATUS` (2-char with named values for 3 states), `WS-BALANCE` (signed decimal 11+2 packed), and a nested group with two limit fields.

---

### PICTURE Clause Characters

| Character | Meaning | Example | Interpretation |
|---|---|---|---|
| `9` | Numeric digit | `PIC 9(10)` | 10-digit unsigned integer |
| `A` | Alphabetic character | `PIC A(20)` | 20-char alphabetic only |
| `X` | Alphanumeric | `PIC X(35)` | 35-char general string |
| `S` | Signed | `PIC S9(5)` | Signed 5-digit integer |
| `V` | Implied decimal point | `PIC S9(7)V99` | Signed 7+2 decimal (e.g., $99999.99) |
| `P` | Scaling position | `PIC 9(3)P(2)` | Implies trailing zeros |
| `.` | Actual decimal point | `PIC 99.99` | Display/print items only |
| `Z` | Zero-suppress | `PIC ZZZ,ZZ9` | Leading zeros replaced by spaces |
| `$` | Currency symbol | `PIC $ZZZ.99` | Display formatting |
| `+`/`-` | Sign character | `PIC +99.99` | Display with sign |
| `B` | Blank insert | `PIC 9999B99` | Inserts space at position |

---

### Storage Formats (USAGE Clause)

| Clause | Storage Format | When Used |
|---|---|---|
| `DISPLAY` (default) | Character (EBCDIC on z/OS) | Input/output, character fields |
| `COMP` / `BINARY` / `COMP-4` | Binary integer | Subscripts, loop counters, integer keys |
| `COMP-1` | Single-precision float | Rarely — avoid for financials |
| `COMP-2` | Double-precision float | Rarely — avoid for financials |
| `COMP-3` / `PACKED-DECIMAL` | Packed decimal (2 digits per byte) | **Financial calculations — the standard for money** |
| `COMP-5` | Native binary | Platform-specific integer |
| `INDEX` | Internal index for table access | OCCURS table indexes |

**Critical:** `COMP-3` packed decimal → Java `BigDecimal`. Never `double` or `float`. Financial precision loss is a production defect.

---

### REDEFINES Clause

Allows multiple data definitions to occupy the same storage. A major source of complexity.

```cobol
01 WS-DATE-WORK.
   05 WS-DATE-ISO      PIC X(8).
   05 WS-DATE-NUMERIC REDEFINES WS-DATE-ISO.
      10 WS-YEAR       PIC 9(4).          <- same bytes, read as YYYY
      10 WS-MONTH      PIC 9(2).          <- same bytes, read as MM
      10 WS-DAY        PIC 9(2).          <- same bytes, read as DD
```

**When documenting:** always flag REDEFINES. Identify all names and what each shape represents. In modernization, this typically becomes a union type or a separate parsing method.

---

### OCCURS Clause (Arrays/Tables)

```cobol
05 WS-VELOCITY-ARRAY OCCURS 7 TIMES
   INDEXED BY WS-VEL-IDX
   PIC S9(9)V99 COMP-3.
```
An array of 7 packed-decimal values. In modernization: a fixed array or list.

```cobol
05 WS-MCC-TABLE OCCURS 1 TO 100 TIMES
   DEPENDING ON WS-MCC-COUNT
   PIC 9(4).
```
Variable-length OCCURS: size determined by `WS-MCC-COUNT` at runtime. In modernization: a dynamic list.

---

### COPY Statement

```cobol
COPY ACCTRECRD.
COPY TXNHDR REPLACING ==WS-TXN== BY ==WS-REQUEST==.
```
`COPY` includes an external copybook (like a header file or shared class). When analyzing:
1. Flag the copybook name as an external dependency
2. Note what it defines (record layout, constants, working storage)
3. If unavailable, mark as `[COPYBOOK NOT AVAILABLE — record layout unknown]`
4. Copybooks define shared contracts → become shared classes/records in modernization

---

## Procedure Division Patterns

### PERFORM — Subroutine Invocation

```cobol
PERFORM 2000-VALIDATE-CARD.
PERFORM 3000-CHECK-LIMITS THRU 3000-CHECK-LIMITS-EXIT.
PERFORM 4000-PROCESS-TXN UNTIL WS-EOF-FLAG = 'Y'.
PERFORM 5000-VELOCITY-CHECK VARYING WS-IDX FROM 1 BY 1 UNTIL WS-IDX > 7.
```
- `PERFORM paragraph-name` → method call
- `PERFORM ... THRU ...-EXIT` → execute a range of paragraphs (common structured exit via `GO TO`)
- `PERFORM ... UNTIL` → loop (condition checked BEFORE first execution — pre-condition loop)
- `PERFORM ... WITH TEST AFTER UNTIL` → do-while (condition after first execution)
- `PERFORM ... VARYING` → counted loop

**Document all PERFORM statements.** Target paragraphs = subroutine call graph, essential for understanding structure.

---

### EVALUATE — Decision Tables

```cobol
EVALUATE WS-RETURN-CODE
    WHEN 0000
        MOVE 'APPROVED'     TO WS-DECISION
    WHEN 0051
        MOVE 'DECLINED'     TO WS-DECISION
        MOVE 'INSUF-FUNDS'  TO WS-DECLINE-REASON
    WHEN OTHER
        PERFORM 9000-UNEXPECTED-RESPONSE
END-EVALUATE.
```
`EVALUATE` = switch/case. Maps to a modern switch expression or strategy pattern.

```cobol
EVALUATE TRUE
    WHEN WS-CARD-LOST OR WS-CARD-STOLEN
        MOVE 'BLOCKED-CARD'    TO WS-DECLINE-REASON
    WHEN WS-ACCOUNT-DELINQUENT
        MOVE 'BLOCKED-ACCOUNT' TO WS-DECLINE-REASON
    WHEN WS-OVER-LIMIT
        MOVE 'OVER-LIMIT'      TO WS-DECLINE-REASON
END-EVALUATE.
```
`EVALUATE TRUE` = multi-branch if-else idiom. Each WHEN is a boolean condition.

**Every EVALUATE block is a business rule.** Extract each WHEN condition and outcome as a named rule (RULE-NNN). These are the entries for your business rules register.

---

### MOVE — Data Assignment

```cobol
MOVE WS-ACCOUNT-ID    TO DB-ACCOUNT-KEY.
MOVE SPACES           TO WS-ERROR-MSG.       <- clears string field
MOVE ZEROS            TO WS-BALANCE-WORK.    <- clears numeric field
MOVE 'Y'              TO WS-FRAUD-FLAG.
MOVE CORRESPONDING WS-INPUT-RECORD TO WS-WORK-RECORD.
```
`MOVE CORRESPONDING` copies fields with matching names between two records — flag this. If field names partially match, it may copy unintended fields. Always verify all fields in both records.

---

### COMPUTE — Arithmetic

```cobol
COMPUTE WS-AVAILABLE-CREDIT ROUNDED =
    WS-CREDIT-LIMIT - WS-CURRENT-BALANCE - WS-PENDING-AUTH-TOTAL.
```
`ROUNDED` applies standard rounding. In modernization: use `BigDecimal.setScale(2, RoundingMode.HALF_UP)` to replicate COBOL rounding. **This matters for financial amounts.**

Also used: `ADD`, `SUBTRACT`, `MULTIPLY`, `DIVIDE`. Note `DIVIDE ... REMAINDER` → maps to `%` operator.

---

### File Operations

```cobol
OPEN INPUT  TRANSACTION-FILE.
OPEN I-O    ACCOUNT-VSAM.

READ TRANSACTION-FILE INTO WS-TXN-RECORD
    AT END     MOVE 'Y' TO WS-EOF-FLAG
    NOT AT END PERFORM 2000-PROCESS-RECORD
END-READ.

READ ACCOUNT-VSAM
    KEY IS ACC-CARD-NUM
    INVALID KEY     PERFORM 9100-ACCOUNT-NOT-FOUND
    NOT INVALID KEY CONTINUE
END-READ.

REWRITE ACCOUNT-VSAM-RECORD.
WRITE AUDIT-RECORD FROM WS-AUDIT-WORK.
DELETE ACCOUNT-VSAM-RECORD.

CLOSE TRANSACTION-FILE ACCOUNT-VSAM.
```
**Document for each file:** name, organization, access mode, open mode (INPUT/OUTPUT/I-O/EXTEND), operations (READ/WRITE/REWRITE/DELETE), and key field used for indexed access.

---

### EXEC SQL — DB2 Database Access

```cobol
EXEC SQL
    SELECT ACCT_STATUS, CREDIT_LIMIT, CURRENT_BALANCE
    INTO   :WS-ACCT-STATUS, :WS-CREDIT-LIMIT, :WS-BALANCE
    FROM   ACCT_MASTER
    WHERE  CARD_NUMBER = :WS-CARD-NUM
END-EXEC.

IF SQLCODE = 0
    CONTINUE
ELSE IF SQLCODE = +100
    PERFORM 9200-ACCOUNT-NOT-FOUND
ELSE
    PERFORM 9999-DB-ERROR
END-IF.
```

**Document for each EXEC SQL:** table name, columns, WHERE predicates and their data sources, SQLCODE handling.

Common SQLCODE values:
| SQLCODE | Meaning |
|---|---|
| `0` | Success |
| `+100` | Row not found (SELECT INTO or FETCH returned nothing) |
| `-803` | Duplicate key on INSERT |
| `-811` | Multiple rows returned for SELECT INTO (expected exactly one) |
| `-922` | Authorization failure |

**DB2 Cursors (result sets):**
```cobol
EXEC SQL
    DECLARE TRANS-CURSOR CURSOR FOR
    SELECT TXN_ID, TXN_AMOUNT, TXN_DATE
    FROM   TRANSACTION_HISTORY
    WHERE  ACCOUNT_ID = :WS-ACCOUNT-ID
    ORDER BY TXN_DATE DESC
END-EXEC.

EXEC SQL OPEN TRANS-CURSOR END-EXEC.

PERFORM UNTIL SQLCODE = +100
    EXEC SQL
        FETCH TRANS-CURSOR
        INTO :WS-TXN-ID, :WS-TXN-AMT, :WS-TXN-DATE
    END-EXEC
    IF SQLCODE = 0
        PERFORM 3000-PROCESS-TRANSACTION
    END-IF
END-PERFORM.

EXEC SQL CLOSE TRANS-CURSOR END-EXEC.
```
Cursor FETCH loop → Stream or paginated query in modern Java.

---

### EXEC CICS — Transaction Services

CICS commands are used in online transaction programs.

```cobol
EXEC CICS RETURN                                        END-EXEC.  <- End transaction
EXEC CICS RETURN TRANSID('AUTH') COMMAREA(WS-COMMAREA)
          LENGTH(WS-COMMAREA-LEN)                       END-EXEC.  <- Pseudo-conversational

EXEC CICS LINK PROGRAM('AUTHSUB1')
          COMMAREA(WS-LINK-AREA)
          LENGTH(WS-LINK-LEN)
          RESP(WS-CICS-RESP)                            END-EXEC.  <- Synchronous call

EXEC CICS XCTL PROGRAM('AUTHMENU')
          COMMAREA(WS-XCTL-DATA)                        END-EXEC.  <- Transfer (no return)

EXEC CICS SEND MAP('AUTHMAP1') MAPSET('AUTHMAPS')
          FROM(WS-MAP-DATA) ERASE                       END-EXEC.  <- Send screen

EXEC CICS RECEIVE MAP('AUTHMAP1') MAPSET('AUTHMAPS')
          INTO(WS-MAP-DATA)                             END-EXEC.  <- Receive input

EXEC CICS WRITEQ TD QUEUE('AUDT')
          FROM(WS-AUDIT-REC) LENGTH(WS-AUDIT-LEN)       END-EXEC.  <- Transient data queue

EXEC CICS ENQUEUE RESOURCE(WS-ACCOUNT-ID) LENGTH(10)   END-EXEC.  <- Lock resource
EXEC CICS DEQUEUE RESOURCE(WS-ACCOUNT-ID) LENGTH(10)   END-EXEC.  <- Release lock
```

**EXEC CICS LINK** = synchronous program-to-program call → REST call or method call in modernization. Document the target program name and COMMAREA structure (this is the API contract).

**EXEC CICS RETURN with TRANSID** = pseudo-conversational pattern. Program ends but leaves a "continue on next input" marker. In modernization → stateless REST with a session token or state persisted in a database.

**EXEC CICS LINK vs EXEC CICS XCTL:** LINK returns to caller. XCTL transfers control permanently — no return. Critical distinction for modernization.

**EIBRESP check pattern:**
```cobol
EVALUATE WS-CICS-RESP
    WHEN DFHRESP(NORMAL)   CONTINUE
    WHEN DFHRESP(PGMIDERR) PERFORM 9100-PROGRAM-NOT-FOUND
    WHEN OTHER             PERFORM 9999-CICS-ERROR
END-EVALUATE.
```
`DFHRESP(NORMAL)` = 0 (success). Document all RESP handling to understand error paths.

---

## Common Patterns to Identify

### Main Processing Loop (Batch)
```cobol
PERFORM UNTIL WS-EOF-FLAG = 'Y'
    READ TRANSACTION-FILE INTO WS-TXN-RECORD
        AT END MOVE 'Y' TO WS-EOF-FLAG
    END-READ
    IF WS-EOF-FLAG = 'N'
        PERFORM 2000-PROCESS-TRANSACTION
    END-IF
END-PERFORM.
```
Classic batch driver loop → Spring Batch `ItemReader`/`ItemProcessor`/`ItemWriter` chain.

### Error Handling with Return Code Evaluation
```cobol
PERFORM 3000-UPDATE-ACCOUNT.
EVALUATE WS-RETURN-CODE
    WHEN +0    CONTINUE
    WHEN +4    PERFORM 8100-WARN-PARTIAL-UPDATE
    WHEN +8    PERFORM 9100-FATAL-UPDATE-ERROR
    WHEN OTHER PERFORM 9999-UNEXPECTED-RC
END-EVALUATE.
```
`WS-RETURN-CODE` pattern → exception types or `Result<T, Error>` in modern code.

### Date Arithmetic (Risk Area)
```cobol
*  Julian date (YYDDD) — watch for Y2K-era workarounds:
IF WS-YEAR-2DIGIT < 50
    MOVE 2000 TO WS-CENTURY
ELSE
    MOVE 1900 TO WS-CENTURY.
```
**Date logic is a major modernization risk.** Document every date field: format (YYYYMMDD, YYMMDD, Julian YYDDD, packed decimal), century-handling workarounds. In modernization → `LocalDate`, eliminate all 2-digit year logic.

### Flag Variables / State Tracking
```cobol
01 WS-FLAGS.
   05 WS-FRAUD-FLAG       PIC X(1).
      88 FRAUD-DETECTED   VALUE 'Y'.
      88 FRAUD-CLEAR      VALUE 'N' 'C'.
   05 WS-HOLD-FLAG        PIC X(1).
      88 HOLD-APPLIED     VALUE 'Y'.
      88 NO-HOLD          VALUE 'N'.
```
Flag variables with 88-levels are state machines in disguise. The 88-level names are the state names. In modernization → enums or dedicated state fields. Document all flags and possible values — they encode business rules.

---

## What to Document in a COBOL Analysis

When producing a COBOL analysis (e.g., for T2 Decision Inventory in pre-engagement):

### 1. Program Classification
- **Program type:** Batch driver / Batch sub-program / CICS online program / Called sub-program
- **Invocation mechanism:** JCL scheduled job / CICS transaction code / Called via CALL
- **Approximate size:** Lines of code, number of paragraphs/sections

### 2. Entry Points and Parameters
- `PROCEDURE DIVISION USING` parameters (name and type)
- CICS COMMAREA structure
- Return code mechanism (`MOVE X TO RETURN-CODE` or `WS-RETURN-CODE`)

### 3. Files Accessed
| File Name | Organization | Access Mode | Open Mode | Key Field | Operations |
|---|---|---|---|---|---|
| TRANSACTION-FILE | SEQUENTIAL | SEQUENTIAL | INPUT | N/A | READ |
| ACCOUNT-VSAM | INDEXED | DYNAMIC | I-O | ACC-CARD-NUM | READ, REWRITE |

### 4. DB2 Tables Accessed
| Table Name | Operation | Key Columns | Data Columns Used | Cursor? |
|---|---|---|---|---|
| ACCT_MASTER | SELECT | CARD_NUMBER | ACCT_STATUS, CREDIT_LIMIT | No |
| TRANSACTION_HISTORY | SELECT | ACCOUNT_ID, TXN_DATE | TXN_ID, TXN_AMOUNT | Yes |

### 5. External Program Calls
| Call Type | Target Program | COMMAREA/Parameters | Synchronous? | Purpose |
|---|---|---|---|---|
| EXEC CICS LINK | AUTHSUB1 | WS-AUTH-COMMAREA | Yes | Fraud score lookup |
| CALL | DATECALC | BY REFERENCE WS-DATE-PARMS | Yes | Date conversion |

### 6. Side Effects
- Files written or updated (with conditions: "writes to AUDIT-FILE for every processed transaction")
- DB2 rows inserted/updated/deleted (with conditions: "updates ACCT_MASTER balance after approval")
- CICS queues written (TD or TS queue names and conditions)
- Return code set for caller (values and what they mean)

### 7. Error Handling
Every error path:
- SQLCODE checks: values handled and action taken
- CICS RESP checks: values handled and action taken
- File status checks: values handled and action taken
- Business error paths: EVALUATE branches resulting in decline/error decisions

### 8. Business Decision Logic
Every EVALUATE block that affects a business outcome → named business rule:
```
RULE-AUTH-001: Card Status Check
CONDITION: WS-CARD-STATUS = 'BL' (Blocked) OR 'LO' (Lost) OR 'ST' (Stolen)
ACTION: Decline transaction with ISO 8583 code 0062
```

---

## Modernization Mapping Reference

| COBOL Construct | Modern Java Equivalent | Notes |
|---|---|---|
| `PROCEDURE DIVISION` paragraph | Java method | Named paragraphs → named methods (preserve names) |
| `PERFORM paragraph` | Method call | Direct mapping |
| `EVALUATE` | `switch` expression, strategy pattern | Complex multi-factor EVALUATE → rule engine |
| `IF/ELSE` chain | `if/else if/else` | Direct mapping |
| Copybook `01` record | Java record or DTO class | Shared copybooks → shared library types |
| `PIC S9(n)V99 COMP-3` | `BigDecimal` with `scale(2)` | **Never `double` or `float`** |
| `PIC 9(n) COMP` / `COMP-4` | `int` or `long` | 9(9) → int; 9(18) → long |
| `PIC X(n)` | `String` | Fixed-length — may need `.trim()` |
| `88` condition name | Enum constant | Preserve the business name |
| `EXEC SQL SELECT` | JPA Query, JDBC, R2DBC | |
| `EXEC SQL INSERT/UPDATE/DELETE` | JPA save/update, JDBC | Wrap in `@Transactional` |
| DB2 cursor | `Stream` / paginated query | FETCH loop → iterator/stream |
| `EXEC CICS LINK` | REST call, gRPC, event publish | Check if target program is also in modernization scope |
| `EXEC CICS SEND/RECEIVE MAP` | REST endpoint + UI | BMS map → HTML form or REST contract |
| `EXEC CICS ENQUEUE/DEQUEUE` | Distributed lock (Redis, DB-level) | Identify the resource and why it's locked |
| `EXEC CICS WRITEQ TD` | Kafka message / JMS | Transient data queue → async event |
| VSAM INDEXED file | JPA entity + database table | Key field → primary key |
| VSAM SEQUENTIAL file | Spring Batch flat file reader | Batch → Spring Batch step |
| Batch PERFORM UNTIL EOF | Spring Batch job / Kafka stream | |
| `WS-RETURN-CODE` pattern | Exception type / `Result<T, Error>` | Map specific codes to exception subtypes |
| Julian date (YYDDD) | `LocalDate` | Eliminate all Julian date arithmetic |
| `COMP-1` / `COMP-2` float | `BigDecimal` (if financial) | Flag float for financial values as a defect |
| `REDEFINES` | Union type or parsing method | Flag all occurrences — each shape needs documentation |
| `MOVE CORRESPONDING` | Manual field-by-field copy | Verify all matched field names explicitly |

---

## Key Patterns Summary

- **`PERFORM ... UNTIL EOF + READ AT END`** — the universal batch driver loop
- **`EVALUATE WS-RETURN-CODE`** — the universal error dispatch mechanism
- **`EVALUATE TRUE WHEN condition`** — boolean multi-branch decision (if-else)
- **`EXEC CICS LINK + COMMAREA`** — synchronous program call (equivalent to REST call)
- **`EXEC CICS RETURN TRANSID + COMMAREA`** — pseudo-conversational stateful session
- **`01 record + 05 fields + 88 conditions`** — typed state variable with named values
- **`REDEFINES`** — union type, same memory with two interpretations
- **`COPY copybook`** — shared type/contract definition
- **`COMP-3 V99 COMPUTE ROUNDED`** — the standard financial arithmetic pattern

---

## Anti-Patterns — What Not To Do

- **Do not map `COMP-3` to floating-point.** `COMP-3` packed decimal → `BigDecimal`. Non-negotiable for financial fields.
- **Do not ignore `REDEFINES`.** Same storage for two things — failing to document both shapes produces incorrect mappings.
- **Do not guess at copybook contents.** If a COPY member is unavailable, mark it `[COPYBOOK NOT AVAILABLE — record structure unknown]` and stop.
- **Do not flatten `EVALUATE` to if/else without extracting it as a named business rule.** Each EVALUATE block is a business rule for the rule register.
- **Do not ignore `88`-level condition names.** They are named values — the names encode business meaning. Document as enum candidates.
- **Do not treat all date fields as equal.** Identify the format, century handling, and comparison method for each date field.
- **Do not skip SQLCODE handling.** Every EXEC SQL must have SQLCODE checked. Undocumented paths are potential data integrity issues.
- **Do not confuse `EXEC CICS LINK` with `EXEC CICS XCTL`.** LINK returns to caller. XCTL transfers control without returning.
- **Do not omit side effects.** Every file write, DB update, and queue write must be replicated exactly in the modernized system. Missing side effects are the most common source of behavioral divergence.

---

## Expected AI Behavior When This File Is Loaded

When analyzing a COBOL program with this file loaded, the AI must:

1. Identify and name each Division present (or note if missing)
2. Parse all DATA DIVISION items → structured data dictionary: level number, field name, PIC clause, USAGE format, REDEFINES, OCCURS, 88-level names
3. Flag every COPY as an external dependency requiring the copybook for complete analysis
4. List all EXEC SQL blocks: table name, operation, predicates, SQLCODE handling
5. List all EXEC CICS commands: purpose, target program/resource, RESP handling
6. Extract business rules from every EVALUATE block — name the rule, don't just describe the code
7. Document all side effects — file writes, DB updates, queue writes
8. Produce a modernization mapping for every non-trivial construct
9. Never guess at copybook contents, DB schemas, or external program behavior — flag unknowns explicitly
10. Use the "What to Document" section format above for all analysis outputs

---

*FORGE Legacy COBOL Reading Guide | Domain: Mainframe Modernization | Version 2.0*
