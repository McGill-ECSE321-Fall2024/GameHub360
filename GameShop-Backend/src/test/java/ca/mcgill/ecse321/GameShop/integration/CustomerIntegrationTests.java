package ca.mcgill.ecse321.GameShop.integration;

import ca.mcgill.ecse321.GameShop.dto.*;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    private static final String VALID_EMAIL = "customer@example.com";
    private static final String VALID_PASSWORD = "Secure@Pass1";
    private static final String INVALID_PASSWORD = "wrongpass";
    private static final String NON_EXISTENT_EMAIL = "unknown@example.com";
    private static final String VALID_NAME = "Jane Smith";
    private static final String VALID_PHONE = "111-222-3333";
    private static final String INVALID_PHONE = "123";
    private static final String VALID_POSTAL_CODE = "A1A1A1";
    private static final int VALID_CARD_NUMBER = 12345678;
    private static final int VALID_EXP_MONTH = 12;
    private static final int VALID_EXP_YEAR = 24;

    private Integer validCustomerId;

    @BeforeEach
    public void setUp() {
        customerAccountRepository.deleteAll();

        // Create a customer to use in tests
        CustomerRequestDto request = new CustomerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);
        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customers/", request, CustomerResponseDto.class);
        validCustomerId = response.getBody().getCustomerId();
    }

    @AfterEach
    public void cleanUp() {
        customerAccountRepository.deleteAll();
    }

    // -- tests for createCustomer() --

    @Test
    @Order(1)
    public void testCreateCustomer_Success() {
        // Arrange
        CustomerRequestDto request = new CustomerRequestDto("newemail@example.com", VALID_PASSWORD, VALID_NAME, VALID_PHONE);

        // Act
        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customers/", request, CustomerResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newemail@example.com", response.getBody().getEmail());
        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_PHONE, response.getBody().getPhoneNumber());
    }

    @Test
    @Order(2)
    public void testCreateCustomer_Conflict() {
        // Arrange
        CustomerRequestDto request = new CustomerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/customers/", request, ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("An account with this email already exists.", errorResponse.getError());
    }

    @Test
    @Order(3)
    public void testCreateCustomer_BadRequest() {
        // Arrange
        CustomerRequestDto request = new CustomerRequestDto("unique@example.com", INVALID_PASSWORD, VALID_NAME, INVALID_PHONE);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/customers/", request, ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Password does not meet security requirements.", errorResponse.getError());
    }

    // -- tests for updateCustomer() --

    @Test
    @Order(4)
    public void testUpdateCustomer_Success() {
        CustomerRequestDto updateRequest = new CustomerRequestDto(VALID_EMAIL, VALID_PASSWORD, "Updated Name", VALID_PHONE);
        String url = "/customers/" + validCustomerId;

        ResponseEntity<CustomerResponseDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequest), CustomerResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(VALID_NAME, response.getBody().getName());
        assertEquals("Updated Name", response.getBody().getName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
    }

    @Test
    @Order(5)
    public void testUpdateCustomer_NotFound() {
        CustomerRequestDto updateRequest = new CustomerRequestDto("notfound@example.com", VALID_PASSWORD, "Another Name", VALID_PHONE);
        String url = "/customers/9999";

        ResponseEntity<ErrorResponseDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequest), ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Customer not found.", errorResponse.getError());
    }

    @Test
    @Order(6)
    public void testUpdateCustomer_BadRequest() {
        CustomerRequestDto updateRequest = new CustomerRequestDto(VALID_EMAIL, INVALID_PASSWORD, "Jane Doe", INVALID_PHONE);
        String url = "/customers/" + validCustomerId;

        ResponseEntity<ErrorResponseDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequest), ErrorResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Password does not meet security requirements.", errorResponse.getError());
    }

    // -- tests for getAllCustomers() --

    @Test
    @Order(7)
    public void testGetAllCustomers_Success() {
        ResponseEntity<CustomerResponseDto[]> response = client.getForEntity("/customers", CustomerResponseDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }

    // -- tests for getCustomerById() --

    @Test
    @Order(8)
    public void testGetCustomerById_Success() {
        String url = "/customers/" + validCustomerId;
        ResponseEntity<CustomerResponseDto> response = client.getForEntity(url, CustomerResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_PHONE, response.getBody().getPhoneNumber());
    }

    @Test
    @Order(9)
    public void testGetCustomerById_NotFound() {
        ResponseEntity<ErrorResponseDto> response = client.getForEntity("/customers/9999", ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Customer not found.", errorResponse.getError());
    }

    // -- tests for login() --

    @Test
    @Order(10)
    public void testLoginCustomer_Success() {
        CustomerRequestDto loginRequest = new CustomerRequestDto(VALID_EMAIL, VALID_PASSWORD);
        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customers/login", loginRequest, CustomerResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
    }

    @Test
    @Order(11)
    public void testLoginCustomer_Unauthorized() {
        CustomerRequestDto invalidLogin = new CustomerRequestDto(VALID_EMAIL, INVALID_PASSWORD);
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/customers/login", invalidLogin, ErrorResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid email or password.", errorResponse.getError());
    }

    // -- tests for getOrderHistoryByCustomerId() --

    @Test
    @Order(12)
    public void testViewOrderHistory_Success() {
        String url = "/customers/" + validCustomerId + "/orders";
        ResponseEntity<OrderResponseDto[]> response = client.getForEntity(url, OrderResponseDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().length);
        assertNotNull(response.getBody());
    }

    @Test
    @Order(13)
    public void testViewOrderHistory_NotFound() {
        ResponseEntity<ErrorResponseDto> response = client.getForEntity("/customers/9999/orders", ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Customer not found.", errorResponse.getError());
    }

    // -- tests for getPaymentCardById() --

//    @Test
//    @Order(14)
//    public void testGetPaymentCardById_Success() {
//        // First, create a payment card for the customer
//        PaymentDetailsRequestDto paymentRequest = new PaymentDetailsRequestDto(VALID_NAME, VALID_POSTAL_CODE, VALID_CARD_NUMBER, VALID_EXP_MONTH, VALID_EXP_YEAR, validCustomerId);
//        String paymentUrl = "/customers/" + validCustomerId + "/payment";
//        ResponseEntity<PaymentDetailsResponseDto> createResponse = client.postForEntity(paymentUrl, paymentRequest, PaymentDetailsResponseDto.class);
//
////        String getUrl = "/customers/" + validCustomerId + "/card/" + createResponse.getBody().getPaymentDetailsId();
////        ResponseEntity<PaymentDetailsResponseDto> response = client.getForEntity(getUrl, PaymentDetailsResponseDto.class);
////
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals(VALID_NAME, response.getBody().getCardName());
//    }

    @Test
    @Order(15)
    public void testGetPaymentCardById_NotFound() {
        ResponseEntity<ErrorResponseDto> response = client.getForEntity("/customers/" + validCustomerId + "/card/9999", ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Card not found.", errorResponse.getError());
    }

    //
}

