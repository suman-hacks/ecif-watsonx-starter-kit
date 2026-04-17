# Legacy COBOL Knowledge Pack

## Purpose

This pack teaches an AI coding assistant how to read, analyze, and accurately document COBOL programs. It covers program structure, data definition interpretation, procedure division patterns, CICS and DB2 integration idioms, and the mapping from COBOL constructs to modern equivalents. Load this pack when working on any task that involves reading, understanding, or transforming COBOL source code.

## When to Load

- Pre-engagement analysis (Stage 00): Analyzing COBOL programs before a discovery workshop
- Discovery (Stage 01): Understanding what a legacy COBOL system does
- Architecture (Stage 03): Mapping legacy COBOL capabilities to target microservice design
- Development (Stage 05): Transforming COBOL logic into Java, Python, or another target language
- Testing (Stage 06): Generating test cases that prove parity between COBOL and modern implementation
- Any task where the AI must read a `.cbl`, `.cob`, `.copy`, or `.jcl` file and produce a structured analysis

## Inputs This Pack Requires

- Source COBOL file(s) loaded into the AI session's context
- Any COPY members (copybooks) referenced in the program, if available
- DB2 bind files or catalog information if SQL analysis is required (optional — flag as unknown if unavailable)
- CICS map definitions (BMS) if online transaction analysis is required (optional)
- The project context file: what is being modernized and why

---

## Core Knowledge

### COBOL Program Structure

A standard COBOL program is organized into four Divisions, always in this order. Each Division is mandatory (some may be minimal if not needed).

#### IDENTIFICATION DIVISION
Program metadata. Always present.
```cobol
IDENTIFICATION DIVISION.
PROGRAM-ID.   AUTHZ001.
AUTHOR.        JOHN DOE.
DATE-WRITTEN.  2003-07-15.
REMARKS.       AUTHORIZATION DECISION ENGINE FOR CREDIT CARDS.
```
Key field: `PROGRAM-ID` — this is the program's name and the value used in CALL statements from other programs.

#### ENVIRONMENT DIVISION
System interface definitions. Declares file assignments connecting logical file names to physical files (JCL DD names).
```cobol
ENVIRONMENT DIVISION.
CONFIGURATION SECTION.
    SOURCE-COMPUTER.  IBM-390.
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
Key items to document: file names, organization (SEQUENTIAL, INDEXED, RELATIVE), access mode (SEQUENTIAL, RANDOM, DYNAMIC), record key fields.

#### DATA DIVISION
All data definitions. Most complex and information-dense division. Contains multiple sections.

**FILE SECTION** — describes the record layout of files declared in ENVIRONMENT DIVISION.
**WORKING-STORAGE SECTION** — program working variables, constants, flags, and work areas.
**LINKAGE SECTION** — parameters passed to/from this program by callers (CALL statement or CICS COMMAREA).
**LOCAL-STORAGE SECTION** — like WORKING-STORAGE but initialized fresh for each invocation; important in multi-threaded CICS environments.

#### PROCEDURE DIVISION
Contains all executable business logic. May begin with:
```cobol
PROCEDURE DIVISION.
```
or with parameter specification when the program is called:
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

### Data Definition Reading Guide

#### Level Numbers

Level numbers define the hierarchy of data items. Read them as a tree structure where higher-numbered items are children of lower-numbered items.

| Level | Meaning | Example |
|---|---|---|
| `01` | Top-level record or standalone group | `01 WS-ACCOUNT-RECORD.` |
| `02`–`49` | Fields or sub-groups within a record | `05 WS-ACCOUNT-ID PIC 9(10).` |
| `66` | RENAMES clause — alias for a range of fields | Rarely used; flag when seen |
| `77` | Standalone elementary item (not in a group) | `77 WS-EOF-FLAG PIC X VALUE 'N'.` |
| `88` | Condition name — named value for a field | `88 EOF-REACHED VALUE 'Y'.` |

Practical example — reading a data structure:
```cobol
01 WS-ACCOUNT-RECORD.
   05 WS-ACCOUNT-ID      PIC 9(10).
   05 WS-ACCOUNT-STATUS  PIC X(2).
      88 ACCT-ACTIVE     VALUE 'AC'.
      88 ACCT-BLOCKED    VALUE 'BL'.
      88 ACCT-EXPIRED    VALUE 'EX'.
   05 WS-BALANCE         PIC S9(11)V99 COMP-3.
   05 WS-LIMIT-GROUP.
      10 WS-CREDIT-LIMIT PIC S9(9)V99 COMP-3.
      10 WS-DAILY-LIMIT  PIC S9(7)V99 COMP-3.
```
Reading this: `WS-ACCOUNT-RECORD` is a record with `WS-ACCOUNT-ID` (10-digit number), `WS-ACCOUNT-STATUS` (2-char code, with named values for ACTIVE/BLOCKED/EXPIRED), `WS-BALANCE` (signed decimal, 11 digits + 2 decimal places, packed), and a nested group `WS-LIMIT-GROUP` containing two limit fields.

#### PICTURE Clause Characters

| Character | Meaning | Example | Interpretation |
|---|---|---|---|
| `9` | Numeric digit | `PIC 9(10)` | 10-digit unsigned integer |
| `A` | Alphabetic character | `PIC A(20)` | 20-char alphabetic only |
| `X` | Alphanumeric | `PIC X(35)` | 35-char general string |
| `S` | Signed | `PIC S9(5)` | Signed 5-digit integer |
| `V` | Implied decimal point | `PIC S9(7)V99` | Signed 7+2 decimal (e.g., $99999.99) |
| `P` | Scaling position | `PIC 9(3)P(2)` | Implies trailing zeros |
| `.` | Actual decimal point | `PIC 99.99` | In display/print items only |
| `,` | Comma edit | `PIC ZZ,ZZZ` | Display formatting only |
| `Z` | Zero-suppress | `PIC ZZZ,ZZ9` | Leading zeros replaced by spaces |
| `$` | Currency symbol | `PIC $ZZZ.99` | Display formatting |
| `+`/`-` | Sign character | `PIC +99.99` | Display with sign |
| `B` | Blank insert | `PIC 9999B99` | Inserts space at position |

#### Storage Formats (USAGE Clause)

| Clause | Storage Format | When Used |
|---|---|---|
| `DISPLAY` (default) | Character (EBCDIC on z/OS) | Input/output, character fields |
| `COMP` or `BINARY` or `COMP-4` | Binary integer | Subscripts, loop counters, integer keys |
| `COMP-1` | Single-precision float | Rarely — avoid for financials |
| `COMP-2` | Double-precision float | Rarely — avoid for financials |
| `COMP-3` or `PACKED-DECIMAL` | Packed decimal (2 digits per byte) | **Financial calculations — the standard for money** |
| `COMP-5` | Native binary | Platform-specific integer |
| `INDEX` | Internal index for table access | OCCURS table indexes |

**Critical for modernization:** `COMP-3` packed decimal fields map to Java `BigDecimal`. Never map them to `double` or `float`. Loss of precision in financial calculations is a production defect.

#### REDEFINES Clause

`REDEFINES` allows multiple data definitions to occupy the same storage location. This is a major source of complexity in COBOL programs.

```cobol
01 WS-DATE-WORK.
   05 WS-DATE-ISO      PIC X(8).         <- "20231205" as string
   05 WS-DATE-NUMERIC REDEFINES WS-DATE-ISO.
      10 WS-YEAR       PIC 9(4).         <- same bytes, read as YYYY
      10 WS-MONTH      PIC 9(2).         <- same bytes, read as MM
      10 WS-DAY        PIC 9(2).         <- same bytes, read as DD
```

**When documenting REDEFINES:** always flag it. The program uses the same memory for two (or more) different data shapes. Identify all the names and what each shape represents. In modernization, this typically becomes a union type or a separate parsing method.

#### OCCURS Clause (Arrays/Tables)

```cobol
05 WS-VELOCITY-ARRAY OCCURS 7 TIMES
   INDEXED BY WS-VEL-IDX
   PIC S9(9)V99 COMP-3.
```
Read as: an array of 7 packed-decimal values, indexed by `WS-VEL-IDX`. In modernization, this becomes an array or list.

```cobol
05 WS-MCC-TABLE OCCURS 1 TO 100 TIMES
   DEPENDING ON WS-MCC-COUNT
   PIC 9(4).
```
Variable-length OCCURS: the actual size is determined by `WS-MCC-COUNT` at runtime. Treat as a dynamic list in the target language.

**Index vs Subscript:** OCCURS tables can be accessed via INDEX (set with `SET`) or subscript (plain numeric field). Note which is used — index access is slightly faster in COBOL but both access the same array.

#### COPY Statement

```cobol
COPY ACCTRECRD.
COPY TXNHDR REPLACING ==WS-TXN== BY ==WS-REQUEST==.
```
`COPY` includes an external copybook (like a header file or shared class). Every COPY is an external dependency. When analyzing:
1. Flag the copybook name
2. Note what it defines (record layout, constants, working storage?)
3. If the copybook is not available, mark as `[COPYBOOK NOT AVAILABLE — record layout unknown]`
4. Copybooks often define shared record contracts — they become shared classes/records in modernization

---

### Procedure Division Patterns

#### PERFORM — Subroutine Invocation

```cobol
PERFORM 2000-VALIDATE-CARD.
PERFORM 3000-CHECK-LIMITS THRU 3000-CHECK-LIMITS-EXIT.
PERFORM 4000-PROCESS-TXN UNTIL WS-EOF-FLAG = 'Y'.
PERFORM 5000-VELOCITY-CHECK VARYING WS-IDX FROM 1 BY 1 UNTIL WS-IDX > 7.
```

- `PERFORM paragraph-name` — equivalent to a method call
- `PERFORM ... THRU ...-EXIT` — execute a range of paragraphs, common pattern for structured exit via `GO TO ...-EXIT`
- `PERFORM ... UNTIL` — loop until condition is true (check condition BEFORE first execution in standard COBOL)
- `PERFORM ... WITH TEST AFTER UNTIL` — check condition AFTER first execution (do-while equivalent)
- `PERFORM ... VARYING` — counted loop

**When analyzing PERFORM:** list all PERFORM statements and their target paragraphs. These are your subroutine call graph — essential for understanding program structure.

#### EVALUATE — Decision Tables

```cobol
EVALUATE WS-RETURN-CODE
    WHEN 0000
        MOVE 'APPROVED'  TO WS-DECISION
    WHEN 0051
        MOVE 'DECLINED'  TO WS-DECISION
        MOVE 'INSUF-FUNDS' TO WS-DECLINE-REASON
    WHEN 0062
        MOVE 'DECLINED'  TO WS-DECISION
        MOVE 'RESTRICT-CARD' TO WS-DECLINE-REASON
    WHEN OTHER
        PERFORM 9000-UNEXPECTED-RESPONSE
END-EVALUATE.
```

`EVALUATE` is COBOL's switch/case. It maps directly to a modern switch expression or strategy pattern.

```cobol
EVALUATE TRUE
    WHEN WS-CARD-LOST OR WS-CARD-STOLEN
        MOVE 'BLOCKED-CARD' TO WS-DECLINE-REASON
    WHEN WS-ACCOUNT-DELINQUENT
        MOVE 'BLOCKED-ACCOUNT' TO WS-DECLINE-REASON
    WHEN WS-OVER-LIMIT
        MOVE 'OVER-LIMIT' TO WS-DECLINE-REASON
END-EVALUATE.
```
`EVALUATE TRUE` is an idiom for evaluating complex boolean conditions — each WHEN is an if-else branch.

**When documenting EVALUATE:** every EVALUATE block is a business rule. Extract each WHEN condition and its outcome as a named business rule. These are your business rule candidates for the rule register.

#### MOVE — Data Assignment

```cobol
MOVE WS-ACCOUNT-ID    TO DB-ACCOUNT-KEY.
MOVE SPACES           TO WS-ERROR-MSG.
MOVE ZEROS            TO WS-BALANCE-WORK.
MOVE 'Y'              TO WS-FRAUD-FLAG.
MOVE CORRESPONDING WS-INPUT-RECORD TO WS-WORK-RECORD.
```

`MOVE CORRESPONDING` copies fields with matching names between two records — a source of subtle bugs if field names partially match. Always flag `MOVE CORRESPONDING` as requiring careful verification in modernization.

#### COMPUTE — Arithmetic

```cobol
COMPUTE WS-AVAILABLE-CREDIT ROUNDED =
    WS-CREDIT-LIMIT - WS-CURRENT-BALANCE - WS-PENDING-AUTH-TOTAL.

COMPUTE WS-FEE = WS-TRANSACTION-AMT * WS-FEE-RATE / 100.
```

`ROUNDED` applies standard rounding. In modernization: use `BigDecimal.setScale(2, RoundingMode.HALF_UP)` or equivalent to replicate COBOL rounding behavior. **This matters for financial amounts.**

Also used: `ADD`, `SUBTRACT`, `MULTIPLY`, `DIVIDE`. Note `DIVIDE ... REMAINDER` which captures integer division remainder — map to `%` operator.

#### File Operations

```cobol
OPEN INPUT  TRANSACTION-FILE.
OPEN I-O    ACCOUNT-VSAM.
OPEN OUTPUT AUDIT-FILE.

READ TRANSACTION-FILE INTO WS-TXN-RECORD
    AT END MOVE 'Y' TO WS-EOF-FLAG
    NOT AT END PERFORM 2000-PROCESS-RECORD
END-READ.

READ ACCOUNT-VSAM
    KEY IS ACC-CARD-NUM
    INVALID KEY PERFORM 9100-ACCOUNT-NOT-FOUND
    NOT INVALID KEY CONTINUE
END-READ.

REWRITE ACCOUNT-VSAM-RECORD.
WRITE AUDIT-RECORD FROM WS-AUDIT-WORK.
DELETE ACCOUNT-VSAM-RECORD.

CLOSE TRANSACTION-FILE ACCOUNT-VSAM AUDIT-FILE.
```

**When documenting file operations:** for each file, record: file name, organization, access mode, open mode (INPUT/OUTPUT/I-O/EXTEND), operations performed (READ/WRITE/REWRITE/DELETE), and key field used for indexed access.

#### EXEC SQL — DB2 Database Access

```cobol
EXEC SQL
    SELECT ACCT_STATUS, CREDIT_LIMIT, CURRENT_BALANCE
    INTO   :WS-ACCT-STATUS, :WS-CREDIT-LIMIT, :WS-BALANCE
    FROM   ACCT_MASTER
    WHERE  CARD_NUMBER = :WS-CARD-NUM
    AND    ACCT_STATUS != 'CL'
END-EXEC.

IF SQLCODE = 0
    CONTINUE
ELSE IF SQLCODE = +100
    PERFORM 9200-ACCOUNT-NOT-FOUND
ELSE
    PERFORM 9999-DB-ERROR
END-IF.
```

**When documenting EXEC SQL:** record: table name, columns selected/updated/inserted, WHERE predicates and their data sources, SQLCODE handling, cursor definitions (DECLARE CURSOR, OPEN, FETCH, CLOSE).

Common SQLCODE values:
- `0` — success
- `+100` — row not found (SELECT INTO or FETCH returned nothing)
- `-803` — duplicate key on INSERT
- `-811` — multiple rows returned for SELECT INTO (should return exactly one row)
- `-922` — authorization failure

DB2 cursors for result sets:
```cobol
EXEC SQL
    DECLARE TRANS-CURSOR CURSOR FOR
    SELECT TXN_ID, TXN_AMOUNT, TXN_DATE
    FROM   TRANSACTION_HISTORY
    WHERE  ACCOUNT_ID = :WS-ACCOUNT-ID
    AND    TXN_DATE >= :WS-START-DATE
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

#### EXEC CICS — Transaction Services

CICS commands are used in online transaction programs. Recognize these patterns:

```cobol
EXEC CICS RETURN                           END-EXEC.  <- End transaction
EXEC CICS RETURN TRANSID('AUTH') COMMAREA(WS-COMMAREA)
          LENGTH(WS-COMMAREA-LEN)          END-EXEC.  <- Pseudo-conversational return

EXEC CICS LINK PROGRAM('AUTHSUB1')
          COMMAREA(WS-LINK-AREA)
          LENGTH(WS-LINK-LEN)              END-EXEC.  <- Synchronous call to sub-program

EXEC CICS XCTL PROGRAM('AUTHMENU')
          COMMAREA(WS-XCTL-DATA)           END-EXEC.  <- Transfer control (no return)

EXEC CICS SEND MAP('AUTHMAP1')
          MAPSET('AUTHMAPS')
          FROM(WS-MAP-DATA)
          ERASE                            END-EXEC.  <- Send screen to terminal

EXEC CICS RECEIVE MAP('AUTHMAP1')
          MAPSET('AUTHMAPS')
          INTO(WS-MAP-DATA)               END-EXEC.  <- Receive input from terminal

EXEC CICS GETMAIN SET(ADDRESS OF WS-DYNA-AREA)
          LENGTH(WS-AREA-SIZE)            END-EXEC.  <- Allocate dynamic storage
EXEC CICS FREEMAIN DATA(WS-DYNA-AREA)    END-EXEC.  <- Free dynamic storage

EXEC CICS WRITEQ TD QUEUE('AUDT')
          FROM(WS-AUDIT-REC)
          LENGTH(WS-AUDIT-LEN)            END-EXEC.  <- Write to transient data queue

EXEC CICS READQ TS QUEUE('TMPQ0001')
          INTO(WS-TS-DATA)
          LENGTH(WS-TS-LEN)
          ITEM(WS-TS-ITEM)               END-EXEC.  <- Read from temporary storage

EXEC CICS ENQUEUE RESOURCE(WS-ACCOUNT-ID) LENGTH(10) END-EXEC.  <- Lock resource
EXEC CICS DEQUEUE RESOURCE(WS-ACCOUNT-ID) LENGTH(10) END-EXEC.  <- Release lock
```

**EXEC CICS LINK** is a synchronous program-to-program call — equivalent to a method call or REST call in modern systems. Document the program name and the COMMAREA structure (this is the API contract).

**EXEC CICS RETURN with TRANSID** is the pseudo-conversational pattern — the program terminates but leaves a "continue on next input" marker. This is CICS's way of handling stateful sessions without holding resources between user interactions. In modernization, this becomes stateless REST with a session token or conversational state in a database.

**EIBRESP check after CICS commands:**
```cobol
EXEC CICS LINK PROGRAM('AUTHSUB1')
          COMMAREA(WS-LINK-AREA)
          LENGTH(WS-LINK-LEN)
          RESP(WS-CICS-RESP)    END-EXEC.

EVALUATE WS-CICS-RESP
    WHEN DFHRESP(NORMAL)   CONTINUE
    WHEN DFHRESP(PGMIDERR) PERFORM 9100-PROGRAM-NOT-FOUND
    WHEN OTHER             PERFORM 9999-CICS-ERROR
END-EVALUATE.
```
`DFHRESP(NORMAL)` = 0 (success). Always document the RESP handling to understand error paths.

---

### Common Patterns to Identify

#### Main Processing Loop (Batch)
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
Classic batch driver loop. In modernization, this becomes a Spring Batch `ItemReader`/`ItemProcessor`/`ItemWriter` chain or a Kafka stream processor.

#### Error Handling with Return Code Evaluation
```cobol
PERFORM 3000-UPDATE-ACCOUNT.
EVALUATE WS-RETURN-CODE
    WHEN +0    CONTINUE
    WHEN +4    PERFORM 8100-WARN-PARTIAL-UPDATE
    WHEN +8    PERFORM 9100-FATAL-UPDATE-ERROR
    WHEN OTHER PERFORM 9999-UNEXPECTED-RC
END-EVALUATE.
```
`WS-RETURN-CODE` is a common pattern for returning status from a sub-section. Map to modern exception types or `Result<T, Error>` patterns.

#### Date Arithmetic and Century Handling
```cobol
MOVE FUNCTION CURRENT-DATE TO WS-CURRENT-DATE.
*  WS-CURRENT-DATE format: YYYYMMDDHHMMSS

*  Julian date (YYDDD) processing — watch for Y2K-era workarounds:
IF WS-YEAR-2DIGIT < 50
    MOVE 2000 TO WS-CENTURY
ELSE
    MOVE 1900 TO WS-CENTURY.

*  Date comparison using packed decimal representation:
IF WS-EXPIRY-DATE < WS-CURRENT-YYMMDD
    PERFORM 5000-CARD-EXPIRED.
```
**Date logic is a major modernization risk area.** Document every date field, its format (YYYYMMDD, YYMMDD, Julian YYDDD, packed decimal), and any century-handling workarounds. In modernization, map to `LocalDate` and eliminate all 2-digit year logic.

#### Packed Decimal Arithmetic (Financial)
```cobol
01 WS-CALC-AREA.
   05 WS-TXN-AMOUNT      PIC S9(9)V99  COMP-3.
   05 WS-CASH-BACK       PIC S9(7)V99  COMP-3.
   05 WS-FEE-AMOUNT      PIC S9(7)V99  COMP-3.
   05 WS-TOTAL-DEBIT     PIC S9(11)V99 COMP-3.

COMPUTE WS-TOTAL-DEBIT ROUNDED =
    WS-TXN-AMOUNT + WS-CASH-BACK + WS-FEE-AMOUNT.
```
`COMP-3` with `V99` = exactly 2 decimal places, rounding specified. This is the standard for all financial calculations in COBOL. **Never map to floating point in the target language.** Always map to `BigDecimal` with explicit scale and rounding mode.

#### Flag Variables / State Tracking
```cobol
01 WS-FLAGS.
   05 WS-FRAUD-FLAG       PIC X(1).
      88 FRAUD-DETECTED   VALUE 'Y'.
      88 FRAUD-CLEAR      VALUE 'N' 'C'.
   05 WS-VELOCITY-FLAG    PIC X(1).
      88 VELOCITY-EXCEEDED VALUE 'Y'.
      88 VELOCITY-OK       VALUE 'N'.
   05 WS-HOLD-FLAG        PIC X(1).
      88 HOLD-APPLIED      VALUE 'Y'.
      88 NO-HOLD           VALUE 'N'.
```
Flag variables with 88-levels are state machines in disguise. The 88-level names are the state names. In modernization, map to enums or dedicated state fields with clear semantics. Document all flags and their possible values — these encode business rules.

#### COPY Libraries (Shared Record Definitions)
```cobol
COPY ACCTRECRD.      <- Account master record layout
COPY TXNCOMM.        <- Transaction COMMAREA layout
COPY ERRTABLE.       <- Error code table
COPY DFHAID.         <- CICS attention identifier definitions (standard IBM copybook)
COPY DFHBMSCA.       <- CICS BMS attribute definitions (standard IBM copybook)
```
IBM-standard copybooks (`DFHAID`, `DFHBMSCA`, `DFHCOMMAREA`) can be looked up in IBM documentation. Internal copybooks are shared contracts — they become shared library classes or API schema definitions in modernization. Note every COPY and what it contributes.

---

### What to Document in a COBOL Analysis

When producing a COBOL analysis document (e.g., T2 DECISIONS for pre-engagement), include all of the following sections:

#### 1. Program Classification
- **Program type:** Batch driver / Batch sub-program / CICS online program / Called sub-program (subroutine)
- **Invocation mechanism:** Scheduled JCL job / CICS transaction code / Called via CALL from another program
- **Approximate size:** Number of lines, number of paragraphs/sections

#### 2. Entry Points and Parameters
- PROCEDURE DIVISION USING parameters (name and type of each)
- CICS COMMAREA structure if applicable
- ENTRY statements if multiple entry points exist
- Return code mechanism (`MOVE X TO RETURN-CODE` or `WS-RETURN-CODE`)

#### 3. Files Accessed
| File Name | Organization | Access Mode | Open Mode | Key Field | Operations |
|---|---|---|---|---|---|
| TRANSACTION-FILE | SEQUENTIAL | SEQUENTIAL | INPUT | N/A | READ |
| ACCOUNT-VSAM | INDEXED | DYNAMIC | I-O | ACC-CARD-NUM | READ, REWRITE |

#### 4. DB2 Tables Accessed
| Table Name | Operation | Key Columns | Data Columns Used | Cursor? |
|---|---|---|---|---|
| ACCT_MASTER | SELECT | CARD_NUMBER | ACCT_STATUS, CREDIT_LIMIT, BALANCE | No |
| TRANSACTION_HISTORY | SELECT | ACCOUNT_ID, TXN_DATE | TXN_ID, TXN_AMOUNT | Yes |

#### 5. External Program Calls
| Call Type | Target Program | COMMAREA/Parameters | Synchronous? | Purpose |
|---|---|---|---|---|
| EXEC CICS LINK | AUTHSUB1 | WS-AUTH-COMMAREA | Yes | Fraud score lookup |
| CALL | DATECALC | BY REFERENCE WS-DATE-PARMS | Yes | Date conversion utility |

#### 6. Side Effects
- Files written or updated (with conditions: "writes to AUDIT-FILE for every processed transaction")
- DB2 rows inserted/updated/deleted (with conditions: "updates ACCT_MASTER balance after approved transaction")
- CICS queues written (TD or TS queue names and conditions)
- RETURN-CODE set for caller

#### 7. Error Handling
List every error path:
- SQLCODE checks: values handled and action taken
- CICS RESP checks: values handled and action taken
- File status checks: values handled and action taken
- Business error paths: EVALUATE branches that result in decline/error decisions

#### 8. Business Decision Logic
Every EVALUATE block that affects a business outcome should be extracted as a named business rule:
```
RULE-AUTH-001: Card Status Check
CONDITION: WS-CARD-STATUS = 'BL' (Blocked) OR 'LO' (Lost) OR 'ST' (Stolen)
ACTION: Decline transaction with code 0062
```

---

### Modernization Mapping Reference

Use this table as a first-pass mapping guide. Refine based on target architecture decisions.

| COBOL Construct | Modern Equivalent | Notes |
|---|---|---|
| `PROCEDURE DIVISION` paragraph | Java method / Python function | Named paragraphs become named methods |
| `PERFORM paragraph` | Method call | Preserve the name as method name |
| `EVALUATE` | `switch` expression, strategy pattern, rule engine | Complex multi-factor EVALUATE → rule engine |
| `IF/ELSE` chain | `if/else if/else` | Direct mapping |
| Copybook record (`01` level) | Java record / Python dataclass / JSON Schema | Shared copybooks → shared library types |
| `PIC S9(n)V99 COMP-3` | `BigDecimal` with scale(2) | Never map to double/float |
| `PIC 9(n) COMP` or `COMP-4` | `int` or `long` | Check size: 9(9) → int, 9(18) → long |
| `PIC X(n)` | `String` | Note fixed-length: may need trim() |
| `88` condition name | Enum constant / named boolean | Preserve the business names |
| `EXEC SQL SELECT` | JPA Query, JDBC, or R2DBC | |
| `EXEC SQL INSERT/UPDATE/DELETE` | JPA save/update, JDBC | Wrap in transaction |
| DB2 cursor | Stream / paginated query | FETCH loop → iterator/stream |
| `EXEC CICS LINK` | REST call, gRPC call, or event publish | Check if target program is in scope for modernization |
| `EXEC CICS SEND/RECEIVE MAP` | REST API endpoint + frontend | BMS map → HTML/React form or REST contract |
| `EXEC CICS ENQUEUE/DEQUEUE` | Distributed lock (Redis, DB-level) | Identify what resource is being locked and why |
| `EXEC CICS WRITEQ TD` | Kafka message / JMS message | Transient data queue → async message |
| VSAM INDEXED file | JPA entity + DB table, or document store | Key field → primary key or index |
| VSAM SEQUENTIAL file | Spring Batch flat file reader, CSV, or stream | Batch processing → Spring Batch step |
| Batch PERFORM UNTIL EOF loop | Spring Batch job / Kafka stream processor | |
| `WS-RETURN-CODE` pattern | Exception type / `Result<T, Error>` | Map specific codes to exception sub-types |
| Julian date (YYDDD) | `LocalDate` | Eliminate all Julian date arithmetic |
| COMP-1 / COMP-2 float | `BigDecimal` (if financial), `double` (if scientific) | Flag any float used for financial values as a bug |

---

## Key Patterns

- **PERFORM ... UNTIL EOF + READ AT END** — the universal batch driver loop
- **EVALUATE WS-RETURN-CODE** — the universal error dispatch mechanism
- **EVALUATE TRUE WHEN condition** — boolean multi-branch decision
- **EXEC CICS LINK + COMMAREA** — synchronous program-to-program call, equivalent to a REST call
- **EXEC CICS RETURN TRANSID + COMMAREA** — pseudo-conversational stateful session handling
- **01 record + 05 fields + 88 conditions** — typed state variable with named values
- **REDEFINES** — union type, same bytes read two ways
- **COPY copybook** — shared type/contract definition
- **COMP-3 V99 COMPUTE ROUNDED** — the standard financial arithmetic pattern

---

## Anti-Patterns (What Not To Do)

- **Do not map COMP-3 to floating-point types.** COMP-3 packed decimal → `BigDecimal`. This is non-negotiable for financial fields. Floating-point types cannot represent all decimal fractions exactly.
- **Do not ignore REDEFINES.** A program that REDEFINEs a field uses the same storage for two things. Failing to document both shapes will produce incorrect data mappings.
- **Do not guess at copybook contents.** If a COPY member is not available, mark it as `[COPYBOOK NOT AVAILABLE — record structure unknown]` and stop. Do not invent fields.
- **Do not flatten EVALUATE to if/else without extracting it as a business rule.** Each EVALUATE block is a business rule that must be documented in the rule register before modernization.
- **Do not ignore 88-level condition names.** They are named values — the names encode business meaning. Document them as enum candidates.
- **Do not treat all date fields as equal.** Identify the format (YYYYMMDD, YYMMDD, Julian, packed decimal date), the century handling used, and the comparison method.
- **Do not skip SQLCODE handling.** Every EXEC SQL must have SQLCODE checked. Undocumented SQLCODE paths are potential data integrity issues.
- **Do not confuse EXEC CICS LINK with EXEC CICS XCTL.** LINK returns to the caller. XCTL transfers control and does not return. This distinction matters for modernization — LINK becomes a service call, XCTL is more like a redirect.
- **Do not omit side effects.** Every file write, DB update, and queue write is a side effect that the modernized system must replicate exactly. Missing side effects is the most common source of behavior divergence in COBOL modernization.

---

## Examples

### Example: Analyzing a Short COBOL Paragraph

Given:
```cobol
2000-VALIDATE-CARD SECTION.
    MOVE WS-CARD-NUMBER TO DB-CARD-LOOKUP-KEY.

    EXEC SQL
        SELECT CARD_STATUS, EXPIRY_DATE, DAILY_LIMIT
        INTO   :WS-CARD-STATUS, :WS-EXPIRY-DATE, :WS-DAILY-LIMIT
        FROM   CARD_MASTER
        WHERE  CARD_NUMBER = :DB-CARD-LOOKUP-KEY
    END-EXEC.

    EVALUATE SQLCODE
        WHEN 0     CONTINUE
        WHEN +100  PERFORM 9100-CARD-NOT-FOUND
        WHEN OTHER PERFORM 9999-DB-ERROR
    END-EVALUATE.

    IF SQLCODE NOT = 0
        MOVE 8 TO WS-RETURN-CODE
        GO TO 2000-EXIT
    END-IF.

    EVALUATE TRUE
        WHEN ACCT-BLOCKED OR ACCT-LOST OR ACCT-STOLEN
            MOVE 'BLOCKED'   TO WS-DECLINE-REASON
            MOVE 'N'         TO WS-CARD-VALID-FLAG
        WHEN WS-EXPIRY-DATE < WS-TODAY-DATE
            MOVE 'EXPIRED'   TO WS-DECLINE-REASON
            MOVE 'N'         TO WS-CARD-VALID-FLAG
        WHEN OTHER
            MOVE 'Y'         TO WS-CARD-VALID-FLAG
    END-EVALUATE.

2000-EXIT.
    EXIT.
```

**Correct analysis output:**
```
PARAGRAPH: 2000-VALIDATE-CARD

PURPOSE: Validates card eligibility for transaction processing.

DB2 ACCESS:
  Table: CARD_MASTER
  Operation: SELECT (single row)
  Key: CARD_NUMBER = input WS-CARD-NUMBER
  Columns retrieved: CARD_STATUS, EXPIRY_DATE, DAILY_LIMIT
  Error handling:
    SQLCODE 0 → continue
    SQLCODE +100 → card not found → 9100-CARD-NOT-FOUND
    SQLCODE OTHER → DB error → 9999-DB-ERROR
    If SQLCODE != 0 → set WS-RETURN-CODE = 8, exit paragraph

BUSINESS RULES:
  RULE-VAL-001: Card Status Block Check
    Condition: Card status is BLOCKED, LOST, or STOLEN (88-level values)
    Action: Set WS-DECLINE-REASON = 'BLOCKED', mark card invalid

  RULE-VAL-002: Card Expiry Check
    Condition: WS-EXPIRY-DATE < WS-TODAY-DATE
    Action: Set WS-DECLINE-REASON = 'EXPIRED', mark card invalid
    NOTE: Date comparison is numeric — confirm both dates are same format

  RULE-VAL-003: Card Valid
    Condition: None of the above
    Action: Set WS-CARD-VALID-FLAG = 'Y'

OUTPUT:
  WS-CARD-VALID-FLAG: 'Y' (valid) or 'N' (invalid)
  WS-DECLINE-REASON: reason code if invalid
  WS-RETURN-CODE: 8 if DB error (no DB-level card data available)
  WS-DAILY-LIMIT: populated from DB for use by downstream paragraphs

MODERNIZATION MAPPING:
  → Java method: CardValidationService.validateCard(cardNumber)
  → Returns: ValidationResult with isValid flag and optional declineReason
  → DB2 SELECT → JPA: cardMasterRepository.findByCardNumber(cardNumber)
  → 88-level conditions → CardStatus enum (ACTIVE, BLOCKED, LOST, STOLEN, EXPIRED)
  → Date comparison → LocalDate comparison, replace YYMMDD with LocalDate
  → WS-RETURN-CODE = 8 → throw CardValidationException("Database unavailable")
```

---

## Expected AI Behavior When This Pack Is Loaded

When this pack is loaded and the AI is asked to analyze a COBOL program, it should:

1. **Identify and name each division** found (or note if missing)
2. **Parse all DATA DIVISION items** and produce a structured data dictionary noting: level number, field name, PIC clause interpretation, USAGE format, REDEFINES (if any), OCCURS (if any), and 88-level names
3. **Flag every COPY** as an external dependency requiring the copybook to be provided for complete analysis
4. **List all EXEC SQL** blocks with table name, operation, predicates, and SQLCODE handling
5. **List all EXEC CICS** commands with their purpose, target program/resource, and RESP handling
6. **Extract business rules from every EVALUATE block** — not just describe the code but name the rule
7. **Document all side effects** — file writes, DB updates, queue writes
8. **Produce a modernization mapping** for every non-trivial construct
9. **Never guess** at copybook contents, DB schemas, or external program behavior — flag unknowns explicitly
10. **Use the standard COBOL analysis output format** from the "What to Document" section above

---

## Handoff to Next Stage

The COBOL analysis produced using this pack feeds into:
- **Pre-engagement documents:** `TX_DECISIONS.md`, `TX_INTEGRATIONS.md`, `TX_RISK_MAP.md`
- **Architecture stage:** bounded context design, integration map, service identification
- **Business rules register:** every extracted EVALUATE block becomes a row in the rule register
- **Test strategy:** the documented business rules and error paths become test cases for parity testing
- **API design:** the COMMAREA and LINKAGE structures become the basis for modern API contracts

---

*FORGE Legacy COBOL Knowledge Pack | Domain: Mainframe Modernization | Version 1.0*
