package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * Endpoint to submit a new game request.
     *
     * @param gameRequestDto The request object containing game details.
     * @return A response containing the submitted game's details.
     */
    @PostMapping("/request")
    public GameResponseDto submitGameRequest(
            @Validated({ ValidationGroups.Post.class }) @RequestBody GameRequestDto gameRequestDto) {
        return gameService.submitGameRequest(gameRequestDto);
    }

    /**
     * Endpoint to approve or reject a game request.
     *
     * @param requestId The ID of the game request.
     * @param approval  Boolean indicating approval or rejection.
     * @return A response containing the updated game request details.
     */
    @PutMapping("/request/{requestId}/approval")
    public GameResponseDto approveOrRejectGameRequest(
            @PathVariable Integer requestId,
            @RequestParam boolean approval) {
        System.out.println("Approval: " + approval);
        return gameService.approveOrRejectGameRequest(requestId, approval);
    }

    /**
     * Endpoint to create a new game directly.
     *
     * @param gameRequestDto The request object containing game details.
     * @return A response containing the newly created game's details.
     */
    @PostMapping
    public GameResponseDto createGame(
            @Validated({ ValidationGroups.Post.class }) @RequestBody GameRequestDto gameRequestDto) {
        return gameService.createGame(gameRequestDto);
    }

    /**
     * Endpoint to update an existing game.
     *
     * @param gameId         The ID of the game to update.
     * @param gameRequestDto The request object containing updated game details.
     * @return A response containing the updated game's details.
     */
    @PutMapping("/{gameId}")
    public GameResponseDto updateGame(
            @PathVariable Integer gameId,
            @Validated({ ValidationGroups.Update.class }) @RequestBody GameRequestDto gameRequestDto) {
        return gameService.updateGame(gameId, gameRequestDto);
    }

    /**
     * Endpoint to archive a game.
     *
     * @param gameId The ID of the game to archive.
     * @return A response containing the archived game's details.
     */
    @PutMapping("/archive/{gameId}")
    public GameResponseDto deleteGame(@PathVariable Integer gameId) {
        return gameService.archiveGame(gameId);
    }

    /**
     * Endpoint to view all archived games.
     *
     * @return A list of archived games.
     */
    @GetMapping("/archive")
    public List<GameResponseDto> viewArchivedGames() {
        return gameService.viewArchivedGames();
    }

    /**
     * Endpoint to reactivate an archived game.
     *
     * @param gameId The ID of the game to reactivate.
     * @return A response containing the reactivated game's details.
     */
    @PutMapping("/archive/{gameId}/reactivate")
    public GameResponseDto reactivateArchivedGame(@PathVariable Integer gameId) {
        return gameService.reactivateArchivedGame(gameId);
    }

    /**
     * Endpoint to browse all available games.
     *
     * @return A list of available games.
     */
    @GetMapping
    public List<GameResponseDto> browseGames() {
        return gameService.browseGames();
    }

    /**
     * Endpoint to search for games by a query.
     *
     * @param query The search query.
     * @return A list of games matching the search query.
     */
    @GetMapping("/search")
    public List<GameResponseDto> searchGames(@RequestParam String query) {
        return gameService.searchGames(query);
    }

    /**
     * Endpoint to add a game to a category.
     *
     * @param gameId     The ID of the game.
     * @param categoryId The ID of the category.
     * @return A response containing the updated game's details.
     */
    @PutMapping("/{gameId}/categories/{categoryId}")
    public GameResponseDto addGameToCategory(@PathVariable Integer gameId, @PathVariable Integer categoryId) {
        return gameService.addGameToCategory(gameId, categoryId);
    }

    /**
     * Endpoint to update the stock of a game.
     *
     * @param gameId The ID of the game.
     * @param stock  The new stock quantity.
     * @return A response containing the updated game's details.
     */
    @PutMapping("/{gameId}/stock")
    public GameResponseDto updateGameStock(@PathVariable Integer gameId, @RequestParam int stock) {
        return gameService.updateGameStock(gameId, stock);
    }

    /**
     * Endpoint to update the price of a game.
     *
     * @param gameId The ID of the game.
     * @param price  The new price.
     * @return A response containing the updated game's details.
     */
    @PutMapping("/{gameId}/price")
    public GameResponseDto updateGamePrice(@PathVariable Integer gameId, @RequestParam double price) {
        return gameService.updateGamePrice(gameId, price);
    }
}
