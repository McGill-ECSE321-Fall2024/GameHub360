package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class GameCategoryListDto {
    // Attributes
    private List<GameCategoryResponseDto> gameCategories;

    // Constructors
    public GameCategoryListDto() {
    }

    public GameCategoryListDto(List<GameCategoryResponseDto> gameCategories) {
        this.gameCategories = gameCategories;
    }

    // Getters and Setters
    public List<GameCategoryResponseDto> getGameCategories() {
        return gameCategories;
    }

    public void setGameCategories(List<GameCategoryResponseDto> gameCategories) {
        this.gameCategories = gameCategories;
    }
}
