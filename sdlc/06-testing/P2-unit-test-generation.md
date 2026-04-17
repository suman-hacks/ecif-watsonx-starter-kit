# P2 — Unit Test Generation

**Stage:** 06 — Testing  
**Persona:** Developer, QA Engineer  
**Tool notes:** Works with all AI coding tools; specify your language and test framework

---

## The Prompt

```text
You are a senior QA engineer generating comprehensive unit tests.

CONTEXT
Component being tested: [CLASS/MODULE NAME AND PACKAGE]
Language/Test framework: [e.g., Java 21 / JUnit 5 / Mockito 5]
Architecture layer: [Domain / Application / API / Adapter]
Business rules implemented: [PASTE RULE IDs AND DESCRIPTIONS]

SOURCE CODE
[PASTE THE CLASS/FUNCTION TO TEST]

DESIGN SPECIFICATION
[PASTE THE SPEC — what this code is supposed to do]

TASK
Generate a COMPLETE test class covering ALL of the following:

1. **Happy path tests** — one test per public method, primary success scenario
2. **Business rule tests** — one test per business rule; name includes BR-NNN
3. **Validation tests** — each invalid input produces the correct error
4. **Boundary condition tests** — minimum value, maximum value, exactly at limit, just over limit
5. **Error path tests** — each exception type; verify exception message and type
6. **Null/empty input tests** — for each nullable parameter
7. **Idempotency test** — same input twice produces same output (where applicable)

TEST NAMING CONVENTION
Use: `given_[precondition]_when_[action]_then_[expectedOutcome]`
Examples:
- `given_activeAccount_when_authorizePayment_then_returnsApproved`
- `given_insufficientBalance_when_authorizePayment_then_throwsInsufficientFundsException`
- `given_blockedCard_when_authorizePayment_then_declineWithBR007Code` // BR-007: card status check

TEST STRUCTURE (AAA — Arrange, Act, Assert)
```java
@Test
@DisplayName("BR-007: Decline authorization when card status is BLOCKED")
void given_blockedCard_when_authorizePayment_then_declineWithReasonCode() {
    // Arrange
    var request = AuthorizationRequest.builder()
        .cardStatus(CardStatus.BLOCKED)
        .amount(new BigDecimal("50.00"))
        .build();
    
    // Act
    var result = authorizationService.authorize(request);
    
    // Assert
    assertThat(result.decision()).isEqualTo(Decision.DECLINE);
    assertThat(result.reasonCode()).isEqualTo("CARD_BLOCKED");
}
```

TEST DATA RULES
- Use realistic but synthetic data (no real card numbers, SSNs, names)
- Use builder pattern or factory methods — no constructor-parameter-hell in tests
- Create a TestDataFactory class if the same data appears in 3+ tests
- Dates: use fixed clock (Clock.fixed) — never new Date() or LocalDate.now()

MOCKING RULES
- Mock all external dependencies (databases, HTTP clients, message queues)
- DO NOT mock the class under test or its value objects
- Verify interactions only when the call IS the behavior (e.g., verify event published)
- Do NOT verify internal implementation details (which private method was called)

COVERAGE TARGETS
Every test class must achieve:
- Branch coverage: ≥ 90% on business logic classes
- All business rules from the register have at least one test
- All BLOCKER and MAJOR findings from code review are regression-tested

OUTPUT FORMAT
1. Complete test class (all imports, all tests)
2. TestDataFactory class (if needed)  
3. Coverage mapping table: Business Rule → Test Method(s)
4. Missing coverage: anything that can't be unit tested and needs integration test
```

---

## Framework-Specific Notes

**Java / JUnit 5 + Mockito:**
- Use `@ExtendWith(MockitoExtension.class)`
- Use `@Mock` and `@InjectMocks`
- Use AssertJ for fluent assertions (`assertThat`)
- Use `@ParameterizedTest` for boundary conditions

**Python / pytest:**
- Use `pytest.fixture` for setup
- Use `unittest.mock.patch` for dependencies
- Use `pytest.mark.parametrize` for boundary conditions
- Use `pytest.raises` for exception tests

**TypeScript / Jest:**
- Use `jest.mock()` for module mocking
- Use `describe/it` blocks, not flat `test` functions
- Use `expect().toThrow()` for exception tests
- Use `beforeEach` for test isolation
