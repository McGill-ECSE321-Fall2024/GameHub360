package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameListDto;

import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    private Integer validCategoryId;

    @BeforeAll
    public void setUp() {
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();

        GameCategory category = new GameCategory(true, "Action Games");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        validCategoryId = gameCategoryRepository.save(category).getCategoryId();
    }

    @AfterAll
    public void cleanUp() {
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateGame() {
        // Arrange
        GameRequestDto gameDto = new GameRequestDto();
        gameDto.setName("Test Game");
        gameDto.setDescription("This is a detailed description");
        gameDto.setImageUrl("https://example.com/game-image.jpg");
        gameDto.setPrice(29.99);
        gameDto.setQuantityInStock(10);
        gameDto.setIsAvailable(true); // Assuming new games are available by default
        gameDto.setCategoryIds(List.of(validCategoryId)); // Use List.of for single category

        // Act
        ResponseEntity<GameResponseDto> response = client.postForEntity("/games/", gameDto, GameResponseDto.class);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game creation failed");
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Test Game", responseBody.getName(), "Game name should match");
        assertEquals("This is a detailed description", responseBody.getDescription(), "Game description should match");
        assertEquals("https://example.com/game-image.jpg", responseBody.getImageUrl(), "Game image URL should match");
        assertEquals(29.99, responseBody.getPrice(), 0.01, "Game price should match");
        assertEquals(10, responseBody.getQuantityInStock(), "Game quantity should match");
        assertTrue(responseBody.getCategoryIds().contains(validCategoryId), "Game category should match");
        assertTrue(responseBody.isAvailable(), "New game should be available");
    }

    @Test
    @Order(2)
    public void testUpdateGame() {
        // Arrange
        GameRequestDto gameDto = new GameRequestDto();
        gameDto.setName("Initial Game");
        gameDto.setDescription("Initial description");
        gameDto.setImageUrl("https://example.com/initial-image.jpg");
        gameDto.setPrice(29.99);
        gameDto.setQuantityInStock(10);
        gameDto.setIsAvailable(true);
        gameDto.setCategoryIds(List.of(validCategoryId));

        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games/", gameDto, GameResponseDto.class);
        assertNotNull(createResponse, "Create response should not be null");
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        GameResponseDto initialGame = createResponse.getBody();
        assertNotNull(initialGame, "Initial game should not be null");
        Integer gameId = initialGame.getGameId();
        assertNotNull(gameId, "Game ID should not be null");

        // Prepare update request DTO
        GameRequestDto updateRequest = new GameRequestDto();
        updateRequest.setName("Updated Game");
        updateRequest.setDescription("Updated description");
        updateRequest.setImageUrl("https://example.com/updated-image.jpg");
        updateRequest.setQuantityInStock(20);
        updateRequest.setPrice(39.99);
        updateRequest.setIsAvailable(true);
        updateRequest.setCategoryIds(List.of(validCategoryId)); // Update DTO needs category IDs

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/" + gameId,
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                GameResponseDto.class);

        // Assert
        assertNotNull(response, "Update response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game update failed");
        GameResponseDto updatedGame = response.getBody();
        assertNotNull(updatedGame, "Updated game should not be null");
        assertEquals("Updated Game", updatedGame.getName(), "Game name should be updated");
        assertEquals("Updated description", updatedGame.getDescription(), "Game description should be updated");
        assertEquals("https://example.com/updated-image.jpg", updatedGame.getImageUrl(), "Game image should be updated");
        assertEquals(20, updatedGame.getQuantityInStock(), "Game quantity should be updated");
        assertEquals(39.99, updatedGame.getPrice(), 0.01, "Game price should be updated");
        assertTrue(updatedGame.isAvailable(), "Game should be available");
        assertTrue(updatedGame.getCategoryIds().contains(validCategoryId), "Game category should match");
    }

    @Test
    @Order(3)
    public void testArchiveGame() {
        // Arrange
        GameRequestDto gameDto = new GameRequestDto();
        gameDto.setName("Archive Test Game");
        gameDto.setDescription("Test description");
        gameDto.setImageUrl("https://example.com/game-image.jpg");
        gameDto.setPrice(29.99);
        gameDto.setQuantityInStock(10);
        gameDto.setIsAvailable(true);
        gameDto.setCategoryIds(List.of(validCategoryId));

        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games/", gameDto, GameResponseDto.class);
        assertNotNull(createResponse, "Create response should not be null");
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        GameResponseDto initialGame = createResponse.getBody();
        assertNotNull(initialGame, "Initial game should not be null");
        Integer gameId = initialGame.getGameId();
        assertNotNull(gameId, "Game ID should not be null");
        assertTrue(initialGame.isAvailable(), "Game should initially be available");

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Assert
        assertNotNull(response, "Archive response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game archiving failed");
        GameResponseDto archivedGame = response.getBody();
        assertNotNull(archivedGame, "Archived game should not be null");
        assertFalse(archivedGame.isAvailable(), "Game should be archived");
        assertEquals(gameId, archivedGame.getGameId(), "Game ID should match");
        assertEquals("Archive Test Game", archivedGame.getName(), "Game name should not change");
    }

    @Test
    @Order(4)
    public void testViewArchivedGames() {
        // Arrange
        GameRequestDto gameDto = new GameRequestDto();
        gameDto.setName("Archive View Test Game");
        gameDto.setDescription("Test description");
        gameDto.setImageUrl("https://example.com/game-image.jpg");
        gameDto.setPrice(29.99);
        gameDto.setQuantityInStock(10);
        gameDto.setIsAvailable(true);
        gameDto.setCategoryIds(List.of(validCategoryId));

        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games/", gameDto, GameResponseDto.class);
        assertNotNull(createResponse, "Create response should not be null");
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        GameResponseDto initialGame = createResponse.getBody();
        assertNotNull(initialGame, "Initial game should not be null");
        Integer gameId = initialGame.getGameId();

        // Archive the game
        ResponseEntity<GameResponseDto> archiveResponse = client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                null,
                GameResponseDto.class);
        assertNotNull(archiveResponse, "Archive response should not be null");
        assertEquals(HttpStatus.OK, archiveResponse.getStatusCode(), "Game archiving failed");

        // Act
        ResponseEntity<GameListDto> response = client.getForEntity("/games/archive", GameListDto.class);

        // Assert
        assertNotNull(response, "View archived response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Viewing archived games failed");
        
        GameListDto gameList = response.getBody();
        assertNotNull(gameList, "Game list should not be null");
        List<GameResponseDto> archivedGames = gameList.getGames();
        assertNotNull(archivedGames, "Archived games list should not be null");
        assertFalse(archivedGames.isEmpty(), "Archived games list should not be empty");
        
        boolean foundArchivedGame = false;
        for (GameResponseDto game : archivedGames) {
            assertFalse(game.isAvailable(), "All games should be archived");
            if (game.getGameId().equals(gameId)) {
                foundArchivedGame = true;
                assertEquals("Archive View Test Game", game.getName(), "Game name should match");
            }
        }
        assertTrue(foundArchivedGame, "Recently archived game should be in the list");
    }

    @Test
    @Order(5)
    public void testBrowseGames() {
        // Arrange
        GameRequestDto gameDto = new GameRequestDto();
        gameDto.setName("Browse Test Game");
        gameDto.setDescription("Test description");
        gameDto.setImageUrl("https://example.com/game-image.jpg");
        gameDto.setPrice(29.99);
        gameDto.setQuantityInStock(10);
        gameDto.setIsAvailable(true);
        gameDto.setCategoryIds(List.of(validCategoryId));

        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games/", gameDto, GameResponseDto.class);
        assertNotNull(createResponse, "Create response should not be null");
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        GameResponseDto createdGame = createResponse.getBody();
        assertNotNull(createdGame, "Created game should not be null");
        Integer gameId = createdGame.getGameId();

        // Act
        ResponseEntity<GameListDto> response = client.getForEntity("/games", GameListDto.class);

        // Assert
        assertNotNull(response, "Browse response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Browsing games failed");
        
        GameListDto gameList = response.getBody();
        assertNotNull(gameList, "Game list should not be null");
        List<GameResponseDto> games = gameList.getGames();
        assertNotNull(games, "Games list should not be null");
        assertFalse(games.isEmpty(), "Games list should not be empty");
        
        boolean foundNewGame = false;
        for (GameResponseDto game : games) {
            assertTrue(game.isAvailable(), "All browsed games should be available");
            if (game.getGameId().equals(gameId)) {
                foundNewGame = true;
                assertEquals("Browse Test Game", game.getName(), "Game name should match");
                assertEquals(29.99, game.getPrice(), 0.01, "Game price should match");
                assertEquals(10, game.getQuantityInStock(), "Game quantity should match");
            }
        }
        assertTrue(foundNewGame, "Recently created game should be in the browse list");
    }

    @Test
    @Order(6)
    public void testSearchGames() {
        // Arrange
        String uniqueGameName = "Unique Game Name " + System.currentTimeMillis();
        GameRequestDto gameDto = new GameRequestDto();
        gameDto.setName(uniqueGameName);
        gameDto.setDescription("Test description");
        gameDto.setImageUrl("https://example.com/game-image.jpg");
        gameDto.setPrice(29.99);
        gameDto.setQuantityInStock(10);
        gameDto.setIsAvailable(true);
        gameDto.setCategoryIds(List.of(validCategoryId));

        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games/", gameDto, GameResponseDto.class);
        assertNotNull(createResponse, "Create response should not be null");
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        GameResponseDto createdGame = createResponse.getBody();
        assertNotNull(createdGame, "Created game should not be null");
        Integer gameId = createdGame.getGameId();

        // Act
        ResponseEntity<GameListDto> response = client.getForEntity(
            "/games/search?query=" + uniqueGameName, 
            GameListDto.class
        );

        // Assert
        assertNotNull(response, "Search response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game search failed");
        
        GameListDto gameList = response.getBody();
        assertNotNull(gameList, "Game list should not be null");
        List<GameResponseDto> games = gameList.getGames();
        assertNotNull(games, "Search results should not be null");
        assertFalse(games.isEmpty(), "Search results should not be empty");
        
        boolean foundSearchedGame = false;
        for (GameResponseDto game : games) {
            if (game.getName().equals(uniqueGameName)) {
                foundSearchedGame = true;
                assertEquals(29.99, game.getPrice(), 0.01, "Game price should match");
                assertEquals(10, game.getQuantityInStock(), "Game quantity should match");
                assertTrue(game.isAvailable(), "Searched game should be available");
            }
        }
        assertTrue(foundSearchedGame, "Recently created game should be found in search results");
    }
}