---
knowledge-pack: "Testing Patterns"
load-when: "Writing unit tests, integration tests, or test strategies for any language or framework"
tools: "Claude Code, GitHub Copilot, watsonx.ai"
---

# Knowledge Pack: Testing Patterns

## When to Load This Pack

Load when:
- Generating unit tests (Stage 06 P2)
- Generating integration tests (Stage 06 P3)
- Writing a test strategy (Stage 06 P1)
- Reviewing test quality
- Setting up Testcontainers or WireMock

## Core Testing Principles

```text
TESTING CONTEXT — inject into AI session

Philosophy:
- Tests are executable specifications — they document what the code is supposed to do
- Name tests after business rules, not implementation: "BR-001: Decline blocked card" not "testDecline()"
- AAA pattern: Arrange (set up), Act (invoke), Assert (verify) — one concept per test
- Tests must be independent: no shared mutable state, no ordering dependency
- Tests must be deterministic: same input → same output, every time

Test pyramid (allocation of effort):
  Unit tests: 70% — fast, isolated, run on every commit
  Integration tests: 20% — verify service + real dependencies (DB, queue)
  E2E tests: 10% — critical user journeys only, run before each release

What to mock and what NOT to mock:
  MOCK: external APIs, email/SMS senders, time (use clock abstraction), random UUIDs
  NEVER MOCK: the class under test, core business logic, database (use Testcontainers instead)
  DEBATE: repositories — mock in pure unit tests, use real DB in integration tests
  RULE: if you mock the DB, you must also have integration tests with a real DB

Test naming convention:
  Java/JUnit 5: @DisplayName("BR-001: Should decline when card is BLOCKED")
  Python/pytest: def test_br001_decline_when_card_is_blocked()
  JavaScript/Jest: it('BR-001: should decline when card is BLOCKED', ...)
  
Coverage targets (FORGE standard):
  Domain and application layers: 90% branch coverage
  Adapter layer: 80% line coverage
  Controller layer: integration tests (not unit tests)
  Configuration: excluded
```

## Java / JUnit 5 Patterns

```java
// Complete unit test structure
@ExtendWith(MockitoExtension.class)
@DisplayName("CardStatusValidator")
class CardStatusValidatorTest {

    @InjectMocks
    private CardStatusValidator validator;
    
    @Mock
    private Clock clock;
    
    @BeforeEach
    void setup() {
        when(clock.instant()).thenReturn(Instant.parse("2025-06-15T10:00:00Z"));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }
    
    @Nested
    @DisplayName("BR-001: Card status validation")
    class CardStatusValidation {
        
        @Test
        @DisplayName("Should return ELIGIBLE when card status is ACTIVE")
        void shouldReturnEligible_whenCardIsActive() {
            // Arrange
            Card card = CardTestBuilder.activeCard();
            
            // Act
            ValidationResult result = validator.validate(card);
            
            // Assert
            assertThat(result.isEligible()).isTrue();
            assertThat(result.getDeclineReason()).isEmpty();
        }
        
        @Test
        @DisplayName("BR-001: Should return INELIGIBLE with reason CARD_BLOCKED when status is BLOCKED")
        void shouldReturnIneligible_whenCardIsBlocked() {
            Card card = CardTestBuilder.blockedCard();
            
            ValidationResult result = validator.validate(card);
            
            assertThat(result.isEligible()).isFalse();
            assertThat(result.getDeclineReason()).contains("CARD_BLOCKED");
        }
        
        @ParameterizedTest
        @DisplayName("BR-001/BR-002: Should decline for all ineligible card statuses")
        @EnumSource(value = CardStatus.class, names = {"BLOCKED", "EXPIRED", "SUSPENDED", "STOLEN"})
        void shouldDecline_forAllIneligibleStatuses(CardStatus status) {
            Card card = CardTestBuilder.cardWithStatus(status);
            
            ValidationResult result = validator.validate(card);
            
            assertThat(result.isEligible()).isFalse();
        }
    }
    
    @Nested
    @DisplayName("BR-002: Card expiry validation")
    class CardExpiryValidation {
        
        @Test
        @DisplayName("BR-002: Should return INELIGIBLE when card expiry date is in the past")
        void shouldReturnIneligible_whenCardIsExpired() {
            Card card = CardTestBuilder.cardWithExpiry(LocalDate.parse("2020-01-01"));
            
            ValidationResult result = validator.validate(card);
            
            assertThat(result.isEligible()).isFalse();
            assertThat(result.getDeclineReason()).contains("CARD_EXPIRED");
        }
        
        @Test
        @DisplayName("BR-002: Should return ELIGIBLE when card expires today (last day of validity)")
        void shouldReturnEligible_whenCardExpiresToday() {
            Card card = CardTestBuilder.cardWithExpiry(LocalDate.parse("2025-06-15")); // Same as mocked "today"
            
            ValidationResult result = validator.validate(card);
            
            assertThat(result.isEligible()).isTrue();
        }
    }
}
```

## Python / pytest Patterns

```python
import pytest
from unittest.mock import Mock, patch
from datetime import date

class TestCardStatusValidator:
    """Test suite for CardStatusValidator — BR-001 and BR-002"""

    @pytest.fixture
    def validator(self):
        return CardStatusValidator()

    @pytest.fixture
    def active_card(self):
        return Card(
            card_id="CARD-001",
            status=CardStatus.ACTIVE,
            expiry_date=date(2028, 12, 31)
        )

    def test_br001_returns_eligible_when_card_is_active(self, validator, active_card):
        result = validator.validate(active_card)
        assert result.is_eligible is True
        assert result.decline_reason is None

    def test_br001_returns_ineligible_when_card_is_blocked(self, validator):
        card = Card(card_id="CARD-002", status=CardStatus.BLOCKED, expiry_date=date(2028, 12, 31))
        result = validator.validate(card)
        assert result.is_eligible is False
        assert result.decline_reason == "CARD_BLOCKED"

    @pytest.mark.parametrize("status", [CardStatus.BLOCKED, CardStatus.EXPIRED, CardStatus.SUSPENDED])
    def test_br001_br002_declines_all_ineligible_statuses(self, validator, status):
        card = Card(card_id="CARD-003", status=status, expiry_date=date(2028, 12, 31))
        result = validator.validate(card)
        assert result.is_eligible is False

    @patch("card_validator.date")
    def test_br002_card_expired_yesterday_is_ineligible(self, mock_date, validator):
        mock_date.today.return_value = date(2025, 6, 15)
        card = Card(card_id="CARD-004", status=CardStatus.ACTIVE, expiry_date=date(2025, 6, 14))
        result = validator.validate(card)
        assert result.is_eligible is False
        assert result.decline_reason == "CARD_EXPIRED"
```

## TypeScript / Jest Patterns

```typescript
// Business rule test with proper naming
describe('CardStatusValidator', () => {
  let validator: CardStatusValidator;
  
  beforeEach(() => {
    validator = new CardStatusValidator();
  });
  
  describe('BR-001: Card status validation', () => {
    it('should return eligible when card status is ACTIVE', () => {
      const card = buildCard({ status: 'ACTIVE' });
      const result = validator.validate(card);
      expect(result.isEligible).toBe(true);
      expect(result.declineReason).toBeUndefined();
    });
    
    it('BR-001: should return ineligible with CARD_BLOCKED reason when card is blocked', () => {
      const card = buildCard({ status: 'BLOCKED' });
      const result = validator.validate(card);
      expect(result.isEligible).toBe(false);
      expect(result.declineReason).toBe('CARD_BLOCKED');
    });
    
    it.each([['BLOCKED'], ['EXPIRED'], ['SUSPENDED']])(
      'BR-001/BR-002: should decline card with status %s',
      (status) => {
        const card = buildCard({ status: status as CardStatus });
        const result = validator.validate(card);
        expect(result.isEligible).toBe(false);
      }
    );
  });
});

// Test data builder pattern (keeps tests readable)
function buildCard(overrides: Partial<Card> = {}): Card {
  return {
    cardId: 'CARD-001',
    status: 'ACTIVE',
    expiryDate: '2028-12-31',
    dailyLimit: 5000,
    ...overrides
  };
}
```

## Testcontainers Setup (Java)

```java
// Reusable base class for all integration tests
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@ActiveProfiles("integration-test")
abstract class BaseIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(true);  // Reuse container across test classes (faster)

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }
    
    @Autowired
    protected TestRestTemplate restTemplate;
    
    @Autowired
    protected JdbcTemplate jdbc;
    
    @AfterEach
    void cleanDatabase() {
        // Clean in reverse FK order
        jdbc.execute("DELETE FROM authorization");
        jdbc.execute("DELETE FROM card");
    }
}
```

## Test Data Builder Pattern

```java
// Builder makes tests readable without verbose setup
public final class CardTestBuilder {
    
    private String cardId = UUID.randomUUID().toString();
    private CardStatus status = CardStatus.ACTIVE;
    private LocalDate expiryDate = LocalDate.now().plusYears(2);
    private BigDecimal dailyLimit = new BigDecimal("5000.00");
    private String currency = "USD";
    
    public static CardTestBuilder aCard() { return new CardTestBuilder(); }
    
    public static Card activeCard() { return aCard().build(); }
    public static Card blockedCard() { return aCard().withStatus(CardStatus.BLOCKED).build(); }
    public static Card expiredCard() { return aCard().withExpiry(LocalDate.now().minusDays(1)).build(); }
    
    public CardTestBuilder withStatus(CardStatus status) { this.status = status; return this; }
    public CardTestBuilder withExpiry(LocalDate date) { this.expiryDate = date; return this; }
    public CardTestBuilder withDailyLimit(BigDecimal limit) { this.dailyLimit = limit; return this; }
    
    public Card build() {
        return new Card(cardId, status, expiryDate, dailyLimit, currency);
    }
}
```

## AI Session Instructions

When generating tests, the AI must:
1. Name every test after the business rule it verifies (BR-NNN in the test name)
2. Use AAA structure (Arrange/Act/Assert) with blank line separation
3. One assertion concept per test — split multi-concept tests
4. Use parameterized tests for multiple similar cases
5. Use builders or fixtures for test data — no inline object construction in test bodies
6. Use Testcontainers for real DB tests — not H2 in-memory
7. Never mock the class under test or domain business logic
8. Include boundary value tests (at-the-limit, just-over-limit, just-under-limit)
9. Include null/empty input tests for public APIs
10. Produce a coverage summary listing which BR-NNN tests are present vs missing
