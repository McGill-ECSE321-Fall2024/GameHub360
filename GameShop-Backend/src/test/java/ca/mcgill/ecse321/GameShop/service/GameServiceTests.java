package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;

@SpringBootTest
public class GameServiceTests {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameCategoryRepository gameCategoryRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    public void testCreateGame_Success() {
        // Arrange
        GameRequestDto gameRequestDto = new GameRequestDto();
        gameRequestDto.setName("Test Game");
        gameRequestDto.setDescription("Test Description");
        gameRequestDto.setImageURL("http://testimage.com/image.jpg");
        gameRequestDto.setQuantityInStock(100);
        gameRequestDto.setIsAvailable(true);
        gameRequestDto.setPrice(59.99);
        gameRequestDto.setCategoryIds(Arrays.asList(1, 2));

        GameCategory category1 = new GameCategory(true, "Action");
        GameCategory category2 = new GameCategory(true, "Adventure");
        ReflectionTestUtils.setField(category1, "categoryId", 1);
        ReflectionTestUtils.setField(category2, "categoryId", 2);

        when(gameRepository.findGameByName("Test Game")).thenReturn(null);
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(category1);
        when(gameCategoryRepository.findGameCategoryByCategoryId(2)).thenReturn(category2);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> {
            Game savedGame = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedGame, "gameEntityId", 1);
            savedGame.setCategories(new GameCategory[] { category1, category2 });
            return savedGame;
        });

        // Act
        Game createdGame = gameService.createGame(gameRequestDto);

        // Assert
        assertNotNull(createdGame);
        assertEquals("Test Game", createdGame.getName());
        assertEquals("Test Description", createdGame.getDescription());
        assertEquals("http://testimage.com/image.jpg", createdGame.getImageURL());
        assertEquals(100, createdGame.getQuantityInStock());
        assertTrue(createdGame.getIsAvailable());
        assertEquals(59.99, createdGame.getPrice());
        assertEquals(2, createdGame.getCategories().size());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testCreateGame_GameAlreadyExists() {
        // Arrange
        GameRequestDto gameRequestDto = new GameRequestDto();
        gameRequestDto.setName("Existing Game");
        gameRequestDto.setDescription("Existing Description");
        gameRequestDto.setImageURL("http://existingimage.com/image.jpg");
        gameRequestDto.setQuantityInStock(50);
        gameRequestDto.setIsAvailable(true);
        gameRequestDto.setPrice(39.99);
        gameRequestDto.setCategoryIds(Arrays.asList(1));

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", 1);
        existingGame.setName("Existing Game");

        when(gameRepository.findGameByName("Existing Game")).thenReturn(existingGame);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(gameRequestDto);
        });

        assertEquals("Game with this name already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testUpdateGame_Success() {
        // Arrange
        Integer gameId = 1;
        Game gameDto = new Game();
        gameDto.setName("Updated Game");
        gameDto.setDescription("Updated Description");
        gameDto.setImageURL("http://updatedimage.com/image.jpg");
        gameDto.setIsAvailable(false);
        gameDto.setPrice(49.99);
        gameDto.setQuantityInStock(80);

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setName("Original Game");
        existingGame.setDescription("Original Description");
        existingGame.setImageURL("http://originalimage.com/image.jpg");
        existingGame.setIsAvailable(true);
        existingGame.setPrice(59.99);
        existingGame.setQuantityInStock(100);

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);
        when(gameRepository.findGameByName("Updated Game")).thenReturn(null);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Game updatedGame = gameService.updateGame(gameId, gameDto);

        // Assert
        assertNotNull(updatedGame);
        assertEquals("Updated Game", updatedGame.getName());
        assertEquals("Updated Description", updatedGame.getDescription());
        assertEquals("http://updatedimage.com/image.jpg", updatedGame.getImageURL());
        assertFalse(updatedGame.getIsAvailable());
        assertEquals(49.99, updatedGame.getPrice());
        assertEquals(80, updatedGame.getQuantityInStock());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testUpdateGame_GameNotFound() {
        // Arrange
        Integer gameId = 1;
        Game gameDto = new Game();

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.updateGame(gameId, gameDto);
        });

        assertEquals("Game not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testUpdateGame_GameNameAlreadyExists() {
        // Arrange
        Integer gameId = 1;
        Game gameDto = new Game();
        gameDto.setName("Existing Game");

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setName("Original Game");

        Game anotherGame = new Game();
        ReflectionTestUtils.setField(anotherGame, "gameEntityId", 2);
        anotherGame.setName("Existing Game");

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);
        when(gameRepository.findGameByName("Existing Game")).thenReturn(anotherGame);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.updateGame(gameId, gameDto);
        });

        assertEquals("Game with this name already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testArchiveGame_Success() {
        // Arrange
        Integer gameId = 1;
        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setIsAvailable(true);

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Game archivedGame = gameService.archiveGame(gameId);

        // Assert
        assertNotNull(archivedGame);
        assertFalse(archivedGame.getIsAvailable());
        verify(gameRepository, times(1)).save(existingGame);
    }

    @Test
    public void testArchiveGame_GameNotFound() {
        // Arrange
        Integer gameId = 1;

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.archiveGame(gameId);
        });

        assertEquals("Game not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testViewArchivedGames_Success() {
        // Arrange
        Game archivedGame1 = new Game();
        ReflectionTestUtils.setField(archivedGame1, "gameEntityId", 1);
        archivedGame1.setIsAvailable(false);

        Game archivedGame2 = new Game();
        ReflectionTestUtils.setField(archivedGame2, "gameEntityId", 2);
        archivedGame2.setIsAvailable(false);

        Game availableGame = new Game();
        ReflectionTestUtils.setField(availableGame, "gameEntityId", 3);
        availableGame.setIsAvailable(true);

        when(gameRepository.findAll()).thenReturn(Arrays.asList(archivedGame1, archivedGame2, availableGame));

        // Act
        List<Game> archivedGames = gameService.viewArchivedGames();

        // Assert
        assertNotNull(archivedGames);
        assertEquals(2, archivedGames.size());
        assertFalse(archivedGames.get(0).getIsAvailable());
        assertFalse(archivedGames.get(1).getIsAvailable());
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    public void testReactivateArchivedGame_Success() {
        // Arrange
        Integer gameId = 1;
        Game archivedGame = new Game();
        ReflectionTestUtils.setField(archivedGame, "gameEntityId", gameId);
        archivedGame.setIsAvailable(false);

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(archivedGame);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Game reactivatedGame = gameService.reactivateArchivedGame(gameId);

        // Assert
        assertNotNull(reactivatedGame);
        assertTrue(reactivatedGame.getIsAvailable());
        verify(gameRepository, times(1)).save(reactivatedGame);
    }

    @Test
    public void testReactivateArchivedGame_GameNotFound() {
        // Arrange
        Integer gameId = 1;

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.reactivateArchivedGame(gameId);
        });

        assertEquals("Game not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testReactivateArchivedGame_GameAlreadyActive() {
        // Arrange
        Integer gameId = 1;
        Game activeGame = new Game();
        ReflectionTestUtils.setField(activeGame, "gameEntityId", gameId);
        activeGame.setIsAvailable(true);

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(activeGame);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.reactivateArchivedGame(gameId);
        });

        assertEquals("Game is already active", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testBrowseGames_Success_WithAllFilters() {
        // Arrange
        String category = "Action";
        Double minPrice = 20.0;
        Double maxPrice = 60.0;

        GameCategory actionCategory = new GameCategory(true, "Action");
        ReflectionTestUtils.setField(actionCategory, "categoryId", 1);

        Game game1 = new Game();
        ReflectionTestUtils.setField(game1, "gameEntityId", 1);
        game1.setIsAvailable(true);
        game1.setPrice(50.0);
        game1.addCategory(actionCategory);

        Game game2 = new Game();
        ReflectionTestUtils.setField(game2, "gameEntityId", 2);
        game2.setIsAvailable(true);
        game2.setPrice(30.0);
        game2.addCategory(actionCategory);

        Game game3 = new Game();
        ReflectionTestUtils.setField(game3, "gameEntityId", 3);
        game3.setIsAvailable(true);
        game3.setPrice(70.0); // Outside maxPrice
        game3.addCategory(actionCategory);

        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2, game3));

        // Act
        List<Game> filteredGames = gameService.browseGames(category, minPrice, maxPrice);

        // Assert
        assertNotNull(filteredGames);
        assertEquals(2, filteredGames.size());
        assertTrue(filteredGames.contains(game1));
        assertTrue(filteredGames.contains(game2));
        assertFalse(filteredGames.contains(game3));
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    public void testBrowseGames_Success_NoFilters() {
        // Arrange
        GameCategory actionCategory = new GameCategory(true, "Action");
        GameCategory adventureCategory = new GameCategory(true, "Adventure");
        ReflectionTestUtils.setField(actionCategory, "categoryId", 1);
        ReflectionTestUtils.setField(adventureCategory, "categoryId", 2);

        Game game1 = new Game();
        ReflectionTestUtils.setField(game1, "gameEntityId", 1);
        game1.setIsAvailable(true);
        game1.setPrice(50.0);
        game1.addCategory(actionCategory);

        Game game2 = new Game();
        ReflectionTestUtils.setField(game2, "gameEntityId", 2);
        game2.setIsAvailable(true);
        game2.setPrice(30.0);
        game2.addCategory(adventureCategory);

        Game game3 = new Game();
        ReflectionTestUtils.setField(game3, "gameEntityId", 3);
        game3.setIsAvailable(false); // Not available
        game3.setPrice(70.0);
        game3.addCategory(actionCategory);

        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2, game3));

        // Act
        List<Game> filteredGames = gameService.browseGames(null, null, null);

        // Assert
        assertNotNull(filteredGames);
        assertEquals(2, filteredGames.size());
        assertTrue(filteredGames.contains(game1));
        assertTrue(filteredGames.contains(game2));
        assertFalse(filteredGames.contains(game3));
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    public void testSearchGames_Success() {
        // Arrange
        String query = "Adventure";
        String category = "Adventure";
        Double minPrice = 20.0;
        Double maxPrice = 60.0;

        GameCategory adventureCategory = new GameCategory(true, "Adventure");
        ReflectionTestUtils.setField(adventureCategory, "categoryId", 1);

        Game game1 = new Game();
        ReflectionTestUtils.setField(game1, "gameEntityId", 1);
        game1.setName("Adventure Quest");
        game1.setDescription("An exciting adventure game.");
        game1.setIsAvailable(true);
        game1.setPrice(50.0);
        game1.addCategory(adventureCategory);

        Game game2 = new Game();
        ReflectionTestUtils.setField(game2, "gameEntityId", 2);
        game2.setName("Mystery Island");
        game2.setDescription("Solve mysteries on the island.");
        game2.setIsAvailable(true);
        game2.setPrice(30.0);
        game2.addCategory(adventureCategory);

        Game game3 = new Game();
        ReflectionTestUtils.setField(game3, "gameEntityId", 3);
        game3.setName("Space Odyssey");
        game3.setDescription("Explore the cosmos.");
        game3.setIsAvailable(true);
        game3.setPrice(70.0);
        game3.addCategory(adventureCategory);

        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2, game3));

        // Act
        List<Game> searchResults = gameService.searchGames(query, category, minPrice, maxPrice);

        // Assert
        assertNotNull(searchResults);
        assertEquals(2, searchResults.size());
        assertTrue(searchResults.contains(game1));
        assertTrue(searchResults.contains(game2));
        assertFalse(searchResults.contains(game3));
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    public void testAddGameToCategory_Success() {
        // Arrange
        Integer gameId = 1;
        Integer categoryId = 2;

        GameCategory newCategory = new GameCategory(true, "Strategy");
        ReflectionTestUtils.setField(newCategory, "categoryId", categoryId);

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setCategories(new GameCategory[] {});

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);
        when(gameCategoryRepository.findGameCategoryByCategoryId(categoryId)).thenReturn(newCategory);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Game updatedGame = gameService.addGameToCategory(gameId, categoryId);

        // Assert
        assertNotNull(updatedGame);
        assertEquals(1, updatedGame.getCategories().size());
        assertTrue(updatedGame.getCategories().contains(newCategory));
        verify(gameRepository, times(1)).save(existingGame);
    }

    @Test
    public void testAddGameToCategory_GameNotFound() {
        // Arrange
        Integer gameId = 1;
        Integer categoryId = 2;

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.addGameToCategory(gameId, categoryId);
        });

        assertEquals("Game not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testAddGameToCategory_CategoryNotFound() {
        // Arrange
        Integer gameId = 1;
        Integer categoryId = 2;

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setCategories(new GameCategory[] {});

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);
        when(gameCategoryRepository.findGameCategoryByCategoryId(categoryId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.addGameToCategory(gameId, categoryId);
        });

        assertEquals("Category not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testAddGameToCategory_GameAlreadyInCategory() {
        // Arrange
        Integer gameId = 1;
        Integer categoryId = 2;

        GameCategory existingCategory = new GameCategory(true, "Strategy");
        ReflectionTestUtils.setField(existingCategory, "categoryId", categoryId);

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setCategories(new GameCategory[] { existingCategory });

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);
        when(gameCategoryRepository.findGameCategoryByCategoryId(categoryId)).thenReturn(existingCategory);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.addGameToCategory(gameId, categoryId);
        });

        assertEquals("Game already in this category", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testUpdateGameStock_Success() {
        // Arrange
        Integer gameId = 1;
        Integer newStock = 120;

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setQuantityInStock(100);

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Game updatedGame = gameService.updateGameStock(gameId, newStock);

        // Assert
        assertNotNull(updatedGame);
        assertEquals(newStock, updatedGame.getQuantityInStock());
        verify(gameRepository, times(1)).save(existingGame);
    }

    @Test
    public void testUpdateGameStock_GameNotFound() {
        // Arrange
        Integer gameId = 1;
        Integer newStock = 120;

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.updateGameStock(gameId, newStock);
        });

        assertEquals("Game not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testUpdateGameStock_InvalidStock() {
        // Arrange
        Integer gameId = 1;
        Integer newStock = -10;

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setQuantityInStock(100);

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.updateGameStock(gameId, newStock);
        });

        assertEquals("Stock cannot be negative", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testUpdateGamePrice_Success() {
        // Arrange
        Integer gameId = 1;
        Double newPrice = 69.99;

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setPrice(59.99);

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Game updatedGame = gameService.updateGamePrice(gameId, newPrice);

        // Assert
        assertNotNull(updatedGame);
        assertEquals(newPrice, updatedGame.getPrice());
        verify(gameRepository, times(1)).save(existingGame);
    }

    @Test
    public void testUpdateGamePrice_GameNotFound() {
        // Arrange
        Integer gameId = 1;
        Double newPrice = 69.99;

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.updateGamePrice(gameId, newPrice);
        });

        assertEquals("Game not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testUpdateGamePrice_InvalidPrice() {
        // Arrange
        Integer gameId = 1;
        Double newPrice = -20.0;

        Game existingGame = new Game();
        ReflectionTestUtils.setField(existingGame, "gameEntityId", gameId);
        existingGame.setPrice(59.99);

        when(gameRepository.findGameByGameEntityId(gameId)).thenReturn(existingGame);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.updateGamePrice(gameId, newPrice);
        });

        assertEquals("Price must be positive", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(gameRepository, never()).save(any(Game.class));
    }
}