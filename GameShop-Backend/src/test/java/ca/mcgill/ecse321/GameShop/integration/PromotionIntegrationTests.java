package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.GameShop.dto.PromotionRequestDto;
import ca.mcgill.ecse321.GameShop.dto.PromotionResponseDto;
import ca.mcgill.ecse321.GameShop.dto.PromotionListDto;
import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;
import ca.mcgill.ecse321.GameShop.model.Promotion.PromotionType;
import ca.mcgill.ecse321.GameShop.repository.PromotionRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.StoreInformationRepository;

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
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PromotionIntegrationTests {

        @Autowired
        private TestRestTemplate client;

        @Autowired
        private PromotionRepository promotionRepository;

        @Autowired
        private GameRepository gameRepository;

        @Autowired
        private GameCategoryRepository gameCategoryRepository;

        @Autowired
        private StoreInformationRepository storeInformationRepository;

        private static final double VALID_DISCOUNT = 10.0;
        private static final double INVALID_DISCOUNT = -5.0;
        private static final double OVER_MAX_DISCOUNT = 150.0;

        @BeforeEach
        @AfterEach
        public void setUp() {
                promotionRepository.deleteAll();
                gameRepository.deleteAll();
                gameCategoryRepository.deleteAll();
                storeInformationRepository.deleteAll();
        }

        @Test
        @Order(1)
        public void testCreatePromotionWithValidGameIds() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                Game game1 = new Game();
                game1.setName("Game 1");
                gameRepository.save(game1);

                Game game2 = new Game();
                game2.setName("Game 2");
                gameRepository.save(game2);

                PromotionRequestDto requestDto = new PromotionRequestDto();
                requestDto.setPromotionType(PromotionType.GAME);
                requestDto.setDiscountPercentageValue(VALID_DISCOUNT);
                requestDto.setPromotedGameIds(Arrays.asList(game1.getGameEntityId(), game2.getGameEntityId()));
                requestDto.setPromotedCategoryIds(Arrays.asList());

                // Act
                ResponseEntity<PromotionResponseDto> response = client.postForEntity("/promotions/", requestDto,
                                PromotionResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                PromotionResponseDto responseBody = response.getBody();
                assertNotNull(responseBody);
                assertEquals(PromotionType.GAME, responseBody.getPromotionType());
                assertEquals(VALID_DISCOUNT, responseBody.getDiscountPercentageValue());
                assertEquals(2, responseBody.getPromotedGameIds().size());
                assertEquals(0, responseBody.getPromotedCategoryIds().size());
                assertEquals(Arrays.asList(game1.getGameEntityId(), game2.getGameEntityId()),
                                responseBody.getPromotedGameIds());
        }

        @Test
        @Order(2)
        public void testCreatePromotionNoStoreInformation() {
                // Arrange
                storeInformationRepository.deleteAll();

                Game game = new Game();
                game.setName("Game 1");
                gameRepository.save(game);

                PromotionRequestDto requestDto = new PromotionRequestDto();
                requestDto.setPromotionType(PromotionType.GAME);
                requestDto.setDiscountPercentageValue(VALID_DISCOUNT);
                requestDto.setPromotedGameIds(Arrays.asList(game.getGameEntityId()));
                requestDto.setPromotedCategoryIds(Arrays.asList());

                // Act
                ResponseEntity<ErrorResponseDto> response = client.postForEntity("/promotions/", requestDto,
                                ErrorResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                ErrorResponseDto errorResponse = response.getBody();
                assertNotNull(errorResponse);
                assertEquals("Store information not found.", errorResponse.getError());
        }

        @Test
        @Order(3)
        public void testCreatePromotionInvalidPromotionType() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                GameCategory category = new GameCategory();
                category.setName("Category 1");
                gameCategoryRepository.save(category);

                PromotionRequestDto requestDto = new PromotionRequestDto();
                requestDto.setPromotionType(PromotionType.GAME);
                requestDto.setDiscountPercentageValue(VALID_DISCOUNT);
                requestDto.setPromotedGameIds(Arrays.asList());
                requestDto.setPromotedCategoryIds(Arrays.asList(category.getCategoryId()));

                // Act
                ResponseEntity<ErrorResponseDto> response = client.postForEntity("/promotions/", requestDto,
                                ErrorResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                ErrorResponseDto errorResponse = response.getBody();
                assertNotNull(errorResponse);
                assertEquals("Promotion type GAME cannot have associated categories.", errorResponse.getError());
        }

        @Test
        @Order(4)
        public void testUpdatePromotionSuccessfully() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                Game game1 = new Game();
                game1.setName("Game 1");
                gameRepository.save(game1);

                Game game2 = new Game();
                game2.setName("Game 2");
                gameRepository.save(game2);

                PromotionRequestDto createRequestDto = new PromotionRequestDto();
                createRequestDto.setPromotionType(PromotionType.GAME);
                createRequestDto.setDiscountPercentageValue(VALID_DISCOUNT);
                createRequestDto.setPromotedGameIds(Arrays.asList(game1.getGameEntityId()));
                createRequestDto.setPromotedCategoryIds(Arrays.asList());

                ResponseEntity<PromotionResponseDto> createResponse = client.postForEntity("/promotions/",
                                createRequestDto,
                                PromotionResponseDto.class);
                PromotionResponseDto createdPromotion = createResponse.getBody();

                PromotionRequestDto updateRequestDto = new PromotionRequestDto();
                updateRequestDto.setDiscountPercentageValue(15.0);
                updateRequestDto.setPromotedGameIds(Arrays.asList(game2.getGameEntityId()));
                updateRequestDto.setPromotedCategoryIds(Arrays.asList());

                // Act
                ResponseEntity<PromotionResponseDto> updateResponse = client.exchange(
                                "/promotions/" + createdPromotion.getPromotionId(), HttpMethod.PUT,
                                new HttpEntity<>(updateRequestDto), PromotionResponseDto.class);

                // Assert
                assertNotNull(updateResponse);
                assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
                PromotionResponseDto updatedPromotion = updateResponse.getBody();
                assertNotNull(updatedPromotion);
                assertEquals(15.0, updatedPromotion.getDiscountPercentageValue());
                assertEquals(Arrays.asList(game2.getGameEntityId()), updatedPromotion.getPromotedGameIds());
        }

        @Test
        @Order(5)
        public void testUpdatePromotionNotFound() {
                // Arrange
                PromotionRequestDto updateRequestDto = new PromotionRequestDto();
                updateRequestDto.setDiscountPercentageValue(15.0);
                updateRequestDto.setPromotedGameIds(Arrays.asList(1));
                updateRequestDto.setPromotedCategoryIds(Arrays.asList());

                Integer nonExistentId = 999;

                // Act
                ResponseEntity<ErrorResponseDto> updateResponse = client.exchange("/promotions/" + nonExistentId,
                                HttpMethod.PUT, new HttpEntity<>(updateRequestDto), ErrorResponseDto.class);

                // Assert
                assertNotNull(updateResponse);
                assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode());
                ErrorResponseDto errorResponse = updateResponse.getBody();
                assertNotNull(errorResponse);
                assertEquals("Promotion with ID 999 not found.", errorResponse.getError());
        }

        @Test
        @Order(6)
        public void testDeletePromotionSuccessfully() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                // Create a game
                Game game = new Game();
                game.setName("Game 1");
                gameRepository.save(game);

                PromotionRequestDto createRequestDto = new PromotionRequestDto();
                createRequestDto.setPromotionType(PromotionType.GAME);
                createRequestDto.setDiscountPercentageValue(VALID_DISCOUNT);
                createRequestDto.setPromotedGameIds(Arrays.asList(game.getGameEntityId()));
                createRequestDto.setPromotedCategoryIds(Arrays.asList());

                ResponseEntity<PromotionResponseDto> createResponse = client.postForEntity("/promotions/",
                                createRequestDto,
                                PromotionResponseDto.class);
                PromotionResponseDto createdPromotion = createResponse.getBody();

                // Act
                ResponseEntity<Void> deleteResponse = client.exchange(
                                "/promotions/" + createdPromotion.getPromotionId(),
                                HttpMethod.DELETE, null, Void.class);

                // Assert
                assertNotNull(deleteResponse);
                assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

                ResponseEntity<PromotionListDto> getResponse = client.getForEntity("/promotions/",
                                PromotionListDto.class);
                List<PromotionResponseDto> promotions = getResponse.getBody().getPromotions();
                boolean promotionExists = promotions.stream()
                                .anyMatch(promo -> promo.getPromotionId() == createdPromotion.getPromotionId());
                assertEquals(false, promotionExists);
        }

        @Test
        @Order(7)
        public void testDeletePromotionNotFound() {
                // Arrange
                Integer nonExistentId = 999;

                // Act
                ResponseEntity<ErrorResponseDto> deleteResponse = client.exchange("/promotions/" + nonExistentId,
                                HttpMethod.DELETE, null, ErrorResponseDto.class);

                // Assert
                assertNotNull(deleteResponse);
                assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
                ErrorResponseDto errorResponse = deleteResponse.getBody();
                assertNotNull(errorResponse);
                assertEquals("Promotion with ID 999 not found.", errorResponse.getError());
        }

        @Test
        @Order(8)
        public void testGetPromotionsByGameIdSuccessfully() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                Game game = new Game();
                game.setName("Game 1");
                gameRepository.save(game);

                PromotionRequestDto createRequestDto = new PromotionRequestDto();
                createRequestDto.setPromotionType(PromotionType.GAME);
                createRequestDto.setDiscountPercentageValue(VALID_DISCOUNT);
                createRequestDto.setPromotedGameIds(Arrays.asList(game.getGameEntityId()));
                createRequestDto.setPromotedCategoryIds(Arrays.asList());

                client.postForEntity("/promotions/", createRequestDto, PromotionResponseDto.class);

                // Act
                ResponseEntity<PromotionListDto> response = client.getForEntity(
                                "/promotions/game/" + game.getGameEntityId(), PromotionListDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                PromotionListDto promotions = response.getBody();
                assertNotNull(promotions);
                assertEquals(1, promotions.getPromotions().size());
                PromotionResponseDto promotion = promotions.getPromotions().get(0);
                assertEquals(Arrays.asList(game.getGameEntityId()), promotion.getPromotedGameIds());
        }

        @Test
        @Order(9)
        public void testGetPromotionsByGameIdNotFound() {
                // Arrange
                Integer nonExistentGameId = 999;

                // Act
                ResponseEntity<ErrorResponseDto> response = client.getForEntity(
                                "/promotions/game/" + nonExistentGameId, ErrorResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                ErrorResponseDto errorResponse = response.getBody();
                assertNotNull(errorResponse);
                assertEquals("Game with ID 999 not found.", errorResponse.getError());
        }

        @Test
        @Order(10)
        public void testCreatePromotionWithNegativeDiscount() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                // Create a game
                Game game = new Game();
                game.setName("Game");
                gameRepository.save(game);

                PromotionRequestDto requestDto = new PromotionRequestDto();
                requestDto.setPromotionType(PromotionType.GAME);
                requestDto.setDiscountPercentageValue(INVALID_DISCOUNT);
                requestDto.setPromotedGameIds(Arrays.asList(game.getGameEntityId()));
                requestDto.setPromotedCategoryIds(Arrays.asList());

                // Act
                ResponseEntity<ErrorResponseDto> response = client.postForEntity("/promotions/", requestDto,
                                ErrorResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                ErrorResponseDto errorResponse = response.getBody();
                assertNotNull(errorResponse);
                assertEquals("Discount percentage value must be zero or positive.", errorResponse.getError());
        }

        @Test
        @Order(11)
        public void testCreatePromotionWithBothGamesAndCategories() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                Game game = new Game();
                game.setName("Game 1");
                gameRepository.save(game);

                GameCategory category = new GameCategory();
                category.setName("Category 1");
                gameCategoryRepository.save(category);

                PromotionRequestDto requestDto = new PromotionRequestDto();
                requestDto.setPromotionType(PromotionType.GAME);
                requestDto.setDiscountPercentageValue(VALID_DISCOUNT);
                requestDto.setPromotedGameIds(Arrays.asList(game.getGameEntityId()));
                requestDto.setPromotedCategoryIds(Arrays.asList(category.getCategoryId()));

                // Act
                ResponseEntity<ErrorResponseDto> response = client.postForEntity("/promotions/", requestDto,
                                ErrorResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                ErrorResponseDto errorResponse = response.getBody();
                assertNotNull(errorResponse);
                assertEquals("Promotion type GAME cannot have associated categories.", errorResponse.getError());
        }

        @Test
        @Order(12)
        public void testCreatePromotionWithInvalidGameIds() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                PromotionRequestDto requestDto = new PromotionRequestDto();
                requestDto.setPromotionType(PromotionType.GAME);
                requestDto.setDiscountPercentageValue(VALID_DISCOUNT);
                requestDto.setPromotedGameIds(Arrays.asList(999));
                requestDto.setPromotedCategoryIds(Arrays.asList());

                // Act
                ResponseEntity<ErrorResponseDto> response = client.postForEntity("/promotions/", requestDto,
                                ErrorResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                ErrorResponseDto errorResponse = response.getBody();
                assertNotNull(errorResponse);
                assertEquals("Game with ID 999 not found.", errorResponse.getError());
        }

        @Test
        @Order(13)
        public void testCreatePromotionWithEmptyPromotedGameIds() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                PromotionRequestDto requestDto = new PromotionRequestDto();
                requestDto.setPromotionType(PromotionType.GAME);
                requestDto.setDiscountPercentageValue(VALID_DISCOUNT);
                requestDto.setPromotedGameIds(Arrays.asList());
                requestDto.setPromotedCategoryIds(Arrays.asList());

                // Act
                ResponseEntity<ErrorResponseDto> response = client.postForEntity("/promotions/", requestDto,
                                ErrorResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                ErrorResponseDto errorResponse = response.getBody();
                assertNotNull(errorResponse);
                // Assuming the service returns a specific error message for this case
                assertEquals("No promoted games provided for GAME promotion type.", errorResponse.getError());
        }

        @Test
        @Order(14)
        public void testCreatePromotionWithDiscountOver100() {
                // Arrange
                StoreInformation storeInfo = new StoreInformation();
                storeInfo.setStorePolicy("Store policy");
                storeInformationRepository.save(storeInfo);

                Game game = new Game();
                game.setName("Game 1");
                gameRepository.save(game);

                PromotionRequestDto requestDto = new PromotionRequestDto();
                requestDto.setPromotionType(PromotionType.GAME);
                requestDto.setDiscountPercentageValue(OVER_MAX_DISCOUNT);
                requestDto.setPromotedGameIds(Arrays.asList(game.getGameEntityId()));
                requestDto.setPromotedCategoryIds(Arrays.asList());

                // Act
                ResponseEntity<ErrorResponseDto> response = client.postForEntity("/promotions/", requestDto,
                                ErrorResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                ErrorResponseDto errorResponse = response.getBody();
                assertNotNull(errorResponse);
                // Assuming the service returns a specific error message for this case
                assertEquals("Discount percentage cannot exceed 100%.", errorResponse.getError());
        }
}
