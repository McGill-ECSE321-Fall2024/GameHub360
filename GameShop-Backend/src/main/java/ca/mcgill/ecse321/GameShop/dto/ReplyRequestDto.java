package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;

import ca.mcgill.ecse321.GameShop.model.Reply;

public class ReplyRequestDto {

    private String content;
    private Date replyDate;
    private int managerId;//remove this


    public ReplyRequestDto() {
    }

    public ReplyRequestDto(String content, Date replyDate, int managerId) {
        this.content = content;
        this.replyDate = replyDate;
        this.managerId = managerId;
    }

    public ReplyRequestDto(Reply reply) {
        this(reply.getContent(), reply.getReplyDate(), reply.getReviewer().getStaffId());
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

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }









}
