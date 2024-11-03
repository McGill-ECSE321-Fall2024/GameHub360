package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Game;

public class GameResponseDto {

    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private int quantityInStock;
    private boolean isAvailable;
    private double price;

    // Constructor
    public GameResponseDto(Game game) {
        this.id = game.getGameEntityId();
        this.name = game.getName();
        this.price = game.getPrice();
        this.quantityInStock = game.getQuantityInStock();
        this.isAvailable = game.getIsAvailable();
        this.description = game.getDescription();
        this.imageUrl = game.getImageURL();
    }

    // Default constructor
    public GameResponseDto() {
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

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
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
}
