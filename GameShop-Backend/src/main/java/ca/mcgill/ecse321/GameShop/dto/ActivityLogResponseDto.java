package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

import ca.mcgill.ecse321.GameShop.model.ActivityLog;

public class ActivityLogResponseDto {
    private Integer logId;
    private String content;
    private Integer employeeId;
    private List<ActivityLog> activityLogs; //I think this should not be here as it's not a response attribute (based on the model class)

    public ActivityLogResponseDto(List<ActivityLog> activityLogs) {
        this.activityLogs = activityLogs;
    }

    public ActivityLogResponseDto() {
    }

    public ActivityLogResponseDto(Integer logId, String content, Integer employeeId) {
        this.logId = logId;
        this.content = content;
        this.employeeId = employeeId;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public List<ActivityLog> getActivityLogs() {
        return activityLogs;
    }

    public void setActivityLogs(List<ActivityLog> activityLogs) {
        this.activityLogs = activityLogs;
    }
}
