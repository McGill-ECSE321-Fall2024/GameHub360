package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.ActivityLog;

public class ActivityLogResponseDto {

    // Attributes
    private Integer logId;
    private String content;
    private Integer employeeId;

    // Constructors
    public ActivityLogResponseDto() {
    }

    public ActivityLogResponseDto(Integer logId, String content, Integer employeeId) {
        this.logId = logId;
        this.content = content;
        this.employeeId = employeeId;
    }

    public ActivityLogResponseDto(ActivityLog log) {
        this(log.getLogId(), log.getContent(), log.getEmployee().getStaffId());
    }

    // Getters and Setters
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

}
