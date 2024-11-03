package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ca.mcgill.ecse321.GameShop.model.RequestNote;

public class RequestNoteDto {
    private Integer id;

    @NotBlank(message = "Content is required.")
    private String content;

    private String noteDate;

    @NotNull(message = "Staff writer ID is required.")
    private Integer staffWriterId;

    private Integer gameRequestId;

    // Constructors
    public RequestNoteDto() {
    }

    public RequestNoteDto(String content, Integer staffWriterId) {
        this.content = content;
        this.staffWriterId = staffWriterId;
    }

    // Constructor from entity
    public RequestNoteDto(RequestNote note) {
        this.id = note.getNoteId();
        this.content = note.getContent();
        this.noteDate = note.getNoteDate().toString();
        this.staffWriterId = note.getNotesWriter().getStaffId();
        this.gameRequestId = note.getGameRequest().getGameEntityId();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
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