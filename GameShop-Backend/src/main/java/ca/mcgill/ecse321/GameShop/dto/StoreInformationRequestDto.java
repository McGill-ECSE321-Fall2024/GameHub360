package ca.mcgill.ecse321.GameShop.dto;

public class StoreInformationRequestDto {
    // Attributes
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
