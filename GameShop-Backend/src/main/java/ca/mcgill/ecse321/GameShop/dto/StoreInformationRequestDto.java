package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.NotBlank;

public class StoreInformationRequestDto {
    // Attributes
    @NotBlank(message = "Store policy cannot be empty.")
    private String storePolicy;

    // Constructors
    public StoreInformationRequestDto() {
    }

    public StoreInformationRequestDto(String storePolicy) {
        this.storePolicy = storePolicy;
    }

    // Getters and Setters
    public String getStorePolicy() {
        return storePolicy;
    }

    public void setStorePolicy(String storePolicy) {
        this.storePolicy = storePolicy;
    }
}
