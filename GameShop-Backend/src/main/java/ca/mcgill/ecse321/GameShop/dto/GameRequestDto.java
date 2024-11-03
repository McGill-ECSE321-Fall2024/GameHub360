package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ca.mcgill.ecse321.GameShop.model.GameRequest;
import ca.mcgill.ecse321.GameShop.model.GameRequest.RequestStatus;

public class GameRequestDto {
    private Integer id;

    @NotBlank(message = "Name is required.", groups = ValidationGroups.Post.class)
    private String name;

    @NotNull(message = "Price is required.", groups = ValidationGroups.Post.class)
    @Positive(message = "Price must be positive.", groups = ValidationGroups.Post.class)
    private Double price;

    @NotNull(message = "Stock quantity is required.", groups = ValidationGroups.Post.class)
    @Positive(message = "Stock quantity must be positive.", groups = ValidationGroups.Post.class)
    private Integer quantityInStock;

    @NotBlank(message = "Description is required.", groups = ValidationGroups.Post.class)
    private String description;

    @NotBlank(message = "Image URL is required.", groups = ValidationGroups.Post.class)
    private String imageUrl;

    @NotNull(message = "Category ID is required.", groups = ValidationGroups.Post.class)
    private Integer categoryId;

    private RequestStatus requestStatus;
    private String requestDate;
    private Integer requestPlacerId;
    private List<RequestNoteDto> notes;

    // Constructors
    public GameRequestDto() {
    }

    public GameRequestDto(String name, Double price, Integer quantityInStock, String description,
            String imageUrl, Integer categoryId) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    // Constructor from entity
    public GameRequestDto(GameRequest request) {
        this.id = request.getGameEntityId();
        this.name = request.getName();
        this.description = request.getDescription();
        this.imageUrl = request.getImageURL();
        this.requestStatus = request.getRequestStatus();
        this.requestDate = request.getRequestDate().toString();
        this.requestPlacerId = request.getRequestPlacer().getStaffId();
        this.notes = request.getAssociatedNotes().stream()
                .map(RequestNoteDto::new)
                .toList();
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public Integer getRequestPlacerId() {
        return requestPlacerId;
    }

    public void setRequestPlacerId(Integer requestPlacerId) {
        this.requestPlacerId = requestPlacerId;
    }

    public List<RequestNoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<RequestNoteDto> notes) {
        this.notes = notes;
    }
}