/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

import cqu.wis.data.UserData;

/**
 * Manager class that provides a business logic layer for user data operations.
 *
 * <p>
 * This class acts as an intermediary between controllers and the
 * {@link UserData} data access object (DAO). It handles validation and invokes
 * lower-level database operations such as retrieving user records and updating
 * passwords.</p>
 *
 * <p>
 * Used by controller classes to abstract away direct interaction with database
 * APIs.</p>
 *
 * @author Prajita Bhandari
 *
 */
public class UserDataManager {

    /**
     * Reference to the UserData object for performing database operations.
     */
    private final UserData ud;

    /**
     * Constructs a new UserDataManager with the specified UserData instance.
     *
     * @param ud The {@link UserData} instance to use for database operations.
     * Must not be {@code null}.
     * @throws NullPointerException If {@code ud} is {@code null}.
     */
    public UserDataManager(UserData ud) {
        if (ud == null) {
            throw new NullPointerException("UserData cannot be null");
        }
        this.ud = ud;
    }

    /**
     * Finds and retrieves user details by username.
     *
     * @param name The username to search for. Must not be {@code null} or
     * empty.
     * @return A {@link cqu.wis.data.UserData.UserDetails} record containing
     * user information if found, or {@code null} if no user exists with the
     * specified username or if a database error occurs.
     * @throws IllegalArgumentException If {@code name} is {@code null} or
     * empty.
     */
    public cqu.wis.data.UserData.UserDetails findUser(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return ud.findUser(name);
    }

    /**
     * Updates the password for an existing user.
     *
     * @param name The username of the user whose password should be updated.
     * Must not be {@code null} or empty.
     * @param newPassword The new password to set for the user (hashed). Must
     * not be {@code null}.
     * @throws IllegalArgumentException If {@code name} is {@code null} or
     * empty, or if {@code newPassword} is {@code null}.
     */
    public void updatePassword(String name, String newPassword) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (newPassword == null) {
            throw new IllegalArgumentException("New password cannot be null");
        }
        ud.updatePassword(name, newPassword);
    }
}
