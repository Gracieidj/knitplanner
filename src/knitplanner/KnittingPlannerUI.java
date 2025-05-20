package knitplanner;

// JavaFX UI components and layout imports
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

// Import custom model class
import models.KnittingProject;

import java.util.List;

public class KnittingPlannerUI extends Application {

    // Core components for project suggestions and weather lookup
    private final ProjectSuggester suggester = new ProjectSuggester();
    private final WeatherService weatherService = new WeatherService();

    @Override
    public void start(Stage primaryStage) {
        // Main layout container with spacing and padding
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getStyleClass().add("root");

        // Create the menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("menu-bar");

        // Create the "File" menu
        Menu fileMenu = new Menu("File ✚ ✚ ✚ ");

        // Menu items
        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem quitItem = new MenuItem("Quit");

        // Add menu items to the File menu
        fileMenu.getItems().addAll(newItem, openItem, new SeparatorMenuItem(), quitItem);
        menuBar.getMenus().add(fileMenu);

        // Title label
        Label title = new Label("Knitting Planner By Gracie Idonije");
        title.getStyleClass().add("title-label");

        // Container for interactive controls
        FlowPane controls = new FlowPane(10, 10);
        controls.getStyleClass().add("controls-box");
        controls.setPadding(new Insets(5));

        // ComboBox for yarn type selection
        ComboBox<String> yarnComboBox = new ComboBox<>();
        yarnComboBox.setPromptText("Yarn Type");
        yarnComboBox.getItems().addAll("Wool", "Cotton", "Acrylic");
        yarnComboBox.getStyleClass().add("combo-box");

        // ComboBox for skill level selection
        ComboBox<String> skillComboBox = new ComboBox<>();
        skillComboBox.setPromptText("Skill Level");
        skillComboBox.getItems().addAll("Beginner", "Intermediate", "Advanced");
        skillComboBox.getStyleClass().add("combo-box");

        // ComboBox for aesthetic selection
        ComboBox<String> aestheticComboBox = new ComboBox<>();
        aestheticComboBox.setPromptText("Aesthetic");
        aestheticComboBox.getItems().addAll("Modern", "Classic", "Boho");
        aestheticComboBox.getStyleClass().add("combo-box");

        // TextField for entering the city name
        TextField cityField = new TextField();
        cityField.setPromptText("Enter city");
        cityField.getStyleClass().add("text-field");
        cityField.setPrefWidth(160);

        // Buttons for various actions
        Button fetchWeatherBtn = new Button("Fetch Weather");
        fetchWeatherBtn.getStyleClass().addAll("styled-button", "fetch-button");

        Button findButton = new Button("Find Projects");
        findButton.getStyleClass().addAll("styled-button", "find-button");

        Button addProjectBtn = new Button("Add Project");
        addProjectBtn.getStyleClass().addAll("styled-button", "add-project-button");

        // Add all controls to the FlowPane
        controls.getChildren().addAll(
            yarnComboBox, skillComboBox, aestheticComboBox,
            cityField, fetchWeatherBtn, findButton, addProjectBtn
        );

        // ListView to display suggested knitting projects
        ListView<KnittingProject> projectListView = new ListView<>();
        projectListView.getStyleClass().add("project-list-view");

        // Initially show all available projects (no filters applied)
        projectListView.getItems().setAll(suggester.suggestProjects(null, null, null, null));

        // Define actions for menu items

        // Reset all filters and show all projects
        newItem.setOnAction(e -> {
            yarnComboBox.setValue(null);
            skillComboBox.setValue(null);
            aestheticComboBox.setValue(null);
            cityField.clear();
            projectListView.getItems().setAll(suggester.suggestProjects(null, null, null, null));
            showAlert("Filters cleared and project list reset.");
        });

        // Placeholder for open functionality
        openItem.setOnAction(e -> showAlert("Open functionality not yet implemented."));

        // Quit the application
        quitItem.setOnAction(e -> primaryStage.close());

        // Handle "Find Projects" button click to filter based on combo boxes
        findButton.setOnAction(e -> {
            String yarn = yarnComboBox.getValue();
            String skill = skillComboBox.getValue();
            String aesthetic = aestheticComboBox.getValue();
            List<KnittingProject> filteredProjects = suggester.suggestProjects(skill, yarn, aesthetic, null);
            projectListView.getItems().setAll(filteredProjects);
        });

        // Fetch current weather for the entered city and filter projects accordingly
        fetchWeatherBtn.setOnAction(e -> {
            String city = cityField.getText();
            if (city == null || city.isBlank()) {
                showAlert("Please enter a city name to fetch weather.");
                return;
            }

            // Get weather category and show a message
            String weatherCategory = weatherService.getCurrentWeatherCategory(city);
            if ("All".equalsIgnoreCase(weatherCategory)) {
                showAlert("Could not fetch weather data or unknown weather, showing all projects.");
            } else {
                showAlert("Weather in " + city + ": " + weatherCategory);
            }

            // Filter projects based on weather and user selections
            String yarn = yarnComboBox.getValue();
            String skill = skillComboBox.getValue();
            String aesthetic = aestheticComboBox.getValue();

            List<KnittingProject> filteredProjects = suggester.suggestProjects(skill, yarn, aesthetic, weatherCategory);
            projectListView.getItems().setAll(filteredProjects);
        });

        // Show a dialog to add a new project
        addProjectBtn.setOnAction(e -> {
            KnittingProject newProj = ProjectEditor.showDialog(null);
            if (newProj != null) {
                suggester.addProject(newProj);
                projectListView.getItems().setAll(suggester.suggestProjects(null, null, null, null));
            }
        });

        // Add all UI elements to the main layout
        root.getChildren().addAll(menuBar, title, controls, projectListView);

        // Create and set the main scene
        Scene scene = new Scene(root, 800, 480);
        scene.getStylesheets().add(getClass().getResource("/knitplanner/style.css").toExternalForm());

        // Set up and show the primary stage (main application window)
        primaryStage.setScene(scene);
        primaryStage.setTitle("Knitting Planner");
        primaryStage.show();
    }

    // Utility method to show informational alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
