package com.example.diabfitapp.healthmonitoring.sugerlog;

public class SugarLog {
    private int id;
    private int sugarLevel;
    private String type;
    private String time;

    public SugarLog(int sugarLevel, String type, String time) {
        this.sugarLevel = sugarLevel;
        this.type = type;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(int sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
