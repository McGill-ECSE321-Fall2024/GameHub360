package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
import ca.mcgill.ecse321.GameShop.exception.GameException;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameCategoryRepository gameCategoryRepository;

    @InjectMocks
    private GameService gameService;

    private Game testGame;
    private GameCategory testCategory;
    private GameRequestDto validGameRequest;

    @BeforeEach
    public void setUp() {
        // Set up test category
        testCategory = new GameCategory(true, "Test Category");
        testCategory.setCategoryType(GameCategory.CategoryType.GENRE);

        // Set up test game
        testGame = new Game();
        testGame.setName("Test Game");
        testGame.setDescription("Test Description");
        testGame.setImageURL("http://test.com/image.jpg");
        testGame.setQuantityInStock(10);
        testGame.setPrice(29.99);
        testGame.setIsAvailable(true);
        testGame.addCategory(testCategory);

        // Set up valid game request
        validGameRequest = new GameRequestDto();
        validGameRequest.setName("Test Game");
        validGameRequest.setDescription("Test Description");
        validGameRequest.setImageUrl("http://test.com/image.jpg");
        validGameRequest.setQuantityInStock(10);
        validGameRequest.setPrice(29.99);
        validGameRequest.setCategoryId(1);
    }

    @Test
    public void testCreateGameSuccessfully() {
        // Arrange
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(testCategory);
        when(gameRepository.findGameByName("Test Game")).thenReturn(null);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        // Act
        GameResponseDto response = gameService.createGame(validGameRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Test Game", response.getName());
        assertEquals(29.99, response.getPrice());
        assertEquals(10, response.getQuantityInStock());
        assertTrue(response.isAvailable());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    public void testCreateGameWithDuplicateName() {
        // Arrange
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(testCategory);
        when(gameRepository.findGameByName("Test Game")).thenReturn(testGame);

        // Act & Assert
        GameException exception = assertThrows(GameException.class,
                () -> gameService.createGame(validGameRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game with this name already exists", exception.getMessage());
    }

    @Test
    public void testCreateGameWithInvalidCategory() {
        // Arrange
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(null);

        // Act & Assert
        GameException exception = assertThrows(GameException.class,
                () -> gameService.createGame(validGameRequest));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    public void testUpdateGameSuccessfully() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(testGame);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        GameRequestDto updateRequest = new GameRequestDto();
        updateRequest.setName("Updated Game");
        updateRequest.setPrice(39.99);

        // Act
        GameResponseDto response = gameService.updateGame(1, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Updated Game", response.getName());
        assertEquals(39.99, response.getPrice());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    public void testUpdateNonExistentGame() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(null);

        // Act & Assert
        GameException exception = assertThrows(GameException.class,
                () -> gameService.updateGame(1, validGameRequest));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game not found", exception.getMessage());
    }

    @Test
    public void testArchiveGameSuccessfully() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(testGame);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        // Act
        GameResponseDto response = gameService.archiveGame(1);

        // Assert
        assertNotNull(response);
        assertFalse(response.isAvailable());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    public void testViewArchivedGames() {
        // Arrange
        Game archivedGame = new Game();
        archivedGame.setIsAvailable(false);
        archivedGame.setName("Archived Game");

        when(gameRepository.findAll()).thenReturn(Arrays.asList(testGame, archivedGame));

        // Act
        List<GameResponseDto> archivedGames = gameService.viewArchivedGames();

        // Assert
        assertNotNull(archivedGames);
        assertEquals(1, archivedGames.size());
        assertEquals("Archived Game", archivedGames.get(0).getName());
    }

    @Test
    public void testSearchGames() {
        // Arrange
        when(gameRepository.findAll()).thenReturn(Arrays.asList(testGame));

        // Act
        List<GameResponseDto> searchResults = gameService.searchGames("Test", null, null, null);

        // Assert
        assertNotNull(searchResults);
        assertEquals(1, searchResults.size());
        assertEquals("Test Game", searchResults.get(0).getName());
    }

    @Test
    public void testUpdateGameStock() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(testGame);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        // Act
        GameResponseDto response = gameService.updateGameStock(1, 20);

        // Assert
        assertNotNull(response);
        assertEquals(20, response.getQuantityInStock());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    public void testUpdateGameStockWithNegativeValue() {
        // Add game ID to testGame
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(testGame);

        // Act & Assert
        GameException exception = assertThrows(GameException.class,
                () -> gameService.updateGameStock(1, -1));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Stock cannot be negative", exception.getMessage());
    }

    @Test
    public void testUpdateGamePrice() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(testGame);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        // Act
        GameResponseDto response = gameService.updateGamePrice(1, 49.99);

        // Assert
        assertNotNull(response);
        assertEquals(49.99, response.getPrice());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    public void testUpdateGamePriceWithInvalidValue() {
        // Add game ID to testGame
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(testGame);

        // Act & Assert
        GameException exception = assertThrows(GameException.class,
                () -> gameService.updateGamePrice(1, 0.0));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Price must be positive", exception.getMessage());
    }
}