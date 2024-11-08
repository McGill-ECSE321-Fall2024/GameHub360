package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ReviewRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderGameRepository orderGameRepository;

    @Mock
    private ManagerAccountRepository managerAccountRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private OrderGame orderGame;
    private ManagerAccount manager;
    private ReviewRequestDto reviewRequestDto;

    @BeforeEach
    public void setup() {
        // Setup test data
        orderGame = new OrderGame();
        try {
            java.lang.reflect.Field idField = OrderGame.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(orderGame, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Create review with proper initialization
        review = new Review();
        try {
            // Initialize the reviewReplies list using reflection
            java.lang.reflect.Field repliesField = Review.class.getDeclaredField("reviewReplies");
            repliesField.setAccessible(true);
            repliesField.set(review, new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        review.setRating(GameReviewRating.FIVE_STARS);
        review.setComment("Great game!");
        review.setReviewDate(new Date(System.currentTimeMillis()));

        manager = new ManagerAccount();

        reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setRating(GameReviewRating.FIVE_STARS);
        reviewRequestDto.setComment("Great game!");
        reviewRequestDto.setReviewDate(new Date(System.currentTimeMillis()));
    }

    @Test
    public void testCreateReviewSuccess() {
        // Make sure orderGame doesn't have a review
        orderGame.setReview(null);
        
        when(orderGameRepository.findOrderGameById(1)).thenReturn(orderGame);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review created = reviewService.createReview(1, reviewRequestDto);

        assertNotNull(created);
        assertEquals(GameReviewRating.FIVE_STARS, created.getRating());
        assertEquals("Great game!", created.getComment());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    public void testCreateReviewGameNotFound() {
        when(orderGameRepository.findOrderGameById(1)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, 
            () -> reviewService.createReview(1, reviewRequestDto));
        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void testCreateReviewGameAlreadyReviewed() {
        orderGame.setReview(review);
        when(orderGameRepository.findOrderGameById(1)).thenReturn(orderGame);

        GameShopException exception = assertThrows(GameShopException.class, 
            () -> reviewService.createReview(1, reviewRequestDto));
        assertEquals("Game already has a review.", exception.getMessage());
    }

    @Test
    public void testGetReviewsByGameSuccess() {
        orderGame.setReview(review);
        when(orderGameRepository.findOrderGameById(1)).thenReturn(orderGame);

        List<Review> reviews = reviewService.getReviewsByGame(1);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(review, reviews.get(0));
    }

    @Test
    public void testGetReviewsByGameNotFound() {
        when(orderGameRepository.findOrderGameById(1)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, 
            () -> reviewService.getReviewsByGame(1));
        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void testAddReplyToReviewSuccess() {
        when(reviewRepository.findReviewByReviewId(1)).thenReturn(review);
        when(managerAccountRepository.findManagerAccountByEmail("manager@email.com")).thenReturn(manager);

        Reply reply = reviewService.addReplyToReview(1, "Thank you for your review!", "manager@email.com");

        assertNotNull(reply);
        assertEquals("Thank you for your review!", reply.getContent());
        assertEquals(manager, reply.getReviewer());
    }

    @Test
    public void testAddReplyToReviewNotFound() {
        when(reviewRepository.findReviewByReviewId(1)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, 
            () -> reviewService.addReplyToReview(1, "Thank you!", "manager@email.com"));
        assertEquals("Review not found.", exception.getMessage());
    }

    @Test
    public void testAddReplyManagerNotFound() {
        when(reviewRepository.findReviewByReviewId(1)).thenReturn(review);
        when(managerAccountRepository.findManagerAccountByEmail("manager@email.com")).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, 
            () -> reviewService.addReplyToReview(1, "Thank you!", "manager@email.com"));
        assertEquals("Manager not found.", exception.getMessage());
    }

    @Test
    public void testAddReplyEmptyContent() {
        when(reviewRepository.findReviewByReviewId(1)).thenReturn(review);
        when(managerAccountRepository.findManagerAccountByEmail("manager@email.com")).thenReturn(manager);

        GameShopException exception = assertThrows(GameShopException.class, 
            () -> reviewService.addReplyToReview(1, "", "manager@email.com"));
        assertEquals("Reply content cannot be empty.", exception.getMessage());
    }

    @Test
    public void testDeleteReviewSuccess() {
        when(reviewRepository.findReviewByReviewId(1)).thenReturn(review);

        assertDoesNotThrow(() -> reviewService.deleteReview(1));
        verify(reviewRepository).delete(review);
    }

    @Test
    public void testDeleteReviewNotFound() {
        when(reviewRepository.findReviewByReviewId(1)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, 
            () -> reviewService.deleteReview(1));
        assertEquals("Review not found.", exception.getMessage());
    }

    @Test
    public void testGetReviewSuccess() {
        when(reviewRepository.findReviewByReviewId(1)).thenReturn(review);

        Review found = reviewService.getReview(1);
        assertNotNull(found);
        assertEquals(review.getReviewId(), found.getReviewId());
    }

    @Test
    public void testGetReviewNotFound() {
        when(reviewRepository.findReviewByReviewId(1)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, 
            () -> reviewService.getReview(1));
        assertEquals("Review not found.", exception.getMessage());
    }
}
