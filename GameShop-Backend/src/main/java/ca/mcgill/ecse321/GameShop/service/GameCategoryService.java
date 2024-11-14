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
        return (List<GameCategory>) gameCategoryRepository.findAll();
    }

    /**
     * Creates a new category based on the provided category request DTO.
     *
     * @param gameCategoryRequestDto the DTO containing the details of the category to
     *                            create.
     * @return the created category.
     * @throws GameShopException if the category is already in the repository.
     */
    @Transactional
    public GameCategory createGameCategory(GameCategoryRequestDto gameCategoryRequestDto) {
        // Debug logging
        System.out.println("Request DTO isAvailable: " + gameCategoryRequestDto.isAvailable());
        
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryByName(gameCategoryRequestDto.getName());
        if (gameCategory != null) {
            if (gameCategory.getCategoryType() == gameCategoryRequestDto.getCategoryType()) {
                throw new GameShopException(HttpStatus.CONFLICT, "Game category already exists.");
            }
        }

        // Create new category using constructor
        GameCategory newGameCategory = new GameCategory(gameCategoryRequestDto.isAvailable(), gameCategoryRequestDto.getName());
        newGameCategory.setCategoryType(gameCategoryRequestDto.getCategoryType());
        
        // Debug logging
        System.out.println("Before save - isAvailable: " + newGameCategory.getIsAvailable());
        
        GameCategory savedCategory = gameCategoryRepository.save(newGameCategory);
        
        // Debug logging
        System.out.println("After save - isAvailable: " + savedCategory.getIsAvailable());
        
        return savedCategory;
    }

    /**
     * Updates an existing category with the given ID and new details from the
     * provided DTO.
     *
     * @param categoryId         the ID of the category to update.
     * @param gameCategoryRequestDto   the DTO containing the updated promotion details.
     * @return the updated promotion.
     * @throws GameShopException if the category is not found or if updated category already exists.
     */
    @Transactional
    public GameCategory updateGameCategory (Integer categoryId, GameCategoryRequestDto gameCategoryRequestDto) {
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
        if (gameCategory == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game category not found.");
        }
        if (gameCategory.getCategoryType().equals(gameCategoryRequestDto.getCategoryType()) && gameCategory.getName()
                .equals(gameCategoryRequestDto.getName())) {
            throw new GameShopException(HttpStatus.CONFLICT, "Another game category with the same name and type already exists.");
        }
        gameCategory.setName(gameCategoryRequestDto.getName());
        gameCategory.setIsAvailable(gameCategoryRequestDto.isAvailable());
        if (gameCategory.getCategoryType() != null) {
            gameCategory.setCategoryType(gameCategoryRequestDto.getCategoryType());
       }
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
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
        if (gameCategory == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game category with ID " + categoryId + " not found.");
        }
        gameCategoryRepository.delete(gameCategory);
    }

    /**
     * Retrieves all categories associated with the specified promotion ID.
     *
     * @param promotionId the ID of the promotion for which categories are to be retrieved.
     * @return a list of categories associated with the specified promotion.
     * @throws GameShopException if the promotion with the given ID is not found.
     */
    @Transactional
    public List<GameCategory> getGameCategoriesByPromotionId(Integer promotionId) {
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
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
        }
        return gameCategoryRepository.findByGamesContaining(game);
    }

}