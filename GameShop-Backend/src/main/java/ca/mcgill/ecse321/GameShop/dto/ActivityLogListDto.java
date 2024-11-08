package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class ActivityLogListDto {
    private List<ActivityLogResponseDto> logs;

    public ActivityLogListDto() {
    }

    public ActivityLogListDto(List<ActivityLogResponseDto> logs) {
        this.logs = logs;
    }

    public List<ActivityLogResponseDto> getLogs() {
        return logs;
    }

    public void setLogs(List<ActivityLogResponseDto> logs) {
        this.logs = logs;
    }

}
