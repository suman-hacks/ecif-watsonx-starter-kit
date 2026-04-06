# Business Rules

| Rule ID | Description | Source |
|---|---|---|
| BR-001 | Authorization must validate card/account state before spend control logic. | ICSAUTHZ.cbl VALIDATE-CARD |
| BR-002 | Spend control validation executes before calling the core authorization path. | ICSAUTHZ.cbl APPLY-SPEND-CONTROL |
| BR-003 | Successful authorization sets audit publication flag. | ICSAUTHZ.cbl WS-AUDIT-FLAG |
