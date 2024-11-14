package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class ReviewListDto {
    // Attributes
    private List<ReviewResponseDto> reviews;

    // Constructors
    public ReviewListDto() {
    }

    public ReviewListDto(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
    }

    // Getters and Setters
    public List<ReviewResponseDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
    }
}