       IDENTIFICATION DIVISION.
       PROGRAM-ID. PAYVAL01.
       PROCEDURE DIVISION.
           IF ACCT-STATUS = 'B'
              MOVE 'DECLINE' TO AUTH-RESULT
           ELSE
              MOVE 'APPROVE' TO AUTH-RESULT
           END-IF.
           GOBACK.
