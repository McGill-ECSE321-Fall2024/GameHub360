package ca.mcgill.ecse321.GameShop.dto;

public class GameRequestApprovalDto {
    private RequestNoteDto note;
    private Double price;
    private Integer quantityInStock;
    private String rejectionReason;

    // Getters and setters
    public RequestNoteDto getNote() {
        return note;
    }

    public void setNote(RequestNoteDto note) {
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
