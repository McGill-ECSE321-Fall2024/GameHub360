package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.Promotion.PromotionType;
import java.util.List;
import java.util.stream.Collectors;

public class PromotionResponseDto {
    // Attributes
    private int promotionId;
    private PromotionType promotionType;
    private double discountPercentageValue;
    private List<Integer> promotedGameIds;
    private List<Integer> promotedCategoryIds;

    // Constructors
    public PromotionResponseDto() {
    }

    public PromotionResponseDto(int promotionId, PromotionType promotionType, double discountPercentageValue,
            List<Integer> promotedGameIds, List<Integer> promotedCategoryIds) {
        this.promotionId = promotionId;
        this.promotionType = promotionType;
        this.discountPercentageValue = discountPercentageValue;
        this.promotedGameIds = promotedGameIds;
        this.promotedCategoryIds = promotedCategoryIds;
    }

    public PromotionResponseDto(Promotion promotion) {
        this.promotionId = promotion.getPromotionId();
        this.promotionType = promotion.getPromotionType();
        this.discountPercentageValue = promotion.getDiscountPercentageValue();
        this.promotedGameIds = promotion.getPromotedGames().stream().map(game -> game.getGameEntityId())
                .collect(Collectors.toList());
        this.promotedCategoryIds = promotion.getPromotedCategories().stream().map(category -> category.getCategoryId())
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

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
