package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;

public class CustomerAccountDto {
    private int customerId;
    private String email;
    private String name;
    private String phone;

    public CustomerAccountDto() {
    }

    public CustomerAccountDto(int customerId, String email, String name, String phone) {
        this.customerId = customerId;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public CustomerAccountDto(CustomerAccount customerAccount) {
        this(customerAccount.getCustomerId(), customerAccount.getEmail(), 
             customerAccount.getName(), customerAccount.getPhoneNumber());
    }

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

