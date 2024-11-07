package ca.mcgill.ecse321.GameShop.dto;

import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import java.sql.Date;
import java.util.List;

public class OrderResponseDto {
    private int orderId;
    private OrderStatus orderStatus;
    private Date orderDate;
    private int customerId;
    private String customerEmail;  // Additional customer info for convenience
    private int paymentId;
    private List<OrderGameDto> orderedGames;  // Nested DTO for ordered games

    // Inner class to represent ordered games
    public static class OrderGameDto {
        private int orderGameId;
        private int gameId;
        private String gameName;
        private double price;

        // Constructor
        public OrderGameDto(int orderGameId, int gameId, String gameName, double price) {
            this.orderGameId = orderGameId;
            this.gameId = gameId;
            this.gameName = gameName;
            this.price = price;
        }

        // Getters
        public int getOrderGameId() {
            return orderGameId;
        }

        public int getGameId() {
            return gameId;
        }

        public String getGameName() {
            return gameName;
        }

        public double getPrice() {
            return price;
        }
    }

    // Constructor
    public OrderResponseDto(int orderId, OrderStatus orderStatus, Date orderDate, 
                          int customerId, String customerEmail, int paymentId, 
                          List<OrderGameDto> orderedGames) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.customerEmail = customerEmail;
        this.paymentId = paymentId;
        this.orderedGames = orderedGames;
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

    public List<OrderGameDto> getOrderedGames() {
        return orderedGames;
    }

    // Calculate total order price
    public double getTotalPrice() {
        return orderedGames.stream()
                .mapToDouble(OrderGameDto::getPrice)
                .sum();
    }
}
