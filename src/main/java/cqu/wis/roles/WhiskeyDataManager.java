/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

import cqu.wis.data.WhiskeyData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manager class that provides a high-level interface for whiskey data
 * operations and navigation functionality. Acts as a facade over the
 * {@link WhiskeyData} class, providing methods for data retrieval, connection
 * management, and record navigation. Maintains an in-memory cache of whiskey
 * records for efficient navigation and provides search capabilities by region
 * and age range.
 *
 * @author Prajita Bhandari
 *
 */
public class WhiskeyDataManager {

    /**
     * The underlying WhiskeyData object for database access.
     */
    private final WhiskeyData wd;

    /**
     * Cached list of whiskey records loaded in memory.
     */
    private List<WhiskeyData.WhiskeyDetails> records;

    /**
     * Index of the current record in the list.
     */
    private int currentIndex;

    /**
     * Constructs a new WhiskeyDataManager with the specified data source.
     *
     * @param wd The {@link WhiskeyData} instance to use for data operations.
     * Must not be {@code null}.
     * @throws NullPointerException If {@code wd} is {@code null}.
     */
    public WhiskeyDataManager(WhiskeyData wd) {
        if (wd == null) {
            throw new NullPointerException("WhiskeyData cannot be null");
        }
        this.wd = wd;
        this.records = new ArrayList<>();
        this.currentIndex = -1;
    }

    /**
     * Establishes connection to the underlying whiskey data source.
     * <p>
     * Delegates to {@link WhiskeyData#connect()}. Should be called before
     * performing any data operations.
     * </p>
     *
     * @throws SQLException If the underlying {@link WhiskeyData} fails to
     * connect.
     */
    public void connect() throws SQLException {
        wd.connect();
    }

    /**
     * Closes the connection to the underlying whiskey data source.
     * <p>
     * Delegates to {@link WhiskeyData#disconnect()}. Should be called when data
     * operations are complete.
     * </p>
     */
    public void disconnect() {
        wd.disconnect();
    }

    /**
     * Retrieves all malt whiskey records and loads them into memory.
     * <p>
     * After loading, sets the internal cursor to the first record if any
     * records exist.
     * </p>
     *
     * @return The total number of malt whiskey records found and loaded, or 0
     * if none.
     */
    public int findAllMalts() {
        records = wd.getAllMalts();
        currentIndex = (records.isEmpty()) ? -1 : 0;
        return records.size();
    }

    /**
     * Navigates to and returns the first whiskey record in the current
     * collection.
     *
     * @return The first {@link WhiskeyData.WhiskeyDetails} in the collection,
     * or {@code null} if no records are currently loaded.
     */
    public WhiskeyData.WhiskeyDetails first() {
        if (records.isEmpty()) {
            return null;
        }
        currentIndex = 0;
        return records.get(currentIndex);
    }

    /**
     * Navigates to and returns the next whiskey record in the collection,
     * wrapping around cyclically.
     *
     * @return The next {@link WhiskeyData.WhiskeyDetails} in the collection, or
     * {@code null} if no records are loaded.
     */
    public WhiskeyData.WhiskeyDetails next() {
        if (records.isEmpty()) {
            return null;
        }
        currentIndex = (currentIndex + 1) % records.size();
        return records.get(currentIndex);
    }

    /**
     * Navigates to and returns the previous whiskey record in the collection,
     * wrapping around cyclically.
     *
     * @return The previous {@link WhiskeyData.WhiskeyDetails} in the
     * collection, or {@code null} if no records are loaded.
     */
    public WhiskeyData.WhiskeyDetails previous() {
        if (records.isEmpty()) {
            return null;
        }
        currentIndex = (currentIndex - 1 + records.size()) % records.size();
        return records.get(currentIndex);
    }

    /**
     * Returns the record at the current cursor position without changing it.
     *
     * @return The current {@link WhiskeyData.WhiskeyDetails}, or {@code null}
     * if no records are loaded or if the cursor is not initialized.
     */
    public WhiskeyData.WhiskeyDetails getCurrent() {
        if (records.isEmpty() || currentIndex < 0) {
            return null;
        }
        return records.get(currentIndex);
    }

    /**
     * Replaces the in-memory list of records with the provided array and resets
     * the cursor to the first element if any are present.
     *
     * <p>
     * This method is intended for unit testing (setDetails is not part of the
     * normal client API).</p>
     *
     * @param details Array of {@link WhiskeyData.WhiskeyDetails} to load. Can
     * be {@code null} or empty, which will result in an empty records list.
     */
    public void setDetails(WhiskeyData.WhiskeyDetails[] details) {
        if (details == null) {
            records = new ArrayList<>();
            currentIndex = -1;
        } else {
            records = new ArrayList<>(Arrays.asList(details));
            currentIndex = (records.isEmpty()) ? -1 : 0;
        }
    }

    /**
     * Retrieves whiskey records filtered by geographic region, but does NOT set
     * internal state.
     * <p>
     * This method is used directly by controllers that want the raw list of
     * matching records.
     * </p>
     *
     * @param region The geographic region to filter by. Must not be
     * {@code null}.
     * @return A {@link List} of {@link WhiskeyData.WhiskeyDetails} objects from
     * the specified region. Returns an empty list if no malts are found in the
     * region or if an error occurs.
     */
    public List<WhiskeyData.WhiskeyDetails> getMaltsFromRegion(String region) {
        return wd.getMaltsFromRegion(region);
    }

    /**
     * Retrieves whiskey records filtered by age range (inclusive bounds), but
     * does NOT set internal state.
     * <p>
     * This method is used directly by controllers that want the raw list of
     * matching records.
     * </p>
     *
     * @param min The minimum age in years (inclusive). Should be non-negative.
     * @param max The maximum age in years (inclusive). Should be greater than
     * or equal to min.
     * @return A {@link List} of {@link WhiskeyData.WhiskeyDetails} objects with
     * ages between {@code min} and {@code max}. Returns an empty list if no
     * malts are found in the age range or if an error occurs.
     */
    public List<WhiskeyData.WhiskeyDetails> getMaltsInAgeRange(int min, int max) {
        return wd.getMaltsInAgeRange(min, max);
    }
}
