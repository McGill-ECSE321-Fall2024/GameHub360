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
        EmployeeAccount employee = new EmployeeAccount("email@example.com", "password123", true);
        repo.save(employee);

        EmployeeAccount retrievedEmployee = repo.findById(employee.getStaffId()).orElse(null);
        assertNotNull(retrievedEmployee);
        assertEquals("email@example.com", retrievedEmployee.getEmail());
        assertEquals("password123", retrievedEmployee.getPassword());
        assertTrue(retrievedEmployee.getIsActive());
    }

    @Test
    public void testUpdateEmployeeAccount() {
        EmployeeAccount employee = new EmployeeAccount("email@example.com", "password123", true);
        repo.save(employee);

        employee.setPhoneNumber("4388289999");
        employee.setPassword("newpassword123");
        employee.setIsActive(false);
        repo.save(employee);

        EmployeeAccount updatedEmployee = repo.findById(employee.getStaffId()).orElse(null);
        assertNotNull(updatedEmployee);
        assertEquals("4388289999", updatedEmployee.getPhoneNumber());
        assertEquals("newpassword123", updatedEmployee.getPassword());
        assertFalse(updatedEmployee.getIsActive());
    }

    @Test
    public void testDeleteEmployeeAccount() {
        EmployeeAccount employee = new EmployeeAccount("email@example.com", "password123", true);
        repo.save(employee);

        repo.delete(employee);
        EmployeeAccount deletedEmployee = repo.findById(employee.getStaffId()).orElse(null);
        assertNull(deletedEmployee);
    }

    @Test
    public void testFindAllEmployeeAccounts() {
        EmployeeAccount employee1 = new EmployeeAccount("email1@example.com", "password123", true);
        EmployeeAccount employee2 = new EmployeeAccount("email2@example.com", "password1234", true);
        repo.save(employee1);
        repo.save(employee2);


        List<EmployeeAccount> employees = new ArrayList<>();
        repo.findAll().forEach(employees::add);
        assertEquals(2, employees.size());
    }

}
