package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.ReplyRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ReviewRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.ReplyRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderGameRepository orderGameRepository;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    @Autowired
    private GameRepository gameRepository;

    /**
     * Submit a review for an order.
     * 
     * @param orderGameId the id of the order game
     * @param requestDto  the review content
     * @return Review the review
     * @throws GameShopException if orderGame or customer not found
     */
    @Transactional
    public Review submitReview(int orderGameId, ReviewRequestDto requestDto) {
        // Check if orderGame exists
        OrderGame orderGame = orderGameRepository.findOrderGameById(orderGameId);
        if (orderGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "OrderGame not found");
        }

        // Check if customer exists
        CustomerAccount customer = customerAccountRepository
                .findCustomerAccountByCustomerId(requestDto.getCustomerId());
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        // Check if customer is the owner of the order
        if (orderGame.getCustomerOrder().getOrderedBy().getCustomerId() != requestDto.getCustomerId()) {
            throw new GameShopException(HttpStatus.FORBIDDEN, "Customer is not the owner of the order");
        }

        // Clear any existing review
        if (orderGame.getReview() != null) {
            orderGame.getReview().delete();
            orderGame.setReview(null);
        }

        // Create review
        Review review = new Review();
        review.setReviewDate(new Date(System.currentTimeMillis()));
        review.setRating(requestDto.getRating());
        if (requestDto.getComment() != null) {
            review.setComment(requestDto.getComment());
        }

        // Save review to get ID
        review = reviewRepository.save(review);
        
        // Set up bidirectional relationship
        review.setReviewedGame(orderGame);
        
        return reviewRepository.save(review);
    }

    /**
     * Get all reviews for a game.
     * 
     * @param gameId the id of the game
     * @return List<Review> the list of reviews
     * @throws GameShopException if game not found
     */
    public List<Review> getGameReviews(int gameId) {
        // Check if the game exists
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found");
        }

        // Initialize the list of reviews
        List<Review> reviews = new ArrayList<Review>();

        // Get the list of orders for the game
        List<OrderGame> orderGames = game.getOrders();

        // Get the review for each order and add it to the list
        for (OrderGame orderGame : orderGames) {
            Review review = orderGame.getReview();

            // Check if order has a review
            if (review != null) {
                reviews.add(review);
            }
        }

        return reviews;
    }

    /**
     * Reply to a review.
     * 
     * @param reviewId        the id of the review to reply to
     * @param replyRequestDto the reply content
     * @return Reply the reply
     * @throws GameShopException if review or manager not found
     */
    @Transactional
    public Reply replyToReview(int reviewId, ReplyRequestDto replyRequestDto) {
        // Check if review exists
        Review review = reviewRepository.findReviewByReviewId(reviewId);
        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Review not found");
        }

        // Check if manager exists
        ManagerAccount manager = managerAccountRepository.findManagerAccountByStaffId(replyRequestDto.getManagerId());
        if (manager == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Manager not found");
        }

        // Create and save reply
        Date replyDate = new Date(System.currentTimeMillis());
        Reply reply = new Reply(replyRequestDto.getContent(), replyDate, review, manager);

        return replyRepository.save(reply);
    }

    /**
     * Delete a review.
     * 
     * @param reviewId the id of the review to delete
     * @throws GameShopException if review not found
     */
    @Transactional
    public void deleteReview(int reviewId) {
        Review review = reviewRepository.findReviewByReviewId(reviewId);

        // Check if review exists
        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Review not found");
        }

        reviewRepository.delete(review);
    }

    /**
     * Delete a Reply.
     * 
     * 
     * @param replyId the id of the reply to delete
     * @throws GameShopException if reply not found
     */
    @Transactional
    public void deleteReply(int replyId) {
        Reply reply = replyRepository.findReplyByReplyId(replyId);

        // Check if reply exists
        if (reply == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Reply not found");
        }

        replyRepository.delete(reply);
    }

}
