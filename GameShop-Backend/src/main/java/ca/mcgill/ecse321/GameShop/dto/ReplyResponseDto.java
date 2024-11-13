package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Reply;

public class ReplyResponseDto {

    private int replyId;
    private String reply;
    private int reviewId;

    public ReplyResponseDto() {
    }

    public ReplyResponseDto(int replyId, String reply, int reviewId) {
        this.replyId = replyId;
        this.reply = reply;
        this.reviewId = reviewId;
    }

    public ReplyResponseDto(Reply reply2) {
        //TODO Auto-generated constructor stub
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

}
