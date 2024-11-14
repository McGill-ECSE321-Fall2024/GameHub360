package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.mcgill.ecse321.GameShop.dto.PromotionRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.PromotionRepository;
import ca.mcgill.ecse321.GameShop.repository.StoreInformationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PromotionServiceTests {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private StoreInformationRepository storeInformationRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameCategoryRepository gameCategoryRepository;

    @InjectMocks
    private PromotionService promotionService;

    @Test
    public void testCreatePromotionSuccess_GameType() {
        // Arrange
        PromotionRequestDto requestDto = new PromotionRequestDto(
                Promotion.PromotionType.GAME, 10.0, List.of(1), Collections.emptyList());
        StoreInformation storeInfo = new StoreInformation();
        Game game = new Game();

        when(storeInformationRepository.findFirstByOrderByStoreInfoIdAsc()).thenReturn(storeInfo);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Promotion response = promotionService.createPromotion(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(Promotion.PromotionType.GAME, response.getPromotionType());
        assertEquals(10.0, response.getDiscountPercentageValue());
        assertEquals(1, response.getPromotedGames().size());
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionSuccess_CategoryType() {
        // Arrange
        PromotionRequestDto requestDto = new PromotionRequestDto(
                Promotion.PromotionType.CATEGORY, 15.0, Collections.emptyList(), List.of(2));
        StoreInformation storeInfo = new StoreInformation();
        GameCategory category = new GameCategory();

        when(storeInformationRepository.findFirstByOrderByStoreInfoIdAsc()).thenReturn(storeInfo);
        when(gameCategoryRepository.findById(2)).thenReturn(Optional.of(category));
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Promotion response = promotionService.createPromotion(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(Promotion.PromotionType.CATEGORY, response.getPromotionType());
        assertEquals(15.0, response.getDiscountPercentageValue());
        assertEquals(1, response.getPromotedCategories().size());
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionStoreInfoNotFound() {
        // Arrange
        PromotionRequestDto requestDto = new PromotionRequestDto(
                Promotion.PromotionType.GAME, 10.0, List.of(1), Collections.emptyList());

        when(storeInformationRepository.findFirstByOrderByStoreInfoIdAsc()).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> promotionService.createPromotion(requestDto));

        // Assert
        assertEquals("Store information not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testCreatePromotionGameNotFound() {
        // Arrange
        PromotionRequestDto requestDto = new PromotionRequestDto(
                Promotion.PromotionType.GAME, 10.0, List.of(1), Collections.emptyList());
        StoreInformation storeInfo = new StoreInformation();

        when(storeInformationRepository.findFirstByOrderByStoreInfoIdAsc()).thenReturn(storeInfo);
        when(gameRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> promotionService.createPromotion(requestDto));

        // Assert
        assertEquals("Game with ID 1 not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testUpdatePromotionSuccess_GameType() {
        // Arrange
        PromotionRequestDto requestDto = new PromotionRequestDto(
                Promotion.PromotionType.GAME, 20.0, List.of(1, 2), Collections.emptyList());
        Promotion existingPromotion = new Promotion();
        existingPromotion.setPromotionType(Promotion.PromotionType.GAME);
        existingPromotion.setDiscountPercentageValue(15.0);

        Game game1 = new Game();
        Game game2 = new Game();

        when(promotionRepository.findPromotionByPromotionId(1)).thenReturn(existingPromotion);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game1));
        when(gameRepository.findById(2)).thenReturn(Optional.of(game2));
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Promotion response = promotionService.updatePromotion(1, requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(Promotion.PromotionType.GAME, response.getPromotionType());
        assertEquals(20.0, response.getDiscountPercentageValue());
        assertEquals(2, response.getPromotedGames().size());
        verify(promotionRepository, times(1)).save(existingPromotion);
    }

    @Test
    public void testUpdatePromotionNotFound() {
        // Arrange
        PromotionRequestDto requestDto = new PromotionRequestDto(
                Promotion.PromotionType.CATEGORY, 10.0, Collections.emptyList(), List.of(3));

        when(promotionRepository.findPromotionByPromotionId(1)).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> promotionService.updatePromotion(1, requestDto));

        // Assert
        assertEquals("Promotion with ID 1 not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testDeletePromotionSuccess() {
        // Arrange
        Promotion promotion = new Promotion();
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));

        // Act
        promotionService.deletePromotion(1);

        // Assert
        verify(promotionRepository, times(1)).delete(promotion);
    }

    @Test
    public void testDeletePromotionNotFound() {
        // Arrange
        when(promotionRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        GameShopException e = assertThrows(GameShopException.class, () -> promotionService.deletePromotion(1));
        assertEquals("Promotion with ID 1 not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetPromotionsByGameIdSuccess() {
        // Arrange
        Game game = new Game();
        Promotion promotion = new Promotion();
        promotion.addPromotedGame(game);

        when(gameRepository.findGameByGameEntityId(1)).thenReturn(game);
        when(promotionRepository.findByPromotedGamesContaining(game)).thenReturn(List.of(promotion));

        // Act
        List<Promotion> response = promotionService.getPromotionsByGameId(1);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(promotionRepository, times(1)).findByPromotedGamesContaining(game);
    }

    @Test
    public void testGetPromotionsByGameIdNotFound() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> promotionService.getPromotionsByGameId(1));

        // Assert
        assertEquals("Game with ID 1 not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetPromotionsByCategoryIdSuccess() {
        // Arrange
        GameCategory category = new GameCategory();
        Promotion promotion = new Promotion();
        promotion.addPromotedCategory(category);

        when(gameCategoryRepository.findGameCategoryByCategoryId(2)).thenReturn(category);
        when(promotionRepository.findByPromotedCategoriesContaining(category)).thenReturn(List.of(promotion));

        // Act
        List<Promotion> response = promotionService.getPromotionsByCategoryId(2);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(promotionRepository, times(1)).findByPromotedCategoriesContaining(category);
    }

    @Test
    public void testGetPromotionsByCategoryIdNotFound() {
        // Arrange
        when(gameCategoryRepository.findGameCategoryByCategoryId(2)).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> promotionService.getPromotionsByCategoryId(2));

        // Assert
        assertEquals("Category with ID 2 not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }
}
