package com.example.diabfitapp.healthmonitoring.medication;

public class Medicine {
    private int id;
    private String name;
    private int quantity;
    private int hour;
    private int minute;
    private boolean locked;
    private long eatenDate;

    public Medicine(int id, String name, int quantity, int hour, int minute, boolean locked, long eatenDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.hour = hour;
        this.minute = minute;
        this.locked = locked;
        this.eatenDate = eatenDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isLocked() {
        return locked;
    }

    public long getEatenDate() {
        return eatenDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setEatenDate(long eatenDate) {
        this.eatenDate = eatenDate;
    }
}
