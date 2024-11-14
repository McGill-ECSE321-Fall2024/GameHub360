package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import ca.mcgill.ecse321.GameShop.model.OrderGame;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponseDto {
    // Attributes
    private int orderId;
    private OrderStatus orderStatus;
    private Date orderDate;
    private int customerId;
    private String customerEmail;
    private int paymentId;
    private List<Integer> orderGameIds;

    // Constructor
    public OrderResponseDto(CustomerOrder customerOrder) {
        this.orderId = customerOrder.getOrderId();
        this.orderStatus = customerOrder.getOrderStatus();
        this.orderDate = customerOrder.getOrderDate();
        this.customerId = customerOrder.getOrderedBy().getCustomerId();
        this.customerEmail = customerOrder.getOrderedBy().getEmail();
        this.paymentId = customerOrder.getPaymentInformation().getPaymentDetailsId();
        this.orderGameIds = customerOrder.getOrderedGames().stream()
                .map(OrderGame::getOrderGameId)
                .collect(Collectors.toList());
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public List<Integer> getOrderGameIds() {
        return orderGameIds;
    }
}