package com.tennis;

import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class PlayerSearch {
    private final RankingManager rankingManager;

    public PlayerSearch(RankingManager rankingManager) {
        this.rankingManager = rankingManager;
    }

    public void searchAndDisplayPlayer() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Player Search");
        dialog.setHeaderText("Enter player name to search:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::displayPlayerProfile);
    }

    private void displayPlayerProfile(String playerName) {
        Player player = rankingManager.findPlayerByName(playerName);
        
        if (player != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Player Profile");
            alert.setHeaderText(null);
            
            TextArea textArea = new TextArea(player.getPlayerProfile());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefRowCount(15);
            textArea.setPrefColumnCount(40);
            
            GridPane content = new GridPane();
            content.setMaxWidth(Double.MAX_VALUE);
            content.add(textArea, 0, 0);
            
            alert.getDialogPane().setContent(content);
            alert.getDialogPane().setExpandableContent(null);
            
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Player Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No player found with name: " + playerName);
            alert.showAndWait();
        }
    }
}