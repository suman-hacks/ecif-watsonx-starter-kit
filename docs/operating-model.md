# Operating Model

## Recommended role sequence

### Role 1: Legacy Analyzer
Reads source artifacts, summarizes behavior, identifies entry points, dependencies, and side effects.

### Role 2: Business Rule Extractor
Converts procedural or distributed logic into explicit business rules.

### Role 3: Target Architect
Maps source behavior to services, APIs, data ownership, transactions, and events.

### Role 4: Code Generator
Implements target artifacts according to constitution and target patterns.

### Role 5: Reviewer / Hardener
Checks for security, observability, test coverage, and architectural drift.

## Operating rule
Do not skip directly from legacy source to production-ready code without intermediate artifacts.
