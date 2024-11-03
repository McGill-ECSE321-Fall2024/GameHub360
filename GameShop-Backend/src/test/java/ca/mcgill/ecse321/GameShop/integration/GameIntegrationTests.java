package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.model.GameCategory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class GameIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    private static Integer VALID_CATEGORY_ID;
    private static final String VALID_NAME = "Chess";
    private static final Double VALID_PRICE = 19.99;
    private static final int VALID_STOCK = 10;
    private static final String VALID_DESCRIPTION = "A classic board game";
    private static final String VALID_IMAGE_URL = "http://example.com/chess.jpg";

    @BeforeEach
    public void setUp() {
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();

        GameCategory category = new GameCategory(true, "Board Games");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        GameCategory savedCategory = gameCategoryRepository.save(category);
        VALID_CATEGORY_ID = savedCategory.getCategoryId();
    }

    @AfterEach
    public void cleanUp() {
        gameRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSubmitGameRequestSuccessfully() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);

        // Act
        ResponseEntity<GameResponseDto> response = client.postForEntity("/games/request", request,
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_PRICE, responseBody.getPrice());
        assertEquals(VALID_STOCK, responseBody.getQuantityInStock());
    }

    @Test
    @Order(2)
    public void testApproveGameRequest() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games/request", request,
                GameResponseDto.class);
        GameResponseDto createResponseBody = createResponse.getBody();
        assertNotNull(createResponseBody, "Response body should not be null");
        Integer requestId = createResponseBody.getId();

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange(
                "/games/request/" + requestId + "/approval?approval=true",
                HttpMethod.PUT,
                null,
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals(true, responseBody.isAvailable());
    }

    @Test
    @Order(3)
    public void testCreateGameSuccessfully() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);

        // Act
        ResponseEntity<GameResponseDto> response = client.postForEntity("/games", request, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_PRICE, responseBody.getPrice());
        assertEquals(VALID_STOCK, responseBody.getQuantityInStock());
    }

    @Test
    @Order(4)
    public void testUpdateGameSuccessfully() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", createRequest,
                GameResponseDto.class);
        GameResponseDto createResponseBody = createResponse.getBody();
        assertNotNull(createResponseBody, "Response body should not be null");
        Integer gameId = createResponseBody.getId();

        GameRequestDto updateRequest = new GameRequestDto("Updated Chess", 29.99, 20, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange("/games/" + gameId,
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Updated Chess", responseBody.getName());
        assertEquals(29.99, responseBody.getPrice());
        assertEquals(20, responseBody.getQuantityInStock());
    }

    @Test
    @Order(5)
    public void testArchiveGameSuccessfully() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", request, GameResponseDto.class);
        GameResponseDto createResponseBody = createResponse.getBody();
        assertNotNull(createResponseBody, "Response body should not be null");
        Integer gameId = createResponseBody.getId();

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange("/games/archive/" + gameId, HttpMethod.PUT, null,
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals(false, responseBody.isAvailable());
    }

    @Test
    @Order(6)
    public void testViewArchivedGames() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", request, GameResponseDto.class);
        GameResponseDto createResponseBody = createResponse.getBody();
        assertNotNull(createResponseBody, "Response body should not be null");
        Integer gameId = createResponseBody.getId();
        client.exchange("/games/archive/" + gameId, HttpMethod.PUT, null, GameResponseDto.class);

        // Act
        ResponseEntity<GameResponseDto[]> response = client.getForEntity("/games/archive", GameResponseDto[].class);

        // Convert the array to a list
        List<GameResponseDto> responseBody = Arrays.asList(response.getBody());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals(1, responseBody.size());
    }

    @Test
    @Order(7)
    public void testReactivateArchivedGame() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", request, GameResponseDto.class);
        GameResponseDto createResponseBody = createResponse.getBody();
        assertNotNull(createResponseBody, "Response body should not be null");
        Integer gameId = createResponseBody.getId();
        client.exchange("/games/archive/" + gameId, HttpMethod.PUT, null, GameResponseDto.class);

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange("/games/archive/" + gameId + "/reactivate",
                HttpMethod.PUT, null, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals(true, responseBody.isAvailable());
    }

    @Test
    @Order(8)
    public void testBrowseGames() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);
        client.postForEntity("/games", request, GameResponseDto.class);

        // Act
        ResponseEntity<GameResponseDto[]> response = client.getForEntity("/games", GameResponseDto[].class);

        // Convert the array to a list
        List<GameResponseDto> responseBody = Arrays.asList(response.getBody());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals(1, responseBody.size());
    }

    @Test
    @Order(9)
    public void testSearchGames() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);
        client.postForEntity("/games", request, GameResponseDto.class);

        // Act
        ResponseEntity<GameResponseDto> response = client.getForEntity("/games/search?query=Chess",
                GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto game = response.getBody();
        assertNotNull(game, "Response body should not be null");
        assertEquals(VALID_NAME, game.getName());
    }

    @Test
    @Order(10)
    public void testUpdateGameStock() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", request, GameResponseDto.class);
        GameResponseDto createResponseBody = createResponse.getBody();
        assertNotNull(createResponseBody, "Response body should not be null");
        Integer gameId = createResponseBody.getId();

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange("/games/" + gameId + "/stock?stock=50",
                HttpMethod.PUT, null, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals(50, responseBody.getQuantityInStock());
    }

    @Test
    @Order(11)
    public void testUpdateGamePrice() {
        // Arrange
        GameRequestDto request = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK, VALID_DESCRIPTION,
                VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", request, GameResponseDto.class);
        GameResponseDto createResponseBody = createResponse.getBody();
        assertNotNull(createResponseBody, "Response body should not be null");
        Integer gameId = createResponseBody.getId();

        // Act
        ResponseEntity<GameResponseDto> response = client.exchange("/games/" + gameId + "/price?price=29.99",
                HttpMethod.PUT, null, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals(29.99, responseBody.getPrice());
    }
}