package ca.mcgill.ecse321.GameShop.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EncryptionUtilsTests {

    @Test
    public void testEncryptAndMatch() {
        // Arrange
        String password = "SecureP@ssw0rd";

        // Act
        String encryptedPassword = EncryptionUtils.encrypt(password);

        // Assert
        assertTrue(EncryptionUtils.matches(password, encryptedPassword));
    }

    @Test
    public void testMatchWithDifferentPassword() {
        // Arrange
        String originalPassword = "Original@P@ss1";
        String encryptedPassword = EncryptionUtils.encrypt(originalPassword);

        // Act
        boolean matches = EncryptionUtils.matches("Different@P@ss2", encryptedPassword);

        // Assert
        assertFalse(matches);
    }

    @Test
    public void testEncryptProducesDifferentHashes() {
        // Arrange
        String password = "SameP@ssw0rd";

        // Act
        String encryptedPassword1 = EncryptionUtils.encrypt(password);
        String encryptedPassword2 = EncryptionUtils.encrypt(password);

        // Assert
        assertFalse(encryptedPassword1.equals(encryptedPassword2),
                "Encryption should produce different hashes each time for security.");
    }
}
