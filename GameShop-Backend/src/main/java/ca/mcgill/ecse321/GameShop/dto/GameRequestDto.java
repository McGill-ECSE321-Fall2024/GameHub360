package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ca.mcgill.ecse321.GameShop.model.GameRequest;
import java.util.List;

/**
 * Data Transfer Object for GameRequest entity.
 * This class is used to transfer data between the client and server.
 */
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

    /**
     * Default constructor for GameRequestDto.
     */
    public GameRequestDto() {
    }

    /**
     * Constructs a GameRequestDto with specified details.
     *
     * @param name            the name of the game
     * @param description     the description of the game
     * @param imageUrl        the image URL of the game
     * @param price           the price of the game
     * @param quantityInStock the quantity in stock
     */
    public GameRequestDto(String name, String description, String imageUrl, Double price, Integer quantityInStock) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    /**
     * Gets the ID of the game request.
     *
     * @return the ID of the game request
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the game request.
     *
     * @param id the ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name of the game.
     *
     * @return the name of the game
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the game.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the game.
     *
     * @return the description of the game
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the game.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the image URL of the game.
     *
     * @return the image URL of the game
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL of the game.
     *
     * @param imageUrl the image URL to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the price of the game.
     *
     * @return the price of the game
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price of the game.
     *
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Gets the quantity in stock.
     *
     * @return the quantity in stock
     */
    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Sets the quantity in stock.
     *
     * @param quantityInStock the quantity in stock to set
     */
    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    /**
     * Gets the staff ID associated with the game request.
     *
     * @return the staff ID
     */
    public Integer getStaffId() {
        return staffId;
    }

    /**
     * Sets the staff ID associated with the game request.
     *
     * @param staffId the staff ID to set
     */
    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    /**
     * Gets the request status of the game request.
     *
     * @return the request status
     */
    public GameRequest.RequestStatus getRequestStatus() {
        return requestStatus;
    }

    /**
     * Sets the request status of the game request.
     *
     * @param requestStatus the request status to set
     */
    public void setRequestStatus(GameRequest.RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    /**
     * Gets the category ID of the game.
     *
     * @return the category ID
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category ID of the game.
     *
     * @param categoryId the category ID to set
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets the list of notes associated with the game request.
     *
     * @return the list of notes
     */
    public List<RequestNoteDto> getNotes() {
        return notes;
    }

    /**
     * Sets the list of notes associated with the game request.
     *
     * @param notes the list of notes to set
     */
    public void setNotes(List<RequestNoteDto> notes) {
        this.notes = notes;
    }

    /**
     * Gets the request date of the game request.
     *
     * @return the request date
     */
    public String getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the request date of the game request.
     *
     * @param requestDate the request date to set
     */
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}
