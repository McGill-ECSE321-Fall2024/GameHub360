package ca.mcgill.ecse321.GameShop.dto;

import java.util.ArrayList;
import java.util.List;

public class GameListDto {
    // Attributes
    private List<GameResponseDto> games;

    // Constructors
    public GameListDto() {
        this.games = new ArrayList<>();
    }

    public GameListDto(List<GameResponseDto> games) {
        this.games = games;
    }

    // Getters and Setters
    public List<GameResponseDto> getGames() {
        return games;
    }

    public void setGames(List<GameResponseDto> games) {
        this.games = games;
    }
}
