package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.ActivityLog;

public interface ActivityLogRepository extends CrudRepository<ActivityLog, Integer> {
    public ActivityLog findActivityLogByLogId(Integer logId);
}
