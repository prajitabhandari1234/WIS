/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package cqu.wis.roles;

import org.junit.jupiter.api.BeforeEach;
import cqu.wis.data.UserData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserDataValidator}, updated to cover the enhanced
 * password‐complexity rules.
 *
 * @author Prajita Bhandari
 *
 */
public class UserDataValidatorTest {

    private UserDataValidator validator;

    /**
     * Sets up a fresh {@link UserDataValidator} instance before each test.
     */
    @BeforeEach
    public void setup() {
        validator = new UserDataValidator();
    }

    // ======== checkForFieldsPresent (login) ========
    /**
     * Tests that valid username and password are accepted during login
     * validation.
     */
    @Test
    public void checkForFieldsPresentAllPresent() {
        UserDataValidator.ValidationResponse res = validator.checkForFieldsPresent("alice", "secret");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests that missing username during login returns an appropriate error.
     */
    @Test
    public void checkForFieldsPresentUsernameMissing() {
        UserDataValidator.ValidationResponse res = validator.checkForFieldsPresent("", "pwd");
        assertFalse(res.valid());
        assertEquals("Both username and password are required.", res.message());
    }

    /**
     * Tests that missing password during login returns an appropriate error.
     */
    @Test
    public void checkForFieldsPresentPasswordMissing() {
        UserDataValidator.ValidationResponse res = validator.checkForFieldsPresent("alice", "");
        assertFalse(res.valid());
        assertEquals("Both username and password are required.", res.message());
    }

    /**
     * Tests that null values in login credentials are properly handled.
     */
    @Test
    public void checkForFieldsPresentNullValues() {
        UserDataValidator.ValidationResponse res1 = validator.checkForFieldsPresent(null, "pwd");
        UserDataValidator.ValidationResponse res2 = validator.checkForFieldsPresent("alice", null);
        assertFalse(res1.valid());
        assertEquals("Both username and password are required.", res1.message());
        assertFalse(res2.valid());
        assertEquals("Both username and password are required.", res2.message());
    }

    // ======== checkForFieldsPresent (change‐password) ========
    /**
     * Tests that all fields present for password change validation pass.
     */
    @Test
    public void checkForFieldsPresentChangeAllPresent() {
        UserDataValidator.ValidationResponse res
                = validator.checkForFieldsPresent("bob", "oldPass", "newPass");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests missing fields one at a time for password change.
     */
    @Test
    public void checkForFieldsPresentChangeOneMissing() {
        UserDataValidator.ValidationResponse res1
                = validator.checkForFieldsPresent("", "old", "new");
        UserDataValidator.ValidationResponse res2
                = validator.checkForFieldsPresent("bob", "", "new");
        UserDataValidator.ValidationResponse res3
                = validator.checkForFieldsPresent("bob", "old", "");
        assertFalse(res1.valid());
        assertEquals("All fields are required.", res1.message());
        assertFalse(res2.valid());
        assertEquals("All fields are required.", res2.message());
        assertFalse(res3.valid());
        assertEquals("All fields are required.", res3.message());
    }

    /**
     * Tests that null fields in change-password validation are handled.
     */
    @Test
    public void checkForFieldsPresentChangeNullValues() {
        UserDataValidator.ValidationResponse res1
                = validator.checkForFieldsPresent(null, "old", "new");
        UserDataValidator.ValidationResponse res2
                = validator.checkForFieldsPresent("bob", null, "new");
        UserDataValidator.ValidationResponse res3
                = validator.checkForFieldsPresent("bob", "old", null);
        assertFalse(res1.valid());
        assertEquals("All fields are required.", res1.message());
        assertFalse(res2.valid());
        assertEquals("All fields are required.", res2.message());
        assertFalse(res3.valid());
        assertEquals("All fields are required.", res3.message());
    }

    // ======== checkCurrentDetails ========
    /**
     * Tests validation when user is not found in system.
     */
    @Test
    public void checkCurrentDetailsUserNotFound() {
        UserDataValidator.ValidationResponse res = validator.checkCurrentDetails(
                null,
                "alice",
                "whatever"
        );
        assertFalse(res.valid());
        assertEquals("Username not found.", res.message());
    }

    /**
     * Tests system behavior when user is still using default password.
     */
    @Test
    public void checkCurrentDetailsDefaultPassword() {
        UserData.UserDetails ud = new UserData.UserDetails("alice", "password");
        UserDataValidator.ValidationResponse res
                = validator.checkCurrentDetails(ud, "alice", "anything");
        assertTrue(res.valid());
        assertEquals("Default password, please change.", res.message());
    }

    /**
     * Tests successful authentication using SHA-1 encrypted password.
     */
    @Test
    public void checkCurrentDetailsCorrectEncryptedPassword() {
        String raw = "Secret123!";
        String hash = UserDataValidator.generateSHA1(raw);
        assertNotNull(hash);

        UserData.UserDetails ud = new UserData.UserDetails("bob", hash);
        UserDataValidator.ValidationResponse res
                = validator.checkCurrentDetails(ud, "bob", raw);
        assertTrue(res.valid());
        assertEquals("", res.message());
    }

    /**
     * Tests authentication failure for incorrect password.
     */
    @Test
    public void checkCurrentDetailsWrongPassword() {
        String hash = UserDataValidator.generateSHA1("RightOne!");
        UserData.UserDetails ud = new UserData.UserDetails("charlie", hash);

        UserDataValidator.ValidationResponse res
                = validator.checkCurrentDetails(ud, "charlie", "wrong!");
        assertFalse(res.valid());
        assertEquals("Incorrect password.", res.message());
    }

    /**
     * Verifies that known SHA-1 values match expected output.
     */
    @Test
    public void generateSHA1KnownValues() {
        String expectedAbc = "a9993e364706816aba3e25717850c26c9cd0d89d";
        assertEquals(expectedAbc, UserDataValidator.generateSHA1("abc"));

        String expectedEmpty = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
        assertEquals(expectedEmpty, UserDataValidator.generateSHA1(""));
    }

    // ======== checkNewDetails ========
    /**
     * Tests failure when new and confirm passwords do not match.
     */
    @Test
    public void checkNewDetailsPasswordsDoNotMatch() {
        UserData.UserDetails ud = new UserData.UserDetails("david", "anyhash");
        UserDataValidator.ValidationResponse res
                = validator.checkNewDetails(ud, "oldPass", "newOne1!", "different1!");
        assertFalse(res.valid());
        assertEquals("Passwords do not match.", res.message());
    }

    /**
     * Tests rejection of a reused password.
     */
    @Test
    public void checkNewDetailsSameAsOld() {
        UserData.UserDetails ud = new UserData.UserDetails("emma", "anyhash");
        UserDataValidator.ValidationResponse res
                = validator.checkNewDetails(ud, "Reused1!", "Reused1!", "Reused1!");
        assertFalse(res.valid());
        assertEquals("New password must differ from old.", res.message());
    }

    /**
     * Tests rejection of a password that is too short.
     */
    @Test
    public void checkNewDetailsTooShort() {
        UserData.UserDetails ud = new UserData.UserDetails("frank", "oldHash");
        UserDataValidator.ValidationResponse res
                = validator.checkNewDetails(ud, "old1234!", "Ab1$xyz", "Ab1$xyz");
        assertFalse(res.valid());
        assertEquals("Password must be at least 8 characters long.", res.message());
    }

    /**
     * Tests that password missing a digit is rejected.
     */
    @Test
    public void checkNewDetailsMissingDigit() {
        UserData.UserDetails ud = new UserData.UserDetails("gina", "oldHash");
        UserDataValidator.ValidationResponse res
                = validator.checkNewDetails(ud, "oldPass1!", "NoDigit$A", "NoDigit$A");
        assertFalse(res.valid());
        assertEquals("Password must contain at least one digit.", res.message());
    }

    /**
     * Tests that password missing a special character is rejected.
     */
    @Test
    public void checkNewDetailsMissingSpecialCharacter() {
        UserData.UserDetails ud = new UserData.UserDetails("harry", "oldHash");
        UserDataValidator.ValidationResponse res
                = validator.checkNewDetails(ud, "oldPass1!", "NoSpecial1A", "NoSpecial1A");
        assertFalse(res.valid());
        assertEquals("Password must contain at least one non‐alphanumeric character.", res.message());
    }

    /**
     * Tests that password missing an uppercase letter is rejected.
     */
    @Test
    public void checkNewDetailsMissingUppercase() {
        UserData.UserDetails ud = new UserData.UserDetails("ivy", "oldHash");
        UserDataValidator.ValidationResponse res
                = validator.checkNewDetails(ud, "oldPass1!", "lowercase1$", "lowercase1$");
        assertFalse(res.valid());
        assertEquals("Password must contain at least one uppercase letter.", res.message());
    }

    /**
     * Tests that password missing a lowercase letter is rejected.
     */
    @Test
    public void checkNewDetailsMissingLowercase() {
        UserData.UserDetails ud = new UserData.UserDetails("jack", "oldHash");
        UserDataValidator.ValidationResponse res
                = validator.checkNewDetails(ud, "oldPass1!", "ALLUPPER1$", "ALLUPPER1$");
        assertFalse(res.valid());
        assertEquals("Password must contain at least one lowercase letter.", res.message());
    }

    /**
     * Tests rejection of passwords that contain backslash.
     */
    @Test
    public void checkNewDetailsContainsBackslash() {
        UserData.UserDetails ud = new UserData.UserDetails("kate", "oldHash");
        UserDataValidator.ValidationResponse res
                = validator.checkNewDetails(ud, "oldPass1!", "Valid1\\$", "Valid1\\$");
        assertFalse(res.valid());
        assertEquals("Password cannot contain backslash or quote characters.", res.message());
    }

    /**
     * Tests rejection of passwords that contain single or double quote
     * characters.
     */
    @Test
    public void checkNewDetailsContainsQuote() {
        UserData.UserDetails ud = new UserData.UserDetails("liam", "oldHash");
        UserDataValidator.ValidationResponse res1
                = validator.checkNewDetails(ud, "oldPass1!", "Vali'd1$", "Vali'd1$");
        assertFalse(res1.valid());
        assertEquals("Password cannot contain backslash or quote characters.", res1.message());

        UserDataValidator.ValidationResponse res2
                = validator.checkNewDetails(ud, "oldPass1!", "Vali\"d1$", "Vali\"d1$");
        assertFalse(res2.valid());
        assertEquals("Password cannot contain backslash or quote characters.", res2.message());
    }

    /**
     * Tests a valid password change with a complex, matching password.
     */
    @Test
    public void checkNewDetailsValidChange() {
        UserData.UserDetails ud = new UserData.UserDetails("mia", "oldHash");
        UserDataValidator.ValidationResponse res
                = validator.checkNewDetails(ud, "old1234!", "NewPass1$", "NewPass1$");
        assertTrue(res.valid());
        assertEquals("", res.message());
    }
}
