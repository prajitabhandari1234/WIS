/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package cqu.wis.roles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link WhiskeyDataValidator}.
 * <p>
 * Tests cover validation for whiskey regions and age ranges.
 * </p>
 *
 * @author Prajita Bhandari
 *
 */
public class WhiskeyDataValidatorTest {

    private WhiskeyDataValidator validator;

    /**
     * Sets up a new instance of the validator before each test.
     */
    @BeforeEach
    public void setup() {
        validator = new WhiskeyDataValidator();
    }

    // ======== checkRegion ========
    /**
     * Validates a proper alphabetic region name.
     */
    @Test
    public void validRegionTest() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkRegion("Highland");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests region name containing a digit.
     */
    @Test
    public void invalidRegionContainsDigit() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkRegion("Islay1");
        assertFalse(res.valid());
        assertEquals("Region must be alphabetic.", res.message());
    }

    /**
     * Tests region name containing a special character.
     */
    @Test
    public void invalidRegionSpecialChar() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkRegion("Islay@");
        assertFalse(res.valid());
        assertEquals("Region must be alphabetic.", res.message());
    }

    /**
     * Tests validation of an empty region string.
     */
    @Test
    public void invalidRegionEmptyString() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkRegion("");
        assertFalse(res.valid());
        assertEquals("Region must be alphabetic.", res.message());
    }

    /**
     * Tests validation of a null region input.
     */
    @Test
    public void invalidRegionNull() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkRegion(null);
        assertFalse(res.valid());
        assertEquals("Region must be alphabetic.", res.message());
    }

    /**
     * Tests a valid region with mixed case characters.
     */
    @Test
    public void validRegionMixedCase() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkRegion("HighLand");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests a region string consisting only of whitespace.
     */
    @Test
    public void invalidRegionWhitespaceOnly() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkRegion("   ");
        assertFalse(res.valid());
        assertEquals("Region must be alphabetic.", res.message());
    }

    // ======== checkAgeRange ========
    /**
     * Tests a valid numeric age range with lower and upper bounds.
     */
    @Test
    public void validAgeRangeNormalBounds() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("10", "30");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests an age range where both bounds are the same.
     */
    @Test
    public void validAgeRangeEqualBounds() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("20", "20");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests reversed age bounds where the lower is greater than the upper.
     */
    @Test
    public void invalidAgeRangeReversedBounds() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("30", "10");
        assertFalse(res.valid());
        assertEquals("Lower bound must not exceed upper bound.", res.message());
    }

    /**
     * Tests validation when both age fields are empty.
     */
    @Test
    public void validAgeRangeEmptyBoth() {
        // defaults to 0–100
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("", "");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests age range with only the right (upper) bound provided.
     */
    @Test
    public void validAgeRangeLeftEmpty() {
        // defaults to 0–25
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("", "25");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests age range with only the left (lower) bound provided.
     */
    @Test
    public void validAgeRangeRightEmpty() {
        // defaults to 25–100
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("25", "");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests non-numeric input in age fields.
     */
    @Test
    public void invalidAgeRangeNonNumeric() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("ten", "twenty");
        assertFalse(res.valid());
        assertEquals("Invalid age format.", res.message());
    }

    /**
     * Tests age range where the lower bound is a negative number.
     */
    @Test
    public void validAgeRangeNegativeLower() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("-5", "15");
        // Negative parses fine →  -5 ≤ 15
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests validation of very large age bounds.
     */
    @Test
    public void validAgeRangeLargeNumbers() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("1000000", "2000000");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests an age input where a numeric value is partially corrupted with
     * letters.
     */
    @Test
    public void invalidAgeRangePartialNumeric() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange("25a", "50");
        assertFalse(res.valid());
        assertEquals("Invalid age format.", res.message());
    }

    /**
     * Tests an age input with leading/trailing whitespace.
     */
    @Test
    public void invalidAgeRangeEmbeddedSpaces() {
        WhiskeyDataValidator.ValidationResponse res = validator.checkAgeRange(" 10 ", "20");
        assertFalse(res.valid());
        assertEquals("Invalid age format.", res.message());
    }
}
