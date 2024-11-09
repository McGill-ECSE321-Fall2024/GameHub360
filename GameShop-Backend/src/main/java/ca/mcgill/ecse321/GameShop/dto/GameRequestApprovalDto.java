package ca.mcgill.ecse321.GameShop.dto;

/**
 * Data Transfer Object for handling game request approvals.
 * This includes details such as the note, price, quantity in stock, and rejection reason.
 */
public class GameRequestApprovalDto {

    private RequestNoteDto note;
    private Double price;
    private Integer quantityInStock;
    private String rejectionReason;

    /**
     * Retrieves the note associated with the game request approval.
     *
     * @return the note as a RequestNoteDto object.
     */
    public RequestNoteDto getNote() {
        return note;
    }

    /**
     * Sets the note for the game request approval.
     *
     * @param note the RequestNoteDto object to set.
     */
    public void setNote(RequestNoteDto note) {
        this.note = note;
    }

    /**
     * Retrieves the price associated with the game request approval.
     *
     * @return the price as a Double.
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price for the game request approval.
     *
     * @param price the Double value to set.
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Retrieves the quantity in stock for the game request approval.
     *
     * @return the quantity in stock as an Integer.
     */
    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Sets the quantity in stock for the game request approval.
     *
     * @param quantityInStock the Integer value to set.
     */
    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    /**
     * Retrieves the rejection reason for the game request approval.
     *
     * @return the rejection reason as a String.
     */
    public String getRejectionReason() {
        return rejectionReason;
    }

    /**
     * Sets the rejection reason for the game request approval.
     *
     * @param rejectionReason the String value to set.
     */
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
