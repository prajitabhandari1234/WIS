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
 *
 * @author acer
 */
public class UserData {
    private Connection conn;
    private PreparedStatement findUserStmt, updatePasswordStmt;

    public static record UserDetails(String name, String password) {}

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/USERS", "root", "pass");
            findUserStmt = conn.prepareStatement("SELECT * FROM PASSWORDS WHERE USERNAME = ?");
            updatePasswordStmt = conn.prepareStatement("UPDATE PASSWORDS SET PASSWORD = ? WHERE USERNAME = ?");
        } catch (Exception e) {
            System.err.println("User DB connection error: " + e.getMessage());
        }
    }

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

