package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.GameShop.model.ActivityLog;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.repository.ActivityLogRepository;
import ca.mcgill.ecse321.GameShop.repository.EmployeeAccountRepository;

@SpringBootTest
public class ActivityLogServiceTests {

    @Mock
    private EmployeeAccountRepository employeeAccountRepository;

    @Mock
    private ActivityLogRepository activityLogRepository;

    @InjectMocks
    private ActivityLogService activityLogService;

    @Test
    public void testLogActivitySucess() {
        // Arrange
        EmployeeAccount employee = new EmployeeAccount("example@mcgill.ca", "password", true);

        // Mock
        when(activityLogRepository.save(any(ActivityLog.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        ActivityLog createdActivityLog = activityLogService.logActivity("Test1", employee);

        // Assert
        assertNotNull(createdActivityLog);
        assertEquals("Test1", createdActivityLog.getContent());
        assertEquals(employee, createdActivityLog.getEmployee());
        verify(activityLogRepository, times(1)).save(any(ActivityLog.class));
    }

    @Test
    public void testLogActivityForNullEmployeeFailure() {
        // Arrange
        EmployeeAccount employee = null;

        // Act
        try {
            activityLogService.logActivity("Test1", employee);
        } catch (Exception e) {
            // Assert
            assertEquals("Employee not found.", e.getMessage());
        }
    }

    @Test
    public void testGetAllEmployeesActivityLogs() {
        // Arrange
        EmployeeAccount employee1 = new EmployeeAccount("example1@mcgill.ca", "password", true);
        EmployeeAccount employee2 = new EmployeeAccount("example2@mcgill.ca", "password", true);

        // Mock
        when(activityLogRepository.findAll()).thenReturn(List.of(new ActivityLog("Test1", employee1),
                new ActivityLog("Test2", employee2)));

        // Act
        List<ActivityLog> logs = activityLogService.getAllEmployeesActivityLogs();

        // Assert
        assertNotNull(logs);
        assertEquals(2, logs.size());
        verify(activityLogRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployeeActivityLogs() {
        // Arrange
        EmployeeAccount employee = new EmployeeAccount("example@mcgill.ca", "password", true);
        activityLogService.logActivity("Test1", employee);
        activityLogService.logActivity("Test2", employee);

        // Mock
        when(activityLogRepository.findActivityLogsByEmployee_StaffId(employee.getStaffId()))
                .thenReturn(List.of(new ActivityLog("Test1", employee), new ActivityLog("Test2", employee)));

        // Act
        List<ActivityLog> logs = activityLogService.getEmployeeActivityLogs(employee);

        // Assert
        assertNotNull(logs);
        assertEquals(2, logs.size());
        verify(activityLogRepository, times(1)).findActivityLogsByEmployee_StaffId(employee.getStaffId());
    }

    @Test
    public void testGetEmployeeActivityLogsForNullEmployeeFailure() {
        // Arrange
        EmployeeAccount employee = null;

        // Act
        try {
            activityLogService.getEmployeeActivityLogs(employee);
        } catch (Exception e) {
            // Assert
            assertEquals("Employee not found.", e.getMessage());
        }
    }

}
