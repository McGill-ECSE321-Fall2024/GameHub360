package ca.mcgill.ecse321.GameShop.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class EncryptionUtils {

    /**
     * Encrypts a plaintext string using BCrypt.
     *
     * @param plainText The plaintext string to encrypt.
     * @return The encrypted string.
     */
    public static String encrypt(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    /**
     * Checks if a plaintext string matches a previously encrypted string.
     *
     * @param plainText The plaintext string to check.
     * @param hashed The encrypted string to compare against.
     * @return true if the plaintext matches the encrypted string, false otherwise.
     */
    public static boolean matches(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }
}
