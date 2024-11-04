package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.PromotionListDto;
import ca.mcgill.ecse321.GameShop.dto.PromotionRequestDto;
import ca.mcgill.ecse321.GameShop.dto.PromotionResponseDto;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.service.PromotionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public PromotionListDto getAllPromotions() {
        List<PromotionResponseDto> promotionDtos = new ArrayList<PromotionResponseDto>();
        for (Promotion promotion : promotionService.getAllPromotions()) {
            promotionDtos.add(new PromotionResponseDto(promotion));
        }
        return new PromotionListDto(promotionDtos);
    }

    /**
     * Endpoint to create a new store promotion.
     *
     * @param promotionDto The details of the new promotion.
     * @return The created promotion details.
     */
    @PostMapping("/")
    public PromotionResponseDto createPromotion(@RequestBody PromotionRequestDto promotionRequestDto) {
        Promotion promotion = promotionService.createPromotion(promotionRequestDto);
        return new PromotionResponseDto(promotion);
    }

    /**
     * Endpoint to update an existing promotion.
     *
     * @param promotionId  The ID of the promotion to update.
     * @param promotionDto The updated promotion details.
     * @return The updated promotion information.
     */
    @PutMapping("/{promotionId}")
    public PromotionResponseDto updatePromotion(@PathVariable Long promotionId,
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
    public void deletePromotion(@PathVariable Long promotionId) {
        promotionService.deletePromotion(promotionId);
    }

    /**
     * Endpoint to get promotions by game ID.
     *
     * @param gameId The ID of the game.
     * @return A list of promotions associated with the given game.
     */
    @GetMapping("/game/{gameId}")
    public PromotionListDto getPromotionsByGame(@PathVariable Long gameId) {
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
    public PromotionListDto getPromotionsByCategory(@PathVariable Long categoryId) {
        List<PromotionResponseDto> promotionDtos = new ArrayList<PromotionResponseDto>();
        for (Promotion promotion : promotionService.getPromotionsByCategoryId(categoryId)) {
            promotionDtos.add(new PromotionResponseDto(promotion));
        }
        return new PromotionListDto(promotionDtos);
    }
}
