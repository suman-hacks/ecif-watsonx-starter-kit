# Framework Overview

## ECIF definition

ECIF is a vendor-neutral framework for structuring AI-assisted software engineering work in enterprise environments.

It separates six concerns that are often mixed together in ad hoc prompt engineering:

- what the assistant must always obey
- what the assistant should know for this task
- what the assistant is allowed to do
- how work is decomposed into roles
- what must be remembered across tasks
- how the capability is packaged for teams

## Seven layers

### 1. Constitution
Always-on rules and modernization intent.

### 2. Knowledge Packs
Task-specific context loaded on demand.

### 3. Runtime Guardrails
Deterministic enforcement and validation.

### 4. Specialist Agents
Bounded roles for analysis, design, generation, and review.

### 5. Team Distribution
Packaging of prompts, packs, and standards for reuse.

### 6. Memory and Traceability
Persistent project intelligence and source-to-target rationale.

### 7. Execution and Feedback
Measurement and improvement loop.

## Design principles

1. Source-grounded, not speculative.
2. Traceability by default.
3. Separation of facts, assumptions, and recommendations.
4. Incremental modernization over one-shot conversion.
5. Deterministic guardrails around probabilistic generation.
6. Domain-specific knowledge beats generic prompting.
7. Packaging matters for scale.
