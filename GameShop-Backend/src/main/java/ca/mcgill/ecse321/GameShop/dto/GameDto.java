package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.Game;

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

    // Constructor for create/update operations
    public GameDto(String name, String description, String imageUrl, int quantityInStock, boolean isAvailable,
            double price) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.quantityInStock = quantityInStock;
        this.isAvailable = isAvailable;
        this.price = price;
        this.wishLists = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.promotions = new ArrayList<>();
    }

    // Default constructor
    public GameDto() {
        this.wishLists = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.promotions = new ArrayList<>();
    }

    // Constructor that takes a Game object as a parameter
    public GameDto(Game game) {
        this.id = game.getGameEntityId();
        this.name = game.getName();
        this.description = game.getDescription();
        this.imageUrl = game.getImageURL();
        this.quantityInStock = game.getQuantityInStock();
        this.isAvailable = game.getIsAvailable();
        this.price = game.getPrice();
        this.wishLists = new ArrayList<>(game.getWishLists());
        this.orders = new ArrayList<>(game.getOrders());
        this.promotions = new ArrayList<>(game.getPromotions());
    }

    // Getters and setters
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

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<CustomerAccount> getWishLists() {
        return Collections.unmodifiableList(wishLists);
    }

    public void setWishLists(List<CustomerAccount> wishLists) {
        this.wishLists = wishLists;
    }

    public List<OrderGame> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public void setOrders(List<OrderGame> orders) {
        this.orders = orders;
    }

    public List<Promotion> getPromotions() {
        return Collections.unmodifiableList(promotions);
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }
}