package ca.mcgill.ecse321.GameShop.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ManagerExceptionHandler {

    @ExceptionHandler(ManagerException.class)
    public ResponseEntity<String> handleManagerException(ManagerException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}
