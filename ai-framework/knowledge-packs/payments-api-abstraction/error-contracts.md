# Error Contract Guidance

Return errors with:
- business code
- human-readable message
- correlation id
- retryability indicator
- downstream dependency indicator when appropriate

Avoid:
- raw mainframe codes without mapping
- leaking internal file/table names
- ambiguous 500-only behavior
