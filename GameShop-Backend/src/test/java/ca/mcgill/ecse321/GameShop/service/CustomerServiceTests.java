package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.CustomerRequestDto;
import ca.mcgill.ecse321.GameShop.exception.ManagerException;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.utils.EncryptionUtils;
import ca.mcgill.ecse321.GameShop.utils.PasswordUtils;
import ca.mcgill.ecse321.GameShop.utils.PhoneUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTests {

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void testCreateCustomerSuccessfully() {
        // Arrange
        CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "Strong@Pass1", "John Doe",
         "123-456-7890");

        // Mock Repository behavior to simulate a non existing account and the saving of a new one
        when(customerAccountRepository.findCustomerAccountByEmail(requestDto.getEmail())).thenReturn(null);
        when(customerAccountRepository.save(any(CustomerAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CustomerAccount response = customerService.createCustomer(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getEmail(), response.getEmail());
        assertEquals(requestDto.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(requestDto.getName(), response.getName());
        verify(customerAccountRepository, times(1)).save(any(CustomerAccount.class));
    }

     @Test
     public void testCreateCustomerWithExistingEmail() {
         // Arrange
         CustomerRequestDto requestDto = new CustomerRequestDto("customer@example.com", "Strong@Pass1", "John Doe",
                 "123-456-7890");
         when(customerAccountRepository.findCustomerAccountByEmail("customer@example.com")).thenReturn(new CustomerAccount());

         // Act & Assert
         ManagerException exception = assertThrows(ManagerException.class, () -> customerService.createCustomer(requestDto));
         assertEquals(HttpStatus.CONFLICT, exception.getStatus());
         assertEquals("An account with this email already exists.", exception.getMessage());
         verify(customerAccountRepository, never()).save(any(CustomerAccount.class));
     }

     @Test
     public void testCreateCustomerWithInvalidPassword() {
         // Arrange
         CustomerRequestDto requestDto = new CustomerRequestDto(VALID_EMAIL, INVALID_PASSWORD, "John Doe", VALID_PHONE);
         when(customerAccountRepository.findCustomerAccountByEmail(VALID_EMAIL)).thenReturn(null);

         // Act & Assert
         ManagerException exception = assertThrows(ManagerException.class, () -> customerService.createCustomer(requestDto));
         assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
         assertEquals("Password does not meet security requirements.", exception.getMessage());
         verify(customerAccountRepository, never()).save(any(CustomerAccount.class));
     }

     @Test
     public void testCreateCustomerWithInvalidPhoneNumber() {
         // Arrange
         CustomerRequestDto requestDto = new CustomerRequestDto(VALID_EMAIL, VALID_PASSWORD, "John Doe", INVALID_PHONE);
         when(customerAccountRepository.findCustomerAccountByEmail(VALID_EMAIL)).thenReturn(null);

         // Act & Assert
         ManagerException exception = assertThrows(ManagerException.class, () -> customerService.createCustomer(requestDto));
         assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
         assertEquals("Phone Number does not meet formatting criteria.", exception.getMessage());
         verify(customerAccountRepository, never()).save(any(CustomerAccount.class));
     }
}
