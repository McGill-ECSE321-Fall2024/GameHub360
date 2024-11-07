package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Promotion.PromotionType;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class PromotionRequestDto {
    // Attributes
    @NotNull(message = "Promotion type cannot be null.")
    private PromotionType promotionType;

    @PositiveOrZero(message = "Discount percentage value must be zero or positive.")
    private double discountPercentageValue;

    private List<Integer> promotedGameIds;
    private List<Integer> promotedCategoryIds;

    // Constructors
    public PromotionRequestDto() {
    }

    public PromotionRequestDto(PromotionType promotionType, double discountPercentageValue,
            List<Integer> promotedGameIds, List<Integer> promotedCategoryIds) {
        this.promotionType = promotionType;
        this.discountPercentageValue = discountPercentageValue;
        this.promotedGameIds = promotedGameIds;
        this.promotedCategoryIds = promotedCategoryIds;
    }

    // Getters and Setters
    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public double getDiscountPercentageValue() {
        return discountPercentageValue;
    }

    public void setDiscountPercentageValue(double discountPercentageValue) {
        this.discountPercentageValue = discountPercentageValue;
    }

    public List<Integer> getPromotedGameIds() {
        return promotedGameIds;
    }

    public void setPromotedGameIds(List<Integer> promotedGameIds) {
        this.promotedGameIds = promotedGameIds;
    }

    public List<Integer> getPromotedCategoryIds() {
        return promotedCategoryIds;
    }

    public void setPromotedCategoryIds(List<Integer> promotedCategoryIds) {
        this.promotedCategoryIds = promotedCategoryIds;
    }
}
