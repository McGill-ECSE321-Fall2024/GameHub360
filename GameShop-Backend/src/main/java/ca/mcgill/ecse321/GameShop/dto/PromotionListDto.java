package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class PromotionListDto {
    private List<PromotionResponseDto> promotions;

    public PromotionListDto(List<PromotionResponseDto> promotions) {
        this.promotions = promotions;
    }

    public List<PromotionResponseDto> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionResponseDto> promotions) {
        this.promotions = promotions;
    }
}
