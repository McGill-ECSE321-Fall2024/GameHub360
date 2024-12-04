package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;

public class CustomerOrderResponseDto {
    // Attributes
    private int orderId;
    private OrderStatus orderStatus;
    private Date orderDate;
    private List<Integer> orderedGamesIds;
    private int orderedById;
    private int paymentInformationId;

    // Constructors
    public CustomerOrderResponseDto() {
    }

    public CustomerOrderResponseDto(int orderId, OrderStatus orderStatus, Date orderDate, List<Integer> orderedGamesIds,
            int orderedById, int paymentInformationId) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderedGamesIds = orderedGamesIds;
        this.orderedById = orderedById;
        this.paymentInformationId = paymentInformationId;
    }

    public CustomerOrderResponseDto(CustomerOrder customerOrder) {
        this.orderId = customerOrder.getOrderId();
        this.orderStatus = customerOrder.getOrderStatus();
        this.orderDate = customerOrder.getOrderDate();
        this.orderedGamesIds = customerOrder.getOrderedGames().stream()
                .map(orderGame -> orderGame.getOrderGameId())
                .collect(Collectors.toList());
        this.orderedById = customerOrder.getOrderedBy().getCustomerId();
        this.paymentInformationId = customerOrder.getPaymentInformation().getPaymentDetailsId();
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public List<Integer> getOrderedGamesIds() {
        return orderedGamesIds;
    }

    public void setOrderedGamesIds(List<Integer> orderedGamesIds) {
        this.orderedGamesIds = orderedGamesIds;
    }

    public int getOrderedById() {
        return orderedById;
    }

    public void setOrderedById(int orderedById) {
        this.orderedById = orderedById;
    }

    public int getPaymentInformationId() {
        return paymentInformationId;
    }

    public void setPaymentInformationId(int paymentInformationId) {
        this.paymentInformationId = paymentInformationId;
    }
}
