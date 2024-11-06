package ca.mcgill.ecse321.GameShop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public class GameShopException extends RuntimeException {
    @NonNull
    private final HttpStatus status;

    public GameShopException(@NonNull HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}
