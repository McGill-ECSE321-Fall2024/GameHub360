package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.time.LocalDate;

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

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.PaymentDetailsRepository;

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
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private OrderGameRepository orderGameRepository;

    private CustomerOrder customerOrder;
    private CustomerAccount customer;
    private PaymentDetails paymentDetails;
    private Game game;
    private OrderGame orderGame;

    @BeforeEach
    public void setUp() {
        // Clean up all related repositories
        orderGameRepository.deleteAll();
        customerOrderRepository.deleteAll();
        paymentDetailsRepository.deleteAll();
        customerAccountRepository.deleteAll();
        gameRepository.deleteAll();

        // Create test customer
        customer = new CustomerAccount("test@email.com", "password123");
        customer = customerAccountRepository.save(customer);

        // Create test payment details with correct fields
        paymentDetails = new PaymentDetails(
                "John Doe", // cardName
                "H3A 0G4", // postalCode
                1234567890, // cardNumber (as int)
                12, // expMonth
                2025, // expYear
                customer // cardOwner
        );
        paymentDetails = paymentDetailsRepository.save(paymentDetails);

        // Create test game
        game = new Game();
        game.setName("Test Game");
        game.setPrice(19.99);
        game.setQuantityInStock(10);
        game = gameRepository.save(game);

        // Create test order
        customerOrder = new CustomerOrder(
                Date.valueOf(LocalDate.now().minusDays(5)),
                customer,
                paymentDetails);
        customerOrder.setOrderStatus(OrderStatus.DELIVERED);
        customerOrder = customerOrderRepository.save(customerOrder);

        // Create order game association
        orderGame = new OrderGame();
        orderGame.setCustomerOrder(customerOrder);
        orderGame.setGame(game);
        orderGame = orderGameRepository.save(orderGame);
    }

    @AfterEach
    public void cleanUp() {
        orderGameRepository.deleteAll();
        customerOrderRepository.deleteAll();
        paymentDetailsRepository.deleteAll();
        customerAccountRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testReturnOrder_Success() {
        // Act
        ResponseEntity<CustomerOrderResponseDto> response = client.postForEntity(
                "/orders/" + customerOrder.getOrderId() + "/return",
                null,
                CustomerOrderResponseDto.class);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");

        CustomerOrderResponseDto responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals(OrderStatus.RETURNED, responseBody.getOrderStatus(), "Order status should be RETURNED");
        assertEquals(customerOrder.getOrderId(), responseBody.getOrderId(), "Order ID should match");
        assertEquals(customer.getCustomerId(), responseBody.getOrderedById(), "Customer ID should match");
        assertEquals(paymentDetails.getPaymentDetailsId(), responseBody.getPaymentInformationId(),
                "Payment ID should match");

        // Verify the database was updated
        CustomerOrder updatedOrder = customerOrderRepository.findById(customerOrder.getOrderId()).orElse(null);
        assertNotNull(updatedOrder, "Order should exist in database");
        assertEquals(OrderStatus.RETURNED, updatedOrder.getOrderStatus(),
                "Order status should be RETURNED in database");
    }

    @Test
    @Order(2)
    public void testReturnOrder_InShipping() {
        // Arrange
        customerOrder = new CustomerOrder();
        customerOrder.setOrderDate(Date.valueOf(LocalDate.now().minusDays(5)));
        customerOrder.setOrderStatus(OrderStatus.SHIPPING);
        customerOrderRepository.save(customerOrder);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity(
                "/orders/" + customerOrder.getOrderId() + "/return", null, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorBody = response.getBody();
        assertNotNull(errorBody);
        assertEquals("Order with ID " + customerOrder.getOrderId() + " is in shipping", errorBody.getError());
    }

    @Test
    @Order(3)
    public void testReturnOrder_AlreadyReturned() {
        // Arrange
        customerOrder = new CustomerOrder();
        customerOrder.setOrderDate(Date.valueOf(LocalDate.now().minusDays(5)));
        customerOrder.setOrderStatus(OrderStatus.RETURNED);
        customerOrderRepository.save(customerOrder);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity(
                "/orders/" + customerOrder.getOrderId() + "/return", null, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Order with ID " + customerOrder.getOrderId() + " has already been returned",
                errorResponse.getError());
    }
}