package ca.mcgill.ecse321.GameShop.dto;

import java.util.Map;

public class ErrorResponseDto {
    // Attributes
    private String error;
    private String status;
    private Map<String, String> fieldErrors;

    // Constructors
    public ErrorResponseDto() {
    }

    public ErrorResponseDto(String error, String status) {
        this.error = error;
        this.status = status;
    }

    public ErrorResponseDto(String error, String status, Map<String, String> fieldErrors) {
        this.error = error;
        this.status = status;
        this.fieldErrors = fieldErrors;
    }

    // Getters and Setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
