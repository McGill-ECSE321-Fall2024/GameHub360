package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
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
    public ResponseEntity<GameResponseDto> createGame(
            @Validated(ValidationGroups.Post.class) @RequestBody GameRequestDto gameRequestDto) {
        return ResponseEntity.ok(gameService.createGame(gameRequestDto));
    }

    /**
     * Update an existing game
     */
    @PutMapping("/{gameId}")
    public ResponseEntity<GameResponseDto> updateGame(
            @PathVariable Integer gameId,
            @Validated(ValidationGroups.Update.class) @RequestBody GameRequestDto gameRequestDto) {
        return ResponseEntity.ok(gameService.updateGame(gameId, gameRequestDto));
    }

    /**
     * Archive a game
     * 
     * Mapping("/archive/{gameId}")
     * public ResponseEntity<GameResponseDto> archiveGame(@PathVariable Integer
     * gameId) {
     * return ResponseEntity.ok(gameService.archiveGame(gameId));
     * }
     * 
     * /**
     * View all archived games
     */
    @GetMapping("/archive")
    public ResponseEntity<List<GameResponseDto>> viewArchivedGames() {
        return ResponseEntity.ok(gameService.viewArchivedGames());
    }

    /**
     * Reactivate an archived game
     */
    @PutMapping("/archive/{gameId}/reactivate")
    public ResponseEntity<GameResponseDto> reactivateArchivedGame(@PathVariable Integer gameId) {
        return ResponseEntity.ok(gameService.reactivateArchivedGame(gameId));
    }

    /**
     * Browse all available games
     */
    @GetMapping
    public ResponseEntity<List<GameResponseDto>> browseGames(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(gameService.browseGames(category, minPrice, maxPrice));
    }

    /**
     * Search games
     */
    @GetMapping("/search")
    public ResponseEntity<List<GameResponseDto>> searchGames(
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
    public ResponseEntity<GameResponseDto> addGameToCategory(
            @PathVariable Integer gameId,
            @PathVariable Integer categoryId) {
        return ResponseEntity.ok(gameService.addGameToCategory(gameId, categoryId));
    }

    /**
     * Update game stock
     */
    @PutMapping("/{gameId}/stock")
    public ResponseEntity<GameResponseDto> updateGameStock(
            @PathVariable Integer gameId,
            @RequestParam Integer stock) {
        return ResponseEntity.ok(gameService.updateGameStock(gameId, stock));
    }

    /**
     * Update game price
     */
    @PutMapping("/{gameId}/price")
    public ResponseEntity<GameResponseDto> updateGamePrice(
            @PathVariable Integer gameId,
            @RequestParam Double price) {
        return ResponseEntity.ok(gameService.updateGamePrice(gameId, price));
    }
}