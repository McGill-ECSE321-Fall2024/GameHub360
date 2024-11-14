package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.StoreInformation;

public class StoreInformationResponseDto {
    // Attributes
    private int storeInfoId;
    private String storePolicy;

    // Constructors
    public StoreInformationResponseDto() {
    }

    public StoreInformationResponseDto(int storeInfoId, String storePolicy) {
        this.storeInfoId = storeInfoId;
        this.storePolicy = storePolicy;
    }

    public StoreInformationResponseDto(StoreInformation storeInformation) {
        this.storeInfoId = storeInformation.getStoreInfoId();
        this.storePolicy = storeInformation.getStorePolicy();
    }

    // Getters and Setters
    public int getStoreInfoId() {
        return storeInfoId;
    }

    public void setStoreInfoId(int storeInfoId) {
        this.storeInfoId = storeInfoId;
    }

    public String getStorePolicy() {
        return storePolicy;
    }

    public void setStorePolicy(String storePolicy) {
        this.storePolicy = storePolicy;
    }
}
