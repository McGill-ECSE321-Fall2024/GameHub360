package ca.mcgill.ecse321.GameShop.dto;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.GameCategory.CategoryType;
import java.util.List;

public class GameCategoryResponseDto {

    private int categoryId;
    private String name;
    private boolean available;
    private CategoryType categoryType;
    private List<Integer> gameIds;
    private List<Integer> promotionIds;

    public GameCategoryResponseDto() {}

    public GameCategoryResponseDto(GameCategory gameCategory) {
        this.categoryId = gameCategory.getCategoryId();
        this.name = gameCategory.getName();
        this.categoryType = gameCategory.getCategoryType();
        this.available = gameCategory.getIsAvailable();
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
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getGameIds() {
        return gameIds;
    }

    public List<Integer> getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(List<Integer> promotionIds) {
        this.promotionIds = promotionIds;
    }

    public void setGameIds(List<Integer> gameIds) {
        this.gameIds = gameIds;
    }
}
