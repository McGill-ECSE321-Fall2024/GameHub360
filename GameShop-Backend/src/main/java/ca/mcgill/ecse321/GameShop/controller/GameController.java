package ca.mcgill.ecse321.GameShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.GameShop.dto.GameListDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.service.GameService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * Creates a new game.
     *
     * @param gameRequestDto the game data transfer object containing game details
     * @return the created game as a GameResponseDto
     */
    @PostMapping("/")
    public GameResponseDto createGame(
            @Validated(ValidationGroups.Post.class) @RequestBody GameRequestDto gameRequestDto) {
        Game createdGame = gameService.createGame(gameRequestDto);
        return new GameResponseDto(createdGame);
    }

    /**
     * Updates an existing game.
     *
     * @param gameId  the ID of the game to update
     * @param gameDto the game data transfer object containing updated game details
     * @return the updated game as a GameResponseDto
     */
    @PutMapping("/{gameId}")
    public GameResponseDto updateGame(
            @PathVariable("gameId") Integer gameId,
            @Validated(ValidationGroups.Update.class) @RequestBody GameRequestDto gameDto) {
        Game updatedGame = gameService.updateGame(gameId, gameDto);
        return new GameResponseDto(updatedGame);
    }

    /**
     * Archives a game.
     *
     * @param gameId the ID of the game to archive
     * @return the archived game as a GameResponseDto
     */
    @PutMapping("/archive/{gameId}")
    public GameResponseDto archiveGame(@PathVariable Integer gameId) {
        Game archivedGame = gameService.archiveGame(gameId);
        return new GameResponseDto(archivedGame);
    }

    /**
     * Retrieves all archived games.
     *
     * @return a list of archived games as GameResponseDto objects
     */
    @GetMapping("/archive")
    public GameListDto viewArchivedGames() {
        List<Game> archivedGames = gameService.viewArchivedGames();
        List<GameResponseDto> responseDtos = archivedGames.stream()
                .map(GameResponseDto::new)
                .toList();
        return new GameListDto(responseDtos);
    }

    /**
     * Reactivates an archived game.
     *
     * @param gameId the ID of the game to reactivate
     * @return the reactivated game as a GameResponseDto
     */
    @PutMapping("/archive/{gameId}/reactivate")
    public GameResponseDto reactivateArchivedGame(@PathVariable Integer gameId) {
        Game reactivatedGame = gameService.reactivateArchivedGame(gameId);
        return new GameResponseDto(reactivatedGame);
    }

    /**
     * Browses all available games with optional filters.
     *
     * @param category the category to filter by (optional)
     * @param minPrice the minimum price to filter by (optional)
     * @param maxPrice the maximum price to filter by (optional)
     * @return a GameListDto containing available games
     */
    @GetMapping
    public GameListDto browseGames(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<Game> games = gameService.browseGames(category, minPrice, maxPrice);
        List<GameResponseDto> responseDtos = games.stream()
                .map(GameResponseDto::new)
                .toList();
        return new GameListDto(responseDtos);
    }

    /**
     * Searches for games based on a query and optional filters.
     *
     * @param query    the search query
     * @param category the category to filter by (optional)
     * @param minPrice the minimum price to filter by (optional)
     * @param maxPrice the maximum price to filter by (optional)
     * @return a GameListDto containing matching games
     */
    @GetMapping("/search")
    public GameListDto searchGames(
            @RequestParam String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<Game> games = gameService.searchGames(query, category, minPrice, maxPrice);
        List<GameResponseDto> responseDtos = games.stream()
                .map(GameResponseDto::new)
                .toList();
        return new GameListDto(responseDtos);
    }

    /**
     * Adds a game to a category.
     *
     * @param gameId     the ID of the game
     * @param categoryId the ID of the category
     * @return the updated game as a GameResponseDto
     */
    @PutMapping("/{gameId}/categories/{categoryId}")
    public GameResponseDto addGameToCategory(
            @PathVariable Integer gameId,
            @PathVariable Integer categoryId) {
        Game updatedGame = gameService.addGameToCategory(gameId, categoryId);
        return new GameResponseDto(updatedGame);
    }

    /**
     * Updates the stock of a game.
     *
     * @param gameId the ID of the game
     * @param stock  the new stock quantity
     * @return the updated game as a GameResponseDto
     */
    @PutMapping("/{gameId}/stock")
    public GameResponseDto updateGameStock(
            @PathVariable Integer gameId,
            @RequestParam Integer stock) {
        Game updatedGame = gameService.updateGameStock(gameId, stock);
        return new GameResponseDto(updatedGame);
    }

    /**
     * Updates the price of a game.
     *
     * @param gameId the ID of the game
     * @param price  the new price
     * @return the updated game as a GameResponseDto
     */
    @PutMapping("/{gameId}/price")
    public GameResponseDto updateGamePrice(
            @PathVariable Integer gameId,
            @RequestParam Double price) {
        Game updatedGame = gameService.updateGamePrice(gameId, price);
        return new GameResponseDto(updatedGame);
    }
}