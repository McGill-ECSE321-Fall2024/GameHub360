package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ca.mcgill.ecse321.GameShop.model.GameRequest;
import java.util.List;

public class GameRequestDto {
    private Integer id;

    @NotBlank(message = "Name is required.", groups = ValidationGroups.Post.class)
    private String name;

    @NotBlank(message = "Description is required.", groups = ValidationGroups.Post.class)
    private String description;

    @NotBlank(message = "Image URL is required.", groups = ValidationGroups.Post.class)
    private String imageUrl;

    @NotNull(message = "Price is required.", groups = ValidationGroups.Post.class)
    @Positive(message = "Price must be positive.", groups = ValidationGroups.Post.class)
    private Double price;

    @NotNull(message = "Quantity in stock is required.", groups = ValidationGroups.Post.class)
    @Positive(message = "Quantity must be positive.", groups = ValidationGroups.Post.class)
    private Integer quantityInStock;

    private Integer staffId;

    private GameRequest.RequestStatus requestStatus;

    private Integer categoryId;

    private List<RequestNoteDto> notes;

    private String requestDate;

    // Constructors
    public GameRequestDto() {
    }

    public GameRequestDto(String name, String description, String imageUrl, Double price, Integer quantityInStock) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantityInStock = quantityInStock;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public GameRequest.RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(GameRequest.RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setNotes(List<RequestNoteDto> notes) {
        this.notes = notes;
    }

    public List<RequestNoteDto> getNotes() {
        return notes;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestDate() {
        return requestDate;
    }
}
