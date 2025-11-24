/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

import cqu.wis.data.UserData;
import java.security.MessageDigest;
import java.util.regex.Pattern;

/**
 *
 * @author acer
 */
public class UserDataValidator {
    public WhiskeyDataValidator.ValidationResponse checkForFieldsPresent(String... fields) {
        for (String f : fields) {
            if (f == null || f.trim().isEmpty()) {
                return new WhiskeyDataValidator.ValidationResponse(false, "All fields are required.");
            }
        }
        return new WhiskeyDataValidator.ValidationResponse(true, "");
    }

    public WhiskeyDataValidator.ValidationResponse checkCurrentDetails(UserData.UserDetails ud, String enteredName, String enteredPassword) {
        if (ud == null) return new WhiskeyDataValidator.ValidationResponse(false, "Username not found.");
        if (ud.password().equals("password")) {
            return new WhiskeyDataValidator.ValidationResponse(true, "Default password, please change.");
        }
        if (!ud.password().equals(generateSHA1(enteredPassword))) {
            return new WhiskeyDataValidator.ValidationResponse(false, "Incorrect password.");
        }
        return new WhiskeyDataValidator.ValidationResponse(true, "");
    }

    public WhiskeyDataValidator.ValidationResponse checkNewDetails(UserData.UserDetails ud, String oldPass, String newPass, String confirmPass) {
        if (!newPass.equals(confirmPass)) return new WhiskeyDataValidator.ValidationResponse(false, "Passwords do not match.");
        if (oldPass.equals(newPass)) return new WhiskeyDataValidator.ValidationResponse(false, "New password must differ from old.");
        if (!isValidPassword(newPass)) return new WhiskeyDataValidator.ValidationResponse(false, "Weak password.");
        return new WhiskeyDataValidator.ValidationResponse(true, "");
    }

    public String generateSHA1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
               Pattern.compile("[A-Z]").matcher(password).find() &&
               Pattern.compile("[a-z]").matcher(password).find() &&
               Pattern.compile("\\d").matcher(password).find() &&
               Pattern.compile("[^a-zA-Z0-9]").matcher(password).find() &&
               !password.contains("\\") &&
               !password.contains("'") &&
               !password.contains("\"");
    }
}

