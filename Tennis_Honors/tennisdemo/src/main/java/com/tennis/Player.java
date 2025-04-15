package com.tennis;

import java.util.ArrayList;
import java.util.List;

public class Player {
    // Basic instance variables
    private String name;
    private int points;
    private int rank;
    private int age;
    private double utr;
    private List<Tournament> tournamentsWon;

    // Constructor
    public Player(String name, int age, double utr) {
        this.name= name;
        this.age = age;
        this.utr = utr;
        this.points = 0;
        this.rank = 0;
        this.tournamentsWon = new ArrayList<>();
    }

    // Simple getters
    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getRank() {
        return rank;
    }

    public int getAge() {
        return age;
    }

    public double getUtr() {
        return utr;
    }

    public List<Tournament> getTournamentsWon() {
        return tournamentsWon;
    }

    // Simple setters
    public void setRank(int newRank) {
        if (newRank > 0) {
            rank = newRank;
        }
    }

    // Add points to player's total
    public void addPoints(int newPoints) {
        if (newPoints > 0) {
            points = points + newPoints;
        }
    }

    // Add a tournament win
    public void addTournamentWin(Tournament tournament) {
        if (tournament != null && !tournamentsWon.contains(tournament)) {
            tournamentsWon.add(tournament);
        }
    }

    // Create a string representation of the player
    @Override
    public String toString() {
        String playerInfo = name + " (Rank: " + rank + ", Points: " + points + 
                          ", Age: " + age + ", UTR: " + String.format("%.1f", utr) + ")";
        
        // Add tournament information if player has won any
        if (!tournamentsWon.isEmpty()) {
            playerInfo += "\nTournaments Won:";
            for (Tournament t : tournamentsWon) {
                playerInfo += "\n  - " + t.getName();
            }
        }
        
        return playerInfo;
    }

    // Create detailed player profile
    public String getPlayerProfile() {
        String profile = "";
        
        // Add basic information
        profile += "Player Profile\n";
        profile += "==============\n";
        profile += "Name: " + name + "\n";
        profile += "Age: " + age + "\n";
        profile += "UTR: " + String.format("%.1f", utr) + "\n";
        profile += "Current Rank: " + rank + "\n";
        profile += "Total Points: " + points + "\n";
        
        // Add tournament history if any exists
        if (!tournamentsWon.isEmpty()) {
            profile += "\nTournament History\n";
            profile += "=================\n";
            for (Tournament t : tournamentsWon) {
                profile += "- " + t.getName() + " (" + t.getPoints() + " points)\n";
            }
        }
        
        return profile;
    }
}
