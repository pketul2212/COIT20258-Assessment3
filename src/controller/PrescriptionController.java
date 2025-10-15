package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.DataStore;
import model.Patient;
import model.Prescription;
import javafx.scene.input.MouseEvent; 

import java.io.IOException; 

public class PrescriptionController {

    @FXML
    private ListView<Prescription> prescriptionListView;

    @FXML
    private Button requestRefillButton;

    /**
     * Initializes the controller. Called after FXML elements are injected.
     * Populates the ListView with the current patient's prescriptions.
     */
    @FXML
    public void initialize() {
        if (!(DataStore.currentUser instanceof Patient)) {
            showAlert(Alert.AlertType.ERROR, "Access Error", "Only patients can view prescriptions.");
            return;
        }

        Patient patient = (Patient) DataStore.currentUser;
        ObservableList<Prescription> patientPrescriptions = FXCollections.observableArrayList(patient.getPrescriptions());
        prescriptionListView.setItems(patientPrescriptions);

        requestRefillButton.setDisable(true);

        prescriptionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            requestRefillButton.setDisable(newValue == null);
        });
    }

    /**
     * Handles the action when the "Request Refill" button is clicked.
     */
    @FXML
    private void handleRequestRefill(ActionEvent event) {
        Prescription selectedPrescription = prescriptionListView.getSelectionModel().getSelectedItem();

        if (selectedPrescription == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a prescription to refill.");
            return;
        }

        if (selectedPrescription.getStatus() != model.PrescriptionStatus.ACTIVE) {
            showAlert(Alert.AlertType.WARNING, "Invalid Request", "Refill can only be requested for Active prescriptions.");
            return;
        }

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

    /**
     * Navigates to the Booking panel (FXML).
     */
    @FXML
    private void openBookingPanel(ActionEvent event) {
        navigateTo("/view/booking.fxml", "Book Appointment", event);
    }

    /**
     * Navigates to the Booking panel via Label click (MouseEvent).
     */
    @FXML
    public void openBookingPanelLabel(MouseEvent event) {
        navigateTo("/view/booking.fxml", "Book Appointment", event);
    }

    /**
     * Navigates to the Patient Dashboard (FXML).
     */
    @FXML
    public void openDashboard(MouseEvent event) {
        navigateTo("/view/patient_dashboard.fxml", "Patient Dashboard", event);
    }

    /**
     * Navigates to the Vitals panel via Label click (MouseEvent).
     */
    @FXML
    public void openVitalsPanelLabel(MouseEvent event) {
        navigateTo("/view/vitals.fxml", "Record Vitals", event);
    }

    @FXML
    public void openPrescriptionsPanelLabel(MouseEvent event) {

        navigateTo("/view/prescriptions.fxml", "My Prescriptions", event);
    }

    @FXML
    public void openHealthAnalyticsPanelLabel(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/health_analytics.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Health Analytics");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
