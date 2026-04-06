# Servicing Patterns

## Patterns to preserve
- idempotent maintenance operations where duplicates are possible
- complete audit trails for servicing changes
- explicit reason/status code handling
- operator or channel context on updates
- before/after state capture for critical changes

## Common anti-patterns to remove
- screen-driven field bundles leaking into APIs
- shared generic tables with overloaded meanings
- hidden update side effects in utility programs
- silent rejects without structured error contracts
