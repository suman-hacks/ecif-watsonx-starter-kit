# Security Rules

- Never place secrets or live credentials in prompts or generated code.
- Mask regulated or sensitive data before prompt submission.
- Avoid hardcoded tokens, passwords, keys, account identifiers, or PAN-like values.
- Generated code must use approved logging practices and avoid sensitive payload logging.
- All external integrations must be explicit and reviewable.
- Generated code must follow least-privilege assumptions.
