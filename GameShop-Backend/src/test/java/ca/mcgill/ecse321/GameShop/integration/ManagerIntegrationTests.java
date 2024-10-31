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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ManagerIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    private static final String VALID_EMAIL = "manager@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String INVALID_PASSWORD = "wrongpassword";
    private static final String NON_EXISTENT_EMAIL = "nonexistent@example.com";

    @BeforeEach
    public void setUp() {
        managerAccountRepository.deleteAll();

        // Create a test ManagerAccount in the database
        ManagerAccount manager = new ManagerAccount(VALID_EMAIL, VALID_PASSWORD);
        managerAccountRepository.save(manager);
    }

    @AfterEach
    public void cleanUp() {
        managerAccountRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testValidLogin() {
        // Arrange
        ManagerRequestDto request = new ManagerRequestDto(VALID_EMAIL, VALID_PASSWORD);

        // Act
        ResponseEntity<ManagerResponseDto> response = client.postForEntity("/manager/login", request, ManagerResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ManagerResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_EMAIL, responseBody.getEmail());
    }

    @Test
    @Order(2)
    public void testLoginWithInvalidPassword() {
        // Arrange
        ManagerRequestDto request = new ManagerRequestDto(VALID_EMAIL, INVALID_PASSWORD);

        // Act
        ResponseEntity<String> response = client.postForEntity("/manager/login", request, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid email or password.", responseBody);
    }

    @Test
    @Order(3)
    public void testLoginWithNonExistentEmail() {
        // Arrange
        ManagerRequestDto request = new ManagerRequestDto(NON_EXISTENT_EMAIL, VALID_PASSWORD);

        // Act
        ResponseEntity<String> response = client.postForEntity("/manager/login", request, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid email or password.", responseBody);
    }

    @Test
    @Order(4)
    public void testLoginWithEmptyEmail() {
        // Arrange
        ManagerRequestDto request = new ManagerRequestDto("", VALID_PASSWORD);

        // Act
        ResponseEntity<String> response = client.postForEntity("/manager/login", request, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Email cannot be empty.", responseBody);
    }

    @Test
    @Order(5)
    public void testLoginWithEmptyPassword() {
        // Arrange
        ManagerRequestDto request = new ManagerRequestDto(VALID_EMAIL, "");

        // Act
        ResponseEntity<String> response = client.postForEntity("/manager/login", request, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Password cannot be empty.", responseBody);
    }
}
