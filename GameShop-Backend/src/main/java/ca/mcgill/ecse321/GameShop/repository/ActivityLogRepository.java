package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.ActivityLog;

public interface ActivityLogRepository extends CrudRepository<ActivityLog, Integer> {
    // Find activity log by log id
    public ActivityLog findActivityLogByLogId(Integer logId);

    // Find activity logs by employee
    public Iterable<ActivityLog> findActivityLogsByEmployee_StaffId(Integer staffId);
}
