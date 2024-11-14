package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotNull;

public class ReplyRequestDto {
    // Attributes
    @NotNull(message = "Content cannot be empty.")
    private String content;

    @NotNull(message = "Manager ID cannot be empty.")
    private int managerId;

    // Constructors
    public ReplyRequestDto() {
    }

    public ReplyRequestDto(String content, int managerId) {
        this.content = content;
        this.managerId = managerId;
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
}