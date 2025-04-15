package com.tennis;

public class Tournament {
    private String name;
    private int points;

    public Tournament(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return name + " (" + points + " points)";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tournament) {
            return this.name.equals(((Tournament) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getTournamentProfile() {
        return String.format("Tournament: %s\nPoints: %d", name, points);
    }
}