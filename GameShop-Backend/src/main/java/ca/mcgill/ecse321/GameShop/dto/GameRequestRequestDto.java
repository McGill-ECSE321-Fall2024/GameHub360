package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GameRequestRequestDto {

    // Attributes
    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotBlank(message = "Image URL is required.")
    private String imageUrl;

    @NotNull(message = "Request date is required.")
    private Date requestDate;

    @NotNull(message = "Staff ID is required.")
    private Integer staffId;

    @NotNull(message = "Category IDs are required.")
    private List<Integer> categoryIds;

    // Constructors
    public GameRequestRequestDto() {
    }

    public GameRequestRequestDto(String name, String description, String imageUrl, Date requestDate, Integer staffId,
            List<Integer> categoryIds) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.requestDate = requestDate;
        this.staffId = staffId;
        this.categoryIds = categoryIds;
    }

    // Getters and Setters
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

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}