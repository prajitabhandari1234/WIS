/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

import cqu.wis.data.UserData;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Validator class for user data operations including authentication, password
 * validation, and field‐presence checking.
 *
 * <p>
 * This class provides methods to:</p>
 * <ul>
 * <li>Generate SHA‐1 hash for passwords</li>
 * <li>Validate presence of input fields</li>
 * <li>Check current user credentials</li>
 * <li>Validate new password against strength and security policies</li>
 * </ul>
 *
 * @author Prajita Bhandari
 *
 */
public class UserDataValidator {

    /**
     * Minimum acceptable length for a new password.
     */
    private static final int MINIMUM_PASSWORD_LENGTH = 8;

    /**
     * Immutable record representing the result of a validation check.
     *
     * @param valid Boolean indicating whether the check passed.
     * @param message A descriptive message, empty if {@code valid} is true.
     */
    public record ValidationResponse(boolean valid, String message) {

    }

    /**
     * Generates a SHA‐1 hash of the provided input string.
     *
     * @param s The string to hash (e.g., a password); must not be {@code null}.
     * @return Lowercase hexadecimal string of the SHA‐1 hash, or {@code null}
     * if hashing fails.
     */
    public static String generateSHA1(String s) {
        if (s == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(s.getBytes());
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Validates that the login fields are not null or empty.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return A {@link ValidationResponse} indicating whether both fields are
     * present.
     */
    public ValidationResponse checkForFieldsPresent(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return new ValidationResponse(false, "Both username and password are required.");
        }
        return new ValidationResponse(true, "");
    }

    /**
     * Validates that all fields required for password change are not null or
     * empty.
     *
     * @param username The entered username.
     * @param oldPass The current password.
     * @param newPass The proposed new password.
     * @return A {@link ValidationResponse} indicating if all required fields
     * are filled.
     */
    public ValidationResponse checkForFieldsPresent(String username, String oldPass, String newPass) {
        if (username == null || username.trim().isEmpty()
                || oldPass == null || oldPass.trim().isEmpty()
                || newPass == null || newPass.trim().isEmpty()) {
            return new ValidationResponse(false, "All fields are required.");
        }
        return new ValidationResponse(true, "");
    }

    /**
     * Validates the entered password against the stored user credentials.
     *
     * <p>
     * Handles special case where the default password "password" forces a
     * reset.</p>
     *
     * @param ud The stored user record from the database.
     * @param enteredName The username entered (not used in validation here).
     * @param enteredPass The password entered by the user.
     * @return A {@link ValidationResponse} indicating whether credentials are
     * valid.
     */
    public ValidationResponse checkCurrentDetails(
            UserData.UserDetails ud,
            String enteredName,
            String enteredPass
    ) {
        // 1) No user record found
        if (ud == null) {
            return new ValidationResponse(false, "Username not found.");
        }

        // 2) User has default password
        if ("password".equals(ud.password())) {
            return new ValidationResponse(true, "Default password, please change.");
        }

        // 3) Compare SHA-1 hashes
        String hashedInput = generateSHA1(enteredPass);
        if (hashedInput == null || !hashedInput.equals(ud.password())) {
            return new ValidationResponse(false, "Incorrect password.");
        }

        // 4) Passwords match
        return new ValidationResponse(true, "");
    }

    /**
     * Validates a new password request including match, complexity, and
     * difference from old.
     *
     * @param ud The current user record (unused directly, assumed non-null by
     * caller).
     * @param oldPass The current password entered by the user.
     * @param newPass The proposed new password.
     * @param confirm The confirmation password (should match newPass).
     * @return A {@link ValidationResponse} detailing whether the change is
     * allowed.
     */
    public ValidationResponse checkNewDetails(
            UserData.UserDetails ud,
            String oldPass,
            String newPass,
            String confirm
    ) {
        // 1) Check newPass matches confirmation
        if (!newPass.equals(confirm)) {
            return new ValidationResponse(false, "Passwords do not match.");
        }

        // 2) Ensure newPass is different from oldPass
        if (oldPass.equals(newPass)) {
            return new ValidationResponse(false, "New password must differ from old.");
        }

        // 3) Password complexity rules
        if (newPass.length() < MINIMUM_PASSWORD_LENGTH) {
            return new ValidationResponse(false, "Password must be at least 8 characters long.");
        }

        boolean hasDigit = false;
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;

        for (char c : newPass.toCharArray()) {
            // Reject backslash or quotes
            if (c == '\\' || c == '"' || c == '\'') {
                return new ValidationResponse(false,
                        "Password cannot contain backslash or quote characters.");
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        if (!hasDigit) {
            return new ValidationResponse(false, "Password must contain at least one digit.");
        }
        if (!hasSpecial) {
            return new ValidationResponse(false, "Password must contain at least one non‐alphanumeric character.");
        }
        if (!hasUpper) {
            return new ValidationResponse(false, "Password must contain at least one uppercase letter.");
        }
        if (!hasLower) {
            return new ValidationResponse(false, "Password must contain at least one lowercase letter.");
        }

        // 4) All complexity rules passed
        return new ValidationResponse(true, "");
    }
}
