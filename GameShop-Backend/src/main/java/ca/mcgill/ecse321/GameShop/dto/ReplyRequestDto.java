package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotBlank;

public class ReplyRequestDto {
    // Attributes
    @NotBlank(message = "Content cannot be empty.")
    private String content;

    @NotBlank(message = "Manager ID cannot be empty.")
    private int managerId;

    @NotBlank(message = "Review ID cannot be empty.")
    private int reviewId;

    // Constructors
    public ReplyRequestDto() {
    }

    public ReplyRequestDto(String content, int managerId, int reviewId) {
        this.content = content;
        this.managerId = managerId;
        this.reviewId = reviewId;
    }

    // Getters and setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
}