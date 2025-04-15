package com.tennis;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Player {
    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty points = new SimpleIntegerProperty();
    private final IntegerProperty rank = new SimpleIntegerProperty();
    private final IntegerProperty age = new SimpleIntegerProperty();
    private final DoubleProperty utr = new SimpleDoubleProperty();
    private final ListProperty<Tournament> tournamentsWon = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Player(String name, int age, double utr) {
        this.name.set(name);
        this.age.set(age);
        this.utr.set(utr);
        this.points.set(0);
        this.rank.set(0);
    }

    // Name property methods
    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    // Points property methods
    public IntegerProperty pointsProperty() {
        return points;
    }

    public int getPoints() {
        return points.get();
    }

    public void setPoints(int points) {
        if (points >= 0) {
            this.points.set(points);
        }
    }

    public void addPoints(int newPoints) {
        if (newPoints >= 0) {
            points.set(points.get() + newPoints);
        }
    }

    // Rank property methods - read-only since it's managed by RankingManager
    public IntegerProperty rankProperty() {
        return rank;
    }

    public int getRank() {
        return rank.get();
    }

    public void setRank(int rank) {
        if (rank > 0) {
            this.rank.set(rank);
        }
    }
    // Package-private method for RankingManager to update ranks
    void updateRank(int newRank) {
        if (newRank > 0) {
            rank.set(newRank);
        }
    }

    // Age property methods
    public IntegerProperty ageProperty() {
        return age;
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    // UTR property methods
    public DoubleProperty utrProperty() {
        return utr;
    }

    public double getUtr() {
        return utr.get();
    }

    public void setUtr(double utr) {
        this.utr.set(utr);
    }

    // Tournaments won property methods
    public ObservableList<Tournament> getTournamentsWon() {
        return tournamentsWon.get();
    }

    public void addTournamentWin(Tournament tournament) {
        if (tournament != null && !tournamentsWon.get().contains(tournament)) {
            tournamentsWon.get().add(tournament);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (Rank: %d, Points: %d, Age: %d, UTR: %.1f)", 
            getName(), getRank(), getPoints(), getAge(), getUtr()));
        
        if (!tournamentsWon.get().isEmpty()) {
            sb.append("\nTournaments Won: ");
            tournamentsWon.get().forEach(t -> 
                sb.append("\n  - ").append(t.getName())
            );
        }
        return sb.toString();
    }

    public String getPlayerProfile() {
        // Make it simpler and more readable
        String profile = "";
        profile += "Player Profile\n";
        profile += "==============\n";
        profile += "Name: " + getName() + "\n";
        profile += "Age: " + getAge() + "\n";
        profile += "UTR: " + String.format("%.1f", getUtr()) + "\n";
        profile += "Current Rank: " + getRank() + "\n";
        profile += "Total Points: " + getPoints() + "\n";
        
        // Add tournament history if any
        if (!tournamentsWon.get().isEmpty()) {
            profile += "\nTournament History\n";
            profile += "=================\n";
            for (Tournament t : tournamentsWon.get()) {
                profile += "- " + t.getName() + " (" + t.getPoints() + " points)\n";
            }
        }
        
        return profile;
    }
}
