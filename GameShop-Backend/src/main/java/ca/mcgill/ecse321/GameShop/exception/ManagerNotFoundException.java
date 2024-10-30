package ca.mcgill.ecse321.GameShop.exception;

public class ManagerNotFoundException extends RuntimeException {

    public ManagerNotFoundException(String message) {
        super(message);
    }
}
