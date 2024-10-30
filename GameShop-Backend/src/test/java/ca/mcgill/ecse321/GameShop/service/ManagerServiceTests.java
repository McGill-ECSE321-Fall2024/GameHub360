package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;

@SpringBootTest
public class ManagerServiceTests {

    @Mock
    private ManagerAccountRepository managerAccountRepository;

    @InjectMocks
    private ManagerService managerService;

    @Test
    public void testLoginSuccess() {
        // Set up
        ManagerRequestDto requestDto = new ManagerRequestDto("manager@example.com", "password123");
        ManagerAccount manager = new ManagerAccount("manager@example.com", "password123");
        manager.setName("John Doe");
        manager.setPhoneNumber("123-456-7890");

        // Mock repository behavior
        when(managerAccountRepository.findManagerAccountByEmail(any(String.class))).thenReturn(manager);

        // Act
        ManagerResponseDto responseDto = managerService.login(requestDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals(manager.getStaffId(), responseDto.getStaffId());
        assertEquals(manager.getEmail(), responseDto.getEmail());
        assertEquals(manager.getName(), responseDto.getName());
        assertEquals(manager.getPhoneNumber(), responseDto.getPhoneNumber());
        verify(managerAccountRepository, times(1)).findManagerAccountByEmail("manager@example.com");
    }

    @Test
    public void testLoginIncorrectPassword() {
        // Set up
        ManagerRequestDto requestDto = new ManagerRequestDto("manager@example.com", "wrongpassword");
        ManagerAccount manager = new ManagerAccount("manager@example.com", "password123");

        // Mock repository behavior
        when(managerAccountRepository.findManagerAccountByEmail(any(String.class))).thenReturn(manager);

        // Act
        ManagerResponseDto responseDto = managerService.login(requestDto);

        // Assert
        assertNull(responseDto); 
        verify(managerAccountRepository, times(1)).findManagerAccountByEmail("manager@example.com");
    }

    @Test
    public void testLoginNonExistentEmail() {
        // Set up
        ManagerRequestDto requestDto = new ManagerRequestDto("nonexistent@example.com", "password123");

        // Mock repository behavior
        when(managerAccountRepository.findManagerAccountByEmail(any(String.class))).thenReturn(null);

        // Act
        ManagerResponseDto responseDto = managerService.login(requestDto);

        // Assert
        assertNull(responseDto); // Should be null due to non-existent email
        verify(managerAccountRepository, times(1)).findManagerAccountByEmail("nonexistent@example.com");
    }

    @Test
    public void testNullEmailThrowsException() {
        // Set up
        ManagerRequestDto requestDto = new ManagerRequestDto(null, "password123");

        // Act 
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> managerService.login(requestDto));
        
        // Assert
        assertEquals("Email cannot be empty.", e.getMessage());
    }

    @Test
    public void testNullPasswordThrowsException() {
        // Set up
        ManagerRequestDto requestDto = new ManagerRequestDto("manager@example.com", null);

        // Act 
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> managerService.login(requestDto));
        
        // Assert
        assertEquals("Password cannot be empty.", e.getMessage());
    }
}
