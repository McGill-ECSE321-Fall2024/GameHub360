package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentDetailsResponseDto {
    // Attributes
    private int paymentDetailsId;
    private String cardName;
    private String cardNumber;
    private String postalCode;
    private int expMonth;
    private int expYear;
    private Integer customerId; // ID of the associated CustomerAccount
    private List<Integer> paidOrdersIds; // IDs of associated CustomerOrders

    // Constructor that initializes fields from the PaymentDetails model
    public PaymentDetailsResponseDto(PaymentDetails paymentDetails) {
        this.paymentDetailsId = paymentDetails.getPaymentDetailsId();
        this.cardName = paymentDetails.getCardName();
        this.cardNumber = paymentDetails.getCardNumber();
        this.postalCode = paymentDetails.getPostalCode();
        this.expMonth = paymentDetails.getExpMonth();
        this.expYear = paymentDetails.getExpYear();
        this.customerId = paymentDetails.getCardOwner().getCustomerId();
        this.paidOrdersIds = paymentDetails.getPaidOrders().stream()
                .map(CustomerOrder::getOrderId)
                .collect(Collectors.toList());
    }

    public PaymentDetailsResponseDto() {
    }

    // Getters only, as this is a response DTO
    public int getPaymentDetailsId() {
        return paymentDetailsId;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public int getExpMonth() {
        return expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public List<Integer> getPaidOrdersIds() {
        return paidOrdersIds;
    }
}
