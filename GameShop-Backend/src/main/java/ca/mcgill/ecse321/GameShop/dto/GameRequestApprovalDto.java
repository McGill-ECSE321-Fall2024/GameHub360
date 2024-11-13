package ca.mcgill.ecse321.GameShop.dto;

public class GameRequestApprovalDto {
    // Attributes
    private RequestNoteRequestDto note;
    private Double price;
    private Integer quantityInStock;
    private String rejectionReason;

    // Constructors
    public GameRequestApprovalDto() {
    }

    public GameRequestApprovalDto(RequestNoteRequestDto note, Double price, Integer quantityInStock, String rejectionReason) {
        this.note = note;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.rejectionReason = rejectionReason;
    }

    // Setters and getters
    public RequestNoteRequestDto getNote() {
        return note;
    }

    public void setNote(RequestNoteRequestDto note) {
        this.note = note;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
