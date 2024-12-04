package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class PaymentDetailsRequestDto {
    // Attributes
    @NotBlank(message = "Card name cannot be empty.")
    private String cardName;

    @NotBlank(message = "Postal code cannot be empty.")
    private String postalCode;

    @NotBlank(message = "Card number cannot be empty.")
    private String cardNumber;

    @Positive(message = "Expiry month must be a positive integer.")
    private int expMonth;

    @Positive(message = "Expiry year must be a positive integer.")
    private int expYear;

    // Constructor
    public PaymentDetailsRequestDto(String cardName, String postalCode, String cardNumber, int expMonth, int expYear) {
        this.cardName = cardName;
        this.postalCode = postalCode;
        this.cardNumber = cardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
    }

    // Getters and Setters

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(int expMonth) {
        this.expMonth = expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }
}
