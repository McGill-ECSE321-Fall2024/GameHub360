package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;
import java.util.List;

public class OrderRequestDto {
    private int customerId;
    private int paymentId;
    private List<Integer> gameIds;  // List of game IDs to be ordered
    private Date orderDate;

    // Default constructor
    public OrderRequestDto() {
    }

    // Constructor with all fields
    public OrderRequestDto(int customerId, int paymentId, List<Integer> gameIds, Date orderDate) {
        this.customerId = customerId;
        this.paymentId = paymentId;
        this.gameIds = gameIds;
        this.orderDate = orderDate;
    }

    // Getters and setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public List<Integer> getGameIds() {
        return gameIds;
    }

    public void setGameIds(List<Integer> gameIds) {
        this.gameIds = gameIds;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}