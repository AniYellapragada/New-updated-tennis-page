package com.tennis;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;  // Add this import

public class PlayerSearch {
    private RankingManager database;
    private TextField searchInput;
    private TextArea resultsArea;
    
    // Constructor
    public PlayerSearch(RankingManager database) {
        if (database == null) {
            throw new IllegalArgumentException("Database cannot be null");
        }
        this.database = database;
        makeSearchComponents();
    }
    
    // Create the search components
    private void makeSearchComponents() {
        // Make the search input box
        searchInput = new TextField();
        searchInput.setPromptText("Enter player or tournament name");
        
        // Make the results display area
        resultsArea = new TextArea();
        resultsArea.setEditable(false);
        resultsArea.setPrefRowCount(10);
        resultsArea.setPrefColumnCount(40);
        resultsArea.setWrapText(true);
    }
    
    // Create and return the search interface
    public VBox getSearchBox() {
        // Create main container
        VBox searchBox = new VBox(10); // 10 pixels spacing
        searchBox.setPadding(new Insets(10));
        
        // Add label
        Label searchLabel = new Label("Search:");
        
        // Create search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> doSearch());
        
        // Add enter key support
        searchInput.setOnAction(event -> doSearch());
        
        // Add everything to the container
        searchBox.getChildren().addAll(searchLabel, searchInput, searchButton, resultsArea);
        return searchBox;
    }
    
    // Do the actual search
    private void doSearch() {
        // Get what user typed
        String searchText = searchInput.getText().trim();
        
        // Check if empty
        if (searchText.isEmpty()) {
            showError("Please enter a name to search");
            return;
        }
        
        // Search for matches
        String results = database.search(searchText);
        
        // Show what we found
        resultsArea.setText(results);
    }
    
    // Show error message
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}