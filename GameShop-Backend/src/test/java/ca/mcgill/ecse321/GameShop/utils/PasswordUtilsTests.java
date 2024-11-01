package ca.mcgill.ecse321.GameShop.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PasswordUtilsTests {

    @Test
    public void testValidPassword() {
        // Arrange
        String password = "Str0ng@Pass!";

        // Act
        boolean isValid = PasswordUtils.isValidPassword(password);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testInvalidPasswordTooShort() {
        // Arrange
        String password = "Short1!";

        // Act
        boolean isValid = PasswordUtils.isValidPassword(password);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testInvalidPasswordNoUppercase() {
        // Arrange
        String password = "weak@pass1";

        // Act
        boolean isValid = PasswordUtils.isValidPassword(password);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testInvalidPasswordNoLowercase() {
        // Arrange
        String password = "STRONG@PASS1";

        // Act
        boolean isValid = PasswordUtils.isValidPassword(password);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testInvalidPasswordNoDigit() {
        // Arrange
        String password = "NoDigit@Pass";

        // Act
        boolean isValid = PasswordUtils.isValidPassword(password);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testInvalidPasswordNoSpecialCharacter() {
        // Arrange
        String password = "NoSpecialChar1";

        // Act
        boolean isValid = PasswordUtils.isValidPassword(password);

        // Assert
        assertFalse(isValid);
    }
}
