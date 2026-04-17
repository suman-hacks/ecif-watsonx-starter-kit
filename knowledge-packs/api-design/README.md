---
knowledge-pack: "API Design"
load-when: "Designing, implementing, or reviewing REST APIs, GraphQL, or gRPC interfaces"
tools: "Claude Code, GitHub Copilot, watsonx.ai, AWS Q Developer"
---

# Knowledge Pack: API Design

## When to Load This Pack

Load when:
- Designing REST API contracts for a new service
- Reviewing an existing API for quality, security, or consistency
- Implementing an API controller layer
- Designing versioning strategy for breaking changes
- Writing API documentation (OpenAPI/Swagger)

## REST API Design Standards

```text
API DESIGN CONTEXT — inject into AI session

URL design:
- Resources are nouns, not verbs: /authorizations not /authorizeCard
- Collections are plural: /cards not /card
- Hierarchy only when genuinely nested: /cards/{cardId}/transactions
- No deep nesting (max 2 levels): /cards/{cardId}/transactions — not /accounts/{id}/cards/{id}/transactions/{id}/details/{id}
- Lowercase with hyphens: /daily-limits not /dailyLimits or /daily_limits

HTTP methods:
- GET: read, idempotent, no body
- POST: create new resource OR action (trigger authorization, process batch)
- PUT: replace entire resource (idempotent)
- PATCH: partial update (idempotent when using conditional requests)
- DELETE: remove resource (idempotent)

Naming actions (not pure CRUD):
  POST /authorizations              — create/submit an authorization
  POST /authorizations/{id}/void    — action on a resource (not /voidAuthorization)
  POST /cards/{id}/block            — action verb in URL when resource identity changes
  GET  /authorizations/{id}         — read single authorization
  GET  /authorizations?cardId=X&date=Y  — filtered collection query

Status codes:
  200 OK               — success (GET, PUT, PATCH, POST when returning body)
  201 Created          — resource created (POST) — include Location header
  204 No Content       — success, no body (DELETE, action with no response body)
  400 Bad Request      — client input validation failure
  401 Unauthorized     — not authenticated
  403 Forbidden        — authenticated but not authorized
  404 Not Found        — resource does not exist
  409 Conflict         — state conflict (e.g., card already blocked)
  422 Unprocessable    — business rule violation (valid format, invalid business state)
  429 Too Many Requests — rate limited
  500 Internal Error   — server error (never expose details to client)
  503 Service Unavailable — downstream dependency failure

Idempotency:
  All mutating operations should support Idempotency-Key header
  Server stores the result for 24h; returns cached result for duplicate key
  Clients retry safely on network failures
```

## Error Response Standard (RFC 7807 Problem Details)

All APIs must use this format for all error responses:

```json
{
  "type": "https://errors.company.com/card-blocked",
  "title": "Card is blocked",
  "status": 422,
  "detail": "Card XXXX-XXXX-XXXX-1234 is in BLOCKED status and cannot be used for transactions.",
  "instance": "/api/v1/authorizations/AUTH-789",
  "extensions": {
    "businessRuleViolated": "BR-001",
    "correlationId": "550e8400-e29b-41d4-a716-446655440000",
    "timestamp": "2025-01-15T10:23:45Z"
  }
}
```

Rules:
- `type` must be a stable URL (even if not resolvable — it's an identifier)
- `detail` must be human-readable and actionable
- Never include stack traces in error responses
- Never include internal infrastructure details (server names, SQL errors)
- `correlationId` always present — enables support log lookup

## API Versioning Strategy

```
Strategy: URI versioning for major/breaking changes
  /api/v1/authorizations  — current version
  /api/v2/authorizations  — new major version with breaking changes

What requires a new major version (v1 → v2):
  - Removing a field from a response
  - Renaming a field
  - Changing a field type
  - Changing a status code meaning
  - Removing an endpoint

What does NOT require a new version (backwards-compatible):
  - Adding a new optional field to a response
  - Adding a new optional query parameter
  - Adding a new endpoint
  - Adding a new value to a non-enum field

Version lifecycle:
  - New version announced at least 6 months before old version deprecated
  - Old version supported for minimum 12 months after new version GA
  - Deprecation header: Deprecation: Sat, 01 Jan 2026 00:00:00 GMT
  - Sunset header: Sunset: Sat, 01 Jul 2026 00:00:00 GMT
```

## Pagination Standard

```
Cursor-based pagination (preferred for large datasets):
  GET /authorizations?cursor=eyJpZCI6IjEyMyJ9&limit=50

Response:
{
  "data": [...],
  "pagination": {
    "cursor": "eyJpZCI6IjQ1NiJ9",
    "hasMore": true,
    "limit": 50
  }
}

Offset-based pagination (acceptable for small, stable datasets):
  GET /authorizations?page=2&pageSize=20

Response includes:
  "pagination": {
    "page": 2,
    "pageSize": 20,
    "totalCount": 1847,
    "totalPages": 93
  }

Rules:
  - Maximum page size: 100 (never unlimited)
  - Default page size: 20
  - Sort order must be stable (include id as tiebreaker)
```

## OpenAPI Documentation Standards

Every API endpoint must have:

```yaml
paths:
  /authorizations:
    post:
      summary: Submit a card authorization request
      description: |
        Evaluates a card transaction against business rules including card status (BR-001),
        expiry (BR-002), and daily limits (BR-003). Returns an authorization decision.
        
        Idempotent when the same Idempotency-Key header is used within 24 hours.
      operationId: createAuthorization
      tags: [Authorizations]
      security:
        - bearerAuth: []
      parameters:
        - name: Idempotency-Key
          in: header
          required: false
          schema:
            type: string
            format: uuid
          description: Client-generated unique key for idempotent retry safety
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/AuthorizationRequest'
            examples:
              typicalRequest:
                summary: Typical authorization request
                value:
                  cardId: "4111111111111111"
                  amount: 150.00
                  currency: "USD"
                  merchantId: "MERCHANT-001"
      responses:
        '200':
          description: Authorization decision (approved or declined — both are 200)
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AuthorizationResponse'
        '400':
          description: Invalid request format
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/ProblemDetail'
        '422':
          description: Business rule violation
          content:
            application/problem+json:
              schema:
                $ref: '#/components/schemas/ProblemDetail'
        '503':
          description: Required downstream service unavailable
```

## AI Session Instructions

When designing APIs under FORGE, the AI assistant must:
1. Use resource-oriented URLs (nouns, plural, no deep nesting)
2. Use RFC 7807 Problem Details for ALL error responses
3. Include idempotency support for all POST/PUT/PATCH operations
4. Include pagination for all collection endpoints (max page size = 100)
5. Version URLs for breaking changes; backwards-compatible changes do not need a new version
6. Include correlationId in every response (success and error)
7. Never expose stack traces, SQL errors, or internal infrastructure in responses
8. Document all endpoints with OpenAPI including examples
9. Reference business rules (BR-NNN) in endpoint documentation
