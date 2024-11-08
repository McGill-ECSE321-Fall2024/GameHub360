package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.PromotionRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.PromotionRepository;
import ca.mcgill.ecse321.GameShop.repository.StoreInformationRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private StoreInformationRepository storeInformationRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    /**
     * Retrieves all promotions.
     *
     * @return A list of all promotions.
     */
    public List<Promotion> getAllPromotions() {
        return (List<Promotion>) promotionRepository.findAll();
    }

    /**
     * Creates a new promotion based on the given promotion details.
     *
     * @param promotionRequestDto The promotion details to be created.
     * @return The created promotion.
     */
    @Transactional
    public Promotion createPromotion(PromotionRequestDto promotionRequestDto) {
        validatePromotionType(promotionRequestDto);
        validateDiscountPercentage(promotionRequestDto.getDiscountPercentageValue());

        // Retrieve the store information
        StoreInformation storeInfo = storeInformationRepository.findFirstByOrderByStoreInfoIdAsc();
        if (storeInfo == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Store information not found.");
        }

        // Create a new promotion and set attributes
        Promotion promotion = new Promotion();
        promotion.setPromotionType(promotionRequestDto.getPromotionType());
        promotion.setDiscountPercentageValue(promotionRequestDto.getDiscountPercentageValue());
        promotion.setInfo(storeInfo);

        if (promotionRequestDto.getPromotionType() == Promotion.PromotionType.GAME) {
            // Map game IDs to Game entities and add each promoted game
            promotionRequestDto.getPromotedGameIds().forEach(gameId -> {
                Game game = gameRepository.findById(gameId)
                        .orElseThrow(() -> new GameShopException(HttpStatus.NOT_FOUND,
                                "Game with ID " + gameId + " not found."));
                promotion.addPromotedGame(game);
            });
        } else {
            // Map category IDs to GameCategory entities and add each promoted category
            promotionRequestDto.getPromotedCategoryIds().forEach(categoryId -> {
                GameCategory category = gameCategoryRepository.findById(categoryId)
                        .orElseThrow(() -> new GameShopException(HttpStatus.NOT_FOUND,
                                "Category with ID " + categoryId + " not found."));
                promotion.addPromotedCategory(category);
            });
        }

        return promotionRepository.save(promotion);
    }

    /**
     * Updates an existing promotion by ID with the provided details.
     *
     * @param promotionId         The ID of the promotion to update.
     * @param promotionRequestDto The updated promotion details.
     * @return The updated promotion.
     * @throws GameShopException if the promotion is not found or validation fails.
     */
    @Transactional
    public Promotion updatePromotion(Integer promotionId, PromotionRequestDto promotionRequestDto) {
        // Retrieve the existing promotion
        Promotion existingPromotion = promotionRepository.findPromotionByPromotionId(promotionId);

        if (existingPromotion == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Promotion with ID " + promotionId + " not found.");
        }

        // Prevent changing promotion type
        if (promotionRequestDto.getPromotionType() != null &&
                promotionRequestDto.getPromotionType() != existingPromotion.getPromotionType()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Cannot change promotion type during update.");
        }

        // Construct a combined DTO with existing and new values
        PromotionRequestDto combinedDto = new PromotionRequestDto(
                existingPromotion.getPromotionType(),
                promotionRequestDto.getDiscountPercentageValue() != 0.0
                        ? promotionRequestDto.getDiscountPercentageValue()
                        : existingPromotion.getDiscountPercentageValue(),
                promotionRequestDto.getPromotedGameIds() != null && !promotionRequestDto.getPromotedGameIds().isEmpty()
                        ? promotionRequestDto.getPromotedGameIds()
                        : existingPromotion.getPromotedGames().stream().map(Game::getGameEntityId)
                                .collect(Collectors.toList()),
                promotionRequestDto.getPromotedCategoryIds() != null
                        && !promotionRequestDto.getPromotedCategoryIds().isEmpty()
                                ? promotionRequestDto.getPromotedCategoryIds()
                                : existingPromotion.getPromotedCategories().stream().map(GameCategory::getCategoryId)
                                        .collect(Collectors.toList()));

        // Validate the combined DTO
        validatePromotionType(combinedDto);
        validateDiscountPercentage(combinedDto.getDiscountPercentageValue());

        // Apply the valid changes to the existing promotion
        if (promotionRequestDto.getDiscountPercentageValue() != 0.0) {
            existingPromotion.setDiscountPercentageValue(combinedDto.getDiscountPercentageValue());
        }

        if (existingPromotion.getPromotionType() == Promotion.PromotionType.GAME) {
            if (promotionRequestDto.getPromotedGameIds() != null
                    && !promotionRequestDto.getPromotedGameIds().isEmpty()) {
                // Remove existing promoted games
                List<Game> gamesToRemove = new ArrayList<>(existingPromotion.getPromotedGames());
                for (Game game : gamesToRemove) {
                    existingPromotion.removePromotedGame(game);
                }

                // Update the list of promoted games
                combinedDto.getPromotedGameIds().forEach(gameId -> {
                    Game game = gameRepository.findById(gameId)
                            .orElseThrow(() -> new GameShopException(HttpStatus.NOT_FOUND,
                                    "Game with ID " + gameId + " not found."));
                    existingPromotion.addPromotedGame(game);
                });
            }
        } else if (existingPromotion.getPromotionType() == Promotion.PromotionType.CATEGORY
                && promotionRequestDto.getPromotedCategoryIds() != null
                && !promotionRequestDto.getPromotedCategoryIds().isEmpty()) {
            // Remove existing promoted categories
            List<GameCategory> categoriesToRemove = new ArrayList<>(existingPromotion.getPromotedCategories());
            for (GameCategory category : categoriesToRemove) {
                existingPromotion.removePromotedCategory(category);
            }

            // Update the list of promoted categories
            combinedDto.getPromotedCategoryIds().forEach(categoryId -> {
                GameCategory category = gameCategoryRepository.findById(categoryId)
                        .orElseThrow(() -> new GameShopException(HttpStatus.NOT_FOUND,
                                "Category with ID " + categoryId + " not found."));
                existingPromotion.addPromotedCategory(category);
            });
        }

        return promotionRepository.save(existingPromotion);
    }

    /**
     * Deletes a promotion by its ID.
     *
     * @param promotionId The ID of the promotion to delete.
     * @throws GameShopException if the promotion is not found.
     */
    @Transactional
    public void deletePromotion(Integer promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new GameShopException(HttpStatus.NOT_FOUND,
                        "Promotion with ID " + promotionId + " not found."));
        promotionRepository.delete(promotion);
    }

    /**
     * Retrieves a list of promotions associated with a specific game ID.
     *
     * @param gameId The ID of the game.
     * @return A list of promotions associated with the given game.
     * @throws GameShopException if the game is not found.
     */
    public List<Promotion> getPromotionsByGameId(Integer gameId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
        }
        return promotionRepository.findByPromotedGamesContaining(game);
    }

    /**
     * Retrieves a list of promotions associated with a specific category ID.
     *
     * @param categoryId The ID of the category.
     * @return A list of promotions associated with the given category.
     * @throws GameShopException if the category is not found.
     */
    public List<Promotion> getPromotionsByCategoryId(Integer categoryId) {
        GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
        if (category == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Category with ID " + categoryId + " not found.");
        }
        return promotionRepository.findByPromotedCategoriesContaining(category);
    }

    /**
     * Validates that only one type of promotion (either games or categories) is
     * provided.
     *
     * @param promotionRequestDto The promotion request details to validate.
     */
    private void validatePromotionType(PromotionRequestDto promotionRequestDto) {
        if (promotionRequestDto.getPromotionType() == Promotion.PromotionType.GAME) {
            if (promotionRequestDto.getPromotedCategoryIds() != null
                    && !promotionRequestDto.getPromotedCategoryIds().isEmpty()) {
                throw new GameShopException(HttpStatus.BAD_REQUEST,
                        "Promotion type GAME cannot have associated categories.");
            }
            if (promotionRequestDto.getPromotedGameIds() == null
                    || promotionRequestDto.getPromotedGameIds().isEmpty()) {
                throw new GameShopException(HttpStatus.BAD_REQUEST,
                        "No promoted games provided for GAME promotion type.");
            }
        } else if (promotionRequestDto.getPromotionType() == Promotion.PromotionType.CATEGORY) {
            if (promotionRequestDto.getPromotedGameIds() != null
                    && !promotionRequestDto.getPromotedGameIds().isEmpty()) {
                throw new GameShopException(HttpStatus.BAD_REQUEST,
                        "Promotion type CATEGORY cannot have associated games.");
            }
            if (promotionRequestDto.getPromotedCategoryIds() == null
                    || promotionRequestDto.getPromotedCategoryIds().isEmpty()) {
                throw new GameShopException(HttpStatus.BAD_REQUEST,
                        "No promoted categories provided for CATEGORY promotion type.");
            }
        }
    }

    /**
     * Validates the discount percentage value.
     *
     * @param discountPercentage The discount percentage to validate.
     */
    private void validateDiscountPercentage(double discountPercentage) {
        if (discountPercentage < 0.0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Discount percentage value must be zero or positive.");
        }
        if (discountPercentage > 100.0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Discount percentage cannot exceed 100%.");
        }
    }
}
