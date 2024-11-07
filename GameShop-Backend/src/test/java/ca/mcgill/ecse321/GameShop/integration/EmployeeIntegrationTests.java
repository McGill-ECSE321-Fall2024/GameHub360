package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.GameShop.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.GameShop.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.repository.EmployeeAccountRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class EmployeeIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;

    private static final String VALID_EMAIL = "employee@example.com";
    private static final String VALID_PASSWORD = "Strong@Pass1";
    private static final String INVALID_PASSWORD = "wrongpassword";
    private static final String NON_EXISTENT_EMAIL = "nonexistent@example.com";
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_PHONE = "123-456-7890";
    private static final Boolean ISACTIVE_TRUE = true;
    private static final Boolean ISACTIVE_FALSE = false;

    @BeforeEach
    public void setUp() {
        employeeAccountRepository.deleteAll();
    }

    @AfterEach
    public void cleanUp() {
        employeeAccountRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateEmployeeSuccessfully() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE,
                ISACTIVE_TRUE);

        // Act
        ResponseEntity<EmployeeResponseDto> response = client.postForEntity("/employees/", request,
                EmployeeResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EmployeeResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_EMAIL, responseBody.getEmail());
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_PHONE, responseBody.getPhoneNumber());
        assertEquals(ISACTIVE_TRUE, responseBody.getIsActive());
    }

    @Test
    @Order(2)
    public void testCreateEmployeeWithExistingEmailFails() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE,
                ISACTIVE_TRUE);
        testCreateEmployeeSuccessfully(); // Create an employee with the same email first to ensure conflict

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/employees/", request,
                ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("An employee with same email already exists.", errorResponse.getError());

    }

    @Test
    @Order(3)
    public void testCreateEmployeeWithWeakPasswordFails() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, "weak", VALID_NAME, VALID_PHONE,
                ISACTIVE_TRUE);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/employees/", request,
                ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Password does not meet security requirements.", errorResponse.getError());
    }

    @Test
    @Order(4)
    public void testValidLogin() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD);
        testCreateEmployeeSuccessfully(); // Create an employee first

        // Act
        ResponseEntity<EmployeeResponseDto> response = client.postForEntity("/employees/login", request,
                EmployeeResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EmployeeResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_EMAIL, responseBody.getEmail());
    }

    @Test
    @Order(5)
    public void testLoginWithInvalidPassword() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, INVALID_PASSWORD);
        testCreateEmployeeSuccessfully(); // Create an employee first

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/employees/login", request,
                ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid email or password.", errorResponse.getError());
    }

    @Test
    @Order(6)
    public void testLoginWithNonExistentEmail() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(NON_EXISTENT_EMAIL, VALID_PASSWORD);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/employees/login", request,
                ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid email or password.", errorResponse.getError());
    }

    @Test
    @Order(7)
    public void testUpdateEmployeeDetails() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE,
                ISACTIVE_TRUE);
        ResponseEntity<EmployeeResponseDto> creationResponse = client.postForEntity("/employees/", request,
                EmployeeResponseDto.class);
        EmployeeRequestDto updateRequest = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD, "Ahmed AlRawi",
                "987-654-321", ISACTIVE_TRUE);

        // Act
        ResponseEntity<EmployeeResponseDto> updateResponse = client.exchange(
                "/employees/" + creationResponse.getBody().getStaffId(), HttpMethod.PUT,
                new HttpEntity<>(updateRequest), EmployeeResponseDto.class);

        // Assert
        assertNotNull(updateResponse);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        EmployeeResponseDto responseBody = updateResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_EMAIL, responseBody.getEmail());
        assertEquals("Ahmed AlRawi", responseBody.getName());
        assertEquals("987-654-321", responseBody.getPhoneNumber());
        assertEquals(ISACTIVE_TRUE, responseBody.getIsActive());
    }

    @Test
    @Order(8)
    public void testUpdateEmployeeWithValidPasswordOnly() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE,
                ISACTIVE_TRUE);
        ResponseEntity<EmployeeResponseDto> creationResponse = client.postForEntity("/employees/", request,
                EmployeeResponseDto.class);
        EmployeeRequestDto updateRequest = new EmployeeRequestDto(VALID_EMAIL, "New@Pass2", null, null, null);

        // Act
        ResponseEntity<EmployeeResponseDto> updateResponse = client.exchange(
                "/employees/" + creationResponse.getBody().getStaffId(), HttpMethod.PUT,
                new HttpEntity<>(updateRequest), EmployeeResponseDto.class);

        // Assert
        assertNotNull(updateResponse);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        EmployeeResponseDto responseBody = updateResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_EMAIL, responseBody.getEmail());
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_PHONE, responseBody.getPhoneNumber());
        assertEquals(ISACTIVE_TRUE, responseBody.getIsActive());
    }

    @Test
    @Order(9)
    public void testUpdateEmployeeWithWeakPasswordFails() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE,
                ISACTIVE_TRUE);
        ResponseEntity<EmployeeResponseDto> creationResponse = client.postForEntity("/employees/", request,
                EmployeeResponseDto.class);
        EmployeeRequestDto updateRequest = new EmployeeRequestDto(VALID_EMAIL, "weak", null, null, null);

        // Act
        ResponseEntity<ErrorResponseDto> updateResponse = client.exchange(
                "/employees/" + creationResponse.getBody().getStaffId(), HttpMethod.PUT,
                new HttpEntity<>(updateRequest), ErrorResponseDto.class);

        // Assert
        assertNotNull(updateResponse);
        assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
        ErrorResponseDto errorResponse = updateResponse.getBody();
        assertNotNull(errorResponse);
        assertEquals("Password does not meet security requirements.", errorResponse.getError());
    }

    @Test
    @Order(10)
    public void testUpdateEmployeeWithNonExistentIdFails() {
        // Arrange
        EmployeeRequestDto updateRequest = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE,
                ISACTIVE_TRUE);

        // Act
        ResponseEntity<ErrorResponseDto> updateResponse = client.exchange("/employees/999", HttpMethod.PUT,
                new HttpEntity<>(updateRequest), ErrorResponseDto.class);

        // Assert
        assertNotNull(updateResponse);
        assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode());
        ErrorResponseDto errorResponse = updateResponse.getBody();
        assertNotNull(errorResponse);
        assertEquals("Employee not found.", errorResponse.getError());
    }

    @Test
    @Order(11)
    public void testDeactivateEmployee() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE,
                ISACTIVE_TRUE);
        ResponseEntity<EmployeeResponseDto> creationResponse = client.postForEntity("/employees/", request,
                EmployeeResponseDto.class);

        // Act
        ResponseEntity<EmployeeResponseDto> deactivationResponse = client.exchange(
                "/employees/" + creationResponse.getBody().getStaffId() + "/deactivate", HttpMethod.PUT, null,
                EmployeeResponseDto.class);

        // Assert
        assertNotNull(deactivationResponse);
        assertEquals(HttpStatus.OK, deactivationResponse.getStatusCode());
        EmployeeResponseDto responseBody = deactivationResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_EMAIL, responseBody.getEmail());
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_PHONE, responseBody.getPhoneNumber());
        assertEquals(ISACTIVE_FALSE, responseBody.getIsActive());
    }

    @Test
    @Order(12)
    public void testDeactivateEmployeeWithNonExistentIdFails() {
        // Act
        ResponseEntity<ErrorResponseDto> deactivationResponse = client.exchange("/employees/999/deactivate",
                HttpMethod.PUT, null, ErrorResponseDto.class);

        // Assert
        assertNotNull(deactivationResponse);
        assertEquals(HttpStatus.NOT_FOUND, deactivationResponse.getStatusCode());
        ErrorResponseDto errorResponse = deactivationResponse.getBody();
        assertNotNull(errorResponse);
        assertEquals("Employee not found.", errorResponse.getError());
    }

    @Test
    @Order(13)
    public void testDeactivateAlreadyDeactivatedEmployeeKeepsInactive() {
        // Arrange
        EmployeeRequestDto request = new EmployeeRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE,
                ISACTIVE_TRUE);
        ResponseEntity<EmployeeResponseDto> creationResponse = client.postForEntity("/employees/", request,
                EmployeeResponseDto.class);
        client.exchange("/employees/" + creationResponse.getBody().getStaffId() + "/deactivate", HttpMethod.PUT, null,
                EmployeeResponseDto.class);

        // Act
        ResponseEntity<EmployeeResponseDto> deactivationResponse = client.exchange(
                "/employees/" + creationResponse.getBody().getStaffId() + "/deactivate", HttpMethod.PUT, null,
                EmployeeResponseDto.class);

        // Assert
        assertNotNull(deactivationResponse);
        assertEquals(HttpStatus.OK, deactivationResponse.getStatusCode());
        EmployeeResponseDto responseBody = deactivationResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_EMAIL, responseBody.getEmail());
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_PHONE, responseBody.getPhoneNumber());
        assertEquals(ISACTIVE_FALSE, responseBody.getIsActive());
    }

}
