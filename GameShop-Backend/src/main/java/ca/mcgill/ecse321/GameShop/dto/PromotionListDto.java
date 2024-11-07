package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class PromotionListDto {
    // Attributes
    private List<PromotionResponseDto> promotions;

    // Constructors
    public PromotionListDto() {
    }

    public PromotionListDto(List<PromotionResponseDto> promotions) {
        this.promotions = promotions;
    }

    // Getters and Setters
    public List<PromotionResponseDto> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionResponseDto> promotions) {
        this.promotions = promotions;
    }
}
