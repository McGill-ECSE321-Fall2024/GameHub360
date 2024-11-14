package ca.mcgill.ecse321.GameShop.utils;

public class PhoneUtils {

    private static final int MIN_PHONE_LENGTH = 10;
    private static final int MAX_PHONE_LENGTH = 15;

    /**
     * Validates a phone number to ensure it meets common formatting criteria:
     * - Only contains digits and optional characters for formatting (+, -, space, parentheses).
     * - Has a length between 10 and 15 digits (typical for local and international numbers).
     * 
     * @param phoneNumber the phone number string to validate
     * @return {@code true} if the phone number meets all criteria, {@code false} otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null &&
               phoneNumber.matches("^\\+?[0-9\\-\\s()]{10,15}$") && // Optional + and 10-15 characters
               phoneNumber.replaceAll("[^\\d]", "").length() >= MIN_PHONE_LENGTH &&
               phoneNumber.replaceAll("[^\\d]", "").length() <= MAX_PHONE_LENGTH;
    }
}
