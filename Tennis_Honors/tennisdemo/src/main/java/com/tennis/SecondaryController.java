package com.tennis;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SecondaryController {
    @FXML
    private TableView<Player> playerDetailsTable;
    
    @FXML
    private TableColumn<Player, String> nameColumn;
    
    @FXML
    private TableColumn<Player, Integer> pointsColumn;
    
    @FXML
    private TableColumn<Player, Integer> rankColumn;
    
    private RankingManager rankingManager;
    private ObservableList<Player> players;
    
    @FXML
    public void initialize() {
        try {
            rankingManager = new RankingManager();
            players = FXCollections.observableArrayList();
            setupTable();
            setupSorting();
        } catch (Exception e) {
            showError("Initialization Error", "Failed to initialize: " + e.getMessage());
        }
    }
    
    private void setupTable() {
        try {
            // Use PropertyValueFactory for simpler binding
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
            rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
            
            // Set up column sorting
            nameColumn.setSortType(TableColumn.SortType.ASCENDING);
            pointsColumn.setSortType(TableColumn.SortType.DESCENDING);
            rankColumn.setSortType(TableColumn.SortType.ASCENDING);
            
            playerDetailsTable.setItems(players);
            updateTableData();
        } catch (Exception e) {
            showError("Table Setup Error", "Failed to setup table: " + e.getMessage());
        }
    }
    
    private void setupSorting() {
        // Default sort by rank
        playerDetailsTable.getSortOrder().add(rankColumn);
    }
    
    @FXML
    private void handleRefresh() {
        try {
            updateTableData();
        } catch (Exception e) {
            showError("Refresh Error", "Failed to refresh data: " + e.getMessage());
        }
    }
    
    private void updateTableData() {
        try {
            players.clear();
            if (rankingManager != null && rankingManager.getPlayers() != null) {
                players.addAll(rankingManager.getPlayers());
                playerDetailsTable.sort();
            }
        } catch (Exception e) {
            showError("Update Error", "Failed to update table data: " + e.getMessage());
        }
    }

    @FXML
    private void switchToPrimary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/primary.fxml"));
            Parent root = loader.load();
            Scene scene = playerDetailsTable.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            showError("Navigation Error", "Failed to switch to primary view: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}