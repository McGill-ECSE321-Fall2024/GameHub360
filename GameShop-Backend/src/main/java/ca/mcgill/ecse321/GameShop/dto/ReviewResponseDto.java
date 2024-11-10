package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import java.sql.Date;

public class ReviewResponseDto {
    private int reviewId;
    private GameReviewRating rating;
    private String comment;
    private Date reviewDate;
    private Reply reply;
    private CustomerAccount reviewedBy;
    private OrderGame orderedGame;

    // Constructor
    public ReviewResponseDto(int reviewId, GameReviewRating rating, String comment, Date reviewDate, Reply reply,
            CustomerAccount reviewedBy, OrderGame orderedGame) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.reply = reply;
        this.reviewedBy = reviewedBy;
        this.orderedGame = orderedGame;
    }

    // Getters and setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

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

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public CustomerAccount getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(CustomerAccount reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public OrderGame getOrderedGame() {
        return orderedGame;
    }

    public void setOrderedGame(OrderGame orderedGame) {
        this.orderedGame = orderedGame;
    }
}