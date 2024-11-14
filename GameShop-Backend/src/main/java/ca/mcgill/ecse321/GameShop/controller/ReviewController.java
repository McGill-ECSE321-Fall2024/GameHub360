package ca.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.GameShop.dto.ReplyRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReplyResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewListDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewResponseDto;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.service.ReviewService;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/games")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Submit a review for a game
     * 
     * @param orderGameId
     * @param reviewRequestDto
     * @return ResponseEntity<ReviewResponseDto>
     */
    @PostMapping("/{orderGameId}/reviews")
    public ReviewResponseDto submitReview(@PathVariable("orderGameId") int orderGameId,
            @Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        Review review = reviewService.submitReview(orderGameId, reviewRequestDto);
        return new ReviewResponseDto(review);
    }

    /**
     * View reviews for a game
     * 
     * @param gameId
     * @return ResponseEntity<List<ReviewResponseDto>>
     */
    @GetMapping("/{gameId}/reviews")
    public ReviewListDto viewReviews(@PathVariable("gameId") int gameId) {
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<ReviewResponseDto>();
        List<Review> reviews = reviewService.getGameReviews(gameId);
        for (Review review : reviews) {
            reviewResponseDtos.add(new ReviewResponseDto(review));
        }

        return new ReviewListDto(reviewResponseDtos);
    }

    /**
     * Reply to a review
     * 
     * @param reviewId
     * @param reply
     * @return ResponseEntity<ReplyResponseDto>
     */
    @PostMapping("/reviews/{reviewId}/reply") //! HEREEEEEE NOT TESTED
    public ReplyResponseDto replyToReview(@PathVariable("reviewId") int reviewId, @Valid @RequestBody ReplyRequestDto replyRequestDto) {
        Reply reply = reviewService.replyToReview(reviewId, replyRequestDto);
        return new ReplyResponseDto(reply);
    }

    /**
     * Delete a review
     * 
     * @param reviewId
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") int reviewId) {
        reviewService.deleteReview(reviewId);
    }

    /**
     * Delete a reply
     * 
     * @param replyId
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/reviews/reply/{replyId}")
    public void deleteReply(@PathVariable("replyId") int replyId) {
        reviewService.deleteReply(replyId);
    }
}