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
 * Data access layer class for managing whiskey information in the database.
 *
 * <p>
 * This class connects to a MySQL {@code WHISKEY} database and provides methods
 * to retrieve whiskey records, filter by region, and filter by age range.</p>
 *
 * <p>
 * It uses {@link PreparedStatement} to prevent SQL injection and handles all
 * SQL-related exceptions gracefully, returning empty lists in case of
 * errors.</p>
 *
 * @author Prajita Bhandari
 *
 */
public class WhiskeyData {

    /**
     * JDBC connection to the WHISKEY database.
     */
    private Connection conn;

    /**
     * Prepared SQL statement to retrieve all single malt whiskey records.
     */
    private PreparedStatement getAllMalts;

    /**
     * Prepared SQL statement to retrieve whiskey records filtered by region.
     */
    private PreparedStatement getMaltsFromRegion;

    /**
     * Prepared SQL statement to retrieve whiskey records filtered by age range.
     */
    private PreparedStatement getMaltsInAgeRange;

    /**
     * Immutable record class representing whiskey details retrieved from the
     * database.
     *
     * @param distillery Name of the distillery.
     * @param age Age of the whiskey in years.
     * @param region Region of origin for the whiskey.
     * @param price Price of the whiskey.
     */
    public static record WhiskeyDetails(String distillery, int age, String region, int price) {

    }

    /**
     * Establishes a JDBC connection to the WHISKEY database and prepares SQL
     * statements for future execution.
     *
     * <p>
     * Database details:</p>
     * <ul>
     * <li>URL: {@code jdbc:mysql://localhost:3306/WHISKEY}</li>
     * <li>Username: {@code root}</li>
     * <li>Password: {@code pass}</li>
     * </ul>
     *
     * @throws SQLException if a database access error occurs or the connection
     * fails.
     */
    public void connect() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/WHISKEY", "root", "pass"
        );
        getAllMalts = conn.prepareStatement("SELECT * FROM SINGLEMALTS");
        getMaltsFromRegion = conn.prepareStatement("SELECT * FROM SINGLEMALTS WHERE REGION = ?");
        getMaltsInAgeRange = conn.prepareStatement("SELECT * FROM SINGLEMALTS WHERE AGE BETWEEN ? AND ?");
    }

    /**
     * Closes the database connection and any associated resources. Logs an
     * error message if disconnection fails.
     */
    public void disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Disconnection error: " + e.getMessage());
        }
    }

    /**
     * Retrieves all single-malt whiskey records from the database.
     *
     * @return a list of {@link WhiskeyDetails}, or an empty list if no data or
     * an error occurs.
     */
    public List<WhiskeyDetails> getAllMalts() {
        return executeQuery(getAllMalts);
    }

    /**
     * Retrieves all single-malt whiskey records from a specific region.
     *
     * @param region the region to filter by (e.g., "Islay", "Highland").
     * @return a list of {@link WhiskeyDetails} from the specified region, or an
     * empty list if none found or a SQL error occurs.
     */
    public List<WhiskeyDetails> getMaltsFromRegion(String region) {
        try {
            getMaltsFromRegion.setString(1, region);
            return executeQuery(getMaltsFromRegion);
        } catch (SQLException e) {
            System.err.println("Region query error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all single-malt whiskey records within a specific age range.
     *
     * @param lower the lower bound of the age range (inclusive).
     * @param upper the upper bound of the age range (inclusive).
     * @return a list of {@link WhiskeyDetails} within the given age range, or
     * an empty list if none match or a SQL error occurs.
     */
    public List<WhiskeyDetails> getMaltsInAgeRange(int lower, int upper) {
        try {
            getMaltsInAgeRange.setInt(1, lower);
            getMaltsInAgeRange.setInt(2, upper);
            return executeQuery(getMaltsInAgeRange);
        } catch (SQLException e) {
            System.err.println("Age range query error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Executes the given {@link PreparedStatement} and converts the result set
     * into a list of {@link WhiskeyDetails} records.
     *
     * @param stmt a pre-configured SQL statement ready for execution.
     * @return a list of whiskey records from the database, or an empty list if
     * an error occurs.
     */
    private List<WhiskeyDetails> executeQuery(PreparedStatement stmt) {
        List<WhiskeyDetails> results = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                results.add(new WhiskeyDetails(
                        rs.getString("DISTILLERY"),
                        rs.getInt("AGE"),
                        rs.getString("REGION"),
                        rs.getInt("PRICE")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Query execution error: " + e.getMessage());
        }
        return results;
    }
}
