package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import jakarta.validation.constraints.NotBlank;

public class ReviewRequestDto {
    // Attributes
    @NotBlank(message = "Rating cannot be empty.")
    private GameReviewRating rating;

    @NotBlank(message = "Game ID cannot be empty.")
    private int reviewedGameId;

    @NotBlank(message = "Comment cannot be empty.")
    private int customerId;

    private String comment;

    // Constructors
    public ReviewRequestDto() {
    }

    public ReviewRequestDto(GameReviewRating rating, int reviewedGameId, int customerId, String comment) {
        this.rating = rating;
        this.reviewedGameId = reviewedGameId;
        this.customerId = customerId;
        this.comment = comment;
    }

    // Getters and setters
    public GameReviewRating getRating() {
        return rating;
    }

    public void setRating(GameReviewRating rating) {
        this.rating = rating;
    }

    public int getReviewedGameId() {
        return reviewedGameId;
    }

    public void setReviewedGameId(int reviewedGameId) {
        this.reviewedGameId = reviewedGameId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
