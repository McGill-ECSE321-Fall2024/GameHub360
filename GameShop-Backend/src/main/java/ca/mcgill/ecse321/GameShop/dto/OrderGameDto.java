package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.OrderGame;

public class OrderGameDto {

    private int orderGameId;
    private int gameId;
    private int customerId;
    private int orderId;

    public OrderGameDto() {
    }

    public OrderGameDto(OrderGame orderGame) {
        this.orderGameId = orderGame.getOrderGameId();
        this.gameId = orderGame.getGame().getGameEntityId();
        this.customerId = orderGame.getCustomerOrder().getOrderedBy().getCustomerId();
        this.orderId = orderGame.getCustomerOrder().getOrderId();
    }

    public int getOrderGameId() {
        return orderGameId;
    }

    public void setOrderGameId(int orderGameId) {
        this.orderGameId = orderGameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


}
