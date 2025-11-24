/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

import cqu.wis.data.WhiskeyData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author acer
 */
public class WhiskeyDataManager {
    private final WhiskeyData wd;
    private List<WhiskeyData.WhiskeyDetails> records;
    private int currentIndex;

    public WhiskeyDataManager(WhiskeyData wd) {
        this.wd = wd;
        this.records = new ArrayList<>();
        this.currentIndex = -1;
    }

    public void connect() {
        wd.connect();
    }

    public void disconnect() {
        wd.disconnect();
    }

    public int findAllMalts() {
        records = wd.getAllMalts();
        currentIndex = (records.isEmpty()) ? -1 : 0;
        return records.size();
    }

    public WhiskeyData.WhiskeyDetails first() {
        if (records.isEmpty()) return null;
        currentIndex = 0;
        return records.get(currentIndex);
    }

    public WhiskeyData.WhiskeyDetails next() {
        if (records.isEmpty()) return null;
        currentIndex = (currentIndex + 1) % records.size();
        return records.get(currentIndex);
    }

    public WhiskeyData.WhiskeyDetails previous() {
        if (records.isEmpty()) return null;
        currentIndex = (currentIndex - 1 + records.size()) % records.size();
        return records.get(currentIndex);
    }

    public void setDetails(WhiskeyData.WhiskeyDetails[] details) {
        records = new ArrayList<>(Arrays.asList(details));
        currentIndex = (records.isEmpty()) ? -1 : 0;
    }
}
