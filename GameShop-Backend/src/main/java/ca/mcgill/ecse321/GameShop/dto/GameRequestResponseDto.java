package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.GameShop.model.GameRequest;
import ca.mcgill.ecse321.GameShop.model.RequestNote;

public class GameRequestResponseDto {

    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private Date requestDate;
    private GameRequest.RequestStatus requestStatus;
    private Integer staffId;
    private List<Integer> noteIds;
    private List<Integer> categoryIds;

    // Constructors
    public GameRequestResponseDto() {
    }

    // Constructor accepting a GameRequest object
    public GameRequestResponseDto(GameRequest gameRequest) {
        this.id = gameRequest.getGameEntityId();
        this.name = gameRequest.getName();
        this.description = gameRequest.getDescription();
        this.imageUrl = gameRequest.getImageURL();
        this.requestDate = gameRequest.getRequestDate();
        this.requestStatus = gameRequest.getRequestStatus();
        this.staffId = gameRequest.getRequestPlacer() != null ? gameRequest.getRequestPlacer().getStaffId() : null;
        this.noteIds = gameRequest.getAssociatedNotes().stream()
                .map(RequestNote::getNoteId)
                .collect(Collectors.toList());
        this.categoryIds = gameRequest.getCategories().stream()
                .map(category -> category.getCategoryId())
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public GameRequest.RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(GameRequest.RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public List<Integer> getNoteIds() {
        return noteIds;
    }

    public void setNoteIds(List<Integer> noteIds) {
        this.noteIds = noteIds;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}