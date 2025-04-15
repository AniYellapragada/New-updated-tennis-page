package com.tennis;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.stream.Collectors;

public class PrimaryController {
    @FXML
    private ListView<Player> rankingsListView;
    
    @FXML
    private ComboBox<Player> playerComboBox;
    
    @FXML
    private ComboBox<Tournament> tournamentComboBox;
    
    @FXML
    private TextField playerNameField;
    
    @FXML
    private TextField playerAgeField;
    
    @FXML
    private TextField playerUtrField;
    
    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;
    
    @FXML
    private TextArea searchResultArea;
    
    private RankingManager rankingManager;
    
    private PlayerSearch playerSearch;
    
    @FXML
    public void initialize() {
        rankingManager = new RankingManager();
        playerSearch = new PlayerSearch(rankingManager);
        initializeComboBoxes();
    }
    
    @FXML
    private void handleAddPlayer() {
        try {
            String name = playerNameField.getText().trim();
            int age = Integer.parseInt(playerAgeField.getText().trim());
            double utr = Double.parseDouble(playerUtrField.getText().trim());
            
            if (!name.isEmpty() && age > 0 && utr > 0) {
                rankingManager.addPlayer(name, age, utr);
                updateUI();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter valid age and UTR values");
        }
    }
    
    @FXML
    private void handleAddResult() {
        Player selectedPlayer = playerComboBox.getValue();
        Tournament selectedTournament = tournamentComboBox.getValue();
        if (selectedPlayer != null && selectedTournament != null) {
            rankingManager.addTournamentResult(selectedPlayer, selectedTournament);
            updateUI();
        }
    }
    
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            showError("Search Error", "Please enter a search term");
            return;
        }

        // First try to find a player
        Player player = rankingManager.findPlayerByName(searchTerm);
        if (player != null) {
            // Show player profile
            searchResultArea.setText(player.getPlayerProfile());
            return;
        }

        // If no player found, try to find tournament
        List<Player> tournamentWinners = findTournamentWinners(searchTerm);
        if (!tournamentWinners.isEmpty()) {
            // Show tournament winners
            StringBuilder result = new StringBuilder();
            result.append("Tournament: ").append(searchTerm.toUpperCase()).append("\n");
            result.append("Winners:\n");
            tournamentWinners.forEach(p -> 
                result.append(String.format("- %s (Rank: %d, Points: %d)\n", 
                    p.getName(), p.getRank(), p.getPoints()))
            );
            searchResultArea.setText(result.toString());
        } else {
            searchResultArea.setText("No matches found for: " + searchTerm);
        }
    }
    
    private void initializeComboBoxes() {
        tournamentComboBox.setItems(FXCollections.observableArrayList(rankingManager.getTournaments()));
        updateUI();
    }
    
    private void updateUI() {
        rankingsListView.setItems(FXCollections.observableArrayList(rankingManager.getPlayersOrderedByRank()));
        playerComboBox.setItems(FXCollections.observableArrayList(rankingManager.getPlayers()));
    }
    
    private void clearFields() {
        playerNameField.clear();
        playerAgeField.clear();
        playerUtrField.clear();
        searchField.clear();
        searchResultArea.clear();
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private List<Player> findTournamentWinners(String tournamentName) {
        return rankingManager.getPlayers().stream()
            .filter(p -> p.getTournamentsWon().stream()
                .anyMatch(t -> t.getName().toLowerCase().contains(tournamentName)))
            .collect(Collectors.toList());
    }
}
