package com.example.diabfitapp.nutrition.food;

public class FoodEatenItem {
    private String name;
    private int carbsPer100g;
    private int servings;
    private int glycemicIndex;
    private int carbsIntake;
    private int servingSize;

    public FoodEatenItem(String name, int carbsPer100g, int servings, int glycemicIndex, int carbsIntake, int servingSize) {
        this.name = name;
        this.carbsPer100g = carbsPer100g;
        this.servings = servings;
        this.glycemicIndex = glycemicIndex;
        this.carbsIntake = carbsIntake;
        this.servingSize = servingSize;
    }

    public String getName() {
        return name;
    }

    public int getCarbsPer100g() {
        return carbsPer100g;
    }

    public int getServingsSize() {
        return servingSize;
    }

    public int getServings() {
        return servings*servingSize;
    }

    public int getGlycemicIndex() {
        return glycemicIndex;
    }

    public int getCarbsIntake() {
        return carbsIntake;
    }
}
