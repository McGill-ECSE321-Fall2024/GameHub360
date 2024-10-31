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

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.exception.ManagerException;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class ManagerServiceTests {

    @Mock
    private ManagerAccountRepository managerAccountRepository;

    @InjectMocks
    private ManagerService managerService;

    @Test
    public void testLoginSuccess() {
        // Arrange
        ManagerRequestDto requestDto = new ManagerRequestDto("manager@example.com", "password123");
        ManagerAccount manager = new ManagerAccount("manager@example.com", "password123");
        manager.setName("John Doe");
        manager.setPhoneNumber("123-456-7890");

        // Mock repository behavior
        when(managerAccountRepository.findManagerAccountByEmail(any(String.class))).thenReturn(manager);

        // Act
        ManagerAccount response = managerService.login(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(manager.getEmail(), response.getEmail());
        assertEquals(manager.getName(), response.getName());
        assertEquals(manager.getPhoneNumber(), response.getPhoneNumber());
        verify(managerAccountRepository, times(1)).findManagerAccountByEmail("manager@example.com");
    }

    @Test
    public void testLoginWithInvalidPassword() {
        // Arrange
        ManagerRequestDto requestDto = new ManagerRequestDto("manager@example.com", "wrongpassword");
        ManagerAccount manager = new ManagerAccount("manager@example.com", "password123");

        // Mock repository behavior
        when(managerAccountRepository.findManagerAccountByEmail(any(String.class))).thenReturn(manager);

        // Act
        ManagerException e = assertThrows(ManagerException.class, () -> managerService.login(requestDto));

        // Assert
        assertEquals("Invalid email or password.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        verify(managerAccountRepository, times(1)).findManagerAccountByEmail("manager@example.com");
    }

    @Test
    public void testLoginWithNonExistentEmail() {
        // Arrange
        ManagerRequestDto requestDto = new ManagerRequestDto("nonexistent@example.com", "password123");

        // Mock repository behavior
        when(managerAccountRepository.findManagerAccountByEmail(any(String.class))).thenReturn(null);

        // Act
        ManagerException e = assertThrows(ManagerException.class, () -> managerService.login(requestDto));

        // Assert
        assertEquals("Invalid email or password.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        verify(managerAccountRepository, times(1)).findManagerAccountByEmail("nonexistent@example.com");
    }

    @Test
    public void testNullEmailThrowsException() {
        // Arrange
        ManagerRequestDto requestDto = new ManagerRequestDto(null, "password123");

        // Act
        ManagerException e = assertThrows(ManagerException.class, () -> managerService.login(requestDto));

        // Assert
        assertEquals("Email cannot be empty.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void testNullPasswordThrowsException() {
        // Arrange
        ManagerRequestDto requestDto = new ManagerRequestDto("manager@example.com", null);

        // Act
        ManagerException e = assertThrows(ManagerException.class, () -> managerService.login(requestDto));

        // Assert
        assertEquals("Password cannot be empty.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }
}
