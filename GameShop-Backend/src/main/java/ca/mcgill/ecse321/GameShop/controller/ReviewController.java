package ca.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.service.ReviewService;

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
     * @param gameId
     * @param reviewRequestDto
     * @return ResponseEntity<ReviewResponseDto>
     */
    @PostMapping("/{gameId}/reviews")
    public ReviewResponseDto submitReview(@PathVariable int gameId,
            @Validated({ ValidationGroups.Post.class }) @RequestBody ReviewRequestDto reviewRequestDto) {
        Review review = reviewService.submitReview(reviewRequestDto);
        return new ReviewResponseDto(review);
    }

    /**
     * View reviews for a game
     * 
     * @param gameId
     * @return ResponseEntity<List<ReviewResponseDto>>
     */
    @GetMapping("/{gameId}/reviews")
    public ReviewResponseDto viewReviews(@PathVariable int gameId) {
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<ReviewResponseDto>();
        List<Review> reviews = reviewService.viewReviews();
        for (Review review : reviews) {
            reviewResponseDtos.add(new ReviewResponseDto(review));
        }

        return new ReviewResponseDto(reviewResponseDtos);
    }

    /**
     * Reply to a review
     * 
     * @param reviewId
     * @param reply
     * @return ResponseEntity<ReviewResponseDto>
     */
    @PostMapping("/reviews/{reviewId}/reply")
    public ReviewResponseDto replyToReview(@PathVariable int reviewId, @RequestBody Reply reply) {
        Review review = reviewService.replyToReview(reviewId, reply);
        return new ReviewResponseDto(review);
    }

    /**
     * Delete a review
     * 
     * @param reviewId
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
    }
}