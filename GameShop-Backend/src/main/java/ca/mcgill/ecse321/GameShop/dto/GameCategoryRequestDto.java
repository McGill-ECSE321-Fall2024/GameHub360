package ca.mcgill.ecse321.GameShop.dto;
import jakarta.validation.constraints.NotBlank;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.GameCategory.CategoryType;
import java.util.List;

public class GameCategoryRequestDto {
    @NotBlank(message = "Name cannot be empty.")
    private String name;
    private CategoryType categoryType;
    boolean isAvailable;
    private List<Integer> gameIds;
    private List<Integer> promotionIds;
    public GameCategoryRequestDto() {}

    public GameCategoryRequestDto (boolean isAvailable, String name) {
        this.name = name;
        this.isAvailable = isAvailable;
    }

    public GameCategoryRequestDto (String name, CategoryType categoryType, boolean isAvailable) {
        this.name = name;
        this.isAvailable = isAvailable;
        this.categoryType = categoryType;
    }

    public GameCategoryRequestDto (boolean isAvailable, String name, CategoryType categoryType, List<Integer> gameIds,
                                   List<Integer> promotionIds) {
        this.name = name;
        this.isAvailable = isAvailable;
        this.categoryType = categoryType;
        this.gameIds = gameIds;
        this.promotionIds = promotionIds;
    }

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
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        isAvailable = available;
    }

}