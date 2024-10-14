package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
        String email = "email@example.com";
        String password = "password123";
        Boolean isActive = true;
        EmployeeAccount employee = new EmployeeAccount(email, password, isActive);

        // Act
        repo.save(employee);

        EmployeeAccount retrievedEmployee = repo.findEmployeeAccountByStaffId(employee.getStaffId());

        // Assert
        assertNotNull(retrievedEmployee);
        assertEquals(employee.getStaffId(), retrievedEmployee.getStaffId());
        assertEquals(email, retrievedEmployee.getEmail());
        assertEquals(password, retrievedEmployee.getPassword());
        assertEquals(isActive, retrievedEmployee.getIsActive());
    }
}
