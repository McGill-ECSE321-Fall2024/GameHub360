package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.Game;

/**
 * Data Transfer Object for Game entity.
 */
public class GameDto {

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private int quantityInStock;
    private boolean isAvailable;
    private double price;
    private List<CustomerAccount> wishLists;
    private List<OrderGame> orders;
    private List<Promotion> promotions;
    private Integer categoryId;

    /**
     * Constructs a GameDto for create/update operations.
     *
     * @param name            the name of the game
     * @param description     the description of the game
     * @param imageUrl        the image URL of the game
     * @param quantityInStock the quantity in stock
     * @param isAvailable     the availability status
     * @param price           the price of the game
     * @param categoryId      the category ID
     */
    public GameDto(String name, String description, String imageUrl, int quantityInStock, boolean isAvailable,
                   double price, Integer categoryId) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.quantityInStock = quantityInStock;
        this.isAvailable = isAvailable;
        this.price = price;
        this.categoryId = categoryId;
        this.wishLists = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.promotions = new ArrayList<>();
    }

    /**
     * Default constructor.
     */
    public GameDto() {
        this.wishLists = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.promotions = new ArrayList<>();
        this.categoryId = null;
        this.isAvailable = true;
    }

    /**
     * Constructs a GameDto from a Game object.
     *
     * @param game the Game object
     */
    public GameDto(Game game) {
        this.id = game.getGameEntityId();
        this.name = game.getName();
        this.description = game.getDescription();
        this.imageUrl = game.getImageURL();
        this.quantityInStock = game.getQuantityInStock();
        this.isAvailable = game.getIsAvailable();
        this.price = game.getPrice();
        this.wishLists = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.promotions = new ArrayList<>();
        if (!game.getCategories().isEmpty()) {
            this.categoryId = game.getCategories().get(0).getCategoryId();
        }
    }

    // Getters and Setters

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
     * Gets the availability status of the game.
     *
     * @return true if available, false otherwise
     */
    public boolean getIsAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the game.
     *
     * @param isAvailable the availability status
     */
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * Gets the price of the game.
     *
     * @return the game price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the game.
     *
     * @param price the game price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the wish lists containing the game.
     *
     * @return an unmodifiable list of wish lists
     */
    public List<CustomerAccount> getWishLists() {
        return Collections.unmodifiableList(wishLists);
    }

    /**
     * Sets the wish lists containing the game.
     *
     * @param wishLists the list of wish lists
     */
    public void setWishLists(List<CustomerAccount> wishLists) {
        this.wishLists = wishLists;
    }

    /**
     * Gets the orders containing the game.
     *
     * @return an unmodifiable list of orders
     */
    public List<OrderGame> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    /**
     * Sets the orders containing the game.
     *
     * @param orders the list of orders
     */
    public void setOrders(List<OrderGame> orders) {
        this.orders = orders;
    }

    /**
     * Gets the promotions associated with the game.
     *
     * @return an unmodifiable list of promotions
     */
    public List<Promotion> getPromotions() {
        return Collections.unmodifiableList(promotions);
    }

    /**
     * Sets the promotions associated with the game.
     *
     * @param promotions the list of promotions
     */
    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
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
     * @param categoryId the category ID
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
