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
    
        Review review = new Review(Date.valueOf("2024-10-11"), orderGame);
        review.setComment("Amazing game!");
        review.setRating(Review.GameReviewRating.FIVE_STARS);
    
        // Explicitly set bidirectional association and save orderGame after setting the review
        orderGame.setReview(review); // Set the review in orderGame
        reviewRepo.save(review);      // Save review first
        orderGameRepo.save(orderGame); // Save orderGame again to persist the association
    
        // Act
        Review reviewFromDb = reviewRepo.findReviewByReviewId(review.getReviewId());
    
        // Assert

        // Verify all attributes
        assertNotNull(reviewFromDb, "Review should be saved in the database");
        assertEquals(review.getReviewId(), reviewFromDb.getReviewId());
        assertEquals("Amazing game!", reviewFromDb.getComment());
        assertEquals(Date.valueOf("2024-10-11"), reviewFromDb.getReviewDate());
        assertEquals(Review.GameReviewRating.FIVE_STARS, reviewFromDb.getRating());

        // Verify all associations
        assertNotNull(reviewFromDb.getReviewedGame(), "OrderGame should be associated with Review");
        assertEquals(orderGame.getOrderGameId(), reviewFromDb.getReviewedGame().getOrderGameId());
    }
}
