package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ReplyRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReplyResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewListDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewResponseDto;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.PaymentDetailsRepository;
import ca.mcgill.ecse321.GameShop.repository.ReplyRepository;
import ca.mcgill.ecse321.GameShop.repository.ReviewRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ReviewIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private OrderGameRepository orderGameRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    private static final String VALID_EMAIL = "example@mcgill.ca";
    private static final String VALID_PASSWORD = "Strong@Password1";

    @BeforeEach
    public void setup() {
        replyRepository.deleteAll();
        managerAccountRepository.deleteAll();
        orderGameRepository.deleteAll();
        customerOrderRepository.deleteAll();
        gameRepository.deleteAll();
        reviewRepository.deleteAll();
        customerAccountRepository.deleteAll();

    }

    @AfterEach
    public void cleanup() {
        replyRepository.deleteAll();
        customerAccountRepository.deleteAll();
        managerAccountRepository.deleteAll();
        orderGameRepository.deleteAll();
        customerOrderRepository.deleteAll();
        gameRepository.deleteAll();
        reviewRepository.deleteAll();
        customerAccountRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSubmitReviewSuccessfully() {
        // Arrange
        Game game = new Game();
        game.setName("Horror Game");
        gameRepository.save(game);

        CustomerAccount customer = new CustomerAccount(VALID_EMAIL, VALID_PASSWORD);
        customerAccountRepository.save(customer);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardOwner(customer);
        paymentDetailsRepository.save(paymentDetails);

        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf("2024-10-22"), customer, paymentDetails);
        customerOrderRepository.save(customerOrder);

        OrderGame orderGame = new OrderGame();
        orderGame.setGame(game);
        orderGame.setCustomerOrder(customerOrder);
        orderGameRepository.save(orderGame);

        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setRating(GameReviewRating.FIVE_STARS);
        reviewRequestDto.setCustomerId(customer.getCustomerId());
        reviewRequestDto.setComment("Fantastic game!!!");

        // Act
        ResponseEntity<ReviewResponseDto> response = restTemplate.postForEntity(
                "/games/" + orderGame.getOrderGameId() + "/reviews", reviewRequestDto, ReviewResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReviewResponseDto reviewResponseDto = response.getBody();
        assertNotNull(reviewResponseDto);
        assertEquals(GameReviewRating.FIVE_STARS, reviewResponseDto.getRating());
        assertEquals("Fantastic game!!!", reviewResponseDto.getComment());
    }

    @Test
    @Order(2)
    public void testViewReviewsSuccessfully() {
        // Arrange
        Game game = new Game();
        game.setName("Horror Game");
        gameRepository.save(game);
        // first review
        CustomerAccount customer = new CustomerAccount(VALID_EMAIL, VALID_PASSWORD);
        customerAccountRepository.save(customer);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardOwner(customer);
        paymentDetailsRepository.save(paymentDetails);

        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf("2024-10-22"), customer, paymentDetails);
        customerOrderRepository.save(customerOrder);

        OrderGame orderGame = new OrderGame();
        orderGame.setGame(game);
        orderGame.setCustomerOrder(customerOrder);
        orderGameRepository.save(orderGame);

        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setRating(GameReviewRating.FIVE_STARS);
        reviewRequestDto.setCustomerId(customer.getCustomerId());
        reviewRequestDto.setComment("Fantastic game!!!");

        restTemplate.postForEntity("/games/" + orderGame.getOrderGameId() + "/reviews", reviewRequestDto,
                ReviewResponseDto.class);

        // second review
        CustomerAccount customer2 = new CustomerAccount("example2@mail.mcgill.ca", VALID_PASSWORD);
        customerAccountRepository.save(customer2);

        PaymentDetails paymentDetails2 = new PaymentDetails();
        paymentDetails.setCardOwner(customer2);
        paymentDetailsRepository.save(paymentDetails2);

        CustomerOrder customerOrder2 = new CustomerOrder(Date.valueOf("2024-10-22"), customer2, paymentDetails2);
        customerOrderRepository.save(customerOrder2);

        OrderGame orderGame2 = new OrderGame();
        orderGame2.setGame(game);
        orderGame2.setCustomerOrder(customerOrder2);
        orderGameRepository.save(orderGame2);

        ReviewRequestDto reviewRequestDto2 = new ReviewRequestDto();
        reviewRequestDto2.setRating(GameReviewRating.THREE_STARS);
        reviewRequestDto2.setCustomerId(customer2.getCustomerId());
        reviewRequestDto2.setComment("Actually, Not too bad!");

        restTemplate.postForEntity("/games/" + orderGame2.getOrderGameId() + "/reviews", reviewRequestDto2,
                ReviewResponseDto.class);

        // Act
        ResponseEntity<ReviewListDto> response = restTemplate.getForEntity(
                "/games/" + game.getGameEntityId() + "/reviews",
                ReviewListDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReviewListDto reviewListDto = response.getBody();
        assertNotNull(reviewListDto);
        assertEquals(2, reviewListDto.getReviews().size());

        ReviewResponseDto reviewResponseDto = reviewListDto.getReviews().get(0);
        assertEquals(GameReviewRating.FIVE_STARS, reviewResponseDto.getRating());
        assertEquals("Fantastic game!!!", reviewResponseDto.getComment());

        ReviewResponseDto reviewResponseDto2 = reviewListDto.getReviews().get(1);
        assertEquals(GameReviewRating.THREE_STARS, reviewResponseDto2.getRating());
        assertEquals("Actually, Not too bad!", reviewResponseDto2.getComment());
    }

    @Test
    @Order(3)
    public void testSubmitReviewNotFoundFails() {
        // Arrange
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setRating(GameReviewRating.FIVE_STARS);
        reviewRequestDto.setCustomerId(222);
        reviewRequestDto.setComment("Fantastic game!!!");

        // Act
        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity("/games/222/reviews", reviewRequestDto,
                ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals("OrderGame not found", errorResponseDto.getError());
    }

    @Test
    @Order(4)
    public void testSubmitReviewForbiddenFails() {
        // Arrange
        Game game = new Game();
        game.setName("Horror Game");
        gameRepository.save(game);

        CustomerAccount customer = new CustomerAccount(VALID_EMAIL, VALID_PASSWORD);
        customerAccountRepository.save(customer);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardOwner(customer);
        paymentDetailsRepository.save(paymentDetails);

        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf("2024-10-22"), customer, paymentDetails);
        customerOrderRepository.save(customerOrder);

        OrderGame orderGame = new OrderGame();
        orderGame.setGame(game);
        orderGame.setCustomerOrder(customerOrder);
        orderGameRepository.save(orderGame);

        CustomerAccount customer2 = new CustomerAccount("example2@mcgill.ca", VALID_PASSWORD);
        customerAccountRepository.save(customer2);

        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setRating(GameReviewRating.FIVE_STARS);
        reviewRequestDto.setCustomerId(customer2.getCustomerId());
        reviewRequestDto.setComment("Fantastic game!!!");

        // Act
        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(
                "/games/" + orderGame.getOrderGameId() + "/reviews",
                reviewRequestDto, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals("Customer is not the owner of the order", errorResponseDto.getError());
    }

    @Test
    @Order(5)
    public void testSubmitReviewInvalidRatingFails() {
        // Arrange
        Game game = new Game();
        game.setName("Horror Game");
        gameRepository.save(game);

        CustomerAccount customer = new CustomerAccount(VALID_EMAIL, VALID_PASSWORD);
        customerAccountRepository.save(customer);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardOwner(customer);
        paymentDetailsRepository.save(paymentDetails);

        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf("2024-10-22"), customer, paymentDetails);
        customerOrderRepository.save(customerOrder);

        OrderGame orderGame = new OrderGame();
        orderGame.setGame(game);
        orderGame.setCustomerOrder(customerOrder);
        orderGameRepository.save(orderGame);

        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setRating(null);
        reviewRequestDto.setCustomerId(customer.getCustomerId());
        reviewRequestDto.setComment("Fantastic game!!!");

        // Act
        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(
                "/games/" + orderGame.getOrderGameId() + "/reviews",
                reviewRequestDto, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals("Validation failed", errorResponseDto.getError());
    }

    @Test
    @Order(6)
    public void testSubmitReviewInvalidCustomerIdFails() {
        // Arrange
        Game game = new Game();
        game.setName("Horror Game");
        gameRepository.save(game);

        CustomerAccount customer = new CustomerAccount(VALID_EMAIL, VALID_PASSWORD);
        customerAccountRepository.save(customer);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardOwner(customer);
        paymentDetailsRepository.save(paymentDetails);

        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf("2024-10-22"), customer, paymentDetails);
        customerOrderRepository.save(customerOrder);

        OrderGame orderGame = new OrderGame();
        orderGame.setGame(game);
        orderGame.setCustomerOrder(customerOrder);
        orderGameRepository.save(orderGame);

        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setRating(GameReviewRating.FIVE_STARS);
        reviewRequestDto.setCustomerId(222);
        reviewRequestDto.setComment("Fantastic game!!!");
        // Act
        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(
                "/games/" + orderGame.getOrderGameId() + "/reviews",
                reviewRequestDto, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals("Customer not found", errorResponseDto.getError());

    }

    @Test
    @Order(7)
    public void testViewReviewsNotFoundFails() {
        // Arrange
        // Act
        ResponseEntity<ErrorResponseDto> response = restTemplate.getForEntity("/games/222/reviews",
                ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals("Game not found", errorResponseDto.getError());
    }

    @Test
    @Order(8)
    public void testReplyToReviewNotFoundFails() {
        // Arrange
        ManagerAccount manager = new ManagerAccount("manager@gmail.com", VALID_PASSWORD);
        managerAccountRepository.save(manager);

        ReplyRequestDto replyRequestDto = new ReplyRequestDto();
        replyRequestDto.setContent("Thank you for your feedback!");
        replyRequestDto.setManagerId(manager.getStaffId());

        // Act
        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity("/games/reviews/222/reply",
                replyRequestDto,
                ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals("Review not found", errorResponseDto.getError());
    }

    @Test
    @Order(9)
    public void testReplyToReviewNonExistentManagerFails() {
        // Arrange
        ReplyRequestDto replyRequestDto = new ReplyRequestDto();
        replyRequestDto.setContent("Thank you for your feedback!");
        replyRequestDto.setManagerId(33);

        Review review = new Review();
        reviewRepository.save(review);

        // Act
        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(
                "/games/reviews/" + review.getReviewId() + "/reply",
                replyRequestDto,
                ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals("Manager not found", errorResponseDto.getError());
    }

    @Test
    @Order(10)
    public void testReplyToReviewSuccessfully() {
        // Arrange
        ManagerAccount manager = new ManagerAccount("manager@gmail.com", VALID_PASSWORD);
        managerAccountRepository.save(manager);

        Game game = new Game();
        game.setName("Fun Game");
        gameRepository.save(game);

        CustomerAccount customer = new CustomerAccount(VALID_EMAIL, VALID_PASSWORD);
        customerAccountRepository.save(customer);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardOwner(customer);
        paymentDetailsRepository.save(paymentDetails);

        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf("2024-10-22"), customer, paymentDetails);
        customerOrderRepository.save(customerOrder);

        OrderGame orderGame = new OrderGame();
        orderGame.setGame(game);
        orderGame.setCustomerOrder(customerOrder);
        orderGameRepository.save(orderGame);

        Review review = new Review();
        review.setReviewDate(Date.valueOf("2024-10-22"));
        review.setReviewedGame(orderGame);
        reviewRepository.save(review);

        ReplyRequestDto replyRequestDto = new ReplyRequestDto();
        replyRequestDto.setContent("Thank you for your feedback!");
        replyRequestDto.setManagerId(manager.getStaffId());

        // Act
        ResponseEntity<ReplyResponseDto> response = restTemplate.postForEntity(
                "/games/reviews/" + review.getReviewId() + "/reply",
                replyRequestDto,
                ReplyResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReplyResponseDto reviewResponseDto = response.getBody();
        assertNotNull(reviewResponseDto);
        assertEquals("Thank you for your feedback!", reviewResponseDto.getContent());
    }

    @Test
    @Order(11)
    public void testDeleteReviewSuccessfully() {
        // Arrange
        ManagerAccount manager = new ManagerAccount("manager@gmail.com", VALID_PASSWORD);
        managerAccountRepository.save(manager);

        Game game = new Game();
        game.setName("Fun Game");
        gameRepository.save(game);

        CustomerAccount customer = new CustomerAccount(VALID_EMAIL, VALID_PASSWORD);
        customerAccountRepository.save(customer);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardOwner(customer);
        paymentDetailsRepository.save(paymentDetails);

        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf("2024-10-22"), customer, paymentDetails);
        customerOrderRepository.save(customerOrder);

        OrderGame orderGame = new OrderGame();
        orderGame.setGame(game);
        orderGame.setCustomerOrder(customerOrder);
        orderGameRepository.save(orderGame);

        Review review = new Review();
        review.setReviewDate(Date.valueOf("2024-10-22"));
        review.setReviewedGame(orderGame);
        reviewRepository.save(review);

        // Act
        restTemplate.delete("/games/reviews/" + review.getReviewId());

        // Assert
        assertEquals(0, reviewRepository.count());
    }

    @Test
    @Order(12)
    public void testDeleteReviewNotFoundFails() {
        // Arrange
        // Act
        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange("/games/reviews/222", HttpMethod.DELETE, null,
                ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals("Review not found", errorResponseDto.getError());
    }

    @Test
    @Order(13)
    public void testDeleteReplySuccessfully() {
        // Arrange
        ManagerAccount manager = new ManagerAccount("manager@gmail.com", VALID_PASSWORD);
        managerAccountRepository.save(manager);

        Reply reply = new Reply();
        reply.setContent("Thank you for your feedback!");
        reply.setReplyDate(Date.valueOf("2024-10-22"));
        reply.setReviewer(manager);
        replyRepository.save(reply);

        // Act
        restTemplate.delete("/games/reviews/reply/" + reply.getReplyId());

        // Assert
        assertEquals(0, replyRepository.count());
    }

    @Test
    @Order(14)
    public void testDeleteReplyNotFoundFails() {
        // Arrange
        // Act
        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange("/games/reviews/reply/222", HttpMethod.DELETE,
                null, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals("Reply not found", errorResponseDto.getError());
    }
}