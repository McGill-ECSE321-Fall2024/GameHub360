package ca.mcgill.ecse321.GameShop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ManagerRequestDto {
    // Attributes
    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email must be a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be empty.", groups = ValidationGroups.Post.class)
    private String password;

    private String name;
    private String phoneNumber;

    // Constructors
    public ManagerRequestDto() {}

    public ManagerRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public ManagerRequestDto(String email, String password, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
