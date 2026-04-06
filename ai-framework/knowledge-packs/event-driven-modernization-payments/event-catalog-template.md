# Event Catalog Template

| Event Name | Trigger | Producer | Key | Payload Summary | Consumers | Ordering / Idempotency Notes |
|---|---|---|---|---|---|---|
| AccountStatusChanged | Status update | account-lifecycle-service | accountId | status, prior status, reason, timestamp | servicing, notifications, analytics | idempotent on eventId + version |
| PaymentPosted | Payment booked | payment-posting-service | accountId | payment amount, date, source, reference | balance, statements, analytics | preserve posting sequence |
