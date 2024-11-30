package ca.mcgill.ecse321.GameShop.dto;

import java.util.ArrayList;
import java.util.List;

public class GameRequestsListDto {
    // Attributes
    private List<GameRequestResponseDto> gameRequests;

    // Constructors
    public GameRequestsListDto() {
        this.gameRequests = new ArrayList<>();
    }

    public GameRequestsListDto(List<GameRequestResponseDto> gameRequests) {
        this.gameRequests = gameRequests;
    }

    // Getters and Setters
    public List<GameRequestResponseDto> getGameRequests() {
        return gameRequests;
    }

    public void setGameRequests(List<GameRequestResponseDto> gameRequests) {
        this.gameRequests = gameRequests;
    }
}
