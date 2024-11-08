package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameDto;
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
        testGame.setIsAvailable(true);
        testGame.addCategory(testCategory);

        // Set up valid game request
        validGameRequest = new GameRequestDto();
        validGameRequest.setName("Test Game");
        validGameRequest.setDescription("Test Description");
        validGameRequest.setImageUrl("http://test.com/image.jpg");
        validGameRequest.setCategoryId(1);
        validGameRequest.setPrice(29.99);
        validGameRequest.setQuantityInStock(10);
    }

    @Test
    public void testCreateGameSuccessfully() {
        // Arrange
        when(gameRepository.findGameByName("Test Game")).thenReturn(null);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        // Act
        GameDto response = gameService.createGame(validGameRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Test Game", response.getName());
        assertTrue(response.getIsAvailable());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    public void testCreateGameWithDuplicateName() {
        // Arrange
        when(gameRepository.findGameByName("Test Game")).thenReturn(testGame);

        // Act & Assert
        GameException exception = assertThrows(GameException.class,
                () -> gameService.createGame(validGameRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game with this name already exists", exception.getMessage());
    }

    @Test
    public void testUpdateGameSuccessfully() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(testGame);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        GameDto updateRequest = new GameDto();
        updateRequest.setName("Updated Game");

        // Act
        GameDto response = gameService.updateGame(1, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Updated Game", response.getName());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    public void testUpdateNonExistentGame() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(null);
        GameDto updateRequest = new GameDto();
        updateRequest.setName("Updated Game");

        // Act & Assert
        GameException exception = assertThrows(GameException.class,
                () -> gameService.updateGame(1, updateRequest));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game not found", exception.getMessage());
    }

    @Test
    public void testArchiveGameSuccessfully() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(testGame);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        // Act
        GameDto response = gameService.archiveGame(1);

        // Assert
        assertNotNull(response);
        assertFalse(response.getIsAvailable());
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
        List<GameDto> archivedGames = gameService.viewArchivedGames();

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
        List<GameDto> searchResults = gameService.searchGames("Test", null, null, null);

        // Assert
        assertNotNull(searchResults);
        assertEquals(1, searchResults.size());
        assertEquals("Test Game", searchResults.get(0).getName());
    }
}