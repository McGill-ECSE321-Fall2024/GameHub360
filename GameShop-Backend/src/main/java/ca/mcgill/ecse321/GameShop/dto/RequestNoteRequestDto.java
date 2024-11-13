package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestNoteRequestDto {

    // Attributes
    @NotBlank(message = "Content is required.")
    private String content;

    @NotNull(message = "Note date is required.")
    private Date noteDate;

    @NotNull(message = "Staff writer ID is required.")
    private Integer staffWriterId;

    @NotNull(message = "Game request ID is required.")
    private Integer gameRequestId;

    // Constructors
    public RequestNoteRequestDto() {
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }

    public Integer getStaffWriterId() {
        return staffWriterId;
    }

    public void setStaffWriterId(Integer staffWriterId) {
        this.staffWriterId = staffWriterId;
    }

    public Integer getGameRequestId() {
        return gameRequestId;
    }

    public void setGameRequestId(Integer gameRequestId) {
        this.gameRequestId = gameRequestId;
    }
}