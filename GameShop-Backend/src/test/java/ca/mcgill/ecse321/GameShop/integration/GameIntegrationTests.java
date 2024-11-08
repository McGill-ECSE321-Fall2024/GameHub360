package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
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

    private static final String VALID_NAME = "Test Game";
    private static final String VALID_DESCRIPTION = "A test game description";
    private static final String VALID_IMAGE_URL = "http://example.com/game.jpg";
    private static final Double VALID_PRICE = 19.99;
    private static final Integer VALID_QUANTITY_IN_STOCK = 10;

    @BeforeAll
    public void setUp() {
        // Clear repositories
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
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
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_PRICE,
                VALID_QUANTITY_IN_STOCK);

        // Act
        ResponseEntity<GameResponseDto> response = client.postForEntity(
                "/games", request, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game creation failed");
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_NAME, responseBody.getName());
        assertTrue(responseBody.isAvailable());
    }

    @Test
    @Order(2)
    public void testUpdateGame() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_PRICE,
                VALID_QUANTITY_IN_STOCK);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", createRequest,
                GameResponseDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        GameRequestDto updateRequest = new GameRequestDto();
        updateRequest.setName("Updated Game");
        updateRequest.setDescription(VALID_DESCRIPTION); // Include required fields
        updateRequest.setImageUrl(VALID_IMAGE_URL);
        updateRequest.setPrice(VALID_PRICE);
        updateRequest.setQuantityInStock(VALID_QUANTITY_IN_STOCK);

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
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_PRICE,
                VALID_QUANTITY_IN_STOCK);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", createRequest,
                GameResponseDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Assert
        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Error response: " + response.getBody());
        }
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
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_PRICE,
                VALID_QUANTITY_IN_STOCK);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", request, GameResponseDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        // Archive the game
        ResponseEntity<GameResponseDto> archiveResponse = client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                null,
                GameResponseDto.class);
        assertEquals(HttpStatus.OK, archiveResponse.getStatusCode(), "Game archiving failed");

        // Act
        ResponseEntity<GameResponseDto[]> response = client.getForEntity(
                "/games/archive",
                GameResponseDto[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GameResponseDto> archivedGames = Arrays.asList(response.getBody());
        assertFalse(archivedGames.isEmpty(), "Archived games list should not be empty");
        for (GameResponseDto game : archivedGames) {
            assertFalse(game.isAvailable(), "Game should be archived (isAvailable=false)");
        }
    }

    @Test
    @Order(5)
    public void testReactivateArchivedGame() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_PRICE,
                VALID_QUANTITY_IN_STOCK);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", request, GameResponseDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        // Archive the game first
        ResponseEntity<GameResponseDto> archiveResponse = client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                null,
                GameResponseDto.class);
        assertEquals(HttpStatus.OK, archiveResponse.getStatusCode(), "Game archiving failed");

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/archive/" + gameId + "/reactivate",
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Assert
        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Error response: " + response.getBody());
        }
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game reactivation failed");
        GameResponseDto reactivatedGame = response.getBody();
        assertNotNull(reactivatedGame);
        assertTrue(reactivatedGame.isAvailable());
    }

    @Test
    @Order(6)
    public void testBrowseGames() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_PRICE,
                VALID_QUANTITY_IN_STOCK);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", request, GameResponseDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        // Act
        ResponseEntity<GameResponseDto[]> response = client.getForEntity(
                "/games",
                GameResponseDto[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto[] games = response.getBody();
        assertNotNull(games, "Response body should not be null");
        assertTrue(games.length > 0, "Games list should not be empty");
        for (GameResponseDto game : games) {
            assertTrue(game.isAvailable(), "Game should be available (isAvailable=true)");
        }
    }

    @Test
    @Order(7)
    public void testSearchGames() {
        // Arrange
        GameRequestDto request = new GameRequestDto("Unique Game Name", VALID_DESCRIPTION, VALID_IMAGE_URL,
                VALID_PRICE, VALID_QUANTITY_IN_STOCK);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", request, GameResponseDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game creation failed");

        // Act
        ResponseEntity<GameResponseDto[]> response = client.getForEntity(
                "/games/search?query=Unique",
                GameResponseDto[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto[] games = response.getBody();
        assertNotNull(games);
        assertTrue(games.length > 0);
        assertTrue(Arrays.stream(games).anyMatch(game -> game.getName().contains("Unique")));
    }
}