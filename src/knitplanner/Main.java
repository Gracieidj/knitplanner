package knitplanner;

import javafx.application.Application;

/**
 * Main class to launch the JavaFX application.
 * This class contains the main() method which serves as the entry point of the program.
 */
public class Main {

    /**
     * Main method that launches the KnittingPlannerUI JavaFX application.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Launch the JavaFX application by invoking the start() method in KnittingPlannerUI
        Application.launch(KnittingPlannerUI.class, args);
    }
}
