package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;

import ca.mcgill.ecse321.GameShop.model.Reply;

public class ReplyResponseDto {
    // Attributes
    private int replyId;
    private String content;
    private Date replyDate;
    private int reviewId;
    private int staffId;

    // Constructors
    public ReplyResponseDto() {
    }

    public ReplyResponseDto(int replyId, String content, Date replyDate, int reviewId, int staffId) {
        this.replyId = replyId;
        this.content = content;
        this.replyDate = replyDate;
        this.reviewId = reviewId;
        this.staffId = staffId;
    }

    public ReplyResponseDto(Reply reply) {
        this(reply.getReplyId(), reply.getContent(), reply.getReplyDate(), reply.getReviewRecord().getReviewId(),
                reply.getReviewer().getStaffId());
    }

    // Getters and Setters
    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
