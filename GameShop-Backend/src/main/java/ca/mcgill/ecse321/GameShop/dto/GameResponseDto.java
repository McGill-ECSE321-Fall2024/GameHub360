package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;
import java.util.stream.Collectors;
import ca.mcgill.ecse321.GameShop.model.Game;

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

    // Constructor from entity
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

    public List<Integer> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<Integer> categoryId) {
        this.categoryId = categoryId;
    }

    public List<Integer> getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(List<Integer> promotionId) {
        this.promotionId = promotionId;
    }
}
