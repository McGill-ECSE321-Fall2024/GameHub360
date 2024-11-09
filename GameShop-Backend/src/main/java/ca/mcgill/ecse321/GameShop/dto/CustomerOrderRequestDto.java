package ca.mcgill.ecse321.GameShop.dto;

import java.sql.Date;
import java.util.List;

public class CustomerOrderRequestDto {
    private Date orderDate;
    private List<Integer> orderedGameIds;
    private int orderedById;
    private int paymentInformationId;

    public CustomerOrderRequestDto() {
    }

    public CustomerOrderRequestDto(Date orderDate, List<Integer> orderedGameIds, int orderedById, int paymentInformationId) {
        this.orderDate = orderDate;
        this.orderedGameIds = orderedGameIds;
        this.orderedById = orderedById;
        this.paymentInformationId = paymentInformationId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public List<Integer> getOrderedGameIds() {
        return orderedGameIds;
    }

    public void setOrderedGameIds(List<Integer> orderedGameIds) {
        this.orderedGameIds = orderedGameIds;
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
