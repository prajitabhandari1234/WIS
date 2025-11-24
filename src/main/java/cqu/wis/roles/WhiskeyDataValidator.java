/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

/**
 * Validator class specifically designed for whiskey data input validation.
 * <p>
 * Provides validation methods for whiskey-related search parameters including
 * geographic regions and age ranges. Ensures data integrity and format
 * compliance before processing whiskey search and filter operations.
 * </p>
 *
 * @author Prajita Bhandari
 *
 */
public class WhiskeyDataValidator {

    /**
     * Immutable record class that encapsulates whiskey data validation results.
     *
     * @param valid Indicates if the validation succeeded (true) or failed
     * (false).
     * @param message Descriptive feedback message (empty if valid, error
     * description if invalid).
     */
    public record ValidationResponse(boolean valid, String message) {

    }

    /**
     * Validates whiskey region input for geographic filtering operations.
     * Ensures the region parameter contains only alphabetic characters and is
     * not null or empty.
     *
     * @param region The geographic region string to validate. Must not be
     * {@code null}.
     * @return A {@link ValidationResponse} containing:
     * <ul>
     * <li>{@code valid == false} and message "Region must be alphabetic." if
     * invalid</li>
     * <li>{@code valid == true} and empty message if valid</li>
     * </ul>
     */
    public ValidationResponse checkRegion(String region) {
        if (region == null || region.isEmpty() || !region.matches("[a-zA-Z]+")) {
            return new ValidationResponse(false, "Region must be alphabetic.");
        }
        return new ValidationResponse(true, "");
    }

    /**
     * Validates age range parameters for whiskey age filtering operations.
     * Processes string inputs for minimum and maximum age bounds, applying
     * default values for empty inputs and ensuring logical range constraints.
     *
     * @param left String representation of the minimum age bound. Empty string
     * defaults to 0. Must be parseable as a valid integer if not empty.
     * @param right String representation of the maximum age bound. Empty string
     * defaults to 100. Must be parseable as a valid integer if not empty.
     * @return A {@link ValidationResponse} containing:
     * <ul>
     * <li>{@code valid == false} with "Invalid age format." if non-numeric</li>
     * <li>{@code valid == false} with "Lower bound must not exceed upper
     * bound." if left > right</li>
     * <li>{@code valid == true} and empty message if both parameters are
     * valid</li>
     * </ul>
     */
    public ValidationResponse checkAgeRange(String left, String right) {
        try {
            int l = left.isEmpty() ? 0 : Integer.parseInt(left);
            int r = right.isEmpty() ? 100 : Integer.parseInt(right);
            if (l > r) {
                return new ValidationResponse(false, "Lower bound must not exceed upper bound.");
            }
            return new ValidationResponse(true, "");
        } catch (NumberFormatException e) {
            return new ValidationResponse(false, "Invalid age format.");
        }
    }
}
