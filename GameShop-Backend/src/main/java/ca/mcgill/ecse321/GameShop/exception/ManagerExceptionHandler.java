package ca.mcgill.ecse321.GameShop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ManagerExceptionHandler {

    /**
     * Handles ManagerException by returning a response with error details.
     * 
     * @param e the thrown ManagerException.
     * @return a ResponseEntity with error message and status.
     */
    @ExceptionHandler(ManagerException.class)
    public ResponseEntity<Map<String, String>> handleManagerException(ManagerException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        errorResponse.put("status", e.getStatus().toString());
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

    /**
     * Handles validation errors and returns a response containing field-specific errors.
     * 
     * @param ex the thrown MethodArgumentNotValidException.
     * @return a ResponseEntity with validation errors and BAD_REQUEST status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        
        errors.put("error", "Validation failed");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
