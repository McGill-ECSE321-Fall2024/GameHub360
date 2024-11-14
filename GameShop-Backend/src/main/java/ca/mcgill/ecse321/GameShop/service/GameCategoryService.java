package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.GameCategoryRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.PromotionRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class GameCategoryService {

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private GameRepository gameRepository;

    /**
     * Retrieves all categories from the repository.
     *
     * @return a list of all categories.
     */
    @Transactional
    public List<GameCategory> getAllCategories() {
        return gameCategoryRepository.findAll();
    }

    /**
     * Creates a new category based on the provided category request DTO.
     *
     * @param gameCategoryRequestDto the DTO containing the details of the category
     *                               to create.
     * @return GameCategory the created category.
     * @throws GameShopException if the category is already in the repository.
     */
    @Transactional
    public GameCategory createGameCategory(GameCategoryRequestDto gameCategoryRequestDto) {
        // Check if category already exists
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryByName(gameCategoryRequestDto.getName());
        if (gameCategory != null && Objects.equals(gameCategory.getName(), gameCategoryRequestDto.getName())) {
            throw new GameShopException(HttpStatus.CONFLICT, "Game category already exists.");
        }

        // Create new category
        GameCategory newGameCategory = new GameCategory(gameCategoryRequestDto.isAvailable(),
                gameCategoryRequestDto.getName());
        newGameCategory.setCategoryType(gameCategoryRequestDto.getCategoryType());

        // Optionally add games
        if (gameCategoryRequestDto.getGameIds() != null) {
            for (Integer gameId : gameCategoryRequestDto.getGameIds()) {
                Game game = gameRepository.findGameByGameEntityId(gameId);
                // Check if game exists
                if (game == null) {
                    throw new GameShopException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
                }
                newGameCategory.addGame(game);
            }
        }

        // Optionally add promotions
        if (gameCategoryRequestDto.getPromotionIds() != null) {
            for (Integer promotionId : gameCategoryRequestDto.getPromotionIds()) {
                Promotion promotion = promotionRepository.findPromotionByPromotionId(promotionId);
                // Check if promotion exists
                if (promotion == null) {
                    throw new GameShopException(HttpStatus.NOT_FOUND,
                            "Promotion with ID " + promotionId + " not found.");
                }
                newGameCategory.addPromotion(promotion);
            }
        }

        return gameCategoryRepository.save(newGameCategory);
    }

    /**
     * Updates an existing category with the given ID and new details from the
     * provided DTO.
     *
     * @param categoryId             the ID of the category to update.
     * @param gameCategoryRequestDto the DTO containing the updated category
     *                               details.
     * @return the updated category.
     * @throws GameShopException if the category is not found or if the updated
     *                           category
     *                           already exists.
     */
    @Transactional
    public GameCategory updateGameCategory(Integer categoryId, GameCategoryRequestDto gameCategoryRequestDto) {
        // Check if category exists
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
        if (gameCategory == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game category not found.");
        }

        // Update existing category fields
        updateCategoryFields(gameCategory, gameCategoryRequestDto);

        // Add games to the category if provided in the DTO
        addGamesToCategory(gameCategory, gameCategoryRequestDto.getGameIds());

        // Add promotions to the category if provided in the DTO
        addPromotionsToCategory(gameCategory, gameCategoryRequestDto.getPromotionIds());

        return gameCategoryRepository.save(gameCategory);
    }

    /**
     * Deletes the category with the specified ID.
     *
     * @param categoryId the ID of the category to delete.
     * @throws GameShopException if the category with the given ID is not found.
     */
    @Transactional
    public void deleteGameCategory(Integer categoryId) {
        // Check if category exists
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
        if (gameCategory == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game category with ID " + categoryId + " not found.");
        }

        gameCategoryRepository.delete(gameCategory);
    }

    /**
     * Retrieves all categories associated with the specified promotion ID.
     *
     * @param promotionId the ID of the promotion for which categories are to be
     *                    retrieved.
     * @return a list of categories associated with the specified promotion.
     * @throws GameShopException if the promotion with the given ID is not found.
     */
    @Transactional
    public List<GameCategory> getGameCategoriesByPromotionId(Integer promotionId) {
        // Check if promotion exists
        Promotion promotion = promotionRepository.findPromotionByPromotionId(promotionId);
        if (promotion == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Promotion with ID " + promotionId + " not found.");
        }

        return gameCategoryRepository.findByPromotionsContaining(promotion);
    }

    /**
     * Retrieves all categories associated with the specified game ID.
     *
     * @param gameId the ID of the game for which categories are to be retrieved.
     * @return a list of categories associated with the specified game.
     * @throws GameShopException if the game with the given ID is not found.
     */
    @Transactional
    public List<GameCategory> getGameCategoriesByGameId(Integer gameId) {
        // Check if game exists
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
        }

        return gameCategoryRepository.findByGamesContaining(game);
    }

    /**
     * Helper to update the fields of the given GameCategory based on the provided
     * DTO.
     *
     * @param gameCategory           the GameCategory to be updated.
     * @param gameCategoryRequestDto the DTO containing the new field values.
     */
    private void updateCategoryFields(GameCategory gameCategory, GameCategoryRequestDto gameCategoryRequestDto) {
        if (gameCategoryRequestDto.getName() != null) {
            gameCategory.setName(gameCategoryRequestDto.getName());
        }

        if (gameCategoryRequestDto.getCategoryType() != null) {
            gameCategory.setCategoryType(gameCategoryRequestDto.getCategoryType());
        }

        if (gameCategoryRequestDto.isAvailable() != null) {
            gameCategory.setIsAvailable(gameCategoryRequestDto.isAvailable());
        }
    }

    /**
     * Helper to add games to the given GameCategory based on the list of game IDs.
     *
     * @param gameCategory the GameCategory to which games will be added.
     * @param gameIds      the list of game IDs to add.
     * @throws GameShopException if a game with a given ID is not found.
     */
    private void addGamesToCategory(GameCategory gameCategory, List<Integer> gameIds) {
        if (gameIds != null) {
            for (Integer gameId : gameIds) {
                Game game = gameRepository.findGameByGameEntityId(gameId);
                if (game == null) {
                    throw new GameShopException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
                }
                if (!gameCategory.getGames().contains(game)) {
                    gameCategory.addGame(game);
                }
            }
        }
    }

    /**
     * Helper to add promotions to the given GameCategory based on the list of
     * promotion IDs.
     *
     * @param gameCategory the GameCategory to which promotions will be added.
     * @param promotionIds the list of promotion IDs to add.
     * @throws GameShopException if a promotion with a given ID is not found.
     */
    private void addPromotionsToCategory(GameCategory gameCategory, List<Integer> promotionIds) {
        if (promotionIds != null) {
            for (Integer promotionId : promotionIds) {
                Promotion promotion = promotionRepository.findPromotionByPromotionId(promotionId);
                if (promotion == null) {
                    throw new GameShopException(HttpStatus.NOT_FOUND,
                            "Promotion with ID " + promotionId + " not found.");
                }
                if (!gameCategory.getPromotions().contains(promotion)) {
                    gameCategory.addPromotion(promotion);
                }
            }
        }
    }
}