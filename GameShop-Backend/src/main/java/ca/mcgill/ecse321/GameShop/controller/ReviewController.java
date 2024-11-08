package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewResponseDto;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/game/{gameId}")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable int gameId,
            @RequestBody ReviewRequestDto reviewRequest) {
        Review review = reviewService.createReview(gameId, reviewRequest);
        return ResponseEntity.ok(convertToDto(review));
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByGame(@PathVariable int gameId) {
        List<Review> reviews = reviewService.getReviewsByGame(gameId);
        List<ReviewResponseDto> reviewDtos = reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewDtos);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable int reviewId) {
        Review review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(convertToDto(review));
    }

    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<Reply> addReplyToReview(
            @PathVariable int reviewId,
            @RequestParam String content,
            @RequestParam String managerEmail) {
        Reply reply = reviewService.addReplyToReview(reviewId, content, managerEmail);
        return ResponseEntity.ok(reply);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    // Helper method to convert Review to ReviewResponseDto
    private ReviewResponseDto convertToDto(Review review) {
        return new ReviewResponseDto(
                review.getReviewId(),
                review.getRating(),
                review.getComment(),
                review.getReviewDate()
        );
    }
}
