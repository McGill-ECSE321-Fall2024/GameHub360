package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ManagerIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    private static final String VALID_EMAIL = "manager@example.com";
    private static final String VALID_PASSWORD = "Strong@Pass1";
    private static final String INVALID_PASSWORD = "wrongpassword";
    private static final String NON_EXISTENT_EMAIL = "nonexistent@example.com";
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_PHONE = "123-456-7890";

    @BeforeEach
    public void setUp() {
        managerAccountRepository.deleteAll();
    }

    @AfterEach
    public void cleanUp() {
        managerAccountRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateManagerSuccessfully() {
        // Arrange
        ManagerRequestDto request = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);

        // Act
        ResponseEntity<ManagerResponseDto> response = client.postForEntity("/manager/", request,
                ManagerResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ManagerResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_EMAIL, responseBody.getEmail());
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_PHONE, responseBody.getPhoneNumber());
    }

    @Test
    @Order(2)
    public void testCreateDuplicateManagerFails() {
        // Arrange
        ManagerRequestDto request = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);
        client.postForEntity("/manager/", request, ManagerResponseDto.class);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/manager/", request, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("A manager already exists. Only one manager is allowed.", errorResponse.getError());
    }

    @Test
    @Order(3)
    public void testCreateManagerWithWeakPasswordFails() {
        // Arrange
        ManagerRequestDto request = new ManagerRequestDto(VALID_EMAIL, "weak", VALID_NAME, VALID_PHONE);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/manager/", request, ErrorResponseDto.class);

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
        ManagerRequestDto createRequest = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);
        client.postForEntity("/manager/", createRequest, ManagerResponseDto.class);
        ManagerRequestDto loginRequest = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD);

        // Act
        ResponseEntity<ManagerResponseDto> response = client.postForEntity("/manager/login", loginRequest,
                ManagerResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ManagerResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_EMAIL, responseBody.getEmail());
    }

    @Test
    @Order(5)
    public void testLoginWithInvalidPassword() {
        // Arrange
        ManagerRequestDto createRequest = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);
        client.postForEntity("/manager/", createRequest, ManagerResponseDto.class);
        ManagerRequestDto loginRequest = new ManagerRequestDto(VALID_EMAIL, INVALID_PASSWORD);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/manager/login", loginRequest,
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
        ManagerRequestDto loginRequest = new ManagerRequestDto(NON_EXISTENT_EMAIL, VALID_PASSWORD);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/manager/login", loginRequest,
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
    public void testUpdateManagerDetails() {
        // Arrange
        ManagerRequestDto createRequest = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);
        client.postForEntity("/manager/", createRequest, ManagerResponseDto.class);
        ManagerRequestDto updateRequest = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD, "Jane Doe",
                "987-654-3210");

        // Act
        ResponseEntity<ManagerResponseDto> response = client.exchange("/manager", HttpMethod.PUT,
                new HttpEntity<>(updateRequest), ManagerResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ManagerResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Jane Doe", responseBody.getName());
        assertEquals("987-654-3210", responseBody.getPhoneNumber());
    }

    @Test
    @Order(8)
    public void testUpdateManagerPasswordOnly() {
        // Arrange
        ManagerRequestDto createRequest = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);
        client.postForEntity("/manager/", createRequest, ManagerResponseDto.class);
        ManagerRequestDto updateRequest = new ManagerRequestDto(VALID_EMAIL, "New@Pass123");

        // Act
        ResponseEntity<ManagerResponseDto> response = client.exchange("/manager", HttpMethod.PUT,
                new HttpEntity<>(updateRequest), ManagerResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
    }

    @Test
    @Order(9)
    public void testUpdateManagerWithWeakPasswordFails() {
        // Arrange
        ManagerRequestDto createRequest = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);
        client.postForEntity("/manager/", createRequest, ManagerResponseDto.class);
        ManagerRequestDto updateRequest = new ManagerRequestDto(VALID_EMAIL, "weak");

        // Act
        ResponseEntity<ErrorResponseDto> response = client.exchange("/manager", HttpMethod.PUT,
                new HttpEntity<>(updateRequest), ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Password does not meet security requirements.", errorResponse.getError());
    }

    @Test
    @Order(10)
    public void testUpdateManagerWithNonExistentEmailFails() {
        // Arrange
        ManagerRequestDto updateRequest = new ManagerRequestDto(NON_EXISTENT_EMAIL, VALID_PASSWORD, "Jane Doe",
                "987-654-3210");

        // Act
        ResponseEntity<ErrorResponseDto> response = client.exchange("/manager", HttpMethod.PUT,
                new HttpEntity<>(updateRequest), ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Manager not found.", errorResponse.getError());
    }
}
