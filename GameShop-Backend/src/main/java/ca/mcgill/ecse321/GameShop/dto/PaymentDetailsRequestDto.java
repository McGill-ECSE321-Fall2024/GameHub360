package ca.mcgill.ecse321.GameShop.dto;

public class PaymentDetailsRequestDto {
    // Attributes
    private String cardName;
    private String postalCode;
    private int cardNumber;
    private int expMonth;
    private int expYear;
    private Integer customerId; // ID of the associated CustomerAccount

    // Constructor
    public PaymentDetailsRequestDto(String cardName, String postalCode, int cardNumber, int expMonth, int expYear, Integer customerId) {
        this.cardName = cardName;
        this.postalCode = postalCode;
        this.cardNumber = cardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.customerId = customerId;
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

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
