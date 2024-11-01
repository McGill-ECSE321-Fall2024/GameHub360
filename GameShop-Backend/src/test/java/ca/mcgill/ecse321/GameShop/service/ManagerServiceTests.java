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

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.exception.ManagerException;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import ca.mcgill.ecse321.GameShop.utils.EncryptionUtils;

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
        ManagerAccount manager = new ManagerAccount("manager@example.com", EncryptionUtils.encrypt("password123"));
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
        ManagerAccount manager = new ManagerAccount("manager@example.com", EncryptionUtils.encrypt("password123"));

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
    public void testCreateManagerSuccess() {
        // Arrange
        ManagerRequestDto requestDto = new ManagerRequestDto("manager@example.com", "StrongP@ssw0rd", "John Doe",
                "123-456-7890");

        // Mock repository behavior to simulate no existing manager
        when(managerAccountRepository.count()).thenReturn(0L);
        when(managerAccountRepository.save(any(ManagerAccount.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        ManagerAccount response = managerService.createManager(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getEmail(), response.getEmail());
        assertEquals(requestDto.getName(), response.getName());
        assertEquals(requestDto.getPhoneNumber(), response.getPhoneNumber());
        verify(managerAccountRepository, times(1)).save(any(ManagerAccount.class));
    }

    @Test
    public void testCreateManagerWhenManagerExists() {
        // Arrange
        ManagerRequestDto requestDto = new ManagerRequestDto("manager@example.com", "StrongP@ssw0rd");

        // Mock repository behavior to simulate an existing manager
        when(managerAccountRepository.count()).thenReturn(1L);

        // Act
        ManagerException e = assertThrows(ManagerException.class, () -> managerService.createManager(requestDto));

        // Assert
        assertEquals("A manager already exists. Only one manager is allowed.", e.getMessage());
        assertEquals(HttpStatus.CONFLICT, e.getStatus());
    }

    @Test
    public void testUpdateManagerSuccess() {
        // Arrange
        ManagerRequestDto requestDto = new ManagerRequestDto("manager@example.com", "NewStr0ngP@ssw0rd", "John Updated",
                "321-654-0987");
        ManagerAccount manager = new ManagerAccount("manager@example.com", EncryptionUtils.encrypt("OldPassword"));
        manager.setName("John Doe");
        manager.setPhoneNumber("123-456-7890");

        // Mock repository behavior
        when(managerAccountRepository.findManagerAccountByEmail(any(String.class))).thenReturn(manager);
        when(managerAccountRepository.save(any(ManagerAccount.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        ManagerAccount response = managerService.updateManager(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getName(), response.getName());
        assertEquals(requestDto.getPhoneNumber(), response.getPhoneNumber());
        verify(managerAccountRepository, times(1)).save(manager);
    }

    @Test
    public void testUpdateManagerNotFound() {
        // Arrange
        ManagerRequestDto requestDto = new ManagerRequestDto("nonexistent@example.com", "password123");

        // Mock repository behavior to simulate manager not found
        when(managerAccountRepository.findManagerAccountByEmail(any(String.class))).thenReturn(null);

        // Act
        ManagerException e = assertThrows(ManagerException.class, () -> managerService.updateManager(requestDto));

        // Assert
        assertEquals("Manager not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }
}
