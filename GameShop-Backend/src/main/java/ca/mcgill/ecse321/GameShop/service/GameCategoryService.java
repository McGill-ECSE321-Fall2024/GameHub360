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

    @Transactional
    public List<GameCategory> getAllCategories() {
        return (List<GameCategory>) gameCategoryRepository.findAll();
    }

    @Transactional
    public GameCategory createGameCategory (GameCategoryRequestDto gameCategoryRequestDto){
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryByName(gameCategoryRequestDto.getName());
        if (gameCategory != null) {
            if (gameCategory.getCategoryType() == gameCategoryRequestDto.getCategoryType()){
                throw new GameShopException(HttpStatus.CONFLICT, "Game category already exists.");
            }
        }

        GameCategory newGameCategory = new GameCategory(gameCategoryRequestDto.isAvailable(), gameCategoryRequestDto.getName());
        newGameCategory.setCategoryType(gameCategoryRequestDto.getCategoryType());

        return gameCategoryRepository.save(newGameCategory);
    }

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

    @Transactional
    public void deleteGameCategory(Integer categoryId) {
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
        if (gameCategory == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game category with ID " + categoryId + " not found.");
        }
        gameCategoryRepository.delete(gameCategory);
    }

    @Transactional
    public List<GameCategory> getGameCategoriesByPromotionId(Integer promotionId) {
        Promotion promotion = promotionRepository.findPromotionByPromotionId(promotionId);
        if (promotion == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Promotion with ID " + promotionId + " not found.");
        }
        return gameCategoryRepository.findByPromotionsContaining(promotion);
    }

    @Transactional
    public List<GameCategory> getGameCategoriesByGameId(Integer gameId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
        }
        return gameCategoryRepository.findByGamesContaining(game);
    }

}