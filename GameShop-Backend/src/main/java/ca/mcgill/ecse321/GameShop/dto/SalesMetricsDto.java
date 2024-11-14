package ca.mcgill.ecse321.GameShop.dto;

public class SalesMetricsDto {
    // Attributes
    private double totalSales;
    private int totalOrders;
    private int totalGamesSold;
    private int totalCustomers;

    // Constructors
    public SalesMetricsDto() {
    }

    public SalesMetricsDto(double totalSales, int totalOrders, int totalGamesSold, int totalCustomers) {
        this.totalSales = totalSales;
        this.totalOrders = totalOrders;
        this.totalGamesSold = totalGamesSold;
        this.totalCustomers = totalCustomers;
    }

    // Getters and Setters
    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getTotalGamesSold() {
        return totalGamesSold;
    }

    public void setTotalGamesSold(int totalGamesSold) {
        this.totalGamesSold = totalGamesSold;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}
