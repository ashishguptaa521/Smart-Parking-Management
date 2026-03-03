# Testing Documentation - Smart Parking Management System

## Overview
This document provides comprehensive information about the testing implementation for the Smart Parking Management System using **JUnit 5**, **Mockito**, and **Spring Boot Test**.

## Testing Stack

### 1. **JUnit 5 (Jupiter)**
- Modern testing framework for Java
- Provides annotations like `@Test`, `@BeforeEach`, `@DisplayName`
- Support for parameterized tests and dynamic tests

### 2. **Mockito**
- Mocking framework for unit testing
- Used to mock dependencies and isolate units under test
- Provides `@Mock`, `@InjectMocks`, and verification capabilities

### 3. **Spring Boot Test**
- Integration testing support for Spring Boot applications
- Includes `@DataJpaTest` for repository testing
- Includes `@WebMvcTest` for controller testing
- Provides MockMvc for simulating HTTP requests

### 4. **AssertJ**
- Fluent assertion library
- Provides readable and maintainable test assertions

### 5. **H2 Database**
- In-memory database for testing
- No setup required, automatically configured by Spring Boot Test

### 6. **REST Assured**
- Testing framework for REST APIs
- Provides BDD-style API testing

## Test Structure

```
src/test/java/com/smartparking/
├── service/
│   └── ParkingServiceImplTest.java          # Unit tests for service layer
├── repository/
│   ├── ParkingSlotRepositoryTest.java       # Integration tests for slot repository
│   └── ParkingTicketRepositoryTest.java     # Integration tests for ticket repository
└── controller/
    └── ParkingControllerIntegrationTest.java # API integration tests
```

## Test Categories

### 1. Unit Tests (Service Layer)
**File:** `ParkingServiceImplTest.java`

**Purpose:** Test business logic in isolation using mocked dependencies

**Key Features:**
- Uses `@ExtendWith(MockitoExtension.class)` for Mockito support
- Mocks repositories, mappers, and other services
- Tests individual methods with various scenarios
- Verifies method interactions using Mockito's verify

**Test Scenarios:**
- ✅ Successful vehicle entry
- ✅ No available slots handling
- ✅ Successful vehicle exit with fee calculation
- ✅ Ticket not found exception
- ✅ Parking history retrieval
- ✅ Available slots listing
- ✅ License plate search
- ✅ Different slot types handling
- ✅ Fee calculation based on duration

**Example:**
```java
@Test
@DisplayName("Should successfully process vehicle entry when slot is available")
void testVehicleEntry_Success() {
    when(slotService.findAvailableSlot(SlotType.COMPACT)).thenReturn(testSlot);
    when(ticketRepository.save(any(ParkingTicket.class))).thenReturn(testTicket);
    
    EntryResponseDTO response = parkingService.vehicleEntry(entryRequest);
    
    assertThat(response).isNotNull();
    assertThat(response.getLicensePlate()).isEqualTo("KA01AB1234");
    verify(slotService, times(1)).findAvailableSlot(SlotType.COMPACT);
}
```

### 2. Repository Integration Tests
**Files:** 
- `ParkingSlotRepositoryTest.java`
- `ParkingTicketRepositoryTest.java`

**Purpose:** Test database interactions using H2 in-memory database

**Key Features:**
- Uses `@DataJpaTest` for repository layer testing
- Automatically configures H2 database
- Uses `TestEntityManager` for test data setup
- Tests custom repository queries

**ParkingSlotRepository Test Scenarios:**
- ✅ Find slot by slot number
- ✅ Find all unoccupied slots
- ✅ Find unoccupied slots by type
- ✅ Count unoccupied slots
- ✅ Find expired reservations
- ✅ Find reserved but not occupied slots
- ✅ CRUD operations (Create, Read, Update, Delete)

**ParkingTicketRepository Test Scenarios:**
- ✅ Find parking history with pagination
- ✅ Find tickets by license plate
- ✅ Find active parking tickets
- ✅ Find tickets by entry time range
- ✅ Save and update ticket operations
- ✅ Revenue calculation from completed tickets

**Example:**
```java
@Test
@DisplayName("Should find all unoccupied slots")
void testFindByIsOccupiedFalse() {
    List<ParkingSlot> availableSlots = slotRepository.findByIsOccupiedFalse();
    
    assertThat(availableSlots).isNotEmpty();
    assertThat(availableSlots).allMatch(slot -> !slot.isOccupied());
}
```

### 3. API Integration Tests (Controller Layer)
**File:** `ParkingControllerIntegrationTest.java`

**Purpose:** Test REST API endpoints end-to-end

**Key Features:**
- Uses `@WebMvcTest` for controller testing
- Uses `MockMvc` to simulate HTTP requests
- Mocks service layer dependencies
- Tests request/response handling, validation, and HTTP status codes

**Test Scenarios:**
- ✅ POST /api/parking/entry - Successful entry
- ✅ POST /api/parking/entry - Invalid request validation
- ✅ POST /api/parking/exit/{id} - Successful exit
- ✅ GET /api/parking/available - List available slots
- ✅ GET /api/parking/search - Search by license plate
- ✅ GET /api/parking/history/{licensePlate} - Parking history
- ✅ Concurrent vehicle entries
- ✅ Content type validation
- ✅ Missing request body handling

**Example:**
```java
@Test
@DisplayName("POST /api/parking/entry - Should process vehicle entry successfully")
void testVehicleEntry_Success() throws Exception {
    when(parkingService.vehicleEntry(any())).thenReturn(entryResponse);
    
    mockMvc.perform(post("/api/parking/entry")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entryRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.licensePlate").value("KA01AB1234"));
}
```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=ParkingServiceImplTest
```

### Run Tests with Coverage
```bash
mvn test jacoco:report
```

### Run Tests in IDE
- **IntelliJ IDEA:** Right-click on test class/method → Run
- **Eclipse:** Right-click on test class/method → Run As → JUnit Test
- **VS Code:** Click on "Run Test" above test methods

## Test Coverage

The test suite covers:
- ✅ **Service Layer:** Business logic with mocked dependencies
- ✅ **Repository Layer:** Database queries and JPA operations
- ✅ **Controller Layer:** REST API endpoints and request/response handling
- ✅ **Exception Handling:** Error scenarios and edge cases
- ✅ **Validation:** Input validation and constraint checking

## Best Practices Implemented

### 1. **Arrange-Act-Assert (AAA) Pattern**
All tests follow the AAA pattern for clarity:
```java
// Arrange - Setup test data and mocks
when(repository.findById(1L)).thenReturn(Optional.of(entity));

// Act - Execute the method under test
Result result = service.performAction(1L);

// Assert - Verify the outcome
assertThat(result).isNotNull();
```

### 2. **Descriptive Test Names**
Using `@DisplayName` for readable test descriptions:
```java
@Test
@DisplayName("Should throw SlotNotFoundException when no slot is available")
void testParkVehicle_NoSlotAvailable() { ... }
```

### 3. **Test Isolation**
Each test is independent:
- Uses `@BeforeEach` for setup
- No shared mutable state between tests
- Clean database state for each repository test

### 4. **Comprehensive Assertions**
Using AssertJ for fluent and expressive assertions:
```java
assertThat(tickets)
    .isNotEmpty()
    .hasSize(2)
    .allMatch(ticket -> ticket.getLicensePlate().equals("KA01AB1234"));
```

### 5. **Mock Verification**
Verifying interactions with dependencies:
```java
verify(slotService, times(1)).findAvailableSlot(SlotType.COMPACT);
verify(ticketRepository, never()).save(any());
```

## Dependencies in pom.xml

```xml
<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- Spring Boot Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- AssertJ -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <scope>test</scope>
</dependency>

<!-- REST Assured -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>
```

## Test Execution Results

After running `mvn test`, you should see output similar to:

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.smartparking.service.ParkingServiceImplTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Running com.smartparking.repository.ParkingSlotRepositoryTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Running com.smartparking.repository.ParkingTicketRepositoryTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Running com.smartparking.controller.ParkingControllerIntegrationTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 40, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

## Continuous Integration

The test suite is designed to run in CI/CD pipelines:

### GitHub Actions Example
```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Run tests
        run: mvn test
```

## Troubleshooting

### Common Issues

1. **Tests fail due to missing dependencies**
   - Run `mvn clean install` to download all dependencies

2. **H2 database connection issues**
   - Check `application.properties` for test profile configuration
   - Ensure H2 dependency is present

3. **MockMvc tests fail with 404**
   - Verify controller mapping paths
   - Check if `@WebMvcTest` is scanning the correct controller

4. **Mock not returning expected values**
   - Verify `when()` conditions match actual method calls
   - Check if using correct argument matchers

## Next Steps

### Additional Testing Recommendations

1. **Add Performance Tests**
   - Test system behavior under load
   - Use JMeter or Gatling

2. **Add Security Tests**
   - Test authentication and authorization
   - Use Spring Security Test

3. **Add Contract Tests**
   - Test API contracts
   - Use Spring Cloud Contract or Pact

4. **Add End-to-End Tests**
   - Test complete user workflows
   - Use Selenium or Cypress

## Conclusion

The Smart Parking Management System now has comprehensive test coverage using industry-standard tools:
- **JUnit 5** for modern test structure
- **Mockito** for unit testing with mocks
- **Spring Boot Test** for integration testing
- **AssertJ** for fluent assertions

All tests are maintainable, readable, and follow best practices for enterprise Java applications.