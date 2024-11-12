package ca.mcgill.ecse321.GameShop.dto;
import ca.mcgill.ecse321.GameShop.model.GameCategory;

public class GameCategoryResponseDto {

    private int categoryId;
    private String name;
    private boolean isAvailable;
    private CategoryType categoryType;

    public GameCategoryResponseDto() {}

    public GameCategoryResponseDto(GameCategory gameCategory) {
        this.categoryId = gameCategory.getCategoryId();
        this.name = gameCategory.getName();
        this.categoryType = gameCategory.getCategoryType();
        this.isAvailable = gameCategory.getIsAvailable();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}