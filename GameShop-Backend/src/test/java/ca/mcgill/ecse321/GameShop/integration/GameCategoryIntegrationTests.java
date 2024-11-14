package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse321.GameShop.dto.GameCategoryRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameCategoryResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.GameCategory.CategoryType;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.Promotion.PromotionType;
import ca.mcgill.ecse321.GameShop.repository.PromotionRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;

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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class GameCategoryIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    private static final String VALID_NAME = "Action";
    private static final Boolean ISAVAILABLE = true;
    private static final CategoryType VALID_CATEGORY_TYPE = CategoryType.CONSOLE;

    @BeforeEach
    @AfterEach
    public void setUp() {
        promotionRepository.deleteAll();
        gameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateGameCategorySuccessfully() {
        GameCategoryRequestDto request = new GameCategoryRequestDto(VALID_NAME, VALID_CATEGORY_TYPE, true);
        
        ResponseEntity<GameCategoryResponseDto> response = client.postForEntity("/categories/", request, 
                GameCategoryResponseDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameCategoryResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_CATEGORY_TYPE, responseBody.getCategoryType());
        assertTrue(responseBody.isAvailable(), "Expected isAvailable to be true");
    }

    @Test
    @Order(2)
    public void testCreateDuplicateGameCategoryFails() {
        GameCategoryRequestDto request = new GameCategoryRequestDto(VALID_NAME, VALID_CATEGORY_TYPE, ISAVAILABLE);
        client.postForEntity("/categories/", request, GameCategoryResponseDto.class);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity("/categories/", request, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Game category already exists.", errorResponse.getError());
    }

    @Test
    @Order(3)
    public void testUpdateGameCategorySuccessfully() {
        // Arrange
        GameCategoryRequestDto createRequest = new GameCategoryRequestDto(VALID_NAME, VALID_CATEGORY_TYPE, ISAVAILABLE);
        ResponseEntity<GameCategoryResponseDto> createResponse = client.postForEntity("/categories/", createRequest, 
                GameCategoryResponseDto.class);
        Integer categoryId = createResponse.getBody().getCategoryId();
        
        GameCategoryRequestDto updateRequest = new GameCategoryRequestDto("Drama", VALID_CATEGORY_TYPE, ISAVAILABLE);

        // Act
        ResponseEntity<GameCategoryResponseDto> response = client.exchange(
                "/categories/" + categoryId, 
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest), 
                GameCategoryResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameCategoryResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Drama", responseBody.getName());
    }

    @Test
    @Order(4)
    public void testUpdateGameCategoryNotFound() {
        // Arrange
        GameCategoryRequestDto updateRequest = new GameCategoryRequestDto(VALID_NAME, VALID_CATEGORY_TYPE, ISAVAILABLE);

        // Act
        ResponseEntity<ErrorResponseDto> updateResponse = client.exchange("/categories/999", HttpMethod.PUT,
                new HttpEntity<>(updateRequest), ErrorResponseDto.class);

        // Assert
        assertNotNull(updateResponse);
        assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode());
        ErrorResponseDto errorResponse = updateResponse.getBody();
        assertNotNull(errorResponse);
        assertEquals("Game category not found.", errorResponse.getError());
    }

    @Test
    @Order(5)
    public void testDeleteGameCategorySuccessfully() {
        GameCategoryRequestDto request = new GameCategoryRequestDto(VALID_NAME, VALID_CATEGORY_TYPE, ISAVAILABLE);

        ResponseEntity<GameCategoryResponseDto> response = client.postForEntity("/categories/", request,
                GameCategoryResponseDto.class);
        GameCategoryResponseDto responseBody = response.getBody();

        ResponseEntity<Void> deleteResponse = client.exchange(
                "/categories/" + responseBody.getCategoryId(),
                HttpMethod.DELETE, null, Void.class);

        // Assert
        assertNotNull(deleteResponse);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<GameCategoryResponseDto[]> getResponse = client.getForEntity("/categories/",
                GameCategoryResponseDto[].class);
        GameCategoryResponseDto[] categories = getResponse.getBody();
        boolean categoryExists = Arrays.stream(categories)
                .anyMatch(category -> category.getCategoryId() == responseBody.getCategoryId());
        assertEquals(false, categoryExists);
    }

    @Test
    @Order(6)
    public void testDeleteGameCategoryNotFound() {
        // Arrange
        Integer nonExistentId = 999;

        // Act
        ResponseEntity<ErrorResponseDto> deleteResponse = client.exchange("/categories/" + nonExistentId,
                HttpMethod.DELETE, null, ErrorResponseDto.class);

        // Assert
        assertNotNull(deleteResponse);
        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
        ErrorResponseDto errorResponse = deleteResponse.getBody();
        assertNotNull(errorResponse);
        assertEquals("Game category with ID 999 not found.", errorResponse.getError());
    }

    @Test
    @Order(7)
    public void testGetGameCategoriesByGameIdSuccessfully() {
        // Create and save a game category first
        GameCategory category = new GameCategory();
        category.setName(VALID_NAME);
        category.setCategoryType(VALID_CATEGORY_TYPE);
        category.setIsAvailable(ISAVAILABLE);
        category = gameCategoryRepository.save(category);
        
        // Create and save game
        Game game = new Game();
        game.setName("TestGame");
        game = gameRepository.save(game);
        
        // Add game to category
        category.addGame(game);
        gameCategoryRepository.save(category);
        
        ResponseEntity<GameCategoryResponseDto[]> response = client.getForEntity(
                "/categories/game/" + game.getGameEntityId(),
                GameCategoryResponseDto[].class);    

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    @Order(8)
    public void testGetGameCategoriesByGameIdNotFound() {
        // Arrange
        Integer nonExistentGameId = 999;

        // Act
        ResponseEntity<ErrorResponseDto> response = client.getForEntity(
                "/categories/game/" + nonExistentGameId, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Game with ID 999 not found.", errorResponse.getError());
    }

    @Test
    @Order(9)
    public void testGetGameCategoriesByPromotionIdSuccessfully() {
        // Create and save category
        GameCategory category = new GameCategory();
        category.setName(VALID_NAME);
        category.setCategoryType(VALID_CATEGORY_TYPE);
        category.setIsAvailable(ISAVAILABLE);
        category = gameCategoryRepository.save(category);
        
        // Create and save promotion
        Promotion promotion = new Promotion();
        promotion.setDiscountPercentageValue(20.0);
        promotion.setPromotionType(PromotionType.CATEGORY);
        promotion = promotionRepository.save(promotion);
        
        // Add promotion to category
        promotion.addPromotedCategory(category);
        promotionRepository.save(promotion);
        
        ResponseEntity<GameCategoryResponseDto[]> response = client.getForEntity(
                "/categories/promotion/" + promotion.getPromotionId(),
                GameCategoryResponseDto[].class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }

    @Test
    @Order(10)
    public void testGetGameCategoriesByPromotionIdNotFound() {
        // Arrange
        Integer nonExistentGameId = 999;

        // Act
        ResponseEntity<ErrorResponseDto> response = client.getForEntity(
                "/categories/promotion/" + nonExistentGameId, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Promotion with ID 999 not found.", errorResponse.getError());
    }
}