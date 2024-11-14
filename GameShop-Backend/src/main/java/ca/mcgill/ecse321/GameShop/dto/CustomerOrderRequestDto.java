package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public class CustomerOrderRequestDto {
    // Attributes
    @NotEmpty(message = "Ordered game IDs cannot be empty.")
    private List<Integer> orderedGameIds;

    @Positive(message = "Payment information ID cannot be empty.")
    private Integer paymentInformationId;

    @Positive(message = "Customer ID cannot be empty.")
    private Integer customerId;

    // Constructors
    public CustomerOrderRequestDto() {
    }

    public CustomerOrderRequestDto(List<Integer> orderedGameIds, Integer paymentInformationId, Integer customerId) {
        this.orderedGameIds = orderedGameIds;
        this.paymentInformationId = paymentInformationId;
        this.customerId = customerId;
    }

    // Getters and setters
    public List<Integer> getOrderedGameIds() {
        return orderedGameIds;
    }

    public void setOrderedGameIds(List<Integer> orderedGameIds) {
        this.orderedGameIds = orderedGameIds;
    }

    public Integer getPaymentInformationId() {
        return paymentInformationId;
    }

    public void setPaymentInformationId(Integer paymentInformationId) {
        this.paymentInformationId = paymentInformationId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
