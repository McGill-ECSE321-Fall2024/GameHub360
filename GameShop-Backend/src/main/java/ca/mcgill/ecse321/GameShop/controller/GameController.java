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

@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * Create a new game directly
     */
    @PostMapping
    public ResponseEntity<GameDto> createGame(
            @Validated(ValidationGroups.Post.class) @RequestBody GameRequestDto gameDto) {
        return ResponseEntity.ok(gameService.createGame(gameDto));
    }

    /**
     * Update an existing game
     */
    @PutMapping("/{gameId}")
    public ResponseEntity<GameDto> updateGame(
            @PathVariable("gameId") Integer gameId,
            @Validated(ValidationGroups.Update.class) @RequestBody GameDto gameDto) {
        return ResponseEntity.ok(gameService.updateGame(gameId, gameDto));
    }

    /**
     * Archive a game
     */
    @PutMapping("/archive/{gameId}")
    public ResponseEntity<GameDto> archiveGame(@PathVariable("gameId") Integer gameId) {
        return ResponseEntity.ok(gameService.archiveGame(gameId));
    }

    /**
     * View all archived games
     */
    @GetMapping("/archive")
    public ResponseEntity<List<GameDto>> viewArchivedGames() {
        return ResponseEntity.ok(gameService.viewArchivedGames());
    }

    /**
     * Reactivate an archived game
     */
    @PutMapping("/archive/{gameId}/reactivate")
    public ResponseEntity<GameDto> reactivateArchivedGame(@PathVariable("gameId") Integer gameId) {
        return ResponseEntity.ok(gameService.reactivateArchivedGame(gameId));
    }

    /**
     * Browse all available games
     */
    @GetMapping
    public ResponseEntity<List<GameDto>> browseGames(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(gameService.browseGames(category, minPrice, maxPrice));
    }

    /**
     * Search games
     */
    @GetMapping("/search")
    public ResponseEntity<List<GameDto>> searchGames(
            @RequestParam String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(gameService.searchGames(query, category, minPrice, maxPrice));
    }

    /**
     * Add game to category
     */
    @PutMapping("/{gameId}/categories/{categoryId}")
    public ResponseEntity<GameDto> addGameToCategory(
            @PathVariable("gameId") Integer gameId,
            @PathVariable("categoryId") Integer categoryId) {
        return ResponseEntity.ok(gameService.addGameToCategory(gameId, categoryId));
    }

    /**
     * Update game stock
     */
    @PutMapping("/{gameId}/stock")
    public ResponseEntity<GameDto> updateGameStock(
            @PathVariable("gameId") Integer gameId,
            @RequestParam("stock") Integer stock) {
        return ResponseEntity.ok(gameService.updateGameStock(gameId, stock));
    }

    /**
     * Update game price
     */
    @PutMapping("/{gameId}/price")
    public ResponseEntity<GameDto> updateGamePrice(
            @PathVariable("gameId") Integer gameId,
            @RequestParam("price") Double price) {
        return ResponseEntity.ok(gameService.updateGamePrice(gameId, price));
    }
}