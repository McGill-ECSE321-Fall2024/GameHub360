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

    private static Integer VALID_CATEGORY_ID;
    private static final String VALID_NAME = "Test Game";
    private static final Double VALID_PRICE = 59.99;
    private static final int VALID_STOCK = 50;
    private static final String VALID_DESCRIPTION = "A test game description";
    private static final String VALID_IMAGE_URL = "http://example.com/game.jpg";

    @BeforeAll
    public void setUp() {
        // Clear repositories
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();

        // Create test category
        GameCategory category = new GameCategory(true, "Action Games");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        VALID_CATEGORY_ID = gameCategoryRepository.save(category).getCategoryId();
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
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);

        // Act
        ResponseEntity<GameResponseDto> response = client.postForEntity(
                "/games", request, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_PRICE, responseBody.getPrice());
        assertEquals(VALID_STOCK, responseBody.getQuantityInStock());
        assertTrue(responseBody.isAvailable());
    }

    @Test
    @Order(2)
    public void testUpdateGame() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity(
                "/games", createRequest, GameResponseDto.class);
        assertNotNull(createResponse, "Response should not be null");
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        GameRequestDto updateRequest = new GameRequestDto();
        updateRequest.setName("Updated Game");
        updateRequest.setPrice(69.99);

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/" + gameId,
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto updatedGame = response.getBody();
        assertNotNull(updatedGame);
        assertEquals("Updated Game", updatedGame.getName());
        assertEquals(69.99, updatedGame.getPrice());
    }

    @Test
    @Order(3)
    public void testArchiveGame() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity(
                "/games", createRequest, GameResponseDto.class);
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
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto archivedGame = response.getBody();
        assertNotNull(archivedGame);
        assertFalse(archivedGame.isAvailable());
    }

    @Test
    @Order(4)
    public void testViewArchivedGames() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity(
                "/games", request, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Act
        ResponseEntity<GameResponseDto[]> response = client.getForEntity(
                "/games/archive",
                GameResponseDto[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GameResponseDto> archivedGames = Arrays.asList(response.getBody());
        assertFalse(archivedGames.isEmpty());
        assertFalse(archivedGames.get(0).isAvailable());
    }

    @Test
    @Order(5)
    public void testReactivateArchivedGame() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity(
                "/games", request, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        client.exchange(
                "/games/archive/" + gameId,
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/archive/" + gameId + "/reactivate",
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto reactivatedGame = response.getBody();
        assertNotNull(reactivatedGame);
        assertTrue(reactivatedGame.isAvailable());
    }

    @Test
    @Order(6)
    public void testBrowseGames() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        client.postForEntity("/games", request, GameResponseDto.class);

        // Act
        ResponseEntity<GameResponseDto[]> response = client.getForEntity(
                "/games?minPrice=50&maxPrice=70",
                GameResponseDto[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GameResponseDto> games = Arrays.asList(response.getBody());
        assertFalse(games.isEmpty());
        assertTrue(games.stream().allMatch(game -> game.getPrice() >= 50 && game.getPrice() <= 70));
    }

    @Test
    @Order(7)
    public void testSearchGames() {
        // Arrange
        GameRequestDto request = new GameRequestDto("Unique Game Name", VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        client.postForEntity("/games", request, GameResponseDto.class);

        // Act
        ResponseEntity<GameResponseDto[]> response = client.getForEntity(
                "/games/search?query=Unique",
                GameResponseDto[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GameResponseDto> games = Arrays.asList(response.getBody());
        assertFalse(games.isEmpty());
        assertTrue(games.stream().anyMatch(game -> game.getName().contains("Unique")));
    }

    @Test
    @Order(8)
    public void testAddGameToCategory() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity(
                "/games", request, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        // Create new category
        GameCategory newCategory = new GameCategory(true, "Strategy Games");
        newCategory.setCategoryType(GameCategory.CategoryType.GENRE);
        Integer newCategoryId = gameCategoryRepository.save(newCategory).getCategoryId();

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/" + gameId + "/categories/" + newCategoryId,
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto addedGame = response.getBody();
        assertNotNull(addedGame);
        assertTrue(addedGame.getCategoryId().contains(newCategoryId));
    }

    @Test
    @Order(9)
    public void testUpdateGameStock() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity(
                "/games", request, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/" + gameId + "/stock?stock=75",
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto updatedGame = response.getBody();
        assertNotNull(updatedGame);
        assertEquals(75, updatedGame.getQuantityInStock());
    }

    @Test
    @Order(10)
    public void testUpdateGamePrice() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity(
                "/games", request, GameResponseDto.class);
        GameResponseDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer gameId = responseBody.getId();

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/" + gameId + "/price?price=79.99",
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto updatedGame = response.getBody();
        assertNotNull(updatedGame);
        assertEquals(79.99, updatedGame.getPrice());
    }
}