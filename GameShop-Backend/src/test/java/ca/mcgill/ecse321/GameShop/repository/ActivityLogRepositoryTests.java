package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.model.*;
import java.util.List;
@SpringBootTest
public class ActivityLogRepositoryTests {
    @Autowired
    private ActivityLogRepository repo;
    @Autowired
    private EmployeeAccountRepository employeeRepo;

    private EmployeeAccount employee;
    private EmployeeAccount employee2;
    @BeforeEach
    public void setUp() {
        employee = new EmployeeAccount("email@example.com", "password123", true);
        employeeRepo.save(employee);
    }
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
        employeeRepo.deleteAll();
    }
    @Test
    public void testCreateAndRetrieveActivityLog() {
        // Arrange
        ActivityLog log = new ActivityLog("Test activity log", employee);
        // Act
        repo.save(log);
        // Assert
        ActivityLog retrievedLog = repo.findActivityLogByLogId(log.getLogId());
        assertNotNull(retrievedLog);
        assertEquals("Test activity log", retrievedLog.getContent());
        assertEquals(log.getLogId(), retrievedLog.getLogId());

        assertNotNull(retrievedLog.getEmployee());
        assertEquals(employee.getStaffId(), retrievedLog.getEmployee().getStaffId());
    }

    @Test
    public void testUpdateActivityLog() {
        // Arrange
        ActivityLog log = new ActivityLog("Initial description", employee);
        employee2 = new EmployeeAccount("email2@example.com", "password1234", true);
        // Act
        repo.save(log);
        log.setContent("Updated description");
        employeeRepo.save(employee2);
        log.setEmployee(employee2);
        repo.save(log);
        // Assert
        ActivityLog updatedLog = repo.findActivityLogByLogId(log.getLogId());
        assertNotNull(updatedLog);
        assertEquals("Updated description", updatedLog.getContent());
        assertEquals(updatedLog.getEmployee().getStaffId(), employee2.getStaffId());
    }

    @Test
    public void testDeleteActivityLog() {
        // Arrange
        ActivityLog log = new ActivityLog("Test activity log", employee);
        // Act
        repo.save(log);
        repo.delete(log);
        // Assert
        ActivityLog deletedLog = repo.findActivityLogByLogId(log.getLogId());
        assertNull(deletedLog);
    }


}
