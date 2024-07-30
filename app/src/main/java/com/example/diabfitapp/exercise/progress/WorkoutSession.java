package com.example.diabfitapp.exercise.progress;

import java.util.List;

public class WorkoutSession {
    private int id;
    private String day;
    private int timeSpent;
    private double carbsBurnt;
    private double workoutPercentage;
    private List<String> exerciseNames;
    private List<Integer> completedSets;
    private List<Integer> targetSets;

    public WorkoutSession(int id, String day, int timeSpent, double carbsBurnt, double workoutPercentage, List<String> exerciseNames, List<Integer> completedSets, List<Integer> targetSets) {
        this.id = id;
        this.day = day;
        this.timeSpent = timeSpent;
        this.carbsBurnt = carbsBurnt;
        this.workoutPercentage = workoutPercentage;
        this.exerciseNames = exerciseNames;
        this.completedSets = completedSets;
        this.targetSets = targetSets;
    }

    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public double getCarbsBurnt() {
        return carbsBurnt;
    }

    public double getWorkoutPercentage() {
        return workoutPercentage;
    }

    public List<String> getExerciseNames() {
        return exerciseNames;
    }

    public List<Integer> getCompletedSets() {
        return completedSets;
    }

    public List<Integer> getTargetSets() {
        return targetSets;
    }
}
