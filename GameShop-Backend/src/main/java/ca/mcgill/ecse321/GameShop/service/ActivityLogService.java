package ca.mcgill.ecse321.GameShop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.GameShop.dto.ActivityLogResponseDto;
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
    public ActivityLogResponseDto getAllEmployeesActivityLogs() {
        List<ActivityLog> logs = (List<ActivityLog>) activityLogRepository.findAll();
        return new ActivityLogResponseDto(logs);
    }

    /**
     * Logs an activity performed by an employee.
     *
     * @param content  The content of the activity.
     * @param employee The employee who performed the activity.
     */
    public void logActivity(String content, EmployeeAccount employee) {
        ActivityLog log = new ActivityLog(content, employee);
        activityLogRepository.save(log);
    }
}
