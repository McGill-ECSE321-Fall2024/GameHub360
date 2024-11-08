package ca.mcgill.ecse321.GameShop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.GameShop.model.ActivityLog;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.repository.ActivityLogRepository;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    /**
     * Retrieves all activity logs for all employees.
     *
     * @return List of all activity logs for all employees.
     */
    public List<ActivityLog> getAllEmployeesActivityLogs() {
        // Retrieve all activity logs
        List<ActivityLog> logs = (List<ActivityLog>) activityLogRepository.findAll();
        return logs;
    }

    /**
     * Logs an activity performed by an employee.
     *
     * @param content  The content of the activity.
     * @param employee The employee who performed the activity.
     */
    public void logActivity(String content, EmployeeAccount employee) {
        // Create a new activity log
        ActivityLog log = new ActivityLog(content, employee);
        activityLogRepository.save(log);
    }

    /**
     * Retrieves all activity logs for a specific employee.
     *
     * @param employee The employee to retrieve activity logs for.
     * @return List of all activity logs for the specified employee.
     */
    public List<ActivityLog> getEmployeeActivityLogs(EmployeeAccount employee) {
        // Retrieve all activity logs for the employee
        List<ActivityLog> logs = (List<ActivityLog>) activityLogRepository
                .findActivityLogsByEmployee_StaffId(employee.getStaffId());
        return logs;
    }
}
