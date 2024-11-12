package ca.mcgill.ecse321.GameShop.integration;

import ca.mcgill.ecse321.GameShop.dto.*;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;

import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import jakarta.transaction.Transactional;
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
    @Autowired
    private GameRepository gameRepository;

    private static final String VALID_EMAIL = "customer@example.com";
    private static final String VALID_PASSWORD = "Secure@Pass1";
    private static final String INVALID_PASSWORD = "wrongpass";
    private static final String VALID_NAME = "Jane Smith";
    private static final String VALID_PHONE = "111-222-3333";
    private static final String INVALID_PHONE = "123";
    private static final String VALID_POSTAL_CODE = "A1A1A1";
    private static final int VALID_CARD_NUMBER = 12345678;
    private static final int VALID_EXP_MONTH = 12;
    private static final int VALID_EXP_YEAR = 24;

    private Integer validCustomerId;
    private Integer validGameId;
    private Integer validGameId2;

    @BeforeEach
    public void setUp() {
        customerAccountRepository.deleteAll();

        // Create a customer to use in tests
        CustomerRequestDto request = new CustomerRequestDto(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, VALID_PHONE);
        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customers/", request, CustomerResponseDto.class);
        validCustomerId = response.getBody().getCustomerId();

        // Create a Games to use in tests
        Game game = new Game();
        game.setName("Sample Game");
        game.setPrice(59.99);
        Game game2 = new Game();
        game2.setName("Sample Game 2");
        game2.setPrice(59.99);
        // Save the game to the database
        Game savedGame = gameRepository.save(game);
        Game savedGame2 = gameRepository.save(game2);
        // Set the valid game ID for use in tests
        validGameId = savedGame.getGameEntityId(); // Assuming getGameId() returns the ID
        validGameId2 = savedGame2.getGameEntityId(); // Assuming getGameId() returns the ID
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
        // Arrange
        CustomerRequestDto updateRequest = new CustomerRequestDto(VALID_EMAIL, VALID_PASSWORD, "Updated Name", VALID_PHONE);
        String url = "/customers/" + validCustomerId;

        // Act
        ResponseEntity<CustomerResponseDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequest), CustomerResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(VALID_NAME, response.getBody().getName());
        assertEquals("Updated Name", response.getBody().getName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
    }

    @Test
    @Order(5)
    public void testUpdateCustomer_NotFound() {
        // Arrange
        CustomerRequestDto updateRequest = new CustomerRequestDto("notfound@example.com", VALID_PASSWORD, "Another Name", VALID_PHONE);
        String url = "/customers/9999";

        // Act
        ResponseEntity<ErrorResponseDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequest), ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Customer not found.", errorResponse.getError());
    }

    @Test
    @Order(6)
    public void testUpdateCustomer_BadRequest() {
        // Arrange
        CustomerRequestDto updateRequest = new CustomerRequestDto(VALID_EMAIL, INVALID_PASSWORD, "Jane Doe", INVALID_PHONE);
        String url = "/customers/" + validCustomerId;

        // Act
        ResponseEntity<ErrorResponseDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequest), ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Password does not meet security requirements.", errorResponse.getError());
    }

    // -- tests for getAllCustomers() --

    @Test
    @Order(7)
    public void testGetAllCustomers_Success() {
        // Arrange & Act
        ResponseEntity<CustomerAccountListDto> response = client.getForEntity("/customers", CustomerAccountListDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getCustomers());
        assertEquals(1, response.getBody().getCustomers().size());
    }

    // -- tests for getCustomerById() --

    @Test
    @Order(8)
    public void testGetCustomerById_Success() {
        // Arrange & Act
        String url = "/customers/" + validCustomerId;
        ResponseEntity<CustomerResponseDto> response = client.getForEntity(url, CustomerResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_PHONE, response.getBody().getPhoneNumber());
    }

    @Test
    @Order(9)
    public void testGetCustomerById_NotFound() {
        // Arrange & Act
        ResponseEntity<ErrorResponseDto> response = client.getForEntity("/customers/9999", ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Customer not found.", errorResponse.getError());
    }

    // -- tests for login() --

    @Test
    @Order(10)
    public void testLoginCustomer_Success() {
        // Arrange
        CustomerRequestDto loginRequest = new CustomerRequestDto(VALID_EMAIL, VALID_PASSWORD);

        // Act
        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customers/login", loginRequest, CustomerResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
    }

    @Test
    @Order(11)
    public void testLoginCustomer_Unauthorized() {
        // Arrange
        CustomerRequestDto invalidLogin = new CustomerRequestDto(VALID_EMAIL, INVALID_PASSWORD);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/customers/login", invalidLogin, ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid email or password.", errorResponse.getError());
    }

    // -- tests for viewOrderHistory() --

    @Test
    @Order(12)
    public void testViewOrderHistory_Success() {
        // Arrange
        String url = "/customers/" + validCustomerId + "/orders";

        // Act
        ResponseEntity<OrderHistoryDto> response = client.getForEntity(url, OrderHistoryDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getOrders());
        assertEquals(0, response.getBody().getOrders().size());
    }

    @Test
    @Order(13)
    public void testViewOrderHistory_NotFound() {
        // Arrange
        String url = "/customers/9999/orders";

        // Act
        ResponseEntity<ErrorResponseDto> response = client.getForEntity(url, ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Customer not found.", errorResponse.getError());
    }

    // -- tests for getPaymentCardById() --

    @Test
    @Order(14)
    public void testGetPaymentCardById_Success() {
        // Arrange
        PaymentDetailsRequestDto paymentRequest = new PaymentDetailsRequestDto(VALID_NAME, VALID_POSTAL_CODE, VALID_CARD_NUMBER, VALID_EXP_MONTH, VALID_EXP_YEAR, validCustomerId);
        String paymentUrl = "/customers/" + validCustomerId + "/payment";
        ResponseEntity<PaymentDetailsResponseDto> createResponse = client.postForEntity(paymentUrl, paymentRequest, PaymentDetailsResponseDto.class);

        // Act
        String getUrl = "/customers/" + validCustomerId + "/card/" + createResponse.getBody().getPaymentDetailsId();
        ResponseEntity<PaymentDetailsResponseDto> response = client.getForEntity(getUrl, PaymentDetailsResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_NAME, response.getBody().getCardName());
    }

    @Test
    @Order(15)
    public void testGetPaymentCardById_NotFound() {
        // Arrange & Act
        ResponseEntity<ErrorResponseDto> response = client.getForEntity("/customers/" + validCustomerId + "/card/9999", ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Card not found.", errorResponse.getError());
    }

    // -- tests for getAllPaymentCards() --

    @Test
    @Order(16)
    public void testGetAllPaymentCards_Success() {
        // Arrange
        PaymentDetailsRequestDto paymentRequest = new PaymentDetailsRequestDto(VALID_NAME, VALID_POSTAL_CODE, VALID_CARD_NUMBER, VALID_EXP_MONTH, VALID_EXP_YEAR, validCustomerId);
        String paymentUrl = "/customers/" + validCustomerId + "/payment";
        client.postForEntity(paymentUrl, paymentRequest, PaymentDetailsResponseDto.class);

        // Act
        String getAllUrl = "/customers/" + validCustomerId + "/cards";
        ResponseEntity<PaymentCardListDto> response = client.getForEntity(getAllUrl, PaymentCardListDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getPaymentCards());
        assertEquals(1, response.getBody().getPaymentCards().size());
    }

    @Test
    @Order(17)
    public void testGetAllPaymentCards_NotFound() {
        // Arrange & Act
        ResponseEntity<ErrorResponseDto> response = client.getForEntity("/customers/9999/cards", ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Customer not found.", errorResponse.getError());
    }

    // -- tests for updatePaymentCards() --

    @Test
    @Order(18)
    public void testCreateOrUpdatePaymentCard_Success() {
        // Arrange
        PaymentDetailsRequestDto paymentRequest = new PaymentDetailsRequestDto(VALID_NAME, VALID_POSTAL_CODE, VALID_CARD_NUMBER, VALID_EXP_MONTH, VALID_EXP_YEAR, validCustomerId);
        String url = "/customers/" + validCustomerId + "/payment";

        // Act
        ResponseEntity<PaymentDetailsResponseDto> response = client.postForEntity(url, paymentRequest, PaymentDetailsResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_NAME, response.getBody().getCardName());
    }

    @Test
    @Order(19)
    public void testCreateOrUpdatePaymentCard_NotFound() {
        // Arrange
        PaymentDetailsRequestDto paymentRequest = new PaymentDetailsRequestDto(VALID_NAME, VALID_POSTAL_CODE, VALID_CARD_NUMBER, VALID_EXP_MONTH, VALID_EXP_YEAR, validCustomerId);
        String url = "/customers/9999/payment";

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity(url, paymentRequest, ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Customer not found.", errorResponse.getError());
    }

    // -- tests for addToWishlist() --

    @Test
    @Order(20)
    public void testAddToWishlist_Success() {
        // Arrange
        String url = "/customers/" + validCustomerId + "/wishlist/" + validGameId;

        // Act
        ResponseEntity<GameResponseDto> response = client.postForEntity(url, null, GameResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getName(), "Sample Game");
        assertEquals(response.getBody().getPrice(), 59.99);
    }

    @Test
    @Order(21)
    public void testAddToWishlist_NotFound() {
        // Arrange
        String url = "/customers/" + validCustomerId + "/wishlist/" + 9999;

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity(url, null, ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Game not found.", errorResponse.getError());
    }

    // -- tests for removeFromWishlist() --

    @Test
    @Order(22)
    public void testRemoveFromWishlist_Success() {
        // Arrange
        String url = "/customers/" + validCustomerId + "/wishlist/" + validGameId;

        // Act
        client.postForEntity(url, null, GameResponseDto.class); // Add first to then remove
        ResponseEntity<GameResponseDto> response = client.exchange(url, HttpMethod.DELETE, null, GameResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(23)
    public void testRemoveFromWishlist_NotFound() {
        // Arrange & Act
        ResponseEntity<ErrorResponseDto> response = client.exchange("/customers/9999/wishlist/" + validGameId, HttpMethod.DELETE, null, ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Customer not found.", errorResponse.getError());
    }

    // -- tests for viewWishlist() --

    @Test
    @Order(24)
    public void testViewWishlist_Success() {
        // Arrange
        String url = "/customers/" + validCustomerId + "/wishlist/" + validGameId;
        client.postForEntity(url, null, GameResponseDto.class); // Add game to wishlist
        String url2 = "/customers/" + validCustomerId + "/wishlist/" + validGameId2;
        client.postForEntity(url2, null, GameResponseDto.class); // Add another game to wishlist

        // Act
        String viewWishlistUrl = "/customers/" + validCustomerId + "/wishlist";
        ResponseEntity<WishlistDto> response = client.getForEntity(viewWishlistUrl, WishlistDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getGames());
        assertEquals(2, response.getBody().getGames().size());
    }

    @Test
    @Order(25)
    public void testViewWishlist_NotFound() {
        // Arrange
        String url = "/customers/9999/wishlist";

        // Act
        ResponseEntity<ErrorResponseDto> response = client.getForEntity(url, ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertTrue(errorResponse.getError().contains("Customer not found")); // Assuming the error message includes this text
    }

}

