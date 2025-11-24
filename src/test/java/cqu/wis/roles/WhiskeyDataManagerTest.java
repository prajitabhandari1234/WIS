/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package cqu.wis.roles;

import org.junit.jupiter.api.BeforeEach;
import cqu.wis.data.WhiskeyData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link WhiskeyDataManager} class.
 * <p>
 * These tests verify the behavior of navigating through whiskey records,
 * setting data, and retrieving the current selection from a list of whiskey
 * details.
 * </p>
 *
 * @author Prajita Bhandari
 *
 */
public class WhiskeyDataManagerTest {

    private WhiskeyDataManager manager;

    /**
     * Sets up a new instance of {@link WhiskeyDataManager} with a stub
     * {@link WhiskeyData} before each test. This avoids actual database
     * interaction.
     */
    @BeforeEach
    public void setup() {
        // We do not actually hit a real database; we just need a non-null WhiskeyData stub.
        // For our tests, we will override using setDetails(...).
        manager = new WhiskeyDataManager(new WhiskeyData());
    }

    /**
     * Verifies that when a single record is set, both first() and getCurrent()
     * return it.
     */
    @Test
    public void testSetDetailsAndFirstSingleRecord() {
        WhiskeyData.WhiskeyDetails d1
                = new WhiskeyData.WhiskeyDetails("Laphroaig", 10, "Islay", 100);
        manager.setDetails(new WhiskeyData.WhiskeyDetails[]{d1});
        var first = manager.first();
        assertNotNull(first, "first() should not be null when one record is loaded");
        assertEquals("Laphroaig", first.distillery());
        // getCurrent() should also return the same single record
        assertEquals(d1, manager.getCurrent());
    }

    /**
     * Ensures that calling next() with no records returns null.
     */
    @Test
    public void testNextWithNoRecordsReturnsNull() {
        manager.setDetails(new WhiskeyData.WhiskeyDetails[]{});
        assertNull(manager.next(), "next() with empty records should return null");
    }

    /**
     * Ensures that calling previous() with no records returns null.
     */
    @Test
    public void testPreviousWithNoRecordsReturnsNull() {
        manager.setDetails(new WhiskeyData.WhiskeyDetails[]{});
        assertNull(manager.previous(), "previous() with empty records should return null");
    }

    /**
     * Verifies that calling next() on a single-record list cycles back to
     * itself.
     */
    @Test
    public void testNextSingleRecordCyclesToItself() {
        WhiskeyData.WhiskeyDetails d1
                = new WhiskeyData.WhiskeyDetails("Oban", 14, "Highland", 120);
        manager.setDetails(new WhiskeyData.WhiskeyDetails[]{d1});
        manager.first(); // position at d1
        var next = manager.next();
        assertNotNull(next, "next() from the only record should not be null (cycles)");
        assertEquals(d1, next, "next() from single record should cycle back to itself");
    }

    /**
     * Verifies that calling previous() on a single-record list cycles back to
     * itself.
     */
    @Test
    public void testPreviousSingleRecordCyclesToItself() {
        WhiskeyData.WhiskeyDetails d1
                = new WhiskeyData.WhiskeyDetails("Tomatin", 12, "Highland", 90);
        manager.setDetails(new WhiskeyData.WhiskeyDetails[]{d1});
        manager.first(); // position at d1
        var prev = manager.previous();
        assertNotNull(prev, "previous() from the only record should not be null (cycles)");
        assertEquals(d1, prev, "previous() from single record should cycle back to itself");
    }

    /**
     * Tests forward navigation through multiple records including wrap-around
     * behavior.
     */
    @Test
    public void testNextThroughMultipleRecordsCyclicBehavior() {
        WhiskeyData.WhiskeyDetails d1
                = new WhiskeyData.WhiskeyDetails("A", 10, "X", 100);
        WhiskeyData.WhiskeyDetails d2
                = new WhiskeyData.WhiskeyDetails("B", 12, "Y", 110);
        WhiskeyData.WhiskeyDetails d3
                = new WhiskeyData.WhiskeyDetails("C", 14, "Z", 120);

        manager.setDetails(new WhiskeyData.WhiskeyDetails[]{d1, d2, d3});
        manager.first(); // at d1
        assertEquals(d2, manager.next(), "next() from first goes to second");
        assertEquals(d3, manager.next(), "next() from second goes to third");
        assertEquals(d1, manager.next(), "next() from last cycles back to first");
    }

    /**
     * Tests backward navigation through multiple records including wrap-around
     * behavior.
     */
    @Test
    public void testPreviousThroughMultipleRecordsCyclicBehavior() {
        WhiskeyData.WhiskeyDetails d1
                = new WhiskeyData.WhiskeyDetails("A", 10, "X", 100);
        WhiskeyData.WhiskeyDetails d2
                = new WhiskeyData.WhiskeyDetails("B", 12, "Y", 110);
        WhiskeyData.WhiskeyDetails d3
                = new WhiskeyData.WhiskeyDetails("C", 14, "Z", 120);

        manager.setDetails(new WhiskeyData.WhiskeyDetails[]{d1, d2, d3});
        manager.first();        // at d1
        manager.next();         // at d2
        manager.next();         // at d3

        // previous() from d3 goes to d2
        assertEquals(d2, manager.previous(), "previous() from third goes to second");
        // previous() from d2 goes to d1
        assertEquals(d1, manager.previous(), "previous() from second goes to first");
        // previous() from d1 cycles back to d3
        assertEquals(d3, manager.previous(), "previous() from first cycles to last");
    }

    /**
     * Ensures getCurrent() returns null when no data is loaded.
     */
    @Test
    public void testGetCurrentWithNoRecordsReturnsNull() {
        manager.setDetails(new WhiskeyData.WhiskeyDetails[]{});
        assertNull(manager.getCurrent(), "getCurrent() with no records should return null");
    }

    /**
     * Verifies getCurrent() returns the first element immediately after
     * setDetails().
     */
    @Test
    public void testGetCurrentImmediatelyAfterSetDetailsReturnsFirst() {
        WhiskeyData.WhiskeyDetails d1
                = new WhiskeyData.WhiskeyDetails("Dufftown", 8, "Speyside", 80);
        WhiskeyData.WhiskeyDetails d2
                = new WhiskeyData.WhiskeyDetails("Glenfiddich", 12, "Speyside", 100);
        manager.setDetails(new WhiskeyData.WhiskeyDetails[]{d1, d2});
        // We have not called first(); getCurrent() should still point to index 0
        assertEquals(d1, manager.getCurrent(),
                "getCurrent() after setDetails should return first element");
    }

    /**
     * Confirms that setting details to null clears all stored records and
     * resets state.
     */
    @Test
    public void testSetDetailsNullClearsRecords() {
        manager.setDetails(null);
        assertNull(manager.getCurrent(), "After setDetails(null), getCurrent() should be null");
        assertNull(manager.next(), "After setDetails(null), next() should be null");
        assertNull(manager.previous(), "After setDetails(null), previous() should be null");
    }
}
