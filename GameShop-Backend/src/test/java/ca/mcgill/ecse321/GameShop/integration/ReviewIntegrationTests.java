package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.repository.ReviewRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ReviewIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderGameRepository orderGameRepository;

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private GameRepository gameRepository;

    private static final String MANAGER_EMAIL = "manager@example.com";
    private static final String MANAGER_PASSWORD = "Strong@Pass1";
    private static final String VALID_COMMENT = "Great game!";
    private static final GameReviewRating VALID_RATING = GameReviewRating.FIVE_STARS;
    private OrderGame testGame;
    private ManagerAccount testManager;
    private static final String BASE_URL = "/api/reviews";

    @BeforeEach
    public void setUp() {
        // Clean up databases in correct order
        for (OrderGame og : orderGameRepository.findAll()) {
            og.setReview(null);
            orderGameRepository.save(og);
        }
        reviewRepository.deleteAll();
        orderGameRepository.deleteAll();
        managerAccountRepository.deleteAll();
        customerOrderRepository.deleteAll();
        gameRepository.deleteAll();

        // Create a test customer order
        CustomerOrder order = new CustomerOrder();
        order = customerOrderRepository.save(order);

        // Create a test game
        Game game = new Game();
        game = gameRepository.save(game);

        // Create test OrderGame with required associations
        testGame = new OrderGame(order, game);
        testGame = orderGameRepository.save(testGame);

        // Create test manager
        testManager = new ManagerAccount(MANAGER_EMAIL, MANAGER_PASSWORD);
        testManager = managerAccountRepository.save(testManager);
    }

    @AfterEach
    public void cleanUp() {
        // First, set the review reference to null in OrderGame
        for (OrderGame og : orderGameRepository.findAll()) {
            og.setReview(null);
            orderGameRepository.save(og);
        }
        
        // Now we can safely delete everything in the correct order
        orderGameRepository.deleteAll(); // Delete order_game records first
        reviewRepository.deleteAll(); // Then delete review records
        managerAccountRepository.deleteAll();
        customerOrderRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateReviewSuccessfully() {
        // Arrange
        ReviewRequestDto request = new ReviewRequestDto();
        request.setComment(VALID_COMMENT);
        request.setRating(VALID_RATING);
        request.setReviewDate(new Date(System.currentTimeMillis()));

        // Debug - Print the OrderGame ID
        System.out.println("OrderGame ID: " + testGame.getOrderGameId());

        // Verify OrderGame exists in database
        assertTrue(orderGameRepository.existsById(testGame.getOrderGameId()), 
            "OrderGame should exist in database");

        // Act
        ResponseEntity<Review> response = client.postForEntity(
            BASE_URL + "/game/" + testGame.getOrderGameId(), 
            request, 
            Review.class
        );

        // Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(VALID_COMMENT, response.getBody().getComment());
        assertEquals(VALID_RATING, response.getBody().getRating());
        assertEquals(request.getReviewDate(), response.getBody().getReviewDate());
    }

    @Test
    @Order(2)
    public void testCreateDuplicateReviewFails() {
        // Arrange
        ReviewRequestDto request = new ReviewRequestDto();
        request.setComment(VALID_COMMENT);
        request.setRating(VALID_RATING);
        request.setReviewDate(new Date(System.currentTimeMillis()));

        // Create first review
        client.postForEntity(BASE_URL + "/game/" + testGame.getOrderGameId(), request, Review.class);

        // Act - Try to create second review
        ResponseEntity<ErrorResponseDto> response = client.postForEntity(
            BASE_URL + "/game/" + testGame.getOrderGameId(), 
            request, 
            ErrorResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Game already has a review.", errorResponse.getError());
    }

    @Test
    @Order(3)
    public void testAddReplyToReview() {
        // Arrange - Create a review first
        ReviewRequestDto reviewRequest = new ReviewRequestDto();
        reviewRequest.setComment(VALID_COMMENT);
        reviewRequest.setRating(VALID_RATING);
        reviewRequest.setReviewDate(new Date(System.currentTimeMillis()));
        ResponseEntity<Review> reviewResponse = client.postForEntity(
            BASE_URL + "/game/" + testGame.getOrderGameId(), 
            reviewRequest, 
            Review.class
        );
        assertNotNull(reviewResponse.getBody(), "Created review should not be null");
        Review review = reviewResponse.getBody();

        // Act - Add reply to the review
        String replyContent = "Thank you for your review!";
        ResponseEntity<Review> response = client.postForEntity(
            BASE_URL + "/" + review.getReviewId() + "/reply?managerEmail=" + MANAGER_EMAIL,
            replyContent,
            Review.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Review updatedReview = response.getBody();
        assertNotNull(updatedReview);
        assertTrue(updatedReview.hasReviewReplies());
        assertEquals(replyContent, updatedReview.getReviewReply(0).getContent());
    }

    @Test
    @Order(4)
    public void testGetReviewsByGame() {
        // Arrange - Create a review first
        ReviewRequestDto request = new ReviewRequestDto();
        request.setComment(VALID_COMMENT);
        request.setRating(VALID_RATING);
        request.setReviewDate(new Date(System.currentTimeMillis()));
        client.postForEntity(BASE_URL + "/game/" + testGame.getOrderGameId(), request, Review.class);

        // Act
        ResponseEntity<Review[]> response = client.getForEntity(
            BASE_URL + "/game/" + testGame.getOrderGameId(),
            Review[].class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Review[] reviews = response.getBody();
        assertNotNull(reviews);
        assertEquals(1, reviews.length);
        assertEquals(VALID_COMMENT, reviews[0].getComment());
    }

    @Test
    @Order(5)
    public void testDeleteReview() {
        // Arrange - Create a review first
        ReviewRequestDto request = new ReviewRequestDto();
        request.setComment(VALID_COMMENT);
        request.setRating(VALID_RATING);
        request.setReviewDate(new Date(System.currentTimeMillis()));
        ResponseEntity<Review> createResponse = client.postForEntity(
            BASE_URL + "/game/" + testGame.getOrderGameId(), 
            request, 
            Review.class
        );
        assertNotNull(createResponse.getBody(), "Created review should not be null");
        Review review = createResponse.getBody();

        // Act
        ResponseEntity<Void> response = client.exchange(
            BASE_URL + "/" + review.getReviewId(),
            HttpMethod.DELETE,
            null,
            Void.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(reviewRepository.findReviewByReviewId(review.getReviewId()));
    }

    /**@Test
    @Order(6)
    public void testGetNonExistentReview() {
        // Act
        ResponseEntity<ErrorResponseDto> response = client.getForEntity(
            BASE_URL + "/999999",
            ErrorResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Review not found.", errorResponse.getError());
    }
}
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Review not found.", errorResponse.getError());
    }**/
}
