package knitplanner;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import models.KnittingProject;

/**
 * Class responsible for displaying a dialog to add or edit a KnittingProject.
 * Uses JavaFX Dialog to collect project details from the user.
 */
public class ProjectEditor {

    /**
     * Displays a dialog for adding a new project or editing an existing one.
     * @param projectToEdit existing project to edit, or null to add a new project
     * @return a KnittingProject with the entered details if OK pressed, otherwise null
     */
    public static KnittingProject showDialog(KnittingProject projectToEdit) {
        Dialog<KnittingProject> dialog = new Dialog<>();
        dialog.setTitle(projectToEdit == null ? "Add Project" : "Edit Project");
        dialog.initModality(Modality.APPLICATION_MODAL);  // Block interaction with other windows

        // Create UI controls for user input
        TextField nameField = new TextField();
        nameField.setPromptText("Enter project name");

        ComboBox<String> skillCombo = new ComboBox<>();
        skillCombo.getItems().addAll("Beginner", "Intermediate", "Advanced");
        skillCombo.setPromptText("Select skill level");

        ComboBox<String> yarnCombo = new ComboBox<>();
        yarnCombo.getItems().addAll("Wool", "Cotton", "Acrylic");
        yarnCombo.setPromptText("Select yarn type");

        ComboBox<String> aestheticCombo = new ComboBox<>();
        aestheticCombo.getItems().addAll("Modern", "Classic", "Boho");
        aestheticCombo.setPromptText("Select aesthetic");

        ComboBox<String> projectTypeCombo = new ComboBox<>();
        projectTypeCombo.getItems().addAll("Knitting", "Crochet");
        projectTypeCombo.setPromptText("Select project type");

        ComboBox<String> weatherCombo = new ComboBox<>();
        weatherCombo.getItems().addAll("Cold", "Warm", "All");
        weatherCombo.setPromptText("Select suitable weather");

        // If editing an existing project, prefill the fields with its values
        if (projectToEdit != null) {
            nameField.setText(projectToEdit.getName());
            skillCombo.setValue(projectToEdit.getSkillLevel());
            yarnCombo.setValue(projectToEdit.getYarnType());
            aestheticCombo.setValue(projectToEdit.getAesthetic());
            projectTypeCombo.setValue(projectToEdit.getProjectType());
            weatherCombo.setValue(projectToEdit.getSuitableWeather());
        }

        // Layout controls in a grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Skill Level:"), 0, 1);
        grid.add(skillCombo, 1, 1);

        grid.add(new Label("Yarn Type:"), 0, 2);
        grid.add(yarnCombo, 1, 2);

        grid.add(new Label("Aesthetic:"), 0, 3);
        grid.add(aestheticCombo, 1, 3);

        grid.add(new Label("Project Type:"), 0, 4);
        grid.add(projectTypeCombo, 1, 4);

        grid.add(new Label("Weather:"), 0, 5);
        grid.add(weatherCombo, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Add OK and Cancel buttons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Disable OK button initially to enforce validation
        Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        // Validation logic to enable OK only when all required fields have values
        Runnable validate = () -> {
            boolean valid = !nameField.getText().trim().isEmpty()
                && skillCombo.getValue() != null
                && yarnCombo.getValue() != null
                && aestheticCombo.getValue() != null
                && projectTypeCombo.getValue() != null
                && weatherCombo.getValue() != null;
            okButton.setDisable(!valid);
        };

        // Attach listeners to all inputs to validate on every change
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validate.run());
        skillCombo.valueProperty().addListener((obs, oldVal, newVal) -> validate.run());
        yarnCombo.valueProperty().addListener((obs, oldVal, newVal) -> validate.run());
        aestheticCombo.valueProperty().addListener((obs, oldVal, newVal) -> validate.run());
        projectTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> validate.run());
        weatherCombo.valueProperty().addListener((obs, oldVal, newVal) -> validate.run());

        // Run validation initially
        validate.run();

        // Auto focus the name field when dialog shows
        dialog.setOnShown(e -> nameField.requestFocus());

        // Convert dialog result to KnittingProject object if OK clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new KnittingProject(
                    nameField.getText().trim(),
                    skillCombo.getValue(),
                    yarnCombo.getValue(),
                    aestheticCombo.getValue(),
                    projectTypeCombo.getValue(),
                    weatherCombo.getValue()
                );
            }
            return null;  // Return null if cancelled
        });

        // Show the dialog and return the result (or null)
        return dialog.showAndWait().orElse(null);
    }
}
