package app;

import infra.SchemaInitializer;
import infra.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Telehealth System (THS) main entry point.
 *
 * Responsibilities:
 *  1. Ensures the MySQL schema/tables exist at startup.
 *  2. Optionally starts the multithreaded THS server (for concurrent processing demo).
 *  3. Loads the initial JavaFX scene (Login screen).
 *
 * VM options required:
 *  --module-path "C:\\javafx-sdk-24.0.2\\lib" --add-modules javafx.controls,javafx.fxml
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // --- STEP 1: Ensure database schema/tables exist ---
            SchemaInitializer.ensureTables();

            // --- STEP 2 (Optional): Start multi-threaded server in background ---
            // Comment out if not required for your assignment demo
            Server server = new Server(9090);
            server.startAsync();
            System.out.println("THS Server started on port 9090...");

            // --- STEP 3: Load Login Screen ---
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Telehealth System - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Startup Error");
            alert.setHeaderText("Application failed to start");
            alert.setContentText(
                "Cause: " + e.getMessage() + "\n\n" +
                "Check your MySQL connection or FXML resource paths."
            );
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
