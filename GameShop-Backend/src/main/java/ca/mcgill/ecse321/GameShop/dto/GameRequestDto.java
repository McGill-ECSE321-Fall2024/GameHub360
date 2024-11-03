package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GameRequestDto {

    @NotBlank(message = "Name is required.", groups = ValidationGroups.Post.class)
    private String name;

    @NotNull(message = "Price is required.", groups = ValidationGroups.Post.class)
    private Double price;

    private int quantityInStock;
    private String description;
    private String imageUrl;
    private Integer categoryId;

    // Constructors
    public GameRequestDto() {
    }

    public GameRequestDto(String name, Double price, int quantityInStock, String description, String imageUrl,
            Integer categoryId) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public GameRequestDto(String name, Double price, int quantityInStock) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    // Getters and Setters
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
}
