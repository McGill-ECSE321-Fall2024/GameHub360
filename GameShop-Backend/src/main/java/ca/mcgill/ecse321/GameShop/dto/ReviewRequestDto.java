package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import jakarta.validation.constraints.NotNull;

public class ReviewRequestDto {
    // Attributes
    @NotNull(message = "Rating cannot be null.")
    private GameReviewRating rating;

    @NotNull(message = "Customer Id cannot be null.")
    private int customerId;

    @NotNull(message = "Comment cannot be null.")
    private String comment;

    // Constructors
    public ReviewRequestDto() {
    }

    public ReviewRequestDto(GameReviewRating rating, int customerId, String comment) {
        this.rating = rating;
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
