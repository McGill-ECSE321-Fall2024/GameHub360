package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.Promotion;

public class GameResponseDto {

    private int gameId;
    private String name;
    private String description;
    private String imageUrl;
    private int quantityInStock;
    private boolean isAvailable;
    private double price;
    private List<Integer> categoryIds;
    private List<Integer> wishListIds;
    private List<Integer> orderIds;
    private List<Integer> promotionIds;

    // Constructor
    public GameResponseDto() {
    }

    // Constructor to initialize from Game entity
    public GameResponseDto(Game game) {
        this.gameId = game.getGameEntityId();
        this.name = game.getName();
        this.description = game.getDescription();
        this.imageUrl = game.getImageURL();
        this.quantityInStock = game.getQuantityInStock();
        this.isAvailable = game.getIsAvailable();
        this.price = game.getPrice();
        this.categoryIds = game.getCategories().stream().map((GameCategory c) -> c.getCategoryId())
                .collect(Collectors.toList());
        this.wishListIds = game.getWishLists().stream().map((CustomerAccount w) -> w.getCustomerId())
                .collect(Collectors.toList());
        this.orderIds = game.getOrders().stream().map((OrderGame o) -> o.getCustomerOrder().getOrderId())
                .collect(Collectors.toList());
        this.promotionIds = game.getPromotions().stream().map((Promotion p) -> p.getPromotionId())
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Integer> getWishListIds() {
        return wishListIds;
    }

    public void setWishListIds(List<Integer> wishListIds) {
        this.wishListIds = wishListIds;
    }

    public List<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    public List<Integer> getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(List<Integer> promotionIds) {
        this.promotionIds = promotionIds;
    }
}