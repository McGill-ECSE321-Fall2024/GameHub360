package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.GameCategoryListDto;
import ca.mcgill.ecse321.GameShop.dto.GameCategoryRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameCategoryResponseDto;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.service.GameCategoryService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/categories")
public class GameCategoryController {

    @Autowired
    private GameCategoryService gameCategoryService;

    /**
     * Endpoint to view all categories.
     *
     * @return A DTO containing a list of all current categories.
     */
    @GetMapping
    public GameCategoryListDto getAllGameCategories() {
        List<GameCategoryResponseDto> gameCategoryDtos = new ArrayList<>();
        for (GameCategory gameCategory : gameCategoryService.getAllCategories()) {
            gameCategoryDtos.add(new GameCategoryResponseDto(gameCategory));
        }
        return new GameCategoryListDto(gameCategoryDtos);
    }

    /**
     * Endpoint to get categories by game ID.
     *
     * @param gameId The ID of the game.
     * @return A DTO containing a list of categories associated with the given game.
     */
    @GetMapping("/game/{gameId}")
    public GameCategoryListDto getGameCategoriesByGame(@PathVariable("gameId") Integer gameId) {
        List<GameCategoryResponseDto> gameCategoryDtos = new ArrayList<>();
        for (GameCategory gameCategory : gameCategoryService.getGameCategoriesByGameId(gameId)) {
            gameCategoryDtos.add(new GameCategoryResponseDto(gameCategory));
        }
        return new GameCategoryListDto(gameCategoryDtos);
    }

    /**
     * Endpoint to get categories by promotion ID.
     *
     * @param promotionId The ID of the promotion.
     * @return A DTO containing a list of categories associated with the given
     *         promotion.
     */
    @GetMapping("/promotion/{promotionId}")
    public GameCategoryListDto getGameCategoriesByPromotion(@PathVariable("promotionId") Integer promotionId) {
        List<GameCategoryResponseDto> gameCategoryDtos = new ArrayList<>();
        for (GameCategory gameCategory : gameCategoryService.getGameCategoriesByPromotionId(promotionId)) {
            gameCategoryDtos.add(new GameCategoryResponseDto(gameCategory));
        }
        return new GameCategoryListDto(gameCategoryDtos);
    }

    /**
     * Endpoint to create a new category.
     *
     * @param gameCategoryRequestDto The details of the new category.
     * @return The created category details.
     */
    @PostMapping
    public GameCategoryResponseDto createGameCategory(
            @Valid @RequestBody GameCategoryRequestDto gameCategoryRequestDto) {
        GameCategory gameCategory = gameCategoryService.createGameCategory(gameCategoryRequestDto);
        return new GameCategoryResponseDto(gameCategory);
    }

    /**
     * Endpoint to get a category by ID.
     *
     * @param categoryId The ID of the category to retrieve.
     * @return The category details.
     */
    @GetMapping("/{categoryId}")
    public GameCategoryResponseDto getGameCategoryById(@PathVariable("categoryId") Integer categoryId) {
        GameCategory gameCategory = gameCategoryService.getGameCategoryById(categoryId);
        return new GameCategoryResponseDto(gameCategory);
    }

    /**
     * Endpoint to update an existing category.
     *
     * @param categoryId             The ID of the category to update.
     * @param gameCategoryRequestDto The updated category details (can be null).
     * @return The updated category information.
     */
    @PutMapping("/{categoryId}")
    public GameCategoryResponseDto updateGameCategory(@PathVariable("categoryId") Integer categoryId,
            @RequestBody GameCategoryRequestDto gameCategoryRequestDto) {
        GameCategory gameCategory = gameCategoryService.updateGameCategory(categoryId, gameCategoryRequestDto);
        return new GameCategoryResponseDto(gameCategory);
    }

    /**
     * Endpoint to delete a category.
     *
     * @param categoryId The ID of the category to delete.
     */
    @DeleteMapping("/{categoryId}")
    public void deleteGameCategory(@PathVariable("categoryId") Integer categoryId) {
        gameCategoryService.deleteGameCategory(categoryId);
    }

    /**
     * add category to game
     * 
     * @param categoryId The ID of the category to add the game to.
     * @param gameId     The ID of the game to add to the category.
     * @return The updated category information.
     */
    @PutMapping("/{categoryId}/game/{gameId}")
    public GameCategoryResponseDto assignCategoryToGame(@PathVariable("categoryId") Integer categoryId,
            @PathVariable("gameId") Integer gameId) {
        GameCategory gameCategory = gameCategoryService.assignCategoryToGame(categoryId, gameId);
        return new GameCategoryResponseDto(gameCategory);
    }

    /**
     * remove category from game
     * 
     * @param categoryId The ID of the category to remove the game from.
     * @param gameId     The ID of the game to remove from the category.
     * @return The updated category information.
     */
    @DeleteMapping("/{categoryId}/game/{gameId}")
    public GameCategoryResponseDto unassignCategoryFromGame(@PathVariable("categoryId") Integer categoryId,
            @PathVariable("gameId") Integer gameId) {
        GameCategory gameCategory = gameCategoryService.unassignCategoryFromGame(categoryId, gameId);
        return new GameCategoryResponseDto(gameCategory);
    }

    /**
     * add category to promotion
     * 
     * @param categoryId  The ID of the category to add the promotion to.
     * @param promotionId The ID of the promotion to add to the category.
     * @return The updated category information.
     */
    @PutMapping("/{categoryId}/promotion/{promotionId}")
    public GameCategoryResponseDto assignCategoryToPromotion(@PathVariable("categoryId") Integer categoryId,
            @PathVariable("promotionId") Integer promotionId) {
        GameCategory gameCategory = gameCategoryService.assignCategoryToPromotion(categoryId, promotionId);
        return new GameCategoryResponseDto(gameCategory);
    }

    /**
     * remove category from promotion
     * 
     * @param categoryId    The ID of the category to remove the promotion from.
     * @param promotionId The ID of the promotion to remove from the category.
     * @return The updated category information.
     */
    @DeleteMapping("/{categoryId}/promotion/{promotionId}")
    public GameCategoryResponseDto unassignCategoryFromPromotion(@PathVariable("categoryId") Integer categoryId,
            @PathVariable("promotionId") Integer promotionId) {
        GameCategory gameCategory = gameCategoryService.unassignCategoryFromPromotion(categoryId, promotionId);
        return new GameCategoryResponseDto(gameCategory);
    }

}