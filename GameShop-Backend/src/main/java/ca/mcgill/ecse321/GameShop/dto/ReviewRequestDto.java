package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import java.sql.Date;

public class ReviewRequestDto {
    private GameReviewRating rating;
    private String comment;
    private Date reviewDate;
    private int orderGameId;  // to link the review to the specific OrderGame

    // Default constructor
    public ReviewRequestDto() {
    }

    // Constructor with all fields
    public ReviewRequestDto(GameReviewRating rating, String comment, Date reviewDate, int orderGameId) {
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.orderGameId = orderGameId;
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

    public int getOrderGameId() {
        return orderGameId;
    }

    public void setOrderGameId(int orderGameId) {
        this.orderGameId = orderGameId;
    }
}