package ca.mcgill.ecse321.GameShop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.ActivityLog;

public interface ActivityLogRepository extends CrudRepository<ActivityLog, Integer> {
    // Find activity log by log id
    public ActivityLog findActivityLogByLogId(Integer logId);

    // Find activity log by employee id
    public List<ActivityLog> findActivityLogByEmployeeId(Integer employeeId);
}
