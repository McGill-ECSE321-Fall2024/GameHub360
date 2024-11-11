package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.GameShop.dto.StoreInformationRequestDto;
import ca.mcgill.ecse321.GameShop.dto.StoreInformationResponseDto;
import ca.mcgill.ecse321.GameShop.dto.SalesMetricsDto;
import ca.mcgill.ecse321.GameShop.repository.StoreInformationRepository;

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
public class StoreInformationIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private StoreInformationRepository storeInformationRepository;

    private static final String STORE_POLICY = "Return policy: No returns after 30 days.";
    private static final String UPDATED_POLICY = "Updated policy: Returns accepted within 15 days.";
    private static final String EMPTY_POLICY = "";

    @BeforeEach
    public void setUp() {
        storeInformationRepository.deleteAll();
    }

    @AfterEach
    public void cleanUp() {
        storeInformationRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateStorePolicySuccessfully() {
        // Arrange
        StoreInformationRequestDto request = new StoreInformationRequestDto(STORE_POLICY);

        // Act
        ResponseEntity<StoreInformationResponseDto> response = client.postForEntity("/store/policy", request,
                StoreInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StoreInformationResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(STORE_POLICY, responseBody.getStorePolicy());
    }

    @Test
    @Order(2)
    public void testCreateDuplicateStorePolicyFails() {
        // Arrange
        StoreInformationRequestDto request = new StoreInformationRequestDto(STORE_POLICY);
        client.postForEntity("/store/policy", request, StoreInformationResponseDto.class);

        // Act
        ResponseEntity<StoreInformationResponseDto> response = client.postForEntity("/store/policy", request,
                StoreInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void testCreateStorePolicyWithEmptyPolicyFails() {
        // Arrange
        StoreInformationRequestDto request = new StoreInformationRequestDto(EMPTY_POLICY);

        // Act
        ResponseEntity<StoreInformationResponseDto> response = client.postForEntity("/store/policy", request,
                StoreInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(4)
    public void testViewStorePolicySuccessfully() {
        // Arrange
        StoreInformationRequestDto request = new StoreInformationRequestDto(STORE_POLICY);
        client.postForEntity("/store/policy", request, StoreInformationResponseDto.class);

        // Act
        ResponseEntity<StoreInformationResponseDto> response = client.getForEntity("/store/policy",
                StoreInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StoreInformationResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(STORE_POLICY, responseBody.getStorePolicy());
    }

    @Test
    @Order(5)
    public void testViewStorePolicyNotFound() {
        // Act
        ResponseEntity<StoreInformationResponseDto> response = client.getForEntity("/store/policy",
                StoreInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(6)
    public void testUpdateStorePolicySuccessfully() {
        // Arrange
        StoreInformationRequestDto createRequest = new StoreInformationRequestDto(STORE_POLICY);
        client.postForEntity("/store/policy", createRequest, StoreInformationResponseDto.class);
        StoreInformationRequestDto updateRequest = new StoreInformationRequestDto(UPDATED_POLICY);

        // Act
        ResponseEntity<StoreInformationResponseDto> response = client.exchange("/store/policy", HttpMethod.PUT,
                new HttpEntity<>(updateRequest), StoreInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StoreInformationResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(UPDATED_POLICY, responseBody.getStorePolicy());
    }

    @Test
    @Order(7)
    public void testUpdateStorePolicyNotFound() {
        // Arrange
        StoreInformationRequestDto updateRequest = new StoreInformationRequestDto(UPDATED_POLICY);

        // Act
        ResponseEntity<StoreInformationResponseDto> response = client.exchange("/store/policy", HttpMethod.PUT,
                new HttpEntity<>(updateRequest), StoreInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void testViewSalesMetrics() {
        // Act
        ResponseEntity<SalesMetricsDto> response = client.getForEntity("/store/sales/metrics", SalesMetricsDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SalesMetricsDto metrics = response.getBody();
        assertNotNull(metrics);
        assertEquals(0, metrics.getTotalOrders()); // Edge case: Ensure it handles zero orders
        assertEquals(0, metrics.getTotalCustomers()); // Edge case: No customers present
        assertEquals(0, metrics.getTotalGamesSold()); // Edge case: No games sold
        assertEquals(0.0, metrics.getTotalSales(), 0.01); // Edge case: No sales
    }
}
