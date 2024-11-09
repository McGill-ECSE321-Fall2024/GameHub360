package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
import ca.mcgill.ecse321.GameShop.dto.GameDto;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;

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

    private Integer VALID_CATEGORY_ID;

    @BeforeAll
    public void setUp() {
        // Clear repositories
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();

        // Create a game category
        GameCategory category = new GameCategory(true, "Action Games");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        VALID_CATEGORY_ID = gameCategoryRepository.save(category).getCategoryId();
    }

    @AfterAll
    public void cleanUp() {
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    // Create a reusable GameDto for tests
    private GameDto createTestGameDto(String name) {
        return new GameDto(
            name,                                    // non-empty name
            "This is a detailed description",        // non-empty description
            "https://example.com/game-image.jpg",    // non-empty imageUrl
            10,
            false,
            29.99,
            VALID_CATEGORY_ID
        );
    }

    @Test
    @Order(1)
    public void testCreateGame() {
        GameDto gameDto = createTestGameDto("Test Game");
        // Arrange
        // Act
        ResponseEntity<GameResponseDto> response = client.postForEntity(
                "/games", gameDto, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game creation failed");
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Test Game", responseBody.getName());
    }

    @Test
    @Order(2)
    public void testUpdateGame() {
        // Create initial game
        GameDto gameDto = createTestGameDto("Initial Game");
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", gameDto, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody);
        Integer gameId = responseBody.getId();

        // Create update request
        GameDto updateRequest = createTestGameDto("Updated Game");

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/" + gameId,
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game update failed");
        GameResponseDto updatedGame = response.getBody();
        assertNotNull(updatedGame, "Updated game response body should not be null");
        assertEquals("Updated Game", updatedGame.getName());
    }

    @Test
    @Order(3)
    public void testArchiveGame() {
        // Arrange
        GameDto gameDto = createTestGameDto("Archive Test Game");
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", gameDto, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody);
        Integer gameId = responseBody.getId();

        // Act
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                requestEntity,
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game archiving failed");
        GameResponseDto archivedGame = response.getBody();
        assertNotNull(archivedGame);
        assertFalse(archivedGame.isAvailable());
    }

    @Test
    @Order(4)
    public void testViewArchivedGames() {
        // Arrange
        GameDto gameDto = createTestGameDto("Archive View Test Game");
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", gameDto, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody);
        Integer gameId = responseBody.getId();

        // Archive the game
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                requestEntity,
                GameResponseDto.class);

        // Act
        ResponseEntity<GameResponseDto[]> response = client.getForEntity(
                "/games/archive",
                GameResponseDto[].class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto[] archivedGames = response.getBody();
        assertNotNull(archivedGames);
        assertTrue(archivedGames.length > 0, "Archived games list should not be empty");
        for (GameResponseDto game : archivedGames) {
            assertFalse(game.isAvailable(), "Game should be archived (isAvailable=false)");
        }
    }

    @Test
    @Order(5)
    public void testReactivateArchivedGame() {
        // Arrange
        GameDto gameDto = createTestGameDto("Reactivate Test Game");
        ResponseEntity<GameDto> createResponse = client.postForEntity("/games", gameDto, GameDto.class);
        GameDto responseBody = createResponse.getBody();
        assertNotNull(responseBody);
        Integer gameId = responseBody.getId();

        // Archive the game first
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        ResponseEntity<GameDto> archiveResponse = client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                requestEntity,
                GameDto.class);
        assertEquals(HttpStatus.OK, archiveResponse.getStatusCode(), "Game archiving failed");

        // Act
        ResponseEntity<GameDto> response = client.exchange(
                "/games/archive/" + gameId + "/reactivate",
                HttpMethod.PUT,
                requestEntity,
                GameDto.class);

        // Assert
        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Error response: " + response.getBody());
        }
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game reactivation failed");
        GameDto reactivatedGame = response.getBody();
        assertNotNull(reactivatedGame);
        assertTrue(reactivatedGame.getIsAvailable(), "Game should be available after reactivation");
    }

    @Test
    @Order(6)
    public void testBrowseGames() {
        // Arrange
        GameDto gameDto = createTestGameDto("Browse Test Game");
        ResponseEntity<GameDto> createResponse = client.postForEntity("/games", gameDto, GameDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        // Act
        ResponseEntity<List<GameDto>> response = client.exchange(
                "/games",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GameDto>>() {}
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GameDto> games = response.getBody();
        assertNotNull(games, "Response body should not be null");
        assertFalse(games.isEmpty(), "Games list should not be empty");
        for (GameDto game : games) {
            assertTrue(game.getIsAvailable(), "Game should be available (isAvailable=true)");
        }
    }

    @Test
    @Order(7)
    public void testSearchGames() {
        // Arrange
        GameDto gameDto = createTestGameDto("Unique Game Name");
        ResponseEntity<GameDto> createResponse = client.postForEntity("/games", gameDto, GameDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        // Act
        ResponseEntity<List<GameDto>> response = client.exchange(
                "/games/search?query=Unique",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GameDto>>() {}
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GameDto> games = response.getBody();
        assertNotNull(games);
        assertFalse(games.isEmpty());
        assertTrue(games.stream().anyMatch(game -> game.getName().contains("Unique")));
    }
}