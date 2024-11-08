package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ReviewRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.sql.Date;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderGameRepository orderGameRepository;

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    /**
     * Creates a new review for a specific game
     * 
     * @param gameId The ID of the game being reviewed
     * @param reviewRequestDto The review details
     * @return The created Review
     * @throws GameShopException if the game doesn't exist or if review data is invalid
     */
    @Transactional
    public Review createReview(int gameId, ReviewRequestDto reviewRequestDto) {
        OrderGame orderGame = orderGameRepository.findOrderGameById(gameId);
        if (orderGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found.");
        }

        // Check if game already has a review
        if (orderGame.getReview() != null) {
            throw new GameShopException(HttpStatus.CONFLICT, "Game already has a review.");
        }

        // Validate review data
        if (reviewRequestDto.getRating() == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Rating is required.");
        }

        Review review = new Review(reviewRequestDto.getReviewDate(), orderGame);
        review.setRating(reviewRequestDto.getRating());
        review.setComment(reviewRequestDto.getComment());

        return reviewRepository.save(review);
    }

    /**
     * Retrieves all reviews for a specific game
     * 
     * @param gameId The ID of the game
     * @return List of reviews for the game
     * @throws GameShopException if the game doesn't exist
     */
    @Transactional
    public List<Review> getReviewsByGame(int gameId) {
        OrderGame orderGame = orderGameRepository.findOrderGameById(gameId);
        if (orderGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found.");
        }

        // Since we don't have a findReviewsByOrderGame method, we can check if the game has a review
        // and return it as a single-element list
        Review review = orderGame.getReview();
        if (review == null) {
            return List.of(); // Return empty list if no review exists
        }
        return List.of(review);
    }

    /**
     * Adds a reply to a specific review
     * 
     * @param reviewId The ID of the review being replied to
     * @param content The content of the reply
     * @param managerEmail The email of the manager making the reply
     * @return The created Reply
     * @throws GameShopException if the review doesn't exist or if manager is not found
     */
    @Transactional
    public Reply addReplyToReview(int reviewId, String content, String managerEmail) {
        Review review = reviewRepository.findReviewByReviewId(reviewId);
        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Review not found.");
        }

        ManagerAccount manager = managerAccountRepository.findManagerAccountByEmail(managerEmail);
        if (manager == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Manager not found.");
        }

        if (content == null || content.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Reply content cannot be empty.");
        }

        // Create reply with current date
        Date replyDate = new Date(System.currentTimeMillis());
        Reply reply = review.addReviewReply(content, replyDate, manager);

        return reply;
    }

    /**
     * Deletes a specific review
     * 
     * @param reviewId The ID of the review to delete
     * @throws GameShopException if the review doesn't exist
     */
    @Transactional
    public void deleteReview(int reviewId) {
        Review review = reviewRepository.findReviewByReviewId(reviewId);
        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Review not found.");
        }

        reviewRepository.delete(review);
    }

    /**
     * Helper method to get a review by ID
     * 
     * @param reviewId The ID of the review
     * @return The Review object
     * @throws GameShopException if the review doesn't exist
     */
    @Transactional
    public Review getReview(int reviewId) {
        Review review = reviewRepository.findReviewByReviewId(reviewId);
        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Review not found.");
        }
        return review;
    }
}
