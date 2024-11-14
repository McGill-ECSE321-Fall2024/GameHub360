package ca.mcgill.ecse321.GameShop.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.mcgill.ecse321.GameShop.dto.PaymentDetailsRequestDto;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.PaymentDetailsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.GameShop.dto.CustomerRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.utils.EncryptionUtils;

@SpringBootTest
public class CustomerServiceTests {

    @Mock
    private CustomerAccountRepository customerAccountRepository;
    @Mock
    private CustomerOrderRepository customerOrderRepository;
    @Mock
    private PaymentDetailsRepository paymentDetailsRepository;
    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private CustomerService customerService;

    // Tests for createCustomer service method

    @Test
    public void testCreateCustomerSuccessfully() {
        // Arrange
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "ValidP@ss123", "Alice", "123-456-7890");
        CustomerAccount customer = new CustomerAccount("customer@example.com", "ValidP@ss123");
        customer.setName("Alice");
        customer.setPhoneNumber("123-456-7890");

        // Mock repository behavior
        when(customerAccountRepository.findCustomerAccountByEmail(any(String.class))).thenReturn(null);
        when(customerAccountRepository.save(any(CustomerAccount.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CustomerAccount response = customerService.createCustomer(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(customer.getEmail(), response.getEmail());
        assertEquals(customer.getName(), response.getName());
        assertEquals(customer.getPhoneNumber(), response.getPhoneNumber());
        verify(customerAccountRepository, times(1)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerEmailAlreadyExists() {
        // Arrange
        CustomerRequestDto requestDto = new CustomerRequestDto("existing@example.com", "password123");
        
        when(customerAccountRepository.findCustomerAccountByEmail(requestDto.getEmail()))
                .thenReturn(new CustomerAccount());

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> customerService.createCustomer(requestDto));

        // Assert
        assertEquals("An account with this email already exists.", e.getMessage());
        assertEquals(HttpStatus.CONFLICT, e.getStatus());
        verify(customerAccountRepository, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerInvalidPassword() {
        // Arrange
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "weakpass");
        when(customerAccountRepository.findCustomerAccountByEmail(any(String.class))).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> customerService.createCustomer(requestDto));

        // Assert
        assertEquals("Password does not meet security requirements.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        verify(customerAccountRepository, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testCreateCustomerInvalidPhoneNumber() {
        // Arrange
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "ValidP@ss123", "Alice", "invalidPhone");
        when(customerAccountRepository.findCustomerAccountByEmail(any(String.class))).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> customerService.createCustomer(requestDto));

        // Assert
        assertEquals("Phone Number does not meet formatting criteria.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        verify(customerAccountRepository, times(0)).save(any(CustomerAccount.class));
    }

    // Tests for updateCustomer service method

    @Test
    public void testUpdateCustomerSuccessfully() {
        // Arrange
        Integer customerId = 1;
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "NewStr0ngP@ssw0rd", "Alice Updated", "123-456-7890");
        CustomerAccount existingCustomer = new CustomerAccount("customer@example.com", "encryptedOldPassword");
        existingCustomer.setName("Alice");
        existingCustomer.setPhoneNumber("987-654-3210");

        // Mock repository behavior
        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(existingCustomer);
        when(customerAccountRepository.save(any(CustomerAccount.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CustomerAccount updatedCustomer = customerService.updateCustomer(customerId, requestDto);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(requestDto.getEmail(), updatedCustomer.getEmail());
        assertEquals(requestDto.getName(), updatedCustomer.getName());
        assertEquals(requestDto.getPhoneNumber(), updatedCustomer.getPhoneNumber());
        verify(customerAccountRepository, times(1)).save(any(CustomerAccount.class));
    }

    @Test
    public void testUpdateCustomerNotFound() {
        // Arrange
        Integer customerId = 1;
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "password123");

        // Mock repository behavior to simulate non-existent customer
        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> customerService.updateCustomer(customerId, requestDto));

        // Assert
        assertEquals("Customer not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        verify(customerAccountRepository, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testUpdateCustomerInvalidPassword() {
        // Arrange
        Integer customerId = 1;
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "weakpass");
        CustomerAccount existingCustomer = new CustomerAccount("customer@example.com", "encryptedOldPassword");

        // Mock repository behavior
        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(existingCustomer);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> customerService.updateCustomer(customerId, requestDto));

        // Assert
        assertEquals("Password does not meet security requirements.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        verify(customerAccountRepository, times(0)).save(any(CustomerAccount.class));
    }

    @Test
    public void testUpdateCustomerInvalidPhoneNumber() {
        // Arrange
        Integer customerId = 1;
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "NewStr0ngP@ssw0rd", "Alice Updated", "invalidPhone");
        CustomerAccount existingCustomer = new CustomerAccount("customer@example.com", "encryptedOldPassword");

        // Mock repository behavior
        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(existingCustomer);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> customerService.updateCustomer(customerId, requestDto));

        // Assert
        assertEquals("Phone Number does not meet formatting criteria.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        verify(customerAccountRepository, times(0)).save(any(CustomerAccount.class));
    }

    // Tests for getAllCustomers service method

    @Test
    public void testGetAllCustomersSuccessfully() {
        // Arrange: Prepare a list of mock CustomerAccount objects
        CustomerAccount customer1 = new CustomerAccount("customer1@example.com", "password1");
        CustomerAccount customer2 = new CustomerAccount("customer2@example.com", "password2");
        List<CustomerAccount> mockCustomers = Arrays.asList(customer1, customer2);

        // Mock repository behavior to return the mock list
        when(customerAccountRepository.findAll()).thenReturn(mockCustomers);

        // Act: Call the service method
        List<CustomerAccount> customers = customerService.getAllCustomers();

        // Assert: Verify that the returned list matches the expected size and content
        assertEquals(2, customers.size());
        assertEquals(customer1.getEmail(), customers.get(0).getEmail());
        assertEquals(customer2.getEmail(), customers.get(1).getEmail());
    }

    // Tests for getCustomerById service method

    @Test
    public void testGetCustomerByIdSuccessfully() {
        // Arrange
        Integer customerId = 1;
        CustomerAccount customer = new CustomerAccount("customer@example.com", "password");

        // Mock the repo behavior for retrieving customer by its Id
        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);

        // Act
        CustomerAccount response = customerService.getCustomerById(customerId);

        // Assert
        assertNotNull(response);
        assertEquals("customer@example.com", response.getEmail());
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        // Arrange
        Integer customerId = 1;

        // Mock the repo behavior for retrieving non-existing customer account by Id
        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> customerService.getCustomerById(customerId));

        // Assert
        assertEquals("Customer not found.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    // Tests for login service method

    @Test
    public void testLoginSuccessfully() {
        // Arrange
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "password123");
        CustomerAccount customer = new CustomerAccount("customer@example.com", EncryptionUtils.encrypt("password123"));
        when(customerAccountRepository.findCustomerAccountByEmail(requestDto.getEmail())).thenReturn(customer);

        // Act
        CustomerAccount reponse = customerService.login(requestDto);

        // Assert
        assertNotNull(reponse);
        assertEquals("customer@example.com", reponse.getEmail());
    }

    @Test
    public void testLoginInvalidPassword() {
        // Arrange
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "wrongpassword");
        CustomerAccount customer = new CustomerAccount("customer@example.com", EncryptionUtils.encrypt("password123"));
        when(customerAccountRepository.findCustomerAccountByEmail(requestDto.getEmail())).thenReturn(customer);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> customerService.login(requestDto));

        // Assert
        assertEquals("Invalid email or password.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void testLoginCustomerNotFound() {
        // Arrange
        CustomerRequestDto requestDto = new CustomerRequestDto("nonexistent@example.com", "password123");
        when(customerAccountRepository.findCustomerAccountByEmail(requestDto.getEmail())).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> customerService.login(requestDto));

        // Assert
        assertEquals("Invalid email or password.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    // Tests for getOrderHistoryByCustomerId service method

    @Test
    public void testOrderHistoryRetrievalSuccessfully() {
        // Arrange
        Integer customerId = 1;
        CustomerAccount customer = new CustomerAccount(); // Create a real CustomerAccount instance
        // Mock the repository to return this instance
        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);

        CustomerOrder order1 = new CustomerOrder();
        CustomerOrder order2 = new CustomerOrder();
        List<CustomerOrder> mockOrders = Arrays.asList(order1, order2);

        // Mock the order repository to return the mock orders for the customer
        when(customerOrderRepository.findByOrderedBy(customer)).thenReturn(mockOrders);

        // Act
        List<CustomerOrder> response = customerService.getOrderHistoryByCustomerId(customerId);

        // Assert
        assertEquals(2, response.size());
        assertEquals(mockOrders, response);
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(customerOrderRepository, times(1)).findByOrderedBy(customer);
    }

    @Test
    public void testOrderHistoryRetrievalCustomerNotFound() {
        // Arrange
        Integer customerId = 2;

        // Mock the repository to return null when the customer is not found
        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.getOrderHistoryByCustomerId(customerId);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found.", exception.getMessage());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(customerOrderRepository, times(0)).findByOrderedBy(any(CustomerAccount.class));
    }

    @Test
    public void testOrderHistoryRetrievalNoOrdersFound() {
        // Arrange
        Integer customerId = 3;
        CustomerAccount customer = new CustomerAccount();
        // Mock the repository to return this customer for the provided ID
        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);

        // Mock the order repository to return an empty list for this customer
        when(customerOrderRepository.findByOrderedBy(customer)).thenReturn(Arrays.asList());

        // Act
        List<CustomerOrder> response = customerService.getOrderHistoryByCustomerId(customerId);

        // Assert
        assertEquals(0, response.size());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(customerOrderRepository, times(1)).findByOrderedBy(customer);
    }

    // Tests for createPaymentCard() service method

    @Test
    public void testCreatePaymentCardSuccessfully() {
        // Arrange
        Integer customerId = 1;
        PaymentDetailsRequestDto requestDto = new PaymentDetailsRequestDto("John Doe", "12345", 123456789, 12, 2025);
        CustomerAccount customer = new CustomerAccount();
        customer.setName("John Doe");

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(paymentDetailsRepository.save(any(PaymentDetails.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PaymentDetails result = customerService.createPaymentCard(customerId, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(customer.getName(), result.getCardName());
        assertEquals(requestDto.getCardNumber(), result.getCardNumber());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(1)).save(any(PaymentDetails.class));
    }

    @Test
    public void testCreatePaymentCardCustomerNotFound() {
        // Arrange
        Integer customerId = 1;
        PaymentDetailsRequestDto requestDto = new PaymentDetailsRequestDto("John Doe", "12345", 123456789, 12, 2025);

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.createPaymentCard(customerId, requestDto);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found.", exception.getMessage());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(0)).save(any(PaymentDetails.class));
    }

    @Test
    public void testCreatePaymentCardAlreadyExists() {
        // Arrange
        Integer customerId = 1;
        PaymentDetailsRequestDto requestDto = new PaymentDetailsRequestDto("John Doe", "12345", 123456789, 12, 2025);
        CustomerAccount customer = new CustomerAccount();
        customer.setName("John Doe");

        PaymentDetails existingCard = new PaymentDetails("John Doe", "12345", 123456789, 12, 2025, customer);
        customer.addPaymentCard(existingCard);

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.createPaymentCard(customerId, requestDto);
        });

        // Assert
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Payment details already exist.", exception.getMessage());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(0)).save(any(PaymentDetails.class));
    }

    // Tests for updatePaymentCard() service method

    @Test
    public void testUpdatePaymentCardSuccessfully() {
        // Arrange
        Integer customerId = 1;
        int cardId = 2;
        PaymentDetailsRequestDto requestDto = new PaymentDetailsRequestDto("Jane Doe", "54321", 987654321, 6, 2026);

        CustomerAccount customer = new CustomerAccount();
        customer.setName("Jane Doe");

        PaymentDetails card = new PaymentDetails("John Doe", "12345", 123456789, 12, 2025, customer);
        customer.addPaymentCard(card);

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(cardId)).thenReturn(card);

        // Act
        PaymentDetails result = customerService.updatePaymentCard(customerId, cardId, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(requestDto.getCardName(), result.getCardName());
        assertEquals(requestDto.getPostalCode(), result.getPostalCode());
        assertEquals(requestDto.getCardNumber(), result.getCardNumber());
        assertEquals(requestDto.getExpMonth(), result.getExpMonth());
        assertEquals(requestDto.getExpYear(), result.getExpYear());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(1)).findPaymentDetailsByPaymentDetailsId(cardId);
    }

    @Test
    public void testUpdatePaymentCardCustomerNotFound() {
        // Arrange
        Integer customerId = 1;
        int cardId = 2;
        PaymentDetailsRequestDto requestDto = new PaymentDetailsRequestDto("Jane Doe", "54321", 987654321, 6, 2026);

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.updatePaymentCard(customerId, cardId, requestDto);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found.", exception.getMessage());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(0)).findPaymentDetailsByPaymentDetailsId(cardId);
    }

    @Test
    public void testUpdatePaymentCardNotFound() {
        // Arrange
        Integer customerId = 1;
        int cardId = 2;
        PaymentDetailsRequestDto requestDto = new PaymentDetailsRequestDto("Jane Doe", "54321", 987654321, 6, 2026);

        CustomerAccount customer = new CustomerAccount();

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(cardId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.updatePaymentCard(customerId, cardId, requestDto);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Card not found.", exception.getMessage());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(1)).findPaymentDetailsByPaymentDetailsId(cardId);
    }

    @Test
    public void testUpdatePaymentCardDoesNotBelongToCustomer() {
        // Arrange
        Integer customerId = 1;
        int cardId = 2;
        PaymentDetailsRequestDto requestDto = new PaymentDetailsRequestDto("Jane Doe", "54321", 987654321, 6, 2026);

        CustomerAccount customer = new CustomerAccount();

        CustomerAccount otherCustomer = new CustomerAccount();
        PaymentDetails card = new PaymentDetails("Other", "12345", 123456789, 12, 2025, otherCustomer);

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(cardId)).thenReturn(card);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.updatePaymentCard(customerId, cardId, requestDto);
        });

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Card does not belong to the specified customer.", exception.getMessage());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(1)).findPaymentDetailsByPaymentDetailsId(cardId);
    }

    // Tests for getPaymentCardById service method

    @Test
    public void testGetPaymentCardByIdSuccessfully() {
        // Arrange
        Integer customerId = 1;
        Integer cardId = 101;
        CustomerAccount customer = new CustomerAccount();
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setPaymentDetailsId(cardId);
        paymentDetails.setCardOwner(customer);

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(cardId)).thenReturn(paymentDetails);

        // Act
        PaymentDetails result = customerService.getPaymentCardById(customerId, cardId);

        // Assert
        assertNotNull(result);
        assertEquals(cardId, result.getPaymentDetailsId());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(1)).findPaymentDetailsByPaymentDetailsId(cardId);
    }

    @Test
    public void testGetPaymentCardByIdCustomerNotFound() {
        // Arrange
        Integer customerId = 1;
        Integer cardId = 101;

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.getPaymentCardById(customerId, cardId);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found.", exception.getMessage());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(0)).findPaymentDetailsByPaymentDetailsId(cardId);
    }

    @Test
    public void testGetPaymentCardByIdCardNotFound() {
        // Arrange
        Integer customerId = 1;
        Integer cardId = 101;
        CustomerAccount customer = new CustomerAccount();

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(cardId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.getPaymentCardById(customerId, cardId);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Card not found.", exception.getMessage());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
        verify(paymentDetailsRepository, times(1)).findPaymentDetailsByPaymentDetailsId(cardId);
    }

    // Tests for getAllPaymentCardsByCustomerId service method

    @Test
    public void testGetAllPaymentCardsByCustomerIdSuccessfully() {
        // Arrange
        Integer customerId = 1;
        CustomerAccount customer = new CustomerAccount();
        PaymentDetails payment1 = new PaymentDetails();
        PaymentDetails payment2 = new PaymentDetails();
        customer.addPaymentCard(payment1);
        customer.addPaymentCard(payment2);

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);

        // Act
        List<PaymentDetails> result = customerService.getAllPaymentCardsByCustomerId(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
    }

    @Test
    public void testGetAllPaymentCardsByCustomerIdCustomerNotFound() {
        // Arrange
        Integer customerId = 1;

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.getAllPaymentCardsByCustomerId(customerId);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found.", exception.getMessage());
    }


    // Tests for addToWishlist service method

    @Test
    public void testAddToWishlistSuccessfully() {
        // Arrange
        Integer customerId = 1;
        Integer gameId = 101;
        CustomerAccount customer = new CustomerAccount();
        Game game = new Game();

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(game);

        // Act
        Game result = customerService.addToWishlist(customerId, gameId);

        // Assert
        assertNotNull(result);
        assertEquals(game, result);
        assertTrue(customer.getWishListedGames().contains(game));
        verify(customerAccountRepository, times(1)).save(any(CustomerAccount.class));
    }

    @Test
    public void testAddToWishlistCustomerNotFound() {
        // Arrange
        Integer customerId = 1;
        Integer gameId = 101;

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.addToWishlist(customerId, gameId);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found.", exception.getMessage());
    }

    @Test
    public void testAddToWishlistGameNotFound() {
        // Arrange
        Integer customerId = 1;
        Integer gameId = 101;
        CustomerAccount customer = new CustomerAccount();

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.addToWishlist(customerId, gameId);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game not found.", exception.getMessage());
    }

    // Tests for removeFromWishlist service method

    @Test
    public void testRemoveFromWishlistSuccessfully() {
        // Arrange
        Integer customerId = 1;
        Integer gameId = 101;
        CustomerAccount customer = new CustomerAccount();
        Game game = new Game();
        customer.addWishListedGame(game);

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(game);

        // Act
        Game result = customerService.removeFromWishlist(customerId, gameId);

        // Assert
        assertNotNull(result);
        assertEquals(game, result);
        assertFalse(customer.getWishListedGames().contains(game));
        verify(customerAccountRepository, times(1)).save(customer);
    }

    @Test
    public void testRemoveFromWishlistCustomerNotFound() {
        // Arrange
        Integer customerId = 1;
        Integer gameId = 101;

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.removeFromWishlist(customerId, gameId);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found.", exception.getMessage());
    }

    @Test
    public void testRemoveFromWishlistGameNotFound() {
        // Arrange
        Integer customerId = 1;
        Integer gameId = 101;
        CustomerAccount customer = new CustomerAccount();

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);
        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.removeFromWishlist(customerId, gameId);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game not found.", exception.getMessage());
    }

    // Tests for viewWishlist service method

    @Test
    public void testViewWishlistSuccessfully() {
        // Arrange
        Integer customerId = 1;
        CustomerAccount customer = new CustomerAccount();
        customer.addWishListedGame(new Game());
        customer.addWishListedGame(new Game());

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(customer);

        // Act
        List<Game> result = customerService.viewWishlist(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customerId);
    }

    @Test
    public void testViewWishlistCustomerNotFound() {
        // Arrange
        Integer customerId = 1;

        when(customerAccountRepository.findCustomerAccountByCustomerId(customerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            customerService.viewWishlist(customerId);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found.", exception.getMessage());
    }
}
