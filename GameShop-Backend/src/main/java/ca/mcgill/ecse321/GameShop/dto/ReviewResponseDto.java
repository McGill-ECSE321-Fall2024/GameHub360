package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewResponseDto {
    // Attributes
    private int reviewId;
    private GameReviewRating rating;
    private String comment;
    private Date reviewDate;
    private List<Integer> reviewReplies;
    private int reviewedOrderGameId;

    // Constructors
    public ReviewResponseDto() {
    }

    public ReviewResponseDto(int reviewId, GameReviewRating rating, String comment, Date reviewDate,
            List<Integer> reviewReplies, int reviewedOrderGameId) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.reviewReplies = reviewReplies;
        this.reviewedOrderGameId = reviewedOrderGameId;
    }

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.reviewDate = review.getReviewDate();
        this.reviewReplies = review.getReviewReplies().stream().map(Reply::getReplyId).collect(Collectors.toList());
        this.reviewedOrderGameId = review.getReviewedGame().getOrderGameId();
    }

    // Getters and Setters
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

    public List<Integer> getReviewReplies() {
        return reviewReplies;
    }

    public void setReviewReplies(List<Integer> reviewReplies) {
        this.reviewReplies = reviewReplies;
    }

    public int getReviewedOrderGameId() {
        return reviewedOrderGameId;
    }

    public void setReviewedOrderGameId(int reviewedOrderGameId) {
        this.reviewedOrderGameId = reviewedOrderGameId;
    }
}