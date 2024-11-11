package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class WishlistDto {
    // Attributes
    private List<GameResponseDto> games;
    private int totalGames;

    // Constructors
    public WishlistDto() {}

    public WishlistDto(List<GameResponseDto> games) {
        this.games = games;
        this.totalGames = games.size();
    }

    // Getters and Setters
    public List<GameResponseDto> getGames() {
        return games;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setGames(List<GameResponseDto> games) {
        this.games = games;
    }
}

