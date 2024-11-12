package ca.mcgill.ecse321.GameShop.dto;
import jakarta.validation.constraints.NotBlank;
import ca.mcgill.ecse321.GameShop.model.GameCategory;

public class GameCategoryRequestDto {
    @NotBlank(message = "Name cannot be empty.")
    private String name;
    private CategoryType categoryType;
    boolean isAvailable;

    public GameCategoryRequestDto() {}

    public GameCategoryRequestDto (boolean isAvailable, String name) {
        this.name = name;
        this.isAvailable = isAvailable;
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