package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.PromotionRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.dto.GameCategoryRequestDto;

@SpringBootTest
public class GameCategoryServiceTests {

    @Mock
    private GameCategoryRepository gameCategoryRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameCategoryService gameCategoryService;

    @Test
    public void testcreateGameCategorySuccess() {
        // Arrange
        GameCategoryRequestDto requestDto = new GameCategoryRequestDto(false, "Action");
        requestDto.setCategoryType(GameCategory.CategoryType.GENRE);

        when(gameCategoryRepository.save(any(GameCategory.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        GameCategory response = gameCategoryService.createGameCategory(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getName(), response.getName());
        assertFalse(response.getIsAvailable());
        assertEquals(requestDto.getCategoryType(), response.getCategoryType());
        verify(gameCategoryRepository, times(1)).save(any(GameCategory.class));
    }

    @Test
    public void testCreateGameCategoryWithExistingNameAndType() {
        // Arrange
        GameCategoryRequestDto requestDto = new GameCategoryRequestDto(false, "Action");
        requestDto.setCategoryType(GameCategory.CategoryType.GENRE);
        GameCategory gameCategory = new GameCategory(true, "Action");
        gameCategory.setCategoryType(GameCategory.CategoryType.GENRE);

        // Mock repository behavior to simulate existing game category
        when(gameCategoryRepository.findGameCategoryByName(any(String.class))).thenReturn(gameCategory);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> gameCategoryService.createGameCategory(requestDto));

        // Assert
        assertEquals("Game category already exists.", e.getMessage());
        assertEquals(HttpStatus.CONFLICT, e.getStatus());
    }

    @Test
    public void testUpdateGameCategorySuccess() {
        // Arrange
        GameCategoryRequestDto requestDto = new GameCategoryRequestDto(false, "Romance");
        requestDto.setCategoryType(GameCategory.CategoryType.GENRE);
        GameCategory gameCategory = new GameCategory(true, "Action");
        gameCategory.setCategoryType(GameCategory.CategoryType.CONSOLE);

        when(gameCategoryRepository.findGameCategoryByCategoryId(any(Integer.class))).thenReturn(gameCategory);
        when(gameCategoryRepository.save(any(GameCategory.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Integer id = gameCategory.getCategoryId();
        GameCategory response = gameCategoryService.updateGameCategory(id, requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getName(), response.getName());
        assertFalse(response.getIsAvailable());
        assertEquals(requestDto.getCategoryType(), response.getCategoryType());
        verify(gameCategoryRepository, times(1)).save(gameCategory);
    }

    @Test
    public void testUpdateGameNotFound() {
        // Arrange
        GameCategoryRequestDto requestDto = new GameCategoryRequestDto(false, "Romance");
        requestDto.setCategoryType(GameCategory.CategoryType.GENRE);

        when(gameCategoryRepository.findGameCategoryByCategoryId(any(Integer.class))).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> gameCategoryService.updateGameCategory(1, requestDto));

        // Assert
        assertEquals("Game category not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testDeleteGameCategorySuccess() {
        // Arrange
        GameCategory gameCategory = new GameCategory(true, "Action");
        gameCategory.setCategoryType(GameCategory.CategoryType.CONSOLE);

        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(gameCategory);

        // Act
        gameCategoryService.deleteGameCategory(1);

        // Assert
        verify(gameCategoryRepository, times(1)).delete(gameCategory);
    }

    @Test
    public void testDeleteGameCategoryNotFound() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> gameCategoryService.deleteGameCategory(1));

        // Assert
        assertEquals("Game category with ID 1 not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetGameCategoriesByPromotionIdSuccess() {
        // Arrange
        GameCategory gameCategory = new GameCategory(true, "Action");
        Promotion promotion = new Promotion();
        gameCategory.addPromotion(promotion);

        when(promotionRepository.findPromotionByPromotionId(1)).thenReturn(promotion);
        when(gameCategoryRepository.findByPromotionsContaining(promotion)).thenReturn(List.of(gameCategory));

        // Act
        List<GameCategory> response = gameCategoryService.getGameCategoriesByPromotionId(1);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(gameCategoryRepository, times(1)).findByPromotionsContaining(promotion);
    }

    @Test
    public void testGetGameCategoriesByPromotionIdNotFound() {
        // Arrange
        when(promotionRepository.findPromotionByPromotionId(1)).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> gameCategoryService.getGameCategoriesByPromotionId(1));

        // Assert
        assertEquals("Promotion with ID 1 not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetGameCategoriesByGameIdSuccess() {
        // Arrange
        GameCategory gameCategory = new GameCategory(true, "Action");
        Game game = new Game();
        gameCategory.addGame(game);

        when(gameRepository.findGameByGameEntityId(1)).thenReturn(game);
        when(gameCategoryRepository.findByGamesContaining(game)).thenReturn(List.of(gameCategory));

        // Act
        List<GameCategory> response = gameCategoryService.getGameCategoriesByGameId(1);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(gameCategoryRepository, times(1)).findByGamesContaining(game);
    }

    @Test
    public void testGetGameCategoriesByGameIdNotFound() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> gameCategoryService.getGameCategoriesByGameId(1));

        // Assert
        assertEquals("Game with ID 1 not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetAllGameCategories() {
        // Arrange
        GameCategory gameCategory1 = new GameCategory(true, "Action");
        GameCategory gameCategory2 = new GameCategory(true, "Drama");

        when(gameCategoryRepository.findAll()).thenReturn(Arrays.asList(gameCategory1, gameCategory2));

        // Act
        List<GameCategory> response = gameCategoryService.getAllCategories();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Action", response.get(0).getName());
        assertEquals("Drama", response.get(1).getName());
    }
}