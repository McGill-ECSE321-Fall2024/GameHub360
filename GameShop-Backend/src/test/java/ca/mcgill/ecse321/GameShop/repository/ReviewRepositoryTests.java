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
        CustomerAccount customer = new CustomerAccount("email@example.com", "password");
        customer = customerAccountRepo.save(customer);
    
        GameCategory category = new GameCategory(true, "Action");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        category = gameCategoryRepo.save(category);
    
        Game game = new Game("GameTitle", "Description", "ImageURL", 10, true, 19.99, category);
        game = gameRepo.save(game);
 
        PaymentDetails payment = new PaymentDetails("Cardholder", "H3H 1K1", 12345678, 12, 2024, customer);
        payment = paymentDetailsRepo.save(payment);

        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf("2024-10-10"), customer, payment);
        customerOrder = customerOrderRepo.save(customerOrder);

        OrderGame orderGame = new OrderGame(customerOrder, game);
        orderGame = orderGameRepo.save(orderGame);

        // Define variables to avoid hardcoding
        String comment = "Amazing game!";
        Date reviewDate = Date.valueOf("2024-10-11");
        Review.GameReviewRating rating = Review.GameReviewRating.FIVE_STARS;

        Review review = new Review(reviewDate, orderGame);
        review.setComment(comment);
        review.setRating(rating);
    
        // Explicitly set bidirectional association and save orderGame after setting the review
        orderGame.setReview(review);
        reviewRepo.save(review);
        orderGameRepo.save(orderGame);
    
        // Act
        Review reviewFromDb = reviewRepo.findReviewByReviewId(review.getReviewId());
    
        // Assert

        // Verify Attributes
        assertNotNull(reviewFromDb);
        assertEquals(review.getReviewId(), reviewFromDb.getReviewId());
        assertEquals(comment, reviewFromDb.getComment());
        assertEquals(reviewDate, reviewFromDb.getReviewDate());
        assertEquals(rating, reviewFromDb.getRating());

        // Verify associations
        assertNotNull(reviewFromDb.getReviewedGame());
        assertEquals(orderGame.getOrderGameId(), reviewFromDb.getReviewedGame().getOrderGameId());
    }
}
