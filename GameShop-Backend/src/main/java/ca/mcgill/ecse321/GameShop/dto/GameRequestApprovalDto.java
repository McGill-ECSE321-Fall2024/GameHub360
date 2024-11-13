package ca.mcgill.ecse321.GameShop.dto;

// Attributes
public class GameRequestApprovalDto {

    private RequestNoteRequestDto note;
    private Double price;
    private Integer quantityInStock;
    private String rejectionReason;

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
