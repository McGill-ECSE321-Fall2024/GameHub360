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
    @GetMapping("/")
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
    public GameCategoryListDto getGameCategoriesByGame(@PathVariable Integer gameId) {
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
    public GameCategoryListDto getGameCategoriesByPromotion(@PathVariable Integer promotionId) {
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
    @PostMapping("/")
    public GameCategoryResponseDto createGameCategory(
            @Valid @RequestBody GameCategoryRequestDto gameCategoryRequestDto) {
        GameCategory gameCategory = gameCategoryService.createGameCategory(gameCategoryRequestDto);
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
    public GameCategoryResponseDto updateGameCategory(@PathVariable Integer categoryId,
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
    public void deleteGameCategory(@PathVariable Integer categoryId) {
        gameCategoryService.deleteGameCategory(categoryId);
    }
}