package com.tennis;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class App extends Application {
    // All our UI components
    private ListView<String> rankingsList;
    private TextField nameInput;
    private TextField ageInput;
    private TextField utrInput;
    private ComboBox<String> playerDropdown;
    private ComboBox<String> tournamentDropdown;
    private TextArea searchBox;
    private Button addPlayerButton;
    private Button addTournamentButton;
    private Button searchButton;
    
    // Our database
    private RankingManager database;

    @Override
    public void start(Stage window) {
        // Create our database
        database = new RankingManager();
        
        // Make the main window
        VBox mainBox = new VBox();
        mainBox.setSpacing(10);
        mainBox.setPadding(new Insets(10));
        
        // Add all sections
        mainBox.getChildren().add(makeSearchArea());
        mainBox.getChildren().add(makePlayerArea());
        mainBox.getChildren().add(makeTournamentArea());
        mainBox.getChildren().add(makeRankingsArea());
        
        // Show the window
        Scene scene = new Scene(mainBox, 800, 600);
        window.setTitle("Tennis Rankings");
        window.setScene(scene);
        window.show();
        
        // Update the displays
        updateAllDisplays();
    }

    // Make the search area
    private VBox makeSearchArea() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(5));
        
        // Search input and button
        TextField searchInput = new TextField();
        searchInput.setPromptText("Type player or tournament name");
        
        searchButton = new Button("Search");
        searchBox = new TextArea();
        searchBox.setEditable(false);
        searchBox.setPrefRowCount(3);
        
        // What happens when search button is clicked
        searchButton.setOnAction(event -> {
            String searchText = searchInput.getText();
            if (searchText.isEmpty()) {
                showPopup("Error", "Please type something to search");
            } else {
                String results = database.search(searchText);
                searchBox.setText(results);
            }
        });
        
        box.getChildren().addAll(searchInput, searchButton, searchBox);
        return box;
    }

    // Make the add player area
    private VBox makePlayerArea() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(5));
        
        // Text fields for player info
        nameInput = new TextField();
        nameInput.setPromptText("Player Name");
        
        ageInput = new TextField();
        ageInput.setPromptText("Player Age");
        
        utrInput = new TextField();
        utrInput.setPromptText("Player UTR");
        
        // Add player button
        addPlayerButton = new Button("Add New Player");
        addPlayerButton.setOnAction(event -> addNewPlayer());
        
        box.getChildren().addAll(nameInput, ageInput, utrInput, addPlayerButton);
        return box;
    }

    // Make the tournament area
    private VBox makeTournamentArea() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(5));
        
        // Dropdowns for selection
        playerDropdown = new ComboBox<>();
        playerDropdown.setPromptText("Pick a Player");
        
        tournamentDropdown = new ComboBox<>();
        tournamentDropdown.setPromptText("Pick a Tournament");
        
        // Add tournament button
        addTournamentButton = new Button("Add Tournament Win");
        addTournamentButton.setOnAction(event -> addTournamentWin());
        
        box.getChildren().addAll(playerDropdown, tournamentDropdown, addTournamentButton);
        return box;
    }

    // Make the rankings display area
    private VBox makeRankingsArea() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(5));
        
        Label title = new Label("Player Rankings");
        rankingsList = new ListView<>();
        rankingsList.setPrefHeight(200);
        
        box.getChildren().addAll(title, rankingsList);
        return box;
    }

    // Add a new player
    private void addNewPlayer() {
        try {
            // Get the input values
            String name = nameInput.getText();
            String ageText = ageInput.getText();
            String utrText = utrInput.getText();
            
            // Check if anything is empty
            if (name.isEmpty() || ageText.isEmpty() || utrText.isEmpty()) {
                showPopup("Error", "Please fill in all the fields");
                return;
            }
            
            // Convert text to numbers
            int age = Integer.parseInt(ageText);
            double utr = Double.parseDouble(utrText);
            
            // Check if numbers are valid
            if (age <= 0 || utr <= 0) {
                showPopup("Error", "Age and UTR must be positive numbers");
                return;
            }
            
            // Add the player
            database.addPlayer(name, age, utr);
            
            // Update everything and clear inputs
            updateAllDisplays();
            clearInputs();
            showPopup("Success", "Added new player: " + name);
            
        } catch (NumberFormatException e) {
            showPopup("Error", "Please enter valid numbers for age and UTR");
        }
    }

    // Add a tournament win
    private void addTournamentWin() {
        String playerName = playerDropdown.getValue();
        String tournamentName = tournamentDropdown.getValue();
        
        if (playerName == null || tournamentName == null) {
            showPopup("Error", "Please select both a player and tournament");
            return;
        }
        
        try {
            // Simply call addTournamentWin with the names
            database.addTournamentWin(playerName, tournamentName);
            updateAllDisplays();
            showPopup("Success", "Added tournament win for " + playerName);
        } catch (Exception e) {
            showPopup("Error", "Couldn't add tournament win: " + e.getMessage());
        }
    }

    // Update all displays
    private void updateAllDisplays() {
        // Update rankings
        rankingsList.setItems(FXCollections.observableArrayList(
            database.getPlayerList()  // Use getPlayerList instead of getPlayersOrderedByRank
        ));
        
        // Update player dropdown
        playerDropdown.setItems(FXCollections.observableArrayList(
            database.getPlayerNames()  // Use getPlayerNames instead of getPlayers
        ));
        
        // Update tournament dropdown
        tournamentDropdown.setItems(FXCollections.observableArrayList(
            database.getTournamentList()  // Use getTournamentList instead of getTournaments
        ));
    }

    // Clear input fields
    private void clearInputs() {
        nameInput.clear();
        ageInput.clear();
        utrInput.clear();
    }

    // Show a popup message
    private void showPopup(String title, String message) {
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle(title);
        popup.setContentText(message);
        popup.showAndWait();
    }

    // Start the program
    public static void main(String[] args) {
        launch(args);
    }
}