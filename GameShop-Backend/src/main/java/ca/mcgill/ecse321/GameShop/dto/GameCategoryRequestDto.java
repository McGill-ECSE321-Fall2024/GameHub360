package ca.mcgill.ecse321.GameShop.dto;
import jakarta.validation.constraints.NotBlank;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.GameCategory.CategoryType;
import java.util.List;

public class GameCategoryRequestDto {

    // Attributes
    @NotBlank(message = "Name cannot be empty.")
    private String name;
    private CategoryType categoryType;
    private boolean available;
    private List<Integer> gameIds;
    private List<Integer> promotionIds;

    // Constructors
    public GameCategoryRequestDto() {}

    public GameCategoryRequestDto (boolean available, String name) {
        this.name = name;
        this.available = available;
    }

    public GameCategoryRequestDto (String name, CategoryType categoryType, boolean available) {
        this.name = name;
        this.available = available;
        this.categoryType = categoryType;
    }

    public GameCategoryRequestDto (boolean available, String name, CategoryType categoryType, List<Integer> gameIds,
                                   List<Integer> promotionIds) {
        this.name = name;
        this.available = available;
        this.categoryType = categoryType;
        this.gameIds = gameIds;
        this.promotionIds = promotionIds;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<Integer> getGameIds() {
        return gameIds;
    }

    public void setGameIds(List<Integer> gameIds) {
        this.gameIds = gameIds;
    }

    public List<Integer> getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(List<Integer> promotionIds) {
        this.promotionIds = promotionIds;
    }
}
