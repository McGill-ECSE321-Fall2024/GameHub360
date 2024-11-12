package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ReviewRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.ReplyRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;

@SpringBootTest
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderGameRepository orderGameRepository;

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void testSubmitReview() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("exmaple@mcgill.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");

        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        ReviewRequestDto requestDto = new ReviewRequestDto(GameReviewRating.FIVE_STARS, "Great game",
                Date.valueOf(LocalDate.now()), orderGame1.getOrderGameId(), customer.getCustomerId(), 0);

        // Mock repository behavior
        when(orderGameRepository.findOrderGameById(any(Integer.class))).thenReturn(orderGame1);
        when(customerAccountRepository.findCustomerAccountByCustomerId(any(Integer.class))).thenReturn(customer);
        when(replyRepository.findReplyByReplyId(any(Integer.class))).thenReturn(null);
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Review response = reviewService.submitReview(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getRating(), response.getRating());
        assertEquals(requestDto.getComment(), response.getComment());
        assertEquals(requestDto.getReviewDate(), response.getReviewDate());
        assertEquals(orderGame1, response.getReviewedGame());
        verify(orderGameRepository, times(1)).findOrderGameById(orderGame1.getOrderGameId());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customer.getCustomerId());
        verify(replyRepository, times(0)).findReplyByReplyId(0);
    }

    @Test
    public void testSubmitReviewWithInvalidOrderedGame() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("example.@gmail.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");

        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        ReviewRequestDto requestDto = new ReviewRequestDto(GameReviewRating.FIVE_STARS, "Great game",
                Date.valueOf(LocalDate.now()), orderGame1.getOrderGameId(), customer.getCustomerId(), 0);

        // Mock repository behavior
        when(orderGameRepository.findOrderGameById(any(Integer.class))).thenReturn(null);
        when(customerAccountRepository.findCustomerAccountByCustomerId(any(Integer.class))).thenReturn(customer);
        when(replyRepository.findReplyByReplyId(any(Integer.class))).thenReturn(null);
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> reviewService.submitReview(requestDto));

        // Assert
        assertEquals("Ordered game not found", e.getMessage());
        assertEquals(404, e.getStatus().value());
        verify(orderGameRepository, times(1)).findOrderGameById(orderGame1.getOrderGameId());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customer.getCustomerId());
        verify(replyRepository, times(0)).findReplyByReplyId(0);
    }

    @Test
    public void testSubmitReviewWithInvalidCustomer() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("example.@gmail.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");

        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        ReviewRequestDto requestDto = new ReviewRequestDto(GameReviewRating.FIVE_STARS, "Great game",
                Date.valueOf(LocalDate.now()), orderGame1.getOrderGameId(), customer.getCustomerId(), 0);

        // Mock repository behavior
        when(orderGameRepository.findOrderGameById(any(Integer.class))).thenReturn(orderGame1);
        when(customerAccountRepository.findCustomerAccountByCustomerId(any(Integer.class))).thenReturn(null);
        when(replyRepository.findReplyByReplyId(any(Integer.class))).thenReturn(null);
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> reviewService.submitReview(requestDto));

        // Assert
        assertEquals("Customer not found", e.getMessage());
        assertEquals(404, e.getStatus().value());
        verify(orderGameRepository, times(1)).findOrderGameById(orderGame1.getOrderGameId());
        verify(customerAccountRepository, times(1)).findCustomerAccountByCustomerId(customer.getCustomerId());
        verify(replyRepository, times(0)).findReplyByReplyId(0);
    }

    @Test
    public void testViewReviews() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("example.@gmail.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");

        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);
        OrderGame orderGame2 = new OrderGame(customerOrder, game1);

        // Mock repository behavior
        when(reviewRepository.findAll()).thenReturn(List.of(new Review(Date.valueOf(LocalDate.now()), orderGame1),
                new Review(Date.valueOf(LocalDate.now()), orderGame2)));

        // Act
        List<Review> response = reviewService.viewReviews();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    public void testReplyToReview() {
        // Arrange
        ManagerAccount manager = new ManagerAccount("Mike@gmail.com", "Pass1");
        CustomerAccount customer = new CustomerAccount("example.@gmail.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");
        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create CustomerOrder and OrderGame
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        // Create Review and Reply
        Review review = new Review(Date.valueOf(LocalDate.now()), orderGame1);
        Reply reply = new Reply("Thank you for the feedback!", Date.valueOf(LocalDate.now()), review, manager);

        // Mock repository behavior
        when(reviewRepository.findReviewByReviewId(any(Integer.class))).thenReturn(review);
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Review response = reviewService.replyToReview(review.getReviewId(), reply);

        // Assert
        assertNotNull(response);
        assertTrue(response.getReviewReplies().contains(reply));
        verify(reviewRepository, times(1)).findReviewByReviewId(review.getReviewId());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    public void testReplyToReviewWithInvalidReview() {
        // Arrange
        ManagerAccount manager = new ManagerAccount("mike@gmail.com", "pas2");
        CustomerAccount customer = new CustomerAccount("example.@gmail.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");
        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create CustomerOrder and OrderGame
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        // Create Review and Reply
        Review review = new Review(Date.valueOf(LocalDate.now()), orderGame1);
        Reply reply = new Reply("Thank you for the feedback!", Date.valueOf(LocalDate.now()), review, manager);

        // Mock repository behavior
        when(reviewRepository.findReviewByReviewId(any(Integer.class))).thenReturn(null);
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> reviewService.replyToReview(review.getReviewId(), reply));

        // Assert
        assertEquals("Review not found", e.getMessage());
        assertEquals(404, e.getStatus().value());
        verify(reviewRepository, times(1)).findReviewByReviewId(review.getReviewId());
        verify(reviewRepository, times(0)).save(review);
    }

    @Test
    public void testDeleteReview() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("example.@gmail.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");
        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create CustomerOrder and OrderGame
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        // Create Review and Reply
        Review review = new Review(Date.valueOf(LocalDate.now()), orderGame1);

        // Mock repository behavior
        when(reviewRepository.findReviewByReviewId(any(Integer.class))).thenReturn(review);

        // Act
        reviewService.deleteReview(review.getReviewId());

        // Assert
        verify(reviewRepository, times(1)).findReviewByReviewId(review.getReviewId());
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    public void testDeleteReviewWithInvalidReview() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("example.@gmail.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");
        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create CustomerOrder and OrderGame
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        // Create Review and Reply
        Review review = new Review(Date.valueOf(LocalDate.now()), orderGame1);

        // Mock repository behavior
        when(reviewRepository.findReviewByReviewId(any(Integer.class))).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> reviewService.deleteReview(review.getReviewId()));

        // Assert
        assertEquals("Review not found", e.getMessage());
        assertEquals(404, e.getStatus().value());
        verify(reviewRepository, times(1)).findReviewByReviewId(review.getReviewId());
        verify(reviewRepository, times(0)).delete(review);
    }

}