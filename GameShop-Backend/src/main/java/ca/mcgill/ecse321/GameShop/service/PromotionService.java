package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.PromotionRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameListDto;
import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
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
     * Retrieves all promotions from the repository.
     * 
     * @return a list of all promotions.
     */
    @Transactional
    public List<Promotion> getAllPromotions() {
        return (List<Promotion>) promotionRepository.findAll();
    }

    /**
     * Creates a new promotion based on the provided promotion request DTO.
     * 
     * @param promotionRequestDto the DTO containing the details of the promotion to
     *                            create.
     * @return the created promotion.
     * @throws GameShopException if the store information is not found or if
     *                           validation fails.
     */
    @Transactional
    public Promotion createPromotion(PromotionRequestDto promotionRequestDto) {
        validatePromotionType(promotionRequestDto);
        validateDiscountPercentage(promotionRequestDto.getDiscountPercentageValue());

        StoreInformation storeInfo = getStoreInformation();

        Promotion promotion = new Promotion();
        promotion.setPromotionType(promotionRequestDto.getPromotionType());
        promotion.setDiscountPercentageValue(promotionRequestDto.getDiscountPercentageValue());
        promotion.setInfo(storeInfo);

        if (promotionRequestDto.getPromotionType() == Promotion.PromotionType.GAME) {
            addPromotedGames(promotion, promotionRequestDto.getPromotedGameIds());
        } else {
            addPromotedCategories(promotion, promotionRequestDto.getPromotedCategoryIds());
        }

        return promotionRepository.save(promotion);
    }

    /**
     * Updates an existing promotion with the given ID and new details from the
     * provided DTO.
     * 
     * @param promotionId         the ID of the promotion to update.
     * @param promotionRequestDto the DTO containing the updated promotion details.
     * @return the updated promotion.
     * @throws GameShopException if the promotion is not found, validation fails, or
     *                           if the promotion type is changed.
     */
    @Transactional
    public Promotion updatePromotion(Integer promotionId, PromotionRequestDto promotionRequestDto) {
        Promotion existingPromotion = getExistingPromotion(promotionId);
        validatePromotionTypeChange(promotionRequestDto, existingPromotion);

        PromotionRequestDto combinedDto = buildCombinedDto(existingPromotion, promotionRequestDto);
        validatePromotionType(combinedDto);
        validateDiscountPercentage(combinedDto.getDiscountPercentageValue());

        applyUpdatesToPromotion(existingPromotion, combinedDto, promotionRequestDto);

        return promotionRepository.save(existingPromotion);
    }

    /**
     * Deletes the promotion with the specified ID.
     * 
     * @param promotionId the ID of the promotion to delete.
     * @throws GameShopException if the promotion with the given ID is not found.
     */
    @Transactional
    public void deletePromotion(Integer promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new GameShopException(HttpStatus.NOT_FOUND,
                        "Promotion with ID " + promotionId + " not found."));
        promotionRepository.delete(promotion);
    }

    /**
     * Retrieves all promotions filtered by the specified type.
     *
     * @param type the type of promotion to filter by.
     * @return a list of promotions matching the given type.
     */
    @Transactional
    public List<Promotion> getPromotionsByType(String type) {
        Promotion.PromotionType promotionType;
        try {
            promotionType = Promotion.PromotionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Invalid promotion type: " + type);
        }
        return promotionRepository.findByPromotionType(promotionType); // New repository method
    }

    /**
     * Retrieves all promotions associated with the specified game ID.
     * 
     * @param gameId the ID of the game for which promotions are to be retrieved.
     * @return a list of promotions associated with the specified game.
     * @throws GameShopException if the game with the given ID is not found.
     */
    public List<Promotion> getPromotionsByGameId(Integer gameId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
        }
        return promotionRepository.findByPromotedGamesContaining(game);
    }

    /**
     * Retrieves all promotions associated with the specified category ID.
     * 
     * @param categoryId the ID of the category for which promotions are to be
     *                   retrieved.
     * @return a list of promotions associated with the specified category.
     * @throws GameShopException if the category with the given ID is not found.
     */
    public List<Promotion> getPromotionsByCategoryId(Integer categoryId) {
        GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
        if (category == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Category with ID " + categoryId + " not found.");
        }
        return promotionRepository.findByPromotedCategoriesContaining(category);
    }

    /**
     * Retrieves the store information.
     * 
     * @return the store information.
     * @throws GameShopException if the store information is not found.
     */
    private StoreInformation getStoreInformation() {
        StoreInformation storeInfo = storeInformationRepository.findFirstByOrderByStoreInfoIdAsc();
        if (storeInfo == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Store information not found.");
        }
        return storeInfo;
    }

    /**
     * Retrieves an existing promotion by its ID.
     * 
     * @param promotionId the ID of the promotion to retrieve.
     * @return the existing promotion.
     * @throws GameShopException if the promotion with the given ID is not found.
     */
    private Promotion getExistingPromotion(Integer promotionId) {
        Promotion existingPromotion = promotionRepository.findPromotionByPromotionId(promotionId);
        if (existingPromotion == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Promotion with ID " + promotionId + " not found.");
        }
        return existingPromotion;
    }

    /**
     * Validates if the promotion type can be changed during an update.
     * 
     * @param promotionRequestDto the DTO containing the new promotion type.
     * @param existingPromotion   the existing promotion to compare with.
     * @throws GameShopException if the promotion type is attempted to be changed.
     */
    private void validatePromotionTypeChange(PromotionRequestDto promotionRequestDto, Promotion existingPromotion) {
        if (promotionRequestDto.getPromotionType() != null &&
                promotionRequestDto.getPromotionType() != existingPromotion.getPromotionType()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Cannot change promotion type during update.");
        }
    }

    /**
     * Builds a combined DTO for updating promotions by merging existing and new
     * details.
     * 
     * @param existingPromotion   the existing promotion.
     * @param promotionRequestDto the new promotion details.
     * @return a combined DTO with merged data.
     */
    private PromotionRequestDto buildCombinedDto(Promotion existingPromotion, PromotionRequestDto promotionRequestDto) {
        return new PromotionRequestDto(
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
    }

    /**
     * Applies updates to an existing promotion.
     * 
     * @param promotion   the existing promotion to update.
     * @param combinedDto the combined DTO with updated data.
     * @param originalDto the original DTO for validation reference.
     */
    private void applyUpdatesToPromotion(Promotion promotion, PromotionRequestDto combinedDto,
            PromotionRequestDto originalDto) {
        if (originalDto.getDiscountPercentageValue() != 0.0) {
            promotion.setDiscountPercentageValue(combinedDto.getDiscountPercentageValue());
        }

        if (promotion.getPromotionType() == Promotion.PromotionType.GAME) {
            updatePromotedGames(promotion, combinedDto, originalDto);
        } else if (promotion.getPromotionType() == Promotion.PromotionType.CATEGORY) {
            updatePromotedCategories(promotion, combinedDto, originalDto);
        }
    }

    /**
     * Updates the promoted games in a promotion.
     * 
     * @param promotion   the promotion to update.
     * @param combinedDto the combined DTO with new game data.
     * @param originalDto the original DTO for validation.
     */
    private void updatePromotedGames(Promotion promotion, PromotionRequestDto combinedDto,
            PromotionRequestDto originalDto) {
        if (originalDto.getPromotedGameIds() != null && !originalDto.getPromotedGameIds().isEmpty()) {
            removeExistingGames(promotion);
            addPromotedGames(promotion, combinedDto.getPromotedGameIds());
        }
    }

    /**
     * Retrieves a promotion by ID.
     *
     * @param promotionId The ID of the promotion to retrieve.
     * @return the Promotion.
     * @throws GameShopException if the promotion is not found.
     */
    @Transactional
    public Promotion getPromotionById(Integer promotionId) {
        // Find employee by ID
        Promotion promotion = promotionRepository.findPromotionByPromotionId(promotionId);

        // Check if employee exists
        if (promotion == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Promotion not found.");
        }
        return promotion;
    }

    /**
     * Updates the promoted categories in a promotion.
     * 
     * @param promotion   the promotion to update.
     * @param combinedDto the combined DTO with new category data.
     * @param originalDto the original DTO for validation.
     */
    private void updatePromotedCategories(Promotion promotion, PromotionRequestDto combinedDto,
            PromotionRequestDto originalDto) {
        if (originalDto.getPromotedCategoryIds() != null && !originalDto.getPromotedCategoryIds().isEmpty()) {
            removeExistingCategories(promotion);
            addPromotedCategories(promotion, combinedDto.getPromotedCategoryIds());
        }
    }

    /**
     * Removes existing games from a promotion.
     * 
     * @param promotion the promotion from which to remove games.
     */
    private void removeExistingGames(Promotion promotion) {
        List<Game> gamesToRemove = new ArrayList<>(promotion.getPromotedGames());
        for (Game game : gamesToRemove) {
            promotion.removePromotedGame(game);
        }
    }

    /**
     * Removes existing categories from a promotion.
     * 
     * @param promotion the promotion from which to remove categories.
     */
    private void removeExistingCategories(Promotion promotion) {
        List<GameCategory> categoriesToRemove = new ArrayList<>(promotion.getPromotedCategories());
        for (GameCategory category : categoriesToRemove) {
            promotion.removePromotedCategory(category);
        }
    }

    /**
     * Adds promoted games to a promotion.
     * 
     * @param promotion the promotion to which games are to be added.
     * @param gameIds   the list of game IDs to be added.
     * @throws GameShopException if any game ID is not found.
     */
    private void addPromotedGames(Promotion promotion, List<Integer> gameIds) {
        gameIds.forEach(gameId -> {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new GameShopException(HttpStatus.NOT_FOUND,
                            "Game with ID " + gameId + " not found."));
            promotion.addPromotedGame(game);
        });
    }

    /**
     * Adds promoted categories to a promotion.
     * 
     * @param promotion   the promotion to which categories are to be added.
     * @param categoryIds the list of category IDs to be added.
     * @throws GameShopException if any category ID is not found.
     */
    private void addPromotedCategories(Promotion promotion, List<Integer> categoryIds) {
        categoryIds.forEach(categoryId -> {
            GameCategory category = gameCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new GameShopException(HttpStatus.NOT_FOUND,
                            "Category with ID " + categoryId + " not found."));
            promotion.addPromotedCategory(category);
        });
    }

    /**
     * Validates the promotion type and its related data in the DTO.
     * 
     * @param promotionRequestDto the DTO containing promotion type and related
     *                            data.
     * @throws GameShopException if the promotion type and associated data do not
     *                           align.
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

    private void validateDiscountPercentage(double discountPercentage) {
        if (discountPercentage < 0.0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Discount percentage value must be zero or positive.");
        }
        if (discountPercentage > 100.0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Discount percentage cannot exceed 100%.");
        }
    }

    /**
     * Retrieves promoted games by promotion.
     *
     * @param promotionId The id of the promotion.
     * @return A list of promoted games.
     */
    @Transactional
    public GameListDto getPromotedGames(Integer promotionId) {
        // Fetch the promotion by ID
        Promotion promotion = promotionRepository.findPromotionByPromotionId(promotionId);
        if (promotion == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Promotion with ID " + promotionId + " not found.");
        }

        // Convert associated games to GameResponseDto
        List<GameResponseDto> gameDtos = promotion.getPromotedGames().stream()
                .map(GameResponseDto::new)
                .collect(Collectors.toList());

        // Return the games wrapped in GameListDto
        return new GameListDto(gameDtos);
    }
}