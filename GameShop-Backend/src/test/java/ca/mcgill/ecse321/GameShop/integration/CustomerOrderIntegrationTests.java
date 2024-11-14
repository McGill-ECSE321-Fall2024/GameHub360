package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderRequestDto;
import ca.mcgill.ecse321.GameShop.dto.CustomerOrderResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.model.*;
import ca.mcgill.ecse321.GameShop.repository.*;

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

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerOrderIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    @Autowired
    private OrderGameRepository orderGameRepository;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;
    @Autowired
    private GameRepository gameRepository;

    private static final String VALID_EMAIL = "customer@example.com";
    private static final String VALID_PASSWORD = "Secure@Pass1";

    private Integer customerAccountId;
    private Integer paymentDetailsId;
    private List<Integer> gameIds;
    private Integer customerOrderId;

    @BeforeEach
    public void setUp() {
        // Clear existing data
        customerAccountRepository.deleteAll();
        orderGameRepository.deleteAll();
        paymentDetailsRepository.deleteAll();
        customerOrderRepository.deleteAll();
        gameRepository.deleteAll(); // Clear previous game data

        // Create and save Game entities
        Game game1 = new Game();
        game1.setName("Game 1");
        game1.setIsAvailable(true);
        game1.setQuantityInStock(10);
        game1 = gameRepository.save(game1);  // Explicitly save Game

        Game game2 = new Game();
        game2.setName("Game 2");
        game2.setIsAvailable(true);
        game2.setQuantityInStock(10);
        game2 = gameRepository.save(game2);  // Explicitly save Game

        // Create and save CustomerAccount
        CustomerAccount customerAccount = new CustomerAccount("test@example.com", "pwd2025$");
        customerAccount = customerAccountRepository.save(customerAccount);
        customerAccountId = customerAccount.getCustomerId();

        // Create and save PaymentDetails
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardOwner(customerAccount);
        paymentDetails = paymentDetailsRepository.save(paymentDetails);
        paymentDetailsId = paymentDetails.getPaymentDetailsId();

        // Create and save CustomerOrder
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customerAccount, paymentDetails);
        customerOrder.setOrderStatus(CustomerOrder.OrderStatus.DELIVERED);
        customerOrder = customerOrderRepository.save(customerOrder);
        customerOrderId = customerOrder.getOrderId();

        // Create and link OrderGame instances to CustomerOrder and Game
        OrderGame orderGame1 = new OrderGame();
        orderGame1.setCustomerOrder(customerOrder);
        orderGame1.setGame(game1);  // Associate already persisted Game
        orderGame1 = orderGameRepository.save(orderGame1);

        OrderGame orderGame2 = new OrderGame();
        orderGame2.setCustomerOrder(customerOrder);
        orderGame2.setGame(game2);  // Associate already persisted Game
        orderGame2 = orderGameRepository.save(orderGame2);

        gameIds = List.of(game1.getGameEntityId(), game2.getGameEntityId());
    }

    @AfterEach
    public void cleanUp() {
        // Clear existing data
        customerAccountRepository.deleteAll();
        orderGameRepository.deleteAll();
        paymentDetailsRepository.deleteAll();
        customerOrderRepository.deleteAll();
        gameRepository.deleteAll(); // Clear previous game data
    }

    // -- Tests createCustomerOrder() --

    @Test
    @Order(1)
    public void testCreateCustomerOrderSuccessfully() {
        CustomerOrderRequestDto request = new CustomerOrderRequestDto(gameIds, paymentDetailsId, customerAccountId);

        // Act
        ResponseEntity<CustomerOrderResponseDto> response = client.postForEntity("/orders", request, CustomerOrderResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomerOrderResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(customerAccountId, responseBody.getOrderedById());
        assertEquals(paymentDetailsId, responseBody.getPaymentInformationId());
        assertEquals(gameIds, responseBody.getOrderedGamesIds());
    }

    @Test
    @Order(2)
    public void testCreateCustomerOrderWithInvalidGameIds() {
        List<Integer> invalidGameIds = List.of(-1, -2); // Invalid game IDs
        CustomerOrderRequestDto request = new CustomerOrderRequestDto(invalidGameIds, paymentDetailsId, customerAccountId);

        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/orders", request, ErrorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Game with ID -1 not found", responseBody.getError()); // Customize message based on actual implementation
    }

    @Test
    @Order(3)
    public void testCreateCustomerOrderWithInvalidCustomerId() {
        Integer invalidCustomerId = 9999; // Invalid customer ID
        CustomerOrderRequestDto request = new CustomerOrderRequestDto(gameIds, paymentDetailsId, invalidCustomerId);

        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/orders", request, ErrorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Customer with ID 9999 not found", responseBody.getError());
    }

    @Test
    @Order(4)
    public void testCreateCustomerOrderWithInvalidPaymentDetailsId() {
        Integer invalidPaymentDetailsId = 9999; // Invalid payment details ID
        CustomerOrderRequestDto request = new CustomerOrderRequestDto(gameIds, invalidPaymentDetailsId, customerAccountId);

        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/orders", request, ErrorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Payment information with ID 9999 not found", responseBody.getError());
    }

    // -- Tests getCustomerOrderById() --

    @Test
    @Order(5)
    public void testGetCustomerOrderById() {
        testCreateCustomerOrderSuccessfully();

        ResponseEntity<CustomerOrderResponseDto> response = client.getForEntity("/orders/" + customerOrderId, CustomerOrderResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomerOrderResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(customerOrderId, responseBody.getOrderId());
        assertEquals(customerAccountId, responseBody.getOrderedById());
        assertEquals(paymentDetailsId, responseBody.getPaymentInformationId());
        assertEquals(new HashSet<>(gameIds), new HashSet<>(responseBody.getOrderedGamesIds()));
    }

    @Test
    @Order(6)
    public void testGetNonExistentCustomerOrderById() {
        int invalidOrderId = 9999;
        ResponseEntity<ErrorResponseDto> response = client.getForEntity("/orders/" + invalidOrderId, ErrorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Order with ID " + invalidOrderId + " not found", responseBody.getError());
    }

    // -- Tests

    @Test
    @Order(7)
    public void testReturnCustomerOrderWithinReturnPeriod() {

        ResponseEntity<CustomerOrderResponseDto> response = client.postForEntity("/orders/" + customerOrderId + "/return", null, CustomerOrderResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomerOrderResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(customerOrderId, responseBody.getOrderId());
        assertEquals(CustomerOrder.OrderStatus.RETURNED, responseBody.getOrderStatus());
    }
}

