package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.PromotionRequestDto;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {

    /**
     * Retrieves all promotions.
     * 
     * @return A list of all promotions.
     */
    public List<Promotion> getAllPromotions() {
        return null;
    }

    /**
     * Creates a new promotion based on the given promotion details.
     * 
     * @param promotionDto The promotion details to be created.
     * @return The created promotion.
     */
    public Promotion createPromotion(PromotionRequestDto promotionRequestDto) {
        return null;
    }

    /**
     * Updates an existing promotion by ID with the provided details.
     * 
     * @param promotionId   The ID of the promotion to update.
     * @param promotionDto The updated promotion details.
     * @return The updated promotion.
     */
    public Promotion updatePromotion(Long promotionId, PromotionRequestDto promotionRequestDto) {
        return null;
    }

    /**
     * Deletes a promotion by its ID.
     * 
     * @param promotionId The ID of the promotion to delete.
     */
    public void deletePromotion(Long promotionId) {
        // No return needed
    }

    /**
     * Retrieves a list of promotions associated with a specific game ID.
     * 
     * @param gameId The ID of the game.
     * @return A list of promotions associated with the given game.
     */
    public List<Promotion> getPromotionsByGameId(Long gameId) {
        return null;
    }

    /**
     * Retrieves a list of promotions associated with a specific category ID.
     * 
     * @param categoryId The ID of the category.
     * @return A list of promotions associated with the given category.
     */
    public List<Promotion> getPromotionsByCategoryId(Long categoryId) {
        return null;
    }
}
