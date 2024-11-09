package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.GameDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing games in the Game Shop.
 */
@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * Creates a new game.
     *
     * @param gameDto the game data transfer object containing game details
     * @return the created game as a GameDto wrapped in a ResponseEntity
     */
    @PostMapping
    public ResponseEntity<GameDto> createGame(
            @Validated(ValidationGroups.Post.class) @RequestBody GameRequestDto gameDto) {
        GameDto createdGame = gameService.createGame(gameDto);
        return ResponseEntity.ok(createdGame);
    }

    /**
     * Updates an existing game.
     *
     * @param gameId  the ID of the game to update
     * @param gameDto the game data transfer object containing updated game details
     * @return the updated game as a GameDto wrapped in a ResponseEntity
     */
    @PutMapping("/{gameId}")
    public ResponseEntity<GameDto> updateGame(
            @PathVariable Integer gameId,
            @Validated(ValidationGroups.Update.class) @RequestBody GameDto gameDto) {
        GameDto updatedGame = gameService.updateGame(gameId, gameDto);
        return ResponseEntity.ok(updatedGame);
    }

    /**
     * Archives a game.
     *
     * @param gameId the ID of the game to archive
     * @return the archived game as a GameDto wrapped in a ResponseEntity
     */
    @PutMapping("/archive/{gameId}")
    public ResponseEntity<GameDto> archiveGame(@PathVariable Integer gameId) {
        GameDto archivedGame = gameService.archiveGame(gameId);
        return ResponseEntity.ok(archivedGame);
    }

    /**
     * Retrieves all archived games.
     *
     * @return a list of archived games as GameDto objects wrapped in a ResponseEntity
     */
    @GetMapping("/archive")
    public ResponseEntity<List<GameDto>> viewArchivedGames() {
        List<GameDto> archivedGames = gameService.viewArchivedGames();
        return ResponseEntity.ok(archivedGames);
    }

    /**
     * Reactivates an archived game.
     *
     * @param gameId the ID of the game to reactivate
     * @return the reactivated game as a GameDto wrapped in a ResponseEntity
     */
    @PutMapping("/archive/{gameId}/reactivate")
    public ResponseEntity<GameDto> reactivateArchivedGame(@PathVariable Integer gameId) {
        GameDto reactivatedGame = gameService.reactivateArchivedGame(gameId);
        return ResponseEntity.ok(reactivatedGame);
    }

    /**
     * Browses all available games with optional filters.
     *
     * @param category the category to filter by (optional)
     * @param minPrice the minimum price to filter by (optional)
     * @param maxPrice the maximum price to filter by (optional)
     * @return a list of available games as GameDto objects wrapped in a ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<GameDto>> browseGames(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<GameDto> games = gameService.browseGames(category, minPrice, maxPrice);
        return ResponseEntity.ok(games);
    }

    /**
     * Searches for games based on a query and optional filters.
     *
     * @param query    the search query
     * @param category the category to filter by (optional)
     * @param minPrice the minimum price to filter by (optional)
     * @param maxPrice the maximum price to filter by (optional)
     * @return a list of games matching the search criteria as GameDto objects wrapped in a ResponseEntity
     */
    @GetMapping("/search")
    public ResponseEntity<List<GameDto>> searchGames(
            @RequestParam String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<GameDto> games = gameService.searchGames(query, category, minPrice, maxPrice);
        return ResponseEntity.ok(games);
    }

    /**
     * Adds a game to a category.
     *
     * @param gameId    the ID of the game
     * @param categoryId the ID of the category
     * @return the updated game as a GameDto wrapped in a ResponseEntity
     */
    @PutMapping("/{gameId}/categories/{categoryId}")
    public ResponseEntity<GameDto> addGameToCategory(
            @PathVariable Integer gameId,
            @PathVariable Integer categoryId) {
        GameDto updatedGame = gameService.addGameToCategory(gameId, categoryId);
        return ResponseEntity.ok(updatedGame);
    }

    /**
     * Updates the stock of a game.
     *
     * @param gameId the ID of the game
     * @param stock  the new stock quantity
     * @return the updated game as a GameDto wrapped in a ResponseEntity
     */
    @PutMapping("/{gameId}/stock")
    public ResponseEntity<GameDto> updateGameStock(
            @PathVariable Integer gameId,
            @RequestParam Integer stock) {
        GameDto updatedGame = gameService.updateGameStock(gameId, stock);
        return ResponseEntity.ok(updatedGame);
    }

    /**
     * Updates the price of a game.
     *
     * @param gameId the ID of the game
     * @param price  the new price
     * @return the updated game as a GameDto wrapped in a ResponseEntity
     */
    @PutMapping("/{gameId}/price")
    public ResponseEntity<GameDto> updateGamePrice(
            @PathVariable Integer gameId,
            @RequestParam Double price) {
        GameDto updatedGame = gameService.updateGamePrice(gameId, price);
        return ResponseEntity.ok(updatedGame);
    }
}