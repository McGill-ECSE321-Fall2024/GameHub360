package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;
import java.util.stream.Collectors;
import ca.mcgill.ecse321.GameShop.model.Game;

/**
 * Data Transfer Object for Game entity.
 * This class is used to transfer game data between the client and server.
 */
public class GameResponseDto {

    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private int quantityInStock;
    private boolean isAvailable;
    private double price;
    private List<Integer> categoryId;
    private List<Integer> promotionId;

    /**
     * Constructs a GameResponseDto from a Game entity.
     *
     * @param game the Game entity
     */
    public GameResponseDto(Game game) {
        this.id = game.getGameEntityId();
        this.name = game.getName();
        this.description = game.getDescription();
        this.imageUrl = game.getImageURL();
        this.quantityInStock = game.getQuantityInStock();
        this.isAvailable = game.getIsAvailable();
        this.price = game.getPrice();
        this.categoryId = game.getCategories().stream()
                .map(category -> category.getCategoryId())
                .collect(Collectors.toList());
        this.promotionId = game.getPromotions().stream()
                .map(promotion -> promotion.getPromotionId())
                .collect(Collectors.toList());
    }

    /**
     * Default constructor for GameResponseDto.
     */
    public GameResponseDto() {
    }

    /**
     * Gets the ID of the game.
     *
     * @return the game ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the game.
     *
     * @param id the game ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name of the game.
     *
     * @return the game name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the game.
     *
     * @param name the game name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the game.
     *
     * @return the game price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price of the game.
     *
     * @param price the game price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Gets the quantity in stock.
     *
     * @return the quantity in stock
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Sets the quantity in stock.
     *
     * @param quantityInStock the quantity in stock
     */
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    /**
     * Checks if the game is available.
     *
     * @return true if available, false otherwise
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the game.
     *
     * @param available the availability status
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Gets the description of the game.
     *
     * @return the game description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the game.
     *
     * @param description the game description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the image URL of the game.
     *
     * @return the image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL of the game.
     *
     * @param imageUrl the image URL
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the category IDs associated with the game.
     *
     * @return a list of category IDs
     */
    public List<Integer> getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category IDs associated with the game.
     *
     * @param categoryId a list of category IDs
     */
    public void setCategoryId(List<Integer> categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets the promotion IDs associated with the game.
     *
     * @return a list of promotion IDs
     */
    public List<Integer> getPromotionId() {
        return promotionId;
    }

    /**
     * Sets the promotion IDs associated with the game.
     *
     * @param promotionId a list of promotion IDs
     */
    public void setPromotionId(List<Integer> promotionId) {
        this.promotionId = promotionId;
    }
}