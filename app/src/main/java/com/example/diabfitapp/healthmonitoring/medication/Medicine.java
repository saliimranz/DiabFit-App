package com.example.diabfitapp.healthmonitoring.medication;

public class Medicine {
    private int id; // Added id field
    private String name;
    private int quantity;
    private int hour;
    private int minute;

    public Medicine(int id, String name, int quantity, int hour, int minute) {
        this.id = id; // Initialize id
        this.name = name;
        this.quantity = quantity;
        this.hour = hour;
        this.minute = minute;
    }

    // Overloaded constructor without id for convenience
    public Medicine(String name, int quantity, int hour, int minute) {
        this.name = name;
        this.quantity = quantity;
        this.hour = hour;
        this.minute = minute;
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

    // Add setter for id if needed
    public void setId(int id) {
        this.id = id;
    }
}
