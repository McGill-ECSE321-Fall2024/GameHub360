package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.model.*;
import java.sql.Date;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private CustomerAccountRepository customerAccountRepo;

    @Autowired
    private CustomerOrderRepository customerOrderRepo;

    @Autowired
    private OrderGameRepository orderGameRepo;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepo;

    @Autowired
    private GameCategoryRepository gameCategoryRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        orderGameRepo.deleteAll();
        reviewRepo.deleteAll();
        customerOrderRepo.deleteAll();
        paymentDetailsRepo.deleteAll();
        gameRepo.deleteAll();
        customerAccountRepo.deleteAll();
        gameCategoryRepo.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveReview() {
        // Arrange
        String customerEmail = "email@example.com";
        String customerPassword = "password";
        CustomerAccount customer = new CustomerAccount(customerEmail, customerPassword);
        customer = customerAccountRepo.save(customer);

        boolean categoryIsAvailable = true;
        String categoryName = "Action";
        GameCategory category = new GameCategory(categoryIsAvailable, categoryName);
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        category = gameCategoryRepo.save(category);

        String gameTitle = "GameTitle";
        String gameDescription = "Description";
        String gameImageUrl = "ImageURL";
        int gameStock = 10;
        boolean gameIsAvailable = true;
        double gamePrice = 19.99;
        Game game = new Game(gameTitle, gameDescription, gameImageUrl, gameStock, gameIsAvailable, gamePrice, category);
        game = gameRepo.save(game);

        String cardName = "Cardholder";
        String postalCode = "H3H 1K1";
        int cardNumber = 12345678;
        int expMonth = 12;
        int expYear = 2024;
        PaymentDetails payment = new PaymentDetails(cardName, postalCode, cardNumber, expMonth, expYear, customer);
        payment = paymentDetailsRepo.save(payment);

        Date orderDate = Date.valueOf("2024-10-10");
        CustomerOrder customerOrder = new CustomerOrder(orderDate, customer, payment);
        customerOrder = customerOrderRepo.save(customerOrder);

        OrderGame orderGame = new OrderGame(customerOrder, game);
        orderGame = orderGameRepo.save(orderGame);

        String reviewComment = "Amazing game!";
        Date reviewDate = Date.valueOf("2024-10-11");
        Review.GameReviewRating reviewRating = Review.GameReviewRating.FIVE_STARS;
        Review review = new Review(reviewDate, orderGame);
        review.setComment(reviewComment);
        review.setRating(reviewRating);

        orderGame.setReview(review);
        reviewRepo.save(review);
        orderGameRepo.save(orderGame);

        // Act
        Review reviewFromDb = reviewRepo.findReviewByReviewId(review.getReviewId());

        // Assert
        assertNotNull(reviewFromDb);
        assertEquals(review.getReviewId(), reviewFromDb.getReviewId());
        assertEquals(reviewComment, reviewFromDb.getComment());
        assertEquals(reviewDate, reviewFromDb.getReviewDate());
        assertEquals(reviewRating, reviewFromDb.getRating());

        assertNotNull(reviewFromDb.getReviewedGame());
        assertEquals(orderGame.getOrderGameId(), reviewFromDb.getReviewedGame().getOrderGameId());
    }
}