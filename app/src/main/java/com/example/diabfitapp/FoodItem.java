package com.example.diabfitapp;

public class FoodItem {
    private String name;
    private int glycemicIndex;
    private int carbsPer100g;
    private int sizeOfServing;

    public FoodItem(String name, int glycemicIndex, int carbsPer100g, int sizeOfServing) {
        this.name = name;
        this.glycemicIndex = glycemicIndex;
        this.carbsPer100g = carbsPer100g;
        this.sizeOfServing = sizeOfServing;
    }

    public String getName() {
        return name;
    }

    public int getGlycemicIndex() {
        return glycemicIndex;
    }

    public int getCarbsPer100g() {
        return carbsPer100g;
    }

    public int getSizeOfServing() {
        return sizeOfServing;
    }
}
