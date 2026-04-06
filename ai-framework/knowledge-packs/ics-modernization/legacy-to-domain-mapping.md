# Legacy to Domain Mapping Template

| Legacy Capability | Legacy Module / Screen / Program | Proposed Domain | Target Service | Sync/Async | Notes |
|---|---|---|---|---|---|
| Account status inquiry | TBD | Account Lifecycle | account-status-service | Sync | Preserve response semantics |
| Card block/unblock | TBD | Card Lifecycle | card-controls-service | Sync + event | Emit status-change event |
| Payment posting update | TBD | Payments and Adjustments | payment-posting-service | Async preferred | Include replay handling |
