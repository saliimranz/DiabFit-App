package com.example.diabfitapp.exercise.workout;

import java.io.Serializable;

public class Exercise implements Serializable {
    private String id;
    private String name;
    private String bodyPart;
    private String equipment;
    private String gifUrl;
    private String target;
    private String secondaryMuscles;
    private String instructions;
    private int sets;
    private int completedSets;

    public Exercise(String id, String name, String bodyPart, String equipment, String gifUrl, String target, String secondaryMuscles, String instructions, int sets, int completedSets) {
        this.id = id;
        this.name = name;
        this.bodyPart = bodyPart;
        this.equipment = equipment;
        this.gifUrl = gifUrl;
        this.target = target;
        this.secondaryMuscles = secondaryMuscles;
        this.instructions = instructions;
        this.sets = sets;
        this.completedSets = completedSets;
    }

    public int getCompletedSets() {return completedSets;}

    public void setCompletedSets(int completedSets) {this.completedSets = completedSets;}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public String getTarget() {
        return target;
    }

    public String getSecondaryMuscles() {
        return secondaryMuscles;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }
}
