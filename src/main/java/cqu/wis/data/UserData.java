/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access layer class for managing user authentication data in the
 * database.
 *
 * <p>
 * This class connects to a MySQL USERS database and performs operations related
 * to user credential lookup and password updates. It uses
 * {@link PreparedStatement} to avoid SQL injection and manages connection
 * internally via JDBC.</p>
 *
 * <p>
 * Used in the Whiskey Information System (WIS) for secure user login and
 * credential management.</p>
 *
 * @author Prajita Bhandari
 */
public class UserData {

    /**
     * JDBC connection to the USERS database.
     */
    private Connection conn;

    /**
     * Prepared SQL statement to retrieve user details by username.
     */
    private PreparedStatement findUserStmt;

    /**
     * Prepared SQL statement to update a user's password.
     */
    private PreparedStatement updatePasswordStmt;

    /**
     * Immutable record class representing user details.
     *
     * <p>
     * This record holds both the username and password associated with a user
     * account.</p>
     *
     * @param name The username of the user. Must not be {@code null} or empty.
     * @param password The password of the user (may be hashed). Must not be
     * {@code null}.
     */
    public static record UserDetails(String name, String password) {

    }

    /**
     * Establishes a connection to the MySQL USERS database and prepares
     * reusable SQL statements.
     *
     * <p>
     * Connection details:</p>
     * <ul>
     * <li>URL: {@code jdbc:mysql://localhost:3306/USERS}</li>
     * <li>Username: {@code root}</li>
     * <li>Password: {@code pass}</li>
     * </ul>
     *
     * @throws SQLException If a database access error occurs, such as
     * connection failure or invalid credentials.
     */
    public void connect() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/USERS", "root", "pass"
        );
        findUserStmt = conn.prepareStatement(
                "SELECT * FROM PASSWORDS WHERE USERNAME = ?"
        );
        updatePasswordStmt = conn.prepareStatement(
                "UPDATE PA" + "SSWORDS SET PASSWORD = ? WHERE USERNAME = ?"
        );
    }

    /**
     * Retrieves user credentials from the database by username.
     *
     * @param name The username to search for. Must not be {@code null} or
     * empty.
     * @return A {@link UserDetails} record containing the username and password
     * if found; otherwise, returns {@code null} if the user is not found or a
     * database error occurs.
     */
    public UserDetails findUser(String name) {
        try {
            findUserStmt.setString(1, name);
            ResultSet rs = findUserStmt.executeQuery();
            if (rs.next()) {
                return new UserDetails(name, rs.getString("PASSWORD"));
            }
        } catch (SQLException e) {
            System.err.println("Find user error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates the password for a given user in the database.
     *
     * @param name The username whose password should be updated. Must not be
     * {@code null} or empty.
     * @param newPassword The new password to store (typically hashed). Must not
     * be {@code null}.
     */
    public void updatePassword(String name, String newPassword) {
        try {
            updatePasswordStmt.setString(1, newPassword);
            updatePasswordStmt.setString(2, name);
            updatePasswordStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Update password error: " + e.getMessage());
        }
    }
}
