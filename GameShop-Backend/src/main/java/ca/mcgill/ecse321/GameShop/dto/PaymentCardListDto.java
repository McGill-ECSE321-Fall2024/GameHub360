package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class PaymentCardListDto {
    // Attributes
    private List<PaymentDetailsResponseDto> paymentCards;
    private int totalCards;

    // Constructors
    public PaymentCardListDto() {}

    public PaymentCardListDto(List<PaymentDetailsResponseDto> paymentCards) {
        this.paymentCards = paymentCards;
        this.totalCards = paymentCards.size();
    }

    // Getters and Setters
    public List<PaymentDetailsResponseDto> getPaymentCards() {
        return paymentCards;
    }

    public int getTotalCards() {
        return totalCards;
    }

    public void setPaymentCards(List<PaymentDetailsResponseDto> paymentCards) {
        this.paymentCards = paymentCards;
    }
}
