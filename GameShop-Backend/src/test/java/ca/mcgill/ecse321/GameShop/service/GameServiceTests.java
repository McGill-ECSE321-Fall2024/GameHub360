package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
import ca.mcgill.ecse321.GameShop.exception.GameException;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class GameServiceTests {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameCategoryRepository gameCategoryRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    public void testCreateGameSuccess() {
        // Arrange
        GameRequestDto requestDto = new GameRequestDto();
        requestDto.setName("Chess");
        requestDto.setPrice(19.99);
        requestDto.setQuantityInStock(10);
        GameCategory category = new GameCategory(true, "Board Games");
        when(gameCategoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));

        Game game = new Game("Chess", "A classic game", "image_url", 10, true, 19.99, category);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        // Act
        GameResponseDto response = gameService.createGame(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals("Chess", response.getName());
        assertEquals(19.99, response.getPrice());
        assertEquals(10, response.getQuantityInStock());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testCreateGameWithExistingTitle() {
        // Arrange
        GameRequestDto requestDto = new GameRequestDto();
        requestDto.setName("Chess");
        requestDto.setPrice(19.99);
        requestDto.setQuantityInStock(10);
        GameCategory category = new GameCategory(true, "Board Games");
        when(gameCategoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));

        Game existingGame = new Game("Chess", "A classic game", "image_url", 10, true, 19.99, category);
        when(gameRepository.findGameByName("Chess")).thenReturn(existingGame);

        // Act
        GameException e = assertThrows(GameException.class, () -> gameService.createGame(requestDto));

        // Assert
        assertEquals("Game with this title already exists.", e.getMessage());
        assertEquals(HttpStatus.CONFLICT, e.getStatus());
    }

    @Test
    public void testUpdateGameSuccess() {
        // Arrange
        GameRequestDto requestDto = new GameRequestDto();
        requestDto.setName("Chess");
        requestDto.setPrice(25.99);
        requestDto.setQuantityInStock(15);
        GameCategory category = new GameCategory(true, "Board Games");
        when(gameCategoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));

        Game game = new Game("Chess", "A classic game", "image_url", 10, true, 19.99, category);
        when(gameRepository.findById(any(Integer.class))).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        // Act
        GameResponseDto response = gameService.updateGame(1, requestDto);

        // Assert
        assertNotNull(response);
        assertEquals("Chess", response.getName());
        assertEquals(25.99, response.getPrice());
        assertEquals(15, response.getQuantityInStock());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testUpdateGameNotFound() {
        // Arrange
        GameRequestDto requestDto = new GameRequestDto();
        requestDto.setName("Chess");
        requestDto.setPrice(25.99);
        requestDto.setQuantityInStock(15);
        when(gameRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // Act
        GameException e = assertThrows(GameException.class, () -> gameService.updateGame(1, requestDto));

        // Assert
        assertEquals("Game not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testDeleteGameSuccess() {
        // Arrange
        GameCategory category = new GameCategory(true, "Board Games");
        Game game = new Game("Chess", "A classic game", "image_url", 10, true, 19.99, category);
        when(gameRepository.findById(any(Integer.class))).thenReturn(Optional.of(game));

        // Act
        gameService.deleteGame(1);

        // Assert
        verify(gameRepository, times(1)).delete(game);
    }

    @Test
    public void testDeleteGameNotFound() {
        // Arrange
        when(gameRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // Act
        GameException e = assertThrows(GameException.class, () -> gameService.deleteGame(1));

        // Assert
        assertEquals("Game not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetGameByIdSuccess() {
        // Arrange
        GameCategory category = new GameCategory(true, "Board Games");
        Game game = new Game("Chess", "A classic game", "image_url", 10, true, 19.99, category);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        // Act
        GameResponseDto response = gameService.getGameById(1);

        // Assert
        assertNotNull(response);
        assertEquals("Chess", response.getName());
        assertEquals(19.99, response.getPrice());
        assertEquals(10, response.getQuantityInStock());
    }

    @Test
    public void testGetGameByIdNotFound() {
        // Arrange
        when(gameRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        GameException e = assertThrows(GameException.class, () -> gameService.getGameById(1));

        // Assert
        assertEquals("Game not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testUpdateGameStockSuccess() {
        // Arrange
        GameCategory category = new GameCategory(true, "Board Games");
        Game game = new Game("Chess", "A classic game", "image_url", 10, true, 19.99, category);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        // Act
        GameResponseDto response = gameService.updateGameStock(1, 20);

        // Assert
        assertNotNull(response);
        assertEquals(20, response.getQuantityInStock());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testUpdateGamePriceSuccess() {
        // Arrange
        GameCategory category = new GameCategory(true, "Board Games");
        Game game = new Game("Chess", "A classic game", "image_url", 10, true, 19.99, category);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        // Act
        GameResponseDto response = gameService.updateGamePrice(1, 29.99);

        // Assert
        assertNotNull(response);
        assertEquals(29.99, response.getPrice());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testFindAllGames() {
        // Arrange
        GameCategory category = new GameCategory(true, "Board Games");
        when(gameCategoryRepository.save(any(GameCategory.class))).thenReturn(category);

        Game game1 = new Game("Chess", "A classic game", "image_url", 10, true, 19.99, category);
        Game game2 = new Game("Checkers", "Another classic game", "image_url", 5, true, 9.99, category);

        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2));

        // Act
        List<GameResponseDto> games = gameService.findAllGames();

        // Assert
        assertNotNull(games);
        assertEquals(2, games.size());
        assertEquals("Chess", games.get(0).getName());
        assertEquals("Checkers", games.get(1).getName());
    }
}