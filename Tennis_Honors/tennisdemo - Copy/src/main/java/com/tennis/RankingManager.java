package com.tennis;

import java.util.ArrayList;
import java.util.List;

public class RankingManager {
    private List<Player> players;
    private List<Tournament> tournaments;

    public RankingManager() {
        players = new ArrayList<>();
        tournaments = new ArrayList<>();
        setupTournaments();
    }

    private void setupTournaments() {
        // Add Grand Slams
        tournaments.add(new Tournament("Australian Open", 2000));
        tournaments.add(new Tournament("French Open", 2000));
        tournaments.add(new Tournament("Wimbledon", 2000));
        tournaments.add(new Tournament("US Open", 2000));
        
        // Add Masters
        tournaments.add(new Tournament("Indian Wells", 1000));
        tournaments.add(new Tournament("Miami Open", 1000));
        
        // Add smaller tournaments
        tournaments.add(new Tournament("Rotterdam", 500));
        tournaments.add(new Tournament("Adelaide", 250));
    }

    public void addPlayer(String name, int age, double utr) {
        Player newPlayer = new Player(name, age, utr);
        players.add(newPlayer);
        updateRankings();
    }

    public void addTournamentWin(String playerName, String tournamentName) {
        Player player = findPlayer(playerName);
        Tournament tournament = findTournament(tournamentName);
        
        if (player != null && tournament != null) {
            player.addPoints(tournament.getPoints());
            player.addTournamentWin(tournament);
            updateRankings();
        }
    }

    private void updateRankings() {
        // Sort players by points
        players.sort((p1, p2) -> p2.getPoints() - p1.getPoints());
        
        // Update ranks
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setRank(i + 1);
        }
    }

    public Player findPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public Tournament findTournament(String name) {
        for (Tournament t : tournaments) {
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }

    public List<String> getPlayerList() {
        List<String> names = new ArrayList<>();
        for (Player p : players) {
            names.add(p.toString());
        }
        return names;
    }

    public List<String> getTournamentList() {
        List<String> names = new ArrayList<>();
        for (Tournament t : tournaments) {
            names.add(t.getName());
        }
        return names;
    }

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

    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player p : players) {
            names.add(p.getName());
        }
        return names;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public List<Tournament> getTournaments() {
        return new ArrayList<>(tournaments);
    }

    public Player findPlayerByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return players.stream()
            .filter(p -> p.getName().equalsIgnoreCase(name.trim()))
            .findFirst()
            .orElse(null);
    }

    public List<Player> getPlayersOrderedByRank() {
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((p1, p2) -> Integer.compare(p1.getRank(), p2.getRank()));
        return sortedPlayers;
    }

    public void addTournamentResult(Player player, Tournament tournament) {
        if (player == null || tournament == null) {
            throw new IllegalArgumentException("Player and tournament cannot be null");
        }
        player.addPoints(tournament.getPoints());
        player.addTournamentWin(tournament);
        updateRankings();
    }
}