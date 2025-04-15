package com.tennis;

import java.util.ArrayList;
import java.util.List;

public class RankingManager {
    // Lists to store our players and tournaments
    private List<Player> players;
    private List<Tournament> tournaments;

    // Constructor - called when we create a new RankingManager
    public RankingManager() {
        // Create empty lists
        players = new ArrayList<>();
        tournaments = new ArrayList<>();
        // Add default tournaments
        addDefaultTournaments();
    }

    // Add all our tennis tournaments
    private void addDefaultTournaments() {
        // Grand Slams (2000 points each)
        tournaments.add(new Tournament("Australian Open", 2000));
        tournaments.add(new Tournament("French Open", 2000));
        tournaments.add(new Tournament("Wimbledon", 2000));
        tournaments.add(new Tournament("US Open", 2000));
        
        // Masters Tournaments (1000 points each)
        tournaments.add(new Tournament("Indian Wells", 1000));
        tournaments.add(new Tournament("Miami Open", 1000));
        
        // Other Tournaments
        tournaments.add(new Tournament("Rotterdam", 500));
        tournaments.add(new Tournament("Adelaide", 250));
    }

    // Add a new player to the rankings
    public void addPlayer(String name, int age, double utr) {
        // Create new player
        Player newPlayer = new Player(name, age, utr);
        // Add to our list
        players.add(newPlayer);
        // Update everyone's ranking
        updatePlayerRankings();
    }

    // Record a tournament win for a player
    public void addTournamentWin(String playerName, String tournamentName) {
        // Find the player and tournament
        Player player = findPlayer(playerName);
        Tournament tournament = findTournament(tournamentName);
        
        // Make sure we found both
        if (player != null && tournament != null) {
            // Add points to player's total
            player.addPoints(tournament.getPoints());
            // Record the tournament win
            player.addTournamentWin(tournament);
            // Update everyone's ranking
            updatePlayerRankings();
        }
    }

    // Update rankings based on points
    private void updatePlayerRankings() {
        // Sort players by points (highest first)
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                if (players.get(i).getPoints() < players.get(j).getPoints()) {
                    // Swap players if they're in wrong order
                    Player temp = players.get(i);
                    players.set(i, players.get(j));
                    players.set(j, temp);
                }
            }
        }
        
        // Set each player's rank
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setRank(i + 1);
        }
    }

    // Find a player by their name
    public Player findPlayer(String name) {
        // Look through all players
        for (Player p : players) {
            // Compare names ignoring case
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        // Return null if not found
        return null;
    }

    // Find a tournament by its name
    public Tournament findTournament(String name) {
        // Look through all tournaments
        for (Tournament t : tournaments) {
            // Compare names ignoring case
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        // Return null if not found
        return null;
    }

    // Get a list of all players (for ui)
    public List<String> getPlayerList() {
        List<String> playerInfo = new ArrayList<>();
        for (Player p : players) {
            playerInfo.add(p.toString());
        }
        return playerInfo;
    }

    // Get a list of all tournaments (for display)
    public List<String> getTournamentList() {
        List<String> tournamentNames = new ArrayList<>();
        for (Tournament t : tournaments) {
            tournamentNames.add(t.getName());
        }
        return tournamentNames;
    }

    // Search for a player or tournament by name
    public String search(String term) {
        // Search for player
        Player player = findPlayer(term);
        if (player != null) {
            return player.getPlayerProfile();
        }

        // Search for tournament
        Tournament tournament = findTournament(term);
        if (tournament != null) {
            String result = "Tournament: " + tournament.getName() + "\n";
            result += "Points: " + tournament.getPoints() + "\n\n";
            result += "Winners:\n";
            
            boolean hasWinners = false;
            for (Player p : players) {
                if (p.getTournamentsWon().contains(tournament)) {
                    result += "- " + p.getName() + "\n";
                    hasWinners = true;
                }
            }
            
            if (!hasWinners) {
                result += "No winners yet";
            }
            return result;
        }

        return "No results found for: " + term;
    }

    // Get a list of player names
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player p : players) {
            names.add(p.getName());
        }
        return names;
    }

    // Get a list of all players
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    // Get a list of all tournaments
    public List<Tournament> getTournaments() {
        return new ArrayList<>(tournaments);
    }

    // Find a player by their name (using streams)
    public Player findPlayerByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return players.stream()
            .filter(p -> p.getName().equalsIgnoreCase(name.trim()))
            .findFirst()
            .orElse(null);
    }

    // Get a list of players ordered by rank
    public List<Player> getPlayersOrderedByRank() {
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((p1, p2) -> Integer.compare(p1.getRank(), p2.getRank()));
        return sortedPlayers;
    }

    // Add a tournament result for a player
    public void addTournamentResult(Player player, Tournament tournament) {
        if (player == null || tournament == null) {
            throw new IllegalArgumentException("Player and tournament cannot be null");
        }
        player.addPoints(tournament.getPoints());
        player.addTournamentWin(tournament);
        updatePlayerRankings();
    }

    // Get a list of players who have won a specific tournament
    public List<Player> getTournamentWinners(Tournament tournament) {
        List<Player> winners = new ArrayList<>();
        for (Player player : players) {
            if (player.getTournamentsWon().contains(tournament)) {
                winners.add(player);
            }
        }
        return winners;
    }
}