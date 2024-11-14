package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;

public class CustomerResponseDto {
    // Attributes
    private int staffId;
    private String email;
    private String name;
    private String phoneNumber;

    // Constructors
    public CustomerResponseDto() {}

    public CustomerResponseDto(CustomerAccount customer) {
        this.staffId = customer.getCustomerId();
        this.email = customer.getEmail();
        this.name = customer.getName();
        this.phoneNumber = customer.getPhoneNumber();
    }

    // Getters and Setters
    public int getCustomerId() {
        return staffId;
    }

    public void setCustomerId(int staffId) {
        this.staffId = staffId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
