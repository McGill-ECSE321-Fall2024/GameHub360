package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import java.sql.Date;

public class ReviewResponseDto {
    private int reviewId;
    private GameReviewRating rating;
    private String comment;
    private Date reviewDate;

    // Constructor
    public ReviewResponseDto(int reviewId, GameReviewRating rating, String comment, Date reviewDate) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    // Getters
    public int getReviewId() {
        return reviewId;
    }

    public GameReviewRating getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }
}