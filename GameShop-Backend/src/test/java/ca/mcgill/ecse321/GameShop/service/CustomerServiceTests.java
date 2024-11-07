package ca.mcgill.ecse321.GameShop.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import ca.mcgill.ecse321.GameShop.utils.PasswordUtils;
import ca.mcgill.ecse321.GameShop.utils.PhoneUtils;

@SpringBootTest
public class CustomerServiceTests {

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @InjectMocks
    private CustomerService customerService;

    // Tests for createCustomer service method

    @Test
    public void testCreateCustomerSuccess() {
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
    public void testUpdateCustomerSuccess() {
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
    public void testGetAllCustomers() {
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
    public void testGetCustomerByIdSuccess() {
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
    public void testLoginSuccess() {
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
}
