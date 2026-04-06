# ICS Domain Decomposition Guide

## Candidate domains
- Customer Profile
- Account Lifecycle
- Card Lifecycle
- Authorizations Support
- Payments and Adjustments
- Statements and Billing
- Limits and Spend Controls
- Fees and Interest
- Disputes and Servicing Cases
- Notifications and Preferences

## Decomposition rules
- define bounded contexts around ownership of business rules and data
- avoid service boundaries that mirror copybooks or screens one-for-one
- keep high-frequency and SLA-sensitive functions intentionally simple
- isolate mainframe interaction behind adapters or façade services
- use anti-corruption mappings when source model is screen/file oriented but target model is domain oriented

## Output required from assistant
- domain candidates
- service boundaries
- data ownership proposal
- sync vs async interactions
- risks if decomposition is done incorrectly
