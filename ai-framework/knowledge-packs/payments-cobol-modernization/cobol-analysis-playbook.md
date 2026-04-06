# COBOL Analysis Playbook

## Analysis sequence
1. Identify program type:
   - online/CICS
   - batch
   - utility
   - file transform
   - service facade
2. Identify interfaces:
   - COMMAREA, channels/containers, file I/O, DB2 SQL, MQ, copybooks, sort steps
3. Extract business entities:
   - account
   - card
   - customer
   - authorization
   - statement
   - payment
   - dispute
   - limit
4. Trace decision logic:
   - status checks
   - product checks
   - reason codes
   - amount thresholds
   - date windows
   - reversal/adjustment handling
5. Capture write-side effects:
   - table updates
   - VSAM rewrites
   - event/message emission
   - audit/reject files
6. Separate:
   - business rules
   - data mapping rules
   - operational controls

## Prompt add-on
Ask the model to return:
- business rule table
- field mapping table
- side-effect sequence
- exceptions/reject paths
- target modernization suggestions
