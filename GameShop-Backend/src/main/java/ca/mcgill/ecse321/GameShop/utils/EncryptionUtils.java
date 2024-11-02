package ca.mcgill.ecse321.GameShop.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtils {

    private static final String ALGORITHM = "SHA-256";

    /**
     * Encrypts a plaintext string using SHA-256 and a generated salt.
     *
     * @param plainText The plaintext string to encrypt.
     * @return The encrypted string with the salt appended.
     */
    public static String encrypt(String plainText) {
        byte[] salt = generateSalt();
        String saltStr = Base64.getEncoder().encodeToString(salt);
        String hash = hashWithSalt(plainText, salt);
        return hash + ":" + saltStr; // Store hash with salt for verification
    }

    /**
     * Checks if a plaintext string matches a previously encrypted string.
     *
     * @param plainText      The plaintext string to check.
     * @param hashedWithSalt The stored encrypted string (hash:salt).
     * @return true if the plaintext matches the encrypted string, false otherwise.
     */
    public static boolean matches(String plainText, String hashedWithSalt) {
        String[] parts = hashedWithSalt.split(":");
        String storedHash = parts[0];
        String saltStr = parts[1];
        byte[] salt = Base64.getDecoder().decode(saltStr);
        String calculatedHash = hashWithSalt(plainText, salt);
        return storedHash.equals(calculatedHash);
    }

    /**
     * Hashes a plaintext string with a given salt using SHA-256.
     *
     * @param plainText The plaintext to hash.
     * @param salt      The salt to use in hashing.
     * @return The hashed string.
     */
    private static String hashWithSalt(String plainText, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedBytes = md.digest(plainText.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing with SHA-256", e);
        }
    }

    /**
     * Generates a random salt.
     *
     * @return A byte array containing the generated salt.
     */
    private static byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
