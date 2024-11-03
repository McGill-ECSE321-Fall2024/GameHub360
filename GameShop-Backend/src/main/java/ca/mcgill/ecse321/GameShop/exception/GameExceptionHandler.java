package ca.mcgill.ecse321.GameShop.exception;

import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(GameException.class)
    public ResponseEntity<ErrorResponseDto> handleGameException(GameException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), e.getStatus().toString());
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }
}
