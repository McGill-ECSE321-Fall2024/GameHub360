package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class GameRequestDto {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotBlank(message = "Image URL is required.")
    private String imageURL;

    @Positive(message = "Quantity in stock must be positive.")
    private int quantityInStock;

    @NotNull(message = "Availability status is required.")
    private Boolean isAvailable;

    @Positive(message = "Price must be positive.")
    private double price;

    @NotNull(message = "Category IDs are required.")
    private List<Integer> categoryIds;


    // Constructor
    public GameRequestDto(){
    }

    public GameRequestDto(String name, String description, String imageURL, int quantityInStock, Boolean isAvailable, double price, List<Integer> categoryIds) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.quantityInStock = quantityInStock;
        this.isAvailable = isAvailable;
        this.price = price;
        this.categoryIds = categoryIds;
    }

    // Getters and Setters
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
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
}