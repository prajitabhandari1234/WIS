/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

/**
 *
 * @author acer
 */
public class WhiskeyDataValidator {
    public record ValidationResponse(boolean isValid, String message) {}

    public ValidationResponse checkRegion(String region) {
        if (region == null || !region.matches("[a-zA-Z]+")) {
            return new ValidationResponse(false, "Region must be alphabetic.");
        }
        return new ValidationResponse(true, "");
    }

    public ValidationResponse checkAgeRange(String left, String right) {
        try {
            int l = left.isEmpty() ? 0 : Integer.parseInt(left);
            int r = right.isEmpty() ? 100 : Integer.parseInt(right);
            if (l > r) return new ValidationResponse(false, "Lower bound must not exceed upper bound.");
            return new ValidationResponse(true, "");
        } catch (NumberFormatException e) {
            return new ValidationResponse(false, "Invalid age format.");
        }
    }
}