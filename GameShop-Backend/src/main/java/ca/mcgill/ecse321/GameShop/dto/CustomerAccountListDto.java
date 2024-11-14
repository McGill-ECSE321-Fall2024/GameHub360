package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class CustomerAccountListDto {
    // Attributes
    private List<CustomerResponseDto> customers;
    private int totalCustomers;

    // Constructors
    public CustomerAccountListDto() {}

    public CustomerAccountListDto(List<CustomerResponseDto> customers) {
        this.customers = customers;
        this.totalCustomers = customers.size();
    }

    // Getters and Setters
    public List<CustomerResponseDto> getCustomers() {
        return customers;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setCustomers(List<CustomerResponseDto> customers) {
        this.customers = customers;
    }
}
