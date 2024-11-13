package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;
import ca.mcgill.ecse321.GameShop.model.RequestNote;

public class RequestNoteResponseDto {

    // Attributes
    private Integer noteId;
    private String content;
    private Date noteDate;
    private Integer staffWriterId;
    private Integer gameRequestId;

    // Constructors 
    public RequestNoteResponseDto() {
    }

    // Constructor accepting a RequestNote object
    public RequestNoteResponseDto(RequestNote requestNote) {
        this.noteId = requestNote.getNoteId();
        this.content = requestNote.getContent();
        this.noteDate = requestNote.getNoteDate();
        this.staffWriterId = requestNote.getNotesWriter() != null ? requestNote.getNotesWriter().getStaffId() : null;
        this.gameRequestId = requestNote.getGameRequest() != null ? requestNote.getGameRequest().getGameEntityId() : null;
    }

    // Getters and Setters
    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

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