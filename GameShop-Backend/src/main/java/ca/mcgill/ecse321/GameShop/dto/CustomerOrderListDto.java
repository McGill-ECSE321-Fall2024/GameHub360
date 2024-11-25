package ca.mcgill.ecse321.GameShop.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderListDto {
    // Attributes
    private List<CustomerOrderResponseDto> orders;

    // Constructors
    public CustomerOrderListDto() {
        this.orders = new ArrayList<>();
    }

    public CustomerOrderListDto(List<CustomerOrderResponseDto> orders) {
        this.orders = orders;
    }

    // Getters and Setters
    public List<CustomerOrderResponseDto> getOrders() {
        return orders;
    }

    public void setOrders(List<CustomerOrderResponseDto> orders) {
        this.orders = orders;
    }
}
