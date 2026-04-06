# Prompt Boundary Policy

## Disallowed prompt content
- secrets and credentials
- production customer data
- regulated identifiers unless approved and masked
- sensitive payloads not required for the task

## Required behavior
- use synthetic or masked examples when possible
- summarize large sensitive artifacts instead of pasting raw content
- strip irrelevant data before submission
