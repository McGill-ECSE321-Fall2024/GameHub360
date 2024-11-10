package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;
import java.util.List;

import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;

public class CustomerOrderResponseDto {
    private int orderId;
    private OrderStatus orderStatus;
    private Date orderDate;
    private List<OrderGame> orderedGames;
    private CustomerAccount orderedBy;
    private PaymentDetails paymentInformation;

    public CustomerOrderResponseDto() {
    }

    public CustomerOrderResponseDto(int orderId, OrderStatus orderStatus, Date orderDate, List<OrderGame> orderedGames, CustomerAccount orderedBy, PaymentDetails paymentInformation) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderedGames = orderedGames;
        this.orderedBy = orderedBy;
        this.paymentInformation = paymentInformation;
    }

    public CustomerOrderResponseDto(CustomerOrder customerOrder) {
        this(customerOrder.getOrderId(), customerOrder.getOrderStatus(), customerOrder.getOrderDate(), customerOrder.getOrderedGames(), customerOrder.getOrderedBy(), customerOrder.getPaymentInformation());
    }

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

    public List<OrderGame> getOrderedGames() {
        return orderedGames;
    }

    public void setOrderedGames(List<OrderGame> orderedGames) {
        this.orderedGames = orderedGames;
    }

    public CustomerAccount getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(CustomerAccount orderedBy) {
        this.orderedBy = orderedBy;
    }

    public PaymentDetails getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(PaymentDetails paymentInformation) {
        this.paymentInformation = paymentInformation;
    }
}