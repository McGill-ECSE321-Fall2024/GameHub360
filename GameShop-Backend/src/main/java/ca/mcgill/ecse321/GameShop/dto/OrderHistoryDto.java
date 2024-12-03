package ca.mcgill.ecse321.GameShop.dto;

import java.util.List;

public class OrderHistoryDto {
    // Attributes
    private List<CustomerOrderResponseDto> orders;
    private int totalOrders;

    // Constructors
    public OrderHistoryDto() {}

    public OrderHistoryDto(List<CustomerOrderResponseDto> orders) {
        this.orders = orders;
        this.totalOrders = orders.size();
    }

    // Getters and Setters
    public List<CustomerOrderResponseDto> getOrders() {
        return orders;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setOrders(List<CustomerOrderResponseDto> orders) {
        this.orders = orders;
    }
}
