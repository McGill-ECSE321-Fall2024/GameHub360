package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.ReplyRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ReviewRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.ReplyRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
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

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

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
    public Reply replyToReview(int reviewId, ReplyRequestDto replyRequestDto) {
        Review review = reviewRepository.findReviewByReviewId(reviewId);

        
        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Review not found");
        }

        ManagerAccount manager = managerAccountRepository.findManagerAccountByStaffId(replyRequestDto.getManagerId());

        if (manager == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Manager not found");
        }
        Reply reply = new Reply();
        reply.setContent(replyRequestDto.getContent());
        reply.setReplyDate(replyRequestDto.getReplyDate());
        reply.setReviewer(manager);
    
        review.addReviewReply(reply);

        
        Reply savedReply = replyRepository.save(reply);

        return savedReply;
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

    /**
     * delete Reply
     *  
     * 
     * @param replyId
     * @throws GameShopException
     */
    @Transactional
    public void deleteReply(int replyId) {
        Reply reply = replyRepository.findReplyByReplyId(replyId);

        if (reply == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Reply not found");
        }

        replyRepository.delete(reply);
    }

}
