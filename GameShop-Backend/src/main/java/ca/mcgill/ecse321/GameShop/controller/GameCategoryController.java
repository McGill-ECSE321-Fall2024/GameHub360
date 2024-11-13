package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.GameCategoryRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameCategoryResponseDto;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.service.GameCategoryService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class GameCategoryController {

    @Autowired
    private GameCategoryService gameCategoryService;

    @GetMapping("/")
    public List<GameCategoryResponseDto> getAllGameCategories() {
        List<GameCategoryResponseDto> gameCategoryDtos = new ArrayList<GameCategoryResponseDto>();
        for (GameCategory gameCategory : gameCategoryService.getAllCategories()) {
            gameCategoryDtos.add(new GameCategoryResponseDto(gameCategory));
        }
        return gameCategoryDtos;
    }

    @GetMapping("/game/{gameId}")
    public List<GameCategoryResponseDto> getGameCategoriesByGame(@PathVariable Integer gameId) {
        List<GameCategoryResponseDto> gameCategoriesDtos = new ArrayList<GameCategoryResponseDto>();
        for (GameCategory gameCategory : gameCategoryService.getGameCategoriesByGameId(gameId)) {
            gameCategoriesDtos.add(new GameCategoryResponseDto(gameCategory));
        }
        return gameCategoriesDtos;
    }

    @GetMapping("/promotion/{promotionId}")
    public List<GameCategoryResponseDto> getGameCategoriesByPromotion(@PathVariable Integer promotionId) {
        List<GameCategoryResponseDto> gameCategoriesDtos = new ArrayList<GameCategoryResponseDto>();
        for (GameCategory gameCategory : gameCategoryService.getGameCategoriesByPromotionId(promotionId)) {
            gameCategoriesDtos.add(new GameCategoryResponseDto(gameCategory));
        }
        return gameCategoriesDtos;
    }

    @PostMapping("/")
    public GameCategoryResponseDto createGameCategory(@Valid @RequestBody GameCategoryRequestDto gameCategoryRequestDto) {
        GameCategory gameCategory = gameCategoryService.createGameCategory(gameCategoryRequestDto);
        return new GameCategoryResponseDto(gameCategory);
    }

    @PutMapping("/{categoryId}")
    public GameCategoryResponseDto updateGameCategory(@PathVariable Integer categoryId,
                                                @RequestBody GameCategoryRequestDto gameCategoryRequestDto) {
        GameCategory gameCategory = gameCategoryService.updateGameCategory(categoryId, gameCategoryRequestDto);
        return new GameCategoryResponseDto(gameCategory);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteGameCategory(@PathVariable Integer categoryId) {
        gameCategoryService.deleteGameCategory(categoryId);
    }
}