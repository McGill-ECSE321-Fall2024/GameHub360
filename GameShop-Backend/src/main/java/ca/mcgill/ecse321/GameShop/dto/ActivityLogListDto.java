package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class ActivityLogListDto {
    // Attributes
    private List<ActivityLogResponseDto> logs;

    // Constructors
    public ActivityLogListDto() {
    }

    public ActivityLogListDto(List<ActivityLogResponseDto> logs) {
        this.logs = logs;
    }
    
    // Getters and Setters
    public List<ActivityLogResponseDto> getLogs() {
        return logs;
    }

    public void setLogs(List<ActivityLogResponseDto> logs) {
        this.logs = logs;
    }

}
