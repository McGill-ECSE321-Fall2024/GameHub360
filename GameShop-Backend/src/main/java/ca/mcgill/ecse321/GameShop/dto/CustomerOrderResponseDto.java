package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;

public class CustomerOrderResponseDto {
    private int orderId;
    private OrderStatus orderStatus;
    private Date orderDate;
    private List<OrderGameDto> orderedGames;
    private CustomerAccountDto orderedBy;
    private PaymentDetailsDto paymentInformation;

    public CustomerOrderResponseDto() {
    }

    public CustomerOrderResponseDto(CustomerOrder customerOrder) {
        this.orderId = customerOrder.getOrderId();
        this.orderStatus = customerOrder.getOrderStatus();
        this.orderDate = customerOrder.getOrderDate();
        this.orderedGames = customerOrder.getOrderedGames().stream().map(OrderGameDto::new).collect(Collectors.toList());
        this.orderedBy = new CustomerAccountDto(customerOrder.getOrderedBy());
        this.paymentInformation = new PaymentDetailsDto(customerOrder.getPaymentInformation());
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

    public List<OrderGameDto> getOrderedGames() {
        return orderedGames;
    }

    public void setOrderedGames(List<OrderGameDto> orderedGames) {
        this.orderedGames = orderedGames;
    }

    public CustomerAccountDto getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(CustomerAccountDto orderedBy) {
        this.orderedBy = orderedBy;
    }

    public PaymentDetailsDto getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(PaymentDetailsDto paymentInformation) {
        this.paymentInformation = paymentInformation;
    }
}