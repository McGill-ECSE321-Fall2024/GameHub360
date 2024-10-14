package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
@SpringBootTest
public class EmployeeAccountRepositoryTests {
    @Autowired
    private EmployeeAccountRepository repo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveEmployeeAccount() {
        // Arrange
        EmployeeAccount employee = new EmployeeAccount("email@example.com", "password123", true);
        // Act
        repo.save(employee);
        // Assert
        EmployeeAccount retrievedEmployee = repo.findEmployeeAccountByStaffId(employee.getStaffId());
        assertNotNull(retrievedEmployee);
        assertEquals("email@example.com", retrievedEmployee.getEmail());
        assertEquals("password123", retrievedEmployee.getPassword());
        assertTrue(retrievedEmployee.getIsActive());
    }

    @Test
    public void testUpdateEmployeeAccount() {
        // Arrange
        EmployeeAccount employee = new EmployeeAccount("email@example.com", "password123", true);
        // Act
        repo.save(employee);
        employee.setPhoneNumber("4388289999");
        employee.setPassword("newpassword123");
        employee.setIsActive(false);
        repo.save(employee);
        // Assert
        EmployeeAccount updatedEmployee = repo.findEmployeeAccountByStaffId(employee.getStaffId());
        assertNotNull(updatedEmployee);
        assertEquals("4388289999", updatedEmployee.getPhoneNumber());
        assertEquals("newpassword123", updatedEmployee.getPassword());
        assertFalse(updatedEmployee.getIsActive());
    }

    @Test
    public void testDeleteEmployeeAccount() {
        // Arrange
        EmployeeAccount employee = new EmployeeAccount("email@example.com", "password123", true);
        // Act
        repo.save(employee);
        repo.delete(employee);
        // Assert
        EmployeeAccount deletedEmployee = repo.findEmployeeAccountByStaffId(employee.getStaffId());
        assertNull(deletedEmployee);
    }

}
