package ca.mcgill.ecse321.GameShop.service;

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

        // Mock repository behavior
        when(customerAccountRepository.findCustomerAccountByEmail(any(String.class))).thenReturn(null);
        when(customerAccountRepository.save(any(CustomerAccount.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CustomerAccount response = customerService.createCustomer(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getEmail(), response.getEmail());
        assertEquals(EncryptionUtils.encrypt(requestDto.getPassword()), response.getPassword());
        assertEquals(requestDto.getName(), response.getName());
        assertEquals(requestDto.getPhoneNumber(), response.getPhoneNumber());
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
        // assertEquals(EncryptionUtils.encrypt(requestDto.getPassword()), updatedCustomer.getPassword());
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

        // Act & Assert
        GameShopException e = assertThrows(GameShopException.class, () -> customerService.updateCustomer(customerId, requestDto));
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

        // Act & Assert
        GameShopException e = assertThrows(GameShopException.class, () -> customerService.updateCustomer(customerId, requestDto));
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

        // Act & Assert
        GameShopException e = assertThrows(GameShopException.class, () -> customerService.updateCustomer(customerId, requestDto));
        assertEquals("Phone Number does not meet formatting criteria.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        verify(customerAccountRepository, times(0)).save(any(CustomerAccount.class));
    }

}
