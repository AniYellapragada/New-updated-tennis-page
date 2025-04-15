package com.tennis;

public class Tournament {
    // Store the tournament information
    private String name;
    private int points;

    // Constructor - create a new tournament
    public Tournament(String name, int points) {
        // Save the tournament details
        this.name = name;
        this.points = points;
    }

    // Get the tournament name
    public String getName() {
        return name;
    }

    // Get the points awarded for winning
    public int getPoints() {
        return points;
    }

    // Create a simple text version of the tournament
    @Override
    public String toString() {
        return name + " (" + points + " points)";
    }

    // Check if two tournaments are the same
    @Override
    public boolean equals(Object obj) {
        // First check if the object is a Tournament
        if (obj instanceof Tournament) {
            // Compare tournament names
            Tournament other = (Tournament) obj;
            return this.name.equals(other.name);
        }
        return false;
    }

    // Generate a unique number for this tournament
    @Override
    public int hashCode() {
        // Use the name to make the number
        //compare numbers for similarity
        return name.hashCode();
    }

    // Create detailed tournament information
    public String getTournamentProfile() {
        String profile = "";
        profile += "Tournament Details\n";
        profile += "=================\n";
        profile += "Name: " + name + "\n";
        profile += "Points: " + points;
        return profile;
    }
}