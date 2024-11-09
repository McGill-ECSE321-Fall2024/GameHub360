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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

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
        GameDto gameDto = new GameDto(
            "Test Game",
            "This is a detailed description",
            "https://example.com/game-image.jpg",
            10,
            false,
            29.99,
            validCategoryId
        );

        // Act
        ResponseEntity<GameResponseDto> response = client.postForEntity("/games", gameDto, GameResponseDto.class);

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
        // Arrange
        GameDto gameDto = new GameDto(
            "Initial Game",
            "This is a detailed description",
            "https://example.com/game-image.jpg",
            10,
            false,
            29.99,
            validCategoryId
        );
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", gameDto, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody);
        Integer gameId = responseBody.getId();

        GameDto updateRequest = new GameDto(
            "Updated Game",
            "This is a detailed description",
            "https://example.com/game-image.jpg",
            10,
            false,
            29.99,
            validCategoryId
        );

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
        GameDto gameDto = new GameDto(
            "Archive Test Game",
            "This is a detailed description",
            "https://example.com/game-image.jpg",
            10,
            false,
            29.99,
            validCategoryId
        );
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", gameDto, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody);
        Integer gameId = responseBody.getId();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        // Act
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
        GameDto gameDto = new GameDto(
            "Archive View Test Game",
            "This is a detailed description",
            "https://example.com/game-image.jpg",
            10,
            false,
            29.99,
            validCategoryId
        );
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", gameDto, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody);
        Integer gameId = responseBody.getId();

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
    public void testBrowseGames() {
        // Arrange
        GameDto gameDto = new GameDto(
            "Browse Test Game",
            "This is a detailed description",
            "https://example.com/game-image.jpg",
            10,
            false,
            29.99,
            validCategoryId
        );
        ResponseEntity<GameDto> createResponse = client.postForEntity("/games", gameDto, GameDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        // Act
        ResponseEntity<String> rawResponse = client.getForEntity("/games", String.class);

        // Assert
        assertNotNull(rawResponse);
        assertEquals(HttpStatus.OK, rawResponse.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        try {
            GameDto[] games = mapper.readValue(rawResponse.getBody(), GameDto[].class);
            assertNotNull(games, "Response body should not be null");
            assertTrue(games.length > 0, "Games list should not be empty");
            for (GameDto game : games) {
                assertTrue(game.getIsAvailable(), "Game should be available (isAvailable=true)");
            }
        } catch (JsonProcessingException e) {
            fail("Failed to parse JSON response: " + e.getMessage());
        }
    }

    @Test
    @Order(6)
    public void testSearchGames() {
        // Arrange
        GameDto gameDto = new GameDto(
            "Unique Game Name",
            "This is a detailed description",
            "https://example.com/game-image.jpg",
            10,
            false,
            29.99,
            validCategoryId
        );
        ResponseEntity<GameDto> createResponse = client.postForEntity("/games", gameDto, GameDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        // Act
        ResponseEntity<String> rawResponse = client.getForEntity("/games/search?query=Unique", String.class);

        // Assert
        assertNotNull(rawResponse);
        assertEquals(HttpStatus.OK, rawResponse.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        try {
            GameDto[] games = mapper.readValue(rawResponse.getBody(), GameDto[].class);
            assertNotNull(games);
            assertTrue(games.length > 0);
            assertTrue(Arrays.stream(games).anyMatch(game -> game.getName().contains("Unique")));
        } catch (JsonProcessingException e) {
            fail("Failed to parse JSON response: " + e.getMessage());
        }
    }
}