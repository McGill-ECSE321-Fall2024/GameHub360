package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import java.sql.Date;

public class ReviewRequestDto {
    private GameReviewRating rating;
    private String comment;
    private Date reviewDate;
    private int orderedGameId;
    private int reviewedById;


    // Default constructor
    public ReviewRequestDto() {
    }

    // Constructor with all fields
    public ReviewRequestDto(GameReviewRating rating, String comment, Date reviewDate, int orderedGameId, int reviewedById) {
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.orderedGameId = orderedGameId;
        this.reviewedById = reviewedById;
    }

    public ReviewRequestDto(Review review) { 
        this(review.getRating(), review.getComment(), review.getReviewDate(), review.getReviewedGame().getOrderGameId(), review.getReviewId());
    }

    // Getters and setters
    public GameReviewRating getRating() {
        return rating;
    }

    public void setRating(GameReviewRating rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getOrderedGameId() {
        return orderedGameId;
    }

    public void setOrderedGameId(int orderedGameId) {
        this.orderedGameId = orderedGameId;
    }

    public int getReviewedById() {
        return reviewedById;
    }

    public void setReviewedById(int reviewedById) {
        this.reviewedById = reviewedById;
    }

}