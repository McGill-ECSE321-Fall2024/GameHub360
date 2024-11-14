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

import ca.mcgill.ecse321.GameShop.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.repository.EmployeeAccountRepository;
import ca.mcgill.ecse321.GameShop.utils.EncryptionUtils;

@SpringBootTest
public class EmployeeServiceTests {

    @Mock
    private EmployeeAccountRepository employeeAccountRepository;

    @Mock
    private ActivityLogService activityLogService;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testLoginSuccess() {
        // Arrange
        EmployeeRequestDto requestDto = new EmployeeRequestDto("employee@example.com", "password123");
        EmployeeAccount employee = new EmployeeAccount("employee@example.com", EncryptionUtils.encrypt("password123"),
                true);
        employee.setName("John Doe");
        employee.setPhoneNumber("123-456-7890");

        // Mock repository behavior
        when(employeeAccountRepository.findEmployeeAccountByEmail(any(String.class))).thenReturn(employee);

        // Act
        EmployeeAccount response = employeeService.login(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(employee.getEmail(), response.getEmail());
        assertEquals(employee.getName(), response.getName());
        assertEquals(employee.getPhoneNumber(), response.getPhoneNumber());
        verify(employeeAccountRepository, times(1)).findEmployeeAccountByEmail("employee@example.com");
    }

    @Test
    public void testLoginWithInvalidPassword() {
        // Arrange
        EmployeeRequestDto requestDto = new EmployeeRequestDto("employee@example.com", "wrongpassword");
        EmployeeAccount employee = new EmployeeAccount("employee@example.com", EncryptionUtils.encrypt("password123"),
                true);

        // Mock repository behavior
        when(employeeAccountRepository.findEmployeeAccountByEmail(any(String.class))).thenReturn(employee);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> employeeService.login(requestDto));

        // Assert
        assertEquals("Invalid email or password.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        verify(employeeAccountRepository, times(1)).findEmployeeAccountByEmail("employee@example.com");
    }

    @Test
    public void testLoginWithNonExistentEmail() {
        // Arrange
        EmployeeRequestDto requestDto = new EmployeeRequestDto("nonexistent@example.com", "password123");

        // Mock repository behavior
        when(employeeAccountRepository.findEmployeeAccountByEmail(any(String.class))).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> employeeService.login(requestDto));

        // Assert
        assertEquals("Invalid email or password.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        verify(employeeAccountRepository, times(1)).findEmployeeAccountByEmail("nonexistent@example.com");
    }

    @Test
    public void testCreateEmployeeSuccess() {
        // Arrange
        EmployeeRequestDto requestDto = new EmployeeRequestDto("employee@example.com", "StrongP@ssw0rd", "John Doe",
                "123-456-7890", true);

        // Mock repository behavior to simulate no existing employee
        when(employeeAccountRepository.count()).thenReturn(0L);
        when(employeeAccountRepository.save(any(EmployeeAccount.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        EmployeeAccount response = employeeService.createEmployee(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getEmail(), response.getEmail());
        assertEquals(requestDto.getName(), response.getName());
        assertEquals(requestDto.getPhoneNumber(), response.getPhoneNumber());
        verify(employeeAccountRepository, times(1)).save(any(EmployeeAccount.class));
    }

    @Test
    public void testCreateEmployeeWhenEmployeeExistsWithTheSameEmail() {
        // Arrange
        EmployeeRequestDto requestDto = new EmployeeRequestDto("employee@example.com", "StrongP@ssw0rd", "John Doe",
                "123-456-7890", true);
        EmployeeAccount employee = new EmployeeAccount("employee@example.com", "StrongP@ssw0rd", true);

        // Mock repository behavior to simulate existing employee
        when(employeeAccountRepository.findEmployeeAccountByEmail(any(String.class))).thenReturn(employee);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> employeeService.createEmployee(requestDto));

        // Assert
        assertEquals("An employee with the same email already exists.", e.getMessage());
        assertEquals(HttpStatus.CONFLICT, e.getStatus());
    }

    @Test
    public void testUpdateEmployeeSuccess() {
        // Arrange
        EmployeeRequestDto requestDto = new EmployeeRequestDto("employee@example.com", "NewStr0ngP@ssw0rd",
                "John Updated",
                "321-654-0987", true);
        EmployeeAccount employee = new EmployeeAccount("employee@example.com", EncryptionUtils.encrypt("OldPassword"),
                true);
        employee.setName("John Doe");
        employee.setPhoneNumber("123-456-7890");

        // Mock repository behavior
        when(employeeAccountRepository.findEmployeeAccountByStaffId(any(Integer.class))).thenReturn(employee);
        when(employeeAccountRepository.save(any(EmployeeAccount.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        EmployeeAccount response = employeeService.updateEmployee(employee.getStaffId(), requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getName(), response.getName());
        assertEquals(requestDto.getPhoneNumber(), response.getPhoneNumber());
        verify(employeeAccountRepository, times(1)).save(employee);
    }

    @Test
    public void testUpdateEmployeeNotFound() {
        // Arrange
        EmployeeRequestDto requestDto = new EmployeeRequestDto("nonexistent@example.com", "password123");

        // Mock repository behavior
        when(employeeAccountRepository.findEmployeeAccountByStaffId(any(Integer.class))).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> employeeService.updateEmployee(1, requestDto));

        // Assert
        assertEquals("Employee not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testUpdateEmployeeWithInvalidPassword() {
        // Arrange
        EmployeeRequestDto requestDto = new EmployeeRequestDto("employee@example.com", "NewWeakPassword",
                "John Updated",
                "321-654-0987", true);
        EmployeeAccount employee = new EmployeeAccount("employee@example.com", EncryptionUtils.encrypt("OldPassword"),
                true);
        employee.setName("John Doe");
        employee.setPhoneNumber("123-456-7890");

        // Mock repository behavior
        when(employeeAccountRepository.findEmployeeAccountByStaffId(any(Integer.class))).thenReturn(employee);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> employeeService.updateEmployee(employee.getStaffId(), requestDto));

        // Assert
        assertEquals("Password does not meet security requirements.", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void testDeactivateEmployeeSuccess() {
        // Arrange
        EmployeeAccount employee = new EmployeeAccount("employee@example.com", EncryptionUtils.encrypt("OldPassword"),
                true);
        employee.setName("John Doe");

        // Mock repository behavior
        when(employeeAccountRepository.findEmployeeAccountByStaffId(any(Integer.class))).thenReturn(employee);
        when(employeeAccountRepository.save(any(EmployeeAccount.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        EmployeeAccount response = employeeService.deactivateEmployee(employee.getStaffId());

        // Assert
        assertNotNull(response);
        assertEquals(false, response.getIsActive());
        verify(employeeAccountRepository, times(1)).save(employee);
    }

    @Test
    public void testDeactivateEmployeeNotFound() {
        // Arrange
        // Mock repository behavior to simulate employee not found
        when(employeeAccountRepository.findEmployeeAccountByStaffId(any(Integer.class))).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> employeeService.deactivateEmployee(1));

        // Assert
        assertEquals("Employee not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetEmployeeByIdSuccess() {
        // Arrange
        EmployeeAccount employee = new EmployeeAccount("employee@example.com", EncryptionUtils.encrypt("OldPassword"),
                true);
        employee.setName("John Doe");

        // Mock repository behavior
        when(employeeAccountRepository.findEmployeeAccountByStaffId(any(Integer.class))).thenReturn(employee);

        // Act
        EmployeeAccount response = employeeService.getEmployeeById(employee.getStaffId());

        // Assert
        assertNotNull(response);
        assertEquals(employee.getEmail(), response.getEmail());
        assertEquals(employee.getName(), response.getName());
        assertEquals(employee.getPhoneNumber(), response.getPhoneNumber());
        verify(employeeAccountRepository, times(1)).findEmployeeAccountByStaffId(employee.getStaffId());
    }

    @Test
    public void testGetEmployeeByIdNotFound() {
        // Arrange
        // Mock repository behavior to simulate employee not found
        when(employeeAccountRepository.findEmployeeAccountByStaffId(any(Integer.class))).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> employeeService.getEmployeeById(1));

        // Assert
        assertEquals("Employee not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

}
