package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;

public class EmployeeResponseDto {
    // Attributes
    private int staffId;
    private String email;
    private String name;
    private String phoneNumber;
    private Boolean isActive;

    // Constructors
    public EmployeeResponseDto() {
    }

    public EmployeeResponseDto(EmployeeAccount employee) {
        this.staffId = employee.getStaffId();
        this.email = employee.getEmail();
        this.name = employee.getName();
        this.phoneNumber = employee.getPhoneNumber();
        this.isActive = employee.getIsActive();
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
