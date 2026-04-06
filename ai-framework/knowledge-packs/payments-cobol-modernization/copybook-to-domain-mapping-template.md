# Copybook to Domain Mapping Template

| Copybook / Field | Type / Format | Business Meaning | Domain Entity | Target Field | Notes |
|---|---|---|---|---|---|
| ACCT-NUM | PIC X(...) | Account identifier | Account | accountId | Confirm masking/classification |
| CURR-BAL | PIC S9(...) COMP-3 | Current balance | AccountBalance | currentBalance | Check implied decimals |
| AUTH-CD | PIC X(...) | Authorization code | AuthorizationDecision | authCode | Validate code list |
