package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.repository.ReviewRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.ReplyRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    /**
     * submit a review
     * 
     * @param requestDto
     * @return Review
     * @throws GameShopException
     */
    @Transactional
    public Review submitReview(ReviewRequestDto requestDto) {
        OrderGame orderedGame = orderGameRepository.findOrderGameById(requestDto.getOrderedGameId());
        CustomerAccount reviewedBy = customerAccountRepository
                .findCustomerAccountByCustomerId(requestDto.getReviewedById());
        //Reply reply = replyRepository.findReplyByReplyId(requestDto.getReplyId());

        if (orderedGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Ordered game not found");
        }

        if (reviewedBy == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        //if (reply == null) {
          //  throw new GameShopException(HttpStatus.NOT_FOUND, "Reply not found");
        //}

        Review review = new Review(requestDto.getReviewDate(), orderedGame);
        review.setReviewedGame(orderedGame);
        review.setRating(requestDto.getRating());

        if (requestDto.getComment() != null) {
            review.setComment(requestDto.getComment());
        }

       // review.addReviewReply(reply);

        return reviewRepository.save(review);
    }

    /**
     * get all reviews
     * 
     * @return List<Review>
     */
    public List<Review> viewReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    /**
     * reply to review
     * 
     * @param reviewId
     * @param reply
     * @return Review
     * @throws GameShopException
     */
    @Transactional
    public Review replyToReview(int reviewId, Reply reply) {
        Review review = reviewRepository.findReviewByReviewId(reviewId);

        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Review not found");
        }

        review.addReviewReply(reply);

        return reviewRepository.save(review);
    }

    /**
     * delete review
     * 
     * @param reviewId
     * @throws GameShopException
     */
    @Transactional
    public void deleteReview(int reviewId) {
        Review review = reviewRepository.findReviewByReviewId(reviewId);

        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Review not found");
        }

        reviewRepository.delete(review);
    }

}
