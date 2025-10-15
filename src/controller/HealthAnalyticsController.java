package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.DataStore;
import model.Patient;
import javafx.scene.input.MouseEvent;
import java.io.IOException; 

public class HealthAnalyticsController {

    @FXML
    private Button generateReportButton;

    @FXML
    private TextArea reportTextArea;

    /**
     * Handles the action when the "Generate Report" button is clicked.
     */
    @FXML
    private void handleGenerateReport(ActionEvent event) {
        if (!(DataStore.currentUser instanceof Patient)) {
 
        }

        Patient patient = (Patient) DataStore.currentUser;
        String report = patient.generateHealthReport();
        reportTextArea.setText(report);
    }

    /**
     * Displays an alert dialog to the user.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void openBookingPanel(ActionEvent event) {
        navigateTo("/view/booking.fxml", "Book Appointment", event);
    }

    @FXML
    public void openBookingPanelLabel(MouseEvent event) {
        navigateTo("/view/booking.fxml", "Book Appointment", event);
    }

    @FXML
    public void openDashboard(MouseEvent event) {
         navigateTo("/view/patient_dashboard.fxml", "Patient Dashboard", event);
    }

    @FXML
    public void openVitalsPanelLabel(MouseEvent event) {
         navigateTo("/view/vitals.fxml", "Record Vitals", event);
    }

    @FXML
    public void openPrescriptionsPanelLabel(MouseEvent event) {
         navigateTo("/view/prescriptions.fxml", "My Prescriptions", event);
    }

    /**
     * Navigates to the Health Analytics panel itself.
     */
    @FXML
    public void openHealthAnalyticsPanelLabel(MouseEvent event) {
        
         navigateTo("/view/health_analytics.fxml", "Health Analytics", event);
    }

    /**
     * Generic navigation helper method.
     */
    private void navigateTo(String fxmlPath, String title, javafx.event.Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the " + title + " view.");
        }
    }
}