package ca.mcgill.ecse321.GameShop.utils;

public class PasswordUtils {

    private static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Validates a password to ensure it meets security criteria:
     * - Minimum length of 8 characters
     * - At least one uppercase and one lowercase letter
     * - At least one digit
     * - At least one special character (e.g., @, #, $, %, ^, &, +, =, !)
     * 
     * @param password the password string to validate
     * @return {@code true} if the password meets all security criteria, {@code false} otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null &&
               password.length() >= MIN_PASSWORD_LENGTH &&
               password.matches(".*[A-Z].*") &&        
               password.matches(".*[a-z].*") &&        
               password.matches(".*\\d.*") &&          
               password.matches(".*[@#$%^&+=!].*");    
    }
}
