package cqu.wis.data;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author acer
 */
public class WhiskeyData {
    private Connection conn;
    private PreparedStatement getAllMalts;

    public static record WhiskeyDetails(String distillery, int age, String region, int price) {}

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/WHISKEY", "root", "pass");
            getAllMalts = conn.prepareStatement("SELECT * FROM SINGLEMALTS");
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
            System.exit(0);
        }
    }

    public void disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Disconnection error: " + e.getMessage());
        }
    }

    public List<WhiskeyDetails> getAllMalts() {
        List<WhiskeyDetails> results = new ArrayList<>();
        try (ResultSet rs = getAllMalts.executeQuery()) {
            while (rs.next()) {
                results.add(new WhiskeyDetails(
                    rs.getString("DISTILLERY"),
                    rs.getInt("AGE"),
                    rs.getString("REGION"),
                    rs.getInt("PRICE")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Query error: " + e.getMessage());
        }
        return results;
    }
}
