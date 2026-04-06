# Payments COBOL Modernization Pack

## Purpose
Use this pack when analyzing COBOL modules, copybooks, batch jobs, DB2 access, VSAM access, file feeds, and payment servicing flows.

## Load this pack for
- business rule extraction from COBOL
- batch-to-service decomposition
- DB2/VSAM dependency discovery
- copybook field interpretation
- command and control flow analysis
- side-effect and update sequencing analysis

## Expected outputs
1. entry points and inputs
2. files/tables/copybooks used
3. critical decision branches
4. update sequence and commit expectations
5. error and reject paths
6. source-to-target modernization candidates
7. unresolved ambiguities

## Rules
- do not assume paragraph names reflect full business meaning
- distinguish display fields, packed decimals, signs, and implied decimals
- identify business rules separately from technical control flow
- note external dependencies such as CICS, MQ, JCL, schedulers, sort utilities, and downstream files
