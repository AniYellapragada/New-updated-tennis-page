package com.tennis;

import java.util.ArrayList;
import java.util.List;

public class TennisDatabase {
    // Lists to store our data
    private List<Player> playerList;
    private List<Tournament> tournamentList;

    // Constructor - runs when we create a new database
    public TennisDatabase() {
        // Create empty lists
        playerList = new ArrayList<>();
        tournamentList = new ArrayList<>();
        
        // Add all our tournaments
        setupTournaments();
    }

    // Add all the tennis tournaments we want to track
    private void setupTournaments() {
        // Big tournaments - Grand Slams (2000 points)
        addNewTournament("Australian Open", 2000);
        addNewTournament("French Open", 2000);
        addNewTournament("Wimbledon", 2000);
        addNewTournament("US Open", 2000);
        
        // Medium tournaments - Masters (1000 points)
        addNewTournament("Indian Wells", 1000);
        addNewTournament("Miami Open", 1000);
        addNewTournament("Monte Carlo", 1000);
        
        // Smaller tournaments
        addNewTournament("Dubai", 500);
        addNewTournament("Rotterdam", 500);
        addNewTournament("Adelaide", 250);
        addNewTournament("Doha", 250);
    }

    // Helper method to add a new tournament
    private void addNewTournament(String name, int points) {
        Tournament newTournament = new Tournament(name, points);
        tournamentList.add(newTournament);
    }

    // Add a new player to our database
    public void addPlayer(String name, int age, double utr) {
        // Create the new player
        Player newPlayer = new Player(name, age, utr);
        // Add them to our list
        playerList.add(newPlayer);
        // Update everyone's rankings
        updatePlayerRankings();
    }

    // Record a tournament win for a player
    public void addTournamentWin(String playerName, String tournamentName) {
        // Find the player and tournament
        Player player = findPlayerByName(playerName);
        Tournament tournament = findTournamentByName(tournamentName);
        
        // Make sure we found both
        if (player != null && tournament != null) {
            // Add points to player's score
            player.addPoints(tournament.getPoints());
            // Record that they won this tournament
            player.addTournamentWin(tournament);
            // Update everyone's rankings
            updatePlayerRankings();
        }
    }

    // Get a list of all rankings to display
    public List<String> getRankingsList() {
        List<String> rankings = new ArrayList<>();
        
        // Go through each player
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            // Add their ranking info to the list
            String playerInfo = (i + 1) + ". " + player.getName() +
            " - Points: " + player.getPoints();
            rankings.add(playerInfo);
        }
        
        return rankings;
    }

    // Get a list of all player names
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player player : playerList) {
            names.add(player.getName());
        }
        return names;
    }

    // Get a list of all tournament names
    public List<String> getTournamentNames() {
        List<String> names = new ArrayList<>();
        for (Tournament tournament : tournamentList) {
            names.add(tournament.getName());
        }
        return names;
    }

    // Search for a player or tournament
    public String search(String searchTerm) {
        // Convert to lowercase to make search easier
        String searchLower = searchTerm.toLowerCase();
        
        // First try to find a player
        Player player = findPlayerByName(searchLower);
        if (player != null) {
            return player.getPlayerProfile();
        }

        // If no player found, try to find tournament
        Tournament tournament = findTournamentByName(searchLower);
        if (tournament != null) {
            // Create the tournament info
            String result = "";
            result += "Tournament: " + tournament.getName() + "\n";
            result += "Points: " + tournament.getPoints() + "\n";
            result += "Winners:\n";
            
            // Add list of winners
            boolean foundWinner = false;
            for (Player p : playerList) {
                if (p.getTournamentsWon().contains(tournament)) {
                    result += "- " + p.getName() + "\n";
                    foundWinner = true;
                }
            }
            
            if (!foundWinner) {
                result += "No winners yet\n";
            }
            
            return result;
        }

        // If nothing found
        return "No matches found for: " + searchTerm;
    }

    // Helper method to find a player by name
    private Player findPlayerByName(String name) {
        for (Player player : playerList) {
            if (player.getName().toLowerCase().equals(name.toLowerCase())) {
                return player;
            }
        }
        return null;
    }

    // Helper method to find a tournament by name
    private Tournament findTournamentByName(String name) {
        for (Tournament tournament : tournamentList) {
            if (tournament.getName().toLowerCase().equals(name.toLowerCase())) {
                return tournament;
            }
        }
        return null;
    }

    // Update all player rankings based on points
    private void updatePlayerRankings() {
        // First sort players by points (highest to lowest)
        for (int i = 0; i < playerList.size(); i++) {
            for (int j = i + 1; j < playerList.size(); j++) {
                if (playerList.get(i).getPoints() < playerList.get(j).getPoints()) {
                    // Swap players if they're in wrong order
                    Player temp = playerList.get(i);
                    playerList.set(i, playerList.get(j));
                    playerList.set(j, temp);
                }
            }
        }
        
        // Then update each player's rank
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setRank(i + 1);
        }
    }
}