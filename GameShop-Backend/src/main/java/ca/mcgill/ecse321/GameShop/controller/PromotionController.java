package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.PromotionListDto;
import ca.mcgill.ecse321.GameShop.dto.PromotionRequestDto;
import ca.mcgill.ecse321.GameShop.dto.PromotionResponseDto;
import ca.mcgill.ecse321.GameShop.dto.GameListDto;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.service.PromotionService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    /**
     * Endpoint to view all store promotions.
     *
     * @return A list of all current promotions.
     */
    @GetMapping("/")
    public PromotionListDto getAllPromotions(@RequestParam(value = "type", required = false) String type) {
        List<PromotionResponseDto> promotionDtos = new ArrayList<>();
        List<Promotion> promotions;

        if (type != null) {
            promotions = promotionService.getPromotionsByType(type);
        } else {
            promotions = promotionService.getAllPromotions();
        }

        for (Promotion promotion : promotions) {
            promotionDtos.add(new PromotionResponseDto(promotion));
        }
        return new PromotionListDto(promotionDtos);
    }

    /**
     * Endpoint to create a new store promotion.
     *
     * @param promotionRequestDto The details of the new promotion.
     * @return The created promotion details.
     */
    @PostMapping
    public ResponseEntity<PromotionResponseDto> createPromotion(
            @Valid @RequestBody PromotionRequestDto promotionRequestDto) {
        Promotion promotion = promotionService.createPromotion(promotionRequestDto);
        return ResponseEntity.status(201).body(new PromotionResponseDto(promotion));
    }

    /**
     * Endpoint to update an existing promotion.
     *
     * @param promotionId         The ID of the promotion to update.
     * @param promotionRequestDto The updated promotion details (can be null).
     * @return The updated promotion information.
     */
    @PutMapping("/{promotionId}")
    public PromotionResponseDto updatePromotion(@PathVariable Integer promotionId,
            @RequestBody PromotionRequestDto promotionRequestDto) {
        Promotion promotion = promotionService.updatePromotion(promotionId, promotionRequestDto);
        return new PromotionResponseDto(promotion);
    }

    /**
     * Endpoint to delete a promotion.
     *
     * @param promotionId The ID of the promotion to delete.
     */
    @DeleteMapping("/{promotionId}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Integer promotionId) {
        promotionService.deletePromotion(promotionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to get promotions by game ID.
     *
     * @param gameId The ID of the game.
     * @return A list of promotions associated with the given game.
     */
    @GetMapping("/game/{gameId}")
    public PromotionListDto getPromotionsByGame(@PathVariable Integer gameId) {
        List<PromotionResponseDto> promotionDtos = new ArrayList<PromotionResponseDto>();
        for (Promotion promotion : promotionService.getPromotionsByGameId(gameId)) {
            promotionDtos.add(new PromotionResponseDto(promotion));
        }
        return new PromotionListDto(promotionDtos);
    }

    /**
     * Endpoint to get promotions by category ID.
     *
     * @param categoryId The ID of the category.
     * @return A list of promotions associated with the given category.
     */
    @GetMapping("/category/{categoryId}")
    public PromotionListDto getPromotionsByCategory(@PathVariable Integer categoryId) {
        List<PromotionResponseDto> promotionDtos = new ArrayList<PromotionResponseDto>();
        for (Promotion promotion : promotionService.getPromotionsByCategoryId(categoryId)) {
            promotionDtos.add(new PromotionResponseDto(promotion));
        }
        return new PromotionListDto(promotionDtos);
    }

    /**
     * Endpoint to get promotions by type.
     *
     * @param type The type of the promotion (e.g., GAME, CATEGORY).
     * @return A list of promotions matching the given type.
     */
    @GetMapping("/type/{type}")
    public PromotionListDto getPromotionsByType(@PathVariable String type) {
        List<PromotionResponseDto> promotionDtos = new ArrayList<>();
        for (Promotion promotion : promotionService.getPromotionsByType(type)) {
            promotionDtos.add(new PromotionResponseDto(promotion));
        }
        return new PromotionListDto(promotionDtos);
    }

    /**
     * Endpoint to get promoted games by promotion.
     *
     * @param promotionId The id of the promotion.
     * @return A list of promoted games.
     */
    @GetMapping("/{promotionId}/games")
    public ResponseEntity<GameListDto> getPromotedGames(@PathVariable Integer promotionId) {
        GameListDto games = promotionService.getPromotedGames(promotionId);
        return ResponseEntity.ok(games);
    }
}
