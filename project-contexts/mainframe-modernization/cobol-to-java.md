# FORGE Project Context: COBOL to Java/Spring Modernization

Load alongside `constitution/01-core-principles.md` and `knowledge-packs/legacy-cobol/README.md`.

---

## AI Behavior for COBOL-to-Java Projects

### Phase 1: Analysis Only (Stages 01-02)

**The AI in analysis mode must:**
- Read COBOL as a behavior specification, not as code to translate line-by-line
- Extract business rules in business language (not COBOL constructs)
- Identify all COPY statements (shared copybooks = shared contracts in Java)
- Map each PERFORM to its target paragraph and trace the full call tree
- Identify all EXEC SQL — these become repository/JDBC calls
- Identify all EXEC CICS — these become REST calls, events, or adapter interfaces
- Identify all file I/O (VSAM/QSAM) — these become persistence layer operations
- Flag all ambiguities BEFORE proceeding to design

**The AI in analysis mode must NOT:**
- Generate Java code
- Propose class names or package structures
- Make design decisions about sync vs async
- Fill in ambiguities with assumptions (flag them)

### Phase 2: Design (Stage 03)

**COBOL construct → Java/Spring mapping:**

| COBOL Construct | Modern Equivalent |
|---|---|
| COBOL program (CICS online) | Spring Boot @RestController + @Service |
| COBOL program (batch) | Spring Batch Job + Step |
| COBOL program (called sub-program) | Spring @Service or domain object |
| COPY copybook (data record) | Java record or @Entity |
| COPY copybook (shared across programs) | Shared contract module (separate artifact) |
| WORKING-STORAGE section | Instance variables / method locals |
| LINKAGE SECTION / COMMAREA | Request/Response DTO (Java record) |
| EXEC SQL SELECT | JPA @Query or JdbcTemplate |
| EXEC SQL INSERT/UPDATE | JPA save() or JdbcTemplate update() |
| EXEC SQL cursor | JPA streaming query or pagination |
| EXEC CICS LINK PROGRAM() | Synchronous REST call via RestClient |
| EXEC CICS WRITEQ TD | Kafka produce / JMS send |
| EXEC CICS READQ TD | Kafka consume / JMS receive |
| VSAM KSDS file | JPA @Entity with indexed primary key |
| VSAM ESDS file | Append-only log table |
| EVALUATE WHEN | Switch expression / Strategy pattern |
| PIC S9(13)V9(2) COMP-3 | BigDecimal (scale 2) |
| PIC 9(8) date | LocalDate (parse from YYYYMMDD) |
| 88-level condition names | Java enum or named constants |
| PERFORM UNTIL EOF | Java while loop or Stream |
| CALL "PROGRAM" USING | Interface + implementation (anti-corruption layer during transition) |

### COBOL Analysis Checklist (run on every program)

Before declaring analysis complete:
- [ ] Program type identified (CICS online / batch / sub-program / utility)
- [ ] All COPY statements identified and copybooks retrieved (or flagged as missing)
- [ ] Full PERFORM call tree traced
- [ ] All EXEC SQL statements extracted with table names, predicates, and intent
- [ ] All EXEC CICS commands mapped to modern equivalent
- [ ] All file I/O operations mapped (file name, organization, operations)
- [ ] All external CALL statements identified
- [ ] All decision points extracted as business rules (BR-NNN format)
- [ ] All error/response codes documented
- [ ] All side effects documented (what does this program write/update?)
- [ ] Confidence level per section (High/Medium/Low with basis)
- [ ] Open questions listed (what requires SME clarification)

### Common COBOL Modernization Anti-Patterns

The AI must flag these if it detects them being proposed:

1. **Line-by-line translation**: COBOL procedural structure → procedural Java with COBOL variable names. This produces untestable, unmaintainable code. Require behavior-preserving OO design.

2. **Copybook field dump**: COBOL copybook → Java class with all the same field names. Creates anemic data bags. Require domain-modeled classes with business meaning.

3. **Flat file persistence**: Replacing VSAM with a flat CSV file or in-memory list. Require proper relational or document persistence.

4. **COMMAREA as method parameters**: Passing 50+ parameters to a method like a COMMAREA. Require proper request/response objects.

5. **Keeping 88-level logic**: Using boolean flags instead of proper Java enums or state machines.

6. **Ignoring dead code**: Modernizing code paths that ADDI/analysis shows are never executed. Require dead code identification before analysis.

### Transition Pattern: Anti-Corruption Layer

During the transition, the new Java service calls the old COBOL program while it's being built:

```java
// During transition: LegacyAuthorizationGateway
// After cutover: replaced by native Java implementation
public interface AuthorizationGateway {
    AuthorizationResult authorize(AuthorizationRequest request);
}

@Component
@ConditionalOnProperty("feature.legacy-gateway.enabled")
public class LegacyCobolAuthorizationGateway implements AuthorizationGateway {
    // Calls COBOL via CICS or MQ during parallel-run period
}

@Component  
@ConditionalOnProperty(value = "feature.legacy-gateway.enabled", havingValue = "false")
public class NativeAuthorizationGateway implements AuthorizationGateway {
    // Pure Java implementation — switches on after validation
}
```

### WCA4Z Integration Pattern

When using watsonx Code Assist for Z for initial COBOL analysis:
1. Run WCA4Z analysis in IBM Developer for z/OS (IDz)
2. Export analysis as structured Markdown to `analysis/legacy-behavior-summary.md`
3. This file becomes the Stage 01 output in FORGE
4. Claude Code picks up from Stage 02 onwards using this file as input

Configure CLAUDE.md: `"If analysis/legacy-behavior-summary.md exists, treat it as Stage 01 complete and begin Stage 02."`
