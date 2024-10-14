package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.model.*;

@SpringBootTest
public class ActivityLogRepositoryTests {
    @Autowired
    private ActivityLogRepository repo;
    @Autowired
    private EmployeeAccountRepository employeeRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
        employeeRepo.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveActivityLog() {
        // Arrange
        EmployeeAccount employee = new EmployeeAccount("email@example.com", "password123", true);
        employeeRepo.save(employee);

        String content = "Test activity log";
        ActivityLog log = new ActivityLog(content, employee);

        // Act
        repo.save(log);

        ActivityLog logFromDb = repo.findActivityLogByLogId(log.getLogId());

        // Assert
        assertNotNull(logFromDb);
        assertEquals(log.getLogId(), logFromDb.getLogId());
        assertEquals(content, logFromDb.getContent());

        assertNotNull(logFromDb.getEmployee());
        assertEquals(employee.getStaffId(), logFromDb.getEmployee().getStaffId());
    }
}
