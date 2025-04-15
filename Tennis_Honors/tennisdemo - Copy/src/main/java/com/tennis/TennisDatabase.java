package com.tennis;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TennisDatabase {
    // Simple data structures to store information
    private List<Player> players;
    private List<String> tournaments;
    private Map<String, Integer> tournamentPoints;

    public TennisDatabase() {
        players = new ArrayList<>();
        tournaments = new ArrayList<>();
        tournamentPoints = new HashMap<>();
        
        // Initialize default tournaments
        initializeTournaments();
    }

    private void initializeTournaments() {
        // Add Grand Slams (2000 points)
        addTournament("Australian Open", 2000);
        addTournament("French Open", 2000);
        addTournament("Wimbledon", 2000);
        addTournament("US Open", 2000);
        
        // Add Masters 1000
        addTournament("Indian Wells", 1000);
        addTournament("Miami Open", 1000);
        addTournament("Monte Carlo", 1000);
        
        // Add ATP 500
        addTournament("Dubai", 500);
        addTournament("Rotterdam", 500);
        
        // Add ATP 250
        addTournament("Adelaide", 250);
        addTournament("Doha", 250);
    }

    private void addTournament(String name, int points) {
        tournaments.add(name);
        tournamentPoints.put(name, points);
    }

    public void addPlayer(String name, int age, double utr) {
        Player newPlayer = new Player(name, age, utr);
        players.add(newPlayer);
        updateRankings();
    }

    public void addTournamentWin(String playerName, String tournamentName) {
        Player player = findPlayer(playerName);
        if (player != null && tournaments.contains(tournamentName)) {
            int points = tournamentPoints.get(tournamentName);
            player.addPoints(points);
            player.addTournamentWin(new Tournament(tournamentName, points));
            updateRankings();
        }
    }

    public List<String> getRankingsList() {
        List<String> rankings = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            rankings.add(String.format("%d. %s - Points: %d", 
                (i + 1), p.getName(), p.getPoints()));
        }
        return rankings;
    }

    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player p : players) {
            names.add(p.getName());
        }
        return names;
    }

    public List<String> getTournamentNames() {
        return new ArrayList<>(tournaments);
    }

    public String search(String searchTerm) {
        StringBuilder result = new StringBuilder();
        searchTerm = searchTerm.toLowerCase();

        // Search for players
        Player player = findPlayer(searchTerm);
        if (player != null) {
            return player.getPlayerProfile();
        }

        // Search for tournaments
        for (String tournament : tournaments) {
            if (tournament.toLowerCase().contains(searchTerm)) {
                result.append("Tournament: ").append(tournament).append("\n");
                result.append("Points: ").append(tournamentPoints.get(tournament)).append("\n");
                result.append("Winners:\n");
                for (Player p : players) {
                    if (p.getTournamentsWon().stream()
                            .anyMatch(t -> t.getName().equals(tournament))) {
                        result.append("- ").append(p.getName()).append("\n");
                    }
                }
                return result.toString();
            }
        }

        return "No matches found for: " + searchTerm;
    }

    private Player findPlayer(String name) {
        for (Player p : players) {
            if (p.getName().toLowerCase().equals(name.toLowerCase())) {
                return p;
            }
        }
        return null;
    }

    private void updateRankings() {
        // Sort players by points (descending order)
        players.sort((p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()));
        
        // Update ranks
        for (int i = 0; i < players.size(); i++) {
            players.get(i).updateRank(i + 1);
        }
    }
}