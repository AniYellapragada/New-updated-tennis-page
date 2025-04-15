package com.tennis;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import java.util.ArrayList;

public class App extends Application {
    // Database manager
    private RankingManager database;
    
    // UI Components for Add Player tab
    private TextField nameInput;
    private TextField ageInput;
    private TextField utrInput;
    
    // UI Components for Tournament tab
    private ComboBox<String> playerDropdown;
    private ComboBox<String> tournamentDropdown;
    
    // UI Components for Search tab
    private TextField searchInput;
    private TextArea searchResults;
    
    // Rankings display
    private ListView<String> rankingsList;

    public App() {
        try {
            // Initialize database first
            database = new RankingManager();
            
            // Initialize UI components
            nameInput = new TextField();
            ageInput = new TextField();
            utrInput = new TextField();
            playerDropdown = new ComboBox<>();
            tournamentDropdown = new ComboBox<>();
            searchInput = new TextField();
            searchResults = new TextArea();
            rankingsList = new ListView<>();
            
            // Verify initialization
            checkInitialization();
        } catch (Exception e) {
            System.err.println("Failed to initialize application: " + e.getMessage());
        }
    }

    private void checkInitialization() {
        if (nameInput == null || ageInput == null || utrInput == null ||
            playerDropdown == null || tournamentDropdown == null ||
            searchInput == null || searchResults == null || rankingsList == null) {
            throw new IllegalStateException("UI components not properly initialized");
        }
    }

    @Override
    public void start(Stage window) {
        checkInitialization();
        setupEventHandlers();  // Add this line
        
        // Initialize database
        database = new RankingManager();
        
        // Create tab pane
        TabPane tabPane = new TabPane();
        
        // Create tabs
        Tab searchTab = new Tab("Search", createSearchTab());
        Tab addPlayerTab = new Tab("Add Player", createAddPlayerTab());
        Tab tournamentTab = new Tab("Tournament Results", createTournamentTab());
        Tab rankingsTab = new Tab("Rankings", createRankingsTab());
        
        // Don't allow tabs to be closed
        searchTab.setClosable(false);
        addPlayerTab.setClosable(false);
        tournamentTab.setClosable(false);
        rankingsTab.setClosable(false);
        
        // Add tabs to pane
        tabPane.getTabs().addAll(searchTab, addPlayerTab, tournamentTab, rankingsTab);
        
        // Set minimum window size
        window.setMinWidth(600);
        window.setMinHeight(400);
        
        // Create scene
        Scene scene = new Scene(tabPane, 800, 600);
        applyStyles(scene);
        
        // Set up window
        window.setTitle("Tennis Rankings Manager");
        window.setScene(scene);
        
        // Add window close handler
        window.setOnCloseRequest(event -> {
            // Clean up resources if needed
            database = null;
            System.gc();
        });
        
        window.show();
        
        // Initial update
        updateAllDisplays();
    }

    private VBox createBaseTab(String title) {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.TOP_CENTER);
        // Center align content
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(0, 0, 10, 0));
        
        box.getChildren().add(titleLabel);
        return box;
    }

    private VBox createSearchTab() {
        VBox box = createBaseTab("Search Players and Tournaments");
        
        searchInput = new TextField();
        searchInput.setPromptText("Enter player or tournament name");
        
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> performSearch());
        
        searchResults = new TextArea();
        searchResults.setEditable(false);
        searchResults.setPrefRowCount(20);
        
        box.getChildren().addAll(searchInput, searchButton, searchResults);
        return box;
    }

    private VBox createAddPlayerTab() {
        VBox box = createBaseTab("Add New Player");
        
        nameInput = new TextField();
        nameInput.setPromptText("Player Name");
        
        ageInput = new TextField();
        ageInput.setPromptText("Age");
        
        utrInput = new TextField();
        utrInput.setPromptText("UTR Rating");
        
        Button addButton = new Button("Add Player");
        addButton.setOnAction(e -> addNewPlayer());
        
        box.getChildren().addAll(nameInput, ageInput, utrInput, addButton);
        return box;
    }

    private VBox createTournamentTab() {
        VBox box = createBaseTab("Record Tournament Results");
        
        playerDropdown = new ComboBox<>();
        playerDropdown.setPromptText("Select Player");
        
        tournamentDropdown = new ComboBox<>();
        tournamentDropdown.setPromptText("Select Tournament");
        
        Button addButton = new Button("Record Win");
        addButton.setOnAction(e -> addTournamentWin());
        
        box.getChildren().addAll(playerDropdown, tournamentDropdown, addButton);
        return box;
    }

    private VBox createRankingsTab() {
        VBox box = createBaseTab("Current Rankings");
        
        rankingsList = new ListView<>();
        rankingsList.setPrefHeight(400);
        
        box.getChildren().add(rankingsList);
        return box;
    }

    // Add a new player
    private void addNewPlayer() {
        try {
            // Clear previous error styles
            nameInput.setStyle("");
            ageInput.setStyle("");
            utrInput.setStyle("");
            
            String name = nameInput.getText().trim();
            String ageText = ageInput.getText().trim();
            String utrText = utrInput.getText().trim();
            
            // Validate name
            if (name.isEmpty() || !name.matches("[a-zA-Z\\s]+")) {
                setErrorStyle(nameInput);
                showPopup("Error", "Please enter a valid name (letters only)");
                return;
            }
            
            // Validate age and UTR
            if (ageText.isEmpty()) {
                setErrorStyle(ageInput);
                showPopup("Error", "Please enter an age");
                return;
            }
            if (utrText.isEmpty()) {
                setErrorStyle(utrInput);
                showPopup("Error", "Please enter a UTR rating");
                return;
            }
            
            // Parse numbers
            int age = Integer.parseInt(ageText);
            double utr = Double.parseDouble(utrText);
            
            // Validate ranges
            if (age <= 0 || age > 100) {
                showPopup("Error", "Age must be between 1 and 100");
                return;
            }
            if (utr <= 0 || utr > 16.5) {
                showPopup("Error", "UTR must be between 1 and 16.5");
                return;
            }
            
            database.addPlayer(name, age, utr);
            updateAllDisplays();
            clearInputs();
            showPopup("Success", "Added new player: " + name);
            
        } catch (NumberFormatException e) {
            showPopup("Error", "Please enter valid numbers for age and UTR");
        } catch (Exception e) {
            showPopup("Error", "Failed to add player: " + e.getMessage());
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
        if (database == null) {
            showPopup("Error", "Database not initialized");
            return;
        }

        try {
            // Update rankings list
            List<String> rankings = database.getPlayerList();
            rankingsList.setItems(FXCollections.observableArrayList(
                rankings.isEmpty() ? List.of("No players yet") : rankings
            ));
            
            // Update player dropdown
            List<String> players = database.getPlayerNames();
            playerDropdown.setItems(FXCollections.observableArrayList(
                players.isEmpty() ? List.of("No players available") : players
            ));
            
            // Update tournament dropdown
            List<String> tournaments = database.getTournamentList();
            tournamentDropdown.setItems(FXCollections.observableArrayList(tournaments));
            
        } catch (Exception e) {
            showPopup("Error", "Failed to update displays: " + e.getMessage());
        }
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

    // Perform search
    private void performSearch() {
        try {
            String searchText = searchInput.getText().trim();
            
            // Validate search input
            if (searchText.isEmpty()) {
                showPopup("Error", "Please enter a name to search");
                return;
            }
            
            // Get search results
            String results = database.search(searchText);
            
            // Update results area
            if (results != null && !results.isEmpty()) {
                searchResults.setText(results);
            } else {
                searchResults.setText("No results found for: " + searchText);
            }
            
        } catch (Exception e) {
            showPopup("Error", "Search failed: " + e.getMessage());
        }
    }

    // Apply styles to the scene
    private void applyStyles(Scene scene) {
        try {
            String cssPath = "/styles.css";
            if (getClass().getResource(cssPath) != null) {
                scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            } else {
                System.err.println("Warning: styles.css not found");
            }
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());
        }
    }

    private void setupEventHandlers() {
        // Search on Enter key
        searchInput.setOnAction(e -> performSearch());
        
        // Add player on Enter key in last field
        utrInput.setOnAction(e -> addNewPlayer());
        
        // Clear error style on focus
        nameInput.setOnMouseClicked(e -> nameInput.setStyle(""));
        ageInput.setOnMouseClicked(e -> ageInput.setStyle(""));
        utrInput.setOnMouseClicked(e -> utrInput.setStyle(""));
    }

    private void setErrorStyle(TextField field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }

    // Start the program
    public static void main(String[] args) {
        launch(args);
    }
}