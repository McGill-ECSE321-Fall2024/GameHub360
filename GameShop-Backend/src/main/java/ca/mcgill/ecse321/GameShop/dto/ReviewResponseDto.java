package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.model.Review.GameReviewRating;
import java.sql.Date;
import java.util.List;

public class ReviewResponseDto {
    private int reviewId;
    private GameReviewRating rating;
    private String comment;
    private Date reviewDate;
    private List<ReviewResponseDto> reviews;
    private List<Reply> reply;
    private OrderGame orderedGame;

    // Constructor
    public ReviewResponseDto(int reviewId, GameReviewRating rating, String comment, Date reviewDate, List<Reply> reply,
            OrderGame orderedGame) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.reply = reply;
        this.orderedGame = orderedGame;
    }

    public ReviewResponseDto(Review review) {
        this(review.getReviewId(), review.getRating(), review.getComment(), review.getReviewDate(),
                review.getReviewReplies(), review.getReviewedGame());
    }

    public ReviewResponseDto(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
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

    public List<Reply> getReply() {
        return reply;
    }

    public void setReply(List<Reply> reply) {
        this.reply = reply;
    }

    public OrderGame getOrderedGame() {
        return orderedGame;
    }

    public void setOrderedGame(OrderGame orderedGame) {
        this.orderedGame = orderedGame;
    }
}