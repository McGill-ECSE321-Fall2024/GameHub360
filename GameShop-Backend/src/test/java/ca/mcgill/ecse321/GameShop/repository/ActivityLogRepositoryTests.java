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
        ActivityLog log = new ActivityLog("Test activity log", employee);
        repo.save(log);

        ActivityLog retrievedLog = repo.findById(log.getLogId()).orElse(null);
        assertNotNull(retrievedLog);
        assertEquals("Test activity log", retrievedLog.getContent());
        assertEquals(employee.getStaffId(), retrievedLog.getEmployee().getStaffId());
    }

    @Test
    public void testUpdateActivityLog() {

        ActivityLog log = new ActivityLog("Initial description", employee);
        repo.save(log);

        log.setContent("Updated description");
        repo.save(log);

        ActivityLog updatedLog = repo.findById(log.getLogId()).orElse(null);
        assertNotNull(updatedLog);
        assertEquals("Updated description", updatedLog.getContent());

        employee2 = new EmployeeAccount("email2@example.com", "password1234", true);
        employeeRepo.save(employee2);

        log.setEmployee(employee2);
        repo.save(log);

        ActivityLog updatedLog2 = repo.findById(log.getLogId()).orElse(null);
        assertNotNull(updatedLog2);
        assertEquals(updatedLog2.getEmployee().getStaffId(), employee2.getStaffId());
    }

    @Test
    public void testDeleteActivityLog() {
        ActivityLog log = new ActivityLog("Test activity log", employee);
        repo.save(log);

        repo.delete(log);
        ActivityLog deletedLog = repo.findById(log.getLogId()).orElse(null);
        assertNull(deletedLog);
    }

    @Test
    public void testFindAllActivityLogs() {
        ActivityLog log1 = new ActivityLog("First log", employee);
        repo.save(log1);

        ActivityLog log2 = new ActivityLog("Second log", employee);
        repo.save(log2);

        assertEquals(2, employee.numberOfLogs());
    }

}
