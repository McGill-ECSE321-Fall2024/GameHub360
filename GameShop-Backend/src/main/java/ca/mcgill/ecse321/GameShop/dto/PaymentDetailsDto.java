package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.PaymentDetails;

public class PaymentDetailsDto {
    private int paymentDetailsId;
    private String cardHolderName;
    private String postalCode;
    private long cardNumber;
    private int expirationMonth;
    private int expirationYear;

    public PaymentDetailsDto() {
    }

    public PaymentDetailsDto(int paymentDetailsId, String cardHolderName, String postalCode, 
                             long cardNumber, int expirationMonth, int expirationYear) {
        this.paymentDetailsId = paymentDetailsId;
        this.cardHolderName = cardHolderName;
        this.postalCode = postalCode;
        this.cardNumber = cardNumber;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public PaymentDetailsDto(PaymentDetails paymentDetails) {
        this(paymentDetails.getPaymentDetailsId(), paymentDetails.getCardName(),
             paymentDetails.getPostalCode(), paymentDetails.getCardNumber(),
             paymentDetails.getExpMonth(), paymentDetails.getExpYear());
    }

    // Getters and Setters
    public int getPaymentDetailsId() { return paymentDetailsId; }
    public void setPaymentDetailsId(int paymentDetailsId) { this.paymentDetailsId = paymentDetailsId; }

    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public long getCardNumber() { return cardNumber; }
    public void setCardNumber(long cardNumber) { this.cardNumber = cardNumber; }

    public int getExpirationMonth() { return expirationMonth; }
    public void setExpirationMonth(int expirationMonth) { this.expirationMonth = expirationMonth; }

    public int getExpirationYear() { return expirationYear; }
    public void setExpirationYear(int expirationYear) { this.expirationYear = expirationYear; }
}
