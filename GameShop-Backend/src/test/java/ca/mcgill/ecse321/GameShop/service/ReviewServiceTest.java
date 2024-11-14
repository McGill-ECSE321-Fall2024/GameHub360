package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.GameShop.dto.ReplyRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.*;
import ca.mcgill.ecse321.GameShop.repository.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderGameRepository orderGameRepository;

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private ManagerAccountRepository managerAccountRepository;

    @Mock
    private GameRepository gameRepository;

    private ReviewRequestDto reviewRequestDto;
    private ReplyRequestDto replyRequestDto;
    private OrderGame orderGame;
    private Review review;
    private CustomerAccount customer;
    private ManagerAccount manager;
    private Game game;

    @BeforeEach
    public void setUp() {
        customer = new CustomerAccount("customer@example.com", "securePassword");
        ReflectionTestUtils.setField(customer, "customerId", 1);
        manager = new ManagerAccount("manager@example.com", "strongPassword");
        game = new Game();
        game.setDescription("Test Game");

        orderGame = new OrderGame();
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setOrderedBy(customer);
        orderGame.setCustomerOrder(customerOrder);
        orderGame.setGame(game);

        review = new Review();
        review.setReviewDate(new java.sql.Date(System.currentTimeMillis()));
        review.setComment("Great game!");
        review.setRating(Review.GameReviewRating.FIVE_STARS);
        ReflectionTestUtils.setField(review, "reviewReplies", new ArrayList<Reply>());
        
        orderGame.setReview(review);
        review.setReviewedGame(orderGame);

        reviewRequestDto = new ReviewRequestDto(Review.GameReviewRating.FIVE_STARS, 1, "Great game!");
        replyRequestDto = new ReplyRequestDto("Thank you for your feedback!", 1);
    }

    @AfterEach
    public void tearDown() {
        reviewRepository.deleteAll();
        replyRepository.deleteAll();
        orderGameRepository.deleteAll();
        gameRepository.deleteAll();
        customerAccountRepository.deleteAll();
        managerAccountRepository.deleteAll();
    }

    @Test
    public void testSubmitReview_Success() {
        // Arrange
        when(orderGameRepository.findOrderGameById(anyInt())).thenReturn(orderGame);
        when(customerAccountRepository.findCustomerAccountByCustomerId(1)).thenReturn(customer);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
            Review savedReview = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedReview, "reviewId", 1);
            
            // If the review has a reviewedGame, maintain the bidirectional relationship
            OrderGame game = savedReview.getReviewedGame();
            if (game != null) {
                game.setReview(savedReview);
            }
            
            return savedReview;
        });

        // Act
        Review createdReview = reviewService.submitReview(1, reviewRequestDto);

        // Assert
        assertNotNull(createdReview);
        assertEquals(orderGame, createdReview.getReviewedGame());
        assertEquals("Great game!", createdReview.getComment());
        assertEquals(orderGame.getReview(), createdReview);
        verify(reviewRepository, times(2)).save(any(Review.class));
    }

    @Test
    public void testSubmitReview_OrderGameNotFound() {
        // Arrange
        when(orderGameRepository.findOrderGameById(anyInt())).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class,
                () -> reviewService.submitReview(1, reviewRequestDto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("OrderGame not found", exception.getMessage());
    }

    @Test
    public void testSubmitReview_CustomerNotFound() {
        // Arrange
        when(orderGameRepository.findOrderGameById(anyInt())).thenReturn(orderGame);
        when(customerAccountRepository.findCustomerAccountByCustomerId(anyInt())).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class,
                () -> reviewService.submitReview(1, reviewRequestDto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    public void testSubmitReview_CustomerNotOwnerOfOrder() {
        // Arrange
        CustomerAccount otherCustomer = new CustomerAccount("othercustomer@example.com", "anotherPassword");
        orderGame.getCustomerOrder().setOrderedBy(otherCustomer);

        when(orderGameRepository.findOrderGameById(anyInt())).thenReturn(orderGame);
        when(customerAccountRepository.findCustomerAccountByCustomerId(anyInt())).thenReturn(customer);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class,
                () -> reviewService.submitReview(1, reviewRequestDto));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Customer is not the owner of the order", exception.getMessage());
    }

    @Test
    public void testGetGameReviews_Success() {
        // Arrange
        game.addOrder(orderGame);
        orderGame.setReview(review);

        when(gameRepository.findGameByGameEntityId(anyInt())).thenReturn(game);

        // Act
        List<Review> reviews = reviewService.getGameReviews(1);

        // Assert
        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(review, reviews.get(0));
    }

    @Test
    public void testGetGameReviews_GameNotFound() {
        // Arrange
        when(gameRepository.findGameByGameEntityId(anyInt())).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> reviewService.getGameReviews(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game not found", exception.getMessage());
    }

    @Test
    public void testReplyToReview_Success() {
        // Arrange
        when(reviewRepository.findReviewByReviewId(anyInt())).thenReturn(review);
        when(managerAccountRepository.findManagerAccountByStaffId(anyInt())).thenReturn(manager);
        when(replyRepository.save(any(Reply.class))).thenAnswer(invocation -> {
            Reply savedReply = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedReply, "replyId", 1);
            return savedReply;
        });

        // Act
        Reply createdReply = reviewService.replyToReview(1, replyRequestDto);

        // Assert
        assertNotNull(createdReply);
        assertEquals("Thank you for your feedback!", createdReply.getContent());
        verify(replyRepository, times(1)).save(any(Reply.class));
    }

    @Test
    public void testReplyToReview_ReviewNotFound() {
        // Arrange
        when(reviewRepository.findReviewByReviewId(anyInt())).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class,
                () -> reviewService.replyToReview(1, replyRequestDto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Review not found", exception.getMessage());
    }

    @Test
    public void testReplyToReview_ManagerNotFound() {
        // Arrange
        when(reviewRepository.findReviewByReviewId(anyInt())).thenReturn(review);
        when(managerAccountRepository.findManagerAccountByStaffId(anyInt())).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class,
                () -> reviewService.replyToReview(1, replyRequestDto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Manager not found", exception.getMessage());
    }

    @Test
    public void testDeleteReview_Success() {
        // Arrange
        when(reviewRepository.findReviewByReviewId(anyInt())).thenReturn(review);
        doNothing().when(reviewRepository).delete(any(Review.class));

        // Act
        reviewService.deleteReview(1);

        // Assert
        verify(reviewRepository, times(1)).delete(any(Review.class));
    }

    @Test
    public void testDeleteReview_ReviewNotFound() {
        // Arrange
        when(reviewRepository.findReviewByReviewId(anyInt())).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> reviewService.deleteReview(1));
        assertEquals("Review not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testDeleteReply_Success() {
        // Arrange
        Reply reply = new Reply();
        when(replyRepository.findReplyByReplyId(anyInt())).thenReturn(reply);
        doNothing().when(replyRepository).delete(any(Reply.class));

        // Act
        reviewService.deleteReply(1);

        // Assert
        verify(replyRepository, times(1)).delete(any(Reply.class));
    }

    @Test
    public void testDeleteReply_ReplyNotFound() {
        // Arrange
        when(replyRepository.findReplyByReplyId(anyInt())).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> reviewService.deleteReply(1));
        assertEquals("Reply not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
