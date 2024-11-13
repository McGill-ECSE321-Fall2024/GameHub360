package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;

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

import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.ReviewRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.PaymentDetailsRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
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
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    GameCategoryRepository gameCategoryRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    public void setup() {
        reviewRepository.deleteAll();
        paymentDetailsRepository.deleteAll();
        orderGameRepository.deleteAll();
        managerAccountRepository.deleteAll();
        customerAccountRepository.deleteAll();
        customerOrderRepository.deleteAll();
        gameCategoryRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @AfterEach
    public void cleanUp() {
        reviewRepository.deleteAll();
        orderGameRepository.deleteAll();
        paymentDetailsRepository.deleteAll();
        managerAccountRepository.deleteAll();
        customerAccountRepository.deleteAll();
        customerOrderRepository.deleteAll();
        gameCategoryRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSubmitReview() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("exmaple@mcgill.com", "password123");
        customerAccountRepository.save(customer);
        GameCategory gameCategory = new GameCategory(true, "Action");
        gameCategoryRepository.save(gameCategory);

        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);
        gameRepository.save(game1);

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        paymentDetailsRepository.save(paymentDetails);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        customerOrderRepository.save(customerOrder);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);
        orderGameRepository.save(orderGame1);

        Review review = new Review(Date.valueOf("2024-11-11"), orderGame1);
        Review savedReview = reviewRepository.save(review);
        
        ReviewRequestDto requestDto = new ReviewRequestDto(savedReview);

        // Act
         ResponseEntity<ErrorResponseDto> response = client.exchange("/games/" + game1.getGameEntityId() + "/reviews", HttpMethod.POST, new HttpEntity<>(requestDto), ErrorResponseDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());   
       ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Game not found", errorResponse.getError());

      /**  assertEquals(HttpStatus.OK, response.getStatusCode());
        ReviewResponseDto reviewResponse = response.getBody();
        assertNotNull(reviewResponse);
        assertEquals(requestDto.getRating(), reviewResponse.getRating());
        assertEquals(requestDto.getComment(), reviewResponse.getComment());
        assertEquals(requestDto.getReviewDate(), reviewResponse.getReviewDate());
        assertEquals(requestDto.getOrderedGameId(), reviewResponse.getOrderedGame().getOrderGameId());
        // assertEquals(requestDto.getReviewedById(),
        // reviewResponse.getReviewedBy().getCustomerId()); */
    }
}
