package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.ManagerAccount;

public class ManagerResponseDto {
    // Attributes
    private int staffId;
    private String email;
    private String name;
    private String phoneNumber;

    // Constructors
    public ManagerResponseDto() {}

    public ManagerResponseDto(ManagerAccount manager) {
        this.staffId = manager.getStaffId();
        this.email = manager.getEmail();
        this.name = manager.getName();
        this.phoneNumber = manager.getPhoneNumber();
    }

    // Getters and Setters
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
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
